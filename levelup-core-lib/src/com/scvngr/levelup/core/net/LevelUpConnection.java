package com.scvngr.levelup.core.net;

import android.content.Context;

import java.util.HashMap;
import java.util.Locale;

import net.jcip.annotations.GuardedBy;
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
 * Class for interacting with the LevelUp web service API.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.DRAFT)
public final class LevelUpConnection {
    /**
     * Application context.
     */
    @NonNull
    private final Context mContext;

    /**
     * For Testing: HashMap of the last request that was made with the key of the URL for the
     * request.
     */
    @GuardedBy("mRequestIntrinsicLock")
    @Nullable
    private final HashMap<String, AbstractRequest> mLastRequestMap =
            new HashMap<String, AbstractRequest>();

    /**
     * For Testing: HashMap of pre-made responses to use when the String key URL is requested.
     */
    @GuardedBy("mResponseIntrinsicLock")
    @NonNull
    private final HashMap<String, LevelUpResponse> mNextResponseMap =
            new HashMap<String, LevelUpResponse>();

    /**
     * Intrinsic lock for guarding {@link #mNextResponseMap}.
     */
    @NonNull
    private final Object[] mResponseIntrinsicLock = new Object[0];

    /**
     * Intrinsic lock for guarding {@link #mLastRequestMap}.
     */
    @NonNull
    private final Object[] mRequestIntrinsicLock = new Object[0];

    /**
     * For Testing: If false, if any network activity happens, an exception will be thrown.
     */
    private static volatile boolean sIsNetworkEnabled = true;

    /**
     * For Testing: The next LevelUpConnection instance for {@link #newInstance(Context)}
     * to return.
     */
    @Nullable
    private static volatile LevelUpConnection sNextInstance;

    /**
     * Clients should use {@link #newInstance(Context)}.
     *
     * @param context Application context.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpConnection(@NonNull final Context context) {
        if (null == context) {
            throw new IllegalArgumentException("context cannot be null"); //$NON-NLS-1$
        }

        mContext = context.getApplicationContext();
    }

    /**
     * Method to get a new instance of LevelUpConnection.
     *
     * @param context the context to use to get package info
     * @return an instance of {@link LevelUpConnection} to use to interact with the
     *         LevelUp API
     */
    @NonNull
    public static LevelUpConnection newInstance(@NonNull final Context context) {
        LevelUpConnection connection = null;

        if (null != sNextInstance) {
            // For Testing: if the client set a next instance, use that instead of new object
            connection = sNextInstance;
        } else {
            connection = new LevelUpConnection(context);
        }

        return connection;
    }

    /**
     * For Testing: set the next instance of LevelUpConnection that
     * {@link #newInstance(Context)} will return. This instance will be returned for ALL SUBSEQUENT
     * calls to {@link #newInstance(Context)} until it is cleared by calling
     * {@link #setNextInstance(LevelUpConnection)} with null.
     *
     * @param connection the next LevelUpConnection to return. Setting {@code null}
     *        ensures that {@link #newInstance(Context)} will return a new instance.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static void
            setNextInstance(@Nullable final LevelUpConnection connection) {
        sNextInstance = connection;
    }

    /**
     * @param url the URL of the request, if null will return any request in the map if there are
     *        any.
     * @throws AssertionError if a call is made with a null URL and there are multiple requests
     *         cached in the map.
     * @return the last request made for the URL passed.
     */
    @Nullable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */AbstractRequest getLastRequest(@Nullable final String url) {
        synchronized (mRequestIntrinsicLock) {
            LogManager.i("getLastRequest url=%s size=%d", url, mLastRequestMap.size()); //$NON-NLS-1$
            AbstractRequest request = null;
            String key = null;
            if (null != url) {
                key = url;
            } else {
                if (mLastRequestMap.size() > 1) {
                    throw new AssertionError(
                            "This method of getting last request is not supported if there are multiple requests being made"); //$NON-NLS-1$
                }

                if (mLastRequestMap.size() > 0) {
                    key = mLastRequestMap.keySet().iterator().next();
                }
            }

            request = mLastRequestMap.get(key);

            return request;
        }
    }

    /**
     * Testing Helper: set the last request for a URL.
     *
     * @param url the URL for this request
     * @param request the last request that was made.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */void setLastRequest(@Nullable final String url,
            @Nullable final AbstractRequest request) {
        synchronized (mRequestIntrinsicLock) {
            mLastRequestMap.put(url, request);
        }
    }

    /**
     * Testing Helper: set the next response to return for the URL passed.
     *
     * @param url the URL that this response should be returned for (null sets the response for any
     *        request).
     * @param nextResponse the next response to return (null clears the response).
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */void setNextResponse(@Nullable final String url,
            @Nullable final LevelUpResponse nextResponse) {
        synchronized (mResponseIntrinsicLock) {
            mNextResponseMap.put(url, nextResponse);
        }
    }

    /**
     * Testing Helper: get the next response to return for the URL passed.
     *
     * @param url the URL that this response should be returned for.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpResponse getNextResponse(@NonNull final String url) {
        LevelUpResponse response = null;

        synchronized (mResponseIntrinsicLock) {
            response = mNextResponseMap.get(url);

            if (null == response) {
                response = mNextResponseMap.get(null);
            }
        }

        return response;
    }

    /**
     * Testing Helper: set if un-caught (see {@link #setNextResponse}) network activity should be
     * enabled. If disabled, any network activity that does not have a pre-created response will
     * throw an exception.
     *
     * @param enabled true if network connections should be enabled.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static void setNetworkEnabled(final boolean enabled) {
        sIsNetworkEnabled = enabled;
    }

    /**
     * Performs the request. Will add the headers to the request and build the full URL.
     *
     * @param request the {@link AbstractRequest} to send to the server.
     * @return the parsed {@link LevelUpResponse} from the request.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    public LevelUpResponse send(@NonNull final AbstractRequest request) {
        LevelUpResponse response = null;

        String requestUrl = null;

        try {
            requestUrl = request.getUrlString(mContext);
        } catch (final BadRequestException e) {
            LogManager.e("BadRequestException", e); //$NON-NLS-1$
            // Don't need to do anything, since this URL is just for logging.
        }
        // Set the last request for testing
        setLastRequest(requestUrl, request);

        LogManager.v("Requesting URL: %s %s", request.getMethod(), requestUrl); //$NON-NLS-1$
        final LevelUpResponse nextResponse = getNextResponse(requestUrl);

        if (null != nextResponse) {
            LogManager.d("Returning canned response instead of performing network operation"); //$NON-NLS-1$
            // TESTING: If the client set a next response, use that instead.
            response = nextResponse;
            setNextResponse(requestUrl, null);
        } else {
            if (!sIsNetworkEnabled) {
                throw new RuntimeException(String.format(Locale.US,
                        "Network Activity detected when it was explicitly disabled: %s", requestUrl)); //$NON-NLS-1$
            }

            response = new LevelUpResponse(NetworkConnection.send(mContext, request));
        }

        return response;
    }
}
