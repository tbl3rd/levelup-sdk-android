/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Campaign;
import com.scvngr.levelup.core.model.Campaign.CampaignBuilder;
import com.scvngr.levelup.core.model.util.JsonUtils;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link Campaign}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class CampaignJsonFactory extends AbstractJsonModelFactory<Campaign> {

    /**
     * Constructor.
     */
    public CampaignJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected Campaign createFrom(@NonNull final JSONObject json) throws JSONException {
        final CampaignBuilder builder = Campaign.builder();

        builder.appliesToAllMerchants(json.getBoolean(JsonKeys.APPLIES_TO_ALL_MERCHANTS));
        builder.confirmationHtml(json.getString(JsonKeys.CONFIRMATION_HTML));
        builder.id(json.getLong(JsonKeys.ID));
        builder.messageForEmailBody(JsonUtils.optString(json, JsonKeys.MESSAGE_FOR_EMAIL_BODY));
        builder.messageForEmailSubject(JsonUtils
                .optString(json, JsonKeys.MESSAGE_FOR_EMAIL_SUBJECT));
        builder.messageForFacebook(JsonUtils.optString(json, JsonKeys.MESSAGE_FOR_FACEBOOK));
        builder.messageForTwitter(JsonUtils.optString(json, JsonKeys.MESSAGE_FOR_TWITTER));
        builder.name(json.getString(JsonKeys.NAME));
        builder.offerHtml(json.getString(JsonKeys.OFFER_HTML));
        builder.shareable(json.optBoolean(JsonKeys.SHAREABLE, false));
        builder.shareUrlEmail(JsonUtils.optString(json, JsonKeys.SHARE_URL_EMAIL));
        builder.shareUrlFacebook(JsonUtils.optString(json, JsonKeys.SHARE_URL_FACEBOOK));
        builder.shareUrlTwitter(JsonUtils.optString(json, JsonKeys.SHARE_URL_TWITTER));
        builder.sponsor(JsonUtils.optString(json, JsonKeys.SPONSOR));
        builder.type(JsonUtils.optString(json, JsonKeys.TYPE));
        builder.value(JsonUtils.getMonetaryValue(json, JsonKeys.VALUE));

        // Lombok's Builder.build() is an undocumented @NonNull.
        return NullUtils.nonNullContract(builder.build());
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {
        /**
         * The key under which this model can be nested.
         */
        @NonNull
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "campaign";

        @NonNull
        @JsonValueType(JsonType.BOOLEAN)
        public static final String APPLIES_TO_ALL_MERCHANTS = "applies_to_all_merchants";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String CONFIRMATION_HTML = "confirmation_html";

        @NonNull
        @JsonValueType(JsonType.LONG)
        public static final String ID = "id";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_EMAIL_BODY = "message_for_email_body";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_EMAIL_SUBJECT = "message_for_email_subject";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_FACEBOOK = "message_for_facebook";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_TWITTER = "message_for_twitter";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String NAME = "name";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String OFFER_HTML = "offer_html";

        @NonNull
        @JsonValueType(JsonType.BOOLEAN)
        public static final String SHAREABLE = "shareable";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_EMAIL = "share_url_email";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_FACEBOOK = "share_url_facebook";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_TWITTER = "share_url_twitter";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String SPONSOR = "sponsor";

        @NonNull
        @JsonValueType(JsonType.STRING)
        public static final String TYPE = "type";

        @NonNull
        @JsonValueType(JsonType.LONG)
        public static final String VALUE = "value_amount";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }
}
