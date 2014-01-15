/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.jcip.annotations.Immutable;

import org.json.JSONObject;

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
import com.scvngr.levelup.core.net.LevelUpRequest;

/**
 * Class to build requests to interact with the order endpoint.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class OrderRequestFactory extends AbstractRequestFactory {
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_PAGE = "page"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_MERCHANT_IDS = "merchant_ids"; //$NON-NLS-1$

    /**
     * The endpoint for the app-centric view of orders.
     */
    private static final String ENDPOINT_APPS_ID_ORDERS_FORMAT = "apps/%d/orders"; //$NON-NLS-1$

    /**
     * The endpoint for a given order.
     */
    private static final String ENDPOINT_ORDERS_UUID_FORMAT = "orders/%s"; //$NON-NLS-1$

    /**
     * Constructor.
     *
     * @param context Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link User}'s {@link AccessToken}.
     */
    public OrderRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Builds a request to get a list of orders for the current user.
     *
     * @param appId the ID of the app whose orders will be displayed.
     * @param page the "page" of results to get because the endpoint supports pagination. The
     *        expected first page from the web service starts at 1. The web service will return an
     *        empty set if there are no more results to show
     * @return Request to get a list of orders for the current user.
     */
    @NonNull
    public AbstractRequest newGetAppOrdersRequest(final long appId, final int page) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_PAGE, Integer.toString(page));

        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        ENDPOINT_APPS_ID_ORDERS_FORMAT, appId), params, (JSONObject) null,
                getAccessTokenRetriever());
    }

    /**
     * Builds a request to get a single order.
     *
     * @param orderUuid the UUID of the order to request.
     * @return Request to get a single order.
     */
    @NonNull
    public AbstractRequest newGetOrderRequest(@NonNull final String orderUuid) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        ENDPOINT_ORDERS_UUID_FORMAT, orderUuid), null, (JSONObject) null,
                getAccessTokenRetriever());
    }
}
