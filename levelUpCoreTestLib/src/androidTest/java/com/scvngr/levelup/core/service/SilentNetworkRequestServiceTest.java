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
package com.scvngr.levelup.core.service;

import android.content.Intent;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpConnection;
import com.scvngr.levelup.core.net.LevelUpConnectionHelper;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.LevelUpStatus;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.service.SilentNetworkRequestService}.
 */
public final class SilentNetworkRequestServiceTest extends SupportAndroidTestCase {

    /**
     * Tests {@link com.scvngr.levelup.core.service.SilentNetworkRequestService#performRequest(android.content.Context, android.content.Intent)}
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
     * Tests {@link com.scvngr.levelup.core.service.SilentNetworkRequestService#performRequest(android.content.Context, android.content.Intent)}
     * with a valid request passed.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException on bad request
     */
    @SmallTest
    public void testSendRequest_withValidRequest() throws BadRequestException {
        final LevelUpRequest requestToSend =
                new LevelUpRequest(getContext(), HttpMethod.GET,
                        LevelUpRequest.API_VERSION_CODE_V14,
                        "test", null, null);
        final Intent intent = new Intent(getContext(), SilentNetworkRequestService.class);
        intent.putExtra(SilentNetworkRequestService.EXTRA_PARCELABLE_REQUEST, requestToSend);
        final LevelUpConnection connection =
                LevelUpConnectionHelper.setNextResponse(getContext(), "", LevelUpStatus.OK);

        final SilentNetworkRequestService service = new SilentNetworkRequestService();
        service.performRequest(getContext(), intent);

        final AbstractRequest requestSent =
                LevelUpConnectionHelper.getLastRequest(requestToSend.getUrl(getContext())
                        .toString(), connection);
        assertNotNull(requestSent);
        assertEquals(requestToSend, requestSent);
    }
}
