/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.PaymentToken;

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
        public static final String MODEL_ROOT = "payment_token"; //$NON-NLS-1$

        /**
         * Key in the JSON object to get the {@link PaymentToken}'s data.
         */
        @JsonValueType(JsonType.STRING)
        public static final String DATA = "data"; //$NON-NLS-1$

        /**
         * Key in the JSON object to get the {@link PaymentToken}'s web service ID.
         */
        @JsonValueType(JsonType.LONG)
        public static final String ID = "id"; //$NON-NLS-1$

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
