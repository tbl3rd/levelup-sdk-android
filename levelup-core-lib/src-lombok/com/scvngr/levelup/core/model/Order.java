/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Builder;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.MonetaryValue;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents an order.
 */
@Immutable
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class Order implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    public static final Creator<Order> CREATOR = new OrderCreator();

    /**
     * The total spent after credit was applied (i.e.: how much was charged to the user's card).
     */
    @Nullable
    private final MonetaryValue balanceAmount;

    /**
     * The time the bundle containing this order closed (or null if it's still open).
     */
    @Nullable
    private final String bundleClosedAt;

    /**
     * The descriptor (or the best guess at) that this order will show up as on the user's payment
     * statement.
     */
    @Nullable
    private final String bundleDescriptor;

    /**
     * The amount contributed to the user's chosen contribution target.
     */
    @Nullable
    private final MonetaryValue contributionAmount;

    /**
     * The name of the user's chosen contribution target.
     */
    @Nullable
    private final String contributionTargetName;

    /**
     * The date and time the order was created in the database. See {@link #transactedAt} for the
     * date and time the order was placed.
     */
    @NonNull
    private final String createdAt;

    /**
     * The sum of all credit used to fund the order.
     */
    @Nullable
    private final MonetaryValue creditAppliedAmount;

    /**
     * Credit earned by this purchase (e.g. the user unlocked loyalty).
     */
    @Nullable
    private final MonetaryValue creditEarnedAmount;

    /**
     * The order's {@link Location}'s extended address.
     */
    @Nullable
    private final String locationExtendedAddress;

    /**
     * The locality (city) of this order's {@link Location}.
     */
    @Nullable
    private final String locationLocality;

    /**
     * The name of the order's location, if different from the merchant name.
     */
    @Nullable
    private final String locationName;

    /**
     * The postal code for this order's {@link Location}.
     */
    @Nullable
    private final String locationPostalCode;

    /**
     * The region (state in the US) for this order's {@link Location}.
     */
    @Nullable
    private final String locationRegion;

    /**
     * The address of this order's {@link Location}.
     */
    @Nullable
    private final String locationStreetAddress;

    /**
     * The web service ID of the {@link Location} where the order was placed.
     */
    @Nullable
    private final Long locationWebServiceId;

    /**
     * The name of the merchant where the order took place.
     */
    @Nullable
    private final String merchantName;

    /**
     * The web service ID of the merchant where this order took place.
     */
    @Nullable
    private final Long merchantWebServiceId;

    /**
     * The timestamp when the order was refunded (if it was refunded).
     */
    @Nullable
    private final String refundedAt;

    /**
     * The amount of the transaction before tip.
     */
    @Nullable
    private final MonetaryValue spendAmount;

    /**
     * The amount the user tipped.
     */
    @Nullable
    private final MonetaryValue tipAmount;

    /**
     * Total amount (spend+tip) before any credit was applied.
     */
    @Nullable
    private final MonetaryValue totalAmount;

    /**
     * The date and time the user placed the order.
     */
    @Nullable
    private final String transactedAt;

    /**
     * The globally-unique ID (UUID) for this order.
     */
    @NonNull
    private final String uuid;

    /**
     * Implements parceling/unparceling for {@link Order}.
     */
    @Immutable
    private static final class OrderCreator implements Creator<Order> {

        @Override
        public Order[] newArray(final int size) {
            return new Order[size];
        }

        @Override
        @NonNull
        public Order createFromParcel(final Parcel in) {
            final ClassLoader loader = getClass().getClassLoader();

            final OrderBuilder builder = Order.builder();

            builder.balanceAmount((MonetaryValue) in.readParcelable(loader));
            builder.bundleClosedAt(in.readString());
            builder.bundleDescriptor(in.readString());
            builder.contributionAmount((MonetaryValue) in.readParcelable(loader));
            builder.contributionTargetName(in.readString());
            builder.createdAt(in.readString());
            builder.creditAppliedAmount((MonetaryValue) in.readParcelable(loader));
            builder.creditEarnedAmount((MonetaryValue) in.readParcelable(loader));
            builder.locationExtendedAddress(in.readString());
            builder.locationLocality(in.readString());
            builder.locationName(in.readString());
            builder.locationPostalCode(in.readString());
            builder.locationRegion(in.readString());
            builder.locationStreetAddress(in.readString());
            builder.locationWebServiceId((Long) in.readValue(Long.class.getClassLoader()));
            builder.merchantName(in.readString());
            builder.merchantWebServiceId((Long) in.readValue(Long.class.getClassLoader()));
            builder.refundedAt(in.readString());
            builder.spendAmount((MonetaryValue) in.readParcelable(loader));
            builder.tipAmount((MonetaryValue) in.readParcelable(loader));
            builder.totalAmount((MonetaryValue) in.readParcelable(loader));
            builder.transactedAt(in.readString());
            builder.uuid(in.readString());

            return builder.build();
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Order order) {
            dest.writeParcelable(order.balanceAmount, flags);
            dest.writeString(order.bundleClosedAt);
            dest.writeString(order.bundleDescriptor);
            dest.writeParcelable(order.contributionAmount, flags);
            dest.writeString(order.contributionTargetName);
            dest.writeString(order.createdAt);
            dest.writeParcelable(order.creditAppliedAmount, flags);
            dest.writeParcelable(order.creditEarnedAmount, flags);
            dest.writeString(order.locationExtendedAddress);
            dest.writeString(order.locationLocality);
            dest.writeString(order.locationName);
            dest.writeString(order.locationPostalCode);
            dest.writeString(order.locationRegion);
            dest.writeString(order.locationStreetAddress);
            dest.writeValue(order.locationWebServiceId);
            dest.writeString(order.merchantName);
            dest.writeValue(order.merchantWebServiceId);
            dest.writeString(order.refundedAt);
            dest.writeParcelable(order.spendAmount, flags);
            dest.writeParcelable(order.tipAmount, flags);
            dest.writeParcelable(order.totalAmount, flags);
            dest.writeString(order.transactedAt);
            dest.writeString(order.uuid);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((OrderCreator) CREATOR).writeToParcel(dest, flags, this);
    }
}
