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

import com.scvngr.levelup.core.model.Location;
import com.scvngr.levelup.core.model.LocationFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.LocationJsonFactory}.
 */
public final class LocationJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests JSON parsing.
     *
     * @throws org.json.JSONException on parse errors.
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        {
            final Location location =
                    new LocationJsonFactory().from(LocationFixture.getMinimalJsonObject());
            assertEquals(1, location.getId());
        }

        {
            final Location location =
                    new LocationJsonFactory().from(LocationFixture.getFullJsonObject());
            assertEquals(new HashSet<Integer>(Arrays.asList(2, 3, 5, 23)), location.getCategories());
            assertEquals("extended_address", location.getExtendedAddress());
            assertEquals("facebook_url", location.getUrl(Location.URL_FACEBOOK));
            assertEquals("foodler_url", location.getUrl(Location.URL_FOODLER));
            assertEquals("hours", location.getHours());
            assertEquals(1, location.getId());
            assertEquals(LocationFixture.LOCATION_LATITUDE, location.getLatitude());
            assertEquals(LocationFixture.LOCATION_LONGITUDE, location.getLongitude());
            assertEquals("locality", location.getLocality());
            assertEquals(LocationFixture.MERCHANT_ID, location.getMerchantId());
            assertEquals("merchant_name", location.getMerchantName());
            assertEquals("menu_url", location.getUrl(Location.URL_MENU));
            assertEquals("name", location.getName());
            assertEquals("newsletter_url", location.getUrl(Location.URL_NEWSLETTER));
            assertEquals("opentable_url", location.getUrl(Location.URL_OPENTABLE));
            assertEquals("phone", location.getPhone());
            assertEquals("postal_code", location.getPostalCode());
            assertEquals("region", location.getRegion());
            assertEquals("street_address", location.getStreetAddress());
            assertEquals("twitter_url", location.getUrl(Location.URL_TWITTER));
            assertEquals("yelp_url", location.getUrl(Location.URL_YELP));
        }
    }

    /**
     * Tests JSON parsing with missing required keys fails.
     * @throws org.json.JSONException  on parse errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKeys() throws JSONException {
        {
            final JSONObject object = LocationFixture.getMinimalJsonObject();
            object.remove(LocationJsonFactory.JsonKeys.ID);

            try {
                new LocationJsonFactory().from(object);
                fail("should throw JSONException");
            } catch (final JSONException e) {
                // Expected Exception
            }
        }
    }
}
