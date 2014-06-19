/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.DeviceUtil;

import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.CampaignRequestFactory}.
 */
public final class CampaignRequestFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final CampaignRequestFactory factory = new CampaignRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildGetCampaignRequest() throws BadRequestException {
        final AbstractRequest request =
                new CampaignRequestFactory(getContext(), new MockAccessTokenRetriever())
                        .buildGetCampaignRequest(1);
        assertNotNull(request);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue("hits campaigns/<id> endpoint", request.getUrl(mContext).getPath() //$NON-NLS-1$
                .endsWith(String.format(Locale.US, "v14/campaigns/%d", 1))); //$NON-NLS-1$
    }

    @SmallTest
    public void testBuildGetCampaignImageRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new CampaignRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildGetCampaignImageRequest(getContext(),
                        1);

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
        assertEquals("/v14/campaigns/1/image", url.getPath()); //$NON-NLS-1$
    }

    @SmallTest
    public void testBuildGetCampaignMerchantsRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new CampaignRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildGetCampaignMerchantsRequest(1);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getBodyLength(getContext()));
        final URL url = request.getUrl(getContext());
        assertNotNull(url);
        // Make sure we hit the proper API version and url.
        assertEquals("/v14/campaigns/1/merchants", url.getPath()); //$NON-NLS-1$
    }
}
