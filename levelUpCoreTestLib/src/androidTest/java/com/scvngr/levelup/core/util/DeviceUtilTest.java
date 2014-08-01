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

import android.content.res.Resources;
import android.test.mock.MockContext;
import android.test.mock.MockResources;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.DisplayMetrics;

import junit.framework.TestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.DeviceUtil}.
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
     * {@link android.test.mock.MockContext} subclass to allow for changing the Resources {@link android.util.DisplayMetrics}
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
     * {@link android.test.mock.MockResources} subclass to allow for changing the {@link android.util.DisplayMetrics} values.
     */
    private static final class MyMockResources extends MockResources {

        private final DisplayMetrics metrics = new DisplayMetrics();

        @Override
        public DisplayMetrics getDisplayMetrics() {
            return metrics;
        }
    }
}
