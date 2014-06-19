/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpConnection;
import com.scvngr.levelup.core.net.LevelUpConnectionHelper;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.LevelUpResponse;
import com.scvngr.levelup.core.net.LevelUpStatus;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests {@link com.scvngr.levelup.core.service.AbstractNetworkRequestService}.
 */
public final class AbstractNetworkRequestServiceTest extends SupportAndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Clear anything in the LocalBroadcastManager's queue.
        LocalBroadcastManager.getInstance(getContext()).sendBroadcastSync(new Intent());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.service.AbstractNetworkRequestServiceTest.NetworkRequestServiceUnderTest#performRequest(android.content.Context, android.content.Intent)}
     * with no request passed.
     */
    @SmallTest
    public void testSendRequest_withNullRequest() {
        final Intent intent = new Intent(getContext(), NetworkRequestServiceUnderTest.class);

        /**
         * This test passes if no error is thrown. Errors could be thrown if: A Network connection
         * occurs (see LevelUpConnection#setNetworkEnabled(boolean)). Or the service tries
         * an operation on a null request.
         */
        final NetworkRequestServiceUnderTest service = new NetworkRequestServiceUnderTest();
        service.performRequest(getContext(), intent);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.service.AbstractNetworkRequestServiceTest.NetworkRequestServiceUnderTest#performRequest(android.content.Context, android.content.Intent)} with a valid
     * request passed.
     *
     * @throws InterruptedException on interruption
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException on a bad request
     */
    @SmallTest
    public void testSendRequest_withValidRequest() throws InterruptedException, BadRequestException {
        final Context context = getContext();
        final String token = AbstractNetworkRequestService.getToken();
        final TestReceiver receiver =
                new TestReceiver(new LevelUpResponse("", LevelUpStatus.OK), false, token); //$NON-NLS-1$
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter(AbstractNetworkRequestService.ACTION_REQUEST_FINISHED));
        try {

            final LevelUpRequest requestToSend =
                    new LevelUpRequest(getContext(), HttpMethod.GET,
                            LevelUpRequest.API_VERSION_CODE_V14, "test", null, //$NON-NLS-1$
                             null);
            final Intent intent = new Intent(context, NetworkRequestServiceUnderTest.class);
            intent.putExtra(NetworkRequestServiceUnderTest.EXTRA_PARCELABLE_REQUEST, requestToSend);
            intent.putExtra(NetworkRequestServiceUnderTest.EXTRA_STRING_TOKEN, token);

            final LevelUpConnection connection =
                    LevelUpConnectionHelper.setNextResponse(context, "", LevelUpStatus.OK); //$NON-NLS-1$

            final NetworkRequestServiceUnderTest service = new NetworkRequestServiceUnderTest();
            service.performRequest(context, intent);

            final AbstractRequest requestSent =
                    LevelUpConnectionHelper.getLastRequest(
                            requestToSend.getUrl(getContext()).toString(), connection);
            assertNotNull(requestSent);
            assertEquals(requestToSend, requestSent);
            // Make sure Handle response was called.
            assertTrue(service.latch.await(2, TimeUnit.SECONDS));
            // Make sure onRequestFinished was called with the proper data.
            assertTrue(receiver.latch.await(2, TimeUnit.SECONDS));
        } finally {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.service.AbstractNetworkRequestService#getRequest(android.content.Intent)}.
     */
    @SmallTest
    public void testGetRequest() {
        final NetworkRequestServiceUnderTest service = new NetworkRequestServiceUnderTest();

        {
            final Intent intent = new Intent();
            assertNull(service.getRequest(intent));
        }

        {
            final Intent intent = new Intent();
            final LevelUpRequest request =
                    new LevelUpRequest(getContext(), HttpMethod.GET,
                            LevelUpRequest.API_VERSION_CODE_V14, "test", null, //$NON-NLS-1$
                             null);
            intent.putExtra(AbstractNetworkRequestService.EXTRA_PARCELABLE_REQUEST, request);
            assertEquals(request, service.getRequest(intent));
        }
    }

    /**
     * Tests {@link com.scvngr.levelup.core.service.AbstractNetworkRequestService#getToken()}. NOTE: this test is obviously not
     * perfect, but it serves as a small smoke test to make sure that we appear to be generating
     * unique tokens.
     */
    @SmallTest
    public void testGetToken() {
        final int tokenCount = 100;
        final ArrayList<String> tokens = new ArrayList<String>(tokenCount);
        for (int i = 0; i < tokenCount; i++) {
            final String token = AbstractNetworkRequestService.getToken();
            if (tokens.contains(token)) {
                fail("duplicate token created"); //$NON-NLS-1$
            }

            tokens.add(token);
        }
    }

    /**
     * Tests
     * {@link com.scvngr.levelup.core.service.AbstractNetworkRequestService#onRequestFinished(android.content.Context, android.content.Intent, com.scvngr.levelup.core.net.LevelUpResponse, boolean)}
     * .
     *
     * @throws InterruptedException on interruption
     */
    @SmallTest
    public void testOnRequestFinished() throws InterruptedException {
        final Context context = getContext();
        final String token = AbstractNetworkRequestService.getToken();
        final LevelUpResponse response =
                new LevelUpResponse("testing this", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        final TestReceiver receiver = new TestReceiver(response, true, token);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter(AbstractNetworkRequestService.ACTION_REQUEST_FINISHED));
        try {
            final Intent intent = new Intent();
            intent.putExtra(AbstractNetworkRequestService.EXTRA_STRING_TOKEN, token);

            AbstractNetworkRequestService.onRequestFinished(context, intent, response, true);
            assertTrue(receiver.latch.await(2, TimeUnit.SECONDS));
        } finally {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }
    }

    /**
     * Tests
     * {@link com.scvngr.levelup.core.service.AbstractNetworkRequestService#onRequestFinished(android.content.Context, android.content.Intent, com.scvngr.levelup.core.net.LevelUpResponse, boolean)}
     * .
     *
     * @throws InterruptedException on interruption
     */
    @SmallTest
    public void testOnRequestFinished_nullToken() throws InterruptedException {
        final Context context = getContext();
        final LevelUpResponse response =
                new LevelUpResponse("testing this", LevelUpStatus.ERROR_SERVER); //$NON-NLS-1$
        final TestReceiver receiver = new TestReceiver(response, true, null);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter(AbstractNetworkRequestService.ACTION_REQUEST_FINISHED));
        try {
            final Intent intent = new Intent();
            AbstractNetworkRequestService.onRequestFinished(context, intent, response, true);
            assertTrue(receiver.latch.await(2, TimeUnit.SECONDS));
        } finally {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }
    }

    /**
     * A concrete implementation of {@link com.scvngr.levelup.core.service.AbstractNetworkRequestService} which counts calls to
     * {@link #handleResponse(android.content.Context, com.scvngr.levelup.core.net.LevelUpResponse)}.
     */
    private static final class NetworkRequestServiceUnderTest extends AbstractNetworkRequestService {

        final CountDownLatch latch = new CountDownLatch(1);

        @Override
        protected boolean handleResponse(@NonNull final Context context,
                @NonNull final LevelUpResponse response) {
            latch.countDown();
            return false;
        }
    }

    /**
     * BroadcastRecevier for handling broadcasts from the service.
     */
    private final class TestReceiver extends BroadcastReceiver {

        final CountDownLatch latch = new CountDownLatch(1);
        final LevelUpResponse expectedResponse;
        final boolean expectedStatus;
        final String expectedToken;

        private TestReceiver(final LevelUpResponse response, final boolean status,
                final String token) {
            expectedResponse = response;
            expectedStatus = status;
            expectedToken = token;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            assertEquals(AbstractNetworkRequestService.ACTION_REQUEST_FINISHED, intent.getAction());
            assertTrue(intent.hasExtra(AbstractNetworkRequestService.EXTRA_PARCELABLE_RESPONSE));
            final LevelUpResponse response =
                    intent.getParcelableExtra(AbstractNetworkRequestService.EXTRA_PARCELABLE_RESPONSE);
            assertEquals(expectedResponse, response);
            assertEquals(expectedStatus, intent.getBooleanExtra(
                    AbstractNetworkRequestService.EXTRA_BOOLEAN_IS_REQUEST_SUCCESSFUL,
                    !expectedStatus));
            assertEquals(expectedToken,
                    intent.getStringExtra(AbstractNetworkRequestService.EXTRA_STRING_TOKEN));
            latch.countDown();
        }
    }
}
