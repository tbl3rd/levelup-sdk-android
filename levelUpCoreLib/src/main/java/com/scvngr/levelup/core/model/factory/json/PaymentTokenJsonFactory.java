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
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.PaymentToken;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link PaymentToken}s from JSON.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.DRAFT)
public final class PaymentTokenJsonFactory extends AbstractJsonModelFactory<PaymentToken> {

    /**
     * Constructs a new factory.
     */
    public PaymentTokenJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @Override
    @NonNull
    protected PaymentToken createFrom(@NonNull final JSONObject json) throws JSONException {
        final String data = json.getString(JsonKeys.DATA);
        final long id = json.getLong(JsonKeys.ID);
        return new PaymentToken(data, id);
    }

    /**
     * Keys for JSON properties for this object for use in deserialization.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final class JsonKeys {
        /**
         * JSON object key that maps to the root object if it's nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "payment_token";

        /**
         * Key in the JSON object to get the {@link PaymentToken}'s data.
         */
        @JsonValueType(JsonType.STRING)
        public static final String DATA = "data";

        /**
         * Key in the JSON object to get the {@link PaymentToken}'s web service ID.
         */
        @JsonValueType(JsonType.LONG)
        public static final String ID = "id";

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
