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
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

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
    public static Campaign getFullModel(final int webServiceId) {
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
    public static Campaign getMinimalModel(final int webServiceId) {
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
            object.put(CampaignJsonFactory.JsonKeys.CONFIRMATION_HTML, "confirmation_html");
            object.put(CampaignJsonFactory.JsonKeys.NAME, "name");
            object.put(CampaignJsonFactory.JsonKeys.OFFER_HTML, "<h1>offer_html</h1>");
            object.put(CampaignJsonFactory.JsonKeys.SPONSOR, "sponsor");
            object.put(CampaignJsonFactory.JsonKeys.TYPE, "type");
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
                    "message_for_email_body");
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_EMAIL_SUBJECT,
                    "message_for_email_subject");
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_FACEBOOK, "message_for_facebook");
            object.put(CampaignJsonFactory.JsonKeys.MESSAGE_FOR_TWITTER, "message_for_twitter");
            object.put(CampaignJsonFactory.JsonKeys.SHAREABLE, true);
            object.put(CampaignJsonFactory.JsonKeys.SHARE_URL_EMAIL, "share_url_email");
            object.put(CampaignJsonFactory.JsonKeys.SHARE_URL_FACEBOOK, "share_url_facebook");
            object.put(CampaignJsonFactory.JsonKeys.SHARE_URL_TWITTER, "share_url_twitter");
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private CampaignFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
