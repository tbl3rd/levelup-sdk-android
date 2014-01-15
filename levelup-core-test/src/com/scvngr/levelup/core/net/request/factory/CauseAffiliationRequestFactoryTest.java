/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpV13Request;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link CauseAffiliationRequestFactory}.
 */
public final class CauseAffiliationRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final CauseAffiliationRequestFactory factory =
                new CauseAffiliationRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildCauseAffiliationRequest() throws BadRequestException {
        final LevelUpV13Request request =
                (LevelUpV13Request) new CauseAffiliationRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildGetCauseAffiliation();

        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue("hits cause_affiliation.json endpoint", request.getUrl(mContext).getPath() //$NON-NLS-1$
                .endsWith("cause_affiliation.json")); //$NON-NLS-1$
        assertTrue(request.getQueryParams(getContext()).containsKey("access_token")); //$NON-NLS-1$
    }

    @SmallTest
    public void testBuildUpdateCauseAffiliation() throws BadRequestException {
        final long id = 0;
        final double percentDonation = 0.3;
        final LevelUpV13Request request =
                (LevelUpV13Request) new CauseAffiliationRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildCreateCauseAffiliation(id,
                        percentDonation);

        assertNotNull(request);
        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue("hits cause_affiliation.json endpoint", request.getUrl(mContext).getPath() //$NON-NLS-1$
                .endsWith("cause_affiliation.json")); //$NON-NLS-1$
        assertTrue(request.getQueryParams(getContext()).containsKey("access_token")); //$NON-NLS-1$

        assertTrue(request.getPostParams().containsKey(
                CauseAffiliationRequestFactory.PARAM_CAUSE_ID));
        assertEquals(Long.toString(id),
                request.getPostParams().get(CauseAffiliationRequestFactory.PARAM_CAUSE_ID));
        assertTrue(request.getPostParams().containsKey(
                CauseAffiliationRequestFactory.PARAM_PERCENT_DONATION));
        assertEquals(Double.toString(percentDonation),
                request.getPostParams().get(CauseAffiliationRequestFactory.PARAM_PERCENT_DONATION));
    }

    @SmallTest
    public void testBuildDeleteCauseAfiliation() throws BadRequestException {
        final LevelUpV13Request request =
                (LevelUpV13Request) new CauseAffiliationRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildDeleteCauseAffiliation();

        assertNotNull(request);
        assertEquals(HttpMethod.DELETE, request.getMethod());
        assertTrue("hits cause_affiliation.json endpoint", request.getUrl(mContext).getPath() //$NON-NLS-1$
                .endsWith("cause_affiliation.json")); //$NON-NLS-1$
        assertTrue(request.getQueryParams(getContext()).containsKey("access_token")); //$NON-NLS-1$
    }
}
