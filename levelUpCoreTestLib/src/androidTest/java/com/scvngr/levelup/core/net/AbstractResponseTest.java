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
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.AbstractResponse}.
 */
public final class AbstractResponseTest extends SupportAndroidTestCase {

    @SmallTest
    public void testConstructor2() throws MalformedURLException, IOException {
        final Exception e = new Exception();
        final AbstractResponseUnderTest response = new AbstractResponseUnderTest(e);
        assertEquals(AbstractResponse.HTTP_STATUS_CODE_UNUSED, response.getHttpStatusCode());
        assertNotNull(response.getError());
        assertEquals(e, response.getError());
    }

    @SmallTest
    public void testConstructor_withHttpHeaders() {
        final Map<String, List<String>> headers = new HashMap<String, List<String>>();
        final String contentLength = "Content-Length";
        final String contentType = "Content-Type";
        final String emptyList = "Empty-List";
        headers.put(contentLength, Arrays.asList(new String[] { "0" }));

        // Pretend that there are accidentally two headers.
        headers.put(contentType, Arrays.asList(new String[] { "application/json", "text/plain" }));

        // Empty list.
        headers.put(emptyList, Arrays.asList(new String[] {}));

        headers.put("X-Foo", null);

        final AbstractResponseUnderTest response =
                new AbstractResponseUnderTest(HttpStatus.SC_OK, Collections
                        .unmodifiableMap(headers), null);

        assertEquals("0", response.getHttpHeader(contentLength));
        assertNull(response.getHttpHeader("content-length")); // does not normalize yet
        assertNull(response.getHttpHeader("X-Kitteh"));
        assertNull(response.getHttpHeader("X-Foo"));
        assertNull(response.getHttpHeader(emptyList));

        assertEquals("application/json", response.getHttpHeader(contentType));
    }

    @SmallTest
    public void testConstructor_nullHttpHeaders() {
        final AbstractResponseUnderTest response =
                new AbstractResponseUnderTest(HttpStatus.SC_OK, null, null);
        assertNull(response.getHttpHeader("X-Foo"));
    }

    private static final class AbstractResponseUnderTest extends AbstractResponse<Object> {
        final Object object = new Object();

        public AbstractResponseUnderTest(@NonNull final Exception error) {
            super(error);
        }

        public AbstractResponseUnderTest(final int statusCode,
                final Map<String, List<String>> headers, final Exception error) {
            super(statusCode, headers, error);
        }

        @Override
        @Nullable
        public Object getData() {
            return object;
        }
    }
}
