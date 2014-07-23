/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

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
