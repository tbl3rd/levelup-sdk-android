package com.scvngr.levelup.core.model;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.ErrorV13JsonFactory;

/**
 * Fixture for {@link ErrorV13}s.
 */
@ThreadSafe
public final class ErrorV13Fixture {

    /**
     * @return valid, fully populated {@link ErrorV13}.
     */
    @NonNull
    public static final ErrorV13 getFullModel() {
        try {
            return new ErrorV13JsonFactory().from(getFullJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * Create a nested representation of the required JSON.
     *
     * @return valid JSON representation of the error
     * @throws JSONException for parsing errors.
     */
    @NonNull
    public static JSONObject getNestedJsonObject() {
        // Nest under the model root key
        try {
            return new JSONObject().put(ErrorV13JsonFactory.JsonKeys.MODEL_ROOT,
                    getMinimalJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Create an unnested representation of the required JSON with optional fields set.
     *
     * @return valid JSON representation of the error
     * @throws JSONException for parsing errors.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        try {
            return getMinimalJsonObject().put(ErrorV13JsonFactory.JsonKeys.SPONSOR_EMAIL,
                    "test@example.com") //$NON-NLS-1$
                    .put(ErrorV13JsonFactory.JsonKeys.ERROR_DESCRIPTION, "a description of the error"); //$NON-NLS-1$
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Create an unnested representation of the required JSON.
     *
     * @return valid JSON representation of the error
     * @throws JSONException for parsing errors.
     */
    @NonNull
    private static JSONObject getMinimalJsonObject() {
        try {
            return new JSONObject().put(ErrorV13JsonFactory.JsonKeys.ERROR, "error"); //$NON-NLS-1$
        } catch (final JSONException e) {
            throw new AssertionError();
        }
    }

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ErrorV13Fixture()
     {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
