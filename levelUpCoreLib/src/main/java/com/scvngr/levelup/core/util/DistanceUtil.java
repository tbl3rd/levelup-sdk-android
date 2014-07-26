/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

/**
 * Utilities to convert distances to other units.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class DistanceUtil {
    private static final float FOOT_IN_METERS = 0.3048f;
    private static final float KILOMETER_IN_METERS = 1000.0f;
    private static final float MILE_IN_METERS = 1609.344f;

    /**
     * Converts a distance in meters to the desired unit.
     *
     * @param distanceInMeters the input distance, in meters.
     * @param desiredUnit the unit to return.
     * @return the distance in meters converted to the desired unit.
     */
    public static float convertDistance(final float distanceInMeters,
            @NonNull final DistanceUnit desiredUnit) {
        float result;

        switch (desiredUnit) {
            case KILOMETER:
                result = distanceInMeters / KILOMETER_IN_METERS;
                break;
            case METER:
                result = distanceInMeters;
                break;
            case MILE:
                result = distanceInMeters / MILE_IN_METERS;
                break;
            case FOOT:
                result = distanceInMeters / FOOT_IN_METERS;
                break;
            default:
                // If this is thrown, it's a programming error as all cases should be handled.
                throw new IllegalArgumentException("unknown distance unit");
        }

        return result;
    }

    /**
     * Rounds the distance to the upper 0.5. E.g. 0.6 will return 1.0 and 0.3 will return 0.5.
     *
     * @param distance input distance.
     * @return the distance, rounded to the upper 0.5.
     */
    public static float roundDistance(final float distance) {
        return (float) (Math.ceil(distance * 2.0f) / 2.0f);
    }

    /**
     * Common distance units.
     */
    public static enum DistanceUnit {
        /**
         * Metric meter.
         */
        @NonNull
        METER,
        /**
         * Metric kilometer.
         */
        @NonNull
        KILOMETER,
        /**
         * Imperial mile (standard).
         */
        @NonNull
        MILE,
        /**
         * Imperial foot.
         */
        @NonNull
        FOOT
    }

    private DistanceUtil() {
        throw new UnsupportedOperationException("this class is non-instantiable");
    }
}
