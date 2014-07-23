/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.factory.json.CauseAffiliationJsonFactory;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link CauseAffiliation}.
 */
@Immutable
public final class CauseAffiliationFixture {

    /**
     * Gets a valid full JSON object.
     *
     * @return a {@link JSONObject} with all the fields.
     */
    @NonNull
    public static CauseAffiliation getFullModel() {
        return getFullModel(1L);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param causeId the web service ID.
     *
     * @return a {@link JSONObject} with all the fields.
     */
    @NonNull
    public static CauseAffiliation getFullModel(@Nullable final Long causeId) {
        try {
            return new CauseAffiliationJsonFactory().from(getFullJsonObject(causeId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a valid full JSON object.
     *
     * @return a {@link JSONObject} with all the {@link CauseAffiliation} fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1L);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param causeId the web service ID.
     *
     * @return a {@link JSONObject} with all the {@link CauseAffiliation} fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(@Nullable final Long causeId) {
        try {
            final JSONObject object = new JSONObject();
            object.put(CauseAffiliationJsonFactory.JsonKeys.ID, causeId);
            object.put(CauseAffiliationJsonFactory.JsonKeys.PERCENT_DONATION, 0d);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private CauseAffiliationFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
