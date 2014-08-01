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

import com.scvngr.levelup.core.model.factory.json.WebLinkJsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link WebLink} models.
 */
public final class WebLinkFixture {

    /**
     * Gets a fully populated model.
     *
     * @return {@link WebLink} model
     */
    @NonNull
    public static WebLink getFullModel() {
        return getFullModel(1);
    }

    /**
     * Gets a fully populated model.
     *
     * @param webLinkTypeId the type ID of the {@link WebLink}.
     * @return {@link WebLink} model
     */
    @NonNull
    public static WebLink getFullModel(final long webLinkTypeId) {
        try {
            return new WebLinkJsonFactory().from(getFullJsonObject(webLinkTypeId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a minimally populated model.
     *
     * @param webLinkTypeId the type ID of the {@link WebLink}.
     * @return {@link WebLink} model
     */
    @NonNull
    public static WebLink getMinimalModel(final long webLinkTypeId) {
        try {
            return new WebLinkJsonFactory().from(getMinimalJsonObject(webLinkTypeId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param webLinkTypeId the type ID of the {@link WebLink}.
     * @return a {@link JSONObject} with all the WebLink required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final long webLinkTypeId) {
        try {
            final JSONObject object = new JSONObject();
            object.put(WebLinkJsonFactory.JsonKeys.WEB_LINK_TYPE_ID, webLinkTypeId);
            object.put(WebLinkJsonFactory.JsonKeys.TITLE, "title");
            object.put(WebLinkJsonFactory.JsonKeys.WEB_URL, "web_url");
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the WebLink required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(1);
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param webLinkTypeId the type ID of the {@link WebLink}.
     * @return a {@link JSONObject} with all the WebLink fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long webLinkTypeId) {
        try {
            final JSONObject object = getMinimalJsonObject(webLinkTypeId);
            object.put(WebLinkJsonFactory.JsonKeys.TITLE, "title");
            object.put(WebLinkJsonFactory.JsonKeys.WEB_URL, "http://web_url");
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the WebLink fields.
     * @throws JSONException if there is a parse error.
     */
    @NonNull
    public static JSONObject getFullJsonObject() throws JSONException {
        return getFullJsonObject(1);
    }

    private WebLinkFixture() {
        throw new UnsupportedOperationException("this class is non-instantiable");
    }
}
