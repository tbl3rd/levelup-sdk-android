/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.os.Parcel;
import android.os.Parcelable;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * A test utility class that tests the {@link Parcelable} interface of a {@link Parcelable}. This
 * simulates round-tripping the parcel system, marshalling down to bytes and back again.
 */
public final class ParcelTestUtils {

    /**
     * Run the given Parcelable through the parceling system. This writes the {@link Parcelable} to
     * a {@link Parcel}, marshals the {@link Parcel} into an in-memory byte array, then unmarshals
     * it into a new {@link Parcel}, which it reads the original input back from.
     *
     * @param <T> type of object to test.
     * @param parcelable the input data.
     * @return a copy of {@code input}, fed through the {@link Parcel} system. Ideally, this should
     *         be the same as {@code input} and not {@code null}.
     */
    @Nullable
    public static <T extends Parcelable> T feedThroughParceling(@NonNull final T parcelable) {
        // Stuff the Parcelable into a byte array.
        final Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(parcelable, 0);
        final byte[] marshalled = parcel.marshall();
        parcel.recycle();

        // Then unpack it again.
        final Parcel parcel2 = Parcel.obtain();
        parcel2.unmarshall(marshalled, 0, marshalled.length);
        parcel2.setDataPosition(0);
        final T result = parcel2.readParcelable(parcelable.getClass().getClassLoader());

        parcel2.recycle();

        return result;
    }

    /**
     * Asserts that the given Parcelable can pass through parceling/unparceling and is equal to the
     * input Parcelable object.
     *
     * @param parcelable the input Parcelable object.
     */
    public static <T extends Parcelable> void
            assertParcelableRoundtrips(@NonNull final T parcelable) {
        final T result = feedThroughParceling(parcelable);
        AndroidTestCase.assertNotNull("resulting Parcelable was null. ", result); //$NON-NLS-1$
        AndroidTestCase.assertEquals("resulting Parcelable differs. ", parcelable, result); //$NON-NLS-1$
    }

    private ParcelTestUtils() {
        // Do nothing.
    }
}
