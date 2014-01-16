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
import com.scvngr.levelup.core.model.Error;
import com.scvngr.levelup.core.model.util.JsonUtils;

/**
 * Class that creates {@link Error}s from JSON.
 */
@LevelUpApi(contract = Contract.DRAFT)
public final class ErrorJsonFactory extends AbstractJsonModelFactory<Error> {

    /**
     * Constructor.
     */
    public ErrorJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @Override
    @NonNull
    protected Error createFrom(@NonNull final JSONObject json) throws JSONException {
        final String message = json.getString(JsonKeys.MESSAGE);
        final String object = JsonUtils.optString(json, JsonKeys.OBJECT);
        final String property = JsonUtils.optString(json, JsonKeys.PROPERTY);

        return new Error(message, object, property);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @Immutable
    public static final class JsonKeys {
        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "error"; //$NON-NLS-1$

        /**
         * The key in JSON where the error message can be parsed.
         */
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE = "message"; //$NON-NLS-1$

        /**
         * The key in JSON where the error object can be parsed.
         */
        @JsonValueType(JsonType.STRING)
        public static final String OBJECT = "object"; //$NON-NLS-1$

        /**
         * The key in JSON where the error object property can be parsed.
         */
        @JsonValueType(JsonType.STRING)
        public static final String PROPERTY = "property"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }
}
