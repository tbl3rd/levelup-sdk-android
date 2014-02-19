/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.tip;

import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link PercentageTip}.
 */
public final class PercentageTipTest extends SupportAndroidTestCase {

    /**
     * Test equals and hash code.
     */
    @SmallTest
    public void testEqualsAndHashCode() {
        final PercentageTip tip1 = new PercentageTip(0);
        final PercentageTip tip2 = new PercentageTip(50);

        MoreAsserts.checkEqualsAndHashCodeMethods(tip1, tip1, true);
        MoreAsserts.checkEqualsAndHashCodeMethods(tip1, tip2, false);
    }

    /**
     * Test {@link PercentageTip#getEncodedValue} gets the value used to encode the tip.
     */
    @SmallTest
    public void testGetEncodedValue() {
        // Minimum
        {
            final int value = 0;
            final PercentageTip tip = new PercentageTip(value);
            assertEquals(PercentageTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL, tip.getEncodedValue());
            assertEquals(PercentageTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL, 0);
        }

        // Typical
        {
            final int value = 250;
            final PercentageTip tip = new PercentageTip(value);
            assertEquals(value, tip.getValue());
            assertEquals(PercentageTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL + value, tip.getEncodedValue());
        }

        // Too small
        try {
            new PercentageTip(-1);
            fail();
        } catch (final IllegalArgumentException e) {
            // Intentionally blank
        }

        // Too large
        try {
            new PercentageTip(PercentageTip.MAXIMUM_VALUE_WITH_OFFSET_DECIMAL
                    - PercentageTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL + 1);
            fail();
        } catch (final IllegalArgumentException e) {
            // Intentionally blank
        }
    }

    /**
     * Test {@link PercentageTip#getValue} gets the value used to construct the tip.
     */
    @SmallTest
    public void testGetValue() {
        assertEquals(0, new PercentageTip(0).getValue());

        final int value = 25;
        assertEquals(value, new PercentageTip(value).getValue());
    }

    /**
     * Test {@link android.os.Parcelable} implementation.
     */
    @SmallTest
    public void testParcel() {
        ParcelTestUtils.assertParcelableRoundtrips(new PercentageTip(0));
        ParcelTestUtils.assertParcelableRoundtrips(new PercentageTip(50));
    }
}