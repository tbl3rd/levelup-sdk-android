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

import android.location.Location;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.DeviceUtil;

import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.LocationRequestFactory}.
 */
public final class LocationRequestFactoryTest extends SupportAndroidTestCase {

    private static final double TEST_LATITUDE = 42.36275;
    private static final double TEST_LONGITUDE = -71.05987;

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final LocationRequestFactory factory = new LocationRequestFactory(getContext());
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertNull(factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildGetLocationDetailsRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new LocationRequestFactory(getContext())
                        .buildGetLocationDetailsRequest(2);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getQueryParams(getContext()).size());
        assertEquals(0, request.getBodyLength(getContext()));
        final String requestString = request.getUrlString(getContext());
        assertNotNull(requestString);
        assertTrue(requestString.endsWith("locations/2"));
        assertTrue("hits the v14 endpoints", requestString
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }

    @SmallTest
    public void testBuildGetLocationImageRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new LocationRequestFactory(getContext())
                        .buildGetLocationImageRequest(getContext(), 1);

        assertEquals(HttpMethod.GET, request.getMethod());
        final Map<String, String> queryParams = request.getQueryParams(getContext());
        assertEquals(3, queryParams.size());
        assertTrue(queryParams.containsKey(LocationRequestFactory.PARAM_DENSITY));
        assertTrue(queryParams.containsKey(LocationRequestFactory.PARAM_HEIGHT));
        assertTrue(queryParams.containsKey(LocationRequestFactory.PARAM_WIDTH));

        assertEquals(DeviceUtil.getDeviceDensityString(getContext()), queryParams
                .get(LocationRequestFactory.PARAM_DENSITY));

        assertEquals(LocationRequestFactory.DEFAULT_HEIGHT, queryParams
                .get(LocationRequestFactory.PARAM_HEIGHT));
        assertEquals(LocationRequestFactory.DEFAULT_WIDTH, queryParams
                .get(LocationRequestFactory.PARAM_WIDTH));

        assertEquals(0, request.getBodyLength(getContext()));
        final URL url = request.getUrl(getContext());
        assertNotNull(url);
        // Make sure we hit the proper API version and url.
        assertEquals("/v14/locations/1/image", url.getPath());
    }

    @SmallTest
    @Deprecated
    public void testBuildGetAppLocationsListRequest_noLocationPassed() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new LocationRequestFactory(getContext())
                        .buildGetAppLocationsListRequest(2, null);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getQueryParams(getContext()).size());
        assertEquals(0, request.getBodyLength(getContext()));
        final String requestString = request.getUrlString(getContext());
        assertNotNull(requestString);
        assertTrue(requestString.endsWith("apps/2/locations"));
        assertTrue("hits the v14 endpoints", requestString
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }

    @SmallTest
    @Deprecated
    public void testBuildGetAppLocationsListRequest_withLocationPassed() throws BadRequestException {
        final Location location = new Location("test");
        location.setLatitude(TEST_LATITUDE);
        location.setLongitude(TEST_LONGITUDE);
        final LevelUpRequest request =
                (LevelUpRequest) new LocationRequestFactory(getContext())
                        .buildGetAppLocationsListRequest(2, location);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(2, request.getQueryParams(getContext()).size());
        assertEquals(0, request.getBodyLength(getContext()));

        final URL url = request.getUrl(getContext());
        assertNotNull(url);

        final String query = url.getQuery();
        assertNotNull(query);
        final String expectedQuery =
                String.format(Locale.US, "%s=%s&%s=%s", AppLocationListRequestFactory.PARAM_LAT, Double
                        .toString(TEST_LATITUDE), AppLocationListRequestFactory.PARAM_LNG, Double
                        .toString(TEST_LONGITUDE));
        assertEquals(expectedQuery, query);

        assertTrue(url.getPath().endsWith("apps/2/locations"));
        assertTrue("hits the v14 endpoints", url.getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
    }

}
