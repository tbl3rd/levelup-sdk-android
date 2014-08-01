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
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

/**
 * This is a utility class to check permissions available to the current app.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class PermissionUtil {
    /**
     * @param context Application context.
     * @param permissionName Name of the permission to check.
     * @return True if the application is able to use {@code permissionName}.
     */
    public static boolean isPermissionGranted(@NonNull final Context context,
            @NonNull final String permissionName) {
        /*
         * Do not call getApplicationContext(), because some unit tests depend on replacing the
         * context.
         */
        PreconditionUtil.assertNotNull(context, "context");
        PreconditionUtil.assertNotNull(permissionName, "permissionName");

        final PackageManager pm = context.getPackageManager();
        final String packageName = context.getPackageName();

        boolean result = true;
        if (PackageManager.PERMISSION_DENIED == pm.checkPermission(permissionName, packageName)) {
            LogManager.w("Permission %s is not granted", permissionName);
            result = false;
        }

        /*
         * Some custom ROMs allow permissions to be disabled dynamically. It is not possible to
         * detect when that happens.
         *
         * http://review.cyanogenmod.com/#/c/4055/
         */

        return result;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PermissionUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
