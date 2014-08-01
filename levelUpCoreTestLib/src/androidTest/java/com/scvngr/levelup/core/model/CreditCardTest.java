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

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.CreditCardJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.CreditCard}.
 */
public final class CreditCardTest extends SupportAndroidTestCase {

    @SmallTest
    public void testConstructor_basic() throws JSONException {
        CreditCardFixture.getFullModel(0);
    }

    @SmallTest
    public void testParcel() {
        final CreditCard card = CreditCardFixture.getFullModel(0);

        final Parcel parcel = Parcel.obtain();
        card.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final CreditCard card2 = CreditCard.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals(card, card2);
    }

    @SmallTest
    public void testParcelWithMinimalModel() {
        final CreditCard card = CreditCardFixture.getMinimalModel(1);

        final Parcel parcel = Parcel.obtain();
        card.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final CreditCard card2 = CreditCard.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals(card, card2);
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(CreditCardJsonFactory.JsonKeys.class,
                new CreditCardJsonFactory(), CreditCardFixture.getFullJsonObject(true),
                new String[] { "MODEL_ROOT" });
    }
}
