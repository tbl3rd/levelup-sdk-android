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

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.ErrorJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.Error}.
 */
public final class ErrorTest extends SupportAndroidTestCase {

    /**
     * Tests the deprecated constructor.
     */
    @SmallTest
    @SuppressWarnings("deprecation")
    public void testConstructor() {
        final Error expected = new Error(null, ErrorFixture.MESSAGE_VALUE, ErrorFixture
                .OBJECT_VALUE, ErrorFixture.PROPERTY_VALUE);
        final Error deprecated = new Error(ErrorFixture.MESSAGE_VALUE, ErrorFixture.OBJECT_VALUE,
                ErrorFixture.PROPERTY_VALUE);

        assertEquals(expected, deprecated);
    }

    /**
     * Tests parceling.
     */
    @SmallTest
    public void testParcelable() {
        ParcelTestUtils.assertParcelableRoundtrips(ErrorFixture.getFullModel());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.Error#equals} and {@link com.scvngr.levelup.core.model.Error#hashCode}.
     */
    @SmallTest
    public void testEqualsAndHashCode() {
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(ErrorJsonFactory.JsonKeys.class,
                new ErrorJsonFactory(), ErrorFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" });
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.Error#toString}.
     */
    @SmallTest
    public void testToString() {
        final Error error = new Error(ErrorFixture.CODE_VALUE, ErrorFixture.MESSAGE_VALUE,
                ErrorFixture.OBJECT_VALUE, ErrorFixture.PROPERTY_VALUE);
        final String errorString = error.toString();

        assertTrue(errorString.contains(ErrorFixture.CODE_VALUE));
        assertTrue(errorString.contains(ErrorFixture.MESSAGE_VALUE));
        assertTrue(errorString.contains(ErrorFixture.OBJECT_VALUE));
        assertTrue(errorString.contains(ErrorFixture.PROPERTY_VALUE));
    }
}
