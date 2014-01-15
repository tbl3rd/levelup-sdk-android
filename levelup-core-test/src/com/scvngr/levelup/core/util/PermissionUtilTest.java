/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.pm.PackageManager;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link PermissionUtil}.
 */
public final class PermissionUtilTest extends SupportAndroidTestCase {

    @SmallTest
    public void testIsPermissionGranted_yes() {
        final TestContext context = new TestContext(new TestPackageManager(
                PackageManager.PERMISSION_GRANTED));

        assertTrue(PermissionUtil.isPermissionGranted(context, "test")); //$NON-NLS-1$
    }

    @SmallTest
    public void testIsPermissionGranted_no() {
        final TestContext context = new TestContext(new TestPackageManager(
                PackageManager.PERMISSION_DENIED));

        assertFalse(PermissionUtil.isPermissionGranted(context, "test")); //$NON-NLS-1$
    }

    /**
     * Mock Context to return a fake PackageManager.
     */
    private static final class TestContext extends MockContext {

        final TestPackageManager mMgr;

        /**
         * Constructor.
         *
         * @param mgr the fake package manager to return
         */
        public TestContext(final TestPackageManager mgr) {
            mMgr = mgr;
        }

        @Override
        public PackageManager getPackageManager() {
            return mMgr;
        }

        @Override
        public String getPackageName() {
            return null;
        }
    }

    /**
     * Fakes the PackageManager API for getting package data.
     */
    private static final class TestPackageManager extends MockPackageManager {

        private final int mPermissionCheckValue;

        /**
         * @param permissionCheckValue the value to return when a permission check occurs.
         */
        public TestPackageManager(final int permissionCheckValue) {
            mPermissionCheckValue = permissionCheckValue;
        }

        @Override
        public int checkPermission(final String permName, final String pkgName) {
            return mPermissionCheckValue;
        }
    }
}
