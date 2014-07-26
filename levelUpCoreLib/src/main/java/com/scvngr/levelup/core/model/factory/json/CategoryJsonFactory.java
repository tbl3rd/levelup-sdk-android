/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Category;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON factory for {@link Category}.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class CategoryJsonFactory extends AbstractJsonModelFactory<Category> {

    /**
     * Constructor.
     */
    public CategoryJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @Override
    @NonNull
    protected Category createFrom(@NonNull final JSONObject json) throws JSONException {
        final int id = json.getInt(JsonKeys.ID);
        final String name = NullUtils.nonNullContract(json.getString(JsonKeys.NAME), JsonKeys.NAME);

        return new Category(id, name);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final class JsonKeys {
        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        @NonNull
        public static final String MODEL_ROOT = "category";

        /**
         * Public web service ID.
         */
        @JsonValueType(JsonType.LONG)
        @NonNull
        public static final String ID = "id";

        /**
         * Category name.
         */
        @JsonValueType(JsonType.STRING)
        @NonNull
        public static final String NAME = "name";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }
}
