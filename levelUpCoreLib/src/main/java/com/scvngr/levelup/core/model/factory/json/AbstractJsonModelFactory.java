/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

/**
 * Superclass for JSON parsers that create model objects using a factory design pattern. Any model
 * object that wishes to be parsed from JSON should have an implementation of this class.
 *
 * @param <T> The type of model this factory produces.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public abstract class AbstractJsonModelFactory<T> {

    /**
     * The type key under which the JSON object can be nested under.
     */
    @NonNull
    private final String mTypeKey;

    /**
     * Constructs a new factory.
     *
     * @param typeKey the key which the object to parse can be nested under. It will usually be the
     *        name of the object's type:
     *
     *        <pre>
     *            { 'typeKey' : { 'field1': 'test' } }.
     * </pre>
     *
     *        When requesting a single object or a list of objects, the object will be nested under
     *        this type key. When you are parsing an object as a field of another object, it will
     *        not be nested under a type key.
     */
    public AbstractJsonModelFactory(@NonNull final String typeKey) {
        PreconditionUtil.assertNotNull(typeKey, "typeKey");
        mTypeKey = typeKey;
    }

    /**
     * @return the type key which this object can be nested under.
     */
    @NonNull
    protected final String getTypeKey() {
        return mTypeKey;
    }

    /**
     * Parse an instance of the model from a {@link JSONObject}.
     *
     * @param json the {@link JSONObject} to parse from.
     * @return an instance of {@code T} parsed from {@code json}.
     * @throws JSONException If the model fails to parse.
     */
    @NonNull
    protected abstract T createFrom(@NonNull final JSONObject json) throws JSONException;

    /**
     * Parse a list of models from the JSON array passed.
     *
     * @param jsonArray the jsonArray to parse from.
     * @return a {@link List} of model instances.
     * @throws JSONException if any of the models fail to parse.
     */
    @NonNull
    public final List<T> fromList(@NonNull final JSONArray jsonArray) throws JSONException {
        PreconditionUtil.assertNotNull(jsonArray, "jsonArray");

        final int count = jsonArray.length();
        final List<T> objectList = new ArrayList<T>(count);

        for (int i = 0; i < count; i++) {
            final JSONObject jsonObject = NullUtils.nonNullContract(jsonArray.getJSONObject(i));
            objectList.add(from(jsonObject));
        }

        return objectList;
    }

    /**
     * Parse a model from the JSON object passed, un-nesting from a root element if necessary.
     *
     * @param jsonObject the JSON representation of the model to parse.
     * @return a model instance.
     * @throws JSONException if the model fails to parse.
     */
    @NonNull
    public final T from(@NonNull final JSONObject jsonObject) throws JSONException {
        PreconditionUtil.assertNotNull(jsonObject, "jsonObject");

        JSONObject objectToParse = jsonObject;

        if (jsonObject.has(mTypeKey) && 1 == jsonObject.length()) {
            /*
             * Root element (probably) found, although we need to check to be sure it's not just a
             * sub-element with the same name
             */
            try {
                objectToParse = NullUtils.nonNullContract(jsonObject.getJSONObject(mTypeKey));
            } catch (final JSONException exception) {
                /*
                 * A JSONException can be thrown if the key we are searching for happens to not be
                 * the key which everything is nested under, but instead the name of a field that
                 * will not return a JSONObject. IE: errors can be returned nested like:
                 *
                 * { 'error' : {'error' : 'the error'} }
                 *
                 * or:
                 *
                 * { 'error' : 'the error' }
                 *
                 * The latter example would thrown an exception when trying to return a JSONObject.
                 */
                objectToParse = jsonObject; // Just use the object passed; don't un-nest.
            }
        }

        return createFrom(objectToParse);
    }
}
