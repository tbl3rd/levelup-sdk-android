/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.MonetaryValue}.
 */
public final class MonetaryValueTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel() throws JSONException {
        final MonetaryValue monetaryValue = MonetaryValueFixture.getFullModel();
        ParcelTestUtils.assertParcelableRoundtrips(monetaryValue);
    }

    @SmallTest
    public void testMinimalConstructor() {
        final MonetaryValue value = new MonetaryValue(100);
        assertEquals(MonetaryValue.USD_CODE, value.getCurrencyCode());
        assertEquals(MonetaryValue.USD_SYMBOL, value.getCurrencySymbol());
        assertEquals(100, value.getAmount());
        assertNotNull(value.getFormattedAmountWithCurrencySymbol(getContext()));
        assertNotNull(value.getFormattedCentStrippedAmountWithCurrencySymbol(getContext()));
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        final MonetaryValue value1 = new MonetaryValue(1);
        final MonetaryValue value2 = new MonetaryValue(2);

        assertNotSame(value1, null);
        assertNotSame(value1, value2);
        assertEquals(value1, value1);
        assertEquals(value2, value2);
    }

    @SmallTest
    public void testGetFormattedMoney() {
        assertEquals("$89.99", MonetaryValue.getFormattedMoney(getContext(), "$", 8999)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("€10.00", MonetaryValue.getFormattedMoney(getContext(), "€", 1000)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @SmallTest
    public void getFormattedMoneyNoDecimal() {
        assertEquals("$89", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "$", 8999)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("€10", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "€", 1001)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("€1", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "€", 999)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("€1", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "€", 999)); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
