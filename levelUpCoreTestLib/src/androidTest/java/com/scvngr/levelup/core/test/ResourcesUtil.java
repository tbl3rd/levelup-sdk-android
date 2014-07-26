/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Utility class for dealing with {@link android.content.res.Resources}.
 */
public final class ResourcesUtil {
    /**
     * Get the {@link android.content.res.Resources} for an app, returning null rather than throwing a
     * {@link android.content.pm.PackageManager.NameNotFoundException} if something goes wrong.
     *
     * @param context the context to get resources for.
     * @return the resources, or null if there was a {@link android.content.pm.PackageManager.NameNotFoundException}.
     */
    @Nullable
    public static Resources getAppResources(@NonNull final Context context) {
        final PackageManager pm = context.getPackageManager();
        final String packageName = context.getApplicationContext().getPackageName();
        try {
            return pm.getResourcesForApplication(packageName);
        } catch (final NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ResourcesUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
