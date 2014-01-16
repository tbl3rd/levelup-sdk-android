/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents some amount of money on the server.
 */
/*
 * The first rule of acquisition: "Once you have their money, you never give it back."
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class MonetaryValue implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    public static final Creator<MonetaryValue> CREATOR = new MonetaryValueCreator();

    /**
     * The currency code for USD.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    protected static final String USD_CODE = "usd"; //$NON-NLS-1$

    /**
     * The symbol for USD.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    protected static final String USD_SYMBOL = "$"; //$NON-NLS-1$

    /**
     * The amount (in cents) for this {@link MonetaryValue}.
     */
    private final long amount;

    /**
     * The raw currency code, such as "USD" or "EUR".
     */
    @NonNull
    private final String currencyCode;

    /**
     * The currency symbol (i.e.: "$" or "€").
     */
    @NonNull
    private final String currencySymbol;


    /**
     * Constructor. Assumes USD.
     *
     * @param amount the amount (in cents) for this {@link MonetaryValue}.
     */
    public MonetaryValue(final long amount) {
        this.amount = amount;
        this.currencyCode = USD_CODE;
        this.currencySymbol = USD_SYMBOL;
    }

    /**
     * Get the formatted currency amount with symbol, assuming standard US/European "symbol+amount"
     * formatting.
     *
     * @param context the Application context.
     * @return the amount formatted with the currency symbol and amount (e.g. "$20" or "$23.05")
     */
    @NonNull
    public String getFormattedAmountWithCurrencySymbol(@NonNull final Context context) {
        return getFormattedMoney(context, currencySymbol, amount);
    }

    /**
     * Get the formatted currency amount with symbol, assuming standard US/European "symbol+amount"
     * formatting; drop the last digits and decimal/comma if they're all 0s.
     *
     * @param context the Application context.
     * @return the amount formatted with the currency symbol and amount (e.g. "$20" or "$23.05")
     */
    @NonNull
    public String getFormattedCentStrippedAmountWithCurrencySymbol(@NonNull final Context context) {
        return getFormattedMoneyNoDecimal(context, currencySymbol, amount);
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((MonetaryValueCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Helper to format a long monetary value to a string. NOTE: this is not internationalized. It
     * assumes that the value is in "cents".
     *
     * @param context the Application context.
     * @param currencySymbol the symbol for the currency of the money.
     * @param amount the amount of money.
     * @return {@link String} with the formatted amount.
     */
    @NonNull
    public static String getFormattedMoney(@NonNull final Context context,
            @NonNull final String currencySymbol, final long amount) {
        return context.getString(R.string.levelup_monetary_value_format, currencySymbol,
                amount / 100f);
    }

    /**
     * Helper to format a monetary value to a string, but truncate the decimals. NOTE: this is not
     * internationalized. It assumes that the value is in "cents".
     *
     * @param context the Application context.
     * @param currencySymbol the symbol for the currency of the money.
     * @param amount the amount of money.
     * @return {@link String} with the formatted amount (with no decimal).
     */
    @NonNull
    public static String getFormattedMoneyNoDecimal(@NonNull final Context context,
            @NonNull final String currencySymbol, final long amount) {
        return context.getString(R.string.levelup_monetary_value_no_decimal_format, currencySymbol,
                Math.round(amount / 100f));
    }

    /**
     * Class to parcel/unparcel {@link MonetaryValue} objects.
     */
    @Immutable
    private static class MonetaryValueCreator implements Creator<MonetaryValue> {

        @Override
        public MonetaryValue createFromParcel(final Parcel in) {
            final long amount = in.readLong();
            final String currencyCode = in.readString();
            final String currencySymbol = in.readString();

            return new MonetaryValue(amount, currencyCode, currencySymbol);
        }

        @Override
        public MonetaryValue[] newArray(final int size) {
            return new MonetaryValue[size];
        }

        /**
         * Write the {@link MonetaryValue} to a parcel.
         *
         * @param dest the {@link Parcel} to write to.
         * @param flag the flags to use to write with.
         * @param money the object to write to the parcel.
         */
        private void writeToParcel(@NonNull final Parcel dest, final int flag,
                @NonNull final MonetaryValue money) {
            dest.writeLong(money.getAmount());
            dest.writeString(money.getCurrencyCode());
            dest.writeString(money.getCurrencySymbol());
        }
    }
}
