/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.model.factory.json.PaymentTokenJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.PaymentToken}.
 */
public final class PaymentTokenTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel() throws JSONException {
        final JSONObject object = PaymentTokenFixture.getFullJsonObject();

        final PaymentToken paymentToken = new PaymentTokenJsonFactory().from(object);
        final Parcel parcel = Parcel.obtain();
        paymentToken.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final PaymentToken parceled = PaymentToken.CREATOR.createFromParcel(parcel);
        assertEquals(paymentToken, parceled);
    }

    /**
     * Tests {@link #equals(Object)} and {@link #hashCode()} methods.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test similarities
        final PaymentToken paymentToken1 =
                new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        final PaymentToken paymentToken2 =
                new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        MoreAsserts.checkEqualsAndHashCodeMethods(paymentToken1, paymentToken2, true);

        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(PaymentTokenJsonFactory.JsonKeys.class,
                new PaymentTokenJsonFactory(), PaymentTokenFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" });
    }

    /**
     * Tests that {@link com.scvngr.levelup.core.model.User#toString()} gives a value.
     *
     * @throws org.json.JSONException if there was a problem running toString.
     */
    @SmallTest
    public void testToString() throws JSONException {
        final PaymentToken paymentToken =
                new PaymentTokenJsonFactory().from((PaymentTokenFixture.getFullJsonObject()));
        final String string = paymentToken.toString();
        assertTrue(string.length() > 0);
    }
}
