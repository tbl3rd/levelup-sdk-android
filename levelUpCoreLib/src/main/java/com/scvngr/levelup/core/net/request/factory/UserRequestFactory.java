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
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.RequiresPermission;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.JSONObjectRequestBody;
import com.scvngr.levelup.core.net.JsonElementRequestBody;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

import com.google.gson.JsonObject;

import net.jcip.annotations.Immutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * <p>
 * Class to build requests to interact with the endpoints that deal with
 * {@link com.scvngr.levelup.core.model.User}s.
 * </p>
 * <p>
 * If you're looking to login, see {@link AccessTokenRequestFactory}.
 * </p>
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class UserRequestFactory extends AbstractRequestFactory {

    @LevelUpApi(contract = Contract.INTERNAL)
    @NonNull
    private static final String FACEBOOK_CONNECTION_ENDPOINT = "facebook_connection";

    @LevelUpApi(contract = Contract.INTERNAL)
    @NonNull
    private static final String USERS_ENDPOINT = "users";

    /**
     * Outer parameter name for user parameters.
     */
    @NonNull
    public static final String OUTER_PARAM_USER = "user";

    /**
     * Outer parameter name for permission keynames parameters.
     */
    @NonNull
    public static final String OUTER_PARAM_PERMISSION_KEYNAMES = "permission_keynames";

    /**
     * User Parameter for longitude, represented as a floating point number.
     */
    @NonNull
    public static final String PARAM_LONGITUDE = "lng";

    /**
     * User Parameter for latitude, represented as a floating point number.
     */
    @NonNull
    public static final String PARAM_LATITUDE = "lat";

    /**
     * User Parameter for password.
     */
    @NonNull
    public static final String PARAM_PASSWORD = "password";

    /**
     * User Parameter for new password.
     */
    @NonNull
    public static final String PARAM_NEW_PASSWORD = "new_password";

    /**
     * User Parameter for new password confirmation.
     */
    @NonNull
    public static final String PARAM_NEW_PASSWORD_CONFIRMATION = "new_password_confirmation";

    /**
     * User Parameter for email.
     */
    @NonNull
    public static final String PARAM_EMAIL = "email";

    /**
     * User Parameter for last name.
     */
    @NonNull
    public static final String PARAM_LAST_NAME = "last_name";

    /**
     * User Parameter for first name.
     */
    @NonNull
    public static final String PARAM_FIRST_NAME = "first_name";

    /**
     * User Parameter for if they have accepted the terms of service.
     */
    @NonNull
    public static final String PARAM_TERMS_ACCEPTED = "terms_accepted";

    /**
     * User Parameter for birth date.
     */
    @NonNull
    public static final String PARAM_BORN_AT = "born_at";

    /**
     * User Parameter for gender.
     */
    @NonNull
    public static final String PARAM_GENDER = "gender";

    /**
     * User Parameter for custom attributes.
     */
    @NonNull
    public static final String PARAM_CUSTOM_ATTRIBUTES = "custom_attributes";

    /**
     * Parameter for Facebook access token during Facebook connect.
     */
    @LevelUpApi(contract = Contract.INTERNAL)
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_FACEBOOK_ACCESS_TOKEN = "facebook_access_token";

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the User's
     *        {@link com.scvngr.levelup.core.model.AccessToken} if needed.
     */
    public UserRequestFactory(@NonNull final Context context,
            @Nullable final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Get a request that will allow the user to reset their password.
     *
     * @param email the email address they signed up with.
     * @return {@link AbstractRequest} to use to reset the password for the user.
     */
    @NonNull
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public AbstractRequest buildForgotPasswordRequest(@NonNull final String email) {
        final JsonObject resetRequest = new JsonObject();
        resetRequest.addProperty("email", email);

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, "passwords", null,
                new JsonElementRequestBody(resetRequest));
    }

    /**
     * Builds a request to connect a user to Facebook.
     *
     * @param facebookAccessToken the Facebook access token to use to connect the user.
     * @return {@link AbstractRequest} to use to connect the user to Facebook.
     */
    @NonNull
    @LevelUpApi(contract = Contract.INTERNAL)
    @AccessTokenRequired
    public AbstractRequest buildFacebookConnectRequest(@NonNull final String facebookAccessToken) {
        PreconditionUtil.assertNotNull(facebookAccessToken, "facebookAccessToken");

        final JSONObject object = new JSONObject();
        final JSONObject userObject = new JSONObject();

        try {
            userObject.put(PARAM_FACEBOOK_ACCESS_TOKEN, facebookAccessToken);
            object.put(OUTER_PARAM_USER, userObject);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, FACEBOOK_CONNECTION_ENDPOINT, null,
                new JSONObjectRequestBody(object), getAccessTokenRetriever());
    }

    /**
     * Builds a request to disconnect a user from Facebook.
     *
     * @return {@link AbstractRequest} to use to disconnect the user from Facebook.
     */
    @NonNull
    @LevelUpApi(contract = Contract.INTERNAL)
    @AccessTokenRequired
    public AbstractRequest buildFacebookDisconnectRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.DELETE,
                LevelUpRequest.API_VERSION_CODE_V14, FACEBOOK_CONNECTION_ENDPOINT, null, null,
                getAccessTokenRetriever());
    }

    /**
     * Build a request to create a new user. Will also get the device UUID and append it to the
     * request.
     *
     * @param firstName user's first name.
     * @param lastName user's last name.
     * @param email user's email address.
     * @param password user's new password.
     * @param location Android Location for the user (or null if none was found).
     * @return {@link AbstractRequest} representing the register request.
     */
    @NonNull
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public final AbstractRequest buildRegisterRequest(@NonNull final String firstName,
            @NonNull final String lastName, @NonNull final String email,
            @NonNull final String password, @Nullable final Location location) {
        PreconditionUtil.assertNotNull(firstName, "firstName");
        PreconditionUtil.assertNotNull(lastName, "lastName");
        PreconditionUtil.assertNotNull(email, "email");
        PreconditionUtil.assertNotNull(password, "password");

        final JSONObject object = new JSONObject();
        final JSONObject userObject = new JSONObject();

        try {
            final Context context = getContext();

            RequestUtils.addApiKeyToRequestBody(context, object);
            userObject.put(PARAM_FIRST_NAME, firstName);
            userObject.put(PARAM_LAST_NAME, lastName);
            userObject.put(PARAM_EMAIL, email);
            userObject.put(PARAM_TERMS_ACCEPTED, true);
            userObject.put(PARAM_PASSWORD, password);
            RequestUtils.addDeviceIdToRequestBody(context, userObject);

            if (null != location) {
                userObject.put(PARAM_LATITUDE, location.getLatitude());
                userObject.put(PARAM_LONGITUDE, location.getLongitude());
            }

            object.put(OUTER_PARAM_USER, userObject);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, USERS_ENDPOINT, null,
                new JSONObjectRequestBody(object));
    }

    /**
     * Build a request to create a new user.
     *
     * @param firstName user's first name.
     * @param lastName user's last name.
     * @param email user's email address.
     * @param permissions array of permissions to grant the new user
     * @return {@link AbstractRequest} representing the register request.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    public final AbstractRequest buildRegisterRequest(@NonNull final String firstName,
            @NonNull final String lastName, @NonNull final String email,
            @NonNull List<String> permissions) {
        PreconditionUtil.assertNotNull(firstName, "firstName");
        PreconditionUtil.assertNotNull(lastName, "lastName");
        PreconditionUtil.assertNotNull(email, "email");
        PreconditionUtil.assertNotNull(permissions, "permissions");

        final JSONObject object = new JSONObject();
        final JSONObject userObject = new JSONObject();
        final JSONArray permissionArray = new JSONArray();

        try {
            RequestUtils.addApiKeyToRequestBody(getContext(), object);
            userObject.put(PARAM_FIRST_NAME, firstName);
            userObject.put(PARAM_LAST_NAME, lastName);
            userObject.put(PARAM_EMAIL, email);
            userObject.put(PARAM_TERMS_ACCEPTED, true);

            for (final String permission : permissions) {
                permissionArray.put(permission);
            }

            object.put(OUTER_PARAM_USER, userObject);
            object.put(OUTER_PARAM_PERMISSION_KEYNAMES, permissionArray);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V15, USERS_ENDPOINT, null,
                new JSONObjectRequestBody(object));
    }

    /**
     * Build a request to register a new User via a facebook access token.
     *
     * @param facebookAccessToken the facebook access token to use to register the new user.
     * @return the {@link AbstractRequest} to use to register the new user.
     */
    @NonNull
    @LevelUpApi(contract = Contract.INTERNAL)
    public AbstractRequest buildFacebookRegisterRequest(@NonNull final String facebookAccessToken) {
        final JSONObject object = new JSONObject();
        final JSONObject userObject = new JSONObject();

        try {
            RequestUtils.addApiKeyToRequestBody(getContext(), object);
            userObject.put(PARAM_FACEBOOK_ACCESS_TOKEN, facebookAccessToken);

            object.put(OUTER_PARAM_USER, userObject);
        } catch (final JSONException e) {
            LogManager.e("JSONException building facebook register request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, USERS_ENDPOINT, null,
                new JSONObjectRequestBody(object));
    }

    /**
     * Build a request to retrieve the User information.
     *
     * @return the request to retrieve the user information.
     */
    @NonNull
    @AccessTokenRequired
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission({Permissions.PERMISSION_READ_USER_BASIC_INFO})
    public AbstractRequest buildGetUserInfoRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, "users", null, null,
                NullUtils.nonNullContract(getAccessTokenRetriever()));
    }

    /**
     * Builder to create a request that updates the User's information.
     */
    @LevelUpApi(contract = Contract.ENTERPRISE)
    public static final class UserInfoRequestBuilder {

        @NonNull
        private static final String USERS_ID_ENDPOINT = "users";

        /**
         * The Application context. A context is required to build any {@link LevelUpRequest}.
         */
        @NonNull
        private final Context mContext;

        /**
         * The implementation of {@link AccessTokenRetriever} to use to get the User's
         * {@link com.scvngr.levelup.core.model.AccessToken} if needed.
         */
        @NonNull
        private final AccessTokenRetriever mAccessTokenRetriever;

        /**
         * The request JSON body.
         */
        @NonNull
        private final JSONObject mParams = new JSONObject();

        /**
         * The JSON object for custom attributes.
         */
        @NonNull
        private final JSONObject mCustomAttributes = new JSONObject();

        /**
         * Creates a new builder for a {@link UserRequestFactory} request.
         *
         * @param context The Application context.
         * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
         *        User's {@link com.scvngr.levelup.core.model.AccessToken} if needed.
         */
        public UserInfoRequestBuilder(@NonNull final Context context,
                @NonNull final AccessTokenRetriever retriever) {
            mContext = context;
            mAccessTokenRetriever = retriever;
        }

        /**
         * Builds the request which updates the User's information.
         *
         * @return The {@link AbstractRequest} which updates the User's information.
         */
        @NonNull
        @AccessTokenRequired
        @LevelUpApi(contract = Contract.ENTERPRISE)
        public AbstractRequest build() {
            return new LevelUpRequest(mContext, HttpMethod.PUT,
                    LevelUpRequest.API_VERSION_CODE_V15, USERS_ID_ENDPOINT, null,
                    new JSONObjectRequestBody(getParams()), mAccessTokenRetriever);
        }

        /**
         * Helper method to get the full JSON object to post.
         *
         * @return the full {@link JSONObject} to post to the server.
         */
        @NonNull
        private JSONObject getParams() {
            if (0 < mCustomAttributes.length()) {

                try {
                    mParams.put(PARAM_CUSTOM_ATTRIBUTES, mCustomAttributes);
                } catch (final JSONException e) {
                    LogManager.e("JSONException when adding custom attributes to body", e);
                }
            }

            // Nest the parameters in the outer 'user' field.
            final JSONObject requestParameters = new JSONObject();

            try {
                requestParameters.put(OUTER_PARAM_USER, mParams);
            } catch (final JSONException e) {
                LogManager.e("JSONException when building the user update request", e);
            }

            LogManager.v("Building user update request with parameters %s",
                    requestParameters);

            return requestParameters;
        }

        /**
         * Adds the User's born at date information. An empty or null attribute value removes the
         * attribute's key from the pending update request.
         *
         * @param bornAt The born at date as an ISO date time.
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withBornAt(@Nullable final String bornAt) {
            if (null != bornAt) {
                setParam(PARAM_BORN_AT, bornAt);
            }

            return this;
        }

        /**
         * Adds the User's custom attributes. An empty or null custom attribute value removes the
         * custom attribute's key from the pending update request. The request is not modified if
         * the custom attribute set is null.
         *
         * @param key The custom attribute key.
         * @param value The custom attribute value.
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withCustomAttribute(@NonNull final String key,
                @Nullable final String value) {
            try {
                mCustomAttributes.put(key, value);
            } catch (final JSONException e) {
                LogManager.e("JSONException when adding custom attribute", e);
            }

            return this;
        }

        /**
         * Adds the User's email address. An empty or null attribute value removes the attribute's
         * key from the pending update request.
         *
         * @param email The User's email address.
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withEmail(@Nullable final String email) {
            setParam(PARAM_EMAIL, email);

            return this;
        }

        /**
         * Adds the User's first name. An empty or null attribute value removes the attribute's key
         * from the pending update request.
         *
         * @param firstName The User's first name.
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withFirstName(@Nullable final String firstName) {
            setParam(PARAM_FIRST_NAME, firstName);

            return this;
        }

        /**
         * Adds the User's gender information. An empty or null attribute value removes the
         * attribute's key from the pending update request.
         *
         * @param gender The gender string generated by
         *        {@link com.scvngr.levelup.core.model.User.Gender#toString()} .
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withGender(@Nullable final String gender) {
            setParam(PARAM_GENDER, gender);

            return this;
        }

        /**
         * Adds the User's last name. An empty or null attribute value removes the attribute's key
         * from the pending update request.
         *
         * @param lastName The User's last name.
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withLastName(@Nullable final String lastName) {
            setParam(PARAM_LAST_NAME, lastName);

            return this;
        }

        /**
         * Adds the User's new password. An empty or null attribute value removes the attribute's
         * key from the pending update request.
         *
         * @param newPassword The User's new password or null if the password is not being modified.
         * @return The {@link UserInfoRequestBuilder} for chaining convenience.
         */
        @NonNull
        public UserInfoRequestBuilder withNewPassword(@Nullable final String newPassword) {
            setParam(PARAM_NEW_PASSWORD, newPassword);

            return this;
        }

        /**
         * Sets a pending request parameter.
         *
         * @param key The key of the parameter.
         * @param value The value of the parameter.
         */
        private void setParam(@NonNull final String key, @Nullable final String value) {
            // We do not allow nulls, but allow empty strings.
            if (null != value) {
                try {
                    mParams.put(key, value);
                } catch (final JSONException e) {
                    LogManager.e("JSONException when adding key(%s) to body", key, e);
                }
            }
        }
    }
}
