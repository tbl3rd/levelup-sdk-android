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

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import com.google.gson.JsonObject;

import net.jcip.annotations.ThreadSafe;

/**
 * Fixtures for {@link Permission}s.
 */
@LevelUpApi(contract=Contract.INTERNAL)
@ThreadSafe
public final class PermissionFixture {

    @NonNull
    public static final String DESCRIPTION_FIXTURE_1 = "View your orders.";

    @NonNull
    public static final String DESCRIPTION_FIXTURE_2 = "Create orders.";

    @NonNull
    public static final String KEYNAME_FIXTURE_1 = "view_order_history";

    @NonNull
    public static final String KEYNAME_FIXTURE_2 = "create_orders";

    /**
     * Full model #1.
     *
     * @return a full model
     */
    @NonNull
    public static Permission getFullModel1() {
        return new Permission(DESCRIPTION_FIXTURE_1, KEYNAME_FIXTURE_1);
    }

    /**
     * Full model #2.
     *
     * @return a full model
     */
    @NonNull
    public static Permission getFullModel2() {
        return new Permission(DESCRIPTION_FIXTURE_2, KEYNAME_FIXTURE_2);
    }

    /**
     * Full JSON object #1.
     *
     * @return a full JSON object
     */
    @NonNull
    public static JsonObject getJsonObject1() {
        return getJsonObject(DESCRIPTION_FIXTURE_1, KEYNAME_FIXTURE_1);
    }

    /**
     * Full JSON object #2.
     *
     * @return a full JSON object
     */
    @NonNull
    public static JsonObject getJsonObject2() {
        return getJsonObject(DESCRIPTION_FIXTURE_2, KEYNAME_FIXTURE_2);
    }

    /**
     * @param description a human-readable description
     * @param keyname the unique key name of the permission
     * @return a fully-populated JSON object
     */
    @NonNull
    public static JsonObject getJsonObject(@NonNull final String description,
            @NonNull final String keyname) {

        final JsonObject model = new JsonObject();
        model.addProperty("description", description);
        model.addProperty("keyname", keyname);

        final JsonObject container = new JsonObject();
        container.add("permission", model);

        return container;
    }

    private PermissionFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
