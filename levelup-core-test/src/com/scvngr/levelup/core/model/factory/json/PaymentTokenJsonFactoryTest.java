package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.PaymentToken;
import com.scvngr.levelup.core.model.PaymentTokenFixture;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link PaymentTokenJsonFactory}.
 */
public final class PaymentTokenJsonFactoryTest extends AndroidTestCase {

    /**
     * Tests basic json parsing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        // Make sure required fields are parsed if set
        PaymentToken token = new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        assertEquals("0123456789", token.getData()); //$NON-NLS-1$
        assertEquals(1, token.getId());

        // Test nested parsing
        token = new PaymentTokenJsonFactory().from(PaymentTokenFixture.getFullJsonObject());
        assertEquals("0123456789", token.getData()); //$NON-NLS-1$
        assertEquals(1, token.getId());
    }

    /**
     * Tests basic json parsing with required key missing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKey() throws JSONException {
        try {
            final JSONObject object = PaymentTokenFixture.getFullJsonObject();
            object.remove(PaymentTokenJsonFactory.JsonKeys.DATA);
            new PaymentTokenJsonFactory().from(object);
            fail("no data field should throw error"); //$NON-NLS-1$
        } catch (final JSONException e) {
            // Expected exception
        }

        try {
            final JSONObject object = PaymentTokenFixture.getFullJsonObject();
            object.remove(PaymentTokenJsonFactory.JsonKeys.ID);
            new PaymentTokenJsonFactory().from(object);
            fail("no id field should throw error"); //$NON-NLS-1$
        } catch (final JSONException e) {
            // Expected exception
        }
    }
}
