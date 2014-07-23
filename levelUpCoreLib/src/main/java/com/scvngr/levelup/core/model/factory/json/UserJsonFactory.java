/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.User.Gender;
import com.scvngr.levelup.core.model.util.JsonUtils;

/**
 * Factory for creating {@link User}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class UserJsonFactory extends AbstractJsonModelFactory<User> {

    /**
     * Constructs new factory.
     */
    public UserJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @Override
    @NonNull
    protected User createFrom(@NonNull final JSONObject json) throws JSONException {
        final String bornAt = JsonUtils.optString(json, JsonKeys.BORN_AT);
        final boolean isConnectedToFacebook =
                json.optBoolean(JsonKeys.CONNECTED_TO_FACEBOOK, false);
        final JSONObject customAttributesJson = json.optJSONObject(JsonKeys.CUSTOM_ATTRIBUTES);

        Map<String, String> customAttributes = null;
        if (null != customAttributesJson) {
            customAttributes = new HashMap<String, String>();
            final Iterator<?> keys = customAttributesJson.keys();
            while (keys.hasNext()) {
                final String key = (String) keys.next();
                customAttributes.put(key, customAttributesJson.get(key).toString());
            }
        }

        final boolean isDebitCardOnly = json.optBoolean(JsonKeys.DEBIT_CARD_ONLY, false);
        final String email = JsonUtils.optString(json, JsonKeys.EMAIL);
        final String firstName = JsonUtils.optString(json, JsonKeys.FIRST_NAME);
        final Gender gender = Gender.forString(JsonUtils.optString(json, JsonKeys.GENDER));
        final MonetaryValue globalCredit =
                JsonUtils.optMonetaryValue(json, JsonKeys.GLOBAL_CREDIT_AMOUNT);
        final long id = json.getLong(JsonKeys.ID);
        final String lastName = JsonUtils.optString(json, JsonKeys.LAST_NAME);
        final int merchantsVisitedCount = json.optInt(JsonKeys.MERCHANTS_VISITED_COUNT, 0);
        final int ordersCount = json.optInt(JsonKeys.ORDERS_COUNT, 0);

        final String termsAcceptedAt = JsonUtils.optString(json, JsonKeys.TERMS_ACCEPTED_AT);
        final MonetaryValue totalSavings =
                JsonUtils.optMonetaryValue(json, JsonKeys.TOTAL_SAVINGS_AMOUNT);

        return new User(bornAt, isConnectedToFacebook, customAttributes, isDebitCardOnly, email,
                firstName, gender, globalCredit, id, lastName, merchantsVisitedCount, ordersCount,
                termsAcceptedAt, totalSavings);
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
        public static final String MODEL_ROOT = "user";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.STRING)
        public static final String BORN_AT = "born_at";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.BOOLEAN)
        public static final String CONNECTED_TO_FACEBOOK = "connected_to_facebook";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String CUSTOM_ATTRIBUTES = "custom_attributes";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.BOOLEAN)
        public static final String DEBIT_CARD_ONLY = "debit_card_only";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.STRING)
        public static final String EMAIL = "email";

        /**
         * Maps to a {@link Gender} in the model; expects "male" or "female" as a string in JSON.
         */
        @JsonValueType(JsonType.STRING)
        public static final String GENDER = "gender";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.LONG)
        public static final String GLOBAL_CREDIT_AMOUNT = "global_credit_amount";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.LONG)
        public static final String ID = "id";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.STRING)
        public static final String FIRST_NAME = "first_name";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.STRING)
        public static final String LAST_NAME = "last_name";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.INT)
        public static final String MERCHANTS_VISITED_COUNT = "merchants_visited_count";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.INT)
        public static final String ORDERS_COUNT = "orders_count";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.STRING)
        public static final String TERMS_ACCEPTED_AT = "terms_accepted_at";

        @SuppressWarnings("javadoc")
        @JsonValueType(JsonType.LONG)
        public static final String TOTAL_SAVINGS_AMOUNT = "total_savings_amount";

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
