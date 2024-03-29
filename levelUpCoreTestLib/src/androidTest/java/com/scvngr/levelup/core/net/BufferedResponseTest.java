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

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.BufferedResponse.ResponseTooLargeException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests {@link com.scvngr.levelup.core.net.BufferedResponse}.
 */
public final class BufferedResponseTest extends SupportAndroidTestCase {

    private static final String ONE_KB_OF_TEXT = getStringOfSize(1024);

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse} parceling.
     */
    @SmallTest
    public void testParcelable() {
        final BufferedResponse response = new BufferedResponse("test");
        final Parcel out = Parcel.obtain();
        response.writeToParcel(out, 0);
        out.setDataPosition(0);
        final BufferedResponse parceledResponse = BufferedResponse.CREATOR.createFromParcel(out);

        assertNull(parceledResponse.getError());
        assertEquals(response.getData(), parceledResponse.getData());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.LevelUpResponse} parceling.
     */
    @SmallTest
    public void testParcelable2() {
        final ExceptionStream stream = new ExceptionStream();
        final BufferedResponse response = new BufferedResponse(stream);
        final Parcel out = Parcel.obtain();
        response.writeToParcel(out, 0);
        out.setDataPosition(0);
        final BufferedResponse parceledResponse = BufferedResponse.CREATOR.createFromParcel(out);

        assertEquals(response.getData(), parceledResponse.getData());
        assertNotNull(response.getError());
        assertNotNull(parceledResponse.getError());
        assertTrue(response.getError() instanceof IOException);
        assertTrue(parceledResponse.getError() instanceof IOException);
        assertEquals(response.getError().getMessage(), parceledResponse.getError().getMessage());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.BufferedResponse#readStream(java.io.InputStream)}.
     */
    @SmallTest
    public void testReadStream_validStreamData() {
        final String testString = "this is a test string.Yes, I said TEST!";
        final InputStream stream = new ByteArrayInputStream(testString.getBytes());

        try {
            final StringBuilder result = BufferedResponse.readStream(stream);
            assertEquals(testString, result.toString());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.BufferedResponse#readStream(java.io.InputStream)} with a large string that is
     * under the max size limit.
     */
    @SmallTest
    public void testReadStream_largeStreamData() {
        final StringBuilder builder = new StringBuilder();
        while (builder.length() < BufferedResponse.MAX_DATA_SIZE_BYTES) {
            builder.append(ONE_KB_OF_TEXT);
        }
        final InputStream stream = new ByteArrayInputStream(builder.toString().getBytes());

        try {
            final StringBuilder result = BufferedResponse.readStream(stream);
            assertEquals(builder.toString(), result.toString());
        } catch (final IOException e) {
            fail("Exception thrown when reading stream");
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.BufferedResponse#readStream(java.io.InputStream)} with a large string that is
     * over the max size limit.
     */
    @SmallTest
    public void testReadStream_tooLargeStreamData() {
        final StringBuilder builder = new StringBuilder();
        while (builder.length() <= BufferedResponse.MAX_DATA_SIZE_BYTES) {
            builder.append(ONE_KB_OF_TEXT);
        }
        final InputStream stream = new ByteArrayInputStream(builder.toString().getBytes());

        try {
            BufferedResponse.readStream(stream);
            fail("readStream() did not throw the proper exception");
        } catch (final IOException e) {
            assertTrue(e instanceof ResponseTooLargeException);
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.BufferedResponse#readStream(java.io.InputStream)} data this is not a multiple of
     * the buffer size. This tests to make sure that we don't add extra junk
     */
    @SmallTest
    public void testReadStream_withDataBufferOfOddSize() {
        final StringBuilder builder = new StringBuilder();

        // Make sure the string size leaves room at the end of the last buffer that's read
        final int stringSize = (int) (BufferedResponse.READ_BUFFER_SIZE_BYTES * 1.5);
        for (int i = 0; i < stringSize; i++) {
            builder.append('a');
        }

        final InputStream stream = new ByteArrayInputStream(builder.toString().getBytes());

        try {
            final StringBuilder result = BufferedResponse.readStream(stream);
            assertEquals(builder.toString(), result.toString());
        } catch (final IOException e) {
            fail("Exception thrown when reading stream");
        }
    }

    @SmallTest
    public void testGetError() {
        {
            final Exception e = new Exception();
            final BufferedResponse response = new BufferedResponse(new StreamingResponse(e));
            assertEquals(e, response.getError());
        }

        {
            final ExceptionStream stream = new ExceptionStream();
            final BufferedResponse response = new BufferedResponse(stream);
            assertEquals(stream.e, response.getError());
        }
    }

    /**
     * Helper method to generate a string of a certain length. Does it the inefficient way.
     *
     * @param size the length of string to generate
     * @return the string of the size requested
     */
    private static String getStringOfSize(final int size) {
        final StringBuilder b = new StringBuilder();

        for (int i = 0; i < size; i++) {
            b.append('a');
        }

        return b.toString();
    }

    private static final class ExceptionStream extends InputStream {

        final IOException e;

        public ExceptionStream() {
            e = new IOException();
        }

        @Override
        public int read() throws IOException {
            throw e;
        }
    }
}
