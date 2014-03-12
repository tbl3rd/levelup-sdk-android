/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;

/**
 * Tests {@link LevelUpRequestWithCurrentUser}.
 */
public final class LevelUpRequestWithCurrentUserTest extends SupportAndroidTestCase {

    @NonNull
    private static final String TEST_API_VERSION = "v14"; //$NON-NLS-1$

    /**
     * Tests the parceling of {@link LevelUpRequestWithCurrentUser}.
     *
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testParcel() throws BadRequestException {
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(getContext(), HttpMethod.GET, TEST_API_VERSION,
                        "test/%d", null, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        ParcelTestUtils.assertParcelableRoundtrips(request);
    }

    /**
     * Tests that {@link LevelUpRequestWithCurrentUser#getUrlString(android.content.Context)}
     * doesn't throw an exception with an invalid format string.
     *
     * @throws BadRequestException on bad request.
     */
    @SmallTest
    public void testGetUrlString_invalidFormatString() throws BadRequestException {
        final String endpoint = "test"; //$NON-NLS-1$
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(getContext(), HttpMethod.GET, TEST_API_VERSION,
                        endpoint, null, null, new MockAccessTokenRetriever());

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority), TEST_API_VERSION,
                        endpoint);
        assertEquals(url, request.getUrlString(getContext()));
    }

    /**
     * Tests that {@link LevelUpRequestWithCurrentUser#getUrlString(android.content.Context)}
     * formats the current user id properly.
     *
     * @throws BadRequestException on bad request.
     */
    @SmallTest
    public void testGetUrlString_valid() throws BadRequestException {
        final String endpoint = "test/%d"; //$NON-NLS-1$
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(getContext(), HttpMethod.GET, TEST_API_VERSION,
                        endpoint, null, null, new MockAccessTokenRetriever());

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority), TEST_API_VERSION,
                        String.format(Locale.US, endpoint, 1));
        assertEquals(url, request.getUrlString(getContext()));
    }

    /**
     * Tests that {@link LevelUpRequestWithCurrentUser#getUrlString(android.content.Context)} throws
     * a {@link BadRequestException} if the user is not logged in.
     */
    @SmallTest
    public void testGetUrlString_exceptionThrowing() {
        final String endpoint = "test/%d"; //$NON-NLS-1$
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(getContext(), HttpMethod.GET, TEST_API_VERSION,
                        endpoint, null, null, new MockAccessTokenRetriever(null));

        try {
            request.getUrlString(getContext());
            fail("should throw BadRequestException"); //$NON-NLS-1$
        } catch (final BadRequestException e) {
            // Expected exception.
        }
    }
}
