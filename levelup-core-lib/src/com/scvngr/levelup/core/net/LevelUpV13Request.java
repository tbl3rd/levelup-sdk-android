package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.net.request.RequestUtils;
import com.scvngr.levelup.core.util.CoreLibConstants;
import com.scvngr.levelup.core.util.LogManager;

/**
 * Class which represents a request to the LevelUp web service.
 */
@Deprecated
@LevelUpApi(contract = Contract.INTERNAL)
public class LevelUpV13Request extends BufferedRequest implements Parcelable {

    /**
     * Creator for parceling.
     */
    public static final Creator<LevelUpV13Request> CREATOR =
            new Creator<LevelUpV13Request>() {

                @Override
                public LevelUpV13Request[] newArray(final int size) {
                    return new LevelUpV13Request[size];
                }

                @Override
                public LevelUpV13Request createFromParcel(final Parcel in) {
                    return new LevelUpV13Request(in);
                }
            };

    /**
     * API version code for v13.
     */
    public static final String API_VERSION_CODE_V13 = "v13"; //$NON-NLS-1$

    /**
     * API version code for v14.
     */
    public static final String API_VERSION_CODE_V14 = "v14"; //$NON-NLS-1$

    /**
     * Default API version for requests to the server.
     */
    @Deprecated
    public static final String DEFAULT_API_VERSION = API_VERSION_CODE_V13;

    /**
     * Query string or body body parameter key for the OAuth access token.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final String PARAM_ACCESS_TOKEN = "access_token"; //$NON-NLS-1$

    /**
     * Implementation of {@link AccessTokenRetriever} to use to get the user's
     * {@link OAuthAccessToken} if it is needed for the request. This allows the request to get the
     * access token at request time, so disk/db operations won't have to be done in the main thread.
     */
    @Nullable
    private final AccessTokenRetriever mAccessTokenRetriever;

    /**
     * Set of post body parameters.
     * <p>
     * The key/value pairs in this map have not yet been URL encoded.
     * <p>
     * This has been wrapped in a call to {@link Collections#unmodifiableMap(Map)}.
     */
    @NonNull
    private final Map<String, String> mPostParams;

    /**
     * Creates a new {@link LevelUpV13Request}. This request will not try to append the
     * user's access token to the request.
     *
     * @param context Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the API version code.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param postParams the body of the request if it's a post. This map cannot contain null keys
     *        or values.
     */
    public LevelUpV13Request(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> postParams) {
        this(context, method, apiVersion, endpoint, queryParams, postParams, null);
    }

    /**
     * Creates a new {@link LevelUpV13Request}. This request will not try to append the
     * user's access token to the request.
     *
     * @param context Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param endpoint the API endpoint to request.
     * @param queryParams the query string parameters.
     * @param postParams the body of the request if it's a post. This map cannot contain null keys
     *        or values.
     */
    public LevelUpV13Request(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String endpoint,
            @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> postParams) {
        this(context, method, endpoint, queryParams, postParams, null);
    }

    /**
     * Creates a new {@link LevelUpV13Request}. This request will attempt to append the
     * user's access_token to the request if the {@link AccessTokenRetriever} passed is not null and
     * returns a non-null AccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param endpoint the api endpoint to request.
     * @param queryParams the query string parameters.
     * @param postParams the body of the request if its a post.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     * @deprecated please use the constructor which explicitly states the API version.
     */
    @Deprecated
    public LevelUpV13Request(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String endpoint,
            @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> postParams,
            @Nullable final AccessTokenRetriever retriever) {
        this(context, method, DEFAULT_API_VERSION, endpoint, queryParams, postParams, retriever);
    }

    /**
     * Creates a new {@link LevelUpV13Request}. This request will attempt to append the
     * user's access_token to the request if the {@link AccessTokenRetriever} passed is not null and
     * returns a non-null AccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the API version code.
     * @param endpoint the api endpoint to request.
     * @param queryParams the query string parameters.
     * @param postParams the body of the request if its a post.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     */
    public LevelUpV13Request(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> postParams,
            @Nullable final AccessTokenRetriever retriever) {
        super(method, getFullJsonUrl(context, apiVersion, endpoint), RequestUtils
                .getDefaultRequestHeaders(context), queryParams, null);

        mAccessTokenRetriever = retriever;

        if (null != postParams) {
            mPostParams = Collections.unmodifiableMap(new HashMap<String, String>(postParams));
        } else {
            mPostParams = Collections.unmodifiableMap(new HashMap<String, String>());
        }

        checkRep();
    }

    /**
     * Creates a new {@link LevelUpV13Request} from a URL. This request will attempt to
     * append the user's access_token to the request if the {@link AccessTokenRetriever} passed is
     * not null and returns a non-null AccessToken.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param url the request URL. This can include query parameters.
     * @param postParams the body of the request if its a post.
     * @param retriever implementation of {@link AccessTokenRetriever} to use to try to append the
     *        access token to this request.
     * @throws IllegalArgumentException if the URI specified isn't an absolute URL.
     */
    public LevelUpV13Request(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final Map<String, String> postParams,
            @Nullable final AccessTokenRetriever retriever) throws IllegalArgumentException {
        super(method, url, RequestUtils.getDefaultRequestHeaders(context), null);

        mAccessTokenRetriever = retriever;

        if (null != postParams) {
            mPostParams = Collections.unmodifiableMap(new HashMap<String, String>(postParams));
        } else {
            mPostParams = Collections.unmodifiableMap(new HashMap<String, String>());
        }

        checkRep();
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from.
     */
    public LevelUpV13Request(@NonNull final Parcel in) {
        super(in);
        mAccessTokenRetriever = in.readParcelable(LevelUpV13Request.class.getClassLoader());

        final Map<String, String> postParams = new HashMap<String, String>();
        in.readMap(postParams, HashMap.class.getClassLoader());
        mPostParams = Collections.unmodifiableMap(postParams);

        checkRep();
    }

    /**
     * Asserts representation invariants of this class.
     */
    private void checkRep() {
        if (CoreLibConstants.IS_CHECKREP_ENABLED) {
            if (null == mPostParams) {
                throw new NullPointerException("mPostParams cannot be null"); //$NON-NLS-1$
            }

            for (final Entry<String, String> entry : mPostParams.entrySet()) {
                if (null == entry.getKey()) {
                    throw new NullPointerException("mPostParams cannot contain null keys"); //$NON-NLS-1$
                }

                if (null == entry.getValue()) {
                    throw new NullPointerException("mPostParams cannot contain null values"); //$NON-NLS-1$
                }
            }
        }
    }

    @NonNull
    @Override
    public Map<String, String> getQueryParams(@NonNull final Context context) {
        final Map<String, String> params =
                new HashMap<String, String>(super.getQueryParams(context));

        if (null != mAccessTokenRetriever) {
            final AccessToken accessToken = mAccessTokenRetriever.getAccessToken(context);
            if (null != accessToken) {
                params.put(PARAM_ACCESS_TOKEN, accessToken.getAccessToken());
            }
        }

        return Collections.unmodifiableMap(params);
    }

    @Override
    public String getBody(@NonNull final Context context) {
        return getEncodedBody(getPostParams());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mAccessTokenRetriever, flags);
        dest.writeMap(mPostParams);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "LevelUpV13Request [mAccessTokenRetriever=%s, mPostParams=%s, super=%s]", //$NON-NLS-1$
                mAccessTokenRetriever, mPostParams, super.toString());
    }

    /**
     * @return the Post Parameters.
     */
    @NonNull
    public Map<String, String> getPostParams() {
        return mPostParams;
    }

    /**
     * @param context the Application context.
     * @return the User's {@link AccessToken}.
     */
    @Nullable
    public AccessToken getAccessToken(@NonNull final Context context) {
        AccessToken token = null;

        if (null != mAccessTokenRetriever) {
            token = mAccessTokenRetriever.getAccessToken(context);
        }

        return token;
    }

    /**
     * Gets the full JSON endpoint URL. E.g. "https://api.thelevelup.com/v13/users.json"
     *
     * @param context Application context.
     * @param apiVersion the version of the API to hit.
     * @param endpoint the endpoint to hit.
     * @return the full url to the endpoint passed.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static String getFullJsonUrl(@NonNull final Context context,
            @NonNull final String apiVersion, @NonNull final String endpoint) {
        return new Uri.Builder().scheme(context.getString(R.string.levelup_api_scheme))
                .encodedAuthority(context.getString(R.string.levelup_api_authority)).encodedPath(
                        apiVersion).appendEncodedPath(endpoint + ".json").build().toString(); //$NON-NLS-1$
    }

    /**
     * Takes the params passed and forms a properly encoded POST body.
     *
     * @param params the post parameters.
     * @return {@link String} with the encoded post content.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @Nullable
    /* package */static String getEncodedBody(@Nullable final Map<String, String> params) {
        String encodedBody = null;

        if (null != params && params.size() > 0) {
            final StringBuilder builder = new StringBuilder();

            // Sort by key name
            final Set<String> keys = new TreeSet<String>(params.keySet());
            int count = keys.size();

            for (final String key : keys) {
                count--;
                try {
                    builder.append(String.format(Locale.US, "%s=%s", //$NON-NLS-1$
                            URLEncoder.encode(key, "utf-8"), //$NON-NLS-1$
                            URLEncoder.encode(params.get(key), "utf-8"))); //$NON-NLS-1$
                } catch (final UnsupportedEncodingException e) {
                    LogManager.e("Failed to encode post param/value", e); //$NON-NLS-1$
                }

                if (count > 0) {
                    // Only append the & if we are not at the last param
                    builder.append("&"); //$NON-NLS-1$
                }
            }

            encodedBody = builder.toString();
        }

        return encodedBody;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((null == mPostParams) ? 0 : mPostParams.hashCode());
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

        if (!(obj instanceof LevelUpV13Request)) {
            return false;
        }

        final LevelUpV13Request other = (LevelUpV13Request) obj;
        if (null == mPostParams) {
            if (null != other.mPostParams) {
                return false;
            }
        } else if (!mPostParams.equals(other.mPostParams)) {
            return false;
        }

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
