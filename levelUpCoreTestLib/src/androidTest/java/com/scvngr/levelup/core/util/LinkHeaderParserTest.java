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
package com.scvngr.levelup.core.util;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link LinkHeaderParser}.
 *
 */
public final class LinkHeaderParserTest extends SupportAndroidTestCase {

    @NonNull
    public static final String RFC5988_EXAMPLE_1 =
            "<http://example.com/TheBook/chapter2>; rel=\"previous\"; title=\"previous chapter\"";

    @NonNull
    public static final String RFC5988_EXAMPLE_2 = "</>; rel=\"http://example.net/foo\"";

    @NonNull
    public static final String RFC5988_EXAMPLE_3 =
            "<http://example.org/>; rel=\"start http://example.net/relation/other\"";

    /**
     * Tests the parser against the known-good example 1 from RFC5988.
     *
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if parsing fails.
     */
    public void testRFC5988Example1() throws LinkHeaderParser.MalformedLinkHeaderException {
        final LinkHeaderParser.LinkHeader example1 = LinkHeaderParser
                .parseLinkHeader(RFC5988_EXAMPLE_1);

        assertNotNull(example1);

        assertEquals(Uri.parse("http://example.com/TheBook/chapter2"), example1.getLink());

        assertEquals(2, example1.getParameters().size());

        assertEquals("previous", example1.getParameters().get("rel"));

        assertEquals("previous chapter", example1.getParameters().get("title"));
    }

    /**
     * Tests the parser against the known-good example 2 from RFC5988.
     *
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if parsing fails.
     */
    public void testRFC5988Example2() throws LinkHeaderParser.MalformedLinkHeaderException {
        final LinkHeaderParser.LinkHeader example2 = LinkHeaderParser
                .parseLinkHeader(RFC5988_EXAMPLE_2);

        assertNotNull(example2);

        assertEquals(Uri.parse("/"), example2.getLink());

        assertEquals(1, example2.getParameters().size());

        assertEquals("http://example.net/foo", example2.getParameters().get("rel"));
    }

    /**
     * Tests the parser against the known-good example 2 from RFC5988 using the context URL.
     *
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if parsing fails.
     */
    public void testRFC5988Example2Relative() throws LinkHeaderParser.MalformedLinkHeaderException {
        final LinkHeaderParser.LinkHeader example2 =
                LinkHeaderParser
                        .parseLinkHeader(Uri.parse("http://example.org/"), RFC5988_EXAMPLE_2);

        assertNotNull(example2);

        assertEquals(Uri.parse("http://example.org/"), example2.getLink());

        assertEquals(1, example2.getParameters().size());

        assertEquals("http://example.net/foo", example2.getParameters().get("rel"));
    }

    /**
     * Tests the parser against the known-good example 3 from RFC5988.
     *
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if parsing fails.
     */
    public void testRFC5988Example3() throws LinkHeaderParser.MalformedLinkHeaderException {
        final LinkHeaderParser.LinkHeader example3 = LinkHeaderParser
                .parseLinkHeader(RFC5988_EXAMPLE_3);

        assertNotNull(example3);

        assertEquals(Uri.parse("http://example.org/"), example3.getLink());

        assertEquals(1, example3.getParameters().size());

        assertEquals("start http://example.net/relation/other", example3.getParameters().get("rel"));
    }

    /**
     * Tests the parser against the known-good example 3 from RFC5988 using the context URL.
     *
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if parsing fails.
     */
    public void testRFC5988Example3Relative() throws LinkHeaderParser.MalformedLinkHeaderException {
        final LinkHeaderParser.LinkHeader example3 =
                LinkHeaderParser
                        .parseLinkHeader(Uri.parse("http://example.com/"), RFC5988_EXAMPLE_3);

        assertNotNull(example3);

        assertEquals(Uri.parse("http://example.org/"), example3.getLink());

        assertEquals(1, example3.getParameters().size());

        assertEquals("start http://example.net/relation/other", example3.getParameters().get("rel"));
    }

    /**
     * Tests a number of malformed header strings.
     */
    public void testMalformedWebLink() {
        // empty case
        assertThrowsMalformedWebLinkException("");

        // missing link
        assertThrowsMalformedWebLinkException("<>");

        // only whitespace in link
        assertThrowsMalformedWebLinkException("<     >");

        // missing link
        assertThrowsMalformedWebLinkException(";");

        // only whitespace
        assertThrowsMalformedWebLinkException("     ");

        // a number of permutations of bad parameters
        assertThrowsMalformedWebLinkException("</> ;");
        assertThrowsMalformedWebLinkException("</> ;;;;;;;");
        assertThrowsMalformedWebLinkException("</> ;a");
        assertThrowsMalformedWebLinkException("</> ;aaaaa");
        assertThrowsMalformedWebLinkException("</> ;a=");
        assertThrowsMalformedWebLinkException("</> ;aaaaa=");
        assertThrowsMalformedWebLinkException("</>; =a");
        assertThrowsMalformedWebLinkException("</>; =aaaaa");
        assertThrowsMalformedWebLinkException("</>; ======a");
    }

    /**
     * Asserts that the given link header, when parsed, throws a {@link com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException}.
     *
     * @param link the link to parse.
     */
    private void assertThrowsMalformedWebLinkException(@NonNull final String link) {
        boolean caught = false;
        try {
            LinkHeaderParser.parseLinkHeader(link);
        } catch (final LinkHeaderParser.MalformedLinkHeaderException e) {
            caught = true;
        }

        assertTrue("exception should be thrown", caught);
    }
}
