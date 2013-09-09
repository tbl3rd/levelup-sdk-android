package com.scvngr.levelup.core.service;

import android.content.Intent;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.LevelUpStatus;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpConnection;
import com.scvngr.levelup.core.net.LevelUpConnectionHelper;
import com.scvngr.levelup.core.net.LevelUpRequest;

import org.json.JSONObject;

/**
 * Tests {@link SilentNetworkRequestService}.
 */
public final class SilentNetworkRequestServiceTest extends AndroidTestCase {

    /**
     * Tests {@link SilentNetworkRequestService#performRequest(android.content.Context, Intent)}
     * with no request passed.
     */
    @SmallTest
    public void testSendRequest_withNullRequest() {
        final Intent intent = new Intent(getContext(), SilentNetworkRequestService.class);

        /**
         * This test passes if no error is thrown. Errors could be thrown if: A Network connection
         * occurs (see LevelUpConnection#setNetworkEnabled(boolean)). Or the service tries
         * an operation on a null request.
         */
        final SilentNetworkRequestService service = new SilentNetworkRequestService();
        service.performRequest(getContext(), intent);
    }

    /**
     * Tests {@link SilentNetworkRequestService#performRequest(android.content.Context, Intent)}
     * with a valid request passed.
     *
     * @throws BadRequestException on bad request
     */
    @SmallTest
    public void testSendRequest_withValidRequest() throws BadRequestException {
        final LevelUpRequest requestToSend =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14,
                        "test", null, (JSONObject) null); //$NON-NLS-1$
        final Intent intent = new Intent(getContext(), SilentNetworkRequestService.class);
        intent.putExtra(SilentNetworkRequestService.EXTRA_PARCELABLE_REQUEST, requestToSend);
        final LevelUpConnection connection =
                LevelUpConnectionHelper.setNextResponse(getContext(), "", LevelUpStatus.OK); //$NON-NLS-1$

        final SilentNetworkRequestService service = new SilentNetworkRequestService();
        service.performRequest(getContext(), intent);

        final AbstractRequest requestSent =
                LevelUpConnectionHelper.getLastRequest(requestToSend.getUrl(getContext())
                        .toString(), connection);
        assertNotNull(requestSent);
        assertEquals(requestToSend, requestSent);
    }
}
