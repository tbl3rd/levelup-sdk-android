/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.ErrorJsonFactory;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Fixture for {@link Error}s.
 */
@ThreadSafe
public final class ErrorFixture {

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#CODE}.
     */
    @NonNull
    public static final String CODE_VALUE = "homework_not_submitted"; //$NON-NLS-1$

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#MESSAGE}.
     */
    @NonNull
    public static final String MESSAGE_VALUE = "The dog ate my homework."; //$NON-NLS-1$

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#OBJECT}.
     */
    @NonNull
    public static final String OBJECT_VALUE = "access_token"; //$NON-NLS-1$

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#PROPERTY}.
     */
    @NonNull
    public static final String PROPERTY_VALUE = "base"; //$NON-NLS-1$

    /**
     * @return valid, fully populated {@link Error}.
     */
    @NonNull
    public static Error getFullModel() {
        try {
            return new ErrorJsonFactory().from(getFullJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * @return valid, fully populated {@link Error}.
     */
    @NonNull
    public static List<Error> getFullModelList() {
        final ErrorJsonFactory factory = new ErrorJsonFactory();
        try {
            return factory.fromList(getListOfFullJsonObjects());
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * @return get a {@link JSONArray} of {@link Error} {@link JSONObject}s.
     */
    @NonNull
    public static JSONArray getListOfFullJsonObjects() {
        final JSONArray array = new JSONArray();
        array.put(getFullJsonObject());
        array.put(getFullJsonObject());
        array.put(getFullJsonObject());
        return array;
    }

    /**
     * Create a nested representation of the required JSON.
     *
     * @return valid JSON representation of the error.
     */
    @NonNull
    public static JSONObject getNestedJsonObject() {
        // Nest under the model root key
        try {
            return new JSONObject().put(ErrorJsonFactory.JsonKeys.MODEL_ROOT,
                    getMinimalJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Create an unnested representation of the required JSON with optional fields set.
     *
     * @return valid JSON representation of the error
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        try {
            return getMinimalJsonObject().put(ErrorJsonFactory.JsonKeys.CODE, CODE_VALUE)
                    .put(ErrorJsonFactory.JsonKeys.OBJECT, OBJECT_VALUE)
                    .put(ErrorJsonFactory.JsonKeys.PROPERTY, PROPERTY_VALUE);
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Create an unnested representation of the required JSON.
     *
     * @return valid JSON representation of the error
     */
    @NonNull
    private static JSONObject getMinimalJsonObject() {
        try {
            return new JSONObject().put(ErrorJsonFactory.JsonKeys.MESSAGE, MESSAGE_VALUE);
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ErrorFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
