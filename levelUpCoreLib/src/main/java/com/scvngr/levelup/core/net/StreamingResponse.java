/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.util.LogManager;

/**
 * An {@link AbstractResponse} that uses a streaming interface where the response data is read from
 * a stream.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public class StreamingResponse extends AbstractResponse<InputStream> {

    @Nullable
    private final InputStream mData;

    @Nullable
    private final HttpURLConnection mConnection;

    /**
     * Constructor for a successful response.
     *
     * @param connection the @link HttpURLConnection} that this response is for.
     * @throws IOException if the @code InputStream} from the connection cannot be read.
     */
    protected StreamingResponse(@NonNull final HttpURLConnection connection) throws IOException {
        super(connection.getResponseCode(), connection.getHeaderFields(), null);

        mConnection = connection;
        LogManager.v("Got HTTP status code %d", getHttpStatusCode());

        if (getHttpStatusCode() >= STATUS_CODE_SUCCESS_MIN_INCLUSIVE
                && getHttpStatusCode() < STATUS_CODE_SUCCESS_MAX_EXCLUSIVE) {
            mData = connection.getInputStream();
        } else {
            mData = connection.getErrorStream();
        }
    }

    /**
     * Constructor for an error response.
     *
     * @param error any exception that was thrown during the request.
     */
    protected StreamingResponse(@NonNull final Exception error) {
        super(error);
        mConnection = null;
        mData = null;
    }

    /**
     * Default constructor for testing.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */StreamingResponse() {
        super();
        mConnection = null;
        mData = null;
    }

    /**
     * @return the {@link InputStream} containing the data from the server's response
     */
    @Override
    @Nullable
    public final InputStream getData() {
        return mData;
    }

    /**
     * Closes this connection. You must call this when you are done reading this response.
     */
    public final void close() {
        if (null != mConnection) {
            mConnection.disconnect();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mData == null) ? 0 : mData.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("StreamingResponse [mData=%s]", mData);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StreamingResponse)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        final StreamingResponse other = (StreamingResponse) obj;
        if (mData == null) {
            if (other.mData != null) {
                return false;
            }
        } else if (!mData.equals(other.mData)) {
            return false;
        }
        return true;
    }
}
