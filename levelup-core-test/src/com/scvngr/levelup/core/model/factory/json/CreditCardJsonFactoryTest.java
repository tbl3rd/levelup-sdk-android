package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.model.CreditCardFixture;

/**
 * Tests {@link CreditCardJsonFactory}.
 */
public final class CreditCardJsonFactoryTest extends AndroidTestCase {

    /**
     * Tests json parsing.
     *
     * @throws JSONException
     */
    @SmallTest
    public void testJsonParse_singleObject() throws JSONException {
        final CreditCardJsonFactory factory = new CreditCardJsonFactory();
        final JSONObject object = CreditCardFixture.getFullJsonObject(true);
        final CreditCard card = factory.from(object);
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.DESCRIPTION), card.getDescription());
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.EXPIRATION_MONTH), card
                .getExpirationMonth());
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.EXPIRATION_YEAR), card
                .getExpirationYear());
        assertEquals(object.getLong(CreditCardJsonFactory.JsonKeys.ID), card.getId());
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.LAST_4), card.getLast4());
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.PROMOTED), card.isPromoted());
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.TYPE), card.getType());
    }

    /**
     * Tests json parsing a list of credit cards.
     *
     * @throws JSONException
     */
    @SmallTest
    public void testJsonParse_objectList() throws JSONException {
        final CreditCardJsonFactory factory = new CreditCardJsonFactory();
        final JSONArray array = new JSONArray();
        array.put(CreditCardFixture.getFullJsonObject(true));
        array.put(CreditCardFixture.getFullJsonObject(false));
        array.put(CreditCardFixture.getFullJsonObject(true));
        final List<CreditCard> cards = factory.fromList(array);

        assertEquals(3, cards.size());
        assertTrue(cards.get(0).isPromoted());
        assertFalse(cards.get(1).isPromoted());
        assertTrue(cards.get(2).isPromoted());
    }
}
