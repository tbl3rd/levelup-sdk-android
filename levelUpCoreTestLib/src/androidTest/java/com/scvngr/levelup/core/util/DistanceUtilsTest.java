/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit;

/**
 * Tests {@link com.scvngr.levelup.core.util.DistanceUtil}. Expected values were computed manually using GNU Units.
 */
public final class DistanceUtilsTest extends SupportAndroidTestCase {

    private static final double ASSERT_DELTA = 0.01;

    /**
     * Tests {@link com.scvngr.levelup.core.util.DistanceUtil#convertDistance(float, com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit)} with {@link com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit#MILE}.
     */
    public void testDistanceConversion_miles() {
        final DistanceUnit unit = DistanceUnit.MILE;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(0.62137, DistanceUtil.convertDistance(1000, unit), ASSERT_DELTA);
        assertEquals(62.13711, DistanceUtil.convertDistance(100000, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.util.DistanceUtil#convertDistance(float, com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit)} with {@link com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit#METER}.
     */
    public void testDistanceConversion_meters() {
        final DistanceUnit unit = DistanceUnit.METER;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(1, DistanceUtil.convertDistance(1, unit), ASSERT_DELTA);
        assertEquals(100, DistanceUtil.convertDistance(100, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.util.DistanceUtil#convertDistance(float, com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit)} with {@link com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit#FOOT}.
     */
    public void testDistanceConversion_foot() {
        final DistanceUnit unit = DistanceUnit.FOOT;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(3.28083, DistanceUtil.convertDistance(1, unit), ASSERT_DELTA);
        assertEquals(328.0839, DistanceUtil.convertDistance(100, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.util.DistanceUtil#convertDistance(float, com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit)} with
     * {@link com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit#KILOMETER}.
     */
    public void testDistanceConversion_kilometer() {
        final DistanceUnit unit = DistanceUnit.KILOMETER;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(1, DistanceUtil.convertDistance(1000, unit), ASSERT_DELTA);
        assertEquals(100, DistanceUtil.convertDistance(100000, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.util.DistanceUtil#roundDistance(float)}.
     */
    public void testRoundDistance() {
        assertEquals(0.0f, DistanceUtil.roundDistance(0.0f), ASSERT_DELTA);
        assertEquals(0.5f, DistanceUtil.roundDistance(0.1f), ASSERT_DELTA);
        assertEquals(0.5f, DistanceUtil.roundDistance(0.5f), ASSERT_DELTA);
        assertEquals(1.0f, DistanceUtil.roundDistance(0.6f), ASSERT_DELTA);
        assertEquals(1.0f, DistanceUtil.roundDistance(0.9999999f), ASSERT_DELTA);
        assertEquals(1.5f, DistanceUtil.roundDistance(1.1f), ASSERT_DELTA);
        assertEquals(100.5f, DistanceUtil.roundDistance(100.1f), ASSERT_DELTA);
        assertEquals(101.0f, DistanceUtil.roundDistance(100.7f), ASSERT_DELTA);
    }
}