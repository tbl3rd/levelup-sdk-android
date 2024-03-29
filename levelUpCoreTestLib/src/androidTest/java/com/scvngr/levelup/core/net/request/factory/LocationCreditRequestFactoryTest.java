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

import com.scvngr.levelup.core.model.LocationFixture;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.URL;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.LocationCreditRequestFactory}.
 */
public final class LocationCreditRequestFactoryTest extends SupportAndroidTestCase {

    private static int LOCATION_ID_FIXTURE = 100;

    /**
     * Tests building a location credit request.
     */
    @SmallTest
    public void testBuildLocationCreditRequest_noToken()
            throws AbstractRequest.BadRequestException, IOException {
        final LocationCreditRequestFactory builder =
                new LocationCreditRequestFactory(getContext(), new MockAccessTokenRetriever(null));
        final AbstractRequest request = builder.buildLocationCreditRequest(
                LocationFixture.getFullModel(LOCATION_ID_FIXTURE));

        assertNotNull(request);
        final URL url = request.getUrl(getContext());
        assertTrue("hits /locations/:id/credit endpoint",
                url.getPath().contains("/locations/"));
        assertTrue("hits /locations/:id/credit endpoint",
                url.getPath().contains("/credit"));
        assertFalse(request.getRequestHeaders(getContext())
                .containsKey(LevelUpRequest.HEADER_AUTHORIZATION));

        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON,
                request.getRequestHeaders(getContext()).get(HTTP.CONTENT_TYPE));

        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue(url.getPath().startsWith("/v15"));
    }

    /**
     * Tests building a location credit request.
     */
    @SmallTest
    public void testBuildLocationCreditRequest_withToken()
            throws AbstractRequest.BadRequestException, IOException {
        final LocationCreditRequestFactory builder =
                new LocationCreditRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.buildLocationCreditRequest(
                LocationFixture.getFullModel(LOCATION_ID_FIXTURE));

        assertNotNull(request);
        final URL url = request.getUrl(getContext());
        assertTrue("hits /locations/:id/credit endpoint",
                url.getPath().contains("/locations/"));
        assertTrue("hits /locations/:id/credit endpoint",
                url.getPath().contains("/credit"));
        assertTrue(request.getRequestHeaders(getContext())
                .containsKey(LevelUpRequest.HEADER_AUTHORIZATION));

        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON,
                request.getRequestHeaders(getContext()).get(HTTP.CONTENT_TYPE));

        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue(url.getPath().startsWith("/v15"));
    }
}
