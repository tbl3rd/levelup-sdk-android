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
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to build requests to interact with the order endpoint.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class OrderRequestFactory extends AbstractRequestFactory {
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_PAGE = "page";

    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_MERCHANT_IDS = "merchant_ids";

    /**
     * The endpoint for the app-centric view of orders.
     */
    @NonNull
    private static final String ENDPOINT_APPS_ORDERS = "apps/orders";

    /**
     * The v14 endpoint for the app-centric view of orders.
     */
    private static final String ENDPOINT_APPS_ID_ORDERS_FORMAT = "apps/%d/orders";

    /**
     * The endpoint for a given order.
     */
    @NonNull
    private static final String ENDPOINT_ORDERS_UUID_FORMAT = "orders/%s";

    /**
     * Constructor.
     *
     * @param context Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public OrderRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Builds a request to get a list of orders for the current user.
     *
     * @param page the "page" of results to get because the endpoint supports pagination. The
     *        expected first page from the web service starts at 1. The web service will return an
     *        empty set if there are no more results to show
     * @return Request to get a list of orders for the current user.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_READ_USER_ORDERS)
    @AccessTokenRequired
    public AbstractRequest newGetAppOrdersRequest(final int page) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_PAGE, Integer.toString(page));

        return new LevelUpRequest(getContext(), HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V15,
                ENDPOINT_APPS_ORDERS, params, null, getAccessTokenRetriever());
    }

    /**
     * Builds a request to get a single order.
     *
     * @param orderUuid the UUID of the order to request.
     * @return Request to get a single order.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_READ_USER_ORDERS)
    @AccessTokenRequired
    public AbstractRequest newGetOrderRequest(@NonNull final String orderUuid) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format(ENDPOINT_ORDERS_UUID_FORMAT,
                        orderUuid), null, null, getAccessTokenRetriever());
    }

    /**
     * Builds a request to get a list of orders for the current user. Replaced by {@link
     * #newGetAppOrdersRequest(int)}.
     *
     * @param appId the ID of the app whose orders will be displayed.
     * @param page the "page" of results to get because the endpoint supports pagination. The
     *        expected first page from the web service starts at 1. The web service will return an
     *        empty set if there are no more results to show
     * @return Request to get a list of orders for the current user.
     */
    @NonNull
    @AccessTokenRequired
    @Deprecated
    @LevelUpApi(contract = Contract.INTERNAL)
    public AbstractRequest newGetAppOrdersRequest(final long appId, final int page) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_PAGE, Integer.toString(page));

        return new LevelUpRequest(getContext(), HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V14,
                NullUtils.format(ENDPOINT_APPS_ID_ORDERS_FORMAT, appId), params, null,
                getAccessTokenRetriever());
    }
}
