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
package com.scvngr.levelup.core.net;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.util.NullUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A generic set of tests for {@link com.scvngr.levelup.core.net.RequestBody}.
 *
 * @param <T> the type of {@link com.scvngr.levelup.core.net.RequestBody}.
 */
public abstract class AbstractRequestBodyTest<T extends RequestBody> extends AndroidTestCase {

    /**
     * Tests that the content returned by the fixture is the same as what is expected.
     */
    @SmallTest
    public void testContent() {
        assertEquals(getFixture().toString(), getBodyAsString(getFixture()));
    }

    /**
     * Tests that the implementation of {@link com.scvngr.levelup.core.net.RequestBody#getContentLength()} matches the actual
     * output.
     */
    @SmallTest
    public void testContentLength() {
        final T body = getFixture();

        assertEquals(body.getContentLength(), getBodyLength(body));
    }

    /**
     * Tests that the implementation of {@link com.scvngr.levelup.core.net.RequestBody#getContentType()} is sane.
     */
    @SmallTest
    public void testGetContentType() {
        final String contentType = getFixture().getContentType();
        assertTrue("Content type must have a /", contentType.contains("/"));
    }

    /**
     * Tests that the fixture survives parceling.
     */
    @SmallTest
    public void testParceling() {
        ParcelTestUtils.assertParcelableRoundtrips(getFixture());
    }

    /**
     * @return a fixture body to be used in tests.
     */
    @NonNull
    protected abstract T getFixture();

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

    /**
     * @param body the content to get length.
     * @return the byte array length of the body
     */
    private int getBodyLength(@NonNull final T body) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int result = 0;

        try {
            body.writeToOutputStream(NullUtils.nonNullContract(getContext()), baos);
            result =  baos.size();
            baos.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
