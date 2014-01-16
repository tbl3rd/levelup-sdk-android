/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * Helper to increase visibility to {@link LevelUpConnection} test-only methods.
 */
public final class LevelUpConnectionHelper {

    /**
     * Creates a new {@link LevelUpResponse} with the the data and the {@link LevelUpStatus}
     * passed and sets it as the next response that
     * {@link LevelUpConnection#send(AbstractRequest)} will return.
     *
     * @param context Application context.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     *
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection setNextResponse(@NonNull final Context context,
            @NonNull final String data, @NonNull final LevelUpStatus status) {
        final LevelUpConnection connection = getTestLevelUpConnection(context);
        connection.setNextResponse(null, new LevelUpResponse(data, status));

        return connection;
    }

    /**
     * Creates a new {@link LevelUpResponse} with the the data and the {@link LevelUpStatus}
     * passed and sets it as the next response that
     * {@link LevelUpConnection#send(AbstractRequest)} will return.
     *
     * @param context Application context.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     *
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection
            setNextResponse(@NonNull final Context context, @Nullable final String url,
                    @NonNull final String data, @NonNull final LevelUpStatus status) {
        final LevelUpConnection connection = getTestLevelUpConnection(context);
        connection.setNextResponse(url, new LevelUpResponse(data, status));

        return connection;
    }

    /**
     * Adds a new {@link LevelUpResponse} with the the data and the {@link LevelUpStatus}
     * passed and sets it as the next response that
     * {@link LevelUpConnection#send(AbstractRequest)} will for the url string passed.
     *
     * @param connection the {@link LevelUpConnection} to add the response to.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     *
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection addResponse(
            @NonNull final LevelUpConnection connection, @Nullable final String url,
            @NonNull final String data, @NonNull final LevelUpStatus status) {
        connection.setNextResponse(url, new LevelUpResponse(data, status));
        return connection;
    }

    /**
     * @param requestUrl the url of the request to get.
     * @param connection An the instance of {@link LevelUpConnection} returned by
     * {@link #setNextResponse}.
     * @return the last request made.
     */
    @Nullable
    public static AbstractRequest getLastRequest(@NonNull final String requestUrl,
            @NonNull final LevelUpConnection connection) {
        return connection.getLastRequest(requestUrl);
    }

    /**
     * Set the next {@link LevelUpConnection} instance that will be used by clients and
     * return it so it can be modified.
     *
     * @param context Application context
     * @return the instance of {@link LevelUpConnection} that was set as the next instance
     */
    @NonNull
    private static LevelUpConnection getTestLevelUpConnection(
            @NonNull final Context context) {
        final LevelUpConnection connection = new LevelUpConnection(context);
        LevelUpConnection.setNextInstance(connection);
        return connection;
    }

    /**
     * Creates a new {@link LevelUpResponse} with the the data and the {@link LevelUpStatus}
     * passed and sets it as the next response that {@link LevelUpConnection#send} will return. The
     * newly created connection is set as the next {@link LevelUpConnection} instance that will be
     * used by clients.
     *
     * @param context Application context.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     * @return the {@link LevelUpConnection} with the response set.
     */
    @NonNull
    public static LevelUpConnection setNextGlobalResponse(@NonNull final Context context,
            @NonNull final String data, @NonNull final LevelUpStatus status) {
        final LevelUpConnection connection = setNextResponse(context, data, status);
        LevelUpConnection.setNextInstance(connection);
        return connection;
    }

    /**
     * Sets if network connections are enabled for the {@link LevelUpConnection} instance
     * passed.
     *
     * @param enabled if true, network is enabled. If false, network connections that do not have
     *        pre-created responses will throw a {@link RuntimeException}.
     */
    public static void setNetworkEnabled(final boolean enabled) {
        LevelUpConnection.setNetworkEnabled(enabled);
    }
}
