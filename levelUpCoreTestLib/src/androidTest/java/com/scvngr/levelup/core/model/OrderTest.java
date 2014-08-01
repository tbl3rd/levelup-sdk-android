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

import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.OrderJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.User}.
 */
public final class OrderTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel_full() throws JSONException {
        final JSONObject object = OrderFixture.getFullJsonObject();

        final Order order = new OrderJsonFactory().from(object);
        ParcelTestUtils.assertParcelableRoundtrips(order);
    }

    @SmallTest
    public void testParcel_small() throws JSONException {
        final JSONObject object = OrderFixture.getMinimalJsonObject();

        final Order order = new OrderJsonFactory().from(object);
        ParcelTestUtils.assertParcelableRoundtrips(order);
    }

    /**
     * Tests {@link #equals(Object)} and {@link #hashCode()} methods.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test identical objects
        final Order order1 = new OrderJsonFactory().from(OrderFixture.getFullJsonObject());
        final Order order2 = new OrderJsonFactory().from(OrderFixture.getFullJsonObject());
        MoreAsserts.checkEqualsAndHashCodeMethods(order1, order2, true);

        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(OrderJsonFactory.JsonKeys.class,
                new OrderJsonFactory(), OrderFixture.getFullJsonObject(), new String[] {
                        "MODEL_ROOT"});
    }

    @SmallTest
    public void testGetUuid() {
        final Order order =
                Order.builder().uuid(OrderFixture.UUID_FIXTURE_1)
                        .createdAt(OrderFixture.DATE_TIME_FIXTURE_1).build();
        assertEquals(OrderFixture.UUID_FIXTURE_1, order.getUuid());
    }

    @SmallTest
    public void testToString() throws JSONException {
        final String string = OrderFixture.getMinimalJsonObject().toString();
        assertTrue(string.length() > 0);
    }
}
