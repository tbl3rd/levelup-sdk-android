/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Permission;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.JSONObjectRequestBody;
import com.scvngr.levelup.core.net.JsonElementRequestBody;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Class to build requests to interact with the endpoints that deal with
 * {@link com.scvngr.levelup.core.model.AccessToken}s.
 */
@Immutable
@LevelUpApi(contract = Contract.ENTERPRISE)
public final class AccessTokenRequestFactory extends AbstractRequestFactory {

    @NonNull
    /* package */static final String ENDPOINT = "access_tokens";

    @NonNull
    /* package */static final String ENDPOINT_DOWNGRADES = "access_tokens/downgrades";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_OUTER_ACCESS_TOKEN = "access_token";

    @LevelUpApi(contract = Contract.INTERNAL)
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_FACEBOOK_ACCESS_TOKEN = "facebook_access_token";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_PASSWORD = "password";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_USERNAME = "username";

    /* package */static final String PARAM_PERMISSION_KEYNAMES = "permission_keynames";

    /**
     * Constructor.
     *
     * @param context the Application context.
     */
    public AccessTokenRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * Constructor with access token retriever.
     *
     * @param context the Application context.
     * @param retriever the AccessTokenRetriever.
     */
    public AccessTokenRequestFactory(@NonNull final Context context,
            final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to perform a login with email/password.
     *
     * @param email the user's email.
     * @param password the user's password.
     * @return {@link AbstractRequest} representing a login request.
     */
    @NonNull
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public AbstractRequest buildLoginRequest(@NonNull final String email,
            @NonNull final String password) {
        PreconditionUtil.assertNotNull(email, "email");
        PreconditionUtil.assertNotNull(password, "password");

        final JSONObject object = new JSONObject();
        final JSONObject token = new JSONObject();

        try {
            token.put(PARAM_USERNAME, email);
            token.put(PARAM_PASSWORD, password);

            final Context context = getContext();

            RequestUtils.addApiKeyToRequestBody(context, token);
            RequestUtils.addDeviceIdToRequestBody(context, token);

            object.put(PARAM_OUTER_ACCESS_TOKEN, token);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, ENDPOINT, null, new JSONObjectRequestBody(
                        object));
    }

    /**
     * Build a request to perform a login with Facebook.
     *
     * @param facebookAccessToken the user's Facebook access token.
     * @return {@link AbstractRequest} representing a login request.
     */
    @NonNull
    @LevelUpApi(contract = Contract.INTERNAL)
    public AbstractRequest buildFacebookLoginRequest(@NonNull final String facebookAccessToken) {
        PreconditionUtil.assertNotNull(facebookAccessToken, "facebookAccessToken");

        final JSONObject object = new JSONObject();
        final JSONObject token = new JSONObject();

        try {
            token.put(PARAM_FACEBOOK_ACCESS_TOKEN, facebookAccessToken);

            final Context context = getContext();

            RequestUtils.addApiKeyToRequestBody(context, token);
            RequestUtils.addDeviceIdToRequestBody(context, token);

            object.put(PARAM_OUTER_ACCESS_TOKEN, token);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, ENDPOINT, null, new JSONObjectRequestBody(
                        object));
    }

    /**
     * Build a request for a downgraded scoped access token.
     *
     * @param permissions The permissions for the access token you want.
     * @return an {@link AbstractRequest} to retrieve an access token scoped to the provided
     *         permissions.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public AbstractRequest buildDowngradeRequest(@NonNull final Collection<Permission> permissions) {
        final JsonObject body = new JsonObject();
        final JsonObject accessToken = new JsonObject();
        final JsonArray permissionsKeyNames = new JsonArray();

        for (final Permission permission : permissions) {
            permissionsKeyNames.add(new JsonPrimitive(permission.getKeyname()));
        }

        accessToken.add(PARAM_PERMISSION_KEYNAMES, permissionsKeyNames);
        body.add(PARAM_OUTER_ACCESS_TOKEN, accessToken);

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V15, ENDPOINT_DOWNGRADES, null,
                new JsonElementRequestBody(body), getAccessTokenRetriever());
    }

    /**
     * Build a request for a downgraded scoped access token.
     *
     * @param permissionKeyNames The key names for permissions on the access token you want.
     * @return an {@link AbstractRequest} to retrieve an access token scoped to the provided
     *         permissions.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public AbstractRequest buildDowngradeRequest(@NonNull final String... permissionKeyNames) {
        final ArrayList<Permission> permissions =
                new ArrayList<Permission>(permissionKeyNames.length);
        for (final String keyName : permissionKeyNames) {
            permissions.add(new Permission("", NullUtils.nonNullContract(keyName)));
        }
        return buildDowngradeRequest(permissions);
    }

    /**
     * Build a request for a downgraded scoped access token.
     *
     * @param permissions The permissions for the access token you want.
     * @return an {@link AbstractRequest} to retrieve an access token scoped to the provided
     *         permissions.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public AbstractRequest buildDowngradeRequest(@NonNull final Permission... permissions) {
        return buildDowngradeRequest(NullUtils.nonNullContract(Arrays.asList(permissions)));
    }
}
