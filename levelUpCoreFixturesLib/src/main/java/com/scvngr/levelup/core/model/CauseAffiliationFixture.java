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
import android.support.annotation.Nullable;

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
