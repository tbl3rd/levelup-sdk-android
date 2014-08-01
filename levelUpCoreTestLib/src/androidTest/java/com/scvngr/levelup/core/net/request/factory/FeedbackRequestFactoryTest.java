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
import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Feedback;
import com.scvngr.levelup.core.model.FeedbackFixture;
import com.scvngr.levelup.core.model.OrderFixture;
import com.scvngr.levelup.core.model.factory.json.FeedbackJsonFactory;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.JsonObject;

import java.util.Locale;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.FeedbackRequestFactory}.
 */
public final class FeedbackRequestFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testNewPostFeedbackRequest() throws BadRequestException {
        final FeedbackRequestFactory builder =
                new FeedbackRequestFactory(getContext(), new MockAccessTokenRetriever());
        final Feedback expectedFeedback = FeedbackFixture.getFullModel();
        final AbstractRequest request =
                builder.buildFeedbackRequest(OrderFixture.UUID_FIXTURE_1, expectedFeedback);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(
                "hits orders/:id/feedback endpoint", request.getUrl(getContext()).getPath()
                        .contains(
                                String.format(Locale.US,
                                        "orders/%s/feedback", OrderFixture.UUID_FIXTURE_1)));

        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));

        final String body =
                NullUtils.nonNullContract(((LevelUpRequest) request).getBody(getContext()));

        final Feedback actualFeedback = new FeedbackJsonFactory().from(body);

        assertEquals(expectedFeedback.getRating(), actualFeedback.getRating());
        assertEquals(expectedFeedback.getComment(), actualFeedback.getComment());
        assertEquals(expectedFeedback.getQuestionText(), actualFeedback.getQuestionText());
    }

    @SmallTest
    public void testNewPostFeedbackRequest_nullComment() throws BadRequestException {
        final FeedbackRequestFactory builder =
                new FeedbackRequestFactory(getContext(), new MockAccessTokenRetriever());

        final JsonObject feedbackJson = FeedbackFixture.getFullJsonObject();
        feedbackJson.addProperty("comment", (String) null);
        final Feedback expectedFeedback = new FeedbackJsonFactory().from(feedbackJson);

        final AbstractRequest request =
                builder.buildFeedbackRequest(OrderFixture.UUID_FIXTURE_1, expectedFeedback);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(
                "hits orders/:id/feedback endpoint", request.getUrl(getContext()).getPath()
                        .contains(
                                String.format(Locale.US,
                                        "orders/%s/feedback", OrderFixture.UUID_FIXTURE_1)));

        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));

        final String body =
                NullUtils.nonNullContract(((LevelUpRequest) request).getBody(getContext()));

        final Feedback actualFeedback = new FeedbackJsonFactory().from(body);

        assertNull(actualFeedback.getComment());
        assertEquals(expectedFeedback.getQuestionText(), actualFeedback.getQuestionText());
        assertEquals(expectedFeedback.getRating(), actualFeedback.getRating());
    }

    @Override
    @NonNull
    public Context getContext() {
        return NullUtils.nonNullContract(super.getContext());
    }
}
