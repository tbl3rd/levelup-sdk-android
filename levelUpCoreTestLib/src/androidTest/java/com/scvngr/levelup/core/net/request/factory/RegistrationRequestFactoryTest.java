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

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Tests {@link RegistrationRequestFactory}.
 */
public final class RegistrationRequestFactoryTest extends SupportAndroidTestCase {

    @NonNull
    private static final String TEST_EMAIL = "test@example.com";

    /**
     * Test {@link RegistrationRequestFactory#buildRegistrationRequest}.
     *
     * @throws AbstractRequest.BadRequestException from {@link AbstractRequest#getUrl}
     */
    @SmallTest
    public void testBuildRegistrationRequest() throws AbstractRequest.BadRequestException {
        final Context context = NullUtils.nonNullContract(getContext());
        final LevelUpRequest request =
                (LevelUpRequest) new RegistrationRequestFactory(context)
                        .buildRegistrationRequest(TEST_EMAIL);

        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getBodyLength(context));
        assertNull(request.getAccessToken(context));

        final String path = request.getUrl(context).getPath();

        assertTrue(path.contains(LevelUpRequest.API_VERSION_CODE_V15));
        assertTrue(path.contains(RegistrationRequestFactory.ENDPOINT_REGISTRATION));

        final Uri.Builder builder = new Uri.Builder();

        builder.appendQueryParameter(RequestUtils.PARAM_API_KEY, RequestUtils.getApiKey(context));
        builder.appendQueryParameter(RegistrationRequestFactory.PARAM_EMAIL, TEST_EMAIL);

        assertEquals(NullUtils.nonNullContract(builder.build()).getEncodedQuery(),
                request.getUrl(context).getQuery());
    }
}
