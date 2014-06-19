/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Permission;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.CryptographicHashUtil;
import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;
import com.scvngr.levelup.core.util.DeviceIdentifier;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.AccessTokenRequestFactory}.
 */
public final class AccessTokenRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final AccessTokenRequestFactory factory = new AccessTokenRequestFactory(getContext());
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertNull(factory.getAccessTokenRetriever());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.request.factory.AccessTokenRequestFactory#buildLoginRequest(String, String)}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException on bad requests.
     * @throws org.json.JSONException on malformed JSON
     */
    @SmallTest
    public void testGetLoginRequest_withValidArguments() throws BadRequestException, JSONException {
        final AccessTokenRequestFactory builder = new AccessTokenRequestFactory(getContext());
        final LevelUpRequest request =
                (LevelUpRequest) builder.buildLoginRequest("email", "password"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, request.getQueryParams(getContext()).size());
        assertFalse(request.getRequestHeaders(getContext()).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));
        final JSONObject params = new JSONObject(request.getBody(getContext()));

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
        assertTrue("Url ends with the proper endpoint", //$NON-NLS-1$
                request.getUrl(getContext()).getPath().endsWith("access_tokens")); //$NON-NLS-1$
        assertTrue(params.has(AccessTokenRequestFactory.PARAM_OUTER_ACCESS_TOKEN));
        final JSONObject token =
                params.getJSONObject(AccessTokenRequestFactory.PARAM_OUTER_ACCESS_TOKEN);
        assertTrue("Params include username", //$NON-NLS-1$
                token.has(AccessTokenRequestFactory.PARAM_USERNAME));
        assertTrue("Params include password", //$NON-NLS-1$
                token.has(AccessTokenRequestFactory.PARAM_PASSWORD));
        assertTrue("Params include device id", //$NON-NLS-1$
                token.has(RequestUtils.PARAM_DEVICE_IDENTIFIER));
        assertTrue("Params include api_key", //$NON-NLS-1$
                token.has(RequestUtils.PARAM_API_KEY));

        assertEquals("email", token.getString(AccessTokenRequestFactory.PARAM_USERNAME)); //$NON-NLS-1$
        assertEquals("password", token.getString(AccessTokenRequestFactory.PARAM_PASSWORD)); //$NON-NLS-1$
        final String deviceId = DeviceIdentifier.getDeviceId(getContext());

        if (null != deviceId) {
            assertEquals(CryptographicHashUtil.getHexHash(deviceId, Algorithms.SHA256),
                    token.getString(RequestUtils.PARAM_DEVICE_IDENTIFIER));
        } else {
            fail("Device ID was null"); //$NON-NLS-1$
        }

        assertEquals(getContext().getString(com.scvngr.levelup.core.R.string.levelup_api_key),
                token.getString(RequestUtils.PARAM_API_KEY));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.net.request.factory.AccessTokenRequestFactory#buildFacebookLoginRequest(String)}.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException on bad requests.
     * @throws org.json.JSONException on malformed JSON
     */
    @SmallTest
    public void testGetFacebookLoginRequest_withValidArgument() throws BadRequestException,
            JSONException {
        final AccessTokenRequestFactory builder = new AccessTokenRequestFactory(getContext());
        final LevelUpRequest request =
                (LevelUpRequest) builder.buildFacebookLoginRequest("facebook_access_token"); //$NON-NLS-1$
        assertEquals(0, request.getQueryParams(getContext()).size());
        assertFalse(request.getRequestHeaders(getContext()).containsKey(
                LevelUpRequest.HEADER_AUTHORIZATION));
        final JSONObject params = new JSONObject(request.getBody(getContext()));

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                .contains(LevelUpRequest.API_VERSION_CODE_V14));
        assertTrue("Url ends with the proper endpoint", //$NON-NLS-1$
                request.getUrl(getContext()).getPath().endsWith("access_tokens")); //$NON-NLS-1$
        assertTrue(params.has(AccessTokenRequestFactory.PARAM_OUTER_ACCESS_TOKEN));
        final JSONObject token =
                params.getJSONObject(AccessTokenRequestFactory.PARAM_OUTER_ACCESS_TOKEN);
        assertTrue("Params include facebook_access_token", //$NON-NLS-1$
                token.has(AccessTokenRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
        assertTrue("Params include device id", //$NON-NLS-1$
                token.has(RequestUtils.PARAM_DEVICE_IDENTIFIER));
        assertTrue("Params include api_key", //$NON-NLS-1$
                token.has(RequestUtils.PARAM_API_KEY));

        assertEquals("facebook_access_token", //$NON-NLS-1$
                token.getString(AccessTokenRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));

        final String deviceId = DeviceIdentifier.getDeviceId(getContext());

        if (null != deviceId) {
            assertEquals(CryptographicHashUtil.getHexHash(deviceId, Algorithms.SHA256),
                    token.getString(RequestUtils.PARAM_DEVICE_IDENTIFIER));
        } else {
            fail("Device ID was null"); //$NON-NLS-1$
        }

        assertEquals(getContext().getString(com.scvngr.levelup.core.R.string.levelup_api_key),
                token.getString(RequestUtils.PARAM_API_KEY));
    }

    public void testBuildDowngradeRequest_withValidArgument() throws BadRequestException {
        final AccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final AccessTokenRequestFactory builder =
                new AccessTokenRequestFactory(getContext(), retriever);

        assertEquals(retriever, builder.getAccessTokenRetriever());

        final List<Permission> permissions =
                NullUtils.nonNullContract(Arrays
                        .asList(new Permission[] {
                                new Permission("Create orders for you", "create_orders"),
                                new Permission("Do dances for you through LevelUp(R)",
                                        "do_a_little_dance") }));

        final LevelUpRequest request =
                (LevelUpRequest) builder
                        .buildDowngradeRequest("create_orders", "do_a_little_dance");

        assertEquals(HttpMethod.POST, request.getMethod());
        assertTrue("Url points to proper api version", request.getUrl(getContext()).getPath() //$NON-NLS-1$
                .contains(LevelUpRequest.API_VERSION_CODE_V15));
        assertTrue("Url ends with the proper endpoint", //$NON-NLS-1$
                request.getUrl(getContext()).getPath().endsWith("access_tokens/downgrades")); //$NON-NLS-1$

        final JsonObject params =
                (JsonObject) new JsonParser().parse(request.getBody(getContext()));
        assertTrue(params.has(AccessTokenRequestFactory.PARAM_OUTER_ACCESS_TOKEN));
        final JsonObject token =
                params.getAsJsonObject(AccessTokenRequestFactory.PARAM_OUTER_ACCESS_TOKEN);
        assertTrue(token.has(AccessTokenRequestFactory.PARAM_PERMISSION_KEYNAMES));
        final JsonArray permissionKeyNames =
                token.getAsJsonArray(AccessTokenRequestFactory.PARAM_PERMISSION_KEYNAMES);

        final Set<String> expectedPermissionKeyNames = new HashSet<String>();
        for (final Permission permission : permissions) {
            expectedPermissionKeyNames.add(permission.getKeyname());
        }
        final Set<String> actualPermissionKeyNames = new HashSet<String>();
        for (final JsonElement permission : permissionKeyNames) {
            actualPermissionKeyNames.add(permission.getAsString());
        }
        assertEquals(expectedPermissionKeyNames, actualPermissionKeyNames);
    }
}
