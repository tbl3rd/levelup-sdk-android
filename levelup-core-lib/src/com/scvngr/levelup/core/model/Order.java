// Generated by delombok at Wed Feb 26 18:03:01 EST 2014
/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import net.jcip.annotations.Immutable;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.NullUtils;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents an order.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class Order implements Parcelable {
    
    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
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
            builder.balanceAmount((MonetaryValue)in.readParcelable(loader));
            builder.bundleClosedAt(in.readString());
            builder.bundleDescriptor(in.readString());
            builder.contributionAmount((MonetaryValue)in.readParcelable(loader));
            builder.contributionTargetName(in.readString());
            builder.createdAt(in.readString());
            builder.creditAppliedAmount((MonetaryValue)in.readParcelable(loader));
            builder.creditEarnedAmount((MonetaryValue)in.readParcelable(loader));
            builder.locationExtendedAddress(in.readString());
            builder.locationLocality(in.readString());
            builder.locationName(in.readString());
            builder.locationPostalCode(in.readString());
            builder.locationRegion(in.readString());
            builder.locationStreetAddress(in.readString());
            builder.locationWebServiceId((Long)in.readValue(Long.class.getClassLoader()));
            builder.merchantName(in.readString());
            builder.merchantWebServiceId((Long)in.readValue(Long.class.getClassLoader()));
            builder.refundedAt(in.readString());
            builder.spendAmount((MonetaryValue)in.readParcelable(loader));
            builder.tipAmount((MonetaryValue)in.readParcelable(loader));
            builder.totalAmount((MonetaryValue)in.readParcelable(loader));
            builder.transactedAt(in.readString());
            builder.uuid(in.readString());
            return NullUtils.nonNullContract(builder.build());
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Order order) {
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
        ((OrderCreator)CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    @SuppressWarnings("all")
    public static class OrderBuilder {
        private MonetaryValue balanceAmount;
        private String bundleClosedAt;
        private String bundleDescriptor;
        private MonetaryValue contributionAmount;
        private String contributionTargetName;
        private String createdAt;
        private MonetaryValue creditAppliedAmount;
        private MonetaryValue creditEarnedAmount;
        private String locationExtendedAddress;
        private String locationLocality;
        private String locationName;
        private String locationPostalCode;
        private String locationRegion;
        private String locationStreetAddress;
        private Long locationWebServiceId;
        private String merchantName;
        private Long merchantWebServiceId;
        private String refundedAt;
        private MonetaryValue spendAmount;
        private MonetaryValue tipAmount;
        private MonetaryValue totalAmount;
        private String transactedAt;
        private String uuid;

        @SuppressWarnings("all")
        OrderBuilder() {
        }

        @SuppressWarnings("all")
        public OrderBuilder balanceAmount(final MonetaryValue balanceAmount) {
            this.balanceAmount = balanceAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder bundleClosedAt(final String bundleClosedAt) {
            this.bundleClosedAt = bundleClosedAt;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder bundleDescriptor(final String bundleDescriptor) {
            this.bundleDescriptor = bundleDescriptor;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder contributionAmount(final MonetaryValue contributionAmount) {
            this.contributionAmount = contributionAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder contributionTargetName(final String contributionTargetName) {
            this.contributionTargetName = contributionTargetName;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder createdAt(final String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder creditAppliedAmount(final MonetaryValue creditAppliedAmount) {
            this.creditAppliedAmount = creditAppliedAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder creditEarnedAmount(final MonetaryValue creditEarnedAmount) {
            this.creditEarnedAmount = creditEarnedAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationExtendedAddress(final String locationExtendedAddress) {
            this.locationExtendedAddress = locationExtendedAddress;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationLocality(final String locationLocality) {
            this.locationLocality = locationLocality;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationName(final String locationName) {
            this.locationName = locationName;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationPostalCode(final String locationPostalCode) {
            this.locationPostalCode = locationPostalCode;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationRegion(final String locationRegion) {
            this.locationRegion = locationRegion;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationStreetAddress(final String locationStreetAddress) {
            this.locationStreetAddress = locationStreetAddress;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder locationWebServiceId(final Long locationWebServiceId) {
            this.locationWebServiceId = locationWebServiceId;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder merchantName(final String merchantName) {
            this.merchantName = merchantName;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder merchantWebServiceId(final Long merchantWebServiceId) {
            this.merchantWebServiceId = merchantWebServiceId;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder refundedAt(final String refundedAt) {
            this.refundedAt = refundedAt;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder spendAmount(final MonetaryValue spendAmount) {
            this.spendAmount = spendAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder tipAmount(final MonetaryValue tipAmount) {
            this.tipAmount = tipAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder totalAmount(final MonetaryValue totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder transactedAt(final String transactedAt) {
            this.transactedAt = transactedAt;
            return this;
        }

        @SuppressWarnings("all")
        public OrderBuilder uuid(final String uuid) {
            this.uuid = uuid;
            return this;
        }

        @SuppressWarnings("all")
        public Order build() {
            return new Order(balanceAmount, bundleClosedAt, bundleDescriptor, contributionAmount, contributionTargetName, createdAt, creditAppliedAmount, creditEarnedAmount, locationExtendedAddress, locationLocality, locationName, locationPostalCode, locationRegion, locationStreetAddress, locationWebServiceId, merchantName, merchantWebServiceId, refundedAt, spendAmount, tipAmount, totalAmount, transactedAt, uuid);
        }

        @Override
        @SuppressWarnings("all")
        public String toString() {
            return "Order.OrderBuilder(balanceAmount=" + this.balanceAmount + ", bundleClosedAt=" + this.bundleClosedAt + ", bundleDescriptor=" + this.bundleDescriptor + ", contributionAmount=" + this.contributionAmount + ", contributionTargetName=" + this.contributionTargetName + ", createdAt=" + this.createdAt + ", creditAppliedAmount=" + this.creditAppliedAmount + ", creditEarnedAmount=" + this.creditEarnedAmount + ", locationExtendedAddress=" + this.locationExtendedAddress + ", locationLocality=" + this.locationLocality + ", locationName=" + this.locationName + ", locationPostalCode=" + this.locationPostalCode + ", locationRegion=" + this.locationRegion + ", locationStreetAddress=" + this.locationStreetAddress + ", locationWebServiceId=" + this.locationWebServiceId + ", merchantName=" + this.merchantName + ", merchantWebServiceId=" + this.merchantWebServiceId + ", refundedAt=" + this.refundedAt + ", spendAmount=" + this.spendAmount + ", tipAmount=" + this.tipAmount + ", totalAmount=" + this.totalAmount + ", transactedAt=" + this.transactedAt + ", uuid=" + this.uuid + ")";
        }
    }

    @SuppressWarnings("all")
    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    /**
     * The total spent after credit was applied (i.e.: how much was charged to the user's card).
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getBalanceAmount() {
        return this.balanceAmount;
    }

    /**
     * The time the bundle containing this order closed (or null if it's still open).
     */
    @Nullable
    @SuppressWarnings("all")
    public String getBundleClosedAt() {
        return this.bundleClosedAt;
    }

    /**
     * The descriptor (or the best guess at) that this order will show up as on the user's payment
     * statement.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getBundleDescriptor() {
        return this.bundleDescriptor;
    }

    /**
     * The amount contributed to the user's chosen contribution target.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getContributionAmount() {
        return this.contributionAmount;
    }

    /**
     * The name of the user's chosen contribution target.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getContributionTargetName() {
        return this.contributionTargetName;
    }

    /**
     * The date and time the order was created in the database. See {@link #transactedAt} for the
     * date and time the order was placed.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getCreatedAt() {
        return this.createdAt;
    }

    /**
     * The sum of all credit used to fund the order.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getCreditAppliedAmount() {
        return this.creditAppliedAmount;
    }

    /**
     * Credit earned by this purchase (e.g. the user unlocked loyalty).
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getCreditEarnedAmount() {
        return this.creditEarnedAmount;
    }

    /**
     * The order's {@link Location}'s extended address.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getLocationExtendedAddress() {
        return this.locationExtendedAddress;
    }

    /**
     * The locality (city) of this order's {@link Location}.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getLocationLocality() {
        return this.locationLocality;
    }

    /**
     * The name of the order's location, if different from the merchant name.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getLocationName() {
        return this.locationName;
    }

    /**
     * The postal code for this order's {@link Location}.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getLocationPostalCode() {
        return this.locationPostalCode;
    }

    /**
     * The region (state in the US) for this order's {@link Location}.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getLocationRegion() {
        return this.locationRegion;
    }

    /**
     * The address of this order's {@link Location}.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getLocationStreetAddress() {
        return this.locationStreetAddress;
    }

    /**
     * The web service ID of the {@link Location} where the order was placed.
     */
    @Nullable
    @SuppressWarnings("all")
    public Long getLocationWebServiceId() {
        return this.locationWebServiceId;
    }

    /**
     * The name of the merchant where the order took place.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getMerchantName() {
        return this.merchantName;
    }

    /**
     * The web service ID of the merchant where this order took place.
     */
    @Nullable
    @SuppressWarnings("all")
    public Long getMerchantWebServiceId() {
        return this.merchantWebServiceId;
    }

    /**
     * The timestamp when the order was refunded (if it was refunded).
     */
    @Nullable
    @SuppressWarnings("all")
    public String getRefundedAt() {
        return this.refundedAt;
    }

    /**
     * The amount of the transaction before tip.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getSpendAmount() {
        return this.spendAmount;
    }

    /**
     * The amount the user tipped.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getTipAmount() {
        return this.tipAmount;
    }

    /**
     * Total amount (spend+tip) before any credit was applied.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getTotalAmount() {
        return this.totalAmount;
    }

    /**
     * The date and time the user placed the order.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getTransactedAt() {
        return this.transactedAt;
    }

    /**
     * The globally-unique ID (UUID) for this order.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getUuid() {
        return this.uuid;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        final Order other = (Order)o;
        final Object this$balanceAmount = this.getBalanceAmount();
        final Object other$balanceAmount = other.getBalanceAmount();
        if (this$balanceAmount == null ? other$balanceAmount != null : !this$balanceAmount.equals(other$balanceAmount)) return false;
        final Object this$bundleClosedAt = this.getBundleClosedAt();
        final Object other$bundleClosedAt = other.getBundleClosedAt();
        if (this$bundleClosedAt == null ? other$bundleClosedAt != null : !this$bundleClosedAt.equals(other$bundleClosedAt)) return false;
        final Object this$bundleDescriptor = this.getBundleDescriptor();
        final Object other$bundleDescriptor = other.getBundleDescriptor();
        if (this$bundleDescriptor == null ? other$bundleDescriptor != null : !this$bundleDescriptor.equals(other$bundleDescriptor)) return false;
        final Object this$contributionAmount = this.getContributionAmount();
        final Object other$contributionAmount = other.getContributionAmount();
        if (this$contributionAmount == null ? other$contributionAmount != null : !this$contributionAmount.equals(other$contributionAmount)) return false;
        final Object this$contributionTargetName = this.getContributionTargetName();
        final Object other$contributionTargetName = other.getContributionTargetName();
        if (this$contributionTargetName == null ? other$contributionTargetName != null : !this$contributionTargetName.equals(other$contributionTargetName)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$creditAppliedAmount = this.getCreditAppliedAmount();
        final Object other$creditAppliedAmount = other.getCreditAppliedAmount();
        if (this$creditAppliedAmount == null ? other$creditAppliedAmount != null : !this$creditAppliedAmount.equals(other$creditAppliedAmount)) return false;
        final Object this$creditEarnedAmount = this.getCreditEarnedAmount();
        final Object other$creditEarnedAmount = other.getCreditEarnedAmount();
        if (this$creditEarnedAmount == null ? other$creditEarnedAmount != null : !this$creditEarnedAmount.equals(other$creditEarnedAmount)) return false;
        final Object this$locationExtendedAddress = this.getLocationExtendedAddress();
        final Object other$locationExtendedAddress = other.getLocationExtendedAddress();
        if (this$locationExtendedAddress == null ? other$locationExtendedAddress != null : !this$locationExtendedAddress.equals(other$locationExtendedAddress)) return false;
        final Object this$locationLocality = this.getLocationLocality();
        final Object other$locationLocality = other.getLocationLocality();
        if (this$locationLocality == null ? other$locationLocality != null : !this$locationLocality.equals(other$locationLocality)) return false;
        final Object this$locationName = this.getLocationName();
        final Object other$locationName = other.getLocationName();
        if (this$locationName == null ? other$locationName != null : !this$locationName.equals(other$locationName)) return false;
        final Object this$locationPostalCode = this.getLocationPostalCode();
        final Object other$locationPostalCode = other.getLocationPostalCode();
        if (this$locationPostalCode == null ? other$locationPostalCode != null : !this$locationPostalCode.equals(other$locationPostalCode)) return false;
        final Object this$locationRegion = this.getLocationRegion();
        final Object other$locationRegion = other.getLocationRegion();
        if (this$locationRegion == null ? other$locationRegion != null : !this$locationRegion.equals(other$locationRegion)) return false;
        final Object this$locationStreetAddress = this.getLocationStreetAddress();
        final Object other$locationStreetAddress = other.getLocationStreetAddress();
        if (this$locationStreetAddress == null ? other$locationStreetAddress != null : !this$locationStreetAddress.equals(other$locationStreetAddress)) return false;
        final Object this$locationWebServiceId = this.getLocationWebServiceId();
        final Object other$locationWebServiceId = other.getLocationWebServiceId();
        if (this$locationWebServiceId == null ? other$locationWebServiceId != null : !this$locationWebServiceId.equals(other$locationWebServiceId)) return false;
        final Object this$merchantName = this.getMerchantName();
        final Object other$merchantName = other.getMerchantName();
        if (this$merchantName == null ? other$merchantName != null : !this$merchantName.equals(other$merchantName)) return false;
        final Object this$merchantWebServiceId = this.getMerchantWebServiceId();
        final Object other$merchantWebServiceId = other.getMerchantWebServiceId();
        if (this$merchantWebServiceId == null ? other$merchantWebServiceId != null : !this$merchantWebServiceId.equals(other$merchantWebServiceId)) return false;
        final Object this$refundedAt = this.getRefundedAt();
        final Object other$refundedAt = other.getRefundedAt();
        if (this$refundedAt == null ? other$refundedAt != null : !this$refundedAt.equals(other$refundedAt)) return false;
        final Object this$spendAmount = this.getSpendAmount();
        final Object other$spendAmount = other.getSpendAmount();
        if (this$spendAmount == null ? other$spendAmount != null : !this$spendAmount.equals(other$spendAmount)) return false;
        final Object this$tipAmount = this.getTipAmount();
        final Object other$tipAmount = other.getTipAmount();
        if (this$tipAmount == null ? other$tipAmount != null : !this$tipAmount.equals(other$tipAmount)) return false;
        final Object this$totalAmount = this.getTotalAmount();
        final Object other$totalAmount = other.getTotalAmount();
        if (this$totalAmount == null ? other$totalAmount != null : !this$totalAmount.equals(other$totalAmount)) return false;
        final Object this$transactedAt = this.getTransactedAt();
        final Object other$transactedAt = other.getTransactedAt();
        if (this$transactedAt == null ? other$transactedAt != null : !this$transactedAt.equals(other$transactedAt)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 277;
        int result = 1;
        final Object $balanceAmount = this.getBalanceAmount();
        result = result * PRIME + ($balanceAmount == null ? 0 : $balanceAmount.hashCode());
        final Object $bundleClosedAt = this.getBundleClosedAt();
        result = result * PRIME + ($bundleClosedAt == null ? 0 : $bundleClosedAt.hashCode());
        final Object $bundleDescriptor = this.getBundleDescriptor();
        result = result * PRIME + ($bundleDescriptor == null ? 0 : $bundleDescriptor.hashCode());
        final Object $contributionAmount = this.getContributionAmount();
        result = result * PRIME + ($contributionAmount == null ? 0 : $contributionAmount.hashCode());
        final Object $contributionTargetName = this.getContributionTargetName();
        result = result * PRIME + ($contributionTargetName == null ? 0 : $contributionTargetName.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 0 : $createdAt.hashCode());
        final Object $creditAppliedAmount = this.getCreditAppliedAmount();
        result = result * PRIME + ($creditAppliedAmount == null ? 0 : $creditAppliedAmount.hashCode());
        final Object $creditEarnedAmount = this.getCreditEarnedAmount();
        result = result * PRIME + ($creditEarnedAmount == null ? 0 : $creditEarnedAmount.hashCode());
        final Object $locationExtendedAddress = this.getLocationExtendedAddress();
        result = result * PRIME + ($locationExtendedAddress == null ? 0 : $locationExtendedAddress.hashCode());
        final Object $locationLocality = this.getLocationLocality();
        result = result * PRIME + ($locationLocality == null ? 0 : $locationLocality.hashCode());
        final Object $locationName = this.getLocationName();
        result = result * PRIME + ($locationName == null ? 0 : $locationName.hashCode());
        final Object $locationPostalCode = this.getLocationPostalCode();
        result = result * PRIME + ($locationPostalCode == null ? 0 : $locationPostalCode.hashCode());
        final Object $locationRegion = this.getLocationRegion();
        result = result * PRIME + ($locationRegion == null ? 0 : $locationRegion.hashCode());
        final Object $locationStreetAddress = this.getLocationStreetAddress();
        result = result * PRIME + ($locationStreetAddress == null ? 0 : $locationStreetAddress.hashCode());
        final Object $locationWebServiceId = this.getLocationWebServiceId();
        result = result * PRIME + ($locationWebServiceId == null ? 0 : $locationWebServiceId.hashCode());
        final Object $merchantName = this.getMerchantName();
        result = result * PRIME + ($merchantName == null ? 0 : $merchantName.hashCode());
        final Object $merchantWebServiceId = this.getMerchantWebServiceId();
        result = result * PRIME + ($merchantWebServiceId == null ? 0 : $merchantWebServiceId.hashCode());
        final Object $refundedAt = this.getRefundedAt();
        result = result * PRIME + ($refundedAt == null ? 0 : $refundedAt.hashCode());
        final Object $spendAmount = this.getSpendAmount();
        result = result * PRIME + ($spendAmount == null ? 0 : $spendAmount.hashCode());
        final Object $tipAmount = this.getTipAmount();
        result = result * PRIME + ($tipAmount == null ? 0 : $tipAmount.hashCode());
        final Object $totalAmount = this.getTotalAmount();
        result = result * PRIME + ($totalAmount == null ? 0 : $totalAmount.hashCode());
        final Object $transactedAt = this.getTransactedAt();
        result = result * PRIME + ($transactedAt == null ? 0 : $transactedAt.hashCode());
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 0 : $uuid.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "Order(balanceAmount=" + this.getBalanceAmount() + ", bundleClosedAt=" + this.getBundleClosedAt() + ", bundleDescriptor=" + this.getBundleDescriptor() + ", contributionAmount=" + this.getContributionAmount() + ", contributionTargetName=" + this.getContributionTargetName() + ", createdAt=" + this.getCreatedAt() + ", creditAppliedAmount=" + this.getCreditAppliedAmount() + ", creditEarnedAmount=" + this.getCreditEarnedAmount() + ", locationExtendedAddress=" + this.getLocationExtendedAddress() + ", locationLocality=" + this.getLocationLocality() + ", locationName=" + this.getLocationName() + ", locationPostalCode=" + this.getLocationPostalCode() + ", locationRegion=" + this.getLocationRegion() + ", locationStreetAddress=" + this.getLocationStreetAddress() + ", locationWebServiceId=" + this.getLocationWebServiceId() + ", merchantName=" + this.getMerchantName() + ", merchantWebServiceId=" + this.getMerchantWebServiceId() + ", refundedAt=" + this.getRefundedAt() + ", spendAmount=" + this.getSpendAmount() + ", tipAmount=" + this.getTipAmount() + ", totalAmount=" + this.getTotalAmount() + ", transactedAt=" + this.getTransactedAt() + ", uuid=" + this.getUuid() + ")";
    }

    @SuppressWarnings("all")
    public Order(@Nullable final MonetaryValue balanceAmount, @Nullable final String bundleClosedAt, @Nullable final String bundleDescriptor, @Nullable final MonetaryValue contributionAmount, @Nullable final String contributionTargetName, @NonNull final String createdAt, @Nullable final MonetaryValue creditAppliedAmount, @Nullable final MonetaryValue creditEarnedAmount, @Nullable final String locationExtendedAddress, @Nullable final String locationLocality, @Nullable final String locationName, @Nullable final String locationPostalCode, @Nullable final String locationRegion, @Nullable final String locationStreetAddress, @Nullable final Long locationWebServiceId, @Nullable final String merchantName, @Nullable final Long merchantWebServiceId, @Nullable final String refundedAt, @Nullable final MonetaryValue spendAmount, @Nullable final MonetaryValue tipAmount, @Nullable final MonetaryValue totalAmount, @Nullable final String transactedAt, @NonNull final String uuid) {
        if (createdAt == null) {
            throw new NullPointerException("createdAt");
        }
        if (uuid == null) {
            throw new NullPointerException("uuid");
        }
        this.balanceAmount = balanceAmount;
        this.bundleClosedAt = bundleClosedAt;
        this.bundleDescriptor = bundleDescriptor;
        this.contributionAmount = contributionAmount;
        this.contributionTargetName = contributionTargetName;
        this.createdAt = createdAt;
        this.creditAppliedAmount = creditAppliedAmount;
        this.creditEarnedAmount = creditEarnedAmount;
        this.locationExtendedAddress = locationExtendedAddress;
        this.locationLocality = locationLocality;
        this.locationName = locationName;
        this.locationPostalCode = locationPostalCode;
        this.locationRegion = locationRegion;
        this.locationStreetAddress = locationStreetAddress;
        this.locationWebServiceId = locationWebServiceId;
        this.merchantName = merchantName;
        this.merchantWebServiceId = merchantWebServiceId;
        this.refundedAt = refundedAt;
        this.spendAmount = spendAmount;
        this.tipAmount = tipAmount;
        this.totalAmount = totalAmount;
        this.transactedAt = transactedAt;
        this.uuid = uuid;
    }
}