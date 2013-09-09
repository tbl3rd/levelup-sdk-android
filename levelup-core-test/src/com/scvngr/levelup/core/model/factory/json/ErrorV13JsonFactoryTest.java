package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.ErrorV13;
import com.scvngr.levelup.core.model.ErrorV13Fixture;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link ErrorV13JsonFactory}.
 */
public final class ErrorV13JsonFactoryTest extends AndroidTestCase {
    /**
     * Tests basic json parsing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        // Make sure required fields are parsed, and that optional fields are null if missing
        ErrorV13 error = new ErrorV13JsonFactory().from(ErrorV13Fixture.getNestedJsonObject());
        assertEquals("error", error.getError()); //$NON-NLS-1$
        assertNull(error.getErrorDescription());
        assertNull(error.getSponsorEmail());

        // Make sure optional fields are parsed if set
        final JSONObject fullRepNested = ErrorV13Fixture.getNestedJsonObject();
        fullRepNested.getJSONObject(ErrorV13JsonFactory.JsonKeys.MODEL_ROOT).put(
                ErrorV13JsonFactory.JsonKeys.SPONSOR_EMAIL, "sponsor_email"); //$NON-NLS-1$
        fullRepNested.getJSONObject(ErrorV13JsonFactory.JsonKeys.MODEL_ROOT).put(
                ErrorV13JsonFactory.JsonKeys.ERROR_DESCRIPTION, "error_description"); //$NON-NLS-1$

        error = new ErrorV13JsonFactory().from(fullRepNested);
        assertEquals("error_description", error.getErrorDescription()); //$NON-NLS-1$
        assertEquals("sponsor_email", error.getSponsorEmail()); //$NON-NLS-1$
    }

    /**
     * Tests basic json parsing with required key missing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKey() throws JSONException {
        try {
            final JSONObject object = ErrorV13Fixture.getNestedJsonObject();
            object.getJSONObject(ErrorV13JsonFactory.JsonKeys.MODEL_ROOT).remove(
                    ErrorV13JsonFactory.JsonKeys.ERROR);
            new ErrorV13JsonFactory().from(object);
            fail("no error field should throw error"); //$NON-NLS-1$
        } catch (final JSONException e) {
            // Expected exception
        }
    }
}
