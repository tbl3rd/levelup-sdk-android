/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
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
 * Tests {@link InterstitialJsonFactory}.
 */
public final class InterstitialJsonFactoryTest extends SupportAndroidTestCase {
    @NonNull
    private final InterstitialJsonFactory mFactory = new InterstitialJsonFactory();

    @SmallTest
    public void testJsonParse_claimAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getClaimActionJsonObject());
        assertEquals("claim", interstitial.getType()); //$NON-NLS-1$
        assertTrue(interstitial.getAction() instanceof ClaimAction);
    }

    @SmallTest
    public void testJsonParse_shareAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getShareActionJsonObject());
        assertEquals("share", interstitial.getType()); //$NON-NLS-1$
        assertTrue(interstitial.getAction() instanceof ShareAction);
    }

    @SmallTest
    public void testJsonParse_feedbackAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getFeedbackActionJsonObject());
        assertEquals("feedback", interstitial.getType()); //$NON-NLS-1$
        assertTrue(interstitial.getAction() instanceof FeedbackAction);
    }

    @SmallTest
    public void testJsonParse_urlAction() throws JSONException {
        final Interstitial interstitial =
                mFactory.from(InterstitialFixture.getUrlActionJsonObject());
        assertEquals("url", interstitial.getType()); //$NON-NLS-1$
        assertTrue(interstitial.getAction() instanceof UrlAction);
    }

    @SmallTest
    public void testJsonParse_minimal_object() throws JSONException {
        final Interstitial interstitial = mFactory.from(InterstitialFixture.getMinimalJsonObject());
        assertEquals("description_html", interstitial.getDescriptionHtml()); //$NON-NLS-1$
        assertEquals(InterstitialFixture.TEST_IMAGE_URL, interstitial.getImageUrl());
        assertEquals("title", interstitial.getTitle()); //$NON-NLS-1$
        assertEquals("type", interstitial.getType()); //$NON-NLS-1$
        assertNull(interstitial.getAction());
    }

    @SmallTest
    public void testParseAction_noActionKey() throws JSONException {
        final JsonModelHelper helper = new JsonModelHelper(new JSONObject());

        assertNull(InterstitialJsonFactory.parseAction(helper, "type")); //$NON-NLS-1$
    }

    @SmallTest
    public void testParseAction_withShareAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getShareActionJsonObject());
        final ShareAction action =
                (ShareAction) InterstitialJsonFactory.parseAction(mh, Interstitial.TYPE_SHARE);
        assertNotNull(action);
        assertEquals("message_for_email_body", action.getMessageForEmailBody()); //$NON-NLS-1$
        assertEquals("message_for_email_subject", action.getMessageForEmailSubject()); //$NON-NLS-1$
        assertEquals("message_for_facebook", action.getMessageForFacebook()); //$NON-NLS-1$
        assertEquals("message_for_twitter", action.getMessageForTwitter()); //$NON-NLS-1$
        assertEquals("share_url_email", action.getShareUrlEmail()); //$NON-NLS-1$
        assertEquals("share_url_facebook", action.getShareUrlFacebook()); //$NON-NLS-1$
        assertEquals("share_url_twitter", action.getShareUrlTwitter()); //$NON-NLS-1$
    }

    @SmallTest
    public void testParseAction_withFeedbackAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getFeedbackActionJsonObject());

        @NonNull
        final FeedbackAction action =
                NullUtils.nonNullContract((FeedbackAction) InterstitialJsonFactory.parseAction(mh,
                        Interstitial.TYPE_FEEDBACK));
        assertNotNull(action);
        assertEquals("question_prompt", action.getQuestionText()); //$NON-NLS-1$
    }

    @SmallTest
    public void testParseAction_withClaimAction() throws JSONException {
        final JsonModelHelper mh =
                new JsonModelHelper(InterstitialFixture.getClaimActionJsonObject());
        final ClaimAction action =
                (ClaimAction) InterstitialJsonFactory.parseAction(mh, Interstitial.TYPE_CLAIM);
        assertNotNull(action);
        assertEquals("code", action.getCode()); //$NON-NLS-1$
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
