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
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An {@link AbstractResponse} that uses a buffered interface where the response data is buffered in
 * memory.
 */
@LevelUpApi(contract = Contract.DRAFT)
public class BufferedResponse extends AbstractResponse<String> implements Parcelable {

    /**
     * Creator for parceling.
     */
    public static final Creator<BufferedResponse> CREATOR = new Creator<BufferedResponse>() {

        @Override
        public BufferedResponse[] newArray(final int size) {
            return new BufferedResponse[size];
        }

        @Override
        public BufferedResponse createFromParcel(final Parcel in) {
            return new BufferedResponse(in);
        }
    };

    /**
     * The maximum download size allowed.
     * <p>
     * This prevents the server from crashing the app by returning an excessively large response.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final int MAX_DATA_SIZE_BYTES = 1024 * 450; // 450kb

    /**
     * Size of the in memory buffer when reading the input stream.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final int READ_BUFFER_SIZE_BYTES = 4096;

    /**
     * Data represented in the response.
     */
    @NonNull
    private final String mData;

    /**
     * Error representing and error that occurred during the reading of the response.
     */
    @Nullable
    private final Exception mReadError;

    /**
     * @param data the string data (typically JSON) from the response from the server.
     */
    public BufferedResponse(@NonNull final String data) {
        mData = data;
        mReadError = null;
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from.
     */
    public BufferedResponse(@NonNull final Parcel in) {
        super(in.readInt(), (Exception) in.readSerializable());
        mData = in.readString();
        mReadError = (Exception) in.readSerializable();
    }

    /**
     * @param data the input stream from the response.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */BufferedResponse(@NonNull final InputStream data) {
        Exception error = null;
        StringBuilder builder = null;
        try {
            builder = readStream(data);
        } catch (final IOException e) {
            builder = new StringBuilder();
            error = e;
        }

        mData = builder.toString();
        mReadError = error;
    }

    /**
     * Constructor to use to build the LevelUpResponse for testing.
     *
     * @param data the string content of the response.
     * @param statusCode HTTP status code.
     * @param headers HTTP headers.
     * @param error error from response or null if there was none.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */BufferedResponse(@NonNull final String data, final int statusCode,
            @Nullable final Map<String, List<String>> headers, @Nullable final Exception error) {
        super(statusCode, headers, error);
        mReadError = null;
        mData = data;
    }

    /**
     * Constructor to use to build the LevelUpResponse from a {@link StreamingResponse}. Reads the
     * data from the response and calls {@link StreamingResponse#close} after.
     *
     * @param response the {@link StreamingResponse} to convert to an LevelUpResponse.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */BufferedResponse(@NonNull final StreamingResponse response) {
        super(response.getHttpStatusCode(), response.getHttpHeaders(), response.getError());
        Exception error = null;
        StringBuilder builder = null;

        try {
            final InputStream data = response.getData();

            if (null != data) {
                builder = readStream(data);
            }
        } catch (final IOException e) {
            error = e;
        }

        mData = (null == builder) ? "" : NullUtils.nonNullContract(builder.toString());
        mReadError = error;
        response.close();
    }

    @Override
    @Nullable
    public String getData() {
        return mData;
    }

    /**
     * Get the error that occurred during the sending of the request OR during the reading of the
     * response.
     *
     * @return the error that occurred.
     */
    @Override
    @Nullable
    public Exception getError() {
        Exception error = mReadError;
        if (null == error) {
            error = super.getError();
        }

        return error;
    }

    /**
     * Reads the contents of the input stream passed into a {@link StringBuilder}.
     *
     * @param data the {@link InputStream} containing the response data.
     * @return a {@link StringBuilder} containing the data read.
     * @throws IOException if an error occurs during the reading of the stream.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static StringBuilder readStream(@NonNull final InputStream data)
            throws IOException {
        final StringBuilder builder = new StringBuilder();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(data, "utf-8"));
        final char[] chars = new char[READ_BUFFER_SIZE_BYTES];
        int size = 0;

        try {
            while (true) {
                final int read = reader.read(chars);
                if (-1 == read) {
                    break;
                }

                size += read;

                if (MAX_DATA_SIZE_BYTES < size) {
                    throw new ResponseTooLargeException();
                }

                // Only append the number of characters read (no extra junk)
                builder.append(chars, 0, read);
            }
        } finally {
            reader.close();
        }

        LogManager.v("Response is %s", builder);

        return builder;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "BufferedResponse [mData=%s, AbstractResponse=%s]", mData, super.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(getHttpStatusCode());
        dest.writeSerializable(super.getError());
        dest.writeString(mData);
        dest.writeSerializable(mReadError);
    }

    /**
     * Exception subclass that gets thrown if the response from the server is larger than
     * {@link BufferedResponse#MAX_DATA_SIZE_BYTES}.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static class ResponseTooLargeException extends IOException {

        /**
         * Implements the {@link java.io.Serializable} interface.
         */
        private static final long serialVersionUID = 2498672579135573453L;
    }
}
