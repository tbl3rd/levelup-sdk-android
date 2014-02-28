/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests {@link BufferedRequest}.
 */
public final class BufferedRequestTest extends SupportAndroidTestCase {

    public static final String BASE_URL = "http://www.example.com"; //$NON-NLS-1$

    /**
     * Tests {@link BufferedRequest} parceling.
     *
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testParcelable_basic() throws BadRequestException {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.POST, BASE_URL, new HashMap<String, String>(),
                        new HashMap<String, String>(), null);

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final BufferedRequest parceledRequest = BufferedRequest.CREATOR.createFromParcel(out);

        out.recycle();

        assertEquals(request, parceledRequest);
        assertEquals(0, parceledRequest.getRequestHeaders(getContext()).size());
        assertEquals(0, parceledRequest.getQueryParams(getContext()).size());
        assertEquals(null, parceledRequest.getBody(getContext()));
        assertEquals(0, parceledRequest.getBodyLength(getContext()));
        assertEquals(request.getUrl(getContext()), parceledRequest.getUrl(getContext()));
    }

    /**
     * Tests {@link BufferedRequest} parceling.
     *
     * @throws MalformedURLException if {@link URL} constructor throws
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testParcelable_mapValues() throws MalformedURLException, BadRequestException {
        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("content-length", "200"); //$NON-NLS-1$ //$NON-NLS-2$
        final HashMap<String, String> query = new HashMap<String, String>();
        query.put("name", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        query.put("test", "hey"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, headers, query, "testing"); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final BufferedRequest parceledRequest = BufferedRequest.CREATOR.createFromParcel(out);

        out.recycle();

        assertEquals(request, parceledRequest);
        assertEquals(1, parceledRequest.getRequestHeaders(getContext()).size());
        assertEquals(2, parceledRequest.getQueryParams(getContext()).size());
        assertEquals("testing", parceledRequest.getBody(getContext())); //$NON-NLS-1$
        assertEquals(7, parceledRequest.getBodyLength(getContext()));
        assertEquals(request.getUrl(getContext()), parceledRequest.getUrl(getContext()));
    }

    /**
     * Tests
     * {@link BufferedRequest#BufferedRequest(HttpMethod, String, java.util.Map, java.util.Map, String)}
     * .
     */
    @SmallTest
    public void testConstructor_withNullMethod() {
        try {
            new BufferedRequest(null, BASE_URL, new HashMap<String, String>(),
                    new HashMap<String, String>(), null);
            fail("AssertionError not thrown"); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests
     * {@link BufferedRequest#BufferedRequest(HttpMethod, String, java.util.Map, java.util.Map, String)}
     * .
     */
    @SmallTest
    public void testConstructor_withNullUrl() {
        try {
            new BufferedRequest(HttpMethod.GET, null, new HashMap<String, String>(),
                    new HashMap<String, String>(), null);
            fail("AssertionError not thrown"); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests
     * {@link BufferedRequest#BufferedRequest(HttpMethod, String, java.util.Map, java.util.Map, String)}
     * .
     */
    @SmallTest
    public void testConstructor_withNullHeaders() {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, null, new HashMap<String, String>(),
                        null);

        assertNotNull("Headers should not be null", request.getRequestHeaders(getContext())); //$NON-NLS-1$
        assertEquals(0, request.getRequestHeaders(getContext()).size());
    }

    /**
     * Tests
     * {@link BufferedRequest#BufferedRequest(HttpMethod, String, java.util.Map, java.util.Map, String)}
     * .
     */
    @SmallTest
    public void testConstructor_withNullQueryParams() {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, new HashMap<String, String>(), null,
                        null);

        assertNotNull("Query Params should not be null", request.getQueryParams(getContext())); //$NON-NLS-1$
        assertEquals(0, request.getQueryParams(getContext()).size());
    }

    /**
     * Tests {@link BufferedRequest#getUrl(android.content.Context)}.
     *
     * @throws Exception if the url we are checking against is malformed
     */
    @SmallTest
    public void testGetUrl_basic() throws Exception {
        final String url = BASE_URL;
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, url, new HashMap<String, String>(), null, null);

        final URL requestUrl = request.getUrl(getContext());
        assertNull("URL should have no query part", requestUrl.getQuery()); //$NON-NLS-1$
        assertEquals(new URL(url), requestUrl);
        assertEquals("www.example.com", requestUrl.getHost()); //$NON-NLS-1$
        assertEquals("http", requestUrl.getProtocol()); //$NON-NLS-1$
    }

    /**
     * Tests {@link BufferedRequest#getUrl(android.content.Context)}.
     *
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testGetUrl_withQueryParams() throws BadRequestException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("param2", "param"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("test_value", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, new HashMap<String, String>(),
                        params, null);

        final URL requestUrl = request.getUrl(getContext());
        assertNotNull("URL should have query part", requestUrl.getQuery()); //$NON-NLS-1$
        assertEquals("param2=param&test_value=test", requestUrl.getQuery()); //$NON-NLS-1$
    }

    /**
     * Tests {@link BufferedRequest#getUrl(android.content.Context)}.
     *
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testGetUrl_withOutOfOrderQueryParams() throws BadRequestException {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("zzz", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("test_value", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("param2", "param"); //$NON-NLS-1$ //$NON-NLS-2$
        params.put("aaaa", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, new HashMap<String, String>(),
                        params, null);

        final URL requestUrl = request.getUrl(getContext());
        assertNotNull("URL should have query part", requestUrl.getQuery()); //$NON-NLS-1$
        assertEquals("aaaa=test&param2=param&test_value=test&zzz=test", requestUrl.getQuery()); //$NON-NLS-1$
    }

    /**
     * Tests {@link BufferedRequest#getUrl(android.content.Context)}.
     *
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testGetUrl_withMalformedUrl() throws BadRequestException {
        final String url = "httpwwwexamplecom"; //$NON-NLS-1$
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, url, new HashMap<String, String>(), null, null);

        try {
            request.getUrl(getContext());
            fail("Expected exception"); //$NON-NLS-1$
        } catch (final BadRequestException e) {
            // Expected exception.
        }
    }

    @SmallTest
    public void testGetBodyLength() {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, new HashMap<String, String>(), null,
                        "test"); //$NON-NLS-1$
        assertEquals(4, request.getBodyLength(getContext()));
    }

    @SmallTest
    public void testGetBodyLength_nullBody() {
        final BufferedRequest request =
                new BufferedRequest(HttpMethod.GET, BASE_URL, new HashMap<String, String>(), null,
                        null);
        assertEquals(0, request.getBodyLength(getContext()));
    }

    /**
     * Tests the {@link BufferedRequest#equals(Object)} and {@link BufferedRequest#hashCode()}
     * methods.
     */
    @SmallTest
    public void testEqualsAndHashCode() {
        BufferedRequest request1 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, null, null);
        BufferedRequest request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, null, null);
        final Map<String, String> hash = new HashMap<String, String>();
        hash.put("test", "test"); //$NON-NLS-1$ //$NON-NLS-2$

        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);
        // Make sure that null and empty param/query objects return same
        request2 =
                new BufferedRequest(HttpMethod.GET, BASE_URL, new HashMap<String, String>(), null,
                        null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request2 =
                new BufferedRequest(HttpMethod.GET, BASE_URL, null, new HashMap<String, String>(),
                        null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Modify various params in the right object making sure the tests fail
        request2 = new BufferedRequest(HttpMethod.POST, BASE_URL, null, null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL + "/test.json", null, null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, hash, null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, hash, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, hash, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, null, "test"); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        // Test all of the fields set to custom values return same
        request1 = new BufferedRequest(HttpMethod.POST, BASE_URL, null, null, null);
        request2 = new BufferedRequest(HttpMethod.POST, BASE_URL, null, null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new BufferedRequest(HttpMethod.GET, BASE_URL + "/.", null, null, null); //$NON-NLS-1$
        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL + "/.", null, null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new BufferedRequest(HttpMethod.GET, BASE_URL, hash, null, null);
        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, hash, null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, hash, null);
        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, hash, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, null, "test"); //$NON-NLS-1$
        request2 = new BufferedRequest(HttpMethod.GET, BASE_URL, null, null, "test"); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request2 = null;
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);
    }
}
