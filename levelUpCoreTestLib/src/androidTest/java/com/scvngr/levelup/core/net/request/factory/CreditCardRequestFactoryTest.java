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

import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.model.CreditCardFixture;
import com.scvngr.levelup.core.model.factory.json.CreditCardJsonFactory;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.CreditCardRequestFactory}.
 */
public final class CreditCardRequestFactoryTest extends SupportAndroidTestCase {

    private static final String EXPECTED_CREDIT_CARDS_ENDPOINT = "credit_cards";

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final CreditCardRequestFactory factory =
                new CreditCardRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    /**
     * Tests
     * {@link com.scvngr.levelup.core.net.request.factory.CreditCardRequestFactory#buildCreateCardRequest(String, String, String, String, String)}
     * .
     */
    @SmallTest
    public void testBuildCreateCardRequest_invalidParams() {
        try {
            new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever())
                    .buildCreateCardRequest(null, "", "", "", "");
            fail("should have thrown an exception");
        } catch (final AssertionError e) {
            // Expected exception.
        }

        try {
            new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever())
                    .buildCreateCardRequest("", null, "", "", "");
            fail("should have thrown an exception");
        } catch (final AssertionError e) {
            // Expected exception.
        }

        try {
            new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever())
                    .buildCreateCardRequest("", "", null, "", "");
            fail("should have thrown an exception");
        } catch (final AssertionError e) {
            // Expected exception.
        }

        try {
            new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever())
                    .buildCreateCardRequest("", "", "", null, "");
            fail("should have thrown an exception");
        } catch (final AssertionError e) {
            // Expected exception.
        }

        try {
            new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever())
                    .buildCreateCardRequest("", "", "", "", null);
            fail("should have thrown an exception");
        } catch (final AssertionError e) {
            // Expected exception.
        }
    }

    /**
     * Tests
     * {@link com.scvngr.levelup.core.net.request.factory.CreditCardRequestFactory#buildCreateCardRequest(String, String, String, String, String)}
     * .
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException if the request throws
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testBuildCreateCardRequest() throws BadRequestException, JSONException {
        final CreditCardRequestFactory builder =
                new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.buildCreateCardRequest("card_number", "cvv",
                "01", "1999", "00000");

        final LevelUpRequest apiRequest = (LevelUpRequest) request;

        final JSONObject params = new JSONObject(apiRequest.getBody(getContext()));

        assertEquals(1, params.length());

        final JSONObject creditCard = params.getJSONObject("credit_card");

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue("hits credit_cards endpoint", request.getUrl(mContext).getPath()
                .endsWith(EXPECTED_CREDIT_CARDS_ENDPOINT));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));

        /*
         * assertFalse is used because the request parameters are encrypted, so they *shouldn't* be
         * the values we entered.
         */
        assertFalse("card_number".equals(creditCard.getString("encrypted_number")));
        assertFalse("cvv".equals(creditCard.getString("encrypted_cvv")));
        assertFalse("01".equals(creditCard.getString("encrypted_expiration_month")));
        assertFalse("1999".equals(creditCard.getString("encrypted_expiration_year")));
        assertEquals("00000", creditCard.getString("postal_code"));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.request.factory.CreditCardRequestFactory#buildGetCardsRequest()}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     */
    @SmallTest
    public void testBuildGetCardsRequest() throws BadRequestException {
        final CreditCardRequestFactory builder =
                new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.buildGetCardsRequest();

        assertNotNull(request);
        assertTrue(request instanceof LevelUpRequest);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue("hits credit_cards endpoint", request.getUrl(mContext).getPath()
                .endsWith(EXPECTED_CREDIT_CARDS_ENDPOINT));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.request.factory.CreditCardRequestFactory#buildPromoteCardRequest(com.scvngr.levelup.core.model.CreditCard)}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testBuildPromoteCardRequest() throws BadRequestException, JSONException {
        final CreditCardRequestFactory builder =
                new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever());
        final CreditCard card =
                new CreditCardJsonFactory().from(CreditCardFixture.getFullJsonObject(1, false));
        final AbstractRequest request = builder.buildPromoteCardRequest(card);

        assertNotNull(request);
        assertTrue(request instanceof LevelUpRequest);
        assertEquals(HttpMethod.PUT, request.getMethod());
        assertTrue("hits credit_cards/%d endpoint", request.getUrl(mContext).getPath()
                .endsWith(String.format(Locale.US, "%s/%d", EXPECTED_CREDIT_CARDS_ENDPOINT, 1)));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.request.factory.CreditCardRequestFactory#buildPromoteCardRequest(com.scvngr.levelup.core.model.CreditCard)}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testBuildDeleteCardRequest() throws BadRequestException, JSONException {
        final CreditCardRequestFactory builder =
                new CreditCardRequestFactory(getContext(), new MockAccessTokenRetriever());
        final CreditCard card =
                new CreditCardJsonFactory().from(CreditCardFixture.getFullJsonObject(1, false));
        final AbstractRequest request = builder.buildDeleteCardRequest(card);

        assertNotNull(request);
        assertTrue(request instanceof LevelUpRequest);
        assertEquals(HttpMethod.DELETE, request.getMethod());
        assertTrue("hits credit_cards/%d endpoint", request.getUrl(mContext).getPath()
                .endsWith(String.format(Locale.US, "%s/%d", EXPECTED_CREDIT_CARDS_ENDPOINT, 1)));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }
}
