/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.BuildUtil}.
 */
public final class BuildUtilTest extends SupportAndroidTestCase {

    private static final int VERSION_CODE_FIXTURE = 31415927;

    @NonNull
    private static final String VERSION_NAME_FIXTURE = "pi";

    @NonNull
    private static final String LABEL_FIXTURE = "LevelUp";

    @SmallTest
    public void testGetLabel() {
        final Context mockContext =
                new TestContext(VERSION_CODE_FIXTURE, VERSION_NAME_FIXTURE, LABEL_FIXTURE);

        assertEquals(VERSION_CODE_FIXTURE, BuildUtil.getVersionCode(mockContext));
    }

    @SmallTest
    public void testGetVersionCode() {
        final Context mockContext =
                new TestContext(VERSION_CODE_FIXTURE, VERSION_NAME_FIXTURE, LABEL_FIXTURE);

        assertEquals(VERSION_CODE_FIXTURE, BuildUtil.getVersionCode(mockContext));
    }

    @SmallTest
    public void testGetVersionName() {
        final Context mockContext =
                new TestContext(VERSION_CODE_FIXTURE, VERSION_NAME_FIXTURE, LABEL_FIXTURE);

        assertEquals(VERSION_NAME_FIXTURE, BuildUtil.getVersionName(mockContext));
    }

    /**
     * A mock context to use {@link com.scvngr.levelup.core.util.BuildUtilTest.TestContext.TestPackageManager}.
     */
    private static final class TestContext extends MockContext {

        @NonNull
        private final PackageManager mPackageManager;

        /**
         * @param versionCode Mock versionCode.
         * @param versionName Mock versionName.
         * @param label Mock label.
         */
        public TestContext(final int versionCode, @NonNull final String versionName,
                @NonNull final String label) {
            mPackageManager = new TestPackageManager(versionCode, versionName, label);
        }

        @Override
        public PackageManager getPackageManager() {
            return mPackageManager;
        }

        @Override
        public String getPackageName() {
            return "com.example.test";
        }

        /**
         * A mock package manager that returns a fixed version code, version name, and label.
         */
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
