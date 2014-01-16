/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.LoyaltyJsonFactory;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link Loyalty} objects.
 */
@ThreadSafe
public final class LoyaltyFixture {

    /**
     * Gets a valid full JSON object.
     *
     * @param merchantWebServiceId the ID of the merchant.
     * @return a {@link JSONObject} with all the fields.
     */
    @NonNull
    public static Loyalty getFullModel(final long merchantWebServiceId) {
        try {
            return new LoyaltyJsonFactory().from(getFullJsonObject(merchantWebServiceId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a valid full JSON object.
     *
     * @return a {@link JSONObject} with all the fields.
     */
    @NonNull
    public static Loyalty getMinimalModel() {
        try {
            return new LoyaltyJsonFactory().from(getMinimalJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the Loyalty required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        // No required fields.
        return new JSONObject();
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param merchantWebServiceId the ID of the merchant.
     * @return a {@link JSONObject} with all the Loyalty fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long merchantWebServiceId) {
        try {
            final JSONObject object = getMinimalJsonObject();
            object.put(LoyaltyJsonFactory.JsonKeys.MERCHANT_LOYALTY_ENABLED, true);
            object.put(LoyaltyJsonFactory.JsonKeys.MERCHANT_WEB_SERVICE_ID, merchantWebServiceId);
            object.put(LoyaltyJsonFactory.JsonKeys.ORDERS_COUNT, 2);
            object.put(LoyaltyJsonFactory.JsonKeys.POTENTIAL_CREDIT, 1000);
            object.put(LoyaltyJsonFactory.JsonKeys.PROGRESS_PERCENT, 50d);
            object.put(LoyaltyJsonFactory.JsonKeys.SHOULD_SPEND, 1000);
            object.put(LoyaltyJsonFactory.JsonKeys.SAVINGS, 1000);
            object.put(LoyaltyJsonFactory.JsonKeys.SPEND_REMAINING, 1000);
            object.put(LoyaltyJsonFactory.JsonKeys.TOTAL_VOLUME, 1000);
            object.put(LoyaltyJsonFactory.JsonKeys.WILL_EARN, 1000);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }
}
