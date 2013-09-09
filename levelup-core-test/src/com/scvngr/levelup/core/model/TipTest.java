package com.scvngr.levelup.core.model;

import android.test.AndroidTestCase;
import android.test.MoreAsserts;

/**
 * Tests {@link Tip}.
 */
public final class TipTest extends AndroidTestCase {

    /**
     * Tests that {@link Tip#getTipPercentages(android.content.Context)} returns a non-null,
     * non-empty result.
     */
    public void testGetTipPercentages_non_empty() {
        final int[] tipPercentages = Tip.getTipPercentages(getContext());

        assertNotNull(tipPercentages);
        MoreAsserts.assertNotEqual(0, tipPercentages.length);
    }

    /**
     * Tests that {@link Tip#getTipPercentages(android.content.Context)} returns a new object each
     * time.
     */
    public void testGetTipPercentages_different_objects() {
        assertNotSame(Tip.getTipPercentages(getContext()), Tip.getTipPercentages(getContext()));
    }

    /**
     * Tests that {@link Tip#getTipPercentages(android.content.Context)} returns a sorted list of
     * tips.
     */
    public void testGetTipPercentages_sorted() {
        final int[] tipPercentages = Tip.getTipPercentages(getContext());

        int lastTipPercentage = Integer.MIN_VALUE;
        for (final int tip : tipPercentages) {
            assertTrue(lastTipPercentage < tip);

            lastTipPercentage = tip;
        }
    }
}
