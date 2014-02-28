/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.util.NullUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A generic set of tests for {@link RequestBody}.
 *
 * @param <T> the type of {@link RequestBody}.
 */
public abstract class AbstractRequestBodyTest<T extends RequestBody> extends AndroidTestCase {

    /**
     * Tests that the content returned by the fixture is the same as what is expected.
     */
    @SmallTest
    public void testContent() {
        assertEquals(getFixtureAsString(), getBodyAsString(getFixture()));
    }

    /**
     * Tests that the implementation of {@link RequestBody#getContentLength()} matches the actual
     * output.
     */
    @SmallTest
    public void testContentLength() {
        final T body = getFixture();

        assertEquals(body.getContentLength(), getBodyAsString(body).length());
    }

    /**
     * Tests that the implementation of {@link RequestBody#getContentType()} is sane.
     */
    @SmallTest
    public void testGetContentType() {
        final String contentType = getFixture().getContentType();
        assertTrue("Content type must have a /", contentType.contains("/")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests that the fixture survives parceling.
     */
    @SmallTest
    public void testParceling() {
        ParcelTestUtils.assertParcelableRoundtrips(getFixture());
    }

    /**
     * @return a fixture body to be used in tests. This should match {@link #getFixtureAsString()}.
     */
    @NonNull
    protected abstract T getFixture();

    /**
     * @return the string contents of the fixture body. This should match what {@link #getFixture()}
     *         should return for the body.
     */
    @NonNull
    protected abstract String getFixtureAsString();

    /**
     * @param body the content to serialize.
     * @return the body, written out to a String.
     */
    @NonNull
    private String getBodyAsString(@NonNull final T body) {
        String bodyAsString;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            body.writeToOutputStream(NullUtils.nonNullContract(getContext()), baos);

            try {
                bodyAsString = NullUtils.nonNullContract(baos.toString());
            } finally {
                baos.close();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return bodyAsString;
    }
}
