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

import com.scvngr.levelup.core.model.factory.json.FeedbackJsonFactory;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.JsonObject;

import net.jcip.annotations.ThreadSafe;

/**
 * Fixture for {@link Feedback}.
 */
@ThreadSafe
public final class FeedbackFixture {

    /**
     * @return A minimal {@link Feedback} model.
     */
    @NonNull
    public static Feedback getMinimalModel() {
        return new Feedback(null, "question_text", 1);
    }

    /**
     * @return A full {@link Feedback} model.
     */
    @NonNull
    public static Feedback getFullModel() {
        return new Feedback("comment", "question_text", 1);
    }

    /**
     * @return A minimal {@link Feedback} model.
     */
    @NonNull
    public static JsonObject getFullJsonObject() {
        return NullUtils.nonNullContract(new FeedbackJsonFactory().toJsonElement(getFullModel())
                .getAsJsonObject());
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private FeedbackFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
