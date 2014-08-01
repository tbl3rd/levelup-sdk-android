/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.storage.CorePreferenceUtil;
import com.scvngr.levelup.core.util.LogManager;

import net.jcip.annotations.Immutable;

/**
 * A summarized list of all LevelUp locations. This list is paged using the Link header. See
 * {@link com.scvngr.levelup.core.util.LinkHeaderParser}.
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
