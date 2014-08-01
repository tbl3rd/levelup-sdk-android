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
import com.scvngr.levelup.core.model.Claim;
import com.scvngr.levelup.core.model.MonetaryValue;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for parsing {@link Claim}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class ClaimJsonFactory extends AbstractJsonModelFactory<Claim> {

    /**
     * Constructor...
     */
    public ClaimJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected Claim createFrom(@NonNull final JSONObject json) throws JSONException {
        final long campaignId = json.getLong(JsonKeys.CAMPAIGN_ID);
        final String code = json.getString(JsonKeys.CODE);
        final long id = json.getLong(JsonKeys.ID);
        final MonetaryValue value = new MonetaryValue(json.getLong(JsonKeys.VALUE));
        final MonetaryValue valueRemaining =
                new MonetaryValue(json.getLong(JsonKeys.VALUE_REMAINING));

        return new Claim(campaignId, code, id, value, valueRemaining);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @NonNull
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "claim";

        @JsonValueType(JsonType.LONG)
        public static final String CAMPAIGN_ID = "campaign_id";

        @JsonValueType(JsonType.STRING)
        public static final String CODE = "code";

        @JsonValueType(JsonType.LONG)
        public static final String ID = "id";

        @JsonValueType(JsonType.LONG)
        public static final String VALUE = "value_amount";

        @JsonValueType(JsonType.LONG)
        public static final String VALUE_REMAINING = "value_remaining_amount";

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
