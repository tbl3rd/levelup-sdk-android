/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

import net.jcip.annotations.ThreadSafe;

/**
 * This is a utility class to determine information about the environment the app is running under.
 */
@ThreadSafe
@LevelUpApi(contract = Contract.INTERNAL)
public final class EnvironmentUtil {
    /**
     * OpenGL version number in the <a href=
     * "http://developer.android.com/guide/topics/manifest/uses-feature-element.html#glEsVersion">format
     * required by Android</a>.
     */
    private static final int OPENGL_V2 = 0x20000;

    /**
     * @param context Application context.
     * @return True if OpenGLES 2.0 is supported.
     */
    public static boolean isOpenGlEs20Supported(@NonNull final Context context) {
        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        final boolean isOpenGlEs20Supported = configurationInfo.reqGlEsVersion >= OPENGL_V2;

        LogManager.v("isOpenGlEs20Supported = %b", isOpenGlEs20Supported); //$NON-NLS-1$
        return isOpenGlEs20Supported;
    }

    /**
     * Determines whether the current SDK is greater than or equal to 11.
     *
     * @return {@code true} if {@link android.os.Build.VERSION#SDK_INT} is greater than or equal to
     *         11.
     */
    public static boolean isSdk11OrGreater() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Determines whether the current SDK is greater than or equal to 12.
     *
     * @return {@code true} if {@link android.os.Build.VERSION#SDK_INT} is greater than or equal to
     *         12.
     */
    public static boolean isSdk12OrGreater() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Determines whether the current SDK is greater than or equal to 14.
     *
     * @return {@code true} if {@link android.os.Build.VERSION#SDK_INT} is greater than or equal to
     *         14.
     */
    public static boolean isSdk14OrGreater() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Determines whether the current SDK is greater than or equal to 16.
     *
     * @return {@code true} if {@link android.os.Build.VERSION#SDK_INT} is greater than or equal to
     *         16.
     */
    public static boolean isSdk16OrGreater() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Determines whether the current SDK is greater than or equal to 17.
     *
     * @return {@code true} if {@link android.os.Build.VERSION#SDK_INT} is greater than or equal to
     *         17.
     */
    public static boolean isSdk17OrGreater() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private EnvironmentUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
