/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.tip;

import android.os.Parcel;
import android.os.Parcelable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;

import java.util.Locale;

/**
 * Represents a tip as a percentage or as US cents for purchases.
 *
 * @param <T> the concrete type of Tip
 * @see com.scvngr.levelup.core.model.qr.PaymentPreferencesV3
 */
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public abstract class Tip<T extends Tip<T>> implements Parcelable {

    /**
     * The value of the tip.
     */
    private final int mValue;

    /**
     * @param value the value of the tip
     */
    public Tip(final int value) {
        mValue = value;
    }

    /**
     * @param parcel the Parcel to construct from
     */
    public Tip(@NonNull final Parcel parcel) {
        mValue = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mValue);
    }

    /**
     * Get the encoded tip value which may include an encoding offset.
     *
     * @return the encoded tip value
     */
    public abstract int getEncodedValue();

    /**
     * Get the value of the tip.
     *
     * @return the value of the tip
     */
    public final int getValue() {
        return mValue;
    }

    /**
     * Get a copy of the Tip with the specified tip value.
     *
     * @param value the value of the tip
     * @return a tip with the specified value
     */
    @NonNull
    public abstract T withValue(final int value);

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Tip that = (Tip) o;

        if (mValue != that.mValue) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return mValue;
    }

    /**
     * Ensures {@link #mValue} is a value that can be encoded.
     *
     * @param maximumValue the maximum value that can be encoded
     */
    /* package */void checkRep(final int maximumValue) {
        if (0 > mValue || maximumValue < mValue) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "value(%d) must be between 0 and %d", mValue, maximumValue));
        }
    }
}
