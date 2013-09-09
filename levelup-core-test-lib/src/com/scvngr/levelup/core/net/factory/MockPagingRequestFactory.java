package com.scvngr.levelup.core.net.factory;

import android.content.Context;
import android.net.Uri;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpConnection;
import com.scvngr.levelup.core.net.LevelUpConnectionHelper;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * A concrete implementation of {@link AbstractPagingRequestFactory} which returns a
 * {@link LevelUpRequest} with the specified URL (or {@link #PAGE_1_URL} for the first
 * page) and empty content.
 * </p>
 *
 * <p>
 * This has three fake pages, whose responses can be set with
 * {@link #setNextResponsePage(Context, String)}.
 * </p>
 * <p>
 * Pages:
 * </p>
 * <ol>
 * <li>{@link #PAGE_1_URL} This page has an HTTP OK status, a link to page 2, and no real content.</li>
 * <li>{@link #PAGE_2_URL} This page has an HTTP OK status, a link to page 3, and no real content.</li>
 * <li>{@link #PAGE_3_URL} This page has an HTTP NO_CONTENT status and no real content.</li>
 * </ol>
 */
public final class MockPagingRequestFactory extends AbstractPagingRequestFactory {

    /**
     * The initial page URL. This has a Link header to page 2.
     */
    public static final String PAGE_1_URL = "http://example.com/"; //$NON-NLS-1$

    /**
     * The second page URL. This has a Link header to page 3.
     */
    public static final String PAGE_2_URL = "http://example.com/?page=2"; //$NON-NLS-1$

    /**
     * The third page URL. This has no Link header and is empty.
     */
    public static final String PAGE_3_URL = "http://example.com/?page=3"; //$NON-NLS-1$

    private static final String EMPTY_DATA = ""; //$NON-NLS-1$

    /**
     * @param context application context.
     * @param retriever the access token retriever.
     * @param pageCacheRetriever the page cache retriever.
     * @param savedPageKey the key to save the page under in the {@code pageCacheRetriever}.
     */
    public MockPagingRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever,
            @NonNull final PageCacheRetriever pageCacheRetriever, @NonNull final String savedPageKey) {
        super(context, retriever, pageCacheRetriever, savedPageKey);
    }

    @Override
    @NonNull
    public AbstractRequest getFirstPageRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                Uri.parse(PAGE_1_URL), (JSONObject) null, getAccessTokenRetriever());
    }

    @Override
    @Nullable
    public AbstractRequest getPageRequest(@NonNull final Uri page) {
        return new LevelUpRequest(getContext(), HttpMethod.GET, page,
                (JSONObject) null, getAccessTokenRetriever());
    }

    /**
     * Sets the next response with {@link LevelUpConnectionHelper} to simulate the
     * given page.
     *
     * @param context application context.
     * @param pageUrl one of {@link #PAGE_1_URL}, {@link #PAGE_2_URL}, or {@link #PAGE_3_URL}.
     * @return the web service connection.
     */
    @NonNull
    public static LevelUpConnection setNextResponsePage(@NonNull final Context context,
            @NonNull final String pageUrl) {
        final LevelUpConnection connection;

        if (PAGE_1_URL.equals(pageUrl)) {
            connection =
                    LevelUpConnectionHelper.setNextResponse(context, EMPTY_DATA,
                            HttpStatus.SC_OK, getLinkHeaders(PAGE_2_URL));
        } else if (PAGE_2_URL.equals(pageUrl)) {
            connection =
                    LevelUpConnectionHelper.setNextResponse(context, EMPTY_DATA,
                            HttpStatus.SC_OK, getLinkHeaders(PAGE_3_URL));
        } else if (PAGE_3_URL.equals(pageUrl)) {
            connection =
                    LevelUpConnectionHelper.setNextResponse(context, EMPTY_DATA,
                            HttpStatus.SC_NO_CONTENT, null);
        } else {
            throw new IllegalArgumentException("URL is unknown"); //$NON-NLS-1$
        }

        return connection;
    }

    /**
     * Adds a basic {@code Link:} header pointing to the specified next page to a new
     * {@link HashMap}.
     *
     * @param nextPageUrl the URL of the next page.
     * @return a new map with the given next page added to the link header.
     */
    @NonNull
    public final static HashMap<String, List<String>> getLinkHeaders(
            @NonNull final String nextPageUrl) {
        final HashMap<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put(
                "Link", Arrays.asList(String.format(Locale.US, "<%s>;rel=\"next\"", nextPageUrl))); //$NON-NLS-1$//$NON-NLS-2$

        return headers;
    }
}
