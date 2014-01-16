/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.os.Build;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link EnvironmentUtil}.
 */
public final class EnvironmentUtilTest extends SupportAndroidTestCase {

    @SmallTest
    public void testSdk11Check() {
        assertEquals(Build.VERSION.SDK_INT >= 11, EnvironmentUtil.isSdk12OrGreater());
    }

    @SmallTest
    public void testSdk12Check() {
        assertEquals(Build.VERSION.SDK_INT >= 12, EnvironmentUtil.isSdk12OrGreater());
    }

    @SmallTest
    public void testSdk14Check() {
        assertEquals(Build.VERSION.SDK_INT >= 14, EnvironmentUtil.isSdk14OrGreater());
    }

    @SmallTest
    public void testSdk16Check() {
        assertEquals(Build.VERSION.SDK_INT >= 16, EnvironmentUtil.isSdk16OrGreater());
    }
}
