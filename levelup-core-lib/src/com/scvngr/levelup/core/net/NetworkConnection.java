/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.util.LogManager;

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

    /*
     * The static fields stored by this class are not necessarily thread-safe. They operate on a
     * best-effort basis to improve testing.
     */

    @Nullable
    private static volatile AbstractRequest sLastRequest = null;

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
        StreamingResponse response = null;

        try {
            response = doSend(context, request);
        } catch (final IOException e) {
            LogManager.v("Error during send", e); //$NON-NLS-1$
            response = new StreamingResponse(e);
        } catch (final BadRequestException e) {
            LogManager.v("Error during send", e); //$NON-NLS-1$
            response = new StreamingResponse(e);
        }

        return response;
    }

    /**
     * Performs the send to the server.
     *
     * @param context Application Context.
     * @param request the request to send.
     * @return {@link StreamingResponse} containing information regarding the outcome of the send.
     * @throws IOException if network operations fail.
     */
    @NonNull
    private static StreamingResponse doSend(@NonNull final Context context,
            @NonNull final AbstractRequest request) throws IOException, BadRequestException {
        // Store the last request for testing purposes.
        sLastRequest = request;

        // Configure the connection based on the request passed
        final HttpURLConnection connection = configureConnection(context, request);

        LogManager.v("HTTP request headers: %s", request.getRequestHeaders(context)); //$NON-NLS-1$

        // Write the post body if necessary
        doOutput(context, connection, request);

        // Get the response from the server
        return getResponse(connection, request);
    }

    /**
     * Configures the {@link HttpURLConnection} to use for this request.
     *
     * @param context Application Context.
     * @param request the request to use to configure the connection
     * @return the configured connection
     * @throws IOException if configuration fails
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
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

        if (0 != request.getBodyLength(context)) {
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

        if (0 != request.getBodyLength(context)) {
            connection.setFixedLengthStreamingMode(request.getBodyLength(context));
            OutputStream stream = null;

            try {
                stream = connection.getOutputStream();
                request.writeBodyToStream(context, stream);
                stream.close();
            } finally {
                if (null != stream) {
                    stream.close();
                    stream = null;
                }
            }
        }
    }

    /**
     * Gets the response from the server.
     *
     * @param connection the connection to use to make the request to the server
     * @param request the request to send to the server
     * @return {@link StreamingResponse} containing information regarding the outcome of the send
     * @throws IOException if network operations fail
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static StreamingResponse getResponse(final HttpURLConnection connection,
            final AbstractRequest request) throws IOException {
        StreamingResponse response = null;

        if (null == sNextResponse) {
            // Create the response object to pass back to the caller
            response = new StreamingResponse(connection);
        } else {
            // If the sNextResponse field is set, return that response instead of doing the
            // network request.
            response = sNextResponse;
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
     * Method to use for testing to get the last request.
     *
     * @return {@link AbstractRequest} the last request from any client.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @Nullable
    /* package */static AbstractRequest getLastRequest() {
        return sLastRequest;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private NetworkConnection() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
