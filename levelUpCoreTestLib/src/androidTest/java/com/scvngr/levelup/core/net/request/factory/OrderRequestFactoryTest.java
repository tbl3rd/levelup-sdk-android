/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.OrderFixture;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.OrderRequestFactory}.
 */
public final class OrderRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * App ID #23 fixture.
     */
    private static final int APP_ID_23 = 23;

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final OrderRequestFactory factory = new OrderRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testNewGetAppOrdersRequest() throws BadRequestException {
        final OrderRequestFactory builder =
                new OrderRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.newGetAppOrdersRequest(2);
        assertTrue(request instanceof LevelUpRequest);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue("hits apps/<id>/orders endpoint", request.getUrl(mContext).getPath()
                .endsWith("apps/orders"));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));

        assertTrue(request.getQueryParams(getContext()).containsKey(OrderRequestFactory.PARAM_PAGE));
        assertEquals("2", request.getQueryParams(getContext()).get(OrderRequestFactory.PARAM_PAGE));
    }

    @SmallTest
    public void testNewGetAppOrdersRequest_v14Version() throws BadRequestException {
        final OrderRequestFactory builder =
                new OrderRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.newGetAppOrdersRequest(APP_ID_23, 2);
        assertTrue(request instanceof LevelUpRequest);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue("hits apps/<id>/orders endpoint", request.getUrl(mContext).getPath()
                .endsWith(String.format(Locale.US, "apps/%d/orders", APP_ID_23)));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));

        assertTrue(request.getQueryParams(getContext()).containsKey(OrderRequestFactory.PARAM_PAGE));
        assertEquals("2", request.getQueryParams(getContext()).get(OrderRequestFactory.PARAM_PAGE));
    }

    @SmallTest
    public void testNewGetOrderRequest() throws BadRequestException {
        final OrderRequestFactory builder =
                new OrderRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.newGetOrderRequest(OrderFixture.UUID_FIXTURE_1);
        assertTrue(request instanceof LevelUpRequest);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue("hits orders/<id> endpoint", request.getUrl(mContext).getPath()
                .endsWith(String.format(Locale.US, "orders/%s", OrderFixture.UUID_FIXTURE_1)));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));
    }
}
