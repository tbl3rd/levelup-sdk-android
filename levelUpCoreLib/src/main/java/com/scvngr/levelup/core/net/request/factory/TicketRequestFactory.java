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
