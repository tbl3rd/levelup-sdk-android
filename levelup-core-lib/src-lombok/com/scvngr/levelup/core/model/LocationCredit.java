/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.jcip.annotations.Immutable;
import lombok.Value;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.NullUtils;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a credit of a {@link Location} on the server.
 */

@Immutable
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class LocationCredit implements Parcelable {

    /**
     * The amount of credit the merchant has credited.
     */
    @Nullable
    private final MonetaryValue merchantFundedCreditValue;

    /**
     * The total amount of credit.
     */
    @NonNull
    private final MonetaryValue totalAmountValue;

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
    public static final Creator<LocationCredit> CREATOR = new LocationCreditCreator();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((LocationCreditCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags,
                this);
    }

    @Immutable
    private static final class LocationCreditCreator implements Creator<LocationCredit> {

        @Override
        public LocationCredit[] newArray(final int size) {
            return new LocationCredit[size];
        }

        @Override
        public LocationCredit createFromParcel(final Parcel in) {
            final MonetaryValue merchantCreditValue =
                    in.readParcelable(MonetaryValue.class.getClassLoader());
            final MonetaryValue totalValue =
                    in.readParcelable(MonetaryValue.class.getClassLoader());

            return new LocationCredit(merchantCreditValue, NullUtils.nonNullContract(totalValue));
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final LocationCredit locationCredit) {
            dest.writeParcelable(locationCredit.getMerchantFundedCreditValue(), flags);
            dest.writeParcelable(locationCredit.getTotalAmountValue(), flags);
        }
    }

    /**
     * @param merchantFundedCreditValue the available merchant credit
     * @param totalAmountValue the total credit available
     */
    public LocationCredit(@Nullable final MonetaryValue merchantFundedCreditValue,
            @NonNull final MonetaryValue totalAmountValue) {
        this.merchantFundedCreditValue = merchantFundedCreditValue;
        this.totalAmountValue = totalAmountValue;
    }
}
