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

import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.TicketRequestFactory}.
 */
public final class TicketRequestFactoryTest extends SupportAndroidTestCase {
    @NonNull
    private static final String MESSAGE_FIXTURE = "the tomato is an interesting fruit";

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

    /**
     * Tests building a support request.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException on test failure.
     * @throws java.io.IOException on test failure.
     */
    @SmallTest
    public void testBuildSupportRequest() throws BadRequestException, IOException {
        final TicketRequestFactory builder =
                new TicketRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.buildSupportRequest(MESSAGE_FIXTURE);

        assertNotNull(request);
        final URL url = request.getUrl(getContext());
        assertTrue("hits /tickets endpoint", url.getPath()
                .endsWith("/tickets"));
        assertTrue(request.getRequestHeaders(getContext()).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));

        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON, request.getRequestHeaders(getContext())
                .get(HTTP.CONTENT_TYPE));

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(url.getPath().startsWith("/v14"));

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            request.writeBodyToStream(getContext(), baos);
            assertTrue("Has expected content", baos.toString().contains(MESSAGE_FIXTURE));
        } finally {
            baos.close();
        }
    }
}
