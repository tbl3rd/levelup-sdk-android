/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
