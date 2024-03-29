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
import android.os.Build;
import android.support.annotation.NonNull;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.CoreLibConstants;
import com.scvngr.levelup.core.util.NullUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.RequestUtils}.
 */
public final class RequestUtilsTest extends SupportAndroidTestCase {
    /**
     * Eventually non-null; initialized in {@link #setUp()}.
     */
    @SuppressWarnings("null")
    @NonNull
    private Context mMockContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContext =
                new RequestUtilsMockContext(getContext(), new RequestUtilsMockPackageManager());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.RequestUtils#getDefaultRequestHeaders(android.content.Context)} when a
     * {@link android.content.pm.PackageManager.NameNotFoundException} is not thrown.
     */
    @SmallTest
    public void testGetDefaultRequestHeaders() {
        final Map<String, String> headers = RequestUtils.getDefaultRequestHeaders(mMockContext);
        assertTrue("Device model header is present",
                headers.containsKey("X-Device-Model"));
        assertEquals("Accept header is set for JSON", //
                RequestUtils.HEADER_CONTENT_TYPE_JSON, headers.get(RequestUtils.HEADER_ACCEPT));
        assertEquals("User-Agent header matches RequestUtils.getUserAgent()",
                RequestUtils.getUserAgent(mMockContext),
                headers.get("User-Agent"));
    }

    @SmallTest
    public void testGetUserAgent() {
        assertTrue(RequestUtils.getUserAgent(mMockContext).contains(
                "com.example.testapp/2.3.8" + getDeviceSpecificUserAgentString()));
        assertTrue(RequestUtils.getUserAgent(mMockContext).contains(
                "LevelUpSdk/" + CoreLibConstants.SDK_VERSION));
        /*
         * Ensure we're not using properties that come back null in our User-Agent (like
         * applicationInfo.name). This is a mocked context so it's not an ideal test, but there's
         * not a great way to unit-test real-world values.
         */
        assertFalse(
                "User-Agent should not have 'null' for any of its properties",
                RequestUtils.getUserAgent(mMockContext).contains(
                "null"));
    }

    /**
     * Test {@link com.scvngr.levelup.core.net.RequestUtils#addApiKeyToRequestQueryParams}.
     */
    @SmallTest
    public void testAddApiKeyToRequestQueryParams() {
        final Map<String, String> expected = new HashMap<String, String>();
        final Map<String, String> params = new HashMap<String, String>();
        final Context context = NullUtils.nonNullContract(getContext());

        expected.put(RequestUtils.PARAM_API_KEY, context.getString(R.string.levelup_api_key));

        RequestUtils.addApiKeyToRequestQueryParams(context, params);
        assertEquals(expected.toString(), params.toString());
    }

    /**
     * Test {@link com.scvngr.levelup.core.net.RequestUtils#addApiKeyToRequestBody}.
     *
     * @throws org.json.JSONException from {@link org.json.JSONObject#put}
     */
    @SmallTest
    public void testAddApiKeyToRequestBody() throws JSONException {
        final JSONObject object = new JSONObject();
        final JSONObject expected = new JSONObject();
        final Context context = NullUtils.nonNullContract(getContext());

        expected.put(RequestUtils.PARAM_API_KEY, context.getString(R.string.levelup_api_key));

        RequestUtils.addApiKeyToRequestBody(context, object);
        assertEquals(expected.toString(), object.toString());
    }

    /**
     * Test {@link com.scvngr.levelup.core.net.RequestUtils#addDeviceIdToRequestBody}.
     *
     * @throws org.json.JSONException from {@link org.json.JSONObject#getString}
     */
    @SmallTest
    public void testAddDeviceIdToRequestBody() throws JSONException {
        final JSONObject object = new JSONObject();
        final Context context = NullUtils.nonNullContract(getContext());
        RequestUtils.addDeviceIdToRequestBody(context, object);

        assertTrue(object.has(RequestUtils.PARAM_DEVICE_IDENTIFIER));
        assertEquals(RequestUtils.getDeviceId(context),
                object.getString(RequestUtils.PARAM_DEVICE_IDENTIFIER));
    }

    /**
     * Helper method to get the device specific portion of the user agent screen.
     *
     * @return device specific part of the user agent string
     */
    private static String getDeviceSpecificUserAgentString() {
        return String.format(Locale.US, " (Linux; U; Android %s; %s/%s; %s)",
                Build.VERSION.RELEASE, Build.BRAND, Build.PRODUCT, Locale.getDefault().toString());
    }

    /**
     * Mock Context to return a fake PackageManager.
     */
    public static final class RequestUtilsMockContext extends MockContext {

        @NonNull
        private final RequestUtilsMockPackageManager mMgr;
        @NonNull
        private final Context mContext;

        /**
         * Constructor.
         *
         * @param context the context.
         * @param mgr the fake package manager to return
         */
        public RequestUtilsMockContext(@NonNull final Context context,
                @NonNull final RequestUtilsMockPackageManager mgr) {
            mContext = context;
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
        public SharedPreferences getSharedPreferences(final String name, final int mode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Context getApplicationContext() {
            return mContext.getApplicationContext();
        }

        @Override
        public Resources getResources() {
            return mContext.getResources();
        }
    }

    /**
     * Fakes the PackageManager API for getting package data.
     */
    public static final class RequestUtilsMockPackageManager extends MockPackageManager {
        @Override
        public PackageInfo getPackageInfo(final String packageName, final int flags)
                throws NameNotFoundException {
            final PackageInfo info = new PackageInfo();
            info.versionName = "2.3.8";
            info.applicationInfo = new ApplicationInfo();
            info.applicationInfo.name = null; // This is the case for many apps.
            info.applicationInfo.packageName = "com.example.testapp";

            return info;
        }
    }
}
