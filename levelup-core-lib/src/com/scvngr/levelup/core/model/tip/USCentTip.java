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
 * Tip as an amount of US cents.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class USCentTip extends Tip<USCentTip> {

    public static final Creator<USCentTip> CREATOR = new Creator<USCentTip>() {
        @NonNull
        @Override
        public USCentTip createFromParcel(final Parcel in) {
            return new USCentTip(NullUtils.nonNullContract(in));
        }

        @NonNull
        @Override
        public USCentTip[] newArray(final int size) {
            return new USCentTip[size];
        }
    };

    /**
     * Encoding comes after {@link PercentageTip}.
     */
    /* package */static final int MINIMUM_VALUE_WITH_OFFSET_DECIMAL =
            PercentageTip.MAXIMUM_VALUE_WITH_OFFSET_DECIMAL + 1;

    /**
     * ZZZ in base 36.
     */
    /* package */static final int MAXIMUM_VALUE_WITH_OFFSET_DECIMAL = 46655;

    /**
     * @param value the value of the tip
     */
    public USCentTip(final int value) {
        super(value);

        checkRep();
    }

    /**
     * @param parcel the Parcel to construct from
     */
    public USCentTip(@NonNull final Parcel parcel) {
        super(parcel);

        checkRep();
    }

    @Override
    public int getEncodedValue() {
        return MINIMUM_VALUE_WITH_OFFSET_DECIMAL + getValue();
    }

    @NonNull
    @Override
    public USCentTip withValue(final int value) {
        return new USCentTip(value);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "USCentTip(value=%s)", getValue());
    }

    /**
     * Ensures {@link #getValue()} returns a value that can be encoded.
     */
    private void checkRep() {
        checkRep(MAXIMUM_VALUE_WITH_OFFSET_DECIMAL - MINIMUM_VALUE_WITH_OFFSET_DECIMAL);
    }
}
