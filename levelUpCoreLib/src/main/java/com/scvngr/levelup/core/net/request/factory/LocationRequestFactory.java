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
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory.PageCacheRetriever;
import com.scvngr.levelup.core.util.DeviceUtil;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.HashMap;

/**
 * Factory to build requests to interact with the {@link com.scvngr.levelup.core.model.Location}
 * related web service endpoints.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class LocationRequestFactory extends AbstractRequestFactory {

    /**
     * @param context the Application context.
     */
    public LocationRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * @param appId the ID of the App on the web service.
     * @param location the location of the user. Used to sort the returned locations by distance.
     * @return the list of locations associated with the app.
     */
    @Deprecated
    @NonNull
    public AbstractRequest buildGetAppLocationsListRequest(final long appId,
            @Nullable final android.location.Location location) {

        final PageCacheRetriever pageCacheRetriever = new PageCacheRetriever() {

            @Override
            public void setNextPage(@NonNull final String pageKey, @Nullable final Uri page) {
                // Do Nothing
            }

            @Override
            @Nullable
            public Uri getNextPageUrl(@NonNull final String pageKey) {
                return null;
            }
        };

        final AppLocationListRequestFactory factory =
                getFactoryForLocationList(appId, location, pageCacheRetriever);

        return factory.getFirstPageRequest();
    }

    @SuppressWarnings("null")
    private AppLocationListRequestFactory getFactoryForLocationList(final long appId,
            @Nullable final android.location.Location location,
            @NonNull final PageCacheRetriever pageCacheRetriever) {
        final AppLocationListRequestFactory factory =
                new AppLocationListRequestFactory(getContext(), null, pageCacheRetriever, appId,
                        location);
        return factory;
    }

    /**
     * @param locationWebServiceId the ID of the {@link com.scvngr.levelup.core.model.Location} on
     *        the web service.
     * @return an {@link AbstractRequest} that represents a request to the web service to get the
     *         details of the {@link com.scvngr.levelup.core.model.Location} with the ID passed.
     */
    @NonNull
    public AbstractRequest buildGetLocationDetailsRequest(final long locationWebServiceId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, NullUtils.format(
                        "locations/%d", locationWebServiceId), null, null);
    }

    /**
     * @param context the Application context.
     * @param locationWebServiceId the web service ID of the
     *        {@link com.scvngr.levelup.core.model.Location} to get the image for.
     * @return an {@link AbstractRequest} that represents a request to the web service to get the
     *         image for a {@link com.scvngr.levelup.core.model.Location} with the web service ID
     *         passed.
     */
    @NonNull
    public AbstractRequest buildGetLocationImageRequest(@NonNull final Context context,
            final long locationWebServiceId) {
        final String deviceDensity = DeviceUtil.getDeviceDensityString(context);
        final HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(PARAM_DENSITY, deviceDensity);
        queryParams.put(PARAM_WIDTH, DEFAULT_WIDTH);
        queryParams.put(PARAM_HEIGHT, DEFAULT_HEIGHT);
        return new LevelUpRequest(context, HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V14,
                NullUtils.format("locations/%d/image", locationWebServiceId), queryParams, null);
    }
}
