/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
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
                                "MODEL_ROOT", "ORDERS_COUNT", "POTENTIAL_CREDIT", "SAVINGS", "SHOULD_SPEND", "SPEND_REMAINING", "TOTAL_VOLUME", "WILL_EARN" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

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
