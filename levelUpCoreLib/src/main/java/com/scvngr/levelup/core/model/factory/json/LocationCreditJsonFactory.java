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
import com.scvngr.levelup.core.model.LocationCredit;
import com.scvngr.levelup.core.model.MonetaryValue;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for parsing {@link LocationCredit}s from JSON.
 */
public final class LocationCreditJsonFactory extends AbstractJsonModelFactory<LocationCredit> {
    /**
     * Constructor...
     */
    public LocationCreditJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected LocationCredit createFrom(@NonNull final JSONObject json) throws JSONException {
        final MonetaryValue merchantAmount =
                new MonetaryValue(json.optLong(JsonKeys.MERCHANT_AMOUNT));
        final MonetaryValue totalAmount = new MonetaryValue(json.getLong(JsonKeys.TOTAL_AMOUNT));

        return new LocationCredit(merchantAmount, totalAmount);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        @NonNull
        public static final String MODEL_ROOT = "credit";

        /**
         * The credit available from the merchant.
         */
        @JsonValueType(JsonType.LONG)
        @NonNull
        public static final String MERCHANT_AMOUNT = "merchant_funded_credit_amount";

        /**
         * The total credit availble.
         */
        @JsonValueType(JsonType.LONG)
        @NonNull
        public static final String TOTAL_AMOUNT = "total_amount";

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
