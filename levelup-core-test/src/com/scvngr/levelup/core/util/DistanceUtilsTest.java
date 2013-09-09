package com.scvngr.levelup.core.util;

import android.test.AndroidTestCase;

import com.scvngr.levelup.core.util.DistanceUtil.DistanceUnit;

/**
 * Tests {@link DistanceUtil}. Expected values were computed manually using GNU Units.
 */
public final class DistanceUtilsTest extends AndroidTestCase {

    private static final double ASSERT_DELTA = 0.01;

    /**
     * Tests {@link DistanceUtil#convertDistance(float, DistanceUnit)} with {@link DistanceUnit#MILE}.
     */
    public void testDistanceConversion_miles() {
        final DistanceUnit unit = DistanceUnit.MILE;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(0.62137, DistanceUtil.convertDistance(1000, unit), ASSERT_DELTA);
        assertEquals(62.13711, DistanceUtil.convertDistance(100000, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link DistanceUtil#convertDistance(float, DistanceUnit)} with {@link DistanceUnit#METER}.
     */
    public void testDistanceConversion_meters() {
        final DistanceUnit unit = DistanceUnit.METER;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(1, DistanceUtil.convertDistance(1, unit), ASSERT_DELTA);
        assertEquals(100, DistanceUtil.convertDistance(100, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link DistanceUtil#convertDistance(float, DistanceUnit)} with {@link DistanceUnit#FOOT}.
     */
    public void testDistanceConversion_foot() {
        final DistanceUnit unit = DistanceUnit.FOOT;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(3.28083, DistanceUtil.convertDistance(1, unit), ASSERT_DELTA);
        assertEquals(328.0839, DistanceUtil.convertDistance(100, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link DistanceUtil#convertDistance(float, DistanceUnit)} with
     * {@link DistanceUnit#KILOMETER}.
     */
    public void testDistanceConversion_kilometer() {
        final DistanceUnit unit = DistanceUnit.KILOMETER;
        assertEquals(0, DistanceUtil.convertDistance(0, unit), ASSERT_DELTA);
        assertEquals(1, DistanceUtil.convertDistance(1000, unit), ASSERT_DELTA);
        assertEquals(100, DistanceUtil.convertDistance(100000, unit), ASSERT_DELTA);
    }

    /**
     * Tests {@link DistanceUtil#roundDistance(float)}.
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
