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
 * Model representing a Credit Card.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class CreditCard implements Parcelable {

    /**
     * Creator for parcels.
     */
    public static final Creator<CreditCard> CREATOR = new CreditCardCreator();

    /**
     * Full brand name of American Express.
     */
    public static final String AMEX = "American Express"; //$NON-NLS-1$

    /**
     * Full brand name of Discover.
     */
    public static final String DISCOVER = "Discover"; //$NON-NLS-1$

    /**
     * Full brand name of MasterCard.
     */
    public static final String MASTERCARD = "MasterCard"; //$NON-NLS-1$

    /**
     * Full brand name of Visa.
     */
    public static final String VISA = "Visa"; //$NON-NLS-1$

    /**
     * The description of the card.
     */
    @Nullable
    private final String description;

    /**
     * The month that this card expires. In the format "MM", so January would return "01".
     */
    @Nullable
    private final String expirationMonth;

    /**
     * The year that this card expires. In the format "yyyy", i.e. "2012".
     */
    @Nullable
    private final String expirationYear;

    /**
     * The web service ID of this credit card.
     */
    private final long id;

    /**
     * The last 4 digits of the card number.
     */
    @Nullable
    private final String last4;

    /**
     * If this card is the user's main card or not.
     */
    private final boolean promoted;

    /**
     * The type of card that this is.
     */
    @Nullable
    private final String type;

    /**
     * The BIN of the card.
     */
    @Nullable
    private final Long bin;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((CreditCardCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    /**
     * Class to parcel/unparcel {@link CreditCard} objects.
     */
    @Immutable
    private static final class CreditCardCreator implements Creator<CreditCard> {
        @Override
        public CreditCard[] newArray(final int size) {
            return new CreditCard[size];
        }

        @Override
        public CreditCard createFromParcel(final Parcel source) {
            final String description = source.readString();
            final String expirationMonth = source.readString();
            final String expirationYear = source.readString();
            final long id = source.readLong();
            final String last4 = source.readString();
            final boolean promoted = source.readInt() == 1;
            final String type = source.readString();
            final Long bin = (Long) source.readValue(Long.class.getClassLoader());

            return new CreditCard(description, expirationMonth, expirationYear, id, last4,
                    promoted, type, bin);
        }

        /**
         * Write the {@link CreditCard} to a parcel.
         *
         * @param dest the {@link Parcel} to write to.
         * @param flag the flags to use to write with.
         * @param card the object to write to the parcel.
         */
        private void writeToParcel(@NonNull final Parcel dest, final int flag,
                @NonNull final CreditCard card) {
            dest.writeString(card.getDescription());
            dest.writeString(card.getExpirationMonth());
            dest.writeString(card.getExpirationYear());
            dest.writeLong(card.getId());
            dest.writeString(card.getLast4());

            if (card.isPromoted()) {
                dest.writeInt(1);
            } else {
                dest.writeInt(0);
            }

            dest.writeString(card.getType());
            dest.writeValue(card.getBin());
        }
    }
}
