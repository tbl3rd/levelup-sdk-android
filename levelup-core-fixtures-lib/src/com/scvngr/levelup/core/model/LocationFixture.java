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
import java.util.Map;

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

    /**
     * Gets a valid base JSON object with the field values provided in the arguments.
     *
     * @param webServiceId the ID of the {@link Location} on the wed service.
     * @param streetAddress the street number and name
     * @param extendedAddress an additional line of address
     * @param hours the hours open as a string
     * @param latitude the latitude of the location as a double
     * @param longitude the longitude of the location as a double
     * @param locality the town, city, etc.
     * @param merchantId the ID of the merchant.
     * @param merchantName the name of the merchant.
     * @param name the location's name.
     * @param region the state, province etc.
     * @param phone the phone number as a string
     * @param postalCode the postal code
     * @param shown whether this location should be shown to the user.
     * @param urls URLs associated with the location. Keys are {@link Location#URL_MENU} and
     *        friends.
     * @return a {@link JSONObject} with all the Location fields from the parameters.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final int webServiceId, final String streetAddress,
            final String extendedAddress, final String hours, final double latitude,
            final double longitude, final String locality, final long merchantId,
            final String merchantName, final String name, final String region, final String phone,
            final String postalCode, final boolean shown, final Map<String, String> urls) {
        try {
            final JSONObject object = getMinimalJsonObject(webServiceId);
            object.put(LocationJsonFactory.JsonKeys.EXTENDED_ADDRESS, extendedAddress);
            object.put(LocationJsonFactory.JsonKeys.HOURS, hours);
            object.put(LocationJsonFactory.JsonKeys.LATITUDE, latitude);
            object.put(LocationJsonFactory.JsonKeys.LONGITUDE, longitude);
            object.put(LocationJsonFactory.JsonKeys.LOCALITY, locality);
            object.put(LocationJsonFactory.JsonKeys.MERCHANT_ID, merchantId);
            object.put(LocationJsonFactory.JsonKeys.MERCHANT_NAME, merchantName);
            object.put(LocationJsonFactory.JsonKeys.NAME, name);
            object.put(LocationJsonFactory.JsonKeys.PHONE, phone);
            object.put(LocationJsonFactory.JsonKeys.POSTAL_CODE, postalCode);
            object.put(LocationJsonFactory.JsonKeys.REGION, region);
            object.put(LocationJsonFactory.JsonKeys.SHOWN, shown);
            object.put(LocationJsonFactory.JsonKeys.STREET_ADDRESS, streetAddress);
            if (null != urls) {
                object.put(LocationJsonFactory.JsonKeys.FACEBOOK_URL,
                        urls.get(Location.URL_FACEBOOK));
                object.put(LocationJsonFactory.JsonKeys.MENU_URL, urls.get(Location.URL_MENU));
                object.put(LocationJsonFactory.JsonKeys.NEWSLETTER_URL,
                        urls.get(Location.URL_NEWSLETTER));
                object.put(LocationJsonFactory.JsonKeys.OPENTABLE_URL,
                        urls.get(Location.URL_OPENTABLE));
                object.put(LocationJsonFactory.JsonKeys.TWITTER_URL, urls.get(Location.URL_TWITTER));
                object.put(LocationJsonFactory.JsonKeys.YELP_URL, urls.get(Location.URL_YELP));
            }
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid base JSON object with the field values provided in the arguments.
     *
     * @param streetAddress the street number and name
     * @param extendedAddress an additional line of address
     * @param hours the hours open as a string
     * @param latitude the latitude of the location as a double
     * @param longitude the longitude of the location as a double
     * @param locality the town, city, etc.
     * @param merchantId the ID of the merchant.
     * @param merchantName the name of the merchant.
     * @param name the location's name.
     * @param region the state, province etc.
     * @param phone the phone number as a string
     * @param postalCode the postal code
     * @param shown whether this location should be shown to the user.
     * @param urls URLs associated with the location. Keys are {@link Location#URL_MENU} and
     *        friends.
     * @return a {@link JSONObject} with all the Location fields from the parameters.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final String streetAddress,
            final String extendedAddress, final String hours, final double latitude,
            final double longitude, final String locality, final long merchantId,
            final String merchantName, final String name, final String region, final String phone,
            final String postalCode, final boolean shown, final Map<String, String> urls) {
        return getFullJsonObject(1, streetAddress, extendedAddress, hours, latitude, longitude,
                locality, merchantId, merchantName, name, region, phone, postalCode, shown, urls);
    }

    private LocationFixture() {
        throw new UnsupportedOperationException("this class is non-instantiable"); //$NON-NLS-1$
    }
}
