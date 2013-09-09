package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;

/**
 * Tests {@link BuildUtil}.
 */
public final class BuildUtilTest extends AndroidTestCase {

    @SmallTest
    public void testGetLabel() {
        final Context mockContext = new TestContext(31415927, "pi", "LevelUp"); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(31415927, BuildUtil.getVersionCode(mockContext));
    }

    @SmallTest
    public void testGetVersionCode() {
        final Context mockContext = new TestContext(31415927, "pi", "LevelUp"); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(31415927, BuildUtil.getVersionCode(mockContext));
    }

    @SmallTest
    public void testGetVersionName() {
        final Context mockContext = new TestContext(31415927, "pi", "LevelUp"); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals("pi", BuildUtil.getVersionName(mockContext)); //$NON-NLS-1$
    }

    private static final class TestContext extends MockContext {

        @NonNull
        private final PackageManager mPackageManager;

        /**
         * @param versionCode Mock versionCode.
         * @param versionName Mock versionName.
         * @param label Mock label.
         */
        public TestContext(final int versionCode, @NonNull final String versionName,
                final String label) {
            mPackageManager = new TestPackageManager(versionCode, versionName, label);
        }

        @Override
        public PackageManager getPackageManager() {
            return mPackageManager;
        }

        @Override
        public String getPackageName() {
            return "com.example.test"; //$NON-NLS-1$
        }

        private static final class TestPackageManager extends MockPackageManager {

            @NonNull
            private final String mLabel;

            private final int mVersionCode;

            @NonNull
            private final String mVersionName;

            /**
             * @param versionCode Mock versionCode.
             * @param versionName Mock versionName.
             * @param label Mock label.
             */
            public TestPackageManager(final int versionCode, @NonNull final String versionName,
                    @NonNull final String label) {
                mVersionCode = versionCode;
                mVersionName = versionName;
                mLabel = label;
            }

            @Override
            public PackageInfo getPackageInfo(final String packageName, final int flags)
                    throws NameNotFoundException {

                final PackageInfo info = new PackageInfo();
                info.versionName = mVersionName;
                info.versionCode = mVersionCode;

                return info;
            }

            @Override
            public CharSequence getApplicationLabel(final ApplicationInfo info) {
                return mLabel;
            }
        }
    }
}
