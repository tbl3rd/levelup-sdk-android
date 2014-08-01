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
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.MoreAsserts;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

import org.apache.http.protocol.HTTP;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.LevelUpRequest}.
 */
public final class LevelUpRequestTest extends SupportAndroidTestCase {

    @NonNull
    private static final String TEST_VERSION = "v14";

    @NonNull
    private static final String TEST_ENDPOINT = "endpoint";

    @NonNull
    private Context mMockContext = new MyMockContext(new MyPackageManager(false));

    @SuppressWarnings({ "null", "unused" })
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        /*
         * WHAT YOU SAY !! This is necessary due to a bug in the 2.3 testing framework.
         * http://code.google.com/p/android/issues/detail?id=4244
         */
        if (null == mMockContext) {
            mMockContext = new MyMockContext(new MyPackageManager(false));
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpRequest} parceling without query parameters, a body, or an
     * {@link com.scvngr.levelup.core.net.AccessTokenRetriever}.
     */
    @SmallTest
    public void testParcelable_basic() {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        null);

        ParcelTestUtils.assertParcelableRoundtrips(request);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpRequest} parceling with query parameters, a body, and an
     * {@link com.scvngr.levelup.core.net.AccessTokenRetriever}.
     */
    @SmallTest
    public void testParcelable_full() {
        final HashMap<String, String> query = new HashMap<String, String>();
        query.put("name", "test");
        query.put("test", "hey");

        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, query,
                        new MockRequestBody(), new MockAccessTokenRetriever());

        ParcelTestUtils.assertParcelableRoundtrips(request);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpRequest#getBody(android.content.Context)} when the user's access token
     * should not be appended to the body.
     */
    @SmallTest
    public void testGetBody_empty() {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        null);

        assertEquals(null, request.getBody(mMockContext));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpRequest#getAccessToken(android.content.Context)}.
     */
    @SmallTest
    public void testGetAccessToken() {
        assertEquals(
                "test_access_token", NullUtils.nonNullContract(new LevelUpRequest(mMockContext,
                                HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null, null,
                                new MockAccessTokenRetriever()).getAccessToken(mMockContext))
                        .getAccessToken());

        assertEquals(null, new LevelUpRequest(mMockContext, HttpMethod.GET, TEST_VERSION,
                TEST_ENDPOINT, null, null).getAccessToken(mMockContext));
    }

    @SmallTest
    public void testHeaders_withoutAccessToken() {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT, null,
                        null);

        final HashMap<String, String> expectedHeaders =
                new HashMap<String, String>(RequestUtils.getDefaultRequestHeaders(mMockContext));
        expectedHeaders.put(HTTP.CONTENT_TYPE, RequestUtils.HEADER_CONTENT_TYPE_JSON);
        expectedHeaders.put(LevelUpRequest.HEADER_LEVELUP_API_KEY, "test_api_key");

        final Map<String, String> actualHeaders = request.getRequestHeaders(mMockContext);
        assertFalse(actualHeaders.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertEquals(expectedHeaders, actualHeaders);
    }

    @SmallTest
    public void testHeaders_withAccessToken() {
        final MockAccessTokenRetriever retreiver = new MockAccessTokenRetriever();
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT,
                        null, null, retreiver);

        final HashMap<String, String> expectedHeaders =
                new HashMap<String, String>(RequestUtils.getDefaultRequestHeaders(mMockContext));
        expectedHeaders.put(HTTP.CONTENT_TYPE, RequestUtils.HEADER_CONTENT_TYPE_JSON);
        expectedHeaders.put(LevelUpRequest.HEADER_LEVELUP_API_KEY, "test_api_key");
        expectedHeaders
                .put(LevelUpRequest.HEADER_AUTHORIZATION, String.format(Locale.US,
                        LevelUpRequest.AUTH_TOKEN_TYPE_FORMAT,
                        NullUtils.nonNullContract(retreiver.getAccessToken(mMockContext))
                                .getAccessToken()));

        final Map<String, String> actualHeaders = request.getRequestHeaders(mMockContext);
        assertEquals(expectedHeaders, actualHeaders);
    }

    @SmallTest
    public void testGetFullUrl() {
        final String endpoint = "users/1";
        final String match =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme),
                        getContext().getString(R.string.levelup_api_authority),
                        LevelUpRequest.API_VERSION_CODE_V14, endpoint);
        assertEquals(match, LevelUpRequest.getFullUrl(getContext(),
                LevelUpRequest.API_VERSION_CODE_V14, endpoint));
    }

    @SmallTest
    public void testGetBody_null() {
        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT,
                        null, null);
        assertNull(request.getBody(mMockContext));
    }

    @SmallTest
    public void testRequestBody_fixtureEquals() {
        assertEquals(new MockRequestBody(), new MockRequestBody());
    }

    @SmallTest
    public void testGetBody_fromRequestBody() {
        final RequestBody body = new MockRequestBody();

        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT, null,
                        body);

        assertEquals(MockRequestBody.BODY_FIXTURE, request.getBody(mMockContext));
    }

    @SmallTest
    public void testRequestBody_contentType() {
        final RequestBody body = new MockRequestBody();

        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT, null,
                        body);

        final Map<String, String> requestHeaders = request.getRequestHeaders(mMockContext);
        assertTrue(requestHeaders.containsKey(HTTP.CONTENT_TYPE));
        assertEquals(MockRequestBody.FIXTURE_CONTENT_TYPE, requestHeaders.get(HTTP.CONTENT_TYPE));
    }

    @SmallTest
    public void testRequestBody_contentLength() {
        final RequestBody body = new MockRequestBody();

        final LevelUpRequest request =
                new LevelUpRequest(mMockContext, HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT, null,
                        body);

        final int expectedLength = body.getContentLength();
        assertTrue(0 < expectedLength);

        final Map<String, String> requestHeaders = request.getRequestHeaders(mMockContext);
        assertTrue(requestHeaders.containsKey(HTTP.CONTENT_LEN));
        assertEquals(String.valueOf(expectedLength), requestHeaders.get(HTTP.CONTENT_LEN));
        assertEquals(expectedLength, request.getBodyLength(mMockContext));
    }

    @SmallTest
    public void testEqualsAndHashCode() {
        LevelUpRequest request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        null);
        LevelUpRequest request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        null);
        final Map<String, String> hash = new HashMap<String, String>();
        hash.put("test", "test");

        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Test equalities
        request1 =
                new LevelUpRequest(getContext(), HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT,
                        null, null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT,
                        null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 = new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION + "version",
                TEST_ENDPOINT, null, null);
        request2 = new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION + "version",
                TEST_ENDPOINT, null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT
                        + "endpoint", null, null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT
                        + "endpoint", null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, hash,
                        null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, hash,
                        null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        new MockRequestBody());
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        new MockRequestBody());
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, true);

        // Test differences
        request1 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        null);
        request2 =
                new LevelUpRequest(getContext(), HttpMethod.POST, TEST_VERSION, TEST_ENDPOINT,
                        null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 = new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION + "version",
                TEST_ENDPOINT, null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT
                        + "endpoint", null, null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, hash,
                        null);
        MoreAsserts.checkEqualsAndHashCodeMethods(request1, request2, false);

        request2 =
                new LevelUpRequest(getContext(), HttpMethod.GET, TEST_VERSION, TEST_ENDPOINT, null,
                        new MockRequestBody());
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
        public MyMockContext(@NonNull final MyPackageManager mgr) {
            mMgr = mgr;
        }

        @Override
        public PackageManager getPackageManager() {
            return mMgr;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            final ApplicationInfo info = new ApplicationInfo();
            info.name = "LevelUp-Core-Fallback";
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
         *        {@link android.content.pm.PackageManager.NameNotFoundException} when called.
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
            info.versionName = "2.3.8";
            info.applicationInfo = new ApplicationInfo();
            info.applicationInfo.name = "LevelUp-Core";

            return info;
        }
    }
}
