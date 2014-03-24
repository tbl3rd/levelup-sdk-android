/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.res.Resources;
import android.test.mock.MockContext;
import android.test.mock.MockResources;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.DisplayMetrics;

import junit.framework.TestCase;

/**
 * Tests {@link DeviceUtil}.
 */
public final class DeviceUtilTest extends TestCase {

    @SmallTest
    public void testGetDeviceDensityString() {
        final MyMockContext context = new MyMockContext();

        {
            // Even currently unrecognized density values below medium should return 1x
            context.resources.metrics.densityDpi = 100;
            assertEquals(DeviceUtil.DENSITY_1X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            context.resources.metrics.densityDpi = DisplayMetrics.DENSITY_DEFAULT;
            assertEquals(DeviceUtil.DENSITY_1X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            context.resources.metrics.densityDpi = DisplayMetrics.DENSITY_HIGH;
            assertEquals(DeviceUtil.DENSITY_1_5X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            // We should return the medium density for anything less than high.
            context.resources.metrics.densityDpi = DisplayMetrics.DENSITY_HIGH - 1;
            assertEquals(DeviceUtil.DENSITY_1X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            context.resources.metrics.densityDpi = DisplayMetrics.DENSITY_LOW;
            assertEquals(DeviceUtil.DENSITY_1X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            context.resources.metrics.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
            assertEquals(DeviceUtil.DENSITY_1X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            context.resources.metrics.densityDpi = DisplayMetrics.DENSITY_XHIGH;
            assertEquals(DeviceUtil.DENSITY_2X, DeviceUtil.getDeviceDensityString(context));
        }

        {
            // Even currently unrecognized density values above medium should return 2x
            context.resources.metrics.densityDpi = 400;
            assertEquals(DeviceUtil.DENSITY_2X, DeviceUtil.getDeviceDensityString(context));
        }
    }

    /**
     * {@link MockContext} subclass to allow for changing the Resources {@link DisplayMetrics}
     * values.
     */
    private static final class MyMockContext extends MockContext {

        private final MyMockResources resources = new MyMockResources();

        @Override
        public Resources getResources() {
            return resources;
        }
    }

    /**
     * {@link MockResources} subclass to allow for changing the {@link DisplayMetrics} values.
     */
    private static final class MyMockResources extends MockResources {

        private final DisplayMetrics metrics = new DisplayMetrics();

        @Override
        public DisplayMetrics getDisplayMetrics() {
            return metrics;
        }
    }
}
