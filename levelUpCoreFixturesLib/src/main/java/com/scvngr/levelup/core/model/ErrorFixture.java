/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

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
    public static final String CODE_VALUE = "homework_not_submitted";

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#MESSAGE}.
     */
    @NonNull
    public static final String MESSAGE_VALUE = "The dog ate my homework.";

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#OBJECT}.
     */
    @NonNull
    public static final String OBJECT_VALUE = "access_token";

    /**
     * Test value for {@link ErrorJsonFactory.JsonKeys#PROPERTY}.
     */
    @NonNull
    public static final String PROPERTY_VALUE = "base";

    /**
     * @return valid, fully populated {@link Error}.
     */
    @NonNull
    public static Error getFullModel() {
        try {
            return new ErrorJsonFactory().from(getFullJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
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
            throw new AssertionError(e);
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
            throw new AssertionError(e);
        }
    }

    /**
     * Create an unnested representation of the required JSON with optional fields set.
     *
     * @return valid JSON representation of the error
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getJsonObjectFromModel(new Error(CODE_VALUE, MESSAGE_VALUE, OBJECT_VALUE,
                PROPERTY_VALUE));
    }

    /**
     * Create an unnested JSON representation of the model without {@code null} fields.
     *
     * @param error the {@link Error} to convert to JSON
     * @return valid JSON representation of the error
     */
    @NonNull
    public static JSONObject getJsonObjectFromModel(@NonNull final Error error) {
        try {
            final JSONObject jsonObject = new JSONObject();
            final String code = error.getCode();

            if (null != code) {
                jsonObject.put(ErrorJsonFactory.JsonKeys.CODE, code);
            }

            jsonObject.put(ErrorJsonFactory.JsonKeys.MESSAGE, error.getMessage());

            final String object = error.getObject();

            if (null != object) {
                jsonObject.put(ErrorJsonFactory.JsonKeys.OBJECT, object);
            }

            final String property = error.getProperty();

            if (null != property) {
                jsonObject.put(ErrorJsonFactory.JsonKeys.PROPERTY, property);
            }

            return jsonObject;
        } catch (final JSONException e) {
            throw new AssertionError(e);
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
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ErrorFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
