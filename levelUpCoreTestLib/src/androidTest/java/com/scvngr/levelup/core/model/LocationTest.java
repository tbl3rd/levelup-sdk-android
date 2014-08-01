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
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.LocationJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.Location}.
 */
public final class LocationTest extends SupportAndroidTestCase {
    @SmallTest
    public void testParcel() throws JSONException {
        final JSONObject object = LocationFixture.getFullJsonObject();

        final Location location = new LocationJsonFactory().from(object);
        final Parcel parcel = Parcel.obtain();
        location.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final Location parceled = Location.CREATOR.createFromParcel(parcel);
        assertEquals(location, parceled);
        parcel.recycle();
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(LocationJsonFactory.JsonKeys.class,
                new LocationJsonFactory(), LocationFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" });
    }
}
