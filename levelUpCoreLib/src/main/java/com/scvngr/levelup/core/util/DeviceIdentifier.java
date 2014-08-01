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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

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
        PreconditionUtil.assertNotNull(context, "context");

        /*
         * NOTE: In the past, some buggy API level 8 devices would all return the same ANDROID_ID.
         * Since this library project requires a newer API level, this bug should no longer be an
         * issue.
         */
        final String deviceId =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        LogManager.v("Device ID is %s", deviceId);

        return deviceId;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private DeviceIdentifier() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
