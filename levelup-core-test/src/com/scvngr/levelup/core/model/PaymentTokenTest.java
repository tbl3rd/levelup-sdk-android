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
 * Tests {@link PaymentToken}.
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
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test similarities
        final PaymentToken PaymentToken1 =
                new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        final PaymentToken PaymentToken2 =
                new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        MoreAsserts.checkEqualsAndHashCodeMethods(PaymentToken1, PaymentToken2, true);

        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(PaymentTokenJsonFactory.JsonKeys.class,
                new PaymentTokenJsonFactory(), PaymentTokenFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }

    /**
     * Tests that {@link User#toString()} gives a value.
     *
     * @throws JSONException if there was a problem running toString.
     */
    @SmallTest
    public void testToString() throws JSONException {
        final PaymentToken PaymentToken =
                new PaymentTokenJsonFactory().from((PaymentTokenFixture.getFullJsonObject()));
        final String string = PaymentToken.toString();
        assertTrue(string.length() > 0);
    }
}
