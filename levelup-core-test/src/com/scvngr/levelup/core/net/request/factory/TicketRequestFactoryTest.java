/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
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
 * Tests {@link TicketRequestFactory}.
 */
public final class TicketRequestFactoryTest extends SupportAndroidTestCase {
    @NonNull
    private static final String MESSAGE_FIXTURE = "the tomato is an interesting fruit"; //$NON-NLS-1$

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
     * @throws BadRequestException on test failure.
     * @throws IOException on test failure.
     */
    @SmallTest
    public void testBuildSupportRequest() throws BadRequestException, IOException {
        final TicketRequestFactory builder =
                new TicketRequestFactory(getContext(), new MockAccessTokenRetriever());
        final AbstractRequest request = builder.buildSupportRequest(MESSAGE_FIXTURE);

        assertNotNull(request);
        final URL url = request.getUrl(getContext());
        assertTrue("hits /tickets endpoint", url.getPath() //$NON-NLS-1$
                .endsWith("/tickets")); //$NON-NLS-1$
        assertTrue(request.getRequestHeaders(getContext()).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));

        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON, request.getRequestHeaders(getContext())
                .get(HTTP.CONTENT_TYPE));

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(url.getPath().startsWith("/v14")); //$NON-NLS-1$

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            request.writeBodyToStream(getContext(), baos);
            assertTrue("Has expected content", baos.toString().contains(MESSAGE_FIXTURE)); //$NON-NLS-1$
        } finally {
            baos.close();
        }
    }
}
