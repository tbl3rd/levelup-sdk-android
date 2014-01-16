/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

public final class ResourcesUtil {
    public static Resources getAppResources(final Context context) {
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
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
