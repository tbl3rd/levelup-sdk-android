/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.util.NullUtils;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class representing a request to the LevelUp web service using the API v14 and above standards.
 *
 * <pre>
 * 1) All POST parameters will be in JSON.
 * 2) Any access token parameter will be passed in an authorization header.
 * 3) .json URLS will no longer be used, must set accepts header.
 *
 * </pre>
 */
@LevelUpApi(contract = Contract.DRAFT)
public class LevelUpRequest extends AbstractRequest {
    /**
     * API version code for v14.
     */
    @NonNull
    public static final String API_VERSION_CODE_V14 = "v14"; //$NON-NLS-1$

    /**
     * API version code for v15.
     */
    @NonNull
    public static final String API_VERSION_CODE_V15 = "v15"; //$NON-NLS-1$

    /**
     * Creator for parceling.
     */
    public static final Creator<LevelUpRequest> CREATOR = new Creator<LevelUpRequest>() {

        @Override
        public LevelUpRequest createFromParcel(final Parcel in) {
            return new LevelUpRequest(NullUtils.nonNullContract(in));
        }

        @Override
        public LevelUpRequest[] newArray(final int size) {
            return new LevelUpRequest[size];
        }
    };

    /**
     * Header key for the authorized user's access token.
     */
    @NonNull
    public static final String HEADER_AUTHORIZATION = "Authorization"; //$NON-NLS-1$

    /**
     * Header key for sending the application's API key for LevelUp.
     */
    @NonNull
    public static final String HEADER_LEVELUP_API_KEY = "X-LevelUp-API-Key"; //$NON-NLS-1$

    /**
     * Format string to use in the authorization header to format the access token in.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String AUTH_TOKEN_TYPE_FORMAT = "token %s"; //$NON-NLS-1$

    /**
     * Gets the full endpoint URL.
     *
     * @param context Application context.
     * @param apiVersion the version of the API to hit.
     * @param endpoint the endpoint to hit.
     * @return the full URL to the endpoint passed.
     */
    @NonNull
    public static String getFullUrl(@NonNull final Context context,
            @NonNull final String apiVersion, @NonNull final String endpoint) {
        return NullUtils.nonNullContract(new Uri.Builder()
                .scheme(context.getString(R.string.levelup_api_scheme))
                .encodedAuthority(context.getString(R.string.levelup_api_authority))
                .encodedPath(apiVersion).appendEncodedPath(endpoint).build().toString());
    }

    /**
     * Implementation of {@link AccessTokenRetriever} to use to get the user's {@link AccessToken}
     * if it is needed for the request. This allows the request to get the access token at request
     * time, so disk/db operations won't have to be done in the main thread.
     */
    @Nullable
    private final AccessTokenRetriever mAccessTokenRetriever;

    @Nullable
    private final RequestBody mBody;

    /**
     * Creates a new {@link LevelUpRequest}. This variant of the constructor is only for
     * non-authenticated requests.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param body the request body to POST/PUT.
     */
    public LevelUpRequest(@NonNull final Context context, @NonNull final HttpMethod method,
            @NonNull final String apiVersion, @NonNull final String endpoint,
            @Nullable final Map<String, String> queryParams, @Nullable final RequestBody body) {
        this(context, method, apiVersion, endpoint, queryParams, body, null);
    }

    /**
     * Creates a new {@link LevelUpRequest}. This variant of the constructor takes the optional
     * {@link AccessTokenRetriever} for authenticated requests.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param body the request body to POST/PUT.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     */
    public LevelUpRequest(@NonNull final Context context, @NonNull final HttpMethod method,
            @NonNull final String apiVersion, @NonNull final String endpoint,
            @Nullable final Map<String, String> queryParams, @Nullable final RequestBody body,
            @Nullable final AccessTokenRetriever retriever) {
        this(context, method, apiVersion, endpoint, queryParams, RequestUtils
                .getDefaultRequestHeaders(context), body, retriever);
    }

    /**
     * Creates a new {@link LevelUpRequest}. This variant of the constructor takes the optional
     * {@link AccessTokenRetriever} for authenticated requests.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param headers allows for specifying request headers.
     * @param body the request body to POST/PUT.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     */
    public LevelUpRequest(@NonNull final Context context, @NonNull final HttpMethod method,
            @NonNull final String apiVersion, @NonNull final String endpoint,
            @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> headers, @Nullable final RequestBody body,
            @Nullable final AccessTokenRetriever retriever) {
        super(method, getFullUrl(context, apiVersion, endpoint), headers, queryParams);

        mBody = body;

        mAccessTokenRetriever = retriever;
    }

    /**
     * Creates a new {@link LevelUpRequest}. This variant of the constructor takes a {@link Uri}
     * which must be the complete URL of the request.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param url the request URL. This can include query parameters.
     * @param body the request body to POST/PUT.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     * @throws IllegalArgumentException if the URI passed in isn't an absolute URL.
     */
    public LevelUpRequest(@NonNull final Context context, @NonNull final HttpMethod method,
            @NonNull final Uri url, @Nullable final RequestBody body,
            @Nullable final AccessTokenRetriever retriever) throws IllegalArgumentException {
        super(method, url, RequestUtils.getDefaultRequestHeaders(context));

        mBody = body;

        mAccessTokenRetriever = retriever;
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from.
     */
    public LevelUpRequest(@NonNull final Parcel in) {
        super(in);
        mAccessTokenRetriever = in.readParcelable(LevelUpRequest.class.getClassLoader());
        mBody = in.readParcelable(LevelUpRequest.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @param context the Application context.
     * @return the User's {@link AccessToken}.
     */
    @Nullable
    public final AccessToken getAccessToken(@NonNull final Context context) {
        AccessToken token = null;

        if (null != mAccessTokenRetriever) {
            token = mAccessTokenRetriever.getAccessToken(context);
        }

        return token;
    }

    /**
     * @param context the context to use to get context dependent parameters
     * @return the body of the request
     */
    @Nullable
    public final String getBody(@NonNull final Context context) {
        String result = null;

        final RequestBody body = mBody;

        try {
            if (body != null && 0 < body.getContentLength()) {
                final ByteArrayOutputStream outputStreamBuffer = new ByteArrayOutputStream();

                try {
                    writeBodyToStream(context, outputStreamBuffer);
                    result = outputStreamBuffer.toString("UTF-8"); //$NON-NLS-1$
                } finally {
                    outputStreamBuffer.close();
                }
            }
        } catch (final UnsupportedEncodingException e) {
            // This is pretty much impossible.
            throw new RuntimeException("The unthinkable happened: there is no UTF-8", e); //$NON-NLS-1$
        } catch (final IOException e) {
            throw new RuntimeException("Error writing body to String", e); //$NON-NLS-1$
        }

        return result;
    }

    @Override
    public final int getBodyLength(@NonNull final Context context) {
        int length = 0;

        if (null != mBody) {
            length = mBody.getContentLength();
        }

        return length;
    }

    @Override
    @NonNull
    public final Map<String, String> getRequestHeaders(@NonNull final Context context) {
        final Map<String, String> headers;
        final HashMap<String, String> temp =
                new HashMap<String, String>(super.getRequestHeaders(context));
        final RequestBody body = mBody;

        if (null != body) {
            temp.put(HTTP.CONTENT_TYPE, body.getContentType());
            temp.put(HTTP.CONTENT_LEN, String.valueOf(body.getContentLength()));
        } else {
            temp.put(HTTP.CONTENT_TYPE, RequestUtils.HEADER_CONTENT_TYPE_JSON);
        }

        temp.put(HEADER_LEVELUP_API_KEY, context.getString(R.string.levelup_api_key));
        final AccessToken token = getAccessToken(context);

        if (null != token) {
            temp.put(HEADER_AUTHORIZATION,
                    String.format(Locale.US, AUTH_TOKEN_TYPE_FORMAT, token.getAccessToken()));
        }

        headers = NullUtils.nonNullContract(Collections.unmodifiableMap(temp));

        return headers;
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LevelUpRequest other = (LevelUpRequest) obj;
        if (mAccessTokenRetriever == null) {
            if (other.mAccessTokenRetriever != null) {
                return false;
            }
        } else if (!mAccessTokenRetriever.equals(other.mAccessTokenRetriever)) {
            return false;
        }
        if (mBody == null) {
            if (other.mBody != null) {
                return false;
            }
        } else if (!mBody.equals(other.mBody)) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("null")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result =
                prime * result
                        + ((mAccessTokenRetriever == null) ? 0 : mAccessTokenRetriever.hashCode());
        result = prime * result + ((mBody == null) ? 0 : mBody.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "LevelUpRequest [mAccessTokenRetriever=%s, mBody=%s, super=%s]", //$NON-NLS-1$
                mAccessTokenRetriever, mBody, super.toString());
    }

    @Override
    public void
            writeBodyToStream(@NonNull final Context context, @NonNull final OutputStream stream)
                    throws IOException {
        if (null != mBody) {
            mBody.writeToOutputStream(context, stream);
        }
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mAccessTokenRetriever, flags);
        dest.writeParcelable(mBody, flags);
    }
}
