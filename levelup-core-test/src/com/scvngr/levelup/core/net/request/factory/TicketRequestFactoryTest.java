/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.LevelUpV13RequestWithCurrentUser;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.request.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.net.URL;
import java.util.Locale;

/**
 * Tests {@link TicketRequestFactory}.
 */
public final class TicketRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final TicketRequestFactory factory = new TicketRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildSupportRequest() throws BadRequestException {
        final TicketRequestFactory builder =
                new TicketRequestFactory(getContext(), new MockAccessTokenRetriever());
        final LevelUpV13RequestWithCurrentUser request =
                (LevelUpV13RequestWithCurrentUser) builder
                        .buildSupportRequest("the tomato is an interesting fruit"); //$NON-NLS-1$

        assertNotNull(request);
        final URL url = request.getUrl(getContext());
        assertTrue("hits users/<id>/tickets.json endpoint", url.getPath() //$NON-NLS-1$
                .endsWith(String.format(Locale.US, "users/%d/tickets.json", 1))); //$NON-NLS-1$
        assertNotNull("request has query params", url.getQuery()); //$NON-NLS-1$
        assertTrue("request query has accessToken", url.getQuery().contains( //$NON-NLS-1$
                "access_token=test_access_token")); //$NON-NLS-1$

        assertEquals(1, request.getPostParams().keySet().size());
        assertTrue(request.getPostParams().containsKey(
                RequestUtils.getNestedParameterKey(TicketRequestFactory.OUTER_PARAM_TICKET,
                        TicketRequestFactory.PARAM_BODY)));
        assertTrue(request.getPostParams().containsValue("the tomato is an interesting fruit")); //$NON-NLS-1$
    }
}
