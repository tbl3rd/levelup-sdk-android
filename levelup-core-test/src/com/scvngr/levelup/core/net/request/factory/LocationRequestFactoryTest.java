/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.DeviceUtil;

import java.net.URL;
import java.util.Map;

/**
 * Tests {@link LocationRequestFactory}.
 */
public final class LocationRequestFactoryTest extends SupportAndroidTestCase {

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
        assertTrue(requestString.endsWith("locations/2")); //$NON-NLS-1$
        assertTrue("hits the v14 endpoints", requestString //$NON-NLS-1$
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
        assertEquals("/v14/locations/1/image", url.getPath()); //$NON-NLS-1$
    }
}
