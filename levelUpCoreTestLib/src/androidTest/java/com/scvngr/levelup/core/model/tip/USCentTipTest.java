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
package com.scvngr.levelup.core.model.tip;

import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.tip.USCentTip}.
 */
public final class USCentTipTest extends SupportAndroidTestCase {

    /**
     * Test equals and hash code.
     */
    @SmallTest
    public void testEqualsAndHashCode() {
        final USCentTip tip1 = new USCentTip(0);
        final USCentTip tip2 = new USCentTip(50);

        MoreAsserts.checkEqualsAndHashCodeMethods(tip1, tip1, true);
        MoreAsserts.checkEqualsAndHashCodeMethods(tip1, tip2, false);
    }

    /**
     * Test {@link com.scvngr.levelup.core.model.tip.USCentTip#getEncodedValue} gets the value used to encode the tip.
     */
    @SmallTest
    public void testGetEncodedValue() {
        // Minimum
        {
            final int value = 0;
            final USCentTip tip = new USCentTip(value);
            assertEquals(USCentTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL, tip.getEncodedValue());
            assertEquals(USCentTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL,
                    PercentageTip.MAXIMUM_VALUE_WITH_OFFSET_DECIMAL + 1);
        }

        // Typical
        {
            final int value = 250;
            final USCentTip tip = new USCentTip(value);
            assertEquals(value, tip.getValue());
            assertEquals(USCentTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL + value, tip.getEncodedValue());
        }

        // Too small
        try {
            new USCentTip(-1);
            fail();
        } catch (final IllegalArgumentException e) {
            // Intentionally blank
        }

        // Too large
        try {
            new USCentTip(USCentTip.MAXIMUM_VALUE_WITH_OFFSET_DECIMAL
                    - USCentTip.MINIMUM_VALUE_WITH_OFFSET_DECIMAL + 1);
            fail();
        } catch (final IllegalArgumentException e) {
            // Intentionally blank
        }
    }

    /**
     * Test {@link com.scvngr.levelup.core.model.tip.USCentTip#getValue} gets the value used to construct the tip.
     */
    @SmallTest
    public void testGetValue() {
        assertEquals(0, new USCentTip(0).getValue());

        final int value = 50;
        assertEquals(value, new USCentTip(value).getValue());
    }

    /**
     * Test {@link android.os.Parcelable} implementation.
     */
    @SmallTest
    public void testParcel() {
        ParcelTestUtils.assertParcelableRoundtrips(new USCentTip(0));
        ParcelTestUtils.assertParcelableRoundtrips(new USCentTip(50));
    }

    /**
     * Test {@link com.scvngr.levelup.core.model.tip.USCentTip#withValue} gets a new {@link com.scvngr.levelup.core.model.tip.USCentTip} with the specified value.
     */
    @SmallTest
    public void testWithValue() {
        final USCentTip zero = new USCentTip(0);
        final USCentTip one = zero.withValue(1);

        assertEquals(0, zero.getValue());
        assertEquals(1, one.getValue());
        MoreAsserts.assertNotEqual(zero, one);
    }
}
