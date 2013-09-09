package com.scvngr.levelup.core.net;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests {@link AbstractResponse}.
 */
public final class AbstractResponseTest extends AndroidTestCase {

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
        final String contentLength = "Content-Length"; //$NON-NLS-1$
        final String contentType = "Content-Type"; //$NON-NLS-1$
        final String emptyList = "Empty-List"; //$NON-NLS-1$
        headers.put(contentLength, Arrays.asList(new String[] { "0" })); //$NON-NLS-1$

        // Pretend that there are accidentally two headers.
        headers.put(contentType, Arrays.asList(new String[] { "application/json", "text/plain" })); //$NON-NLS-1$ //$NON-NLS-2$

        // Empty list.
        headers.put(emptyList, Arrays.asList(new String[] {}));

        headers.put("X-Foo", null); //$NON-NLS-1$

        final AbstractResponseUnderTest response =
                new AbstractResponseUnderTest(HttpStatus.SC_OK, Collections
                        .unmodifiableMap(headers), null);

        assertEquals("0", response.getHttpHeader(contentLength)); //$NON-NLS-1$
        assertNull(response.getHttpHeader("content-length")); // does not normalize yet //$NON-NLS-1$
        assertNull(response.getHttpHeader("X-Kitteh")); //$NON-NLS-1$
        assertNull(response.getHttpHeader("X-Foo")); //$NON-NLS-1$
        assertNull(response.getHttpHeader(emptyList));

        assertEquals("application/json", response.getHttpHeader(contentType)); //$NON-NLS-1$
    }

    @SmallTest
    public void testConstructor_nullHttpHeaders() {
        final AbstractResponseUnderTest response =
                new AbstractResponseUnderTest(HttpStatus.SC_OK, null, null);
        assertNull(response.getHttpHeader("X-Foo")); //$NON-NLS-1$
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
