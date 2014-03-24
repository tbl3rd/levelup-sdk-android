/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.LocationJsonFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Fixture for {@link Location} models.
 */
public final class LocationFixture {

    /**
     * Fixture latitude value.
     */
    public static final double LOCATION_LATITUDE = 42.355;

    /**
     * Fixture longitude value.
     */
    public static final double LOCATION_LONGITUDE = -71.065;

    /**
     * Fixture merchant ID.
     */
    public static final long MERCHANT_ID = 25;

    /**
     * Fixture merchant name.
     */
    public static final String MERCHANT_NAME = "merchant_name"; //$NON-NLS-1$

    /**
     * Gets a fully populated model.
     *
     * @param webseviceId the ID of the {@link Location} on the web service.
     * @return {@link Location} model
     */
    @NonNull
    public static Location getFullModel(final int webseviceId) {
        try {
            return new LocationJsonFactory().from(getFullJsonObject(webseviceId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a minimally populated model.
     *
     * @param webseviceId the ID of the {@link Location} on the web service.
     * @return {@link Location} model
     */
    @NonNull
    public static Location getMinimalModel(final int webseviceId) {
        try {
            return new LocationJsonFactory().from(getMinimalJsonObject(webseviceId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param webServiceId the ID of the {@link Location} on the web service.
     * @return a {@link JSONObject} with all the Location required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final int webServiceId) {
        try {
            final JSONObject object = new JSONObject();
            object.put(LocationJsonFactory.JsonKeys.ID, webServiceId);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the Location required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(1);
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param webServiceId the ID of the {@link Location} on the web service.
     * @return a {@link JSONObject} with all the Location fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final int webServiceId) {
        try {
            final JSONObject object = getMinimalJsonObject(webServiceId);
            object.put(LocationJsonFactory.JsonKeys.CATEGORIES,
                    new JSONArray(Arrays.asList(2, 3, 5, 23)));
            object.put(LocationJsonFactory.JsonKeys.EXTENDED_ADDRESS, "extended_address"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.FACEBOOK_URL, "facebook_url"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.FOODLER_URL, "foodler_url"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.HOURS, "hours"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.LATITUDE, LOCATION_LATITUDE);
            object.put(LocationJsonFactory.JsonKeys.LONGITUDE, LOCATION_LONGITUDE);
            object.put(LocationJsonFactory.JsonKeys.LOCALITY, "locality"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.MERCHANT_ID, MERCHANT_ID);
            object.put(LocationJsonFactory.JsonKeys.MERCHANT_NAME, MERCHANT_NAME);
            object.put(LocationJsonFactory.JsonKeys.MENU_URL, "menu_url"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.NAME, "name"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.NEWSLETTER_URL, "newsletter_url"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.OPENTABLE_URL, "opentable_url"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.PHONE, "phone"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.POSTAL_CODE, "postal_code"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.REGION, "region"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.SHOWN, true);
            object.put(LocationJsonFactory.JsonKeys.STREET_ADDRESS, "street_address"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.TWITTER_URL, "twitter_url"); //$NON-NLS-1$
            object.put(LocationJsonFactory.JsonKeys.YELP_URL, "yelp_url"); //$NON-NLS-1$
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the Location fields.
     * @throws JSONException if there is a parse error.
     */
    @NonNull
    public static JSONObject getFullJsonObject() throws JSONException {
        return getFullJsonObject(1);
    }

    private LocationFixture() {
        throw new UnsupportedOperationException("this class is non-instantiable"); //$NON-NLS-1$
    }
}
