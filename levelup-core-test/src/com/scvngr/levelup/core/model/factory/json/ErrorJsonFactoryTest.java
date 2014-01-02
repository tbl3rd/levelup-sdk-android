package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Error;
import com.scvngr.levelup.core.model.ErrorFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Tests {@link ErrorJsonFactory}.
 */
public final class ErrorJsonFactoryTest extends SupportAndroidTestCase {
    /**
     * Tests basic json parsing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        // Make sure required fields are parsed, and that optional fields are null if missing
        Error error = new ErrorJsonFactory().from(ErrorFixture.getNestedJsonObject());
        assertEquals("message", error.getMessage()); //$NON-NLS-1$
        assertNull(error.getObject());
        assertNull(error.getProperty());

        error = new ErrorJsonFactory().from(ErrorFixture.getFullJsonObject());
        assertEquals("object", error.getObject()); //$NON-NLS-1$
        assertEquals("property", error.getProperty()); //$NON-NLS-1$
    }

    /**
     * Tests JSON parsing of a list with one item.
     *
     * @throws JSONException
     */
    @SmallTest
    public void testJsonParse_listOfOne() throws JSONException {
        final String json =
                "[{ 'error': { 'message': 'The email address or password you provided is incorrect.', 'object': 'access_token', 'property': 'base' }}]"; //$NON-NLS-1$
        final List<Error> errors = new ErrorJsonFactory().fromList(new JSONArray(json));
        assertEquals(1, errors.size());
        final Error error = errors.get(0);
        assertNotNull(error);
        assertEquals("The email address or password you provided is incorrect.", error.getMessage()); //$NON-NLS-1$
        assertEquals("access_token", error.getObject()); //$NON-NLS-1$
        assertEquals("base", error.getProperty()); //$NON-NLS-1$
    }

    /**
     * Tests basic json parsing of a list.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_listBasic() throws JSONException {
        final List<Error> errors =
                new ErrorJsonFactory().fromList(ErrorFixture.getListOfFullJsonObjects());
        assertEquals(3, errors.size());

        for (final Error e : errors) {
            assertEquals("message", e.getMessage()); //$NON-NLS-1$
            assertEquals("object", e.getObject()); //$NON-NLS-1$
            assertEquals("property", e.getProperty()); //$NON-NLS-1$
        }
    }
}
