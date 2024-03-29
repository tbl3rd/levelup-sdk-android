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

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.RequiresPermission;
import com.scvngr.levelup.core.model.Interstitial;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.DeviceUtil;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Request factory for dealing with {@link Interstitial}s.
 */
@LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
public final class InterstitialRequestFactory extends AbstractRequestFactory {

    /**
     * @param context the Application context.
     * @param retriever the {@link AccessTokenRetriever} to use to retrieve the
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public InterstitialRequestFactory(@NonNull final Context context,
            @Nullable final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to get the {@link Interstitial} for the order passed.
     *
     * @param orderUuid the UUID of the {@link com.scvngr.levelup.core.model.Order} @see
     *        {@link com.scvngr.levelup.core.model.Order#getUuid()}
     * @return an {@link AbstractRequest} to use to get the {@link Interstitial} for the
     *         {@link com.scvngr.levelup.core.model.Order}.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
    @RequiresPermission({Permissions.PERMISSION_CREATE_ORDERS, Permissions.PERMISSION_READ_QR_CODE})
    public AbstractRequest buildInterstitialForOrderRequest(@NonNull final String orderUuid) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format(
                        "orders/%s/interstitial", orderUuid), null, null,
                getAccessTokenRetriever());
    }

    /**
     * Build a request to get the image for the {@link Interstitial} passed.
     *
     * @param interstitial the {@link Interstitial} to get the image URL for.
     * @return an {@link AbstractRequest} to use to get the image for the {@link Interstitial}
     *         passed.
     */
    @NonNull
    @LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
    public AbstractRequest buildInterstitialImageRequest(@NonNull final Interstitial interstitial) {
        final String deviceDensity = DeviceUtil.getDeviceDensityString(getContext());
        final Uri.Builder builder = Uri.parse(interstitial.getImageUrl()).buildUpon();
        builder.appendQueryParameter(PARAM_DENSITY, deviceDensity);
        builder.appendQueryParameter(PARAM_WIDTH, DEFAULT_WIDTH);
        builder.appendQueryParameter(PARAM_HEIGHT, DEFAULT_HEIGHT);

        return new LevelUpRequest(getContext(), HttpMethod.GET, NullUtils.nonNullContract(builder
                .build()), null, getAccessTokenRetriever());
    }
}
