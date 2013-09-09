package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Order;
import com.scvngr.levelup.core.model.OrderFixture;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link OrderJsonFactory}.
 */
public final class OrderJsonFactoryTest extends AndroidTestCase {

    @SmallTest
    public void testJsonParse_minimalSingleObject() throws JSONException {
        final OrderJsonFactory factory = new OrderJsonFactory();
        final JSONObject object = OrderFixture.getMinimalJsonObject();
        final Order order = factory.from(object);
        assertEquals(OrderFixture.getMinimalModel(), order);
    }

    @SmallTest
    public void testJsonParse_fullSingleObject() throws JSONException {
        final OrderJsonFactory factory = new OrderJsonFactory();
        final JSONObject object = OrderFixture.getFullJsonObject();
        final Order order = factory.from(object);
        assertEquals(OrderFixture.getFullModel(), order);
    }
}
