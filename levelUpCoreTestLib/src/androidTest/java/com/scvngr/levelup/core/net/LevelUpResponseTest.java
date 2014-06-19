/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.BufferedResponse.ResponseTooLargeException;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse}.
 */
public final class LevelUpResponseTest extends SupportAndroidTestCase {

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse} parceling.
     */
    @SmallTest
    public void testParcelable() {
        final LevelUpResponse response =
                new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        ParcelTestUtils.assertParcelableRoundtrips(response);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_okCode() {
        assertEquals(LevelUpStatus.OK, LevelUpResponse.mapStatus(HttpURLConnection.HTTP_OK));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeLow() {
        // CHECKSTYLE:OFF 100 status code is continue
        assertEquals(LevelUpStatus.ERROR_SERVER, LevelUpResponse.mapStatus(100));
        // CHECKSTYLE:ON
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeNotFound() {
        assertEquals(LevelUpStatus.ERROR_NOT_FOUND, LevelUpResponse.mapStatus(404));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeRedirection() {
        assertEquals(LevelUpStatus.ERROR_SERVER,
                LevelUpResponse.mapStatus(HttpURLConnection.HTTP_MULT_CHOICE));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeClientError() {
        assertEquals(LevelUpStatus.ERROR_SERVER,
                LevelUpResponse.mapStatus(HttpURLConnection.HTTP_BAD_REQUEST));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeMaintenenace() {
        assertEquals(LevelUpStatus.ERROR_MAINTENANCE,
                LevelUpResponse.mapStatus(HttpURLConnection.HTTP_UNAVAILABLE));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeUpgrade() {
        assertEquals(LevelUpStatus.UPGRADE,
                LevelUpResponse.mapStatus(HttpURLConnection.HTTP_NOT_IMPLEMENTED));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(Exception)}.
     */
    @SmallTest
    public void testMapStatusException_tooLarge() {
        assertEquals(LevelUpStatus.ERROR_RESPONSE_TOO_LARGE,
                LevelUpResponse.mapStatus(new ResponseTooLargeException()));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(Exception)}.
     */
    @SmallTest
    public void testMapStatusException_badRequest() {
        assertEquals(LevelUpStatus.ERROR_BAD_REQUEST,
                LevelUpResponse.mapStatus(new BadRequestException(""))); //$NON-NLS-1$
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int)}.
     */
    @SmallTest
    public void testMapStatusException_ioException() {
        assertEquals(LevelUpStatus.ERROR_NETWORK,
                LevelUpResponse.mapStatus(new IOException()));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(com.scvngr.levelup.core.net.StreamingResponse)}.
     *
     * @throws Exception if the URL in the mock object is malformed (i.e. it won't throw this)
     */
    @SmallTest
    public void testMapStatusResponse_responseWithoutError() throws Exception {
        final StreamingResponse response = new StreamingResponse(new MockHttpUrlConnection());

        assertEquals(LevelUpStatus.OK, LevelUpResponse.mapStatus(response));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(com.scvngr.levelup.core.net.StreamingResponse)}. Tests that a response with an
     * error doesn't use the status code to determine the status, but uses the exception in the
     * response object.
     */
    @SmallTest
    public void testMapStatusResponse_responseWithError() {
        final StreamingResponse response = new StreamingResponse(new ResponseTooLargeException());

        assertEquals(LevelUpStatus.ERROR_RESPONSE_TOO_LARGE,
                LevelUpResponse.mapStatus(response));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(com.scvngr.levelup.core.net.StreamingResponse, Exception)}.
     *
     * @throws Exception if the URL in the mock object is malformed (i.e. it won't throw this)
     */
    @SmallTest
    public void testMapStatusResponseException_noError() throws Exception {
        final StreamingResponse response = new StreamingResponse(new MockHttpUrlConnection());

        assertEquals(LevelUpStatus.OK, LevelUpResponse.mapStatus(response, null));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(com.scvngr.levelup.core.net.StreamingResponse, Exception)}. Tests that passing an
     * error with the response doesn't use the status code to determine the status, but uses the
     * exception passed.
     *
     * @throws Exception if the URL in the mock object is malformed (i.e. it won't throw this)
     */
    @SmallTest
    public void testMapStatusResponseException_withError() throws Exception {
        final StreamingResponse response = new StreamingResponse(new MockHttpUrlConnection());

        assertEquals(LevelUpStatus.ERROR_RESPONSE_TOO_LARGE,
                LevelUpResponse.mapStatus(response, new ResponseTooLargeException()));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(com.scvngr.levelup.core.net.StreamingResponse, Exception)}. Tests that passing an
     * error with the response doesn't use the status code or response error to determine the
     * status, but uses the exception passed.
     */
    @SmallTest
    public void testMapStatusResponseException_withErrorAndResponseError() {
        final StreamingResponse response = new StreamingResponse(new ResponseTooLargeException());

        assertEquals(LevelUpStatus.ERROR_NETWORK,
                LevelUpResponse.mapStatus(response, new IOException()));
    }

    /**
     * Tests the {@link com.scvngr.levelup.core.net.LevelUpResponse#equals(Object)} and
     * {@link com.scvngr.levelup.core.net.LevelUpResponse#hashCode()} methods.
     *
     * @throws Exception if {@link com.scvngr.levelup.core.net.LevelUpResponseTest.MockHttpUrlConnection} throws
     */
    @SmallTest
    public void testEqualsAndHashCode() throws Exception {
        final StreamingResponse baseResponse = new StreamingResponse();
        LevelUpResponse response1 = new LevelUpResponse(baseResponse);
        LevelUpResponse response2 = new LevelUpResponse(baseResponse);

        // Same base response should be the same
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, true);

        // New object should be the same.
        response2 = new LevelUpResponse(new StreamingResponse());
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, true);

        // Null shouldn't be the same
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, null, false);

        // Differing data
        response2 =
                new LevelUpResponse(new StreamingResponse(new MockHttpUrlConnection()));
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, false);

        // Now have the same response data.
        response1 =
                new LevelUpResponse(new StreamingResponse(new MockHttpUrlConnection()));
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, true);

        // Reset to initial
        response1 = new LevelUpResponse(baseResponse);

        // Build response 2 from error
        response2 =
                new LevelUpResponse(
                        new StreamingResponse(new ResponseTooLargeException()));
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, false);

        // Match it with response 1
        response1 =
                new LevelUpResponse(
                        new StreamingResponse(new ResponseTooLargeException()));
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, true);

        // Build response 2 from error
        response2 = new LevelUpResponse(new StreamingResponse(new IOException()));
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, false);

        // Match it with response 1
        response1 = new LevelUpResponse(new StreamingResponse(new IOException()));
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, true);

        response2 = null;
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, false);

        response2 = new LevelUpResponse((String) null, LevelUpStatus.ERROR_PARSING);
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, false);

        response1 = new LevelUpResponse((String) null, LevelUpStatus.ERROR_PARSING);
        MoreAsserts.checkEqualsAndHashCodeMethods(response1, response2, true);
    }

    /**
     * Wrapped {@link java.net.HttpURLConnection} to return {@link java.net.HttpURLConnection#HTTP_OK} for
     * {@link java.net.HttpURLConnection#getResponseCode()}.
     */
    private static class MockHttpUrlConnection extends HttpURLConnection {

        /**
         * Constructor.
         *
         * @throws java.net.MalformedURLException if the URL is malformed
         */
        public MockHttpUrlConnection() throws MalformedURLException {
            super(new URL("http://www.example.com")); //$NON-NLS-1$
        }

        @Override
        public int getResponseCode() throws IOException {
            return HttpURLConnection.HTTP_OK;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public void disconnect() {
            // do nothing
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
