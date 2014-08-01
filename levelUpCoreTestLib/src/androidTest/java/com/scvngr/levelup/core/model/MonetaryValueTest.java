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
        assertEquals("$89.99", MonetaryValue.getFormattedMoney(getContext(), "$", 8999));
        assertEquals("€10.00", MonetaryValue.getFormattedMoney(getContext(), "€", 1000));
    }

    @SmallTest
    public void getFormattedMoneyNoDecimal() {
        assertEquals("$89", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "$", 8999));
        assertEquals("€10", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "€", 1001));
        assertEquals("€1", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "€", 999));
        assertEquals("€1", MonetaryValue.getFormattedMoneyNoDecimal(getContext(), "€", 999));
    }
}
