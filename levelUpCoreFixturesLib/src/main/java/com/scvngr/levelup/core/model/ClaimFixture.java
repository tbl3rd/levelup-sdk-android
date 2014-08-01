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
import android.text.format.DateFormat;

import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.factory.json.ClaimJsonFactory;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

/**
 * Fixture for {@link Claim}.
 */
@Immutable
public final class ClaimFixture {

    /**
     * The Date that will be set for the expires at field.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final String EXPIRES_AT_DATE = (String) DateFormat.format(
            "yyyy-MM-dd HH:mm", Calendar.getInstance(Locale.US));

    /**
     * @param webServiceId the ID of the claim.
     * @return a fully populated {@link Claim}.
     */
    @NonNull
    public static Claim getFullModel(final long webServiceId) {
        try {
            return new ClaimJsonFactory().from(getFullJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * @param webServiceId the ID of the claim.
     * @return a valid minimal {@link Claim}.
     */
    @NonNull
    public static Claim getMinimalModel(final long webServiceId) {
        try {
            return new ClaimJsonFactory().from(getMinimalJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the Claim required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(1);
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param webServiceId the ID of the {@link Claim} on the web service.
     * @return a {@link JSONObject} with all the Claim required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final long webServiceId) {
        final long amount = MonetaryValueFixture.getFullModel().getAmount();
        final JSONObject object = new JSONObject();

        try {
            object.put(ClaimJsonFactory.JsonKeys.CAMPAIGN_ID, webServiceId);
            object.put(ClaimJsonFactory.JsonKeys.CODE, "code");
            object.put(ClaimJsonFactory.JsonKeys.ID, webServiceId);
            object.put(ClaimJsonFactory.JsonKeys.VALUE, amount);
            object.put(ClaimJsonFactory.JsonKeys.VALUE_REMAINING, amount);
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }

        return object;
    }

    /**
     * Gets a valid full JSON object.
     *
     * @return a {@link JSONObject} with all the Claim fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param webServiceId the ID of the {@link Claim} on the web service.
     * @return a {@link JSONObject} with all the Claim fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long webServiceId) {
        return getMinimalJsonObject(webServiceId);
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ClaimFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
