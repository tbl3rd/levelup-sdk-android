/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Campaign;
import com.scvngr.levelup.core.model.CampaignFixture;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.MonetaryValueFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.CampaignJsonFactory}.
 */
public final class CampaignJsonFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        final Campaign campaign =
                new CampaignJsonFactory().from(CampaignFixture.getFullJsonObject(1));
        final MonetaryValue money = MonetaryValueFixture.getFullModel();

        assertTrue(campaign.isAppliesToAllMerchants());
        assertEquals("confirmation_html", campaign.getConfirmationHtml());
        assertEquals(1, campaign.getId());
        assertEquals("message_for_email_body", campaign.getMessageForEmailBody());
        assertEquals("message_for_email_subject", campaign.getMessageForEmailSubject());
        assertEquals("message_for_facebook", campaign.getMessageForFacebook());
        assertEquals("message_for_twitter", campaign.getMessageForTwitter());
        assertEquals("name", campaign.getName());
        assertEquals("<h1>offer_html</h1>", campaign.getOfferHtml());
        assertEquals("share_url_email", campaign.getShareUrlEmail());
        assertEquals("share_url_facebook", campaign.getShareUrlFacebook());
        assertEquals("share_url_twitter", campaign.getShareUrlTwitter());
        assertEquals("sponsor", campaign.getSponsor());
        assertEquals("type", campaign.getType());

        assertEquals(money.getAmount(), campaign.getValue().getAmount());
    }

    @SmallTest
    public void testJsonParse_minimal() throws JSONException {
        final Campaign campaign =
                new CampaignJsonFactory().from(CampaignFixture.getMinimalJsonObject(1));
        final MonetaryValue money = MonetaryValueFixture.getFullModel();

        assertTrue(campaign.isAppliesToAllMerchants());
        assertEquals("confirmation_html", campaign.getConfirmationHtml());
        assertEquals(1, campaign.getId());
        assertEquals("name", campaign.getName());
        assertEquals("<h1>offer_html</h1>", campaign.getOfferHtml());
        assertEquals("sponsor", campaign.getSponsor());
        assertEquals("type", campaign.getType());

        assertEquals(money.getAmount(), campaign.getValue().getAmount());
    }

    @SmallTest
    public void testJsonParse_missingRequiredKeys() {
        final JSONObject json = new JSONObject();
        try {
            new CampaignJsonFactory().from(json);
            fail("should throw JSONException");
        } catch (final JSONException e) {
            // Expected Exception
        }
    }
}
