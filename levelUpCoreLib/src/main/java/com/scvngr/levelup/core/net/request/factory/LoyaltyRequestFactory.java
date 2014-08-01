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
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.RequiresPermission;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

/**
 * Factory for creating {@link AbstractRequest}s for interacting with
 * {@link com.scvngr.levelup.core.model.Loyalty} on the web service.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class LoyaltyRequestFactory extends AbstractRequestFactory {

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public LoyaltyRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * @param merchantWebServiceId the web service ID of the merchant to load the
     *        {@link com.scvngr.levelup.core.model.Loyalty} for.
     * @return an {@link AbstractRequest} to get the {@link com.scvngr.levelup.core.model.Loyalty}
     *         for the current user at the merchant.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_MANAGE_USER_CAMPAIGNS)
    @AccessTokenRequired
    public AbstractRequest buildGetLoyaltyForMerchantRequest(final long merchantWebServiceId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format(
                        "merchants/%d/loyalty", merchantWebServiceId), null, null,
                getAccessTokenRetriever());
    }
}
