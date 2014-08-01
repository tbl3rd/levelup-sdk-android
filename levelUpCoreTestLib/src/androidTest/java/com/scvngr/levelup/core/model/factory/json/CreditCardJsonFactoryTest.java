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
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.model.CreditCardFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.CreditCardJsonFactory}.
 */
public final class CreditCardJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests json parsing.
     *
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testJsonParse_singleObject() throws JSONException {
        final CreditCardJsonFactory factory = new CreditCardJsonFactory();
        final JSONObject object = CreditCardFixture.getFullJsonObject(true);
        final CreditCard card = factory.from(object);
        assertEquals(object.get(CreditCardJsonFactory.JsonKeys.DEBIT), card.isDebit());
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
     * Tests that cards with null debit state on the server are treated as non-debit cards by the
     * app. These are usually Amexes we want to treat as credit, not debit, cards.
     */
    @SmallTest
    public void testJsonParse_debitNull() throws JSONException {
        final CreditCardJsonFactory factory = new CreditCardJsonFactory();
        final JSONObject object = CreditCardFixture.getFullJsonObject(true);
        object.put(CreditCardJsonFactory.JsonKeys.DEBIT, null);
        final CreditCard card = factory.from(object);
        assertEquals(false, card.isDebit());
    }

    /**
     * Tests json parsing a list of credit cards.
     *
     * @throws org.json.JSONException
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
