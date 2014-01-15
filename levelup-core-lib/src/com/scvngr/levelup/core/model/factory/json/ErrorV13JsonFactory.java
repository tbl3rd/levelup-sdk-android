/**
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
import com.scvngr.levelup.core.model.ErrorV13;

/**
 * Factory for parsing {@link ErrorV13}s from JSON.
 */
@Immutable
@Deprecated
@LevelUpApi(contract = Contract.INTERNAL)
public final class ErrorV13JsonFactory extends AbstractJsonModelFactory<ErrorV13> {

    /**
     * Constructor.
     */
    public ErrorV13JsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected ErrorV13 createFrom(@NonNull final JSONObject json) throws JSONException {
        final String error = json.getString(JsonKeys.ERROR);
        final String errorDescription = json.optString(JsonKeys.ERROR_DESCRIPTION, null);
        final String sponsorEmail = json.optString(JsonKeys.SPONSOR_EMAIL, null);

        return new ErrorV13(error, errorDescription, sponsorEmail);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {
        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "error"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String ERROR = "error"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String ERROR_DESCRIPTION = "error_description"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String SPONSOR_EMAIL = "sponsor_email"; //$NON-NLS-1$
    }
}
