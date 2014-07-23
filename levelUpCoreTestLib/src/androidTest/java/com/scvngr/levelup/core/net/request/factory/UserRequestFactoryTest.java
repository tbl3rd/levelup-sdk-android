/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.location.Location;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.UserFixture;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.net.request.factory.UserRequestFactory.UserInfoRequestBuilder;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.JsonObject;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.UserRequestFactory}.
 */
public final class UserRequestFactoryTest extends SupportAndroidTestCase {
    @NonNull
    private static final String EMAIL_EXAMPLE_COM = "email@example.com";

    /**
     * Tests the constructor to make sure the object performs parameter checking.
     */
    @SmallTest
    public void testConstructor() {
        new UserRequestFactory(getContext(), new MockAccessTokenRetriever());

        try {
            new UserRequestFactory(null, new MockAccessTokenRetriever());
            fail("null context should throw exception");
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests the {@link com.scvngr.levelup.core.net.AbstractRequest} return from
     * {@link com.scvngr.levelup.core.net.request.factory.UserRequestFactory#buildForgotPasswordRequest}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException from {@link com.scvngr.levelup.core.net.AbstractRequest#getUrl}.
     * @throws java.io.UnsupportedEncodingException from {@link java.net.URLEncoder#encode(String, String)}.
     */
    @SmallTest
    public void testBuildForgotPasswordRequest_withValidArgument() throws BadRequestException,
            UnsupportedEncodingException {
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(getContext(),
                        new MockAccessTokenRetriever())
                        .buildForgotPasswordRequest(EMAIL_EXAMPLE_COM);

        assertEquals(HttpMethod.POST, request.getMethod());
        final String path = request.getUrl(getContext()).getPath();
        assertTrue(path.endsWith("passwords"));
        assertTrue(path.startsWith("/v14"));

        final JsonObject expectedRequest = new JsonObject();
        expectedRequest.addProperty("email", EMAIL_EXAMPLE_COM);

        assertEquals(expectedRequest.toString(), request.getBody(getContext()));

        assertEquals(RequestUtils.HEADER_CONTENT_TYPE_JSON, request.getRequestHeaders(getContext())
                .get(HTTP.CONTENT_TYPE));
    }

    @SmallTest
    public void testBuildFacebookConnectRequest() throws Exception {
        final AbstractRequest request =
                new UserRequestFactory(getContext(), new MockAccessTokenRetriever())
                        .buildFacebookConnectRequest("facebook_access_token");

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath().endsWith("/facebook_connection"));
        final LevelUpRequest apiRequest = (LevelUpRequest) request;
        final JSONObject userObject =
                new JSONObject(apiRequest.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);

        assertTrue(userObject.has(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
        assertEquals("facebook_access_token", userObject
                .getString(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));

        // Make sure we can get the access token.
        assertNotNull(apiRequest.getAccessToken(getContext()));
    }

    @SmallTest
    public void testBuildFacebookDisconnectRequest() throws Exception {
        final AbstractRequest request =
                new UserRequestFactory(getContext(), new MockAccessTokenRetriever())
                        .buildFacebookDisconnectRequest();
        final LevelUpRequest apiRequest = (LevelUpRequest) request;

        assertEquals(HttpMethod.DELETE, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath().endsWith("/facebook_connection"));

        // Make sure we can get the access token.
        assertNotNull(apiRequest.getAccessToken(getContext()));
    }

    @SmallTest
    public void testBuildRegisterWithFacebookRequest() throws BadRequestException, JSONException {
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(getContext(), null)
                        .buildFacebookRegisterRequest("facebook_access_token");
        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
        assertTrue(request.getUrl(getContext()).getPath().endsWith("users"));
        final JSONObject userObject =
                new JSONObject(request.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);

        assertTrue(userObject.has(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
        assertEquals("facebook_access_token", userObject
                .getString(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
    }

    @SmallTest
    public void testBuildBuildGetUserInfoRequest() throws BadRequestException, JSONException {
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(getContext(),
                        new MockAccessTokenRetriever()).buildGetUserInfoRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));
        assertTrue(request.getUrl(getContext()).getPath().endsWith("users"));
    }

    /**
     * Validates the {@link com.scvngr.levelup.core.net.AbstractRequest} returned by {@link com.scvngr.levelup.core.net.request.factory.UserRequestFactory.UserInfoRequestBuilder#build()}
     * after adding all of the possible parameters.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException Thrown from
     *         {@link com.scvngr.levelup.core.net.AbstractRequest#getUrl(android.content.Context)}.
     * @throws org.json.JSONException
     */
    @SmallTest
    public void testUserInfoRequestBuilder_withParameters() throws BadRequestException,
            JSONException {
        final UserInfoRequestBuilder builder =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever());
        final User user = UserFixture.getFullModel();

        builder.withBornAt(user.getBornAt());
        builder.withEmail(user.getEmail());
        builder.withFirstName(user.getFirstName());
        builder.withGender(user.getGender().toString());
        builder.withLastName(user.getLastName());

        final Map<String, String> customAttributes = user.getCustomAttributes();
        for (final String key : customAttributes.keySet()) {
            builder.withCustomAttribute(key, customAttributes.get(key));
        }

        final String newPassword = "password123";
        builder.withNewPassword(newPassword);

        final LevelUpRequest request = (LevelUpRequest) builder.build();

        assertEquals(HttpMethod.PUT, request.getMethod());
        assertTrue("hits users/ endpoint", request.getUrl(getContext()).getPath()
                .endsWith("users"));
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V15));
        validateAccessTokenHeader(request);

        final JSONObject postParams =
                new JSONObject(request.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);
        assertEquals(7, postParams.length());

        assertEquals(user.getBornAt(), postParams.get(UserRequestFactory.PARAM_BORN_AT));
        assertEquals(user.getEmail(), postParams.get(UserRequestFactory.PARAM_EMAIL));
        assertEquals(user.getFirstName(), postParams.get(UserRequestFactory.PARAM_FIRST_NAME));
        assertEquals(user.getGender().toString(), postParams.get(UserRequestFactory.PARAM_GENDER));
        assertEquals(user.getLastName(), postParams.get(UserRequestFactory.PARAM_LAST_NAME));

        final JSONObject customAttributesJson =
                postParams.getJSONObject(UserRequestFactory.PARAM_CUSTOM_ATTRIBUTES);
        assertEquals(2, customAttributesJson.length());
        for (final String key : customAttributes.keySet()) {
            assertEquals(customAttributes.get(key), customAttributesJson.get(key));
        }

        assertEquals(newPassword, postParams.get(UserRequestFactory.PARAM_NEW_PASSWORD));
    }

    /**
     * Validates the {@link AbstractRequest} returned by {@link UserInfoRequestBuilder#build()}
     * after adding all of the possible parameters.
     *
     * @throws BadRequestException Thrown from
     *         {@link AbstractRequest#getUrl(android.content.Context)}.
     * @throws JSONException
     */
    @SmallTest
    public void testUserInfoRequestBuilder_withEnterpriseToken() throws BadRequestException,
            JSONException {
        final UserInfoRequestBuilder builder =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever(
                        new AccessToken("test_access_token")));
        final User user = UserFixture.getFullModel();

        builder.withBornAt(user.getBornAt());
        builder.withEmail(user.getEmail());
        builder.withFirstName(user.getFirstName());
        builder.withGender(user.getGender().toString());
        builder.withLastName(user.getLastName());

        final Map<String, String> customAttributes = user.getCustomAttributes();
        for (final String key : customAttributes.keySet()) {
            builder.withCustomAttribute(key, customAttributes.get(key));
        }

        final String newPassword = "password123";
        builder.withNewPassword(newPassword);

        final AbstractRequest request = builder.build();

        assertEquals(HttpMethod.PUT, request.getMethod());
        assertTrue("hits users/ endpoint", request.getUrl(getContext()).getPath()
                .endsWith("users"));
    }

    @SmallTest
    public void testUserInfoRequestBuilder_NoNullParameters() throws BadRequestException {

        final UserInfoRequestBuilder builder =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever());
        builder.withBornAt(null);
        builder.withEmail(null);
        builder.withFirstName(null);
        builder.withGender(null);
        builder.withLastName(null);
        builder.withNewPassword(null);
        builder.withCustomAttribute("test", null);

        final AbstractRequest expected =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever()).build();
        assertEquals(expected, builder.build());
    }

    @SmallTest
    public void testUserInfoRequestBuilder_AllowEmptyParameters() throws BadRequestException,
            JSONException {

        final UserInfoRequestBuilder builder =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever());
        builder.withBornAt("");
        builder.withEmail("");
        builder.withFirstName("");
        builder.withGender("");
        builder.withLastName("");
        builder.withNewPassword("");
        builder.withCustomAttribute("test", "");

        final AbstractRequest expected =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever()).build();
        final LevelUpRequest request =
                (LevelUpRequest) builder.build();
        assertNotSame(expected, request);
        final JSONObject postParams =
                new JSONObject(request.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);
        assertEquals(7, postParams.length());

        final JSONObject customAttributesJson =
                postParams.getJSONObject(UserRequestFactory.PARAM_CUSTOM_ATTRIBUTES);
        assertEquals(1, customAttributesJson.length());
        assertEquals("", customAttributesJson.getString("test"));
    }

    /**
     * Validates the {@link com.scvngr.levelup.core.net.AbstractRequest} returned by {@link com.scvngr.levelup.core.net.request.factory.UserRequestFactory.UserInfoRequestBuilder#build()}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException Thrown from
     *         {@link com.scvngr.levelup.core.net.AbstractRequest#getUrl(android.content.Context)}.
     */
    @SmallTest
    public void testUserInfoRequestBuilder_withoutParameters() throws BadRequestException {
        final AbstractRequest request =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever()).build();

        assertTrue("hits users endpoint", request.getUrl(getContext()).getPath()
                .endsWith("users"));
        validateAccessTokenHeader(request);
    }

    /**
     * Validates the presence of the access token query parameter in the {@link com.scvngr.levelup.core.net.AbstractRequest}.
     *
     * @param request The {@link com.scvngr.levelup.core.net.AbstractRequest} to validate.
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException thrown by {@link com.scvngr.levelup.core.net.AbstractRequest#getUrl(android.content.Context)}.
     */
    private void validateAccessTokenHeader(@NonNull final AbstractRequest request)
            throws BadRequestException {
        final Map<String, String> headers = request.getRequestHeaders(getContext());

        assertTrue(headers.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertTrue(headers.get(LevelUpRequest.HEADER_AUTHORIZATION).contains(
                "test_access_token"));
    }

    @SmallTest
    public void testBuildRegisterRequest_withoutLocation() throws BadRequestException {
        validateRegisterRequest(null);
    }

    @SmallTest
    public void testBuildRegisterRequest_withLocation() throws BadRequestException {
        final Location location = new Location("test");
        location.setLatitude(10d);
        location.setLongitude(20d);
        validateRegisterRequest(location);
    }

    /**
     * Helper to validate the register request and json.
     *
     * @param location the {@link android.location.Location} to set on register.
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException
     */
    private void validateRegisterRequest(@Nullable final Location location)
            throws BadRequestException {
        final Context context = getContext();
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(context, null).buildRegisterRequest(
                        "first_name", "last_name",
                        "email@example.com", "password123", location);

        assertEquals(HttpMethod.POST, request.getMethod());
        final URL url = request.getUrl(context);
        assertTrue("proper URL/api version", url.getPath().endsWith("v14/users"));
        final String body = request.getBody(context);
        assertNotNull(body);
        assertFalse(request.getRequestHeaders(context).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));
        assertEquals(getExpectedRegisterJson(location), body);
    }

    public void testBuildRegisterRequest_withPermissions() throws BadRequestException {
        final List<String> expectedPermissions = new ArrayList<String>();
        expectedPermissions.add("permission1");
        expectedPermissions.add("permission2");

        final Context context = getContext();
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(context, null).buildRegisterRequest(
                        "first_name", "last_name",
                        "email@example.com", expectedPermissions);

        assertEquals(HttpMethod.POST, request.getMethod());
        final URL url = request.getUrl(context);
        assertTrue("proper URL/api version", url.getPath().endsWith("v15/users"));
        final String body = request.getBody(context);
        assertNotNull(body);
        assertFalse(request.getRequestHeaders(context).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));
        assertEquals(getExpectedRegisterJsonWithPermissions(), body);
    }

    /**
     * Helper to get the String representation of the JSON that we are expecting in the request.
     *
     * @param location the location sent in the request
     * @return the json that we are expecting the request to create.
     */
    @NonNull
    private String getExpectedRegisterJson(@Nullable final Location location) {
        final JSONObject object = new JSONObject();
        final JSONObject userObject = new JSONObject();

        try {
            final Context context = NullUtils.nonNullContract(getContext());

            RequestUtils.addApiKeyToRequestBody(context, object);
            userObject.put(UserRequestFactory.PARAM_FIRST_NAME, "first_name");
            userObject.put(UserRequestFactory.PARAM_LAST_NAME, "last_name");
            userObject.put(UserRequestFactory.PARAM_EMAIL, "email@example.com");
            userObject.put(UserRequestFactory.PARAM_TERMS_ACCEPTED, true);
            userObject.put(UserRequestFactory.PARAM_PASSWORD, "password123");
            RequestUtils.addDeviceIdToRequestBody(context, userObject);

            if (null != location) {
                userObject.put(UserRequestFactory.PARAM_LATITUDE, location.getLatitude());
                userObject.put(UserRequestFactory.PARAM_LONGITUDE, location.getLongitude());
            }

            object.put(UserRequestFactory.OUTER_PARAM_USER, userObject);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return object.toString();
    }

    /**
     * Helper to get the String representation of the JSON that we are expecting in the request.
     *
     * @return the json that we are expecting the request to create.
     */
    @NonNull
    private String getExpectedRegisterJsonWithPermissions() {
        final JSONObject object = new JSONObject();
        final JSONObject userObject = new JSONObject();
        final JSONArray permissionObject = new JSONArray();

        try {
            final Context context = NullUtils.nonNullContract(getContext());

            RequestUtils.addApiKeyToRequestBody(context, object);
            userObject.put(UserRequestFactory.PARAM_FIRST_NAME, "first_name");
            userObject.put(UserRequestFactory.PARAM_LAST_NAME, "last_name");
            userObject.put(UserRequestFactory.PARAM_EMAIL, "email@example.com");
            userObject.put(UserRequestFactory.PARAM_TERMS_ACCEPTED, true);

            permissionObject.put("permission1");
            permissionObject.put("permission2");

            object.put(UserRequestFactory.OUTER_PARAM_USER, userObject);
            object.put(UserRequestFactory.OUTER_PARAM_PERMISSION_KEYNAMES, permissionObject);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e);
        }

        return object.toString();
    }
}
