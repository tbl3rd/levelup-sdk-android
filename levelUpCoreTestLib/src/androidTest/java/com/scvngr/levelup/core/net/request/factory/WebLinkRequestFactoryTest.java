package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;

/**
 * Tests {@link WebLinkRequestFactory}.
 */
public final class WebLinkRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final WebLinkRequestFactory factory = new WebLinkRequestFactory(getContext());
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertNull(factory.getAccessTokenRetriever());
    }

    /**
     * Tests the {@link WebLinkRequestFactory#buildGetWebLinksForLocationRequest(long)} method.
     *
     * @throws BadRequestException thrown by {@link AbstractRequest#getUrl}.
     */
    @SmallTest
    public void testBuildGetWebLinksForLocationRequest() throws BadRequestException {
        final AbstractRequest request =
                new WebLinkRequestFactory(getContext()).buildGetWebLinksForLocationRequest(1);

        assertEquals(HttpMethod.GET, request.getMethod());
        final String path = request.getUrl(getContext()).getPath();
        assertNotNull(path);
        assertTrue(path.contains(LevelUpRequest.API_VERSION_CODE_V15));
        assertTrue(path.endsWith(String.format(Locale.US, "locations/%d/web_links", 1)));
    }
}
