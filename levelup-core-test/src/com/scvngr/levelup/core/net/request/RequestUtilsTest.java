package com.scvngr.levelup.core.net.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.CoreLibConstants;

import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link RequestUtils}.
 */
public final class RequestUtilsTest extends AndroidTestCase {

    @Nullable
    private Context mMockContext = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContext =
                new RequestUtilsMockContext(getContext(), new RequestUtilsMockPackageManager(false));
    }

    /**
     * Tests {@link RequestUtils#getNestedParameterKey(String, String)} with valid params.
     */
    @SmallTest
    public void testGetNestedParameterKey_valid() {
        final String key = RequestUtils.getNestedParameterKey("outer", "inner"); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals("outer[inner]", key); //$NON-NLS-1$
    }

    /**
     * Tests {@link RequestUtils#getNestedParameterKey(String, String)} with null params.
     */
    @SmallTest
    public void testGetNestedParameterKey_nullParameters() {
        try {
            RequestUtils.getNestedParameterKey(null, ""); //$NON-NLS-1$
            fail("null parameters should throw illegal argument exception"); //$NON-NLS-1$
        } catch (final IllegalArgumentException e) {
            // Expected exception
        }

        try {
            RequestUtils.getNestedParameterKey("", null); //$NON-NLS-1$
            fail("null parameters should throw illegal argument exception"); //$NON-NLS-1$
        } catch (final IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests {@link RequestUtils#getDefaultRequestHeaders(android.content.Context)} when a
     * {@link NameNotFoundException} is thrown.
     */
    @SmallTest
    public void testGetDefaultRequestHeaders_withExceptionThrown() {
        final Map<String, String> headers = RequestUtils.getDefaultRequestHeaders(mMockContext);
        assertTrue("User Agent header is present", headers.containsKey("User-Agent")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue("Device model header is present", headers.containsKey("X-Device-Model")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue("Accepts header is present", headers.containsKey(RequestUtils.HEADER_ACCEPT)); //$NON-NLS-1$
        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON, headers.get(RequestUtils.HEADER_ACCEPT));
    }

    /**
     * Tests {@link RequestUtils#getDefaultRequestHeaders(android.content.Context)} when a
     * {@link NameNotFoundException} is not thrown.
     */
    @SmallTest
    public void testGetDefaultRequestHeaders_withoutExceptionThrown() {
        final Map<String, String> headers = RequestUtils.getDefaultRequestHeaders(mMockContext);
        assertTrue("User Agent header is present", headers.containsKey("User-Agent")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue("Device model header is present", headers.containsKey("X-Device-Model")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue("Accepts header is present", headers.containsKey("Accept")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests {@link RequestUtils#getUserAgent(Context)} when a {@link NameNotFoundException} is not thrown.
     */
    @SmallTest
    public void testGetUserAgent_withoutExceptionThrown() {
        assertTrue(RequestUtils.getUserAgent(mMockContext).contains(
                "LevelUp-Core/2.3.8" + getDeviceSpecificUserAgentString())); //$NON-NLS-1$
        assertTrue(RequestUtils.getUserAgent(mMockContext).contains(
                RequestUtils.getUserAgentSdkVersionString(mMockContext)));
    }

    @SmallTest
    public void testGetUserAgentAppVersionString() {
        assertEquals("LevelUp-Core/2.3.8", RequestUtils.getUserAgentAppVersionString(mMockContext)); //$NON-NLS-1$
    }

    @SmallTest
    public void testGetUserAgentSdkVersionString() {
        assertEquals(
                "LevelUpSdk/" + CoreLibConstants.SDK_VERSION, RequestUtils.getUserAgentSdkVersionString(mMockContext)); //$NON-NLS-1$
    }

    /**
     * Helper method to get the device specific portion of the user agent screen.
     *
     * @return device specific part of the user agent string
     */
    private static String getDeviceSpecificUserAgentString() {
        return String.format(Locale.US, " (Linux; U; Android %s; %s/%s; %s)", //$NON-NLS-1$
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
            info.name = "LevelUp-Core-Fallback"; //$NON-NLS-1$
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

        private final boolean mThrowException;

        /**
         * Constructor.
         *
         * @param throwException if true, {@link #getPackageInfo(String, int)} will throw a
         *        {@link NameNotFoundException} when called.
         */
        public RequestUtilsMockPackageManager(final boolean throwException) {
            mThrowException = throwException;
        }

        @Override
        public PackageInfo getPackageInfo(final String packageName, final int flags)
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
