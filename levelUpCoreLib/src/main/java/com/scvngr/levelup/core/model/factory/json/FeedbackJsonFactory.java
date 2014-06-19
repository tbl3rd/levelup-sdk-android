package com.scvngr.levelup.core.model.factory.json;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.Feedback;

/**
 * A JSON factory to create a support {@link Feedback}.
 */
public class FeedbackJsonFactory extends GsonModelFactory<Feedback> {

    @NonNull
    private static final String TYPE_KEY = "feedback"; //$NON-NLS-1$

    /**
     * Constructor.
     */
    public FeedbackJsonFactory() {
        super(TYPE_KEY, Feedback.class, false);
    }
}
