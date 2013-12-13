package com.scvngr.levelup.core.util;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

/**
 * Utilities to convert distances to other units.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class DistanceUtil {

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
                result = distanceInMeters / 1000.0f;
                break;
            case METER:
                result = distanceInMeters;
                break;
            case MILE:
                result = distanceInMeters / 1609.344f;
                break;
            case FOOT:
                result = distanceInMeters / 0.3048f;
                break;
            default:
                // If this is thrown, it's a programming error as all cases should be handled.
                throw new IllegalArgumentException("unknown distance unit"); //$NON-NLS-1$
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
        throw new UnsupportedOperationException("this class is non-instantiable"); //$NON-NLS-1$
    }
}
