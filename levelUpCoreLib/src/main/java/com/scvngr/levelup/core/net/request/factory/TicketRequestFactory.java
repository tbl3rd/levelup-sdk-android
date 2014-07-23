/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.Ticket;
import com.scvngr.levelup.core.model.factory.json.TicketJsonFactory;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;

import net.jcip.annotations.Immutable;

/**
 * Class to build requests to interact with the endpoint that deals with support tickets.
 */
@Immutable
@LevelUpApi(contract = Contract.ENTERPRISE)
public final class TicketRequestFactory extends AbstractRequestFactory {

    /**
     * Web service endpoint.
     */
    @NonNull
    protected static final String ENDPOINT = "tickets";

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public TicketRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to submit a support request for the current user.
     *
     * @param messageBody The message to submit.
     * @return {@link AbstractRequest} representing a support ticket request.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public AbstractRequest buildSupportRequest(@NonNull final String messageBody) {
        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, ENDPOINT, null,
                new TicketJsonFactory().toRequestSerializer(new Ticket(messageBody)),
                getAccessTokenRetriever());
    }
}
