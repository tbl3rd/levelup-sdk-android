package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.CreditCardJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;

import org.json.JSONException;

/**
 * Tests {@link CreditCard}.
 */
public final class CreditCardTest extends AndroidTestCase {

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
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }
}
