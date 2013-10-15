package com.scvngr.levelup.core.net;

import android.content.Context;
import android.os.SystemClock;
import android.text.format.DateUtils;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Helper to increase visibility to {@link LevelUpConnection} test-only methods.
 */
public final class LevelUpConnectionHelper {

    /**
     * The amount of milliseconds to sleep between loop iterations in
     * {@link #waitForRequest(LevelUpConnection, long)}.
     */
    private static final int WAIT_SLEEP_MILLIS = 20;

    /**
     * The default time to wait for a request.
     */
    public static final long DEFAULT_WAIT_MILLIS = 4 * DateUtils.SECOND_IN_MILLIS;

    /**
     * Creates a new {@link LevelUpResponse} with the data and the {@link LevelUpStatus} passed and
     * sets it as the next response that {@link LevelUpConnection#send(AbstractRequest)} will
     * return.
     *
     * @param context Application context.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
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
     * Creates a new {@link LevelUpResponse} with the data and the {@link LevelUpStatus} passed and
     * sets it as the next response that {@link LevelUpConnection#send(AbstractRequest)} will
     * return.
     *
     * @param context Application context.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     * @param headers optional HTTP headers.
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection setNextResponse(@NonNull final Context context,
            @NonNull final String data, @NonNull final LevelUpStatus status,
            @Nullable final Map<String, List<String>> headers) {
        final LevelUpConnection connection = getTestLevelUpConnection(context);
        connection.setNextResponse(null, new LevelUpResponse(data, status, headers, null));

        return connection;
    }

    /**
     * Creates a new {@link LevelUpResponse} with the data and the {@link LevelUpStatus} passed and
     * sets it as the next response that {@link LevelUpConnection#send(AbstractRequest)} will
     * return.
     *
     * @param context Application context.
     * @param requestUrl the URL that the response should be for.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     * @param headers optional HTTP headers.
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection setNextResponse(@NonNull final Context context,
            @NonNull final String requestUrl, @NonNull final String data,
            @NonNull final LevelUpStatus status, @Nullable final Map<String, List<String>> headers) {
        final LevelUpConnection connection = getTestLevelUpConnection(context);
        connection.setNextResponse(requestUrl, new LevelUpResponse(data, status, headers, null));

        return connection;
    }

    /**
     * Creates a new {@link LevelUpResponse} with the data and the {@link LevelUpStatus} passed and
     * sets it as the next response that {@link LevelUpConnection#send(AbstractRequest)} will
     * return.
     *
     * @param context Application context.
     * @param data the data that the request should return.
     * @param statusCode the HTTP status code to return in the response.
     * @param headers optional HTTP headers.
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection setNextResponse(@NonNull final Context context,
            @NonNull final String data, final int statusCode,
            @Nullable final Map<String, List<String>> headers) {
        final LevelUpConnection connection = getTestLevelUpConnection(context);
        connection.setNextResponse(null, new LevelUpResponse(data, statusCode, headers, null));

        return connection;
    }

    /**
     * Creates a new {@link LevelUpResponse} with the data and the {@link LevelUpStatus} passed and
     * sets it as the next response that {@link LevelUpConnection#send(AbstractRequest)} will return
     * for the URL passed. If URL is null, the response will be returned regardless of the requested
     * URL.
     *
     * @param context Application context.
     * @param url the URL that this response should be returned for (null sets the response for any
     *        request).
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection setNextResponse(@NonNull final Context context,
            @Nullable final String url, @NonNull final String data,
            @NonNull final LevelUpStatus status) {
        final LevelUpConnection connection = getTestLevelUpConnection(context);
        connection.setNextResponse(url, new LevelUpResponse(data, status));

        return connection;
    }

    /**
     * Adds a new {@link LevelUpResponse} with the data and the {@link LevelUpStatus} passed and
     * sets it as the next response that {@link LevelUpConnection#send(AbstractRequest)} will for
     * the URL string passed.
     *
     * @param connection the {@link LevelUpConnection} to add the response to.
     * @param url the full URL to use.
     * @param data the data that the request should return.
     * @param status the {@link LevelUpStatus} to return in the response.
     * @return the {@link LevelUpConnection} that has the response set on it.
     */
    @NonNull
    public static LevelUpConnection addResponse(@NonNull final LevelUpConnection connection,
            @Nullable final String url, @NonNull final String data,
            @NonNull final LevelUpStatus status) {
        connection.setNextResponse(url, new LevelUpResponse(data, status));
        return connection;
    }

    /**
     * @param requestUrl the URL of the expected request.
     * @param connection An instance of {@link LevelUpConnection} returned by
     *        {@link #setNextResponse(Context, String, LevelUpStatus)}.
     * @return the last request made for the URL passed.
     */
    @Nullable
    public static AbstractRequest getLastRequest(@NonNull final String requestUrl,
            @NonNull final LevelUpConnection connection) {
        return connection.getLastRequest(requestUrl);
    }

    /**
     * @param connection An instance of {@link LevelUpConnection} returned by
     *        {@link #setNextResponse(Context, String, LevelUpStatus)}.
     * @return the last request made for the connection passed.
     */
    @Nullable
    public static AbstractRequest getLastRequest(@NonNull final LevelUpConnection connection) {
        return connection.getLastRequest(null);
    }

    /**
     * Clears the last request for the URL passed.
     *
     * @param connection An instance of {@link LevelUpConnection} returned by
     *        {@link #setNextResponse(Context, String, LevelUpStatus)}.
     * @param url the URL of the request to remove from the map.
     */
    public static void clearLastRequest(@NonNull final LevelUpConnection connection,
            @NonNull final String url) {
        connection.setLastRequest(url, null);
    }

    /**
     * Set the next {@link LevelUpConnection} instance that will be used by clients and return it so
     * it can be modified.
     *
     * @param context Application context
     * @return the instance of {@link LevelUpConnection} that was set as the next instance
     */
    @NonNull
    private static LevelUpConnection getTestLevelUpConnection(@NonNull final Context context) {
        final LevelUpConnection connection = new LevelUpConnection(context);
        LevelUpConnection.setNextInstance(connection);
        return connection;
    }

    /**
     * Sets if network connections are enabled for the {@link LevelUpConnection} instance passed.
     *
     * @param enabled if true, network is enabled. If false, network connections that do not have
     *        pre-created responses will throw a {@link RuntimeException}.
     */
    public static void setNetworkEnabled(final boolean enabled) {
        LevelUpConnection.setNetworkEnabled(enabled);
    }

    /**
     * Waits for a request to be enqueued to the {@link LevelUpConnection} passed.
     *
     * @param requestUrl the URL of the request to wait for or null to wait for any request.
     * @param connection the {@link LevelUpConnection} instance to monitor for requests.
     * @param waitMillis the time in milliseconds to wait for the request.
     * @return true if a request was detected, false of timeout occurred.
     */
    public static boolean waitForRequest(@Nullable final String requestUrl,
            @NonNull final LevelUpConnection connection, final long waitMillis) {
        final long endTime = SystemClock.elapsedRealtime() + (waitMillis);
        boolean result = true;

        while (true) {
            if (null != connection.getLastRequest(requestUrl)) {
                break;
            }

            if (SystemClock.elapsedRealtime() >= endTime) {
                result = false;
                break;
            }

            SystemClock.sleep(WAIT_SLEEP_MILLIS);
        }

        return result;
    }

    /**
     * Waits for a request to be enqueued to the {@link LevelUpConnection} passed. Waits
     * {@link #DEFAULT_WAIT_MILLIS} for the request, if you need a different timeout length, use
     * {@link #waitForRequest(String, LevelUpConnection, long)}. Waits for a request for the URL
     * specified.
     *
     * @param requestUrl the URL of the request to wait for or null to wait for any request.
     * @param connection the {@link LevelUpConnection} instance to monitor for requests.
     * @return true if a request was detected, false of timeout occurred.
     */
    public static boolean waitForRequest(@Nullable final String requestUrl,
            @NonNull final LevelUpConnection connection) {
        return waitForRequest(requestUrl, connection, DEFAULT_WAIT_MILLIS);
    }

    /**
     * Clear the cached instance of {@link LevelUpConnection}.
     */
    public static void clearInstance() {
        LevelUpConnection.setNextInstance(null);
    }

    private LevelUpConnectionHelper() {
        throw new UnsupportedOperationException("This class is non-instantiable."); //$NON-NLS-1$
    }
}
