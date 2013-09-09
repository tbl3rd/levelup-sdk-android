package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.net.request.RequestUtils;

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
public class LevelUpRequest extends BufferedRequest {
    /**
     * Creator for parceling.
     */
    public static final Creator<LevelUpRequest> CREATOR =
            new Creator<LevelUpRequest>() {

                @Override
                public LevelUpRequest[] newArray(final int size) {
                    return new LevelUpRequest[size];
                }

                @Override
                public LevelUpRequest createFromParcel(final Parcel in) {
                    return new LevelUpRequest(in);
                }
            };

    /**
     * API version code for v14.
     */
    public static final String API_VERSION_CODE_V14 = "v14"; //$NON-NLS-1$

    /**
     * Header key for the authentication.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final String HEADER_AUTHORIZATION = "Authorization"; //$NON-NLS-1$

    /**
     * Format string to use in the authorization header to format the oauth token in.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String AUTH_TOKEN_TYPE_FORMAT = "token %s"; //$NON-NLS-1$

    /**
     * Header key for Content Type.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String HEADER_CONTENT_TYPE = "Content-Type"; //$NON-NLS-1$

    /**
     * Implementation of {@link AccessTokenRetriever} to use to get the user's
     * {@link OAuthAccessToken} if it is needed for the request. This allows the request to get the
     * access token at request time, so disk/db operations won't have to be done in the main thread.
     */
    @Nullable
    private final AccessTokenRetriever mAccessTokenRetriever;

    /**
     * Creates a new {@link LevelUpV13Request}. This request will not try to append the
     * user's access token to the request.
     *
     * @param context Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param bodyParams the body of the request if it's a post/put.
     */
    public LevelUpRequest(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONObject bodyParams) {
        this(context, method, apiVersion, endpoint, queryParams, bodyParams, null);
    }

    /**
     * Creates a new {@link LevelUpV13Request}. This request will attempt to append the
     * user's access_token to the request if the {@link AccessTokenRetriever} is not null and
     * returns a non-null AccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param bodyParams the body of the request if it's a post/put.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     */
    public LevelUpRequest(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONObject bodyParams, @Nullable final AccessTokenRetriever retriever) {
        super(method, getFullUrl(context, apiVersion, endpoint), RequestUtils
                .getDefaultRequestHeaders(context), queryParams, getPostBody(bodyParams));

        mAccessTokenRetriever = retriever;
    }

    /**
     * Creates a new {@link LevelUpRequest} from a URL. This request will attempt to
     * append the user's access_token to the request if the {@link AccessTokenRetriever} passed is
     * not null and returns a non-null OAuthAccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param url the request URL. This can include query parameters.
     * @param postParams the body of the request if its a post.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     * @throws IllegalArgumentException if the URI specified isn't an absolute URL.
     */
    public LevelUpRequest(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final JSONObject postParams, @Nullable final AccessTokenRetriever retriever)
            throws IllegalArgumentException {
        super(method, url, RequestUtils.getDefaultRequestHeaders(context), getPostBody(postParams));
        mAccessTokenRetriever = retriever;
    }

    /**
     * Creates a new {@link LevelUpV13Request}. This request will not try to append the
     * user's access token to the request.
     *
     * @param context Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param bodyParams the body of the request if it's a post/put.
     */
    public LevelUpRequest(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONArray bodyParams) {
        this(context, method, apiVersion, endpoint, queryParams, bodyParams, null);
    }

    /**
     * Creates a new {@link LevelUpV13Request}. This request will attempt to append the
     * user's access_token to the request the {@link AccessTokenRetriever} is not null and returns a
     * non-null OAuthAccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to hit.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param bodyParams the body of the request if it's a post/put.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     */
    public LevelUpRequest(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONArray bodyParams, @Nullable final AccessTokenRetriever retriever) {
        super(method, getFullUrl(context, apiVersion, endpoint), RequestUtils
                .getDefaultRequestHeaders(context), queryParams, getPostBody(bodyParams));

        mAccessTokenRetriever = retriever;
    }

    /**
     * Creates a new {@link LevelUpRequest} from a URL. This request will attempt to
     * append the user's access_token to the request if the {@link AccessTokenRetriever} passed is
     * not null and returns a non-null OAuthAccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param url the request URL. This can include query parameters.
     * @param bodyParams the body of the request if it's a post/put.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     * @throws IllegalArgumentException if the URI specified isn't an absolute URL.
     */
    public LevelUpRequest(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final JSONArray bodyParams, @Nullable final AccessTokenRetriever retriever)
            throws IllegalArgumentException {
        super(method, url, RequestUtils.getDefaultRequestHeaders(context), getPostBody(bodyParams));
        mAccessTokenRetriever = retriever;
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from.
     */
    public LevelUpRequest(@NonNull final Parcel in) {
        super(in);
        mAccessTokenRetriever =
                in.readParcelable(LevelUpRequest.class.getClassLoader());
    }

    @Override
    @NonNull
    public final Map<String, String> getRequestHeaders(@NonNull final Context context) {
        final Map<String, String> headers;
        final HashMap<String, String> temp =
                new HashMap<String, String>(super.getRequestHeaders(context));
        temp.put(HEADER_CONTENT_TYPE, RequestUtils.HEADER_CONTENT_TYPE_JSON);
        final AccessToken token = getAccessToken(context);

        if (null != token) {
            temp.put(HEADER_AUTHORIZATION,
                    String.format(Locale.US, AUTH_TOKEN_TYPE_FORMAT, token.getAccessToken()));
        }

        headers = Collections.unmodifiableMap(temp);

        return headers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mAccessTokenRetriever, flags);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "LevelUpV13Request [mAccessTokenRetriever=%s, super=%s]", //$NON-NLS-1$
                mAccessTokenRetriever, super.toString());
    }

    @Nullable
    private static String getPostBody(final Object postParams) {
        return null == postParams ? null : postParams.toString();
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
     * Gets the full endpoint URL.
     *
     * @param context Application context.
     * @param apiVersion the version of the API to hit.
     * @param endpoint the endpoint to hit.
     * @return the full URL to the endpoint passed.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */final static String getFullUrl(@NonNull final Context context,
            @NonNull final String apiVersion, @NonNull final String endpoint) {
        return new Uri.Builder().scheme(context.getString(R.string.levelup_api_scheme))
                .encodedAuthority(context.getString(R.string.levelup_api_authority))
                .encodedPath(apiVersion).appendEncodedPath(endpoint).build().toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result =
                prime * result
                        + ((null == mAccessTokenRetriever) ? 0 : mAccessTokenRetriever.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof LevelUpRequest)) {
            return false;
        }

        final LevelUpRequest other = (LevelUpRequest) obj;

        if (null == mAccessTokenRetriever) {
            if (null != other.mAccessTokenRetriever) {
                return false;
            }
        } else if (!mAccessTokenRetriever.equals(other.mAccessTokenRetriever)) {
            return false;
        }

        return true;
    }
}
