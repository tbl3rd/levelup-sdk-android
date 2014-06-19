/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
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
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
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
