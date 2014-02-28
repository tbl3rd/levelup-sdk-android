/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.util.LogManager;

/**
 * Object which represents an HTTP request.
 * <p>
 * This object follows a "buffered" model, where the entire request is stored in memory before being
 * sent. This implies a maximum size limit of a request that can be sent using this class. On
 * Android devices, this limit is around 500 kilobytes.
 * @deprecated use a buffered implementation of {@link RequestBody} instead
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
@Deprecated
public class BufferedRequest extends AbstractRequest implements Parcelable {

    /**
     * Creator for parceling.
     */
    public static final Creator<BufferedRequest> CREATOR = new Creator<BufferedRequest>() {

        @Override
        public BufferedRequest[] newArray(final int size) {
            return new BufferedRequest[size];
        }

        @Override
        public BufferedRequest createFromParcel(final Parcel in) {
            return new BufferedRequest(in);
        }
    };

    /**
     * The body of the HTTP request.
     */
    @Nullable
    private final String mBody;

    /**
     * Create a new BufferedRequest.
     * <p>
     * Note that there are some representation invariants for the BufferedRequest object. For
     * example, a {@link HttpMethod#GET} cannot contain a {@code body}.
     *
     * @param method the {@code HttpMethod} of the request type.
     * @param url the URL to request (and to append query string parameters to).
     * @param requestHeaders the headers to add to the request. This cannot contain null keys or
     *        null values.
     * @param queryParams the query string parameters. This cannot contain null keys or null values.
     * @param body the body of the request.
     */
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public BufferedRequest(@NonNull final HttpMethod method, @NonNull final String url,
            @Nullable final Map<String, String> requestHeaders,
            @Nullable final Map<String, String> queryParams, @Nullable final String body) {
        super(method, url, requestHeaders, queryParams);
        mBody = body;
    }

    /**
     * Create a new {@link BufferedRequest} from an absolute URL.
     *
     * @param method the {@code HttpMethod} of the request type.
     * @param url The URL to request. This can include query parameters.
     * @param requestHeaders the headers to add to the request. This cannot contain null keys or
     *        null values.
     * @param body  the body of the request.
     * @throws IllegalArgumentException if the URI passed in isn't an absolute URL.
     */
    public BufferedRequest(@NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final Map<String, String> requestHeaders, @Nullable final String body)
            throws IllegalArgumentException {
        super(method, url, requestHeaders);
        mBody = body;
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from
     */
    public BufferedRequest(@NonNull final Parcel in) {
        super(in);
        mBody = in.readString();
    }

    /**
     * @param context the context to use to get context dependent parameters
     * @return the body of the request
     */
    @Nullable
    public String getBody(@NonNull final Context context) {
        return mBody;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((null == mBody) ? 0 : mBody.hashCode());
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

        if (!(obj instanceof BufferedRequest)) {
            return false;
        }

        final BufferedRequest other = (BufferedRequest) obj;

        if (!super.equals(obj)) {
            return false;
        }

        if (null == mBody) {
            if (null != other.mBody) {
                return false;
            }
        } else if (!mBody.equals(other.mBody)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "BufferedRequest [AbstractRequest=%s mBody=%s]", //$NON-NLS-1$
                super.toString(), mBody);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mBody);
    }

    @Override
    public void
            writeBodyToStream(@NonNull final Context context, @NonNull final OutputStream stream)
                    throws IOException {
        final String body = getBody(context);

        LogManager.d("Request body: %s", body); //$NON-NLS-1$

        if (null != body) {
            final byte[] bytes = body.getBytes("utf-8"); //$NON-NLS-1$
            stream.write(bytes);
        }
    }

    @Override
    public int getBodyLength(@NonNull final Context context) {
        int length = 0;
        final String body = getBody(context);

        if (null != body) {
            length = body.length();
        }

        return length;
    }
}
