/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.LogManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection}.
 */
public final class LevelUpConnectionTest extends SupportAndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LevelUpConnection.setNextInstance(null);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#newInstance(android.content.Context)}.
     */
    @SmallTest
    public void testNewInstance() {
        // Make sure it returns a new object every time.
        assertNotSame(LevelUpConnection.newInstance(getContext()),
                LevelUpConnection.newInstance(getContext()));

        final LevelUpConnection connection = new LevelUpConnection(getContext());
        assertNotSame(connection, LevelUpConnection.newInstance(getContext()));

        // Make sure we return the object we set before calling new instance().
        LevelUpConnection.setNextInstance(connection);
        assertSame(connection, LevelUpConnection.newInstance(getContext()));

        // Subsequent calls should still return the same instance.
        assertSame(connection, LevelUpConnection.newInstance(getContext()));
        LevelUpConnection.setNextInstance(null);
        assertNotSame(connection, LevelUpConnection.newInstance(getContext()));
    }

    @SmallTest
    public void testGetLastRequest() {
        final LevelUpConnection connection = new LevelUpConnection(getContext());
        LevelUpConnection.setNextInstance(connection);
        final LevelUpRequest request =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, getName(), null, null);
        connection.setLastRequest(getName(), request);
        assertEquals(request, connection.getLastRequest(getName()));
        assertEquals(request, connection.getLastRequest(null));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#setNextResponse}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     */
    @SmallTest
    public void testSetNextResponse_withSpecificUrl() throws BadRequestException {
        final LevelUpConnection connection = new LevelUpConnection(getContext());
        final LevelUpRequest request =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "/", null, null);
        final LevelUpResponse response = new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER);
        LevelUpConnection.setNextInstance(connection);
        connection.setNextResponse(request.getUrl(getContext()).toString(), response);

        assertSame(connection, LevelUpConnection.newInstance(getContext()));
        assertSame(response, connection.send(request));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#setNextResponse}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     */
    @SmallTest
    public void testSetNextResponse_withNullUrl() throws BadRequestException {
        final LevelUpConnection connection = new LevelUpConnection(getContext());
        final LevelUpRequest request =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "/", null, null);
        final LevelUpResponse response = new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER);
        LevelUpConnection.setNextInstance(connection);
        connection.setNextResponse(null, response);

        assertSame(connection, LevelUpConnection.newInstance(getContext()));
        assertSame(response, connection.send(request));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#setNextResponse}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     */
    @SmallTest
    public void testSetNextResponse_withUnRecognizedUrl() throws BadRequestException {
        final LevelUpConnection connection = new LevelUpConnection(getContext());
        final LevelUpRequest request =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "/", null, null);
        final LevelUpResponse response = new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER);
        LevelUpConnection.setNextInstance(connection);
        connection.setNextResponse("www.example.com", response);

        assertSame(connection, LevelUpConnection.newInstance(getContext()));

        try {
            // Since no URL match was found, we should throw an exception.
            connection.send(request);
            fail("should throw RuntimeException");
        } catch (final RuntimeException e) {
            // Expected exception.
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#setNextResponse}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     * @throws InterruptedException
     */
    @SmallTest
    public void testSetNextResponse_threading() throws BadRequestException, InterruptedException {
        final LevelUpConnection connection = new LevelUpConnection(getContext());
        final LevelUpRequest request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "1", null, null);
        final LevelUpResponse response1 = new LevelUpResponse("test1", LevelUpStatus.ERROR_SERVER);
        final String requestUrl1 = request1.getUrl(getContext()).toString();
        // Add the first response to the list.
        connection.setNextResponse(requestUrl1, response1);

        final LevelUpRequest request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "2", null, null);
        final LevelUpResponse response2 = new LevelUpResponse("test2", LevelUpStatus.ERROR_SERVER);
        final String requestUrl2 = request2.getUrl(getContext()).toString();
        // Add the second response to the list.
        connection.setNextResponse(requestUrl2, response2);

        // Create one more request and don't set it right now.
        final LevelUpRequest request3 =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "3", null, null);
        final String requestUrl3 = request3.getUrl(getContext()).toString();

        LevelUpConnection.setNextInstance(connection);

        final CountDownLatch latch1 = new CountDownLatch(1);
        final Runnable runnable1 = new Runnable() {

            @Override
            public void run() {
                assertSame(connection, LevelUpConnection.newInstance(getContext()));
                assertSame(response1, connection.send(request1));
                assertNull(connection.getNextResponse(requestUrl1));
                assertNotNull(connection.getNextResponse(requestUrl2));
                latch1.countDown();
            }
        };

        new Thread(runnable1).start();
        assertTrue(latch1.await(2, TimeUnit.SECONDS));
        assertNull(connection.getNextResponse(requestUrl1));
        assertNotNull(connection.getNextResponse(requestUrl2));

        final CountDownLatch latch2 = new CountDownLatch(1);
        final Runnable runnable2 = new Runnable() {

            @Override
            public void run() {
                // Add a new request in a different thread.
                final LevelUpResponse response3 =
                        new LevelUpResponse("test3", LevelUpStatus.ERROR_SERVER);

                connection.setNextResponse(requestUrl3, response3);
                assertSame(response3, connection.send(request3));
                assertNull(connection.getNextResponse(requestUrl1));
                assertNotNull(connection.getNextResponse(requestUrl2));
                assertNull(connection.getNextResponse(requestUrl3));
                latch2.countDown();
            }
        };

        new Thread(runnable2).start();
        assertTrue(latch2.await(2, TimeUnit.SECONDS));
        assertNull(connection.getNextResponse(requestUrl1));
        assertNotNull(connection.getNextResponse(requestUrl2));
        assertNull(connection.getNextResponse(requestUrl3));

        // Request the last remaining stored request.
        assertSame(response2, connection.send(request2));
        assertNull(connection.getNextResponse(requestUrl1));
        assertNull(connection.getNextResponse(requestUrl2));
        assertNull(connection.getNextResponse(requestUrl3));
        // Make sure there are no more cached responses.
        assertNull(connection.getNextResponse(null));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#send}.
     */
    @SmallTest
    public void testSend_basic() {
        final LevelUpConnection connection = LevelUpConnection.newInstance(getContext());
        final LevelUpRequest request =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "user", null, null);
        NetworkConnection.setNextResponse(new StreamingResponse());
        final LevelUpResponse response = connection.send(request);
        assertNotNull(response);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#send}.
     *
     * @throws Exception if {@link com.scvngr.levelup.core.net.LevelUpConnectionTest.MockHttpUrlConnection} throws
     */
    @SmallTest
    public void testSend_withResponse() throws Exception {
        final LevelUpConnection connection = LevelUpConnection.newInstance(getContext());
        final LevelUpRequest request =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14, "user", null, null);
        NetworkConnection.setNextResponse(new StreamingResponse(new MockHttpUrlConnection()));
        final LevelUpResponse response = connection.send(request);

        assertEquals(LevelUpStatus.OK, response.getStatus());
        assertEquals(MockHttpUrlConnection.INPUT_STREAM_STRING, response.getData());

        // Make sure that the last request is set.
        final AbstractRequest sentRequest =
                connection.getLastRequest(request.getUrl(getContext()).toString());
        assertNotNull(sentRequest);
        assertEquals(request, sentRequest);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpConnection#setNetworkEnabled(boolean)}.
     */
    @SmallTest
    public void testSetNetworkEnabled() {
        {
            // Default should not throw a runtime exception.
            NetworkConnection.setNextResponse(new StreamingResponse());
            final LevelUpConnection connection = LevelUpConnection.newInstance(getContext());
            connection.send(new LevelUpRequest(getContext(), HttpMethod.GET,
                    LevelUpRequest.API_VERSION_CODE_V14, "user", null, null));
        }

        {
            final LevelUpConnection connection = LevelUpConnection.newInstance(getContext());
            LevelUpConnection.setNetworkEnabled(false);
            try {
                connection
                        .send(new LevelUpRequest(getContext(), HttpMethod.GET,
                                LevelUpRequest.API_VERSION_CODE_V14,
                                "user", null, null));
                fail("network connection should throw exception");
            } catch (final RuntimeException e) {
                // Expected exception
            }
        }
    }

    /**
     * Tests that requests that throw exceptions are caught properly.
     */
    @SmallTest
    public void testSendWithRequestThatThrowsException() {
        final LevelUpConnection connection = LevelUpConnection.newInstance(getContext());
        final TestRequest request = new TestRequest(getContext());

        final LevelUpResponse response = connection.send(request);
        assertEquals(LevelUpStatus.ERROR_BAD_REQUEST, response.getStatus());
    }

    /**
     * Test impl of {@link com.scvngr.levelup.core.net.LevelUpRequest} that throws an exception in
     * {@link com.scvngr.levelup.core.net.LevelUpRequest#getUrlString(android.content.Context)}.
     */
    private static final class TestRequest extends LevelUpRequest {

        public TestRequest(@NonNull final Context context) {
            super(context, HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V14, "test", null, null);
        }

        @Override
        @NonNull
        public String getUrlString(@NonNull final Context context) throws BadRequestException {
            throw new BadRequestException("test");
        }
    }

    /**
     * Wrapped {@link java.net.HttpURLConnection} to return {@link java.net.HttpURLConnection#HTTP_OK} for
     * {@link java.net.HttpURLConnection#getResponseCode()}.
     */
    private static class MockHttpUrlConnection extends HttpURLConnection {
        public static final String INPUT_STREAM_STRING = "Mock response!";
        private final InputStream mStream;

        /**
         * Constructor.
         *
         * @throws java.net.MalformedURLException if the URL is malformed
         */
        public MockHttpUrlConnection() throws MalformedURLException {
            super(new URL("http://www.example.com"));
            mStream = new ByteArrayInputStream(INPUT_STREAM_STRING.getBytes());
        }

        @Override
        public int getResponseCode() throws IOException {
            return HttpURLConnection.HTTP_OK;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return mStream;
        }

        @Override
        public void disconnect() {
            try {
                mStream.close();
            } catch (final IOException e) {
                LogManager.e("error disconnecting", e);
            }
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {
            // do nothing
        }
    }
}
