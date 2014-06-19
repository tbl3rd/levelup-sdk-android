/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.tip;

import android.os.Parcel;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.Locale;

/**
 * Tip as a percentage of the purchase amount.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class PercentageTip extends Tip<PercentageTip> {

    /**
     * Implements the {@link android.os.Parcelable} interface.
     */
    public static final Creator<PercentageTip> CREATOR = new Creator<PercentageTip>() {
        @NonNull
        @Override
        public PercentageTip createFromParcel(final Parcel in) {
            return new PercentageTip(NullUtils.nonNullContract(in));
        }

        @NonNull
        @Override
        public PercentageTip[] newArray(final int size) {
            return new PercentageTip[size];
        }
    };

    /**
     * First encoded tip type starts at 0.
     */
    /* package */static final int MINIMUM_VALUE_WITH_OFFSET_DECIMAL = 0;

    /**
     * ZZ in base 36.
     */
    /* package */static final int MAXIMUM_VALUE_WITH_OFFSET_DECIMAL = 1295;

    /**
     * @param value the value of the tip
     */
    public PercentageTip(final int value) {
        super(value);

        checkRep();
    }

    /**
     * @param parcel the Parcel to construct from
     */
    public PercentageTip(@NonNull final Parcel parcel) {
        super(parcel);

        checkRep();
    }

    @Override
    public int getEncodedValue() {
        return MINIMUM_VALUE_WITH_OFFSET_DECIMAL + getValue();
    }

    @NonNull
    @Override
    public PercentageTip withValue(final int value) {
        return new PercentageTip(value);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "PercentageTip(value=%s)", getValue()); //$NON-NLS-1$
    }

    /**
     * Ensures {@link #getValue()} returns a value that can be encoded.
     */
    private void checkRep() {
        checkRep(MAXIMUM_VALUE_WITH_OFFSET_DECIMAL - MINIMUM_VALUE_WITH_OFFSET_DECIMAL);
    }
}
