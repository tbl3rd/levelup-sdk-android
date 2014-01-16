/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.test.MoreAsserts;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.AbstractJsonModelFactory;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Utility methods for testing.
 */
public final class JsonTestUtil {
    /**
     * <p>
     * Checks that modifying individual fields in a model will result in its equals/hashCode methods
     * failing. Uses reflection on {@link JsonValueType} annotations on fields of a passed class to
     * figure out how to modify the JSON representation of the model in different ways, then parses
     * the JSON with a {@link AbstractJsonModelFactory} subclass before checking equals/hashcode on
     * both the original and a modified object.
     * </p>
     * <p>
     * This effectively checks that equals/hashcode works across any value changes from fields we
     * read from JSON, but also checks some other potential issues. We're implicitly checking that
     * the JSON typing declared in annotations for the fields matches what we actually use when
     * parsing our JSON (since if it doesn't, we'll get JSON errors when reading the data during the
     * clone/modify). We're also checking for fields that may have been added to the JSON keys and
     * the model without updating equals/hashcode to reflect them (as long as they're declared in
     * the JSONKeys class used here).
     * </p>
     * <p>
     * Note that this is only intended for test use and will turn all checked exceptions it might
     * throw into unchecked ones.
     * </p>
     *
     * @param jsonKeysClass Class of the underlying keys class to test all fields (except
     *        blacklistFields) from. Must have visible fields to read from.
     * @param jsonFactory Factory object to construct model instances from out of the base and
     *        generated-variant JSON objects before checking equals/hashcode.
     * @param baseJsonObject Fully-populated JSON object for the model to use for comparison with
     *        modified copies.
     * @param blacklistFields Fields to exclude from variant testing (either because we need to test
     *        them manually or because they don't reflect fields that are used for parsing into the
     *        model). Note that this is the jsonKeysClass's field name as a string, not the JSON key
     *        value (eg "ID", not "id").
     */
    public static void checkEqualsAndHashCodeOnJsonVariants(@NonNull final Class<?> jsonKeysClass,
            @NonNull final AbstractJsonModelFactory<?> jsonFactory,
            @NonNull final JSONObject baseJsonObject, @NonNull final String[] blacklistFields) {
        Object originalModel;
        Object differentModel;
        Object differentModelReparse;

        try {
            originalModel = jsonFactory.from(baseJsonObject);
        } catch (final JSONException e1) {
            throw new RuntimeException(e1);
        }

        MoreAsserts.checkEqualsAndHashCodeMethods(originalModel, null, false);

        final Field[] jsonKeyFields = jsonKeysClass.getFields();
        final List<String> blacklisted = Arrays.asList(blacklistFields);
        final String key = null;

        MoreAsserts.assertNotEmpty("JSON keys class visible fields", //$NON-NLS-1$
                Arrays.asList(jsonKeyFields));

        for (final Field field : jsonKeyFields) {
            if (!blacklisted.contains(field.getName())) {
                JSONObject copiedDifferingObject;
                String fieldString;
                // Don't check exceptions, just let tests fail.
                try {
                    fieldString = NullUtils.nonNullContract((String) field.get(key));
                    copiedDifferingObject =
                            cloneObjectDifferingOnParam(baseJsonObject, fieldString,
                                    reflectJsonType(field));
                    differentModel = jsonFactory.from(copiedDifferingObject);
                    differentModelReparse = jsonFactory.from(copiedDifferingObject);
                } catch (final IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (final JSONException e) {
                    throw new RuntimeException(e);
                }

                MoreAsserts.checkEqualsAndHashCodeMethods(String.format(Locale.US,
                        "Modified %s and checked equals and hash", fieldString), //$NON-NLS-1$
                        originalModel, differentModel, false);
                MoreAsserts.checkEqualsAndHashCodeMethods(String.format(Locale.US,
                        "Modified %s and checked equals and hash", fieldString), //$NON-NLS-1$
                        differentModel, differentModel, true);
                MoreAsserts.checkEqualsAndHashCodeMethods(String.format(Locale.US,
                        "Modified %s and checked equals and hash", fieldString), //$NON-NLS-1$
                        differentModel, differentModelReparse, true);
            }
        }
    }

    /**
     * Reflects a {@link JsonType} from a field's {@link JsonValueType} annotation.
     *
     * @param field Field with a JsonValueType annotation to read from.
     * @return the {@link JsonType} of the field.
     */
    @NonNull
    private static JsonType reflectJsonType(@NonNull final Field field) {
        final JsonValueType annotation = field.getAnnotation(JsonValueType.class);
        return annotation.value();
    }

    /**
     * Makes a deep copy of a JSONObject, modifying one key based on its {@link JsonType} (e.g.
     * flipping the value for a boolean, adding 1 to numbers, appending data to a string).
     *
     * @param baseObject JSONObject to create a modified deep copy of.
     * @param key Key to modify the value of in the copy.
     * @param jsonType {@link JsonType} to use to infer how to modify the copy's value.
     * @return a deep copy of the JSON object, with one modified field.
     * @throws JSONException if there was a parsing error.
     */
    @NonNull
    private static JSONObject cloneObjectDifferingOnParam(@NonNull final JSONObject baseObject,
            @NonNull final String key, @NonNull final JsonType jsonType) throws JSONException {
        final JSONObject object = new JSONObject(baseObject.toString());
        LogManager.d("Testing field %s", key); //$NON-NLS-1$

        if (JsonType.BOOLEAN.equals(jsonType)) {
            object.put(key, !object.getBoolean(key));
        } else if (JsonType.DOUBLE.equals(jsonType)) {
            object.put(key, object.getDouble(key) + 1);
        } else if (JsonType.INT.equals(jsonType)) {
            object.put(key, object.getInt(key) + 1);
        } else if (JsonType.LONG.equals(jsonType)) {
            object.put(key, object.getLong(key) + 1);
        } else if (JsonType.STRING.equals(jsonType)) {
            String currentString = object.getString(key);

            if (null == currentString) {
                currentString = ""; //$NON-NLS-1$
            }

            object.put(key, currentString + "_testdifferent"); //$NON-NLS-1$

        } else if (JsonType.JSON_ARRAY.equals(jsonType)) {
            JSONArray currentArray = object.getJSONArray(key);

            if (null == currentArray) {
                currentArray = new JSONArray();
            }

            final JSONArray modifiedArray = new JSONArray(currentArray.toString());
            modifiedArray.put(1);
            object.put(key, modifiedArray);
        } else {
            throw new UnsupportedOperationException(String.format(Locale.US,
                    "Can only use for JsonTypes(int/long/bool/string/array) not JsonType(%s)", //$NON-NLS-1$
                    jsonType.name()));
        }

        return object;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private JsonTestUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
