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
        return String.format(Locale.US, "PercentageTip(value=%s)", getValue());
    }

    /**
     * Ensures {@link #getValue()} returns a value that can be encoded.
     */
    private void checkRep() {
        checkRep(MAXIMUM_VALUE_WITH_OFFSET_DECIMAL - MINIMUM_VALUE_WITH_OFFSET_DECIMAL);
    }
}
