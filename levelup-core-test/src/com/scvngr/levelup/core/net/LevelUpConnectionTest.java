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
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests {@link LevelUpConnection}.
 */
public final class LevelUpConnectionTest extends SupportAndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LevelUpConnection.setNextInstance(null);
    }

    /**
     * Tests {@link LevelUpConnection#newInstance(android.content.Context)}.
     */
    @SmallTest
    public void testNewInstance() {
        // Make sure it returns a new object every time.
        assertNotSame(LevelUpConnection.newInstance(getContext()),
                LevelUpConnection.newInstance(getContext()));

        final LevelUpConnection connection =
                new LevelUpConnection(getContext());
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
        final LevelUpConnection connection =
                new LevelUpConnection(getContext());
        LevelUpConnection.setNextInstance(connection);
        final LevelUpV13Request request =
                new LevelUpV13Request(getContext(), HttpMethod.GET, getName(), null, null);
        connection.setLastRequest(getName(), request);
        assertEquals(request, connection.getLastRequest(getName()));
        assertEquals(request, connection.getLastRequest(null));
    }

    /**
     * Tests {@link LevelUpConnection#setNextResponse}.
     *
     * @throws BadRequestException
     */
    @SmallTest
    public void testSetNextResponse_withSpecificUrl() throws BadRequestException {
        final LevelUpConnection connection =
                new LevelUpConnection(getContext());
        final LevelUpV13Request request =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "/", null, null); //$NON-NLS-1$
        final LevelUpResponse response =
                new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        LevelUpConnection.setNextInstance(connection);
        connection.setNextResponse(request.getUrl(getContext()).toString(), response);

        assertSame(connection, LevelUpConnection.newInstance(getContext()));
        assertSame(response, connection.send(request));
    }

    /**
     * Tests {@link LevelUpConnection#setNextResponse}.
     *
     * @throws BadRequestException
     */
    @SmallTest
    public void testSetNextResponse_withNullUrl() throws BadRequestException {
        final LevelUpConnection connection =
                new LevelUpConnection(getContext());
        final LevelUpV13Request request =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "/", null, null); //$NON-NLS-1$
        final LevelUpResponse response =
                new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        LevelUpConnection.setNextInstance(connection);
        connection.setNextResponse(null, response);

        assertSame(connection, LevelUpConnection.newInstance(getContext()));
        assertSame(response, connection.send(request));
    }

    /**
     * Tests {@link LevelUpConnection#setNextResponse}.
     *
     * @throws BadRequestException
     */
    @SmallTest
    public void testSetNextResponse_withUnRecognizedUrl() throws BadRequestException {
        final LevelUpConnection connection =
                new LevelUpConnection(getContext());
        final LevelUpV13Request request =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "/", null, null); //$NON-NLS-1$
        final LevelUpResponse response =
                new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        LevelUpConnection.setNextInstance(connection);
        connection.setNextResponse("www.blah.com", response); //$NON-NLS-1$

        assertSame(connection, LevelUpConnection.newInstance(getContext()));

        try {
            // Since no URL match was found, we should throw an exception.
            connection.send(request);
            fail("should throw RuntimeException"); //$NON-NLS-1$
        } catch (final RuntimeException e) {
            // Expected exception.
        }
    }

    /**
     * Tests {@link LevelUpConnection#setNextResponse}.
     *
     * @throws BadRequestException
     * @throws InterruptedException
     */
    @SmallTest
    public void testSetNextResponse_threading() throws BadRequestException, InterruptedException {
        final LevelUpConnection connection =
                new LevelUpConnection(getContext());
        final LevelUpV13Request request1 =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "1", null, null); //$NON-NLS-1$
        final LevelUpResponse response1 =
                new LevelUpResponse("test1", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        final String requestUrl1 = request1.getUrl(getContext()).toString();
        // Add the first response to the list.
        connection.setNextResponse(requestUrl1, response1);

        final LevelUpV13Request request2 =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "2", null, null); //$NON-NLS-1$
        final LevelUpResponse response2 =
                new LevelUpResponse("test2", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        final String requestUrl2 = request2.getUrl(getContext()).toString();
        // Add the second response to the list.
        connection.setNextResponse(requestUrl2, response2);

        // Create one more request and don't set it right now.
        final LevelUpV13Request request3 =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "3", null, null); //$NON-NLS-1$
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
                        new LevelUpResponse("test3", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$

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
     * Tests {@link LevelUpConnection#send}.
     */
    @SmallTest
    public void testSend_basic() {
        final LevelUpConnection connection =
                LevelUpConnection.newInstance(getContext());
        final LevelUpV13Request request =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "user", null, null); //$NON-NLS-1$
        NetworkConnection.setNextResponse(new StreamingResponse());
        final LevelUpResponse response = connection.send(request);
        assertNotNull(response);
    }

    /**
     * Tests {@link LevelUpConnection#send}.
     *
     * @throws Exception if {@link MockHttpUrlConnection} throws
     */
    @SmallTest
    public void testSend_withResponse() throws Exception {
        final LevelUpConnection connection =
                LevelUpConnection.newInstance(getContext());
        final LevelUpV13Request request =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "user", null, null); //$NON-NLS-1$
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
     * Tests {@link LevelUpConnection#setNetworkEnabled(boolean)}.
     */
    @SmallTest
    public void testSetNetworkEnabled() {
        {
            // Default should not throw a runtime exception.
            NetworkConnection.setNextResponse(new StreamingResponse());
            final LevelUpConnection connection =
                    LevelUpConnection.newInstance(getContext());
            connection.send(new LevelUpV13Request(getContext(), HttpMethod.GET,
                    "user", null, null)); //$NON-NLS-1$
        }

        {
            final LevelUpConnection connection =
                    LevelUpConnection.newInstance(getContext());
            LevelUpConnection.setNetworkEnabled(false);
            try {
                connection.send(new LevelUpV13Request(getContext(), HttpMethod.GET,
                        "user", null, null)); //$NON-NLS-1$
                fail("network connection should throw exception"); //$NON-NLS-1$
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
        final LevelUpConnection connection =
                LevelUpConnection.newInstance(getContext());
        final TestRequest request =
                new TestRequest(mContext, HttpMethod.GET, "test", null, null, null); //$NON-NLS-1$

        final LevelUpResponse response = connection.send(request);
        assertEquals(LevelUpStatus.ERROR_BAD_REQUEST, response.getStatus());
    }

    /**
     * Test impl of {@link LevelUpV13Request} that throws an exception in
     * {@link LevelUpV13Request#getUrlString(Context)}.
     *
     */
    private static final class TestRequest extends LevelUpV13Request {

        public TestRequest(final Context context, final HttpMethod method, final String endpoint,
                final Map<String, String> queryParams, final Map<String, String> postParams,
                final AccessTokenRetriever retriever) {
            super(context, method, endpoint, queryParams, postParams, retriever);
        }

        @Override
        @NonNull
        public String getUrlString(@NonNull final Context context) throws BadRequestException {
            throw new BadRequestException("test"); //$NON-NLS-1$
        }
    }

    /**
     * Wrapped {@link HttpURLConnection} to return {@link HttpURLConnection#HTTP_OK} for
     * {@link HttpURLConnection#getResponseCode()}.
     */
    private static class MockHttpUrlConnection extends HttpURLConnection {
        public static final String INPUT_STREAM_STRING = "Mock response!"; //$NON-NLS-1$
        private final InputStream mStream;
        private final int mResponseCode;

        /**
         * Constructor.
         *
         * @throws MalformedURLException if the URL is malformed
         */
        public MockHttpUrlConnection() throws MalformedURLException {
            super(new URL("http://www.example.com")); //$NON-NLS-1$
            mStream = new ByteArrayInputStream(INPUT_STREAM_STRING.getBytes());
            mResponseCode = HttpURLConnection.HTTP_OK;
        }

        /**
         * Constructor.
         *
         * @param responseCode the response code to return
         * @throws MalformedURLException if the URL is malformed
         */
        public MockHttpUrlConnection(final int responseCode) throws MalformedURLException {
            super(new URL("http://www.example.com")); //$NON-NLS-1$
            mStream = new ByteArrayInputStream(INPUT_STREAM_STRING.getBytes());
            mResponseCode = responseCode;
        }

        @Override
        public int getResponseCode() throws IOException {
            return mResponseCode;
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
                LogManager.e("error disconnecting", e); //$NON-NLS-1$
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
