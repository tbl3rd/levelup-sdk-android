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

import com.scvngr.levelup.core.model.factory.json.AccessTokenJsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link AccessToken}.
 */
public final class AccessTokenFixture {
    /**
     * An arbitrary access token string.
     */
    public static final String ACCESS_TOKEN_FIXTURE_1 = "sx82GslFAv8ZlHOhpftd";

    /**
     * A random user ID.
     *
     * @see <a href="http://xkcd.com/221/">xkcd #221 - Random Number</a>
     */
    @NonNull
    public static final Long USER_ID_FIXTURE_1 = 4L;

    /**
     * @return A full {@link AccessToken} model.
     */
    @NonNull
    public static AccessToken getFullModel() {
        return new AccessToken(ACCESS_TOKEN_FIXTURE_1, USER_ID_FIXTURE_1);
    }

    /**
     * @param accessToken access token string.
     * @param userId web service user ID.
     * @return A full {@link AccessToken} model.
     */
    @NonNull
    public static AccessToken getFullModel(@NonNull final String accessToken, final Long userId) {
        return new AccessToken(accessToken, userId);
    }

    /**
     * @return a full JSON representation of an {@link AccessToken}, as would typically be returned
     *         from the web service.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(ACCESS_TOKEN_FIXTURE_1, USER_ID_FIXTURE_1);
    }

    /**
     * @return a minimal JSON representation of an {@link AccessToken}, as would typically be
     * returned from the web service.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(ACCESS_TOKEN_FIXTURE_1);
    }

    /**
     * @param accessToken access token string.
     * @param userId web service user ID.
     * @return a full JSON representation of an {@link AccessToken}, as would typically be returned
     *         from the web service.
     */
    @NonNull
    public static JSONObject
            getFullJsonObject(@NonNull final String accessToken, @NonNull final Long userId) {
        try {
            final JSONObject object = new JSONObject();
            object.put(AccessTokenJsonFactory.JsonKeys.TOKEN, accessToken);
            object.put(AccessTokenJsonFactory.JsonKeys.USER_ID, userId);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * @param accessToken access token string.
     * @return a minimal JSON representation of an {@link AccessToken},
     * as would typically be returned from the web service.
     */
    public static JSONObject getMinimalJsonObject(@NonNull final String accessToken) {
        try {
            final JSONObject object = new JSONObject();
            object.put(AccessTokenJsonFactory.JsonKeys.TOKEN, accessToken);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private AccessTokenFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
