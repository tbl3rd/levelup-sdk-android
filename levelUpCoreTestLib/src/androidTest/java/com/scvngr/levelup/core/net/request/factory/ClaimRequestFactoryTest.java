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

    private static final String SAMPLE_URL_CODE = "https://www.staging-levelup.com/c/3T5JPD44GZ";
    private static final String SAMPLE_CODE = "abc123";

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
        final String loyaltyId = "2222";
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
        assertEquals("/v15/loyalties/legacy/1/claims", url.getPath());
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
                String.format(Locale.US, "/v15/codes/%s/claims", SAMPLE_CODE);
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

        final String encodedCode = URLEncoder.encode(SAMPLE_URL_CODE, "UTF-8");
        // Make sure we hit the proper API version and url.
        final String expectedUrl =
                String.format(Locale.US, "/v15/codes/%s/claims", encodedCode);
        assertEquals(expectedUrl, url.getPath());
    }
}
