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
import com.scvngr.levelup.core.model.Feedback;
import com.scvngr.levelup.core.model.factory.json.FeedbackJsonFactory;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.JsonElementRequestBody;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.Locale;

/**
 * Class to build requests to interact with the feedback endpoint.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class FeedbackRequestFactory extends AbstractRequestFactory {

    /**
     * The endpoint for a given order.
     */
    @NonNull
    private static final String ENDPOINT_FEEDBACK_FORMAT = "orders/%s/feedback";

    /**
     * Constructor.
     *
     * @param context Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public FeedbackRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Builds a request for posting feedback for a given order.
     *
     * @param orderUuid The order's UUID for the endpoint's URL.
     * @param feedback The feedback to post in the body of this request.
     * @return Request to post the feedback for an order.
     */
    @NonNull
    @LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
    @RequiresPermission({Permissions.PERMISSION_CREATE_ORDERS, Permissions.PERMISSION_READ_QR_CODE})
    @AccessTokenRequired
    public AbstractRequest buildFeedbackRequest(@NonNull final String orderUuid,
            @NonNull final Feedback feedback) {
        return new LevelUpRequest(getContext(), NullUtils.nonNullContract(HttpMethod.POST),
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.nonNullContract(String.format(
                        Locale.US, ENDPOINT_FEEDBACK_FORMAT, orderUuid)), null,
                new JsonElementRequestBody(new FeedbackJsonFactory().toJsonElement(feedback)),
                getAccessTokenRetriever());
    }
}
