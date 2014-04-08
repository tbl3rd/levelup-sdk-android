/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Error;
import com.scvngr.levelup.core.model.ErrorFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Locale;

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
        assertNull(error.getCode());
        assertEquals(ErrorFixture.MESSAGE_VALUE, error.getMessage());
        assertNull(error.getObject());
        assertNull(error.getProperty());

        error = new ErrorJsonFactory().from(ErrorFixture.getFullJsonObject());
        assertEquals(ErrorFixture.CODE_VALUE, error.getCode());
        assertEquals(ErrorFixture.MESSAGE_VALUE, error.getMessage());
        assertEquals(ErrorFixture.OBJECT_VALUE, error.getObject());
        assertEquals(ErrorFixture.PROPERTY_VALUE, error.getProperty());
    }

    /**
     * Tests JSON parsing of a list with one item.
     *
     * @throws JSONException
     */
    @SmallTest
    public void testJsonParse_listOfOne() throws JSONException {
        final String json = String.format(Locale.US,
                "[{ 'error': { 'code': '%s', 'message': '%s', 'object': '%s', 'property': '%s' }}]", //$NON-NLS-1$
                ErrorFixture.CODE_VALUE, ErrorFixture.MESSAGE_VALUE, ErrorFixture.OBJECT_VALUE,
                ErrorFixture.PROPERTY_VALUE);

        final List<Error> errors = new ErrorJsonFactory().fromList(new JSONArray(json));

        assertEquals(1, errors.size());

        final Error error = errors.get(0);

        assertNotNull(error);
        assertEquals(ErrorFixture.CODE_VALUE, error.getCode());
        assertEquals(ErrorFixture.MESSAGE_VALUE, error.getMessage());
        assertEquals(ErrorFixture.OBJECT_VALUE, error.getObject());
        assertEquals(ErrorFixture.PROPERTY_VALUE, error.getProperty());
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
            assertEquals(ErrorFixture.CODE_VALUE, e.getCode());
            assertEquals(ErrorFixture.MESSAGE_VALUE, e.getMessage());
            assertEquals(ErrorFixture.OBJECT_VALUE, e.getObject());
            assertEquals(ErrorFixture.PROPERTY_VALUE, e.getProperty());
        }
    }
}
