package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import net.jcip.annotations.Immutable;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.CoreLibConstants;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.PreconditionUtil;

/**
 * Object which represents an HTTP request.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public abstract class AbstractRequest implements Parcelable {

    /**
     * The method of the HTTP request.
     */
    @NonNull
    private final HttpMethod mMethod;

    /**
     * The URL string where the request will be send. This URL does not yet encode
     * {@link #mQueryParams}.
     */
    @NonNull
    private final String mUrlString;

    /**
     * Key-value pairs representing HTTP headers. The elements stored in this map are not yet URL
     * encoded.
     * <p>
     * This map cannot contain null keys or null values.
     * <p>
     * This field has been wrapped in a call to {@link Collections#unmodifiableMap(Map)}.
     */
    @NonNull
    private final Map<String, String> mRequestHeaders;

    /**
     * Key-value pairs representing query string parameters. The elements stored in this map are not
     * yet URL encoded.
     * <p>
     * This map cannot contain null keys or null values.
     * <p>
     * This field has been wrapped in a call to {@link Collections#unmodifiableMap(Map)}.
     */
    @NonNull
    private final Map<String, String> mQueryParams;

    /**
     * Create a new {@link AbstractRequest}.
     * <p>
     * Note that there are some representation invariants for the {@link AbstractRequest} object.
     * For example, a {@link HttpMethod#GET} cannot contain a {@code body}.
     *
     * @param method the {@code HttpMethod} of the request type.
     * @param url the URL to request (and to append query string parameters to).
     * @param requestHeaders the headers to add to the request. This cannot contain null keys or
     *        null values.
     * @param queryParams the query string parameters. This cannot contain null keys or null values.
     */
    public AbstractRequest(@NonNull final HttpMethod method, @NonNull final String url,
            @Nullable final Map<String, String> requestHeaders,
            @Nullable final Map<String, String> queryParams) {

        PreconditionUtil.assertNotNull(method, "method"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(url, "url"); //$NON-NLS-1$

        if (null != requestHeaders) {
            mRequestHeaders =
                    Collections.unmodifiableMap(new HashMap<String, String>(requestHeaders));
        } else {
            mRequestHeaders = Collections.unmodifiableMap(new HashMap<String, String>());
        }

        if (null != queryParams) {
            mQueryParams = Collections.unmodifiableMap(new HashMap<String, String>(queryParams));
        } else {
            mQueryParams = Collections.unmodifiableMap(new HashMap<String, String>());
        }

        mUrlString = url;
        mMethod = method;

        checkRep();
    }

    /**
     * Create a new {@link AbstractRequest} from an absolute URL.
     *
     * @param method the {@code HttpMethod} of the request type.
     * @param url The URL to request. This can include query parameters.
     * @param requestHeaders the headers to add to the request. This cannot contain null keys or
     *        null values.
     * @throws IllegalArgumentException if the URI passed in isn't an absolute URL.
     */
    public AbstractRequest(@NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final Map<String, String> requestHeaders) throws IllegalArgumentException {
        this(method, stripQueryParameters(url), requestHeaders, extractQueryParameters(url));
    }

    /**
     * @param url the URL whose query parameters will be extracted.
     * @return a map of the query parameters or null if there is an error parsing the URL.
     */
    @Nullable
    private static Map<String, String> extractQueryParameters(final Uri url) {
        Map<String, String> params = null;

        try {
            final List<NameValuePair> paramsList =
                    URLEncodedUtils.parse(new URI(url.toString()), "utf-8"); //$NON-NLS-1$
            params = new HashMap<String, String>(paramsList.size());

            for (final NameValuePair nvp : paramsList) {
                params.put(nvp.getName(), nvp.getValue());
            }
        } catch (final URISyntaxException e) {
            // failsafe
            LogManager.e(String.format(Locale.US, "could not parse uri: '%s'. " //$NON-NLS-1$
                    + "dropping query parameters.", url), e); //$NON-NLS-1$
        }

        return params;
    }

    /**
     * Returns the portion of an absolute URL before the query string. E.g.
     * {@code http://example.com/search?q=kittens} would return {@code "http://example.com/search"}.
     *
     * @param url the URL to strip.
     * @return the URL stripped of any query parameters, including the '?'.
     * @throws IllegalArgumentException if the URI passed in isn't an absolute URL.
     */
    @NonNull
    private static String stripQueryParameters(final Uri url) throws IllegalArgumentException {
        if (!url.isAbsolute() || !url.isHierarchical()) {
            throw new IllegalArgumentException("Request URI must be an absolute URL"); //$NON-NLS-1$
        }

        return url.buildUpon().query(null).build().toString();
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from
     */
    public AbstractRequest(@NonNull final Parcel in) {
        mMethod = HttpMethod.valueOf(in.readString());
        mUrlString = in.readString();

        final Map<String, String> headers = new HashMap<String, String>();
        in.readMap(headers, HashMap.class.getClassLoader());
        mRequestHeaders = Collections.unmodifiableMap(headers);

        final Map<String, String> query = new HashMap<String, String>();
        in.readMap(query, HashMap.class.getClassLoader());
        mQueryParams = Collections.unmodifiableMap(query);

        checkRep();
    }

    /**
     * Asserts representation invariants of this class.
     */
    private void checkRep() {
        if (CoreLibConstants.IS_CHECKREP_ENABLED) {
            if (null == mMethod) {
                throw new NullPointerException("mMethod cannot be null"); //$NON-NLS-1$
            }

            if (null == mUrlString) {
                throw new NullPointerException("mUrlString cannot be null"); //$NON-NLS-1$
            }

            if (null == mRequestHeaders) {
                throw new NullPointerException("mUrlString cannot be null"); //$NON-NLS-1$
            }

            for (final Entry<String, String> entry : mRequestHeaders.entrySet()) {
                if (null == entry.getKey()) {
                    throw new NullPointerException("mRequestHeaders cannot contain null keys"); //$NON-NLS-1$
                }

                if (null == entry.getValue()) {
                    throw new NullPointerException("mRequestHeaders cannot contain null values"); //$NON-NLS-1$
                }
            }

            if (null == mQueryParams) {
                throw new NullPointerException("mQueryParams"); //$NON-NLS-1$
            }

            for (final Entry<String, String> entry : mQueryParams.entrySet()) {
                if (null == entry.getKey()) {
                    throw new NullPointerException("mQueryParams cannot contain null keys"); //$NON-NLS-1$
                }

                if (null == entry.getValue()) {
                    throw new NullPointerException("mQueryParams cannot contain null values"); //$NON-NLS-1$
                }
            }
        }
    }

    /**
     * @param context the context to use to get context dependent headers.
     * @return the HTTP headers for this request. This has been wrapped in a call to
     *         {@link Collections#unmodifiableMap(Map)}.
     */
    @NonNull
    public Map<String, String> getRequestHeaders(@NonNull final Context context) {
        return mRequestHeaders;
    }

    /**
     * @param context the context to use to get context dependent parameters.
     * @return the parameters encoded in the query string. This has been wrapped in a call to
     *         {@link Collections#unmodifiableMap(Map)}.
     */
    @NonNull
    public Map<String, String> getQueryParams(@NonNull final Context context) {
        return mQueryParams;
    }

    /**
     * @param context Application context.
     * @return the base URL String.
     * @throws BadRequestException if the request is invalid.
     */
    public String getUrlString(@NonNull final Context context) throws BadRequestException {
        return mUrlString;
    }

    /**
     * @param context the context to use to get context dependent parameters
     * @return the final {@link URL} to request, including query string parameters.
     * @throws BadRequestException if the request is invalid.
     */
    @NonNull
    public final URL getUrl(@NonNull final Context context) throws BadRequestException {
        URL url = null;
        final Map<String, String> queryParams = getQueryParams(context);
        final Uri.Builder builder = Uri.parse(getUrlString(context)).buildUpon();
        /*
         * Sort the query params by their keys, this is not part of the public interface and is
         * subject to change. We do this for testing purposes.
         */
        final Set<String> keys = new TreeSet<String>(queryParams.keySet());

        for (final String key : keys) {
            builder.appendQueryParameter(key, queryParams.get(key));
        }

        try {
            url = new URL(builder.build().toString());
        } catch (final MalformedURLException e) {
            LogManager.e("MalformedUrlException when getting request url", e); //$NON-NLS-1$
        }

        return url;
    }

    /**
     * @return the {@link HttpMethod} for the request
     */
    public final HttpMethod getMethod() {
        return mMethod;
    }

    /**
     * Subclasses must implement this write the POST body (if it has one) to the
     * {@link OutputStream} passed.
     *
     * @param context the Application context.
     * @param stream the {@link OutputStream} to write the POST body to.
     * @throws IOException if writing to the {@link OutputStream} fails
     */
    public abstract void writeBodyToStream(@NonNull final Context context,
            @NonNull final OutputStream stream) throws IOException;

    /**
     * @param context the Application context.
     * @return the length of the request body if there is one.
     */
    public abstract int getBodyLength(@NonNull final Context context);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((null == mMethod) ? 0 : mMethod.hashCode());
        result = prime * result + ((null == mQueryParams) ? 0 : mQueryParams.hashCode());
        result = prime * result + ((null == mRequestHeaders) ? 0 : mRequestHeaders.hashCode());
        result = prime * result + ((null == mUrlString) ? 0 : mUrlString.hashCode());
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

        if (!(obj instanceof AbstractRequest)) {
            return false;
        }

        final AbstractRequest other = (AbstractRequest) obj;
        if (mMethod != other.mMethod) {
            return false;
        }

        if (null == mQueryParams) {
            if (null != other.mQueryParams) {
                return false;
            }
        } else if (!mQueryParams.equals(other.mQueryParams)) {
            return false;
        }

        if (null == mRequestHeaders) {
            if (null != other.mRequestHeaders) {
                return false;
            }
        } else if (!mRequestHeaders.equals(other.mRequestHeaders)) {
            return false;
        }

        if (null == mUrlString) {
            if (null != other.mUrlString) {
                return false;
            }
        } else if (!mUrlString.equals(other.mUrlString)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "AbstractRequest [mMethod=%s, mUrlString=%s, mRequestHeaders=%s, mQueryParams=%s]", //$NON-NLS-1$
                mMethod, mUrlString, mRequestHeaders, mQueryParams);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mMethod.name());
        dest.writeString(mUrlString);
        dest.writeMap(mRequestHeaders);
        dest.writeMap(mQueryParams);
    }

    /**
     * Exception that is thrown if a request is invalid at the time it it sent.
     */
    public static final class BadRequestException extends Exception {

        /**
         * Implements {@link Serializable}.
         */
        private static final long serialVersionUID = 8423248708984803306L;

        /**
         * Constructor.
         *
         * @param detailMessage the message to display in the backtrace.
         */
        public BadRequestException(final String detailMessage) {
            super(detailMessage);
        }
    }
}
