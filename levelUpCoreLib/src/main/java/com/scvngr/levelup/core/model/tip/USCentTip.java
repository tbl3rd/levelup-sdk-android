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
package com.scvngr.levelup.core.model.tip;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.Locale;

/**
 * Tip as an amount of US cents.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class USCentTip extends Tip<USCentTip> {

    /**
     * Implements the {@link android.os.Parcelable} interface.
     */
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
