/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.CryptographicHashUtil;
import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;
import com.scvngr.levelup.core.util.DeviceIdentifier;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link AccessTokenRequestFactory}.
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
                token.has(UserRequestFactory.PARAM_DEVICE_IDENTIFIER));
        assertTrue("Params include client_id", //$NON-NLS-1$
                token.has(AccessTokenRequestFactory.PARAM_CLIENT_ID));

        assertEquals("email", token.getString(AccessTokenRequestFactory.PARAM_USERNAME)); //$NON-NLS-1$
        assertEquals("password", token.getString(AccessTokenRequestFactory.PARAM_PASSWORD)); //$NON-NLS-1$
        assertEquals(CryptographicHashUtil.getHexHash(DeviceIdentifier.getDeviceId(getContext()),
                Algorithms.SHA256), token.getString(UserRequestFactory.PARAM_DEVICE_IDENTIFIER));
        assertEquals(getContext().getString(com.scvngr.levelup.core.R.string.levelup_api_key),
                token.getString(AccessTokenRequestFactory.PARAM_CLIENT_ID));
    }

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
                token.has(UserRequestFactory.PARAM_DEVICE_IDENTIFIER));
        assertTrue("Params include client_id", //$NON-NLS-1$
                token.has(AccessTokenRequestFactory.PARAM_CLIENT_ID));

        assertEquals("facebook_access_token", //$NON-NLS-1$
                token.getString(AccessTokenRequestFactory.PARAM_FACEBOOK_ACCESS_TOKEN));
        assertEquals(CryptographicHashUtil.getHexHash(DeviceIdentifier.getDeviceId(getContext()),
                Algorithms.SHA256), token.getString(UserRequestFactory.PARAM_DEVICE_IDENTIFIER));
        assertEquals(getContext().getString(com.scvngr.levelup.core.R.string.levelup_api_key),
                token.getString(AccessTokenRequestFactory.PARAM_CLIENT_ID));
    }

    @SmallTest
    public void testAddApiKeyToRequest() throws JSONException {
        final JSONObject object = new JSONObject();
        final JSONObject expected = new JSONObject();
        expected.put(AccessTokenRequestFactory.PARAM_CLIENT_ID,
                getContext().getString(R.string.levelup_api_key));

        AccessTokenRequestFactory.addApiKeyToRequest(getContext(), object);
        assertEquals(expected.toString(), object.toString());
    }
}
