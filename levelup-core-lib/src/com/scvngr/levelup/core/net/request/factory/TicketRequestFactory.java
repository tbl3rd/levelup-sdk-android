/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpV13RequestWithCurrentUser;
import com.scvngr.levelup.core.net.request.RequestUtils;

/**
 * Class to build requests to interact with the endpoints that deal with support tickets.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class TicketRequestFactory extends AbstractRequestFactory {
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final String OUTER_PARAM_TICKET = "ticket"; //$NON-NLS-1$
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final String PARAM_BODY = "body"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link User}'s {@link AccessToken}.
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
    public AbstractRequest buildSupportRequest(@NonNull final String messageBody) {
        if (null == messageBody) {
            throw new IllegalArgumentException("messageBody cannot be null"); //$NON-NLS-1$
        }

        final Map<String, String> params = new HashMap<String, String>();
        params.put(RequestUtils.getNestedParameterKey(OUTER_PARAM_TICKET, PARAM_BODY), messageBody);

        return new LevelUpV13RequestWithCurrentUser(getContext(), HttpMethod.POST, "users/%d/tickets", //$NON-NLS-1$
                null, params, getAccessTokenRetriever());
    }
}
