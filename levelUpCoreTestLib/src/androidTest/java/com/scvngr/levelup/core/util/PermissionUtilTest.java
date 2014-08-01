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
package com.scvngr.levelup.core.util;

import android.content.pm.PackageManager;
import android.test.mock.MockContext;
import android.test.mock.MockPackageManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.PermissionUtil}.
 */
public final class PermissionUtilTest extends SupportAndroidTestCase {

    @SmallTest
    public void testIsPermissionGranted_yes() {
        final TestContext context = new TestContext(new TestPackageManager(
                PackageManager.PERMISSION_GRANTED));

        assertTrue(PermissionUtil.isPermissionGranted(context, "test"));
    }

    @SmallTest
    public void testIsPermissionGranted_no() {
        final TestContext context = new TestContext(new TestPackageManager(
                PackageManager.PERMISSION_DENIED));

        assertFalse(PermissionUtil.isPermissionGranted(context, "test"));
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
