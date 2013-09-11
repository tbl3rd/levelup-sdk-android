package com.scvngr.levelup.core.model.factory.json;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Interstitial;
import com.scvngr.levelup.core.model.Interstitial.ClaimAction;
import com.scvngr.levelup.core.model.Interstitial.InterstitialAction;
import com.scvngr.levelup.core.model.Interstitial.ShareAction;
import com.scvngr.levelup.core.model.Interstitial.UrlAction;

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
        final String descriptionHtml = mh.getString(JsonKeys.DESCRIPTION_HTML);
        final String imageUrl = mh.getString(JsonKeys.IMAGE_URL);
        final String title = mh.getString(JsonKeys.TITLE);
        final String type = mh.getString(JsonKeys.TYPE);
        final InterstitialAction action = parseAction(mh, type);

        return new Interstitial(action, descriptionHtml, imageUrl, title, type);
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
        final InterstitialAction action = null;

        if (mh.has(JsonKeys.ACTION)) {
            final JsonModelHelper actionHelper =
                    new JsonModelHelper(mh.getJSONObject(JsonKeys.ACTION));

            if (Interstitial.TYPE_CLAIM.equals(type)) {
                return new ClaimAction(actionHelper.getString(ClaimActionJsonKeys.CODE));
            } else if (Interstitial.TYPE_SHARE.equals(type)) {
                return new ShareAction(actionHelper
                        .getString(ShareActionJsonKeys.MESSAGE_FOR_EMAIL_BODY), actionHelper
                        .getString(ShareActionJsonKeys.MESSAGE_FOR_EMAIL_SUBJECT), actionHelper
                        .getString(ShareActionJsonKeys.MESSAGE_FOR_FACEBOOK), actionHelper
                        .getString(ShareActionJsonKeys.MESSAGE_FOR_TWITTER), actionHelper
                        .getString(ShareActionJsonKeys.SHARE_URL_EMAIL), actionHelper
                        .getString(ShareActionJsonKeys.SHARE_URL_FACEBOOK), actionHelper
                        .getString(ShareActionJsonKeys.SHARE_URL_TWITTER));
            } else if (Interstitial.TYPE_URL.equals(type)) {
                return new UrlAction(actionHelper.getString(UrlActionJsonKeys.URL));
            } else {
                return null;
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
        public static final String MODEL_ROOT = "interstitial"; //$NON-NLS-1$

        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String ACTION = "action"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String DESCRIPTION_HTML = "description_html"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String IMAGE_URL = "image_url"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String TITLE = "title"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String TYPE = "type"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }

    /**
     * All of the keys in the JSON representation of the ClaimAction model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class ClaimActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String CODE = "code"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private ClaimActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }

    /**
     * All of the keys in the JSON representation of the ShareAction model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class ShareActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_EMAIL_BODY = "message_for_email_body"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_EMAIL_SUBJECT = "message_for_email_subject"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_FACEBOOK = "message_for_facebook"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String MESSAGE_FOR_TWITTER = "message_for_twitter"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_EMAIL = "share_url_email"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_FACEBOOK = "share_url_facebook"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String SHARE_URL_TWITTER = "share_url_twitter"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private ShareActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }

    /**
     * All of the keys in the JSON representation of the UrlAction model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class UrlActionJsonKeys {
        @JsonValueType(JsonType.STRING)
        public static final String URL = "url"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private UrlActionJsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }
}
