package com.scvngr.levelup.core.util;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.util.WebLinkParser.MalformedWebLinkException;
import com.scvngr.levelup.core.util.WebLinkParser.WebLink;

/**
 * Tests {@link WebLinkParser}.
 *
 */
@SuppressWarnings("nls")
public class WebLinkParserTest extends AndroidTestCase {

    public static final String RFC5988_EXAMPLE_1 =
            "<http://example.com/TheBook/chapter2>; rel=\"previous\"; title=\"previous chapter\"";

    public static final String RFC5988_EXAMPLE_2 = "</>; rel=\"http://example.net/foo\"";

    public static final String RFC5988_EXAMPLE_3 =
            "<http://example.org/>; rel=\"start http://example.net/relation/other\"";

    /**
     * Tests the parser against the known-good example 1 from RFC5988.
     *
     * @throws MalformedWebLinkException if parsing fails.
     */
    public void testRFC5988Example1() throws MalformedWebLinkException {
        final WebLink example1 = WebLinkParser.parseLinkHeader(RFC5988_EXAMPLE_1);

        assertNotNull(example1);

        assertEquals(Uri.parse("http://example.com/TheBook/chapter2"), example1.getLink());

        assertEquals(2, example1.getParameters().size());

        assertEquals("previous", example1.getParameters().get("rel"));

        assertEquals("previous chapter", example1.getParameters().get("title"));
    }

    /**
     * Tests the parser against the known-good example 2 from RFC5988.
     *
     * @throws MalformedWebLinkException if parsing fails.
     */
    public void testRFC5988Example2() throws MalformedWebLinkException {
        final WebLink example2 = WebLinkParser.parseLinkHeader(RFC5988_EXAMPLE_2);

        assertNotNull(example2);

        assertEquals(Uri.parse("/"), example2.getLink());

        assertEquals(1, example2.getParameters().size());

        assertEquals("http://example.net/foo", example2.getParameters().get("rel"));
    }

    /**
     * Tests the parser against the known-good example 2 from RFC5988 using the context URL.
     *
     * @throws MalformedWebLinkException if parsing fails.
     */
    public void testRFC5988Example2Relative() throws MalformedWebLinkException {
        final WebLink example2 =
                WebLinkParser.parseLinkHeader(Uri.parse("http://example.org/"), RFC5988_EXAMPLE_2);

        assertNotNull(example2);

        assertEquals(Uri.parse("http://example.org/"), example2.getLink());

        assertEquals(1, example2.getParameters().size());

        assertEquals("http://example.net/foo", example2.getParameters().get("rel"));
    }

    /**
     * Tests the parser against the known-good example 3 from RFC5988.
     *
     * @throws MalformedWebLinkException if parsing fails.
     */
    public void testRFC5988Example3() throws MalformedWebLinkException {
        final WebLink example3 = WebLinkParser.parseLinkHeader(RFC5988_EXAMPLE_3);

        assertNotNull(example3);

        assertEquals(Uri.parse("http://example.org/"), example3.getLink());

        assertEquals(1, example3.getParameters().size());

        assertEquals("start http://example.net/relation/other", example3.getParameters().get("rel"));
    }

    /**
     * Tests the parser against the known-good example 3 from RFC5988 using the context URL.
     *
     * @throws MalformedWebLinkException if parsing fails.
     */
    public void testRFC5988Example3Relative() throws MalformedWebLinkException {
        final WebLink example3 =
                WebLinkParser.parseLinkHeader(Uri.parse("http://example.com/"), RFC5988_EXAMPLE_3);

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
     * Asserts that the given link header, when parsed, throws a {@link MalformedWebLinkException}.
     *
     * @param link the link to parse.
     */
    private void assertThrowsMalformedWebLinkException(final String link) {
        boolean caught = false;
        try {
            WebLinkParser.parseLinkHeader(link);
        } catch (final MalformedWebLinkException e) {
            caught = true;
        }

        assertTrue("exception should be thrown", caught);
    }
}
