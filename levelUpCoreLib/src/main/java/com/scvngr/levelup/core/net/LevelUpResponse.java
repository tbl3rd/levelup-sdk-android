/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.util.PreconditionUtil;

import net.jcip.annotations.ThreadSafe;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Class to encompass a response from the LevelUp Web Service.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.DRAFT)
public final class LevelUpResponse extends BufferedResponse implements Parcelable {
    /**
     * The name of the Server HTTP header.
     */
    @NonNull
    private static final String HTTP_HEADER_SERVER = "Server";

    /**
     * The expected value for the Server header for us to know it's coming from LevelUp Platform
     * (and not a reverse-proxy or failover server).
     */
    @NonNull
    private static final String HTTP_HEADER_VALUE_SERVER = "LevelUp";

    /**
     * Creator for parceling.
     */
    public static final Creator<LevelUpResponse> CREATOR = new Creator<LevelUpResponse>() {

        @Override
        public LevelUpResponse[] newArray(final int size) {
            return new LevelUpResponse[size];
        }

        @Override
        public LevelUpResponse createFromParcel(final Parcel in) {
            return new LevelUpResponse(in);
        }
    };

    /**
     * Status of the response.
     */
    @NonNull
    private final LevelUpStatus mStatus;

    /**
     * @param data the string data (typically JSON) from the response from the server.
     * @param status the status of the response. See {@link LevelUpStatus}.
     */
    public LevelUpResponse(@NonNull final String data, @NonNull final LevelUpStatus status) {
        super(data);
        mStatus = status;
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from.
     */
    public LevelUpResponse(@NonNull final Parcel in) {
        super(in);
        mStatus = LevelUpStatus.valueOf(in.readString());
        checkRep();
    }

    /**
     * Constructor for testing purposes.
     *
     * @param data the string content of the response.
     * @param status HTTP status code.
     * @param headers HTTP headers.
     * @param error error from response or null if there was none.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpResponse(@NonNull final String data, @NonNull final LevelUpStatus status,
            @Nullable final Map<String, List<String>> headers, @Nullable final Exception error) {
        super(data, HTTP_STATUS_CODE_UNUSED, headers, error);
        mStatus = status;
    }

    /**
     * Constructor for testing purposes.
     *
     * @param data the string content of the response.
     * @param statusCode HTTP status code.
     * @param headers HTTP headers.
     * @param error error from response or null if there was none.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpResponse(@NonNull final String data, final int statusCode,
            @Nullable final Map<String, List<String>> headers, @Nullable final Exception error) {
        super(data, statusCode, headers, error);

        String serverHeader = null;

        if (null != headers) {
            final List<String> serverHeaders = headers.get(HTTP_HEADER_SERVER);

            if (null != serverHeaders && serverHeaders.size() > 0) {
                serverHeader = serverHeaders.get(0);
            }
        }

        mStatus = mapStatus(statusCode, serverHeader);
    }

    /**
     * @param data the input stream from the response
     * @param status the status of the response. See {@link LevelUpStatus}.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpResponse(@NonNull final InputStream data,
            @NonNull final LevelUpStatus status) {
        super(data);
        mStatus = mapStatus(null, getError());
        checkRep();
    }

    /**
     * Constructor to use to build the LevelUpResponse from a {@link StreamingResponse}. Reads the
     * data from the response and calls {@link StreamingResponse#close} after.
     *
     * @param response the {@link StreamingResponse} to convert to an LevelUpResponse.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpResponse(@NonNull final StreamingResponse response) {
        super(response);
        mStatus = mapStatus(response, getError());
        checkRep();
    }

    /**
     * Asserts representation invariants of this class.
     */
    private void checkRep() {
        PreconditionUtil.assertNotNull(mStatus, "status");
    }

    /**
     * Maps the data in the response (status code and error) or the error thrown when reading the
     * input stream from the response to a more friendly status code.
     *
     * @param response the response from the server
     * @param streamError the error that occurred during the reading of the stream from the response
     * @return the {@link LevelUpStatus} best representing the contents of the response/stream error
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static LevelUpStatus mapStatus(@Nullable final StreamingResponse response,
            @Nullable final Exception streamError) {
        LevelUpStatus status = LevelUpStatus.ERROR_UNKNOWN;

        if (null != response) {
            status = mapStatus(response);
        }

        if (null != streamError) {
            status = mapStatus(streamError);
        }

        return status;
    }

    /**
     * Maps status from a response object.
     *
     * @param response the response that was received from the server.
     * @return {@link LevelUpStatus} describing the state of the response.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static LevelUpStatus mapStatus(@NonNull final StreamingResponse response) {
        LevelUpStatus status = LevelUpStatus.ERROR_UNKNOWN;

        if (AbstractResponse.HTTP_STATUS_CODE_UNUSED != response.getHttpStatusCode()) {
            status = mapStatus(response.getHttpStatusCode(),
                    response.getHttpHeader(HTTP_HEADER_SERVER));
        }

        if (null != response.getError()) {
            status = mapStatus(response.getError());
        }

        return status;
    }

    /**
     * Maps an {@link LevelUpStatus} to an HTTP status code from the response.
     *
     * @param statusCode the HTTP status code from the response.
     * @param serverHeader the value of the Server HTTP header; this is used to validate whether the
     *        response is coming from the LevelUp Platform server or a backup/reverse-proxy.
     * @return {@link LevelUpStatus} describing the status code.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static LevelUpStatus mapStatus(final int statusCode,
            @Nullable final String serverHeader) {
        final boolean fromLevelUpPlatform = HTTP_HEADER_VALUE_SERVER.equals(serverHeader);
        final LevelUpStatus status;

        if (statusCode >= AbstractResponse.STATUS_CODE_SUCCESS_MIN_INCLUSIVE
                && statusCode < AbstractResponse.STATUS_CODE_SUCCESS_MAX_EXCLUSIVE) {
            // A 2xx status code is OK.
            status = LevelUpStatus.OK;
        } else if (HttpURLConnection.HTTP_NOT_IMPLEMENTED == statusCode && fromLevelUpPlatform) {
            /*
             * The API treats a 501 response as a requirement for the client to be upgraded.
             * However, in failover etc. this response should be ignored since it may have a
             * different meaning coming from other servers.
             */
            status = LevelUpStatus.UPGRADE;
        } else if (HttpURLConnection.HTTP_UNAUTHORIZED == statusCode && fromLevelUpPlatform) {
            /*
             * The API treats a 401 response as a notice that the user needs a new access token, and
             * should be logged out. However, if a reverse-proxy or backup server is sending the
             * response, this should be ignored since it may have a different meaning coming from
             * other servers.
             */
            status = LevelUpStatus.LOGIN_REQUIRED;
        } else if (HttpURLConnection.HTTP_NOT_FOUND == statusCode && fromLevelUpPlatform) {
            /*
             * Some endpoints (like PaymentToken) have special meanings for 404s, but in failover
             * they may just indicate a partially-functioning service and should be treated as
             * generic network errors.
             */
            status = LevelUpStatus.ERROR_NOT_FOUND;
        } else if (HttpURLConnection.HTTP_UNAVAILABLE == statusCode) {
            // LevelUp is down for maintenance (applicable even if the Server header differs).
            status = LevelUpStatus.ERROR_MAINTENANCE;
        } else {
            // All other response codes are server errors.
            status = LevelUpStatus.ERROR_SERVER;
        }

        return status;
    }

    /**
     * Maps status from an {@link Exception}.
     *
     * @param error the error that was thrown.
     * @return {@link LevelUpStatus} describing the state of the response.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static LevelUpStatus mapStatus(final Exception error) {
        LevelUpStatus status = LevelUpStatus.ERROR_UNKNOWN;

        if (error instanceof ResponseTooLargeException) {
            status = LevelUpStatus.ERROR_RESPONSE_TOO_LARGE;
        } else if (error instanceof BadRequestException) {
            status = LevelUpStatus.ERROR_BAD_REQUEST;
        } else {
            // All other errors can be categorized as network errors.
            status = LevelUpStatus.ERROR_NETWORK;
        }

        return status;
    }

    /**
     * @return the {@link LevelUpStatus} for this LevelUpResponse.
     */
    @NonNull
    public LevelUpStatus getStatus() {
        return mStatus;
    }

    @SuppressWarnings("null") // Generated code.
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mStatus == null) ? 0 : mStatus.hashCode());
        result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj) {
            return false;
        }

        if (!(obj instanceof LevelUpResponse)) {
            return false;
        }

        final LevelUpResponse other = (LevelUpResponse) obj;

        if (null == getData()) {
            if (null != other.getData()) {
                return false;
            }
        } else {
            if (!getData().equals(other.getData())) {
                return false;
            }
        }

        if (mStatus != other.mStatus) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "LevelUpResponse [mBufferedResponse=%s, mStatus=%s]",
                super.toString(), mStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mStatus.name());
    }
}
