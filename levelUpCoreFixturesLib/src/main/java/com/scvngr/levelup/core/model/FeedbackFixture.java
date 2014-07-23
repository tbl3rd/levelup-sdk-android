package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
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
