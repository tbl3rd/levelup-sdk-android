package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
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
                "hits orders/:id/feedback endpoint", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                        .contains(
                                String.format(Locale.US,
                                        "orders/%s/feedback", OrderFixture.UUID_FIXTURE_1))); //$NON-NLS-1$

        @NonNull
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
        feedbackJson.addProperty("comment", (String) null); //$NON-NLS-1$
        final Feedback expectedFeedback = new FeedbackJsonFactory().from(feedbackJson);

        final AbstractRequest request =
                builder.buildFeedbackRequest(OrderFixture.UUID_FIXTURE_1, expectedFeedback);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(
                "hits orders/:id/feedback endpoint", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                        .contains(
                                String.format(Locale.US,
                                        "orders/%s/feedback", OrderFixture.UUID_FIXTURE_1))); //$NON-NLS-1$

        @NonNull
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
