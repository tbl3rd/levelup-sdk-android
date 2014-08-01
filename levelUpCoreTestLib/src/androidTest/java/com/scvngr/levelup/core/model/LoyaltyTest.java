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
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.LoyaltyJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a loyalty progression on the server.
 */
public final class LoyaltyTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel_full() {
        final Loyalty loyalty = LoyaltyFixture.getFullModel(1);
        final Parcel parcel = Parcel.obtain();

        try {
            loyalty.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final Loyalty parceled = Loyalty.CREATOR.createFromParcel(parcel);
            assertEquals(loyalty, parceled);
        } finally {
            parcel.recycle();
        }
    }

    @SmallTest
    public void testParcel_minimal() {
        final Loyalty loyalty = LoyaltyFixture.getMinimalModel();
        final Parcel parcel = Parcel.obtain();

        try {
            loyalty.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final Loyalty parceled = Loyalty.CREATOR.createFromParcel(parcel);
            assertEquals(loyalty, parceled);
        } finally {
            parcel.recycle();
        }
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil
                .checkEqualsAndHashCodeOnJsonVariants(
                        LoyaltyJsonFactory.JsonKeys.class,
                        new LoyaltyJsonFactory(),
                        LoyaltyFixture.getFullJsonObject(1),
                        new String[] {
                                "MODEL_ROOT", "ORDERS_COUNT", "POTENTIAL_CREDIT", "SAVINGS", "SHOULD_SPEND", "SPEND_REMAINING", "TOTAL_VOLUME", "WILL_EARN" });

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.ORDERS_COUNT);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.POTENTIAL_CREDIT);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.SAVINGS);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.SHOULD_SPEND);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.SPEND_REMAINING);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.TOTAL_VOLUME);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final JSONObject object = LoyaltyFixture.getFullJsonObject(1);
            final Loyalty loyalty1 = new LoyaltyJsonFactory().from(object);
            object.remove(LoyaltyJsonFactory.JsonKeys.WILL_EARN);
            final Loyalty loyalty2 = new LoyaltyJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, false);
        }

        {
            final Loyalty loyalty1 = LoyaltyFixture.getMinimalModel();
            final Loyalty loyalty2 = LoyaltyFixture.getMinimalModel();
            MoreAsserts.checkEqualsAndHashCodeMethods(loyalty1, loyalty2, true);
        }
    }
}
