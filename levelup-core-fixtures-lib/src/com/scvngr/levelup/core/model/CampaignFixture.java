package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.CampaignJsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for creating {@link Campaign}s.
 */
public final class CampaignFixture {

    /**
     * @param webServiceId the web service ID of the {@link Campaign}.
     * @return a full {@link Campaign} model.
     */
    public static final Campaign getFullModel(final int webServiceId) {
        try {
            return new CampaignJsonFactory().from(getFullJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * @param webServiceId the web service ID of the {@link Campaign}.
     * @return a minimal {@link Campaign} model.
     */
    public static final Campaign getMinimalModel(final int webServiceId) {
        try {
            return new CampaignJsonFactory().from(getMinimalJsonObject(webServiceId));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the Campaign required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(1);
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param webServiceId the object's ID on the server.
     * @return a {@link JSONObject} with all the Campaign required fields.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final long webServiceId) {
        final JSONObject object = new JSONObject();
        try {
            object.put(CampaignJsonFactory.JsonKeys.ID, webServiceId);
            object.put(CampaignJsonFactory.JsonKeys.APPLIES_TO_ALL_MERCHANTS, true);
            object.put(CampaignJsonFactory.JsonKeys.CONFIRMATION_HTML, "confirmation_html"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.NAME, "name"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.OFFER_HTML, "<h1>offer_html</h1>"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.SPONSOR, "sponsor"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.TYPE, "type"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.VALUE, MonetaryValueFixture.getFullModel()
                    .getAmount());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
        return object;
    }

    /**
     * Gets a valid full JSON object.
     *
     * @return a {@link JSONObject} with all the Campaign fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(1);
    }

    /**
     * Gets a valid full JSON object with a cohort that does not have a campaign.
     *
     * @return a {@link JSONObject} with all the Campaign fields.
     */
    @NonNull
    public static JSONObject getFullJsonObjectWithBasicCohort() {
        return getFullJsonObject(1);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param webServiceId the object's ID on the server.
     * @return a {@link JSONObject} with all the Campaign fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final long webServiceId) {
        try {
            final JSONObject object = getMinimalJsonObject(webServiceId);
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_EMAIL_BODY,
                    "message_for_email_body"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_EMAIL_SUBJECT,
                    "message_for_email_subject"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_FACEBOOK, "message_for_facebook"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_TWITTER, "message_for_twitter"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.SHAREABLE, true);
            object.put(CampaignJsonFactory.JsonKeys.SHARE_URL_EMAIL, "share_url_email"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.SHARE_URL_FACEBOOK, "share_url_facebook"); //$NON-NLS-1$
            object.put(CampaignJsonFactory.JsonKeys.SHARE_URL_TWITTER, "share_url_twitter"); //$NON-NLS-1$
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }
}
