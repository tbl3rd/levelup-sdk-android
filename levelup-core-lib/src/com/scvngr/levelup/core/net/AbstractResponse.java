/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;

/**
 * Object which represents an HTTP response.
 *
 * @param <T> the response data type.
 */
@NotThreadSafe
@LevelUpApi(contract = Contract.DRAFT)
public abstract class AbstractResponse<T> {

    /**
     * Sentinel status code when no HTTP status code was received.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final int HTTP_STATUS_CODE_UNUSED = -1;

    /**
     * The minimum HTTP response code from the server considered to be OK.
     */
    /* package */static final int STATUS_CODE_SUCCESS_MIN_INCLUSIVE = HttpURLConnection.HTTP_OK;

    /**
     * The maximum HTTP response code from the server considered to be OK.
     */
    /* package */static final int STATUS_CODE_SUCCESS_MAX_EXCLUSIVE =
            HttpURLConnection.HTTP_MULT_CHOICE;

    /**
     * the HTTP status code of the response.
     */
    private final int mHttpStatusCode;

    /**
     * any exception that was thrown during the request or null if there was none.
     */
    @Nullable
    private final Exception mError;

    /**
     * The HTTP headers.
     */
    @Nullable
    private final Map<String, List<String>> mHttpHeaders;

    /**
     * Constructor for an error response.
     *
     * @param error any exception that was thrown during the request.
     */
    protected AbstractResponse(@NonNull final Exception error) {
        this(HTTP_STATUS_CODE_UNUSED, error);
    }

    /**
     * Default constructor for testing.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */AbstractResponse() {
        this(HTTP_STATUS_CODE_UNUSED, null);
    }

    /**
     * @param statusCode the HTTP status code.
     * @param error the {@link Exception} that was thrown during the request or {@code null} if
     *        there was no error.
     */
    protected AbstractResponse(final int statusCode, @Nullable final Exception error) {
        mHttpStatusCode = statusCode;
        mError = error;
        mHttpHeaders = null;
    }

    /**
     * @param statusCode the HTTP status code of the response.
     * @param httpHeaders the HTTP headers included in the response. This must be an unmodifiable
     *        map, for example one wrapped using {@link Collections#unmodifiableMap(Map)}.
     * @param error the {@link Exception} that was thrown during the request or {@code null} if
     *        there was no error.
     */
    protected AbstractResponse(final int statusCode,
            @Nullable final Map<String, List<String>> httpHeaders, @Nullable final Exception error) {
        mHttpStatusCode = statusCode;
        mError = error;
        mHttpHeaders = httpHeaders;
    }

    /**
     * @return the data sent from the server in this response.
     */
    @Nullable
    public abstract T getData();

    /**
     * @return HTTP status code passed back from the server (if any).
     */
    public final int getHttpStatusCode() {
        return mHttpStatusCode;
    }

    /**
     * Retrieves the desired HTTP header. If multiple headers of the same key are present, this
     * retrieves the first one.
     *
     * @param headerField the HTTP header field (the key). This value must be be normalized to
     *        First-Letter-Caps.
     * @return the value of the header or {@code null} if no header is present.
     */
    @Nullable
    public final String getHttpHeader(@NonNull final String headerField) {
        String result = null;

        if (null != mHttpHeaders) {
            final List<String> values = mHttpHeaders.get(headerField);
            if (null != values && 0 != values.size()) {
                result = values.get(0);
            }
        }

        return result;
    }

    /**
     * @return the HTTP headers.
     */
    protected Map<String, List<String>> getHttpHeaders() {
        return mHttpHeaders;
    }

    /**
     * @return the {@link Exception} that was thrown during a request.
     */
    @Nullable
    public Exception getError() {
        return mError;
    }

    @Override
    public String toString() {
        return String.format("AbstractResponse [mHttpStatusCode=%s, mError=%s]", mHttpStatusCode, //$NON-NLS-1$
                mError);
    }
}
