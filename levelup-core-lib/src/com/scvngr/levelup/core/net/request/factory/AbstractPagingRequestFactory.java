/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.net.Uri;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;

/**
 * <p>
 * A request factory that can page through requests. This is used with endpoints that support the
 * Link header, as noted in {@link com.scvngr.levelup.core.util.WebLinkParser}. This will save the
 * next page URL in a shared preference so that it can be resumed from that point forward.
 * </p>
 *
 * <p>
 * To use, subclass and implement methods to retrieve the first page ({@link #getFirstPageRequest()}
 * ) and to construct a request for an arbitrary page from a URL ({@link #getPageRequest(Uri)}).
 * Once a page has been loaded successfully, {@link #setNextPage(Uri)} should be called with the URL
 * contained in the Link header.
 * </p>
 */
@LevelUpApi(contract = Contract.INTERNAL)
public abstract class AbstractPagingRequestFactory extends AbstractRequestFactory {

    @NonNull
    private final PageCacheRetriever mPageCacheRetriever;

    @NonNull
    private final String mSavedPageKey;

    /**
     * @param context the context.
     * @param retriever an access token retriever.
     * @param pageCacheRetriever a page cache retriever.
     * @param savedPageKey a key that's unique for the endpoint that this request factory is for.
     *        This should be full namespaced to ensure its uniqueness.
     */
    public AbstractPagingRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever,
            @NonNull final PageCacheRetriever pageCacheRetriever, @NonNull final String savedPageKey) {
        super(context, retriever);
        mSavedPageKey = savedPageKey;
        mPageCacheRetriever = pageCacheRetriever;
    }

    /**
     * Subclasses must implement this to construct a new request for the first page. This is usually
     * just a request to the endpoint.
     *
     * @return a request for the first page.
     */
    @NonNull
    public abstract AbstractRequest getFirstPageRequest();

    /**
     * <p>
     * Gets the next page. If no pages have been loaded before, this returns the result of
     * {@link #getFirstPageRequest()}, otherwise it loads a request for the next page with
     * {@link #getPageRequest(Uri)}.
     * </p>
     *
     * <p>
     * This must not be called from the UI thread.
     * </p>
     *
     * @return a request for the next page that should be loaded.
     */
    @NonNull
    public final AbstractRequest getNextPageRequest() {
        AbstractRequest response;

        final Uri nextPage = mPageCacheRetriever.getNextPageUrl(mSavedPageKey);

        if (null == nextPage) {
            response = getFirstPageRequest();
        } else {
            response = getPageRequest(nextPage);
            if (null == response) {
                response = getFirstPageRequest();
            }
        }

        return response;
    }

    /**
     * Subclasses must implement this to construct a new request for the given next page.
     *
     * @param page the URL of the next page that should be loaded.
     * @return a request for the next page or null if there is an error.
     */
    @Nullable
    public abstract AbstractRequest getPageRequest(@NonNull final Uri page);

    /**
     * <p>
     * Sets the next page. This is the URL returned in the Link header for each page that has been
     * successfully loaded into cache.
     * </p>
     *
     * <p>
     * This may be called from the UI thread.
     * </p>
     *
     * @param page the URL as returned by the Link header of the response or null to clear.
     */
    public final void setNextPage(@Nullable final Uri page) {
        mPageCacheRetriever.setNextPage(mSavedPageKey, page);
    }

    /**
     * @return the page cache retriever.
     */
    @NonNull
    protected PageCacheRetriever getPageCacheRetriever() {
        return mPageCacheRetriever;
    }

    /**
     * Implement this to support persisting the URL that points to the next page of information.
     */
    public interface PageCacheRetriever {
        /**
         * <p>
         * Retrieves the next page for the given key.
         * </p>
         *
         * <p>
         * Classes implementing this method must retrieve the URI associated with the provided key.
         * This will not be called from the UI thread.
         * </p>
         *
         * @return the next page of content that should be loaded or {@code null} if none have been
         *         set yet for this key.
         * @param pageKey The key under which the page URI will be stored. This must be unique in
         *        the context of the application and be fully namespaced.
         */
        @Nullable
        Uri getNextPageUrl(@NonNull String pageKey);

        /**
         * <p>
         * Sets the next page. This is the URL returned in the Link header for each page that has
         * been successfully loaded into cache.
         * </p>
         *
         * <p>
         * Classes implementing this method must persist the provided URI so that it may be
         * retrieved by the given key. This may be called from the UI thread.
         * </p>
         *
         * @param pageKey The key under which the page URI will be stored. This must be unique in the
         *        context of the application and be fully namespaced.
         * @param page the URL as returned by the Link header of the response or null to clear.
         */
        void setNextPage(@NonNull String pageKey, @Nullable final Uri page);
    }
}
