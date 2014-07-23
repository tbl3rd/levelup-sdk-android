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

import net.jcip.annotations.Immutable;

/**
 * Factory for creating {@link AbstractRequest}'s to interact with
 * {@link com.scvngr.levelup.core.model.PaymentToken}s on the web service.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class PaymentTokenRequestFactory extends AbstractRequestFactory {

    @NonNull
    private static final String ENDPOINT = "payment_token";

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public PaymentTokenRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * @return an {@link AbstractRequest} to use to get the currently logged in
     *         {@link com.scvngr.levelup.core.model.User}'s
     *         {@link com.scvngr.levelup.core.model.PaymentToken}.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_READ_QR_CODE)
    public AbstractRequest buildGetPaymentTokenRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, ENDPOINT, null, null,
                getAccessTokenRetriever());
    }
}
