/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import net.jcip.annotations.Immutable;

import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.PaymentToken;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;

/**
 * Factory for creating {@link AbstractRequest}'s to interact with {@link PaymentToken}s on the web
 * service.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class PaymentTokenRequestFactory extends AbstractRequestFactory {

    /**
     * @param context the Application context.
     * @param retriever the {@link AccessTokenRetriever} to use to get the {@link User}'s
     *        {@link AccessToken}.
     */
    public PaymentTokenRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * @return an {@link AbstractRequest} to use to get the currently logged in {@link User}'s
     *         {@link PaymentToken}.
     */
    @NonNull
    @AccessTokenRequired
    public AbstractRequest buildGetPaymentTokenRequest() {
        // TODO: change to "payment_token".
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, "payment_token", null, //$NON-NLS-1$
                (JSONObject) null, getAccessTokenRetriever());
    }
}
