/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.LocationCredit;
import com.scvngr.levelup.core.model.MonetaryValue;

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
        public static final String MODEL_ROOT = "credit"; //$NON-NLS-1$

        /**
         * The credit available from the merchant.
         */
        @JsonValueType(JsonType.LONG)
        @NonNull
        public static final String MERCHANT_AMOUNT = "merchant_funded_credit_amount"; //$NON-NLS-1$

        /**
         * The total credit availble.
         */
        @JsonValueType(JsonType.LONG)
        @NonNull
        public static final String TOTAL_AMOUNT = "total_amount"; //$NON-NLS-1$

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
