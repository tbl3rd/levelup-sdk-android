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
package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Interstitial;
import com.scvngr.levelup.core.model.Interstitial.ClaimAction;
import com.scvngr.levelup.core.model.Interstitial.FeedbackAction;
import com.scvngr.levelup.core.model.Interstitial.ShareAction;
import com.scvngr.levelup.core.model.Interstitial.UrlAction;
import com.scvngr.levelup.core.model.InterstitialFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.InterstitialJsonFactory}.
 */
public final class InterstitialJsonFactoryTest extends SupportAndroidTestCase {
    @NonNull
    private final InterstitialJsonFactory mFactory = new InterstitialJsonFactory();

    @SmallTest
    public void testJsonParse_claimAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getClaimActionJsonObject());
        assertEquals("claim", interstitial.getType());
        assertTrue(interstitial.getAction() instanceof ClaimAction);
    }

    @SmallTest
    public void testJsonParse_shareAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getShareActionJsonObject());
        assertEquals("share", interstitial.getType());
        assertTrue(interstitial.getAction() instanceof ShareAction);
    }

    @SmallTest
    public void testJsonParse_feedbackAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getFeedbackActionJsonObject());
        assertEquals("feedback", interstitial.getType());
        assertTrue(interstitial.getAction() instanceof FeedbackAction);
    }

    @SmallTest
    public void testJsonParse_urlAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getUrlActionJsonObject());
        assertEquals("url", interstitial.getType());
        assertTrue(interstitial.getAction() instanceof UrlAction);
    }

    @SmallTest
    public void testJsonParse_minimal_object() throws JSONException {
        final Interstitial interstitial = mFactory.from(InterstitialFixture.getMinimalJsonObject());
        assertEquals("description_html", interstitial.getDescriptionHtml());
        assertEquals(InterstitialFixture.TEST_IMAGE_URL, interstitial.getImageUrl());
        assertEquals("title", interstitial.getTitle());
        assertEquals("type", interstitial.getType());
        assertNull(interstitial.getAction());
    }

    @SmallTest
    public void testParseAction_noActionKey() throws JSONException {
        final JsonModelHelper helper = new JsonModelHelper(new JSONObject());

        assertNull(InterstitialJsonFactory.parseAction(helper, "type"));
    }

    @SmallTest
    public void testParseAction_withShareAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getShareActionJsonObject());
        final ShareAction action =
                (ShareAction) InterstitialJsonFactory.parseAction(mh, Interstitial.TYPE_SHARE);
        assertNotNull(action);
        assertEquals("message_for_email_body", action.getMessageForEmailBody());
        assertEquals("message_for_email_subject", action.getMessageForEmailSubject());
        assertEquals("message_for_facebook", action.getMessageForFacebook());
        assertEquals("message_for_twitter", action.getMessageForTwitter());
        assertEquals("share_url_email", action.getShareUrlEmail());
        assertEquals("share_url_facebook", action.getShareUrlFacebook());
        assertEquals("share_url_twitter", action.getShareUrlTwitter());
    }

    @SmallTest
    public void testParseAction_withFeedbackAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getFeedbackActionJsonObject());

        final FeedbackAction action =
                NullUtils.nonNullContract((FeedbackAction) InterstitialJsonFactory.parseAction(mh,
                        Interstitial.TYPE_FEEDBACK));
        assertNotNull(action);
        assertEquals("question_prompt", action.getQuestionText());
    }

    @SmallTest
    public void testParseAction_withClaimAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getClaimActionJsonObject());
        final ClaimAction action =
                (ClaimAction) InterstitialJsonFactory.parseAction(mh, Interstitial.TYPE_CLAIM);
        assertNotNull(action);
        assertEquals("code", action.getCode());
    }

    @SmallTest
    public void testParseAction_withUrlAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getUrlActionJsonObject());
        final UrlAction action =
                (UrlAction) InterstitialJsonFactory.parseAction(mh, Interstitial.TYPE_URL);
        assertNotNull(action);
        assertEquals(InterstitialFixture.TEST_WEB_URL, action.getUrl());
    }
}
