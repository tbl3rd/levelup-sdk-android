/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.Interstitial;
import com.scvngr.levelup.core.model.InterstitialFixture;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.DeviceUtil;

import java.net.URL;
import java.util.Locale;
import java.util.UUID;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.InterstitialRequestFactory}.
 */
public final class InterstitialRequestFactoryTest extends SupportAndroidTestCase {
    @NonNull
    private static final String TEST_UUID = UUID.randomUUID().toString();

    @SmallTest
    public void testBuildInterstitialForOrderRequest() throws BadRequestException {
        final LevelUpRequest request =
                (LevelUpRequest) new InterstitialRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildInterstitialForOrderRequest(TEST_UUID);

        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getBodyLength(getContext()));

        final URL url = request.getUrl(getContext());
        assertNotNull(url);

        final String expectedUrl =
                String.format(Locale.US, "/v15/orders/%s/interstitial", TEST_UUID);
        assertEquals(expectedUrl, url.getPath());

        assertTrue(
                "Request is authenticated",
                request.getRequestHeaders(getContext()).containsKey(
                        LevelUpRequest.HEADER_AUTHORIZATION));
    }

    @SmallTest
    public void testBuildInterstitialImageRequest() throws BadRequestException {
        final Interstitial interstitial = InterstitialFixture.getClaimActionModel();
        final LevelUpRequest request =
                (LevelUpRequest) new InterstitialRequestFactory(getContext(), null)
                        .buildInterstitialImageRequest(interstitial);

        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getBodyLength(getContext()));

        final URL url = request.getUrl(getContext());
        assertNotNull(url);

        final String fullUrl = url.toString();

        /*
         * The base URL (everything but the query string) should be equal to what was in the
         * interstitial.
         */
        final String baseUrl = fullUrl.substring(0, fullUrl.length() - url.getQuery().length() - 1);
        assertEquals(interstitial.getImageUrl(), baseUrl);

        // The query string should have the density, width, and height.
        // Order matters here, the keys get alphabetized in AbstractRequest.
        final Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(AbstractRequestFactory.PARAM_DENSITY, DeviceUtil
                .getDeviceDensityString(getContext()));
        builder.appendQueryParameter(AbstractRequestFactory.PARAM_HEIGHT,
                AbstractRequestFactory.DEFAULT_HEIGHT);
        builder.appendQueryParameter(AbstractRequestFactory.PARAM_WIDTH,
                AbstractRequestFactory.DEFAULT_WIDTH);
        assertEquals(builder.build().getQuery(), url.getQuery());
    }
}
