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
