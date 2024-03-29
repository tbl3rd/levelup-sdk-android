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
import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.model.util.JsonUtils;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link CreditCard}s from JSON.
 *
 * This is internal until it supports v14.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class CreditCardJsonFactory extends AbstractJsonModelFactory<CreditCard> {

    /**
     * Constructor.
     */
    public CreditCardJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected CreditCard createFrom(@NonNull final JSONObject json) throws JSONException {
        final Long bin;
        {
            if (json.isNull(JsonKeys.BIN)) {
                bin = null;
            } else {
                bin = json.optLong(JsonKeys.BIN);
            }
        }

        final boolean debit = json.optBoolean(JsonKeys.DEBIT, false);
        final String description = JsonUtils.optString(json, JsonKeys.DESCRIPTION);
        final String expirationMonth = JsonUtils.optString(json, JsonKeys.EXPIRATION_MONTH);
        final String expirationYear = JsonUtils.optString(json, JsonKeys.EXPIRATION_YEAR);
        final long id = json.getLong(JsonKeys.ID);
        final boolean promoted = json.optBoolean(JsonKeys.PROMOTED, false);
        final String last4 = JsonUtils.optString(json, JsonKeys.LAST_4);
        final String type = JsonUtils.optString(json, JsonKeys.TYPE);

        return new CreditCard(bin, debit, description, expirationMonth, expirationYear, id, last4,
                promoted, type);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "credit_card";

        @JsonValueType(JsonType.LONG)
        public static final String BIN = "bin";

        @JsonValueType(JsonType.BOOLEAN)
        public static final String DEBIT = "debit";

        @JsonValueType(JsonType.STRING)
        public static final String DESCRIPTION = "description";

        @JsonValueType(JsonType.STRING)
        public static final String EXPIRATION_MONTH = "expiration_month";

        @JsonValueType(JsonType.STRING)
        public static final String EXPIRATION_YEAR = "expiration_year";

        @JsonValueType(JsonType.LONG)
        public static final String ID = "id";

        @JsonValueType(JsonType.STRING)
        public static final String LAST_4 = "last_4";

        @JsonValueType(JsonType.BOOLEAN)
        public static final String PROMOTED = "promoted";

        @JsonValueType(JsonType.STRING)
        public static final String TYPE = "type";

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
