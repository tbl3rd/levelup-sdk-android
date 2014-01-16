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
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.Order;
import com.scvngr.levelup.core.model.Order.OrderBuilder;

/**
 * Factory for creating {@link Order}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class OrderJsonFactory extends AbstractJsonModelFactory<Order> {

    @NonNull
    private final LocationJsonFactory mLocationFactory = new LocationJsonFactory();

    /**
     * Constructs a factory for parsing Order models.
     */
    public OrderJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected Order createFrom(@NonNull final JSONObject json) throws JSONException {
        final JsonModelHelper mh = new JsonModelHelper(json);
        final OrderBuilder builder = Order.builder();

        builder.balanceAmount(mh.optMonetaryValue(JsonKeys.BALANCE));
        builder.bundleClosedAt(mh.optString(JsonKeys.BUNDLE_CLOSED_AT));
        builder.bundleDescriptor(mh.optString(JsonKeys.BUNDLE_DESCRIPTOR));
        builder.contributionAmount(mh.optMonetaryValue(JsonKeys.CONTRIBUTION));
        builder.contributionTargetName(mh.optString(JsonKeys.CONTRIBUTION_TARGET_NAME));
        builder.createdAt(mh.getString(JsonKeys.CREATED_AT));
        builder.creditAppliedAmount(mh.optMonetaryValue(JsonKeys.CREDIT_APPLIED));
        builder.creditEarnedAmount(mh.optMonetaryValue(JsonKeys.CREDIT_EARNED));
        builder.locationExtendedAddress(mh.optString(JsonKeys.LOCATION_EXTENDED_ADDRESS));
        builder.locationLocality(mh.optString(JsonKeys.LOCATION_LOCALITY));
        builder.locationName(mh.optString(JsonKeys.LOCATION_NAME));
        builder.locationPostalCode(mh.optString(JsonKeys.LOCATION_POSTAL_CODE));
        builder.locationRegion(mh.optString(JsonKeys.LOCATION_REGION));
        builder.locationStreetAddress(mh.optString(JsonKeys.LOCATION_STREET_ADDRESS));
        builder.locationWebServiceId(mh.optLongNullable(JsonKeys.LOCATION_WEB_SERVICE_ID));
        builder.merchantName(mh.optString(JsonKeys.MERCHANT_NAME));
        builder.merchantWebServiceId(mh.optLongNullable(JsonKeys.MERCHANT_WEB_SERVICE_ID));
        builder.refundedAt(mh.optString(JsonKeys.REFUNDED_AT));
        builder.spendAmount(mh.optMonetaryValue(JsonKeys.SPEND));
        builder.tipAmount(mh.optMonetaryValue(JsonKeys.TIP));
        builder.totalAmount(mh.optMonetaryValue(JsonKeys.TOTAL));
        builder.transactedAt(mh.optString(JsonKeys.TRANSACTED_AT));
        builder.uuid(mh.getString(JsonKeys.UUID));

        return builder.build();
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
        public static final String MODEL_ROOT = "order"; //$NON-NLS-1$

        /**
         * Required field parsed as a {@link MonetaryValue} model.
         *
         * @see Order#getBalanceAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String BALANCE = "balance_amount"; //$NON-NLS-1$

        /**
         * Optional string field parsed as a date.
         *
         * @see Order#getBundleClosedAt()
         */
        @JsonValueType(JsonType.STRING)
        public static final String BUNDLE_CLOSED_AT = "bundle_closed_at"; //$NON-NLS-1$

        /**
         * Required String field value describing the bundle.
         *
         * @see Order#getBundleDescriptor()
         */
        @JsonValueType(JsonType.STRING)
        public static final String BUNDLE_DESCRIPTOR = "bundle_descriptor"; //$NON-NLS-1$

        /**
         * Optional field parsed as a {@link MonetaryValue} model.
         *
         * @see Order#getContributionAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String CONTRIBUTION = "contribution_amount"; //$NON-NLS-1$

        /**
         * Optional field parsed as a String, containing the name of the contribution target.
         *
         * @see Order#getContributionTargetName()
         */
        @JsonValueType(JsonType.STRING)
        public static final String CONTRIBUTION_TARGET_NAME = "contribution_target_name"; //$NON-NLS-1$

        /**
         * Required string field parsed as a date.
         *
         * @see Order#getCreatedAt()
         */
        @JsonValueType(JsonType.STRING)
        public static final String CREATED_AT = "created_at"; //$NON-NLS-1$

        /**
         * Required string field parsed as a {@link MonetaryValue} model.
         *
         * @see Order#getCreditAppliedAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String CREDIT_APPLIED = "credit_applied_amount"; //$NON-NLS-1$

        /**
         * Required string field parsed as a {@link MonetaryValue} model.
         *
         * @see Order#getCreditEarnedAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String CREDIT_EARNED = "credit_earned_amount"; //$NON-NLS-1$

        /**
         * Optional String field containing the extended location address.
         *
         * @see Order#getLocationExtendedAddress()
         */
        @JsonValueType(JsonType.STRING)
        public static final String LOCATION_EXTENDED_ADDRESS = "location_extended_address"; //$NON-NLS-1$

        /**
         * Required String field containing the location locality.
         *
         * @see Order#getLocationLocality()
         */
        @JsonValueType(JsonType.STRING)
        public static final String LOCATION_LOCALITY = "location_locality"; //$NON-NLS-1$

        /**
         * Optional String field containing the location name if different from the merchant name.
         *
         * @see Order#getLocationName()
         */
        @JsonValueType(JsonType.STRING)
        public static final String LOCATION_NAME = "location_name"; //$NON-NLS-1$

        /**
         * Required integer field containing the location web service ID.
         *
         * @see Order#getLocationWebServiceId()
         */
        @JsonValueType(JsonType.LONG)
        public static final String LOCATION_WEB_SERVICE_ID = "location_id"; //$NON-NLS-1$

        /**
         * Required String field containing the location postal code.
         *
         * @see Order#getLocationPostalCode()
         */
        @JsonValueType(JsonType.STRING)
        public static final String LOCATION_POSTAL_CODE = "location_postal_code"; //$NON-NLS-1$

        /**
         * Required String field containing the location region.
         *
         * @see Order#getLocationRegion()
         */
        @JsonValueType(JsonType.STRING)
        public static final String LOCATION_REGION = "location_region"; //$NON-NLS-1$

        /**
         * Required String field containing the location street address.
         *
         * @see Order#getLocationStreetAddress()
         */
        @JsonValueType(JsonType.STRING)
        public static final String LOCATION_STREET_ADDRESS = "location_street_address"; //$NON-NLS-1$

        /**
         * Required String field containing the merchant name.
         *
         * @see Order#getMerchantName()
         */
        @JsonValueType(JsonType.STRING)
        public static final String MERCHANT_NAME = "merchant_name"; //$NON-NLS-1$

        /**
         * Required integer field containing the merchant web service ID.
         *
         * @see Order#getMerchantWebServiceId()
         */
        @JsonValueType(JsonType.LONG)
        public static final String MERCHANT_WEB_SERVICE_ID = "merchant_web_service_id"; //$NON-NLS-1$

        /**
         * Optional String field parsed as a date.
         */
        @JsonValueType(JsonType.STRING)
        public static final String REFUNDED_AT = "refunded_at"; //$NON-NLS-1$

        /**
         * Required String field containing the spend amount. Parsed as a {@link MonetaryValue}.
         *
         * @see Order#getSpendAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String SPEND = "spend_amount"; //$NON-NLS-1$

        /**
         * Required String field containing the tip amount. Parsed as a {@link MonetaryValue}.
         *
         * @see Order#getTipAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String TIP = "tip_amount"; //$NON-NLS-1$

        /**
         * Required String field containing the total. Parsed as a {@link MonetaryValue}.
         *
         * @see Order#getTotalAmount()
         */
        @JsonValueType(JsonType.INT)
        public static final String TOTAL = "total_amount"; //$NON-NLS-1$

        /**
         * Required String field containing the date this was transacted at.
         *
         * @see Order#getTransactedAt()
         */
        @JsonValueType(JsonType.STRING)
        public static final String TRANSACTED_AT = "transacted_at"; //$NON-NLS-1$

        /**
         * The globally-unique ID for this order.
         */
        @JsonValueType(JsonType.STRING)
        public static final String UUID = "uuid"; //$NON-NLS-1$

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
