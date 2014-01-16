/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.provider.Settings;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * This is a utility class to uniquely determine the identity of the device.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class DeviceIdentifier {
    /**
     * Gets the device ID for this device.
     * <p>
     * Although this method won't generally return null, returning null is possible if the device is
     * buggy.
     *
     * @param context the Application context.
     * @return {@link String} representing the device's unique ID.
     */
    @Nullable
    public static String getDeviceId(@NonNull final Context context) {
        PreconditionUtil.assertNotNull(context, "context"); //$NON-NLS-1$

        /*
         * NOTE: In the past, some buggy API level 8 devices would all return the same ANDROID_ID.
         * Since this library project requires a newer API level, this bug should no longer be an
         * issue.
         */
        final String deviceId =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        LogManager.v("Device ID is %s", deviceId); //$NON-NLS-1$

        return deviceId;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private DeviceIdentifier() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
