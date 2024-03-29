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

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.Location;
import com.scvngr.levelup.core.model.util.JsonUtils;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Factory for creating {@link Location}s from JSON (v14).
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class LocationJsonFactory extends AbstractJsonModelFactory<Location> {

    /**
     * Constructs a new factory.
     */
    public LocationJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @Override
    @NonNull
    protected Location createFrom(@NonNull final JSONObject json) throws JSONException {
        final Set<Integer> categories = JsonUtils.optIntegerSet(json, JsonKeys.CATEGORIES);
        final String extendedAddress = JsonUtils.optString(json, JsonKeys.EXTENDED_ADDRESS);
        final String hours = JsonUtils.optString(json, JsonKeys.HOURS);
        final long id = json.getLong(JsonKeys.ID);
        final double lat = json.optDouble(JsonKeys.LATITUDE);
        final double lng = json.optDouble(JsonKeys.LONGITUDE);
        final String locality = JsonUtils.optString(json, JsonKeys.LOCALITY);

        final long merchantId = json.optLong(JsonKeys.MERCHANT_ID, 0);
        final String merchantName = JsonUtils.optString(json, JsonKeys.MERCHANT_NAME);
        final String name = JsonUtils.optString(json, JsonKeys.NAME);
        final String phone = JsonUtils.optString(json, JsonKeys.PHONE);
        final String postalCode = JsonUtils.optString(json, JsonKeys.POSTAL_CODE);
        final String region = JsonUtils.optString(json, JsonKeys.REGION);
        final boolean shown = json.optBoolean(JsonKeys.SHOWN, true);
        final String streetAddress = JsonUtils.optString(json, JsonKeys.STREET_ADDRESS);

        final HashMap<String, String> urls = new HashMap<String, String>();

        putValueIfKeyExists(urls, json, Location.URL_FACEBOOK, JsonKeys.FACEBOOK_URL);
        putValueIfKeyExists(urls, json, Location.URL_FOODLER, JsonKeys.FOODLER_URL);
        putValueIfKeyExists(urls, json, Location.URL_MENU, JsonKeys.MENU_URL);
        putValueIfKeyExists(urls, json, Location.URL_NEWSLETTER, JsonKeys.NEWSLETTER_URL);
        putValueIfKeyExists(urls, json, Location.URL_OPENTABLE, JsonKeys.OPENTABLE_URL);
        putValueIfKeyExists(urls, json, Location.URL_TWITTER, JsonKeys.TWITTER_URL);
        putValueIfKeyExists(urls, json, Location.URL_YELP, JsonKeys.YELP_URL);

        return new Location(categories, extendedAddress, hours, id, lat, lng, locality, merchantId,
                merchantName, name, phone, postalCode, region, shown, streetAddress, urls);
    }

    private void putValueIfKeyExists(@NonNull final HashMap<String, String> urls,
            @NonNull final JSONObject json, @NonNull final String hashMapKey,
            @NonNull final String jsonKey) {
        if(json.has(jsonKey)) {
            urls.put(hashMapKey, JsonUtils.optString(json, jsonKey));
        }
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {

        @JsonValueType(JsonType.JSON_ARRAY)
        public static final String CATEGORIES = "categories";

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "location";

        @JsonValueType(JsonType.STRING)
        public static final String EXTENDED_ADDRESS = "extended_address";

        @JsonValueType(JsonType.STRING)
        public static final String FACEBOOK_URL = "facebook_url";

        @JsonValueType(JsonType.STRING)
        public static final String FOODLER_URL = "foodler_url";

        @JsonValueType(JsonType.STRING)
        public static final String HOURS = "hours";

        @JsonValueType(JsonType.LONG)
        public static final String ID = "id";

        @JsonValueType(JsonType.DOUBLE)
        public static final String LATITUDE = "latitude";

        @JsonValueType(JsonType.DOUBLE)
        public static final String LONGITUDE = "longitude";

        @JsonValueType(JsonType.STRING)
        public static final String LOCALITY = "locality";

        @JsonValueType(JsonType.STRING)
        public static final String MENU_URL = "menu_url";

        @JsonValueType(JsonType.STRING)
        public static final String NAME = "name";

        @JsonValueType(JsonType.STRING)
        public static final String NEWSLETTER_URL = "newsletter_url";

        @JsonValueType(JsonType.STRING)
        public static final String OPENTABLE_URL = "opentable_url";

        @JsonValueType(JsonType.LONG)
        public static final String MERCHANT_ID = "merchant_id";

        @JsonValueType(JsonType.STRING)
        public static final String MERCHANT_NAME = "merchant_name";

        @JsonValueType(JsonType.STRING)
        public static final String PHONE = "phone";

        @JsonValueType(JsonType.STRING)
        public static final String POSTAL_CODE = "postal_code";

        @JsonValueType(JsonType.STRING)
        public static final String REGION = "region";

        @JsonValueType(JsonType.BOOLEAN)
        public static final String SHOWN = "shown";

        @JsonValueType(JsonType.STRING)
        public static final String STREET_ADDRESS = "street_address";

        @JsonValueType(JsonType.STRING)
        public static final String TWITTER_URL = "twitter_url";

        @JsonValueType(JsonType.STRING)
        public static final String YELP_URL = "yelp_url";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }
}
