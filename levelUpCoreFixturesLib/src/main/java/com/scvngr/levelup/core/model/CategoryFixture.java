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

import com.scvngr.levelup.core.model.factory.json.CategoryJsonFactory;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link Category}s.
 */
@Immutable
@ThreadSafe
public final class CategoryFixture {

    /**
     * @param webServiceId the web service ID of the {@link Category}.
     * @return a fully-populated {@link Category}.
     */
    @NonNull
    public static Category getFullModel(final long webServiceId) {
        try {
            return new CategoryJsonFactory().from(getFullJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * @param webServiceId the web service ID of the {@link Category}.
     * @return a {@link JSONObject} with all the Category fields (none are optional).
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long webServiceId) {
        final JSONObject object = new JSONObject();

        try {
            object.put(CategoryJsonFactory.JsonKeys.ID, webServiceId);
            object.put(CategoryJsonFactory.JsonKeys.NAME, "category name");
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }

        return object;
    }

    /**
     * @return a {@link JSONObject} with all the Category fields (none are optional).
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private CategoryFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
