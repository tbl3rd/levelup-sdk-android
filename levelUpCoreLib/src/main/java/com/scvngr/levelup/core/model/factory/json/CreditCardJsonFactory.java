/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

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
import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.model.util.JsonUtils;

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
        public static final String MODEL_ROOT = "credit_card"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        public static final String BIN = "bin"; //$NON-NLS-1$

        @JsonValueType(JsonType.BOOLEAN)
        public static final String DEBIT = "debit"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String DESCRIPTION = "description"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String EXPIRATION_MONTH = "expiration_month"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String EXPIRATION_YEAR = "expiration_year"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        public static final String ID = "id"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String LAST_4 = "last_4"; //$NON-NLS-1$

        @JsonValueType(JsonType.BOOLEAN)
        public static final String PROMOTED = "promoted"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String TYPE = "type"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }
}
