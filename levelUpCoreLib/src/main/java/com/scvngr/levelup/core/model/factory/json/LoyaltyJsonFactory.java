/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Loyalty;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.util.JsonUtils;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link Loyalty}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class LoyaltyJsonFactory extends AbstractJsonModelFactory<Loyalty> {

    /**
     * Constructor.
     */
    public LoyaltyJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected Loyalty createFrom(@NonNull final JSONObject json) throws JSONException {
        final boolean loyaltyEnabled = json.optBoolean(JsonKeys.MERCHANT_LOYALTY_ENABLED, false);
        final Long merchantId;

        if (json.isNull(JsonKeys.MERCHANT_WEB_SERVICE_ID)) {
            merchantId = null;
        } else {
            merchantId = json.getLong(JsonKeys.MERCHANT_WEB_SERVICE_ID);
        }

        final int ordersCount = json.optInt(JsonKeys.ORDERS_COUNT);
        final MonetaryValue potentialCredit =
                JsonUtils.optMonetaryValue(json, JsonKeys.POTENTIAL_CREDIT);
        final int progressPercent = json.optInt(JsonKeys.PROGRESS_PERCENT);
        final MonetaryValue savings = JsonUtils.optMonetaryValue(json, JsonKeys.SAVINGS);
        final MonetaryValue shouldSpend = JsonUtils.optMonetaryValue(json, JsonKeys.SHOULD_SPEND);
        final MonetaryValue spendRemaining =
                JsonUtils.optMonetaryValue(json, JsonKeys.SPEND_REMAINING);
        final MonetaryValue totalVolume = JsonUtils.optMonetaryValue(json, JsonKeys.TOTAL_VOLUME);
        final MonetaryValue willEarn = JsonUtils.optMonetaryValue(json, JsonKeys.WILL_EARN);

        return new Loyalty(loyaltyEnabled, merchantId, ordersCount, potentialCredit,
                progressPercent, savings, shouldSpend, spendRemaining, totalVolume, willEarn);
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
        public static final String MODEL_ROOT = "loyalty";

        @JsonValueType(JsonType.BOOLEAN)
        public static final String MERCHANT_LOYALTY_ENABLED = "merchant_loyalty_enabled";

        @JsonValueType(JsonType.LONG)
        public static final String MERCHANT_WEB_SERVICE_ID = "merchant_id";

        @JsonValueType(JsonType.INT)
        public static final String ORDERS_COUNT = "orders_count";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String POTENTIAL_CREDIT = "potential_credit_amount";

        @JsonValueType(JsonType.INT)
        public static final String PROGRESS_PERCENT = "progress_percentage";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String SAVINGS = "savings_amount";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String SHOULD_SPEND = "merchant_spend_amount";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String SPEND_REMAINING = "spend_remaining_amount";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String TOTAL_VOLUME = "total_volume_amount";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String WILL_EARN = "merchant_earn_amount";

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
