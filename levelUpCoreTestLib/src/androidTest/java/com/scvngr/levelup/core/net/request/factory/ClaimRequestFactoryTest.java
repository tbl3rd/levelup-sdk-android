/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.ClaimRequestFactory}.
 */
public final class ClaimRequestFactoryTest extends SupportAndroidTestCase {

    private static final String SAMPLE_URL_CODE = "https://www.staging-levelup.com/c/3T5JPD44GZ"; //$NON-NLS-1$
    private static final String SAMPLE_CODE = "abc123"; //$NON-NLS-1$

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final ClaimRequestFactory factory = new ClaimRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildClaimLegacyLoyaltyRequest() throws BadRequestException, JSONException {
        final String loyaltyId = "2222"; //$NON-NLS-1$
        final LevelUpRequest request =
                (LevelUpRequest) new ClaimRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildClaimLegacyLoyaltyRequest(1, loyaltyId);

        assertEquals(HttpMethod.POST, request.getMethod());
        final JSONObject object =
                new JSONObject(request.getBody(getContext()))
                        .getJSONObject(ClaimRequestFactory.OUTER_PARAM_LEGACY_LOYALTY);
        assertTrue(object.has(ClaimRequestFactory.PARAM_LEGACY_ID));
        assertEquals(loyaltyId, object.get(ClaimRequestFactory.PARAM_LEGACY_ID));
        assertFalse(0 == request.getBodyLength(getContext()));

        final URL url = request.getUrl(getContext());
        assertNotNull(url);
        // Make sure we hit the proper API version and url.
        assertEquals("/v14/loyalties/legacy/1/claims", url.getPath()); //$NON-NLS-1$
    }

    @SmallTest
    public void testBuildClaimRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new ClaimRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildClaimCampaignRequest(SAMPLE_CODE);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals(0, request.getBodyLength(getContext()));
        final URL url = request.getUrl(getContext());
        assertNotNull(url);
        // Make sure we hit the proper API version and url.
        final String expectedUrl =
                String.format(Locale.US, "/v14/codes/%s/claims", SAMPLE_CODE); //$NON-NLS-1$
        assertEquals(expectedUrl, url.getPath());
    }

    @SmallTest
    public void testBuildClaimRequest_withUrlCode() throws BadRequestException, UnsupportedEncodingException {
        final LevelUpRequest request =
                (LevelUpRequest) new ClaimRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildClaimCampaignRequest(SAMPLE_URL_CODE);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals(0, request.getBodyLength(getContext()));
        final URL url = request.getUrl(getContext());
        assertNotNull(url);

        final String encodedCode = URLEncoder.encode(SAMPLE_URL_CODE, "UTF-8"); //$NON-NLS-1$
        // Make sure we hit the proper API version and url.
        final String expectedUrl =
                String.format(Locale.US, "/v14/codes/%s/claims", encodedCode); //$NON-NLS-1$
        assertEquals(expectedUrl, url.getPath());
    }
}
