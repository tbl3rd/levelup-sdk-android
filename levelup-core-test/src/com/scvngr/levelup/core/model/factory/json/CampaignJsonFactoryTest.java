package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Campaign;
import com.scvngr.levelup.core.model.CampaignFixture;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.MonetaryValueFixture;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link CampaignJsonFactory}.
 */
public final class CampaignJsonFactoryTest extends AndroidTestCase {

    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        final Campaign campaign =
                new CampaignJsonFactory().from(CampaignFixture.getFullJsonObject(1));
        final MonetaryValue money = MonetaryValueFixture.getFullModel();

        assertTrue(campaign.isAppliesToAllMerchants());
        assertEquals("confirmation_html", campaign.getConfirmationHtml()); //$NON-NLS-1$
        assertEquals(1, campaign.getId());
        assertEquals("message_for_email_body", campaign.getMessageForEmailBody()); //$NON-NLS-1$
        assertEquals("message_for_email_subject", campaign.getMessageForEmailSubject()); //$NON-NLS-1$
        assertEquals("message_for_facebook", campaign.getMessageForFacebook()); //$NON-NLS-1$
        assertEquals("message_for_twitter", campaign.getMessageForTwitter()); //$NON-NLS-1$
        assertEquals("name", campaign.getName()); //$NON-NLS-1$
        assertEquals("<h1>offer_html</h1>", campaign.getOfferHtml()); //$NON-NLS-1$
        assertEquals("share_url_email", campaign.getShareUrlEmail()); //$NON-NLS-1$
        assertEquals("share_url_facebook", campaign.getShareUrlFacebook()); //$NON-NLS-1$
        assertEquals("share_url_twitter", campaign.getShareUrlTwitter()); //$NON-NLS-1$
        assertEquals("sponsor", campaign.getSponsor()); //$NON-NLS-1$
        assertEquals("type", campaign.getType()); //$NON-NLS-1$

        assertEquals(money.getAmount(), campaign.getValue().getAmount());
    }

    @SmallTest
    public void testJsonParse_minimal() throws JSONException {
        final Campaign campaign =
                new CampaignJsonFactory().from(CampaignFixture.getMinimalJsonObject(1));
        final MonetaryValue money = MonetaryValueFixture.getFullModel();

        assertTrue(campaign.isAppliesToAllMerchants());
        assertEquals("confirmation_html", campaign.getConfirmationHtml()); //$NON-NLS-1$
        assertEquals(1, campaign.getId());
        assertEquals("name", campaign.getName()); //$NON-NLS-1$
        assertEquals("<h1>offer_html</h1>", campaign.getOfferHtml()); //$NON-NLS-1$
        assertEquals("sponsor", campaign.getSponsor()); //$NON-NLS-1$
        assertEquals("type", campaign.getType()); //$NON-NLS-1$

        assertEquals(money.getAmount(), campaign.getValue().getAmount());
    }

    @SmallTest
    public void testJsonParse_missingRequiredKeys() {
        final JSONObject json = new JSONObject();
        try {
            new CampaignJsonFactory().from(json);
            fail("should throw JSONException"); //$NON-NLS-1$
        } catch (final JSONException e) {
            // Expected Exception
        }
    }
}
