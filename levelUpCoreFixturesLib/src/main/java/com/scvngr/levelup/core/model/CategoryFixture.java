/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
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
