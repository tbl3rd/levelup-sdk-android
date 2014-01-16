/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.BuildUtil;
import com.scvngr.levelup.core.util.CoreLibConstants;
import com.scvngr.levelup.core.util.CryptographicHashUtil;
import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;
import com.scvngr.levelup.core.util.DeviceIdentifier;

/**
 * Class with methods to help with creating requests.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class RequestUtils {

    /**
     * Header key for the accepts.
     */
    public static final String HEADER_ACCEPT = "Accept"; //$NON-NLS-1$

    /**
     * Value for the accepts header for JSON.
     */
    public static final String HEADER_CONTENT_TYPE_JSON = "application/json"; //$NON-NLS-1$

    /**
     * Header key for the user agent.
     */
    public static final String HEADER_USER_AGENT = "User-Agent"; //$NON-NLS-1$

    /**
     * Header key that maps to the model of the current device.
     *
     * @see RequestUtils#HEADER_DEVICE_MODEL_VALUE
     */
    public static final String HEADER_DEVICE_MODEL_KEY = "X-Device-Model"; //$NON-NLS-1$

    /**
     * Header value representing the model of the current device.
     *
     * @see RequestUtils#HEADER_DEVICE_MODEL_KEY
     */
    public static final String HEADER_DEVICE_MODEL_VALUE = String.format(Locale.US,
            "%s/%s", Build.BRAND, Build.PRODUCT); //$NON-NLS-1$

    /**
     * Method to create a nested parameter key. A nested parameter key looks like: "user[name]".
     *
     * @param outerParam the outer parameter name.
     * @param innerParam the inner parameter name.
     * @return {@link String} formatted like a nested parameter key
     */
    public static String getNestedParameterKey(@NonNull final String outerParam,
            @NonNull final String innerParam) {
        if (null == outerParam) {
            throw new IllegalArgumentException("outerParam cannot be null"); //$NON-NLS-1$
        }
        if (null == innerParam) {
            throw new IllegalArgumentException("innerParam cannot be null"); //$NON-NLS-1$
        }

        return String.format(Locale.US, "%s[%s]", outerParam, innerParam); //$NON-NLS-1$
    }

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
     *
     * "LevelUp/2.3.12 (Linux; U; Android; 1.0; generic_x86/sd_x86; en-US;) LevelUpSdk/0.0.1".
     *
     * @param context Application context.
     * @return the user agent value.
     */
    @NonNull
    public static String getUserAgent(@NonNull final Context context) {
        return String.format(
                Locale.US,
                "%s (Linux; U; Android %s; %s/%s; %s) %s", //$NON-NLS-1$
                getUserAgentAppVersionString(context), Build.VERSION.RELEASE, Build.BRAND,
                Build.PRODUCT, Locale.getDefault().toString(),
                getUserAgentSdkVersionString(context));
    }

    /**
     * Gets the name and version of the app for the user agent header. For example,
     * "LevelUp/2.3.12".
     *
     * @param context the Application Context
     * @return version of the app.
     */
    @NonNull
    public static String getUserAgentAppVersionString(@NonNull final Context context) {
        final StringBuilder builder = new StringBuilder();

        final PackageInfo info = BuildUtil.getMyPackageInfo(context);
        builder.append(info.applicationInfo.name).append("/") //$NON-NLS-1$
                .append(info.versionName);

        return builder.toString();
    }

    /**
     * Gets the name and version of the SDK for the user agent header. For example,
     * "LevelUpSdk/0.1".
     *
     * @param context the Application Context
     * @return version of the SDK.
     */
    @NonNull
    public static String getUserAgentSdkVersionString(@NonNull final Context context) {
        return String.format(Locale.US, "LevelUpSdk/%s", CoreLibConstants.SDK_VERSION); //$NON-NLS-1$
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
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private RequestUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
