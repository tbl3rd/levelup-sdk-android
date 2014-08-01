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
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Interstitial;
import com.scvngr.levelup.core.model.Interstitial.ClaimAction;
import com.scvngr.levelup.core.model.Interstitial.FeedbackAction;
import com.scvngr.levelup.core.model.Interstitial.InterstitialAction;
import com.scvngr.levelup.core.model.Interstitial.ShareAction;
import com.scvngr.levelup.core.model.Interstitial.UrlAction;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for parsing {@link Interstitial}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class InterstitialJsonFactory extends AbstractJsonModelFactory<Interstitial> {

    /**
     * Constructor...
     */
    public InterstitialJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected Interstitial createFrom(@NonNull final JSONObject json) throws JSONException {
        final JsonModelHelper mh = new JsonModelHelper(json);
        final String calloutText = mh.getString(JsonKeys.CALLOUT_TEXT);
        final String descriptionHtml = mh.getString(JsonKeys.DESCRIPTION_HTML);
        final String imageUrl = mh.getString(JsonKeys.IMAGE_URL);
        final String title = mh.getString(JsonKeys.TITLE);
        final String type = mh.getString(JsonKeys.TYPE);
        final InterstitialAction action = parseAction(mh, type);

        return new Interstitial(action, calloutText, descriptionHtml, imageUrl, title, type);
    }

    /**
     * Parse the action from the JSON representation.
     *
     * @param mh the {@link JsonModelHelper} to use to help parse the object.
     * @param type the type of the interstitial.
     * @return the {@link InterstitialAction} that was parsed or null if no_action or unrecognized
     *         action was returned from the web service.
     * @throws JSONException if parsing fails.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @Nullable
    /* package */static InterstitialAction parseAction(@NonNull final JsonModelHelper mh,
            @NonNull final String type) throws JSONException {
        InterstitialAction action = null;

        if (mh.has(JsonKeys.ACTION)) {
            final JsonModelHelper actionHelper =
                    new JsonModelHelper(mh.getJSONObject(JsonKeys.ACTION));

            if (Interstitial.TYPE_CLAIM.equals(type)) {
                action = new ClaimAction(actionHelper.getString(ClaimActionJsonKeys.CODE));
            } else if (Interstitial.TYPE_FEEDBACK.equals(type)) {
                action = new FeedbackAction(
                        actionHelper.getString(FeedbackActionJsonKeys.QUESTION_TEXT));
            } else if (Interstitial.TYPE_SHARE.equals(type)) {
                action = new ShareAction(
                        actionHelper.getString(ShareActionJsonKeys.MESSAGE_FOR_EMAIL_BODY),
                        actionHelper.getString(ShareActionJsonKeys.MESSAGE_FOR_EMAIL_SUBJECT),
                        actionHelper.getString(ShareActionJsonKeys.MESSAGE_FOR_FACEBOOK),
                        actionHelper.getString(ShareActionJsonKeys.MESSAGE_FOR_TWITTER),
                        actionHelper.getString(ShareActionJsonKeys.SHARE_URL_EMAIL),
                        actionHelper.getString(ShareActionJsonKeys.SHARE_URL_FACEBOOK),
                        actionHelper.getString(ShareActionJsonKeys.SHARE_URL_TWITTER));
            } else if (Interstitial.TYPE_URL.equals(type)) {
                action = new UrlAction(actionHelper.getString(UrlActionJsonKeys.URL));
            }
        }

        return action;
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "interstitial";

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String ACTION = "action";

        @JsonValueType(JsonType.STRING)
        public static final String CALLOUT_TEXT = "callout_text";

        @JsonValueType(JsonType.STRING)
        public static final String DESCRIPTION_HTML = "description_html";

        @JsonValueType(JsonType.STRING)
        public static final String IMAGE_URL = "image_url";

        @JsonValueType(JsonType.STRING)
        public static final String TITLE = "title";

        @JsonValueType(JsonType.STRING)
        public static final String TYPE = "type";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }

    /**
     * All of the keys in the JSON representation of the ClaimAction model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class ClaimActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String CODE = "code";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private ClaimActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }

    /**
     * All of the keys in the JSON representation of the {@link FeedbackAction} model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class FeedbackActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String QUESTION_TEXT = "question_text";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private FeedbackActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }

    /**
     * All of the keys in the JSON representation of the ShareAction model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class ShareActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_EMAIL_BODY = "message_for_email_body";

        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_EMAIL_SUBJECT = "message_for_email_subject";

        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_FACEBOOK = "message_for_facebook";

        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_TWITTER = "message_for_twitter";

        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_EMAIL = "share_url_email";

        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_FACEBOOK = "share_url_facebook";

        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_TWITTER = "share_url_twitter";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private ShareActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }

    /**
     * All of the keys in the JSON representation of the UrlAction model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class UrlActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String URL = "url";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private UrlActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }
}
