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
 * Tests {@link com.scvngr.levelup.core.model.factory.json.ErrorJsonFactory}.
 */
public final class ErrorJsonFactoryTest extends SupportAndroidTestCase {
    /**
     * Tests basic json parsing.
     *
     * @throws org.json.JSONException for parsing errors.
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
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testJsonParse_listOfOne() throws JSONException {
        final String json = String.format(Locale.US,
                "[{ 'error': { 'code': '%s', 'message': '%s', 'object': '%s', 'property': '%s' }}]",
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
     * @throws org.json.JSONException for parsing errors.
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
