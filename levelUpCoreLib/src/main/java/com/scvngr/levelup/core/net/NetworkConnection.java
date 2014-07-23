/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
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
            response = doSend(context, request, 0);
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
     * connections. See <a href=http://stackoverflow.com/a/23795099/204480>Stack Overflow</a> for
     * more details.
     * </p>
     *
     * @param context Application Context.
     * @param request the request to send.
     * @param failures the number of failed attempts to send this request.
     * @return {@link StreamingResponse} containing information regarding the outcome of the send.
     * @throws IOException if network operations fail.
     * @throws BadRequestException if the request is invalid.
     */
    @NonNull
    private static StreamingResponse doSend(@NonNull final Context context,
            @NonNull final AbstractRequest request, final int failures)
            throws IOException, BadRequestException {
        LogManager.v("HTTP request headers: %s", request.getRequestHeaders(context));

        HttpURLConnection connection = null;
        StreamingResponse response = null;

        try {
            // Configure the connection based on the request passed
            connection = configureConnection(context, request);

            // Close the connection if this is a retry and additional attempts may be necessary.
            if (0 < failures && shouldRetryAfterEOFException(failures)) {
                connection.setRequestProperty("Connection", "close");
            }

            // Write the post body if necessary
            doOutput(context, connection, request);

            // Get the response from the server
            response = getResponse(connection);
        } catch (final EOFException e) {
            LogManager.e(NullUtils.format("Unable to send request: failures(%d)",
                            failures), e);

            if (shouldRetryAfterEOFException(failures)) {
                disconnect(connection);
                connection = null;

                return doSend(context, request, failures + 1);
            }

            throw e;
        } finally {
            if (null == response) {
                disconnect(connection);
            }
        }

        return response;
    }

    /**
     * Calls {@link HttpURLConnection#disconnect} if {@code connection} is non-null.
     *
     * @param connection the connection to disconnect or null.
     */
    private static void disconnect(@Nullable final HttpURLConnection connection) {
        if (null != connection) {
            connection.disconnect();
        }
    }

    /**
     * @param failures the number of failed attempts to send this request.
     * @return true if the send should be attempted again if it fails with an {@link EOFException},
     * false otherwise.
     */
    private static boolean shouldRetryAfterEOFException(final int failures) {
        return 0 < MAX_POOLED_CONNECTIONS && MAX_POOLED_CONNECTIONS + 1 > failures;
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
            connection.setFixedLengthStreamingMode(bodyLength);
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
        StreamingResponse response;

        // Pull it out to a local variable for static analysis
        final StreamingResponse nextResponse = sNextResponse;

        if (null == nextResponse) {
            // Create the response object to pass back to the caller
            response = new StreamingResponse(connection);
        } else {
            // If the sNextResponse field is set, return that response instead of doing the
            // network request.
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
