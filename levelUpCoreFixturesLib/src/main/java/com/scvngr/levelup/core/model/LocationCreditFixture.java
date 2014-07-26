/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.model.factory.json.LocationCreditJsonFactory;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link LocationCredit}.
 */
@Immutable
public final class LocationCreditFixture {
    /**
     * @param webServiceId the ID of the location credit.
     * @return a fully populated {@link LocationCredit}.
     */
    @NonNull
    public static LocationCredit getFullModel(final long webServiceId) {
        try {
            return new LocationCreditJsonFactory().from(getFullJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * @param webServiceId the ID of the location credit.
     * @return a valid minimal {@link LocationCredit}.
     */
    @NonNull
    public static LocationCredit getMinimalModel(final long webServiceId) {
        try {
            return new LocationCreditJsonFactory().from(getMinimalJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the LocationCredit required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(1);
    }

    /**
     * Gets a valid base JSON object. MonetaryValues will be the base 10 dollars + the webServiceId
     * in cents.
     *
     * @param webServiceId the ID of the {@link LocationCredit} on the web service.
     * @return a {@link JSONObject} with all the LocationCredit required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final long webServiceId) {
        final long amount = MonetaryValueFixture.getFullModel().getAmount() + webServiceId;
        final JSONObject object = new JSONObject();

        try {
            object.put(LocationCreditJsonFactory.JsonKeys.MERCHANT_AMOUNT, amount);
            object.put(LocationCreditJsonFactory.JsonKeys.TOTAL_AMOUNT, amount);
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }

        return object;
    }

    /**
     * Gets a valid full JSON object.
     *
     * @return a {@link JSONObject} with all the LocationCredit fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param webServiceId the ID of the {@link LocationCredit} on the web service.
     * @return a {@link JSONObject} with all the LocationCredit fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long webServiceId) {
        return getMinimalJsonObject(webServiceId);
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private LocationCreditFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
