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
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.PaymentTokenRequestFactory}.
 */
public final class PaymentTokenRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final PaymentTokenRequestFactory factory =
                new PaymentTokenRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildGetPaymentTokenRequest() throws BadRequestException {
        final MockAccessTokenRetriever ret = new MockAccessTokenRetriever();
        final LevelUpRequest request =
                (LevelUpRequest) new PaymentTokenRequestFactory(getContext(), ret)
                        .buildGetPaymentTokenRequest();

        assertEquals(String.format(Locale.US, "/%s/payment_token",
                LevelUpRequest.API_VERSION_CODE_V15), request.getUrl(getContext())
                .getPath());
        final Map<String, String> headers = request.getRequestHeaders(getContext());
        assertTrue(headers.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertTrue(headers.get(LevelUpRequest.HEADER_AUTHORIZATION).contains(
                ret.getAccessToken(getContext()).getAccessToken()));
    }
}
