/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
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
                        "merchants/%d/loyalty", merchantWebServiceId), null, null, //$NON-NLS-1$
                getAccessTokenRetriever());
    }
}
