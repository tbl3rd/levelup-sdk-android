/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.request.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link LevelUpRequest}.
 */
public final class LevelUpRequestTest extends SupportAndroidTestCase {

    private static final String TEST_VERSION = "v13"; //$NON-NLS-1$

    private static final String TEST_ENDPOINT = "endpoint"; //$NON-NLS-1$

    @Nullable
    private Context mMockContext = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContext = new MyMockContext(new MyPackageManager(false));
    }

    /**
     * Tests {@link LevelUpRequest} parceling.
     */
    @SmallTest
    public void testParcelable_basic() throws BadRequestException {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION,
                        "test", null, (JSONObject) null, null); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpRequest parceledRequest =
                LevelUpRequest.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertFalse(request == parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());
        assertEquals(0, parceledRequest.getQueryParams(mMockContext).size());
        assertEquals(null, parceledRequest.getBody(mMockContext));
        assertEquals(request.getUrl(mMockContext), parceledRequest.getUrl(mMockContext));
        out.recycle();
    }

    /**
     * Tests {@link LevelUpRequest} parceling.
     *
     * @throws MalformedURLException if {@link URL} constructor throws
     */
    @SmallTest
    public void testParcelable_queryParamMapValues() throws MalformedURLException,
            BadRequestException {
        final HashMap<String, String> query = new HashMap<String, String>();
        query.put("name", "test"); //$NON-NLS-1$ //$NON-NLS-2$
        query.put("test", "hey"); //$NON-NLS-1$ //$NON-NLS-2$

        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION,
                        "test", query, (JSONObject) null, null); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpRequest parceledRequest =
                LevelUpRequest.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());
        assertEquals(2, parceledRequest.getQueryParams(mMockContext).size());
        assertEquals(request.getBody(mMockContext), parceledRequest.getBody(mMockContext));
        assertEquals(request.getUrl(mMockContext), parceledRequest.getUrl(mMockContext));
        out.recycle();
    }

    @SmallTest
    public void testParcelable_body() throws MalformedURLException, BadRequestException,
            JSONException {
        final JSONObject object = new JSONObject();
        object.put("test", "test_value"); //$NON-NLS-1$ //$NON-NLS-2$
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION,
                        "test", null, object, null); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpRequest parceledRequest =
                LevelUpRequest.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());
        assertEquals(0, parceledRequest.getQueryParams(mMockContext).size());
        assertEquals(request.getBody(mMockContext), parceledRequest.getBody(mMockContext));
        assertEquals(request.getUrl(mMockContext), parceledRequest.getUrl(mMockContext));
        out.recycle();
    }

    /**
     * Tests {@link LevelUpRequest} parceling.
     *
     * @throws MalformedURLException if {@link URL} constructor throws
     */
    @SmallTest
    public void testParcelable_appendAccessToken() throws MalformedURLException,
            BadRequestException {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION,
                        "test", null, (JSONObject) null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpRequest parceledRequest =
                LevelUpRequest.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        final Map<String, String> headers = request.getRequestHeaders(getContext());
        final Map<String, String> parceledHeaders = parceledRequest.getRequestHeaders(getContext());
        assertEquals(parceledHeaders.size(), parceledHeaders.size());
        assertTrue(parceledHeaders.containsKey(LevelUpRequest.HEADER_CONTENT_TYPE));
        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON,
                parceledHeaders.get(LevelUpRequest.HEADER_CONTENT_TYPE));
        assertTrue(parceledHeaders.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertEquals(String.format(Locale.US, LevelUpRequest.AUTH_TOKEN_TYPE_FORMAT,
                new MockAccessTokenRetriever().getAccessToken(mMockContext).getAccessToken()),
                parceledHeaders.get(LevelUpRequest.HEADER_AUTHORIZATION));
        out.recycle();
    }

    /**
     * Tests {@link LevelUpRequest#getBody(android.content.Context)} when the user's
     * access token should not be appended to the body.
     *
     * @throws Exception if {@link URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetBody_empty() throws Exception {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION,
                        "test", null, (JSONObject) null, null); //$NON-NLS-1$

        assertEquals(null, request.getBody(mMockContext));
    }

    /**
     * Tests {@link LevelUpRequest#getAccessToken(android.content.Context)}.
     */
    @SmallTest
    public void testGetAccessToken() {
        assertEquals("test_access_token", new LevelUpRequest(mMockContext, //$NON-NLS-1$
                HttpMethod.GET, TEST_VERSION, "", null, (JSONObject) null, //$NON-NLS-1$
                new MockAccessTokenRetriever()).getAccessToken(mMockContext).getAccessToken());

        assertEquals(null, new LevelUpRequest(mMockContext, HttpMethod.GET,
                TEST_VERSION, "", null, (JSONObject) null, null) //$NON-NLS-1$
                .getAccessToken(mMockContext));
    }

    @SmallTest
    public void testHeaders_withoutAccessToken() {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION,
                        "test", null, (JSONObject) null, null); //$NON-NLS-1$

        final HashMap<String, String> expectedHeaders =
                new HashMap<String, String>(RequestUtils.getDefaultRequestHeaders(mMockContext));
        expectedHeaders.put(LevelUpRequest.HEADER_CONTENT_TYPE,
                RequestUtils.HEADER_CONTENT_TYPE_JSON);

        final Map<String, String> actualHeaders = request.getRequestHeaders(mMockContext);
        assertFalse(actualHeaders.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertEquals(expectedHeaders, actualHeaders);
    }

    @SmallTest
    public void testHeaders_withAccessToken() {
        final MockAccessTokenRetriever retreiver = new MockAccessTokenRetriever();
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION,
                        "test", null, (JSONObject) null, retreiver); //$NON-NLS-1$

        final HashMap<String, String> expectedHeaders =
                new HashMap<String, String>(RequestUtils.getDefaultRequestHeaders(mMockContext));
        expectedHeaders.put(LevelUpRequest.HEADER_CONTENT_TYPE,
                RequestUtils.HEADER_CONTENT_TYPE_JSON);
        expectedHeaders.put(LevelUpRequest.HEADER_AUTHORIZATION, String.format(
                Locale.US, LevelUpRequest.AUTH_TOKEN_TYPE_FORMAT, retreiver
                        .getAccessToken(mMockContext).getAccessToken()));

        final Map<String, String> actualHeaders = request.getRequestHeaders(mMockContext);
        assertEquals(expectedHeaders, actualHeaders);
    }

    @SmallTest
    public void testGetFullUrl() {
        final String endpoint = "users/1"; //$NON-NLS-1$
        final String match =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority),
                        LevelUpRequest.API_VERSION_CODE_V14, endpoint);
        assertEquals(match, LevelUpRequest.getFullUrl(getContext(),
                LevelUpRequest.API_VERSION_CODE_V14, endpoint));
    }

    @SmallTest
    public void testGetBody_nonNullJsonObject() throws JSONException {
        final JSONObject json =
                new JSONObject("{ 'test1' : 1, 'test2' : 'value2', 'test3' : true}"); //$NON-NLS-1$
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST,
                        LevelUpRequest.API_VERSION_CODE_V14, "test", null, json); //$NON-NLS-1$
        assertEquals(json.toString(), request.getBody(mMockContext));
    }

    @SmallTest
    public void testGetBody_nonNullJsonArray() throws JSONException {
        final JSONArray array = new JSONArray();
        final JSONObject json =
                new JSONObject("{ 'test1' : 1, 'test2' : 'value2', 'test3' : true}"); //$NON-NLS-1$
        array.put(json);
        array.put(json);
        array.put(json);
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION,
                        "test", null, array); //$NON-NLS-1$
        assertEquals(array.toString(), request.getBody(mMockContext));
    }

    @SmallTest
    public void testGetBody_null() throws JSONException {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION,
                        "test", null, (JSONObject) null); //$NON-NLS-1$
        assertNull(request.getBody(mMockContext));
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        LevelUpRequest request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, (JSONObject) null);
        LevelUpRequest request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, (JSONObject) null);
        final Map<String, String> hash = new HashMap<String, String>();
        hash.put("test", "test"); //$NON-NLS-1$ //$NON-NLS-2$

        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Test equalities
        request1 =
                new LevelUpRequest(getContext(), HttpMethod.POST, TEST_VERSION,
                        TEST_ENDPOINT, null, (JSONObject) null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.POST, TEST_VERSION,
                        TEST_ENDPOINT, null, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION
                        + "version", TEST_ENDPOINT, null, (JSONObject) null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION
                        + "version", TEST_ENDPOINT, null, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT + "endpoint", null, (JSONObject) null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT + "endpoint", null, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, hash, (JSONObject) null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, hash, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject());
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject());
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject("{ 'test' : 'test' }")); //$NON-NLS-1$
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject("{ 'test' : 'test' }")); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // JSONObject.toString() alphabetizes the keys... these are equal.
        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject(
                                "{ 'test' : 'test', 'test1' : 'test1' }")); //$NON-NLS-1$
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject(
                                "{ 'test1' : 'test1', 'test' : 'test' }")); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Test differences
        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, (JSONObject) null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.POST, TEST_VERSION,
                        TEST_ENDPOINT, null, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION
                        + "version", TEST_ENDPOINT, null, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT + "endpoint", null, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, hash, (JSONObject) null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject());
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject("{ 'test' : 'test' }")); //$NON-NLS-1$
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION,
                        TEST_ENDPOINT, null, new JSONObject("{ 'test' : 'test2' }")); //$NON-NLS-1$
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = null;
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);
    }

    /**
     * Mock Context to return a fake PackageManager.
     */
    private final class MyMockContext extends MockContext {

        @NonNull
        private final MyPackageManager mMgr;

        /**
         * Constructor.
         *
         * @param mgr the fake package manager to return
         */
        public MyMockContext(@Nullable final MyPackageManager mgr) {
            mMgr = mgr;
        }

        @Override
        public PackageManager getPackageManager() {
            return mMgr;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            final ApplicationInfo info = new ApplicationInfo();
            info.name = "LevelUp-Core-Fallback"; //$NON-NLS-1$
            return info;
        }

        @Override
        public String getPackageName() {
            return null;
        }

        @Override
        public SharedPreferences getSharedPreferences(@Nullable final String name, final int mode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Context getApplicationContext() {
            return this;
        }

        @Override
        public Resources getResources() {
            return getContext().getResources();
        }
    }

    /**
     * Fakes the PackageManager API for getting package data.
     */
    private static final class MyPackageManager extends MockPackageManager {

        private final boolean mThrowException;

        /**
         * Constructor.
         *
         * @param throwException if true, {@link #getPackageInfo(String, int)} will throw a
         *        {@link NameNotFoundException} when called.
         */
        public MyPackageManager(final boolean throwException) {
            mThrowException = throwException;
        }

        @Override
        public PackageInfo getPackageInfo(@Nullable final String packageName, final int flags)
                throws NameNotFoundException {

            if (mThrowException) {
                throw new NameNotFoundException();
            }

            final PackageInfo info = new PackageInfo();
            info.versionName = "2.3.8"; //$NON-NLS-1$
            info.applicationInfo = new ApplicationInfo();
            info.applicationInfo.name = "LevelUp-Core"; //$NON-NLS-1$

            return info;
        }
    }
}
