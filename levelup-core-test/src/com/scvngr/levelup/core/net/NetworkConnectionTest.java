/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link NetworkConnection}.
 */
public final class NetworkConnectionTest extends SupportAndroidTestCase {

    private final MockWebServer mServer = new MockWebServer();
    private static final String RESPONSE_BODY = "This is a response body"; //$NON-NLS-1$

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mServer.enqueue(new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
                RESPONSE_BODY).setHeader("content-length", RESPONSE_BODY.getBytes("utf-8").length)); //$NON-NLS-1$ //$NON-NLS-2$
        mServer.play();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mServer.shutdown();
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a basic request properly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_Basic() throws IOException, BadRequestException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BufferedRequestTest.BASE_URL,
                        new HashMap<String, String>(), new HashMap<String, String>(), null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertEquals(connection.getURL(), request.getUrl(getContext()));
        assertEquals(connection.getRequestMethod(), request.getMethod().name());
        assertEquals(connection.getURL().getQuery(), null);
        assertTrue("Request headers are empty", connection.getRequestProperties().isEmpty()); //$NON-NLS-1$
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with headers properly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withHeaders() throws IOException, BadRequestException {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("test_value", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        headers.put("header2", "header"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BufferedRequestTest.BASE_URL, headers,
                        new HashMap<String, String>(), null);
        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertFalse("Request headers are not empty", connection.getRequestProperties().isEmpty()); //$NON-NLS-1$
        assertEquals(headers.size(), connection.getRequestProperties().size());
        for (final String key : headers.keySet()) {
            assertEquals(headers.get(key), connection.getRequestProperty(key));
        }
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with null headers
     * properly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withNullHeaders() throws IOException, BadRequestException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BufferedRequestTest.BASE_URL, null,
                        new HashMap<String, String>(), null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertEquals(connection.getURL(), request.getUrl(getContext()));
        assertEquals(connection.getRequestMethod(), request.getMethod().name());
        assertEquals(connection.getURL().getQuery(), null);
        assertTrue("Request headers are empty", connection.getRequestProperties().isEmpty()); //$NON-NLS-1$
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with query params
     * properly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withQueryParameters() throws IOException,
            BadRequestException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("test_value", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("param2", "param"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BufferedRequestTest.BASE_URL,
                        new HashMap<String, String>(), params, null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        final String expectedQueryString = "param2=param&test_value=test"; //$NON-NLS-1$
        assertEquals(request.getUrl(getContext()).getQuery(), connection.getURL().getQuery());
        assertEquals(expectedQueryString, connection.getURL().getQuery());
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with null query params
     * properly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withNullQueryParamters() throws IOException,
            BadRequestException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BufferedRequestTest.BASE_URL,
                        new HashMap<String, String>(), null, null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertEquals(request.getUrl(getContext()).getQuery(), connection.getURL().getQuery());
        assertEquals(null, connection.getURL().getQuery());
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with a POST body
     * correctly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withPostBody() throws IOException, BadRequestException {
        final String body = "this is a test body"; //$NON-NLS-1$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.POST, BufferedRequestTest.BASE_URL,
                        new HashMap<String, String>(), new HashMap<String, String>(), body);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertTrue("Connection is marked as output", connection.getDoOutput()); //$NON-NLS-1$
        assertEquals(HttpMethod.POST.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with a POST body
     * correctly.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testConfigureConnection_withPostBodyAndPutRequest() throws IOException,
            BadRequestException {
        final String body = "this is a test body"; //$NON-NLS-1$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.PUT, BufferedRequestTest.BASE_URL,
                        new HashMap<String, String>(), new HashMap<String, String>(), body);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertTrue("Connection is marked as output", connection.getDoOutput()); //$NON-NLS-1$
        assertEquals(HttpMethod.PUT.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link NetworkConnection#doOutput} with a get request with no post body does not mark
     * the connection as output and keeps it as a get.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testDoOutput_withNoPostBody() throws IOException, BadRequestException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), null);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertFalse("Connection is not marked as output", connection.getDoOutput()); //$NON-NLS-1$
        NetworkConnection.doOutput(getContext(), connection, request);
        assertEquals(HttpMethod.GET.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link NetworkConnection#configureConnection} builds a request with a POST body
     * correctly even if the request is marked as a GET.
     *
     * @throws IOException if {@link NetworkConnection#configureConnection} throws
     */
    @SmallTest
    public void testDoOutput_withPostBodyAndGetRequest() throws IOException, BadRequestException {
        final String body = "this is a test body"; //$NON-NLS-1$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), body);

        final HttpURLConnection connection =
                NetworkConnection.configureConnection(getContext(), request);
        assertTrue("Connection is marked as output", connection.getDoOutput()); //$NON-NLS-1$
        NetworkConnection.doOutput(getContext(), connection, request);
        assertEquals(HttpMethod.POST.name(), connection.getRequestMethod());
    }

    /**
     * Tests {@link NetworkConnection#send} with a GET request.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_get() throws InterruptedException, IOException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/", recorded.getPath()); //$NON-NLS-1$
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link NetworkConnection#send} with a GET request and query params.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_getWithQueryParams() throws InterruptedException, IOException {
        final Map<String, String> query = new HashMap<String, String>();
        query.put("test1", "value1"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, getMockServerUrl(), new HashMap<String, String>(),
                        query, null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/?test1=value1", recorded.getPath()); //$NON-NLS-1$
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link NetworkConnection#send} with a GET request and headers.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_getWithHeaders() throws InterruptedException, IOException {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("test1", "value1"); //$NON-NLS-1$ //$NON-NLS-2$
        headers.put("testing_this", "more headers"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, getMockServerUrl(), headers,
                        new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/", recorded.getPath()); //$NON-NLS-1$

        assertTrue("First header was found", recorded.getHeaders().contains("test1: value1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue("Second header was found", //$NON-NLS-1$
                recorded.getHeaders().contains("testing_this: more headers")); //$NON-NLS-1$
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link NetworkConnection#send} with a GET request and data returned.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_getWithData() throws InterruptedException, IOException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, getMockServerUrl(), null,
                        new HashMap<String, String>(), null);

        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals("/", recorded.getPath()); //$NON-NLS-1$
        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(response.getData(), "utf-8")); //$NON-NLS-1$
        final String out = reader.readLine();
        assertEquals(RESPONSE_BODY, out);
    }

    /**
     * Tests {@link NetworkConnection#send} with a POST request with post body.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_post() throws InterruptedException, IOException {
        final String body = "this is a test body"; //$NON-NLS-1$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.POST, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), body);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        assertEquals(body, new String(recorded.getBody()));
    }

    /**
     * Tests {@link NetworkConnection#send} with a PUT request.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_put() throws InterruptedException, IOException {
        final String body = "this is a test body"; //$NON-NLS-1$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.PUT, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), body);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        assertEquals(body, new String(recorded.getBody()));
    }

    /**
     * Tests {@link NetworkConnection#send} with a DELETE request.
     *
     * @throws InterruptedException if {@link MockWebServer#takeRequest()} throws it
     * @throws IOException if {@link #getMockServerUrl()} throws it
     */
    @SmallTest
    public void testSend_delete() throws InterruptedException, IOException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.DELETE, getMockServerUrl(), new HashMap<String, String>(),
                        new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);
        final RecordedRequest recorded = mServer.takeRequest();

        assertEquals(request.getMethod().name(), recorded.getMethod());
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
    }

    /**
     * Tests {@link NetworkConnection#send} with a request that throws a
     * {@link BadRequestException}.
     */
    @SmallTest
    public void testSend_withRequestThatThrowsException() throws InterruptedException, IOException {
        final ExceptionThrowingRequest request =
                new ExceptionThrowingRequest(HttpMethod.GET, getMockServerUrl(),
                        new HashMap<String, String>(), new HashMap<String, String>(), null);
        final StreamingResponse response = NetworkConnection.send(getContext(), request);

        assertNotNull(response.getError());
        assertTrue(response.getError() instanceof BadRequestException);
    }

    /**
     * Helper method to get the mock server url.
     *
     * @return the string url to the mock server
     * @throws IOException if {@link MockWebServer#getUrl(String)} throws
     */
    private String getMockServerUrl() throws IOException {
        return mServer.getUrl("/").toString(); //$NON-NLS-1$
    }

    /**
     * Test implementation of {@link AbstractRequest} that throws a {@link BadRequestException} when
     * {@link AbstractRequest#getUrlString} is called.
     */
    private static final class ExceptionThrowingRequest extends BufferedRequest {

        public ExceptionThrowingRequest(final HttpMethod method, final String url,
                final Map<String, String> requestHeaders, final Map<String, String> queryParams,
                final String body) {
            super(method, url, requestHeaders, queryParams, body);
        }

        @Override
        @NonNull
        public String getUrlString(@NonNull final Context context) throws BadRequestException {
            throw new BadRequestException("bad request"); //$NON-NLS-1$
        }
    }
}
