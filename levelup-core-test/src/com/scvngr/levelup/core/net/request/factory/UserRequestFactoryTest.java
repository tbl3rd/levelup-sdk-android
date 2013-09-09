package com.scvngr.levelup.core.net.request.factory;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.location.Location;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.UserFixture;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.LevelUpRequestWithCurrentUser;
import com.scvngr.levelup.core.net.LevelUpV13Request;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.request.RequestUtils;
import com.scvngr.levelup.core.net.request.factory.UserRequestFactory.UserInfoRequestBuilder;
import com.scvngr.levelup.core.util.LogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link UserRequestFactory}.
 */
public final class UserRequestFactoryTest extends AndroidTestCase {
    /**
     * Tests the constructor to make sure the object performs parameter checking.
     */
    @SmallTest
    public void testConstructor() {
        new UserRequestFactory(getContext(), new MockAccessTokenRetriever());

        try {
            new UserRequestFactory(null, new MockAccessTokenRetriever());
            fail("null context should throw exception"); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests {@link UserRequestFactory#buildForgotPasswordRequest(String)} by passing null for then
     * accessToken argument.
     */
    @SmallTest
    public void testBuildForgotPasswordRequest_withNullAccessToken() {
        final UserRequestFactory builder = new UserRequestFactory(getContext(), null);

        try {
            builder.buildForgotPasswordRequest(null);
            fail("null request should throw exception"); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests the returned {@link Request} from
     * {@link UserRequestFactory#buildForgotPasswordRequest(String)} for validity.
     *
     * @throws Exception if URLEncoder throws
     */
    @SmallTest
    public void testBuildForgotPasswordRequest_withValidArgument() throws Exception {
        final LevelUpV13Request request =
                (LevelUpV13Request) new UserRequestFactory(getContext(),
                        new MockAccessTokenRetriever())
                        .buildForgotPasswordRequest("email@example.com"); //$NON-NLS-1$

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath().endsWith("users/forgot_password.json")); //$NON-NLS-1$
        final LevelUpV13Request apiRequest = request;
        assertTrue(apiRequest.getPostParams().containsKey("user[email]")); //$NON-NLS-1$
        assertEquals("email@example.com", apiRequest.getPostParams().get("user[email]")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(apiRequest.getPostParams().containsKey("access_token")); //$NON-NLS-1$
        assertFalse(apiRequest.getQueryParams(getContext()).containsKey("access_token")); //$NON-NLS-1$

        final StringBuilder postBody = new StringBuilder();
        postBody.append(URLEncoder.encode("user[email]", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
        postBody.append("="); //$NON-NLS-1$
        postBody.append(URLEncoder.encode("email@example.com", "utf-8")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(postBody.toString(), request.getBody(getContext()));
    }

    @SmallTest
    public void testBuildFacebookConnectRequest() throws Exception {
        final AbstractRequest request =
                new UserRequestFactory(getContext(), new MockAccessTokenRetriever())
                        .buildFacebookConnectRequest("facebook_access_token"); //$NON-NLS-1$

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath().endsWith("/facebook_connection")); //$NON-NLS-1$
        final LevelUpRequest apiRequest = (LevelUpRequest) request;
        final JSONObject userObject =
                new JSONObject(apiRequest.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);

        assertTrue(userObject.has(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
        assertEquals("facebook_access_token", userObject //$NON-NLS-1$
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
        assertTrue(request.getUrl(getContext()).getPath().endsWith("/facebook_connection")); //$NON-NLS-1$

        // Make sure we can get the access token.
        assertNotNull(apiRequest.getAccessToken(getContext()));
    }

    @SmallTest
    public void testBuildRegisterWithFacebookRequest() throws BadRequestException, JSONException {
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(getContext(), null)
                        .buildFacebookRegisterRequest("facebook_access_token"); //$NON-NLS-1$
        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue(request.getUrl(getContext()).getPath()
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
        assertTrue(request.getUrl(getContext()).getPath().endsWith("users")); //$NON-NLS-1$
        final JSONObject userObject =
                new JSONObject(request.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);

        assertTrue(userObject.has(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
        assertEquals("facebook_access_token", userObject //$NON-NLS-1$
                .getString(UserRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
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

        final String newPassword = "password123"; //$NON-NLS-1$
        builder.withNewPassword(newPassword);

        final AbstractRequest request = builder.build();

        assertEquals(HttpMethod.PUT, request.getMethod());
        assertTrue("hits users/<id> endpoint", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                .endsWith(String.format(Locale.US, "users/%d", 1))); //$NON-NLS-1$
        validateAccessTokenHeader(request);

        assertTrue(request instanceof LevelUpRequestWithCurrentUser);
        final LevelUpRequestWithCurrentUser LevelUpV13Request =
                (LevelUpRequestWithCurrentUser) request;

        final JSONObject postParams =
                new JSONObject(LevelUpV13Request.getBody(getContext()))
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
        builder.withCustomAttribute("test", null); //$NON-NLS-1$

        final AbstractRequest expected =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever()).build();
        assertEquals(expected, builder.build());
    }

    @SmallTest
    public void testUserInfoRequestBuilder_AllowEmptyParameters() throws BadRequestException,
            JSONException {

        final UserInfoRequestBuilder builder =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever());
        builder.withBornAt(""); //$NON-NLS-1$
        builder.withEmail(""); //$NON-NLS-1$
        builder.withFirstName(""); //$NON-NLS-1$
        builder.withGender(""); //$NON-NLS-1$
        builder.withLastName(""); //$NON-NLS-1$
        builder.withNewPassword(""); //$NON-NLS-1$
        builder.withCustomAttribute("test", ""); //$NON-NLS-1$ //$NON-NLS-2$

        final AbstractRequest expected =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever()).build();
        final LevelUpRequestWithCurrentUser request =
                (LevelUpRequestWithCurrentUser) builder.build();
        assertNotSame(expected, request);
        final JSONObject postParams =
                new JSONObject(request.getBody(getContext()))
                        .getJSONObject(UserRequestFactory.OUTER_PARAM_USER);
        assertEquals(7, postParams.length());

        final JSONObject customAttributesJson =
                postParams.getJSONObject(UserRequestFactory.PARAM_CUSTOM_ATTRIBUTES);
        assertEquals(1, customAttributesJson.length());
        assertEquals("", customAttributesJson.getString("test")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Validates the {@link AbstractRequest} returned by {@link UserInfoRequestBuilder#build()}.
     *
     * @throws BadRequestException Thrown from
     *         {@link AbstractRequest#getUrl(android.content.Context)}.
     */
    @SmallTest
    public void testUserInfoRequestBuilder_withoutParameters() throws BadRequestException {
        final AbstractRequest request =
                new UserInfoRequestBuilder(getContext(), new MockAccessTokenRetriever()).build();

        assertTrue("hits users/<id> endpoint", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                .endsWith(String.format(Locale.US, "users/%d", 1))); //$NON-NLS-1$
        validateAccessTokenHeader(request);
    }

    /**
     * Validates the presence of the access token query parameter in the {@link AbstractRequest}.
     *
     * @param request The {@link AbstractRequest} to validate.
     * @throws {@link BadRequestException} Thrown by
     *         {@link AbstractRequest#getUrl(android.content.Context)}.
     */
    private void validateAccessTokenHeader(@NonNull final AbstractRequest request)
            throws BadRequestException {
        final Map<String, String> headers = request.getRequestHeaders(getContext());

        assertTrue(headers.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertTrue(headers.get(LevelUpRequest.HEADER_AUTHORIZATION).contains(
                "test_access_token")); //$NON-NLS-1$
    }

    @SmallTest
    public void testBuildRegisterRequest_withoutLocation() throws BadRequestException {
        validateRegisterRequest(null);
    }

    @SmallTest
    public void testBuildRegisterRequest_withLocation() throws BadRequestException {
        final Location location = new Location("test"); //$NON-NLS-1$
        location.setLatitude(10d);
        location.setLongitude(20d);
        validateRegisterRequest(location);
    }

    @SmallTest
    public void testAddDeviceIdToRequest() throws JSONException {
        final JSONObject object = new JSONObject();
        UserRequestFactory.addDeviceIdToRequest(getContext(), object);

        assertTrue(object.has(UserRequestFactory.PARAM_DEVICE_IDENTIFIER));
        assertEquals(RequestUtils.getDeviceId(getContext()),
                object.getString(UserRequestFactory.PARAM_DEVICE_IDENTIFIER));
    }

    /**
     * Helper to validate the register request and json.
     *
     * @param location the {@link Location} to set on register.
     * @throws BadRequestException
     */
    private void validateRegisterRequest(@Nullable final Location location)
            throws BadRequestException {
        final Context context = getContext();
        final LevelUpRequest request =
                (LevelUpRequest) new UserRequestFactory(context, null)
                        .buildRegisterRequest("first_name", "last_name", "email@email.com", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                "password123", location); //$NON-NLS-1$

        assertEquals(HttpMethod.POST, request.getMethod());
        final URL url = request.getUrl(context);
        assertTrue("proper URL/api version", url.getPath().endsWith("v14/users")); //$NON-NLS-1$ //$NON-NLS-2$
        final String body = request.getBody(context);
        assertNotNull(body);
        assertFalse(request.getRequestHeaders(context).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));
        assertEquals(getExpectedRegisterJson(location), body);
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
            userObject.put(UserRequestFactory.PARAM_FIRST_NAME, "first_name"); //$NON-NLS-1$
            userObject.put(UserRequestFactory.PARAM_LAST_NAME, "last_name"); //$NON-NLS-1$
            userObject.put(UserRequestFactory.PARAM_EMAIL, "email@email.com"); //$NON-NLS-1$
            userObject.put(UserRequestFactory.PARAM_TERMS_ACCEPTED, true);
            userObject.put(UserRequestFactory.PARAM_PASSWORD, "password123"); //$NON-NLS-1$
            UserRequestFactory.addDeviceIdToRequest(getContext(), userObject);

            if (null != location) {
                userObject.put(UserRequestFactory.PARAM_LATITUDE, location.getLatitude());
                userObject.put(UserRequestFactory.PARAM_LONGITUDE, location.getLongitude());
            }

            object.put(UserRequestFactory.OUTER_PARAM_USER, userObject);
        } catch (final JSONException e) {
            LogManager.e("JSONException building register request", e); //$NON-NLS-1$
        }

        return object.toString();
    }
}
