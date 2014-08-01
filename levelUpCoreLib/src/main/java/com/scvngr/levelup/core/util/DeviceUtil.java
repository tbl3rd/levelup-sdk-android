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
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;

/**
 * Utility class for methods pertaining to the user's device.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class DeviceUtil {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DENSITY_1X = "1";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DENSITY_1_5X = "1.5";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DENSITY_2X = "2";

    /**
     * Gets the string representing the device's screen density. This is mapped into the commonly
     * used "@1x" "@1.5x" and "@2x" notation.
     *
     * @param context the Application context.
     * @return the string representation of the device's screen density in the "@1,1.5,2x" notation.
     */
    public static String getDeviceDensityString(@NonNull final Context context) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        String density;

        if (DisplayMetrics.DENSITY_HIGH == metrics.densityDpi) {
            // High density is the only special case.
            density = DENSITY_1_5X;
        } else if (DisplayMetrics.DENSITY_HIGH > metrics.densityDpi) {
            // Anything less than high density returns 1x.
            density = DENSITY_1X;
        } else {
            // Anything greater than high density returns 2x.
            density = DENSITY_2X;
        }

        return density;
    }

    private DeviceUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable.");
    }
}
