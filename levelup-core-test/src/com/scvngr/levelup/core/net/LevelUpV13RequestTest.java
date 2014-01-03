package com.scvngr.levelup.core.net;

import android.content.Context;
import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.request.RequestUtils;
import com.scvngr.levelup.core.net.request.RequestUtilsTest.RequestUtilsMockContext;
import com.scvngr.levelup.core.net.request.RequestUtilsTest.RequestUtilsMockPackageManager;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link LevelUpV13Request}.
 */
public final class LevelUpV13RequestTest extends SupportAndroidTestCase {

    private static final String API_VERSION_FOR_TESTING = "v0"; //$NON-NLS-1$

    @Nullable
    private Context mMockContext = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContext =
                new RequestUtilsMockContext(getContext(), new RequestUtilsMockPackageManager(false));
    }

    /**
     * Tests {@link LevelUpV13Request} parceling.
     */
    @SmallTest
    public void testParcelable_basic() throws BadRequestException {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", null, null, null); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpV13Request parceledRequest =
                LevelUpV13Request.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertFalse(request == parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());
        assertEquals(0, parceledRequest.getQueryParams(mMockContext).size());
        assertEquals(null, parceledRequest.getBody(mMockContext));
        assertEquals(request.getUrl(mMockContext), parceledRequest.getUrl(mMockContext));
    }

    /**
     * Tests {@link LevelUpV13Request} parceling.
     *
     * @throws MalformedURLException if {@link URL} constructor throws
     */
    @SmallTest
    public void testParcelable_mapValues() throws MalformedURLException, BadRequestException {
        final HashMap<String, String> query = new HashMap<String, String>();
        query.put("name", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        query.put("test", "hey"); //$NON-NLS-1$ //$NON-NLS-2$

        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET,
                        "test", query, query, null); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpV13Request parceledRequest =
                LevelUpV13Request.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());
        assertEquals(2, parceledRequest.getQueryParams(mMockContext).size());
        assertEquals(request.getBody(mMockContext), parceledRequest.getBody(mMockContext));
        assertEquals(request.getUrl(mMockContext), parceledRequest.getUrl(mMockContext));
    }

    /**
     * Tests {@link LevelUpV13Request} parceling.
     *
     * @throws MalformedURLException if {@link URL} constructor throws
     */
    @SmallTest
    public void testParcelable_appendAccessToken() throws MalformedURLException,
            BadRequestException {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET,
                        "test", null, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpV13Request parceledRequest =
                LevelUpV13Request.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());
        // Access token should be appended
        assertEquals(1, parceledRequest.getQueryParams(mMockContext).size());
        assertEquals(request.getBody(mMockContext), parceledRequest.getBody(mMockContext));
        assertEquals(request.getUrl(mMockContext), parceledRequest.getUrl(mMockContext));
        assertTrue(request.getQueryParams(mMockContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals("test_access_token", request.getQueryParams(mMockContext).get( //$NON-NLS-1$
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertTrue(parceledRequest.getQueryParams(mMockContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals("test_access_token", parceledRequest.getQueryParams(mMockContext).get( //$NON-NLS-1$
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
    }

    /**
     * Tests {@link LevelUpV13Request#getBody(android.content.Context)} when the user's
     * access token should not be appended to the body.
     *
     * @throws Exception if {@link URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetBody_empty() throws Exception {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", null, null, null); //$NON-NLS-1$

        assertEquals(null, request.getBody(mMockContext));
    }

    /**
     * Tests {@link LevelUpV13Request#getQueryParams(android.content.Context)} when the
     * user's access token should be appended to the params.
     *
     * @throws Exception if {@link URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetQueryParams_withAccessToken() throws Exception {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", null, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        assertTrue(request.getQueryParams(mMockContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals("test_access_token", request.getQueryParams(mMockContext).get( //$NON-NLS-1$
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
    }

    /**
     * Tests {@link LevelUpV13Request#getQueryParams(android.content.Context)} when the
     * user's access token should be appended to the params, but there is no access token cached.
     *
     * @throws Exception if {@link URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetQueryParams_withAppendFlagAndNoCachedAccessToken() throws Exception {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", null, null, null); //$NON-NLS-1$

        assertFalse(request.getQueryParams(mMockContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals(
                null,
                request.getQueryParams(mMockContext).get(
                        LevelUpV13Request.PARAM_ACCESS_TOKEN));
    }

    /**
     * Tests {@link LevelUpV13Request#getQueryParams(android.content.Context)} when the
     * user's access token should be appended to the params.
     *
     * @throws Exception if {@link URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetQueryParams_withAppendFlagAndExistingParams() throws Exception {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("a", "aa"); //$NON-NLS-1$ //$NON-NLS-2$
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", params, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        final Map<String, String> query = request.getQueryParams(mMockContext);
        assertTrue(query.containsKey(LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals("test_access_token", query.get(LevelUpV13Request.PARAM_ACCESS_TOKEN)); //$NON-NLS-1$

        assertTrue(query.containsKey("a")); //$NON-NLS-1$
        assertEquals("aa", query.get("a")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests {@link LevelUpV13Request#getQueryParams(android.content.Context)} when the
     * user's access token should not be appended to the params.
     *
     * @throws Exception if {@link URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetQueryParams_noAppendFlag() throws Exception {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", null, null, null); //$NON-NLS-1$

        assertFalse(request.getQueryParams(mMockContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
    }

    /**
     * Tests {@link LevelUpV13Request#getAccessToken(android.content.Context)}.
     */
    @SmallTest
    public void testGetAccessToken() {
        assertEquals(
                "test_access_token", new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING, "", null, //$NON-NLS-1$ //$NON-NLS-2$
                        null, new MockAccessTokenRetriever()).getAccessToken(mMockContext)
                        .getAccessToken());

        assertEquals(null, new LevelUpV13Request(mMockContext, HttpMethod.GET,
                API_VERSION_FOR_TESTING, "", null, null, null) //$NON-NLS-1$
                .getAccessToken(mMockContext));
    }

    /**
     * Tests {@link LevelUpV13Request#getFullJsonUrl}.
     */
    @SmallTest
    public void testGetFullJsonUrl() {
        final String endpoint = "users/1"; //$NON-NLS-1$
        final String match =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s.json", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority),
                        LevelUpV13Request.DEFAULT_API_VERSION, endpoint);
        assertEquals(match, LevelUpV13Request.getFullJsonUrl(getContext(),
                LevelUpV13Request.DEFAULT_API_VERSION, endpoint));
    }

    /**
     * Tests {@link LevelUpV13Request#getEncodedBody(java.util.Map)} with valid body (also
     * tests the alphabetizing of keys).
     *
     * @throws UnsupportedEncodingException (it won't)
     */
    @SmallTest
    public void testGetEncodedBody() throws UnsupportedEncodingException {
        final Map<String, String> body = new HashMap<String, String>();
        body.put("b", "bb"); //$NON-NLS-1$ //$NON-NLS-2$
        body.put("a", "aa"); //$NON-NLS-1$ //$NON-NLS-2$
        body.put("z", "zz"); //$NON-NLS-1$ //$NON-NLS-2$
        body.put("ab", "ab"); //$NON-NLS-1$ //$NON-NLS-2$

        final StringBuilder postBody = new StringBuilder();
        postBody.append(URLEncoder.encode("a", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("="); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("aa", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("&"); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("ab", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("="); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("ab", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("&"); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("b", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("="); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("bb", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("&"); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("z", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("="); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("zz", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(postBody.toString(), LevelUpV13Request.getEncodedBody(body));
    }

    /**
     * Tests {@link LevelUpV13Request#getEncodedBody(java.util.Map)} with null body.
     */
    @SmallTest
    public void testGetEncodedBody_withNullBody() {
        assertNull(LevelUpV13Request.getEncodedBody(null));
    }

    /**
     * Tests {@link LevelUpV13Request#getEncodedBody(java.util.Map)} with empty body.
     */
    @SmallTest
    public void testGetEncodedBody_withEmptyBody() {
        assertNull(LevelUpV13Request.getEncodedBody(new HashMap<String, String>()));
    }

    /**
     * Tests {@link LevelUpV13Request#getUrl(Context)} when it is requested that an access
     * token be appended to the request.
     */
    @SmallTest
    public void testGetUrlWithAppendedAccessToken() throws BadRequestException {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET,
                        "test", null, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        assertTrue(request.getQueryParams(mMockContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals("test_access_token", request.getQueryParams(mMockContext).get( //$NON-NLS-1$
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        final URL url = request.getUrl(mMockContext);
        assertNotNull("request has query params", url.getQuery()); //$NON-NLS-1$
        assertTrue("request query has accessToken", url.getQuery().contains( //$NON-NLS-1$
                "access_token=test_access_token")); //$NON-NLS-1$
    }

    @SmallTest
    public void testHeaders() {
        final LevelUpV13Request request =
                new LevelUpV13Request(mMockContext, HttpMethod.GET, API_VERSION_FOR_TESTING,
                        "test", null, null); //$NON-NLS-1$

        final Map<String, String> expectedHeaders =
                RequestUtils.getDefaultRequestHeaders(mMockContext);
        assertEquals(expectedHeaders, request.getRequestHeaders(mMockContext));
    }

    /**
     * Tests {@link LevelUpV13Request#equals(Object)} and
     * {@link LevelUpV13Request#hashCode()}.
     */
    @SmallTest
    public void testEqualsAndHashCode() {
        LevelUpV13Request request1 =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, null); //$NON-NLS-1$
        LevelUpV13Request request2 =
                new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, null); //$NON-NLS-1$
        final Map<String, String> hash = new HashMap<String, String>();
        hash.put("test", "test"); //$NON-NLS-1$ //$NON-NLS-2$

        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);
        // Make sure that null and empty param/query objects return same
        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", //$NON-NLS-1$
                new HashMap<String, String>(), null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, //$NON-NLS-1$
                new HashMap<String, String>());
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Test equalities
        request1 = new LevelUpV13Request(getContext(), HttpMethod.POST, "", null, null); //$NON-NLS-1$
        request2 = new LevelUpV13Request(getContext(), HttpMethod.POST, "", null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, null); //$NON-NLS-1$
        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", hash, null); //$NON-NLS-1$
        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", hash, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, hash); //$NON-NLS-1$
        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, hash); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // API version
        request1 =
                new LevelUpV13Request(getContext(), HttpMethod.GET,
                        LevelUpV13Request.API_VERSION_CODE_V14, "", null, null); //$NON-NLS-1$
        request2 =
                new LevelUpV13Request(getContext(), HttpMethod.GET,
                        LevelUpV13Request.API_VERSION_CODE_V14, "", null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Test differences
        request1 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, null); //$NON-NLS-1$
        request2 = new LevelUpV13Request(getContext(), HttpMethod.POST, "", null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", hash, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new LevelUpV13Request(getContext(), HttpMethod.GET, "", null, hash); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = null;
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        // API version
        request1 =
                new LevelUpV13Request(getContext(), HttpMethod.GET,
                        LevelUpV13Request.API_VERSION_CODE_V14, "", null, null); //$NON-NLS-1$
        request2 =
                new LevelUpV13Request(getContext(), HttpMethod.GET,
                        LevelUpV13Request.API_VERSION_CODE_V13, "", null, null); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);
    }
}
