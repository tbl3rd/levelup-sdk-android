package com.scvngr.levelup.core.net.request.factory;

import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory.PageCacheRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.net.URL;
import java.util.Locale;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.AppLocationListRequestFactory}.
 */
public final class LocationListRequestFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testBuildGetLocationsListRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new LocationListRequestFactory(getContext(),
                        new MockAccessTokenRetriever(), new StubPageRetriever())
                        .getFirstPageRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getQueryParams(getContext()).size());
        assertEquals(0, request.getBodyLength(getContext()));
        final String requestString = request.getUrlString(getContext());
        assertNotNull(requestString);
        assertTrue(requestString.endsWith("/locations")); //$NON-NLS-1$
        assertTrue("hits the v14 endpoints", requestString //$NON-NLS-1$
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }

    private final class StubPageRetriever implements PageCacheRetriever {

        @Override
        @Nullable
        public Uri getNextPageUrl(@NonNull final String pageKey) {
            return null;
        }

        @Override
        public void setNextPage(@NonNull final String pageKey, @Nullable final Uri page) {
            // Do nothing
        }
    }
}