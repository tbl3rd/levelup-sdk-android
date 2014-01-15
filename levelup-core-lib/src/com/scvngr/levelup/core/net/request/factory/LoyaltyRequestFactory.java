/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.util.Locale;

import net.jcip.annotations.Immutable;

import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.Loyalty;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;

/**
 * Factory for creating {@link AbstractRequest}s for interacting with {@link Loyalty} on the web
 * service.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class LoyaltyRequestFactory extends AbstractRequestFactory {

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link User}'s {@link AccessToken}.
     */
    public LoyaltyRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * @param merchantWebServiceId the web service ID of the merchant to load the {@link Loyalty}
     *        for.
     * @return an {@link AbstractRequest} to get the {@link Loyalty} for the current user at the
     *         merchant.
     */
    @NonNull
    public AbstractRequest buildGetLoyaltyForMerchantRequest(final long merchantWebServiceId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "merchants/%d/loyalty", merchantWebServiceId), null, (JSONObject) null, //$NON-NLS-1$
                getAccessTokenRetriever());
    }
}
