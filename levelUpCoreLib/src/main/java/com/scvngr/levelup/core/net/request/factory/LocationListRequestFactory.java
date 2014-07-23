/*
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
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.storage.CorePreferenceUtil;
import com.scvngr.levelup.core.util.LogManager;

import net.jcip.annotations.Immutable;

/**
 * A summarized list of all LevelUp locations. This list is paged using the Link header. See
 * {@link com.scvngr.levelup.core.util.WebLinkParser}.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class LocationListRequestFactory extends AbstractPagingRequestFactory {

    /**
     * The API endpoint for locations.
     */
    @NonNull
    public static final String ENDPOINT_LOCATIONS = "locations";

    /**
     * Constructor.
     *
     * @param context the context.
     * @param retriever the access token retriever.
     * @param pageCacheRetriever the retriever to use to get the last retrieved page from the cache.
     */
    public LocationListRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever,
            @NonNull final PageCacheRetriever pageCacheRetriever) {
        super(context, retriever, pageCacheRetriever, CorePreferenceUtil
                .KEY_STRING_LAST_LOCATION_PAGE);
    }

    @Override
    @Nullable
    public AbstractRequest getPageRequest(@NonNull final Uri page) {
        try {
            return new LevelUpRequest(getContext(), HttpMethod.GET, page, null,
                    getAccessTokenRetriever());
        } catch (final IllegalArgumentException e) {
            LogManager.e("error parsing URL for next page", e);
            return null;
        }
    }

    @Override
    @NonNull
    public AbstractRequest getFirstPageRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V14,
                ENDPOINT_LOCATIONS, null, null, getAccessTokenRetriever());
    }
}
