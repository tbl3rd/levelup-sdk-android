package com.scvngr.levelup.core.net;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import java.util.Locale;

/**
 * Tests {@link LevelUpV13RequestWithCurrentUser}.
 */
public final class LevelUpV13RequestWithCurrentUserTest extends SupportAndroidTestCase {

    /**
     * Tests the parceling of {@link LevelUpV13RequestWithCurrentUser}.
     */
    @SmallTest
    public void testParcel() throws BadRequestException {
        final LevelUpV13RequestWithCurrentUser request =
                new LevelUpV13RequestWithCurrentUser(mContext, HttpMethod.GET,
                        "test/%d", null, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        final Parcel out = Parcel.obtain();
        request.writeToParcel(out, 0);
        out.setDataPosition(0);
        final LevelUpV13RequestWithCurrentUser parceledRequest =
                LevelUpV13RequestWithCurrentUser.CREATOR.createFromParcel(out);

        assertEquals(request, parceledRequest);
        assertFalse(request == parceledRequest);
        assertEquals(request.getRequestHeaders(getContext()).size(), parceledRequest
                .getRequestHeaders(getContext()).size());

        // Make sure the access token is appended
        assertEquals(1, parceledRequest.getQueryParams(mContext).size());
        assertEquals(request.getUrl(mContext), parceledRequest.getUrl(mContext));
        assertEquals(request.getUrlString(mContext), parceledRequest.getUrlString(mContext));

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s.json", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority),
                        LevelUpV13Request.DEFAULT_API_VERSION,
                        String.format(Locale.US, "test/%d", 1)); //$NON-NLS-1$
        assertEquals(url, request.getUrlString(mContext));
        assertEquals(url, parceledRequest.getUrlString(mContext));
    }

    /**
     * Tests that
     * {@link LevelUpV13RequestWithCurrentUser#getUrlString(android.content.Context)} doesn't
     * throw an exception with an invalid format string.
     */
    @SmallTest
    public void testGetUrlString_invalidFormatString() throws BadRequestException {
        final String endpoint = "test"; //$NON-NLS-1$
        final LevelUpV13RequestWithCurrentUser request =
                new LevelUpV13RequestWithCurrentUser(mContext, HttpMethod.GET, endpoint,
                        null, null, new MockAccessTokenRetriever());

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s.json", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority),
                        LevelUpV13Request.DEFAULT_API_VERSION, endpoint);
        assertEquals(url, request.getUrlString(mContext));
    }

    /**
     * Tests that
     * {@link LevelUpV13RequestWithCurrentUser#getUrlString(android.content.Context)} formats
     * the current user id properly.
     */
    @SmallTest
    public void testGetUrlString_valid() throws BadRequestException {
        final String endpoint = "test/%d"; //$NON-NLS-1$
        final LevelUpV13RequestWithCurrentUser request =
                new LevelUpV13RequestWithCurrentUser(mContext, HttpMethod.GET, endpoint,
                        null, null, new MockAccessTokenRetriever());

        final String url =
                String.format(
                        Locale.US,
                        "%s://%s/%s/%s.json", getContext().getString(R.string.levelup_api_scheme), //$NON-NLS-1$
                        getContext().getString(R.string.levelup_api_authority),
                        LevelUpV13Request.DEFAULT_API_VERSION,
                        String.format(Locale.US, endpoint, 1));
        assertEquals(url, request.getUrlString(mContext));
    }

    /**
     * Tests that
     * {@link LevelUpV13RequestWithCurrentUser#getUrlString(android.content.Context)} throws
     * a {@link BadRequestException} if the user is not logged in.
     */
    @SmallTest
    public void testGetUrlString_exceptionThrowing() {
        final String endpoint = "test/%d"; //$NON-NLS-1$
        final LevelUpV13RequestWithCurrentUser request =
                new LevelUpV13RequestWithCurrentUser(mContext, HttpMethod.GET, endpoint,
                        null, null, null);

        try {
            request.getUrlString(mContext);
            fail("should throw BadRequestException"); //$NON-NLS-1$
        } catch (final BadRequestException e) {
            // Expected exception.
        }
    }

    /**
     * Tests {@link LevelUpV13RequestWithCurrentUser#getQueryParams} to ensure the user's access
     * token is appended to the params.
     *
     * @throws Exception if {@link java.net.URLEncoder#encode(String, String)} throws
     */
    @SmallTest
    public void testGetQueryParams_appendsAccessToken() throws Exception {
        final LevelUpV13RequestWithCurrentUser request =
                new LevelUpV13RequestWithCurrentUser(mContext, HttpMethod.GET,
                        "test", null, null, new MockAccessTokenRetriever()); //$NON-NLS-1$

        assertTrue(request.getQueryParams(mContext).containsKey(
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
        assertEquals("test_access_token", request.getQueryParams(mContext).get( //$NON-NLS-1$
                LevelUpV13Request.PARAM_ACCESS_TOKEN));
    }
}
