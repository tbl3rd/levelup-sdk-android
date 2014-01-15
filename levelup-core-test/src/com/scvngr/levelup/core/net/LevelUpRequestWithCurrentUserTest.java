/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link LevelUpRequestWithCurrentUser}.
 */
public final class LevelUpRequestWithCurrentUserTest extends SupportAndroidTestCase {

    private static final String TEST_API_VERSION = "v13"; //$NON-NLS-1$

    /**
     * Tests the parceling of {@link LevelUpRequestWithCurrentUser}.
     */
    @SmallTest
    public void testParcel() throws BadRequestException {
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(mContext, HttpMethod.GET,
                        TEST_API_VERSION,
                        "test/%d", null, (JSONObject) null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpRequestWithCurrentUser parceledRequest =
                LevelUpRequestWithCurrentUser.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertFalse(request == parceledRequest);
        final Map<String, String> headers = request.getRequestHeaders(getContext());
        assertEquals(headers.size(), parceledRequest.getRequestHeaders(getContext()).size());
        assertTrue(headers.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority), TEST_API_VERSION,
                        String.format(Locale.US, "test/%d", 1)); //$NON-NLS-1$
        assertEquals(url, request.getUrlString(mContext));
        assertEquals(url, parceledRequest.getUrlString(mContext));
    }

    /**
     * Tests that
     * {@link LevelUpRequestWithCurrentUser#getUrlString(android.content.Context)}
     * doesn't throw an exception with an invalid format string.
     */
    @SmallTest
    public void testGetUrlString_invalidFormatString() throws BadRequestException {
        final String endpoint = "test"; //$NON-NLS-1$
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(mContext, HttpMethod.GET,
                        TEST_API_VERSION, endpoint, null, (JSONObject) null,
                        new MockAccessTokenRetriever());

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority), TEST_API_VERSION,
                        endpoint);
        assertEquals(url, request.getUrlString(mContext));
    }

    /**
     * Tests that
     * {@link LevelUpRequestWithCurrentUser#getUrlString(android.content.Context)}
     * formats the current user id properly.
     */
    @SmallTest
    public void testGetUrlString_valid() throws BadRequestException {
        final String endpoint = "test/%d"; //$NON-NLS-1$
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(mContext, HttpMethod.GET,
                        TEST_API_VERSION, endpoint, null, (JSONObject) null,
                        new MockAccessTokenRetriever());

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority), TEST_API_VERSION,
                        String.format(Locale.US, endpoint, 1));
        assertEquals(url, request.getUrlString(mContext));
    }

    /**
     * Tests that
     * {@link LevelUpRequestWithCurrentUser#getUrlString(android.content.Context)}
     * throws a {@link BadRequestException} if the user is not logged in.
     */
    @SmallTest
    public void testGetUrlString_exceptionThrowing() {
        final String endpoint = "test/%d"; //$NON-NLS-1$
        final LevelUpRequestWithCurrentUser request =
                new LevelUpRequestWithCurrentUser(mContext, HttpMethod.GET,
                        TEST_API_VERSION, endpoint, null, (JSONObject) null, null);

        try {
            request.getUrlString(mContext);
            fail("should throw BadRequestException"); //$NON-NLS-1$
        } catch (final BadRequestException e) {
            // Expected exception.
        }
    }
}
