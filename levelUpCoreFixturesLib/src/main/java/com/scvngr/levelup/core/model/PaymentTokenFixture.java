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

import com.scvngr.levelup.core.model.factory.json.PaymentTokenJsonFactory;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link PaymentToken}s.
 */
@ThreadSafe
public final class PaymentTokenFixture {

    /**
     * Return a fully populated {@link PaymentToken} model.
     *
     * @param webServiceId the ID of the {@link PaymentToken} on the web service.
     * @return valid JSON representation of the model under test
     */
    @NonNull
    public static PaymentToken getFullModel(final long webServiceId) {
        try {
            return new PaymentTokenJsonFactory().from(getFullJsonObjectNested(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Return a fully populated {@link PaymentToken} model.
     *
     * @param webServiceId the ID of the {@link PaymentToken} on the web service.
     * @param data the data for the payment token.
     * @return valid JSON representation of the model under test
     */
    @NonNull
    public static PaymentToken getFullModel(final long webServiceId,
            @Nullable final String data) {
        try {
            return new PaymentTokenJsonFactory().from(getFullJsonObject(webServiceId, data));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Create a nested representation of the required JSON. Visible so tests that nest these can
     * reuse.
     *
     * @return valid JSON representation of the model under test
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1);
    }

    /**
     * Create a nested representation of the required JSON. Visible so tests that nest these can
     * reuse.
     *
     * @param webServiceId the ID of the {@link PaymentToken} on the web service.
     * @return valid JSON representation of the model under test
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long webServiceId) {
        return getFullJsonObject(webServiceId, null);
    }

    /**
     * Create a nested representation of the required JSON. Visible so tests that nest these can
     * reuse.
     *
     * @param webServiceId the ID of the {@link PaymentToken} on the web service.
     * @param data the data for the payment token.
     * @return valid JSON representation of the model under test
     */
    @NonNull
    public static JSONObject
            getFullJsonObject(final long webServiceId, @Nullable final String data) {
        final String tokenData;

        if (null == data) {
            tokenData = "0123456789";
        } else {
            tokenData = data;
        }

        try {
            final JSONObject object = new JSONObject();
            object.put(PaymentTokenJsonFactory.JsonKeys.ID, webServiceId);
            object.put(PaymentTokenJsonFactory.JsonKeys.DATA, tokenData);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Create a nested representation of the required and optional JSON.
     *
     * @param webServiceId the ID of the {@link PaymentToken} on the web service.
     * @return valid JSON representation of the user.
     */
    @NonNull
    public static JSONObject getFullJsonObjectNested(final long webServiceId) {
        try {
            return new JSONObject().put(PaymentTokenJsonFactory.JsonKeys.MODEL_ROOT,
                    getFullJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private PaymentTokenFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
