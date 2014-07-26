/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

/**
 * This is a utility class to determine information about the build of the app.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class BuildUtil {

    /**
     * Gets the "label" of the app.
     *
     * @param context Application context.
     * @return Label of the application.
     */
    @NonNull
    /* package */static String getLabel(@NonNull final Context context) {
        PreconditionUtil.assertNotNull(context, "context");

        final CharSequence label =
                context.getPackageManager().getApplicationLabel(context.getApplicationInfo());

        final String result;
        if (TextUtils.isEmpty(label)) {
            result = context.getPackageName();
        } else {
            result = label.toString();
        }

        return result;
    }

    /**
     * Gets the "versionCode" of the app.
     *
     * @param context Application context.
     * @return versionCode in the AndroidManifest.
     * @see android.content.pm.PackageInfo#versionCode
     */
    public static int getVersionCode(@NonNull final Context context) {
        return getMyPackageInfo(context).versionCode;
    }

    /**
     * Gets the "versionName" of the app.
     *
     * @param context Application context.
     * @return versionName in the AndroidManifest.
     * @see android.content.pm.PackageInfo#versionName
     */
    @NonNull
    public static String getVersionName(@NonNull final Context context) {
        return getMyPackageInfo(context).versionName;
    }

    /**
     * Wrapper to obtain the {@link PackageInfo} for the current package.
     *
     * @param context Application context.
     * @return Package info for the current app's package.
     */
    @NonNull
    public static PackageInfo getMyPackageInfo(@NonNull final Context context) {
        PreconditionUtil.assertNotNull(context, "context");

        final PackageManager pm = context.getPackageManager();
        final String packageName = context.getPackageName();

        try {
            return pm.getPackageInfo(packageName, 0);
        } catch (final NameNotFoundException e) {

            /*
             * If this code is running, our own package must exist.
             */
            throw new AssertionError(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private BuildUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
