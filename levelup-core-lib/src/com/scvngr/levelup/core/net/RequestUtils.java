/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.BuildUtil;
import com.scvngr.levelup.core.util.CoreLibConstants;
import com.scvngr.levelup.core.util.CryptographicHashUtil;
import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;
import com.scvngr.levelup.core.util.DeviceIdentifier;
import com.scvngr.levelup.core.util.NullUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class with methods to help with creating requests.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class RequestUtils {

    /**
     * Header key for the accepts.
     */
    @NonNull
    public static final String HEADER_ACCEPT = "Accept"; //$NON-NLS-1$

    /**
     * Value for the accepts header for JSON.
     */
    @NonNull
    public static final String HEADER_CONTENT_TYPE_JSON = "application/json"; //$NON-NLS-1$

    /**
     * Header key for the user agent.
     */
    @NonNull
    public static final String HEADER_USER_AGENT = "User-Agent"; //$NON-NLS-1$

    /**
     * Header key that maps to the model of the current device.
     *
     * @see RequestUtils#HEADER_DEVICE_MODEL_VALUE
     */
    @NonNull
    public static final String HEADER_DEVICE_MODEL_KEY = "X-Device-Model"; //$NON-NLS-1$

    /**
     * Header value representing the model of the current device.
     *
     * @see RequestUtils#HEADER_DEVICE_MODEL_KEY
     */
    @NonNull
    public static final String HEADER_DEVICE_MODEL_VALUE = NullUtils.format(
            "%s/%s", Build.BRAND, Build.PRODUCT); //$NON-NLS-1$

    /**
     * Parameter for the client identifier.
     */
    @NonNull
    public static final String PARAM_CLIENT_ID = "client_id"; //$NON-NLS-1$

    /**
     * Parameter for the device identifier.
     */
    @NonNull
    public static final String PARAM_DEVICE_IDENTIFIER = "device_identifier"; //$NON-NLS-1$

    /**
     * Get the request headers for all API requests.
     *
     * @param context Application context.
     * @return the headers to add to the request.
     */
    @NonNull
    public static Map<String, String> getDefaultRequestHeaders(@NonNull final Context context) {
        final Map<String, String> headers = new HashMap<String, String>(3);
        headers.put(HEADER_DEVICE_MODEL_KEY, HEADER_DEVICE_MODEL_VALUE);
        headers.put(HEADER_USER_AGENT, getUserAgent(context));
        headers.put(HEADER_ACCEPT, HEADER_CONTENT_TYPE_JSON);
        return headers;
    }

    /**
     * Returns a {@link String} representation to use as the User-Agent string when communicating
     * with the server. A sample string looks like:
     * <p/>
     * "LevelUp/2.3.12 (Linux; U; Android; 1.0; generic_x86/sd_x86; en-US;) LevelUpSdk/0.0.1".
     *
     * @param context Application context.
     * @return the user agent value.
     */
    @NonNull
    public static String getUserAgent(@NonNull final Context context) {
        return NullUtils.format(
                "%s (Linux; U; Android %s; %s/%s; %s) %s", //$NON-NLS-1$
                getUserAgentAppVersionString(context), Build.VERSION.RELEASE, Build.BRAND,
                Build.PRODUCT, Locale.getDefault().toString(),
                getUserAgentSdkVersionString(context));
    }

    /**
     * Get the API key for the context passed.
     *
     * @param context the Application context.
     * @return The API key from resources.
     */
    @NonNull
    public static String getApiKey(@NonNull final Context context) {
        final String apiKey = NullUtils.nonNullContract(context.getString(R.string.levelup_api_key));

        if (TextUtils.isEmpty(apiKey)) {
            throw new AssertionError(String.format(Locale.US, "Application must override %s", //$NON-NLS-1$
                    context.getResources().getResourceEntryName(R.string.levelup_api_key)));
        }

        return apiKey;
    }

    /**
     * Gets the deviceId to add to the login/register requests.
     *
     * @param context the Application context.
     * @return the deviceId or null if it couldn't be determined.
     */
    @Nullable
    public static String getDeviceId(@NonNull final Context context) {
        final String deviceIdentifier = DeviceIdentifier.getDeviceId(context);
        final String hashedDeviceIdentifier;

        if (null != deviceIdentifier) {
            hashedDeviceIdentifier =
                    CryptographicHashUtil.getHexHash(deviceIdentifier, Algorithms.SHA256);
        } else {
            hashedDeviceIdentifier = null;
        }

        return hashedDeviceIdentifier;
    }

    /**
     * Add the API key to the request query parameters.
     *
     * @param context Application context.
     * @param params the query parameter {@link Map} to add the API key to.
     */
    public static void addApiKeyToRequestQueryParams(@NonNull final Context context,
            @NonNull final Map<String, String> params) {
        params.put(PARAM_CLIENT_ID, getApiKey(context));
    }

    /**
     * Add the API key to the request body.
     *
     * @param context Application context.
     * @param body the {@link JSONObject} to add the API key to.
     */
    public static void addApiKeyToRequestBody(@NonNull final Context context,
            @NonNull final JSONObject body) {
        try {
            body.put(PARAM_CLIENT_ID, getApiKey(context));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Add the Device ID to the request body if the Device ID can be determined. The request body is
     * not modified if the Device ID could not be determined.
     *
     * @param context Application context
     * @param body the {@link JSONObject} to add the Device ID to
     */
    public static void addDeviceIdToRequestBody(@NonNull final Context context,
            @NonNull final JSONObject body) {
        try {
            body.putOpt(PARAM_DEVICE_IDENTIFIER, getDeviceId(context));
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Gets the package name and version of the app for the user agent header. For example,
     * "com.scvngr.levelup.app/2.3.12".
     *
     * @param context the Application Context
     * @return version of the app.
     */
    @NonNull
    private static String getUserAgentAppVersionString(@NonNull final Context context) {
        final PackageInfo info = BuildUtil.getMyPackageInfo(context);
        return info.applicationInfo.packageName + "/" + info.versionName; //$NON-NLS-1$
    }

    /**
     * Gets the name and version of the SDK for the user agent header. For example,
     * "LevelUpSdk/0.1".
     *
     * @param context the Application Context
     * @return version of the SDK.
     */
    @NonNull
    private static String getUserAgentSdkVersionString(@NonNull final Context context) {
        return NullUtils.format("LevelUpSdk/%s", CoreLibConstants.SDK_VERSION); //$NON-NLS-1$
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private RequestUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
