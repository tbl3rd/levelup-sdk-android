package com.scvngr.levelup.core.net;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tests {@link StreamingResponse}.
 */
public final class StreamingResponseTest extends SupportAndroidTestCase {

    @SmallTest
    public void testConstructor_successResponseCode() throws MalformedURLException, IOException {
        final StreamingResponse response =
                new StreamingResponse(new MockHttpUrlConnection(HttpURLConnection.HTTP_OK));
        assertTrue(response.getData() instanceof TestInputStream);
    }

    @SmallTest
    public void testConstructor_errorResponseCode() throws MalformedURLException, IOException {
        final StreamingResponse response =
                new StreamingResponse(new MockHttpUrlConnection(HttpURLConnection.HTTP_BAD_REQUEST));
        assertTrue(response.getData() instanceof TestErrorStream);
    }

    @SmallTest
    public void testConstructor() throws MalformedURLException, IOException {
        final StreamingResponse response =
                new StreamingResponse(new MockHttpUrlConnection(HttpURLConnection.HTTP_OK));
        assertEquals(HttpURLConnection.HTTP_OK, response.getHttpStatusCode());
        assertNull(response.getError());
    }

    private static final class TestInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    private static final class TestErrorStream extends InputStream {

        @Override
        public int read() throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    private static class MockHttpUrlConnection extends HttpURLConnection {

        private final int responseCode;

        private final InputStream inputStream = new TestInputStream();
        private final InputStream errorStream = new TestErrorStream();

        /**
         * Constructor.
         *
         * @throws MalformedURLException if the URL is malformed
         */
        public MockHttpUrlConnection(final int responseCode) throws MalformedURLException {
            super(new URL("http://www.example.com")); //$NON-NLS-1$
            this.responseCode = responseCode;
        }

        @Override
        public int getResponseCode() throws IOException {
            return responseCode;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public InputStream getErrorStream() {
            return errorStream;
        }

        @Override
        public void disconnect() {
            // do nothing
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {
            // do nothing
        }
    }
}
