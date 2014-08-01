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
package com.scvngr.levelup.core.net;

import android.support.annotation.NonNull;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import static com.scvngr.levelup.core.util.NullUtils.nonNullContract;

import com.scvngr.levelup.core.model.ErrorFixture;
import com.scvngr.levelup.core.model.Error;
import com.scvngr.levelup.core.model.factory.json.ErrorJsonFactory;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.BufferedResponse.ResponseTooLargeException;
import com.scvngr.levelup.core.net.error.ErrorCode;
import com.scvngr.levelup.core.net.error.ErrorObject;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse}.
 */
public final class LevelUpResponseTest extends SupportAndroidTestCase {
    @NonNull
    private static final String SERVER_LEVELUP_PLATFORM = "LevelUp";

    @NonNull
    private static final String SERVER_NOT_LEVELUP_PLATFORM = "LevelUp/Failover";

    @NonNull
    private static final String EMPTY = "";

    @NonNull
    private static final Error mError1 =
            new Error("debit_card_only", "message1", "credit_card", "property1");

    @NonNull
    private static final Error mError2 =
            new Error("delinquent_bundle", "message2", "payment_token", "property2");

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse} parceling.
     */
    @SmallTest
    public void testParcelable() {
        final JSONArray errorArray = new JSONArray();
        errorArray.put(ErrorFixture.getJsonObjectFromModel(mError1));
        final LevelUpResponse response =
                new LevelUpResponse(errorArray.toString(), LevelUpStatus.ERROR_SERVER);
        final LevelUpResponse parceledResponse = ParcelTestUtils.feedThroughParceling(response);
        assertEquals("resulting Parcelable differs. ", response, parceledResponse);

        final LevelUpResponse exceptionResponse =
                new LevelUpResponse("test", LevelUpStatus.ERROR_SERVER);
        final LevelUpResponse parceledExceptionResponse =
                ParcelTestUtils.feedThroughParceling(exceptionResponse);
        assertEquals("resulting Parcelable differs. ", exceptionResponse,
                parceledExceptionResponse);
        assertEquals(exceptionResponse.getError().getMessage(),
                parceledExceptionResponse.getError().getMessage());

        final LevelUpResponse okResponse = new LevelUpResponse("test", LevelUpStatus.OK);
        final LevelUpResponse parceledOkResponse = ParcelTestUtils.feedThroughParceling(okResponse);
        assertEquals("resulting Parcelable differs. ", okResponse, parceledOkResponse);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_okCode() {
        assertEquals(LevelUpStatus.OK,
                LevelUpResponse.mapStatus(HttpURLConnection.HTTP_OK, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link LevelUpResponse#mapStatus(int, String)} with a sub-200 response code.
     */
    @SmallTest
    public void testMapStatusHttp_okCode_notFromPlatform() {
        assertEquals(LevelUpStatus.OK,
                LevelUpResponse.mapStatus(HttpURLConnection.HTTP_OK, SERVER_NOT_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)} with a
     * sub-200 response code.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeLow() {
        assertEquals(LevelUpStatus.ERROR_SERVER,
                LevelUpResponse.mapStatus(100, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeLow_notFromPlatform() {
        assertEquals(LevelUpStatus.ERROR_SERVER,
                LevelUpResponse.mapStatus(100, SERVER_NOT_LEVELUP_PLATFORM));
    }


    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)} with an
     * unknown code above the 2xx range.
     *
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeUnknown() {
        assertEquals(LevelUpStatus.ERROR_SERVER,
                LevelUpResponse.mapStatus(300, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)} with an
     * unknown code above the 2xx range.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeUnknown_notFromPlatform() {
        assertEquals(LevelUpStatus.ERROR_SERVER,
                LevelUpResponse.mapStatus(300, SERVER_NOT_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeNotFound() {
        assertEquals(LevelUpStatus.ERROR_NOT_FOUND, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_NOT_FOUND, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}. 404s should
     * be treated as generic server failures if the response is coming from a server other than
     * LevelUp Platform.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeNotFound_notFromPlatform() {
        assertEquals(LevelUpStatus.ERROR_SERVER, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_NOT_FOUND, SERVER_NOT_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeRedirection() {
        assertEquals(LevelUpStatus.ERROR_SERVER, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_MULT_CHOICE, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeClientError() {
        assertEquals(LevelUpStatus.ERROR_SERVER, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_BAD_REQUEST, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeMaintenenace() {
        assertEquals(LevelUpStatus.ERROR_MAINTENANCE, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_UNAVAILABLE, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}. Maintenance
     * mode applies even for responses not coming directly from LevelUp Platform.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeMaintenenace_notFromPlatform() {
        assertEquals(LevelUpStatus.ERROR_MAINTENANCE, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_UNAVAILABLE, SERVER_NOT_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(int, String)}.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeUpgrade() {
        assertEquals(LevelUpStatus.UPGRADE, LevelUpResponse
                .mapStatus(HttpURLConnection.HTTP_NOT_IMPLEMENTED, SERVER_LEVELUP_PLATFORM));
    }

    /**
     * Tests {@link LevelUpResponse#mapStatus(int, String)}. Responses not coming from LevelUp
     * Platform should be treated as generic errors rather than upgrade-required messages.
     */
    @SmallTest
    public void testMapStatusHttp_errorCodeUpgrade_notFromLevelUp() {
        assertEquals(LevelUpStatus.ERROR_SERVER, LevelUpResponse.mapStatus(
                HttpURLConnection.HTTP_NOT_IMPLEMENTED, SERVER_NOT_LEVELUP_PLATFORM));
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
                LevelUpResponse.mapStatus(new BadRequestException("")));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse#mapStatus(Exception)}.
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
     * Tests {@link LevelUpResponse#mapStatus(com.scvngr.levelup.core.net.StreamingResponse)}.
     * Tests that a response with an error doesn't use the status code to determine the status, but
     * uses the exception in the response object.
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
     * @throws Exception if {@link com.scvngr.levelup.core.net.LevelUpResponseTest.MockHttpUrlConnection}
     * throws
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
    }

    @SmallTest
    public void testGetError() {

        final JSONArray errorList = new JSONArray();

        errorList.put(ErrorFixture.getJsonObjectFromModel(mError1));
        errorList.put(ErrorFixture.getJsonObjectFromModel(mError2));

        final LevelUpResponse response =
                new LevelUpResponse(nonNullContract(errorList.toString()),
                        LevelUpStatus.ERROR_NETWORK);

        assertEquals(mError1, response.getServerError(
                nonNullContract(ErrorObject.fromString(mError1.getObject())),
                nonNullContract(ErrorCode.fromString(mError1.getCode()))));
        assertEquals(mError2, response.getServerError(
                nonNullContract(ErrorObject.fromString(mError2.getObject())),
                nonNullContract(ErrorCode.fromString(mError2.getCode()))));

        assertNull(response.getServerError(
                nonNullContract(ErrorObject.fromString(mError1.getObject())),
                nonNullContract(ErrorCode.fromString(mError2.getCode()))));
        assertNull(response.getServerError(
                nonNullContract(ErrorObject.fromString(mError2.getObject())),
                nonNullContract(ErrorCode.fromString(mError1.getCode()))));
    }

    @SmallTest
    public void testGetErrorList() {
        final List<Error> expectedErrorList = new LinkedList<Error>();

        expectedErrorList.add(mError1);
        expectedErrorList.add(mError2);

        final JSONArray errorList = new JSONArray();

        errorList.put(ErrorFixture.getJsonObjectFromModel(mError1));
        errorList.put(ErrorFixture.getJsonObjectFromModel(mError2));

        final LevelUpResponse response =
                new LevelUpResponse(nonNullContract(errorList.toString()),
                        LevelUpStatus.ERROR_NETWORK);

        assertEquals(expectedErrorList, response.getServerErrors());

        // Invalid errors should not throw exceptions.
        final LevelUpResponse responseNoErrors = new LevelUpResponse(EMPTY, LevelUpStatus.ERROR_SERVER);
        final List<Error> emptyList = responseNoErrors.getServerErrors();

        assertTrue(emptyList.isEmpty());
    }

    @SmallTest
    public void testParsingInvalidError_containsErrorException() {
        final LevelUpResponse response =
                new LevelUpResponse(nonNullContract("<html />"),
                        LevelUpStatus.ERROR_NETWORK);

        assertTrue(response.getError() instanceof IOException);
    }

    /**
     * Wrapped {@link java.net.HttpURLConnection} to return
     * {@link java.net.HttpURLConnection#HTTP_OK} for
     * {@link java.net.HttpURLConnection#getResponseCode()}.
     */
    private static class MockHttpUrlConnection extends HttpURLConnection {

        /**
         * Constructor.
         *
         * @throws java.net.MalformedURLException if the URL is malformed
         */
        public MockHttpUrlConnection() throws MalformedURLException {
            super(new URL("http://www.example.com"));
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
