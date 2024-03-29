/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.util.EnvironmentUtil;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.ThreadSafe;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Utility class for performing network operations.
 * <p>
 * Via the {@link #send(Context, AbstractRequest)} method, this class takes in
 * {@link AbstractRequest} objects, sends them over the network, and returns
 * {@link StreamingResponse} objects.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.INTERNAL)
public final class NetworkConnection {

    /**
     * The maximum number of simultaneous connections on each server.
     * <p>
     * The connection pool's implementation relies on reading the {@code http.maxConnections} system
     * property during static initialization. The best we can be do is assume that the property's
     * current value was also observed by the connection pool. When the property is not set, a
     * default value of 5 is used on all supported devices up to and including
     * {@link android.os.Build.VERSION_CODES#KITKAT}. The default value is an internal
     * implementation detail that may change when newer versions of Android become available.
     * </p>
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final int MAX_POOLED_CONNECTIONS =
            Integer.valueOf(System.getProperty("http.maxConnections", "5"));

    /*
     * The static fields stored by this class are not necessarily thread-safe. They operate on a
     * best-effort basis to improve testing.
     */

    @Nullable
    private static volatile StreamingResponse sNextResponse = null;

    /**
     * Send the {@link AbstractRequest} using HTTP and create a {@link AbstractResponse}.
     *
     * @param context Application Context.
     * @param request the request to send.
     * @return the {@link AbstractResponse} received.
     */
    @NonNull
    public static StreamingResponse send(@NonNull final Context context,
            @NonNull final AbstractRequest request) {
        StreamingResponse response;

        try {
            response = doSendWithRetry(context, request);
        } catch (final IOException e) {
            LogManager.v("Error during send", e);
            response = new StreamingResponse(e);
        } catch (final BadRequestException e) {
            LogManager.v("Error during send", e);
            response = new StreamingResponse(e);
        }

        return response;
    }

    /**
     * Performs the send to the server.
     * <p>
     * The implementation of this method includes a workaround for {@link EOFException}s caused by
     * the reuse of stale connections. Send may be attempted multiple times in order to close these
     * connections. See <a href="http://stackoverflow.com/a/23795099/204480">Stack Overflow</a> for
     * more details.
     * </p>
     *
     * @param context Application Context.
     * @param request the request to send.
     * @return {@link StreamingResponse} containing information regarding the outcome of the send.
     * @throws IOException if network operations fail.
     * @throws BadRequestException if the request is invalid.
     */
    @NonNull
    private static StreamingResponse doSendWithRetry(@NonNull final Context context,
            @NonNull final AbstractRequest request) throws IOException, BadRequestException {
        LogManager.v("HTTP request headers: %s", request.getRequestHeaders(context));

        final boolean isPooled = MAX_POOLED_CONNECTIONS > 0;

        for (int i = 0; i < MAX_POOLED_CONNECTIONS + 1; i++) {
            // Close the connection if this is a retry and the connections are pooled.
            final boolean shouldCloseConnection = i > 0 && isPooled;

            try {
                return doSend(context, request, shouldCloseConnection);
            } catch (final EOFException e) {
                LogManager.e(NullUtils.format("Unable to send request: failures(%d)", i), e);
            }
        }

        return doSend(context, request, false);
    }

    /**
     * Performs the send to the server.
     *
     * @param context Application Context.
     * @param request the request to send.
     * @param shouldCloseConnection determines whether the connection should be closed after the
     * request has been made.
     * @return {@link StreamingResponse} containing information regarding the outcome of the send.
     * @throws IOException if network operations fail.
     * @throws BadRequestException if the request is invalid.
     */
    @NonNull
    private static StreamingResponse doSend(@NonNull final Context context,
            @NonNull final AbstractRequest request, final boolean shouldCloseConnection)
                    throws IOException, BadRequestException {
        HttpURLConnection connection = null;
        StreamingResponse response = null;

        try {
            // Configure the connection based on the request passed
            connection = configureConnection(context, request);

            if (shouldCloseConnection) {
                connection.setRequestProperty("Connection", "close");
            }

            // Write the post body if necessary
            doOutput(context, connection, request);

            // Get the response from the server
            response = getResponse(connection);
        } finally {
            if (response == null) {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        return response;
    }

    /**
     * Configures the {@link HttpURLConnection} to use for this request.
     *
     * @param context Application Context.
     * @param request the request to use to configure the connection
     * @return the configured connection
     * @throws IOException if configuration fails
     * @throws BadRequestException if the request is invalid.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static HttpURLConnection configureConnection(@NonNull final Context context,
            @NonNull final AbstractRequest request) throws IOException, BadRequestException {
        final HttpURLConnection connection =
                (HttpURLConnection) request.getUrl(context).openConnection();
        // Set the HTTP method (GET, POST, PUT, etc..)
        connection.setRequestMethod(request.getMethod().name());

        // Append the HTTP headers to the request
        final Map<String, String> headers = request.getRequestHeaders(context);
        for (final String headerKey : headers.keySet()) {
            connection.setRequestProperty(headerKey, headers.get(headerKey));
        }

        final int bodyLength = request.getBodyLength(context);

        if (0 != bodyLength) {
            /*
             * Due to an issue with internal buffering on Android 2.2, streaming can cause future
             * connections to hang if an IOException is thrown while writing to a connection's
             * output stream. Both fixed-length and chunked streaming modes are affected. This is
             * fixed in Android 2.3+ with the changes made for
             * https://code.google.com/p/android/issues/detail?id=3164.
             */
            if (EnvironmentUtil.isSdk9OrGreater()) {
                connection.setFixedLengthStreamingMode(bodyLength);
            }

            connection.setDoOutput(true);
        }

        return connection;
    }

    /**
     * Write to the post output stream if necessary.
     *
     * @param context Application Context.
     * @param connection the connection to write to
     * @param request the request containing the post body
     * @throws IOException if writing of the post body fails.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static void doOutput(@NonNull final Context context,
            @NonNull final HttpURLConnection connection, @NonNull final AbstractRequest request)
            throws IOException {
        if (connection.getDoOutput()) {
            OutputStream stream = null;

            try {
                stream = NullUtils.nonNullContract(connection.getOutputStream());

                try {
                    request.writeBodyToStream(context, stream);
                } catch (final IOException e) {
                    // Close the stream quietly.
                    try {
                        stream.close();
                    } catch (final IOException f) {
                        // OutputStream expected more output.
                    }

                    stream = null;
                    throw e;
                }
            } finally {
                if (null != stream) {
                    stream.close();
                }
            }
        }
    }

    /**
     * Gets the response from the server.
     *
     * @param connection the connection to use to make the request to the server
     * @return {@link StreamingResponse} containing information regarding the outcome of the send
     * @throws IOException if network operations fail
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static StreamingResponse getResponse(@NonNull final HttpURLConnection connection)
            throws IOException {
        final StreamingResponse nextResponse = sNextResponse;
        final StreamingResponse response;

        if (null == nextResponse) {
            // Create the response object to pass back to the caller
            response = new StreamingResponse(connection);
        } else {
            // If the sNextResponse field was set return it instead of doing the network request
            response = nextResponse;
            sNextResponse = null;
        }

        return response;
    }

    /**
     * Method to use for testing to set the next response to return regardless of the request.
     *
     * @param nextResponse the response to return for the next network request.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static void setNextResponse(@Nullable final StreamingResponse nextResponse) {
        sNextResponse = nextResponse;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private NetworkConnection() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
