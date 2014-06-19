/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.CauseAffiliation;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link com.scvngr.levelup.core.model.CreditCard}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class CauseAffiliationJsonFactory extends AbstractJsonModelFactory<CauseAffiliation> {

    /**
     * Constructor.
     */
    public CauseAffiliationJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected CauseAffiliation createFrom(@NonNull final JSONObject json) throws JSONException {
        final long id = json.optLong(JsonKeys.ID, -1L);
        final double percentDonation = json.optDouble(JsonKeys.PERCENT_DONATION, 0d);
        return new CauseAffiliation(id, percentDonation);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        @NonNull
        public static final String MODEL_ROOT = "cause_affiliation"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        @NonNull
        public static final String ID = "cause_id"; //$NON-NLS-1$

        @JsonValueType(JsonType.DOUBLE)
        @NonNull
        public static final String PERCENT_DONATION = "percent_donation"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }
}
