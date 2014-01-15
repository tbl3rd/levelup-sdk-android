/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a Loyalty progression at a merchant for a user on the server.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class Loyalty implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    public static final Creator<Loyalty> CREATOR = new LoyaltyCreator();

    /**
     * If {@link Loyalty} progression is enabled.
     */
    private final boolean isLoyaltyEnabled;

    /**
     * The web service ID of merchant for this loyalty.
     */
    @Nullable
    private final Long merchantWebServiceId;

    /**
     * The number of orders the user has at the merchant.
     */
    private final int ordersCount;

    /**
     * The potential credit the user can spend at the merchant.
     */
    @Nullable
    private final MonetaryValue potentialCredit;

    /**
     * The percentage of the user's progress to the next loyalty unlock.
     */
    private final int progressPercentage;

    /**
     * The total savings the user has had at the merchant.
     */
    @Nullable
    private final MonetaryValue savings;

    /**
     * The amount any user must spend to unlock loyalty at the merchant.
     */
    @Nullable
    private final MonetaryValue shouldSpend;

    /**
     * The amount the user still has to spend before unlocking loyalty.
     */
    @Nullable
    private final MonetaryValue spendRemaining;

    /**
     * The total amount the user has spent at the merchant.
     */
    @Nullable
    private final MonetaryValue totalVolume;

    /**
     * The amount the user will earn once they unlock the loyalty reward.
     */
    @Nullable
    private final MonetaryValue willEarn;

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((LoyaltyCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Class to parcel/unparcel {@link Loyalty} objects.
     */
    @Immutable
    private static class LoyaltyCreator implements Creator<Loyalty> {

        @Override
        @NonNull
        public Loyalty createFromParcel(final Parcel source) {
            final boolean isLoyaltyEnabled = 0 != source.readByte();
            final Long merchantWebServiceId = (Long) source.readValue(Long.class.getClassLoader());
            final int ordersCount = source.readInt();
            final MonetaryValue potentialCredit =
                    source.readParcelable(MonetaryValue.class.getClassLoader());
            final int progressPercent = source.readInt();
            final MonetaryValue savings =
                    source.readParcelable(MonetaryValue.class.getClassLoader());
            final MonetaryValue shouldSpend =
                    source.readParcelable(MonetaryValue.class.getClassLoader());
            final MonetaryValue spendRemaining =
                    source.readParcelable(MonetaryValue.class.getClassLoader());
            final MonetaryValue totalVolume =
                    source.readParcelable(MonetaryValue.class.getClassLoader());
            final MonetaryValue willEarn =
                    source.readParcelable(MonetaryValue.class.getClassLoader());

            return new Loyalty(isLoyaltyEnabled, merchantWebServiceId, ordersCount,
                    potentialCredit, progressPercent, savings, shouldSpend, spendRemaining,
                    totalVolume, willEarn);
        }

        @Override
        public Loyalty[] newArray(final int size) {
            return new Loyalty[size];
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Loyalty loyalty) {

            if (loyalty.isLoyaltyEnabled()) {
                dest.writeByte((byte) 1);
            } else {
                dest.writeByte((byte) 0);
            }

            dest.writeValue(loyalty.getMerchantWebServiceId());
            dest.writeInt(loyalty.getOrdersCount());
            dest.writeParcelable(loyalty.getPotentialCredit(), flags);
            dest.writeInt(loyalty.getProgressPercentage());
            dest.writeParcelable(loyalty.getSavings(), flags);
            dest.writeParcelable(loyalty.getShouldSpend(), flags);
            dest.writeParcelable(loyalty.getSpendRemaining(), flags);
            dest.writeParcelable(loyalty.getTotalVolume(), flags);
            dest.writeParcelable(loyalty.getWillEarn(), flags);
        }
    }
}
