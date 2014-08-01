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
import com.scvngr.levelup.core.model.AccessToken;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link AccessToken}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class AccessTokenJsonFactory extends AbstractJsonModelFactory<AccessToken> {

    /**
     * Constructor.
     */
    public AccessTokenJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected AccessToken createFrom(@NonNull final JSONObject json) throws JSONException {
        final JsonModelHelper helper = new JsonModelHelper(json);

        final String accessToken = helper.getString(JsonKeys.TOKEN);
        final Long userId = helper.optLongNullable(JsonKeys.USER_ID);

        return new AccessToken(accessToken, userId);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "access_token";

        /**
         * Key in JSON where the token value can be parsed.
         */
        @JsonValueType(JsonType.STRING)
        public static final String TOKEN = "token";

        /**
         * Key in JSON where the user ID for this token can be parsed.
         */
        @JsonValueType(JsonType.LONG)
        public static final String USER_ID = "user_id";

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
