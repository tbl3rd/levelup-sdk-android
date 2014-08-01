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

import com.scvngr.levelup.core.model.WebLinkFixture;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.DeviceUtil;

import java.net.URL;
import java.util.Map;

/**
 * Tests {@link WebLinkTypeRequestFactory}.
 */
public class WebLinkTypeRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final WebLinkTypeRequestFactory factory = new WebLinkTypeRequestFactory(getContext());
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertNull(factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildLinkIconImageRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new WebLinkTypeRequestFactory(getContext())
                        .buildLinkIconImageRequest(WebLinkFixture.getMinimalModel(1));

        assertEquals(HttpMethod.GET, request.getMethod());
        final Map<String, String> queryParams = request.getQueryParams(getContext());
        assertEquals(3, queryParams.size());
        assertTrue(queryParams.containsKey(WebLinkTypeRequestFactory.PARAM_DENSITY));
        assertTrue(queryParams.containsKey(WebLinkTypeRequestFactory.PARAM_HEIGHT));
        assertTrue(queryParams.containsKey(WebLinkTypeRequestFactory.PARAM_WIDTH));

        assertEquals(DeviceUtil.getDeviceDensityString(getContext()), queryParams
                .get(LocationRequestFactory.PARAM_DENSITY));

        assertEquals(WebLinkTypeRequestFactory.DEFAULT_ICON_HEIGHT_DIP, queryParams
                .get(WebLinkTypeRequestFactory.PARAM_HEIGHT));
        assertEquals(WebLinkTypeRequestFactory.DEFAULT_ICON_WIDTH_DIP, queryParams
                .get(WebLinkTypeRequestFactory.PARAM_WIDTH));

        assertEquals(0, request.getBodyLength(getContext()));
        final URL url = request.getUrl(getContext());
        assertNotNull(url);
        // Make sure we hit the proper API version and url.
        assertEquals("/v15/web_link_types/1/image", url.getPath());
    }
}
