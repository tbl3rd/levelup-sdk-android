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
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Value;

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
    @NonNull
    public static final Creator<CreditCard> CREATOR = new CreditCardCreator();

    /**
     * Full brand name of American Express.
     */
    @NonNull
    public static final String AMEX = "American Express";

    /**
     * Full brand name of Discover.
     */
    @NonNull
    public static final String DISCOVER = "Discover";

    /**
     * Full brand name of MasterCard.
     */
    @NonNull
    public static final String MASTERCARD = "MasterCard";

    /**
     * Full brand name of Visa.
     */
    @NonNull
    public static final String VISA = "Visa";

    /**
     * The BIN of the card.
     */
    @Nullable
    private final Long bin;

    /**
     * Whether this card is a debit card.
     *
     * @return True if the card is a debit card or unknown, false if it's a known credit card.
     */
    private final boolean debit;

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
     * @deprecated Provided for SDK backwards compatibility only. Newer code should use
     *             {@link com.scvngr.levelup.core.model.CreditCard#CreditCard(Long, boolean, String, String, String, long, String, boolean, String)}
     *             instead. This constructor omits the debit field and has the BIN field last.
     */
    @Deprecated
    @SuppressWarnings("all")
    public CreditCard(@Nullable final String description, @Nullable final String expirationMonth,
            @Nullable final String expirationYear, final long id, @Nullable final String last4,
            final boolean promoted, @Nullable final String type, @Nullable final Long bin) {
        this(bin, true, description, expirationMonth, expirationYear, id, last4, promoted, type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((CreditCardCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
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
            final Long bin = (Long) source.readValue(Long.class.getClassLoader());
            final boolean debit = source.readInt() == 1;
            final String description = source.readString();
            final String expirationMonth = source.readString();
            final String expirationYear = source.readString();
            final long id = source.readLong();
            final String last4 = source.readString();
            final boolean promoted = source.readInt() == 1;
            final String type = source.readString();

            return new CreditCard(bin, debit, description, expirationMonth, expirationYear, id,
                    last4, promoted, type);
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
            dest.writeValue(card.getBin());
            dest.writeInt(card.isDebit() ? 1 : 0);
            dest.writeString(card.getDescription());
            dest.writeString(card.getExpirationMonth());
            dest.writeString(card.getExpirationYear());
            dest.writeLong(card.getId());
            dest.writeString(card.getLast4());
            dest.writeInt(card.isPromoted() ? 1 : 0);
            dest.writeString(card.getType());
        }
    }
}
