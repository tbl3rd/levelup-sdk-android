/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.model.factory.json.UserJsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Fixture for {@link User} for testing.
 */
public final class UserFixture {

    /**
     * Helper method to return a minimal user object. Defaults the ID to 1.
     *
     * @return a minimal {@link User} model.
     */
    @NonNull
    public static User getMinimalModel() {
        return getMinimalModel(1);
    }

    /**
     * Helper method to return a minimal user object.
     *
     * @param id User's ID on the web service.
     * @return a minimal {@link User} model.
     */
    @NonNull
    public static User getMinimalModel(final long id) {
        try {
            return new UserJsonFactory().from(getMinimalJsonObject(id));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Helper method to return a valid user object with all of its fields filled. Defaults the ID to
     * 1.
     *
     * @return a valid {@link User} with all fields filled.
     */
    @NonNull
    public static User getFullModel() {
        return getFullModel(1);
    }

    /**
     * Helper method to return a valid user object with all of its fields filled. Defaults the ID to
     * 1. Order and Merchant visit count are set to 30 so all colors are unlocked.
     *
     * @return a valid {@link User} with all fields filled.
     */
    @NonNull
    public static User getFullModelColorUnlocked() {
        try {
            return new UserJsonFactory().from(getFullJsonObjectColorUnlocked());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Helper method to return a valid user object with all of its fields filled.
     *
     * @param id User's ID on the web service.
     * @return a valid {@link User} with all fields filled.
     */
    @NonNull
    public static User getFullModel(final long id) {
        try {
            return new UserJsonFactory().from(getFullJsonObject(id));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Create an unnested representation of the required JSON. Defaults the ID to 1.
     *
     * @return valid JSON representation of the user.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(1);
    }

    /**
     * Gets a valid JSON object with the ID passed.
     *
     * @param id the ID to set as the object's ID
     * @return valid JSON representation of the user
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final long id) {
        try {
            return new JSONObject().put(UserJsonFactory.JsonKeys.ID, id);
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Create an unnested representation of the required and optional JSON. Defaults the ID to 1.
     *
     * @return valid JSON representation of the user.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1);
    }

    /**
     * Create an unnested representation of the required and optional JSON. Allows for the passing
     * of the user ID to use.
     *
     * @param id the ID of the user.
     * @return valid JSON representation of the user.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long id) {
        final HashMap<String, String> customAttributesMap = new HashMap<String, String>();
        customAttributesMap.put("test_attr", "0");
        customAttributesMap.put("test_attr2", "1");

        try {
            return getMinimalJsonObject(id)
                    .put(UserJsonFactory.JsonKeys.BORN_AT, "1911-12-26T00:00:00-0200")
                    .put(UserJsonFactory.JsonKeys.CONNECTED_TO_FACEBOOK, true)
                    .put(UserJsonFactory.JsonKeys.CUSTOM_ATTRIBUTES,
                            new JSONObject(customAttributesMap))
                    .put(UserJsonFactory.JsonKeys.DEBIT_CARD_ONLY, true)
                    .put(UserJsonFactory.JsonKeys.EMAIL, "test@example.com")
                    .put(UserJsonFactory.JsonKeys.FIRST_NAME, "John")
                    .put(UserJsonFactory.JsonKeys.GENDER, "male")
                    .put(UserJsonFactory.JsonKeys.GLOBAL_CREDIT_AMOUNT, 1000)
                    .put(UserJsonFactory.JsonKeys.LAST_NAME, "Doe")
                    .put(UserJsonFactory.JsonKeys.MERCHANTS_VISITED_COUNT, 1)
                    .put(UserJsonFactory.JsonKeys.ORDERS_COUNT, 2)
                    .put(UserJsonFactory.JsonKeys.TERMS_ACCEPTED_AT, "2012-12-04T18:10:45-0500")
                    .put(UserJsonFactory.JsonKeys.TOTAL_SAVINGS_AMOUNT, 1000);
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Create an unnested representation of the required and optional JSON, with order and merchant
     * visit count set to 30. Id defaulted to 1.
     *
     * @return valid JSON representation of the user.
     */
    private static JSONObject getFullJsonObjectColorUnlocked() {
        final HashMap<String, String> customAttributesMap = new HashMap<String, String>();
        customAttributesMap.put("test_attr", "0");
        customAttributesMap.put("test_attr2", "1");

        try {
            return getMinimalJsonObject(1)
                    .put(UserJsonFactory.JsonKeys.BORN_AT, "1911-12-26T00:00:00-0200")
                    .put(UserJsonFactory.JsonKeys.CONNECTED_TO_FACEBOOK, true)
                    .put(UserJsonFactory.JsonKeys.CUSTOM_ATTRIBUTES,
                            new JSONObject(customAttributesMap))
                    .put(UserJsonFactory.JsonKeys.DEBIT_CARD_ONLY, true)
                    .put(UserJsonFactory.JsonKeys.EMAIL, "test@example.com")
                    .put(UserJsonFactory.JsonKeys.FIRST_NAME, "John")
                    .put(UserJsonFactory.JsonKeys.GENDER, "male")
                    .put(UserJsonFactory.JsonKeys.GLOBAL_CREDIT_AMOUNT, 1000)
                    .put(UserJsonFactory.JsonKeys.LAST_NAME, "Doe")
                    .put(UserJsonFactory.JsonKeys.MERCHANTS_VISITED_COUNT, 30)
                    .put(UserJsonFactory.JsonKeys.ORDERS_COUNT, 30)
                    .put(UserJsonFactory.JsonKeys.TERMS_ACCEPTED_AT, "2012-12-04T18:10:45-0500")
                    .put(UserJsonFactory.JsonKeys.TOTAL_SAVINGS_AMOUNT, 1000);
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Create a nested representation of the required and optional JSON.
     *
     * @return valid JSON representation of the user.
     */
    @NonNull
    public static JSONObject getFullJsonObjectNested() {
        try {
            return new JSONObject().put(UserJsonFactory.JsonKeys.MODEL_ROOT, getFullJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Modifies the {@link JSONObject} so the given value will be returned by
     * {@link User#getBornAt()} once a {@link User} is created.
     *
     * @param userJsonObject The {@link JSONObject} representing the {@link User}.
     * @param bornAt The value that will be returned by {@link User#getBornAt()}.
     * @return The modified {@link JSONObject}.
     */
    @NonNull
    public static JSONObject setBornAt(@NonNull final JSONObject userJsonObject,
            @Nullable final String bornAt) {
        try {
            userJsonObject.put(UserJsonFactory.JsonKeys.BORN_AT, bornAt);
            return userJsonObject;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Modifies the {@link JSONObject} so the given value will be returned by
     * {@link User#getEmail()} once a {@link User} is created.
     *
     * @param userJsonObject The {@link JSONObject} representing the {@link User}.
     * @param email The value that will be returned by {@link User#getEmail()}.
     * @return The modified {@link JSONObject}.
     */
    @NonNull
    public static JSONObject setEmail(@NonNull final JSONObject userJsonObject,
            @Nullable final String email) {
        try {
            userJsonObject.put(UserJsonFactory.JsonKeys.EMAIL, email);
            return userJsonObject;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Modifies the {@link JSONObject} so the given value will be returned by
     * {@link User#getFirstName()} once a {@link User} is created.
     *
     * @param userJsonObject The {@link JSONObject} representing the {@link User}.
     * @param firstName The value that will be returned by {@link User#getFirstName()}.
     * @return The modified {@link JSONObject}.
     */
    @NonNull
    public static JSONObject setFirstName(@NonNull final JSONObject userJsonObject,
            @Nullable final String firstName) {
        try {
            userJsonObject.put(UserJsonFactory.JsonKeys.FIRST_NAME, firstName);
            return userJsonObject;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Modifies the {@link JSONObject} so the given value will be returned by
     * {@link User#getGender()} once a {@link User} is created.
     *
     * @param userJsonObject The {@link JSONObject} representing the {@link User}.
     * @param gender The value that will be returned by {@link User#getGender()}.
     * @return The modified {@link JSONObject}.
     */
    @NonNull
    public static JSONObject setGender(@NonNull final JSONObject userJsonObject,
            @Nullable final String gender) {
        try {
            userJsonObject.put(UserJsonFactory.JsonKeys.GENDER, gender);
            return userJsonObject;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Modifies the {@link JSONObject} so the given value will be returned by
     * {@link User#getLastName()} once a {@link User} is created.
     *
     * @param userJsonObject The {@link JSONObject} representing the {@link User}.
     * @param lastName The value that will be returned by {@link User#getLastName()}.
     * @return The modified {@link JSONObject}.
     */
    @NonNull
    public static JSONObject setLastName(@NonNull final JSONObject userJsonObject,
            @Nullable final String lastName) {
        try {
            userJsonObject.put(UserJsonFactory.JsonKeys.LAST_NAME, lastName);
            return userJsonObject;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private UserFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
