/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.factory.json.AppJsonFactory;

import com.google.gson.JsonObject;

import net.jcip.annotations.ThreadSafe;

/**
 * Fixture for {@link App}.
 */
@LevelUpApi(contract=Contract.INTERNAL)
@ThreadSafe
public final class AppFixture {

    /**
     * App description fixture #1.
     */
    @NonNull
    public static final String APP_DESCRIPTION_FIXTURE_1 = "The Best Burgers in Town";

    /**
     * App name fixture #1.
     */
    @NonNull
    public static final String APP_NAME_FIXTURE_1 = "Ariburgers";

    /**
     * A random app ID.
     *
     * @see <a href="http://xkcd.com/221/">xkcd #221 - Random Number</a>
     */
    public static final long APP_ID_FIXTURE_1 = 4;

    /**
     * App image URL fixture #1.
     */
    @NonNull
    public static final String APP_IMAGE_URL_1 = "http://ariburgers.com/logo.jpg";

    /**
     * @param id the web service ID of the app
     * @return a full {@link App} model.
     */
    @NonNull
    public static App getFullModel(final long id) {
        return new App(APP_DESCRIPTION_FIXTURE_1, id, APP_IMAGE_URL_1, APP_NAME_FIXTURE_1);
    }

    /**
     * @return a full {@link App} model.
     */
    @NonNull
    public static App getFullModel() {
        return getFullModel(APP_ID_FIXTURE_1);
    }

    /**
     * @param id the web service ID of the app
     * @return a minimal {@link App} model, with only required fields filled.
     */
    @NonNull
    public static App getMinimalModel(final long id) {
        return new App(APP_DESCRIPTION_FIXTURE_1, id, null, APP_NAME_FIXTURE_1);
    }

    /**
     * @return a minimal {@link App} model, with only required fields filled.
     */
    @NonNull
    public static App getMinimalModel() {
        return getMinimalModel(APP_ID_FIXTURE_1);
    }

    /**
     * @return a full {@link JsonObject} representing an {@link App}.
     */
    @NonNull
    public static JsonObject getFullJsonObject() {
        return getJsonObject(APP_DESCRIPTION_FIXTURE_1, APP_ID_FIXTURE_1, APP_IMAGE_URL_1,
                APP_NAME_FIXTURE_1);
    }

    /**
     * @return a full {@link JsonObject} representing an {@link App}.
     */
    @NonNull
    public static JsonObject getMinimalJsonObject() {
        return getJsonObject(APP_DESCRIPTION_FIXTURE_1, APP_ID_FIXTURE_1, null, APP_NAME_FIXTURE_1);
    }

    /**
     * @param description the description of the app
     * @param appId the web service ID of the app
     * @param imageUrl an optional image URL for the app
     * @param name the name of the app
     * @return a {@link JsonObject} for the given parameters.
     */
    @NonNull
    public static JsonObject getJsonObject(@NonNull final String description, final long appId,
            @Nullable final String imageUrl, @NonNull final String name) {
        final JsonObject object = new JsonObject();

        object.addProperty("description", description);
        object.addProperty("id", appId);
        object.addProperty("image_url", imageUrl);
        object.addProperty("name", name);

        final JsonObject container = new JsonObject();
        container.add(AppJsonFactory.MODEL_ROOT, object);

        return container;
    }

    private AppFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
