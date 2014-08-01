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

package com.scvngr.levelup.core.net.request.factory;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory.PageCacheRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

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
        assertTrue(requestString.endsWith("/locations"));
        assertTrue("hits the v14 endpoints", requestString
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
