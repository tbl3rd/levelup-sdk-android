package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.InterstitialJsonFactory;
import com.scvngr.levelup.core.model.factory.json.InterstitialJsonFactory.JsonKeys;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link Interstitial}.
 */
@Immutable
public final class InterstitialFixture {

    /**
     * Test value for image URL.
     */
    public static final String TEST_IMAGE_URL = "http://www.example.com/v12/images"; //$NON-NLS-1$

    /**
     * Test value for the url of {@link com.scvngr.levelup.core.model.Interstitial.UrlAction} type
     * interstitials created with this class.
     */
    public static final String TEST_WEB_URL = "http://www.example.com"; //$NON-NLS-1$

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the Interstitial required fields.
     * @throws JSONException if we failed to construct the JSONObject.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() throws JSONException {
        final JSONObject object = new JSONObject();
        object.put(InterstitialJsonFactory.JsonKeys.CALLOUT_TEXT, "callout_text"); //$NON-NLS-1$
        object.put(InterstitialJsonFactory.JsonKeys.DESCRIPTION_HTML, "description_html"); //$NON-NLS-1$
        object.put(InterstitialJsonFactory.JsonKeys.IMAGE_URL, TEST_IMAGE_URL);
        object.put(InterstitialJsonFactory.JsonKeys.TITLE, "title"); //$NON-NLS-1$
        object.put(InterstitialJsonFactory.JsonKeys.TYPE, "type"); //$NON-NLS-1$
        return object;
    }

    /**
     * Gets a valid base model.
     *
     * @return a {@link Interstitial} with the required fields.
     */
    @NonNull
    public static Interstitial getMinimalModel() {
        try {
            return new InterstitialJsonFactory().from(getMinimalJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid full JSON object with the claim action.
     *
     * @return a {@link JSONObject} with all the Interstitial fields.
     * @throws JSONException if we failed to construct the JSONObject.
     */
    @NonNull
    public static JSONObject getClaimActionJsonObject() throws JSONException {
        final JSONObject object = getMinimalJsonObject();
        object.put(InterstitialJsonFactory.JsonKeys.TYPE, Interstitial.TYPE_CLAIM);
        final JSONObject action = new JSONObject();
        action.put(InterstitialJsonFactory.ClaimActionJsonKeys.CODE, "code"); //$NON-NLS-1$
        object.put(JsonKeys.ACTION, action);
        return object;
    }

    /**
     * Gets a valid claim action model.
     *
     * @return a {@link Interstitial} with the required fields to be a claim interstitial.
     */
    @NonNull
    public static Interstitial getClaimActionModel() {
        try {
            return new InterstitialJsonFactory().from(getClaimActionJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid full JSON object with no action.
     *
     * @return a {@link JSONObject} with all the Interstitial fields.
     * @throws JSONException if we failed to construct the JSONObject.
     */
    @NonNull
    public static JSONObject getNoActionJsonObject() throws JSONException {
        final JSONObject object = getMinimalJsonObject();
        object.put(InterstitialJsonFactory.JsonKeys.TYPE, Interstitial.TYPE_NO_ACTION);
        return object;
    }

    /**
     * Gets a valid no action model.
     *
     * @return a {@link Interstitial} with the required fields to be a no action interstitial.
     */
    @NonNull
    public static Interstitial getNoActionModel() {
        try {
            return new InterstitialJsonFactory().from(getNoActionJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid full JSON object with the share action.
     *
     * @return a {@link JSONObject} with all the Interstitial fields.
     * @throws JSONException if we failed to construct the JSONObject.
     */
    @NonNull
    public static JSONObject getShareActionJsonObject() throws JSONException {
        final JSONObject object = getMinimalJsonObject();
        object.put(InterstitialJsonFactory.JsonKeys.TYPE, Interstitial.TYPE_SHARE);
        final JSONObject action = new JSONObject();
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.MESSAGE_FOR_EMAIL_BODY,
                "message_for_email_body"); //$NON-NLS-1$
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.MESSAGE_FOR_EMAIL_SUBJECT,
                "message_for_email_subject"); //$NON-NLS-1$
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.MESSAGE_FOR_FACEBOOK,
                "message_for_facebook"); //$NON-NLS-1$
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.MESSAGE_FOR_TWITTER,
                "message_for_twitter"); //$NON-NLS-1$
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.SHARE_URL_EMAIL, "share_url_email"); //$NON-NLS-1$
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.SHARE_URL_FACEBOOK,
                "share_url_facebook"); //$NON-NLS-1$
        action.put(InterstitialJsonFactory.ShareActionJsonKeys.SHARE_URL_TWITTER,
                "share_url_twitter"); //$NON-NLS-1$
        object.put(JsonKeys.ACTION, action);
        return object;
    }

    /**
     * Gets a valid share action model.
     *
     * @return a {@link Interstitial} with the required fields to be a share action interstitial.
     */
    @NonNull
    public static Interstitial getShareActionModel() {
        try {
            return new InterstitialJsonFactory().from(getShareActionJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets a valid full JSON object with the URL action.
     *
     * @return a {@link JSONObject} with all the Interstitial fields.
     * @throws JSONException if we failed to construct the JSONObject.
     */
    @NonNull
    public static JSONObject getUrlActionJsonObject() throws JSONException {
        final JSONObject object = getMinimalJsonObject();
        object.put(InterstitialJsonFactory.JsonKeys.TYPE, Interstitial.TYPE_URL);
        final JSONObject action = new JSONObject();
        action.put(InterstitialJsonFactory.UrlActionJsonKeys.URL, TEST_WEB_URL);
        object.put(JsonKeys.ACTION, action);
        return object;
    }

    /**
     * Gets a valid URL action model.
     *
     * @return a {@link Interstitial} with the required fields to be a URL action interstitial.
     */
    @NonNull
    public static Interstitial getUrlActionModel() {
        try {
            return new InterstitialJsonFactory().from(getUrlActionJsonObject());
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private InterstitialFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
