package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.PreconditionUtil;

/**
 * Class to build requests to interact with the endpoints that deal with {@link AccessToken}s.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class AccessTokenRequestFactory extends AbstractRequestFactory {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_OUTER_ACCESS_TOKEN = "access_token"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_FACEBOOK_ACCESS_TOKEN = "facebook_access_token"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_PASSWORD = "password"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_USERNAME = "username"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_CLIENT_ID = "client_id"; //$NON-NLS-1$

    /**
     * Constructor.
     *
     * @param context the Application context.
     */
    public AccessTokenRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * Build a request to perform a login with email/password.
     *
     * @param email the user's email.
     * @param password the user's password.
     * @return {@link AbstractRequest} representing a login request.
     */
    @NonNull
    public AbstractRequest buildLoginRequest(@NonNull final String email,
            @NonNull final String password) {
        PreconditionUtil.assertNotNull(email, "email"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(password, "password"); //$NON-NLS-1$

        final JSONObject object = new JSONObject();
        final JSONObject token = new JSONObject();
        try {
            token.put(PARAM_USERNAME, email);
            token.put(PARAM_PASSWORD, password);
            addApiKeyAndDeviceId(getContext(), token);

            object.put(PARAM_OUTER_ACCESS_TOKEN, token);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e); //$NON-NLS-1$
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, "access_tokens", null, object); //$NON-NLS-1$
    }

    /**
     * Build a request to perform a login with Facebook.
     *
     * @param facebookAccessToken the user's Facebook access token.
     * @return {@link AbstractRequest} representing a login request.
     */
    @NonNull
    public AbstractRequest buildFacebookLoginRequest(@NonNull final String facebookAccessToken) {
        PreconditionUtil.assertNotNull(facebookAccessToken, "facebookAccessToken"); //$NON-NLS-1$

        final JSONObject object = new JSONObject();
        final JSONObject token = new JSONObject();
        try {
            token.put(PARAM_FACEBOOK_ACCESS_TOKEN, facebookAccessToken);
            addApiKeyAndDeviceId(getContext(), token);
            object.put(PARAM_OUTER_ACCESS_TOKEN, token);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e); //$NON-NLS-1$
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, "access_tokens", null, object); //$NON-NLS-1$
    }

    /**
     * Helper method to add the ApiKey and DeviceId to the request.
     *
     * @param context the Application context.
     * @param token the {@link JSONObject} to add them to.
     * @throws JSONException if adding to the {@link JSONObject} fails.
     */
    private static final void addApiKeyAndDeviceId(@NonNull final Context context,
            @NonNull final JSONObject token) throws JSONException {
        addApiKeyToRequest(context, token);
        UserRequestFactory.addDeviceIdToRequest(context, token);
    }

    /**
     * Helper method to add the Api Key to the request body.
     *
     * @param context the Application context.
     * @param object the {@link JSONObject} to add the Api Key to.
     * @throws JSONException if adding to the {@link JSONObject} fails.
     */
    public static void addApiKeyToRequest(@NonNull final Context context,
            @NonNull final JSONObject object) throws JSONException {
        object.put(PARAM_CLIENT_ID, getApiKey(context));
    }

    /**
     * Get the Api key for the context passed.
     *
     * @param context the Application context.
     * @return The Api key from resources.
     */
    @NonNull
    public static String getApiKey(@NonNull final Context context) {
        final String apiKey = context.getString(R.string.levelup_api_key);

        if (TextUtils.isEmpty(apiKey)) {
            throw new AssertionError(String.format(Locale.US, "Application must override %s", //$NON-NLS-1$
                    context.getResources().getResourceEntryName(R.string.levelup_api_key)));
        }

        return apiKey;
    }
}
