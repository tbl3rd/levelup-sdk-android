package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.pm.PackageManager;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

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
        PreconditionUtil.assertNotNull(context, "context"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(permissionName, "permissionName"); //$NON-NLS-1$

        final PackageManager pm = context.getPackageManager();
        final String packageName = context.getPackageName();

        boolean result = true;
        if (PackageManager.PERMISSION_DENIED == pm.checkPermission(permissionName, packageName)) {
            LogManager.w("Permission %s is not granted", permissionName); //$NON-NLS-1$
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
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
