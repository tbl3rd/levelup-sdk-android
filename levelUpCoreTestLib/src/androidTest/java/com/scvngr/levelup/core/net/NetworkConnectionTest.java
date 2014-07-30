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

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.NetworkConnection}.
 */
public final class NetworkConnectionTest extends SupportAndroidTestCase {

    @NonNull
    private final MockWebServer mServer = new MockWebServer();

    @NonNull
    private static final String RESPONSE_BODY = "This is a response body";

    @NonNull
    private static final String BASE_URL = "http://www.example.com";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mServer.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(RESPONSE_BODY)
                .setHeader("content-length", RESPONSE_BODY.getBytes("utf-8").length));
        mServer.play();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mServer.shutdown();
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a basic request properly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_Basic() throws IOException, BadRequestException {
        final RequestStub request = new RequestStub(HttpMethod.GET, BASE_URL, null, null, null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertEquals(connection.getURL(), request.getUrl(getContext()));
        assertEquals(connection.getRequestMethod(), request.getMethod().name());
        assertEquals(connection.getURL().getQuery(), null);
        assertTrue("Request headers are empty", connection.getRequestProperties().isEmpty());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with headers properly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withHeaders() throws IOException, BadRequestException {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("test_value", "test");
        headers.put("header2", "header");
        final RequestStub request = new RequestStub(HttpMethod.GET, BASE_URL, headers, null, null);
        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);

        assertRequestPropertiesEquals(headers, connection);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with null headers
     * properly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withNullHeaders() throws IOException, BadRequestException {
        final RequestStub request = new RequestStub(HttpMethod.GET, BASE_URL, null, null, null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertEquals(connection.getURL(), request.getUrl(getContext()));
        assertEquals(connection.getRequestMethod(), request.getMethod().name());
        assertEquals(connection.getURL().getQuery(), null);
        assertTrue("Request headers are empty", connection.getRequestProperties().isEmpty());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with query params
     * properly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withQueryParameters() throws IOException,
            BadRequestException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("test_value", "test");
        params.put("param2", "param");
        final RequestStub request = new RequestStub(HttpMethod.GET, BASE_URL, null, params, null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        final String expectedQueryString = "param2=param&test_value=test";
        assertEquals(request.getUrl(getContext()).getQuery(), connection.getURL().getQuery());
        assertEquals(expectedQueryString, connection.getURL().getQuery());

        assertTrue("Request headers are empty", connection.getRequestProperties().isEmpty());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with null query params
     * properly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withNullQueryParamters() throws IOException,
            BadRequestException {
        final RequestStub request = new RequestStub(HttpMethod.GET, BASE_URL, null, null, null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertEquals(request.getUrl(getContext()).getQuery(), connection.getURL().getQuery());
        assertEquals(null, connection.getURL().getQuery());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with a POST body
     * correctly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withPostBody() throws IOException, BadRequestException {
        final String body = "this is a test body";
        final RequestStub request = new RequestStub(HttpMethod.POST, BASE_URL, null, null, body);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertTrue("Connection is marked as output", connection.getDoOutput());
        assertEquals(HttpMethod.POST.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with a POST body
     * correctly.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withPostBodyAndPutRequest() throws IOException,
            BadRequestException {
        final String body = "this is a test body";
        final RequestStub request =
                new RequestStub(HttpMethod.PUT, BASE_URL, new HashMap<String, String>(),
                        new HashMap<String, String>(), body);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertTrue("Connection is marked as output", connection.getDoOutput());
        assertEquals(HttpMethod.PUT.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#doOutput} with a get request with no post body does not mark
     * the connection as output and keeps it as a get.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testDoOutput_withNoPostBody() throws IOException, BadRequestException {
        final RequestStub request =
                new RequestStub(HttpMethod.GET, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertFalse("Connection is not marked as output", connection.getDoOutput());
        NetworkConnection.doOutput(getContext(), connection, request);
        assertEquals(HttpMethod.GET.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} builds a request with a POST body
     * correctly even if the request is marked as a GET.
     *
     * @throws java.io.IOException if {@link com.scvngr.levelup.core.net.NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testDoOutput_withPostBodyAndGetRequest() throws IOException, BadRequestException {
        final String body = "this is a test body";
        final RequestStub request =
                new RequestStub(HttpMethod.GET, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), body);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertTrue("Connection is marked as output", connection.getDoOutput());
        NetworkConnection.doOutput(getContext(), connection, request);
        assertEquals(HttpMethod.POST.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a GET request.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_get() throws InterruptedException, IOException {
        final RequestStub request =
                new RequestStub(HttpMethod.GET, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/", recorded.getPath());
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a GET request and query params.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_getWithQueryParams() throws InterruptedException, IOException {
        final Map<String, String> query = new HashMap<String, String>();
        query.put("test1", "value1");
        final RequestStub request =
                new RequestStub(HttpMethod.GET, getMockServerUrl(), new HashMap<String, String>(),
                        query, null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/?test1=value1", recorded.getPath());
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a GET request and headers.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_getWithHeaders() throws InterruptedException, IOException {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("test1", "value1");
        headers.put("testing_this", "more headers");
        final RequestStub request =
                new RequestStub(HttpMethod.GET, getMockServerUrl(), headers,
                        new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/", recorded.getPath());

        assertTrue("First header was found", recorded.getHeaders().contains("test1: value1"));
        assertTrue("Second header was found",
                recorded.getHeaders().contains("testing_this: more headers"));
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a GET request and data returned.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_getWithData() throws InterruptedException, IOException {
        final RequestStub request =
                new RequestStub(HttpMethod.GET, getMockServerUrl(), new HashMap<String, String>(),
                        null, null);

        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/", recorded.getPath());
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(response.getData(), "utf-8"));
        final String out = reader.readLine();
        assertEquals(RESPONSE_BODY, out);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a POST request with post body.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_post() throws InterruptedException, IOException {
        final String body = "this is a test body";
        final RequestStub request =
                new RequestStub(HttpMethod.POST, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), body);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        assertEquals(body, new String(recorded.getBody()));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a PUT request.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_put() throws InterruptedException, IOException {
        final String body = "this is a test body";
        final RequestStub request =
                new RequestStub(HttpMethod.PUT, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), body);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        assertEquals(body, new String(recorded.getBody()));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a DELETE request.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws java.io.IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_delete() throws InterruptedException, IOException {
        final RequestStub request =
                new RequestStub(HttpMethod.DELETE, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.NetworkConnection#send} with a request that throws a {@link com.scvngr.levelup.core.net.AbstractRequest.BadRequestException}
     * .
     */
    @SmallTest
    public void testSend_withBadRequestException() throws IOException {
        final BadRequestExceptionThrowingRequest request =
                new BadRequestExceptionThrowingRequest(HttpMethod.GET, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final Exception exception = response.getError();

        assertNotNull("Does not retry when a BadRequestException is thrown", exception);
        assertTrue(exception instanceof BadRequestException);
    }

    /**
     * Tests {@link NetworkConnection#send} with a request that throws one {@link IOException}.
     */
    @SmallTest
    public void testSend_withIOException() throws IOException {
        final IOExceptionThrowingRequest request =
                new IOExceptionThrowingRequest(HttpMethod.POST, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), "test body");
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final Exception exception = response.getError();

        assertEquals(1, request.mNumWrites);
        assertNotNull("Does not retry when an IOException is thrown", exception);
        assertTrue(exception instanceof IOException);
    }

    /**
     * Tests {@link NetworkConnection#send} with a request that throws two {@link EOFException}s.
     */
    @SmallTest
    public void testSend_withTwoEOFExceptions() throws IOException {
        final EOFExceptionThrowingRequest request = getEOFExceptionThrowingRequest(2);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);

        assertEquals(3, request.mNumWrites);
        assertNull("Retries at least 2 times to close stale sockets when an EOFException is thrown",
                response.getError());
    }

    /**
     * Tests {@link NetworkConnection#send} with a request that throws one more than
     * {@link NetworkConnection#MAX_POOLED_CONNECTIONS} {@link EOFException}s.
     */
    @SmallTest
    public void testSend_withMaxPlusOneEOFExceptions() throws IOException {
        final EOFExceptionThrowingRequest request =
                getEOFExceptionThrowingRequest(NetworkConnection.MAX_POOLED_CONNECTIONS + 1);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final Exception exception = response.getError();

        assertEquals(NetworkConnection.MAX_POOLED_CONNECTIONS + 2, request.mNumWrites);
        assertNull(NullUtils.format(
                "Retries at least MAX_POOLED_CONNECTIONS(%d) + 1 times to close stale sockets when "
                + "EOFExceptions are thrown", NetworkConnection.MAX_POOLED_CONNECTIONS), exception);
    }

    /**
     * Tests {@link NetworkConnection#send} with a request that throws two more than
     * {@link NetworkConnection#MAX_POOLED_CONNECTIONS} {@link EOFException}s.
     */
    @SmallTest
    public void testSend_withMaxPlusTwoEOFExceptions() throws IOException {
        final EOFExceptionThrowingRequest request =
                getEOFExceptionThrowingRequest(NetworkConnection.MAX_POOLED_CONNECTIONS + 2);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final Exception exception = response.getError();

        assertEquals(NetworkConnection.MAX_POOLED_CONNECTIONS + 2, request.mNumWrites);
        assertNotNull(NullUtils.format(
                "Fails if more than MAX_POOLED_CONNECTIONS(%d) + 2 EOFExceptions are thrown",
                NetworkConnection.MAX_POOLED_CONNECTIONS), exception);
        assertTrue(exception instanceof EOFException);
    }

    /**
     * @param numExceptions the number of {@link EOFException}s to throw.
     * @return the {@link EOFExceptionThrowingRequest}.
     */
    private EOFExceptionThrowingRequest getEOFExceptionThrowingRequest(final int numExceptions) {
        try {
            return new EOFExceptionThrowingRequest(HttpMethod.POST, getMockServerUrl(),
                    new HashMap<String, String>(), new HashMap<String, String>(), "test body",
                    numExceptions);
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Helper method to get the mock server URL.
     *
     * @return the string URL to the mock server
     * @throws java.io.IOException if {@link MockWebServer#getUrl(String)} throws
     */
    @NonNull
    private String getMockServerUrl() throws IOException {
        return NullUtils.nonNullContract(mServer.getUrl("/").toString());
    }

    /**
     * Test implementation of {@link com.scvngr.levelup.core.net.AbstractRequest} that throws a {@link com.scvngr.levelup.core.net.AbstractRequest.BadRequestException} when
     * {@link com.scvngr.levelup.core.net.AbstractRequest#getUrlString} is called.
     */
    private static final class BadRequestExceptionThrowingRequest extends RequestStub {

        private int mNumExceptions = 1;

        public BadRequestExceptionThrowingRequest(@NonNull final HttpMethod method,
                @NonNull final String url, @Nullable final Map<String, String> requestHeaders,
                @Nullable final Map<String, String> queryParams, @Nullable final String body) {
            super(method, url, requestHeaders, queryParams, body);
        }

        @Override
        @NonNull
        public String getUrlString(@NonNull final Context context) throws BadRequestException {
            if (0 < mNumExceptions) {
                mNumExceptions--;

                throw new BadRequestException("bad request");
            }

            return super.getUrlString(context);
        }
    }

    /**
     * Test implementation of {@link AbstractRequest} that throws up to {@code numExceptions}
     * {@link EOFException}s when {@link AbstractRequest#writeBodyToStream} is called.
     */
    private static final class EOFExceptionThrowingRequest extends RequestStub {

        private int mNumExceptions;
        private int mNumWrites;

        public EOFExceptionThrowingRequest(@NonNull final HttpMethod method,
                @NonNull final String url, @Nullable final Map<String, String> requestHeaders,
                @Nullable final Map<String, String> queryParams, @NonNull final String body,
                final int numExceptions) {
            super(method, url, requestHeaders, queryParams, body);

            mNumExceptions = numExceptions;
        }

        @Override
        public void writeBodyToStream(@NonNull final Context context,
                @NonNull final OutputStream stream) throws IOException {
            super.writeBodyToStream(context, stream);

            mNumWrites++;

            if (0 < mNumExceptions) {
                mNumExceptions--;

                throw new EOFException("stale connection");
            }
        }
    }

    /**
     * Test implementation of {@link AbstractRequest} that throws an {@link IOException} the first
     * time {@link AbstractRequest#writeBodyToStream} is called.
     */
    private static final class IOExceptionThrowingRequest extends RequestStub {

        private int mNumExceptions = 1;
        private int mNumWrites;

        public IOExceptionThrowingRequest(@NonNull final HttpMethod method,
                @NonNull final String url, @Nullable final Map<String, String> requestHeaders,
                @Nullable final Map<String, String> queryParams, @Nullable final String body) {
            super(method, url, requestHeaders, queryParams, body);
        }

        @Override
        public void writeBodyToStream(@NonNull final Context context,
                @NonNull final OutputStream stream) throws IOException {
            super.writeBodyToStream(context, stream);

            mNumWrites++;

            if (0 < mNumExceptions) {
                mNumExceptions--;

                throw new IOException("broken pipe");
            }
        }
    }

    private static void assertRequestPropertiesEquals(
            @NonNull final Map<String, String> expectedHeaders,
            @NonNull final HttpURLConnection connection) {
        assertEquals(expectedHeaders.size(), connection.getRequestProperties().size());

        for (final String key : expectedHeaders.keySet()) {
            assertEquals(expectedHeaders.get(key), connection.getRequestProperty(key));
        }
    }

    /**
     * A mock implementation of an AbstractRequest.
     */
    public static class RequestStub extends AbstractRequest {
        @Nullable
        private final RequestBody mRequestBody;

        /**
         * @param method request method
         * @param url mock URL
         * @param requestHeaders optional request headers
         * @param queryParams optional query parameters
         * @param body optional body
         */
        public RequestStub(@NonNull final HttpMethod method, @NonNull final String url,
                           @Nullable final Map<String, String> requestHeaders,
                           @Nullable final Map<String, String> queryParams,
                           @Nullable final String body) {
            super(method, url, requestHeaders, queryParams);
            mRequestBody = body != null ? new StringRequestBodyImpl(body) : null;
        }

        @Override
        public void writeBodyToStream(@NonNull final Context context,
                @NonNull final OutputStream stream) throws IOException {
            if (mRequestBody != null) {
                mRequestBody.writeToOutputStream(context, stream);
            }
        }

        @Override
        public int getBodyLength(@NonNull final Context context) {
            return mRequestBody != null ? mRequestBody.getContentLength() : 0;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((mRequestBody == null) ? 0 : mRequestBody.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final RequestStub other = (RequestStub) obj;
            if (mRequestBody == null) {
                if (other.mRequestBody != null) {
                    return false;
                }
            } else if (!mRequestBody.equals(other.mRequestBody)) {
                return false;
            }
            return true;
        }
    }

    /**
     * Concrete implementation of {@link com.scvngr.levelup.core.net.StringRequestBody}.
     */
    protected static final class StringRequestBodyImpl extends StringRequestBody {

        /**
         * Parcelable creator.
         */
        public static final Creator<StringRequestBodyImpl> CREATOR =
                new Creator<StringRequestBodyImpl>() {

                    @Override
                    public StringRequestBodyImpl createFromParcel(final Parcel source) {
                        return new StringRequestBodyImpl(NullUtils.nonNullContract(source));
                    }

                    @Override
                    public StringRequestBodyImpl[] newArray(final int size) {
                        return new StringRequestBodyImpl[size];
                    }
                };

        /**
         * @param in source
         */
        public StringRequestBodyImpl(@NonNull final Parcel in) {
            super(in);
        }

        /**
         * @param body contents
         */
        public StringRequestBodyImpl(@NonNull final String body) {
            super(body);
        }

        @Override
        @NonNull
        public String getContentType() {
            return "text/plain";
        }
    }
}
