// Generated by delombok at Fri Sep 27 13:34:02 EDT 2013
/**
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
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a Loyalty progression at a merchant for a user on the server.
 */
@Immutable
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
		((LoyaltyCreator)CREATOR).writeToParcel(dest, flags, this);
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
			final Long merchantWebServiceId = (Long)source.readValue(Long.class.getClassLoader());
			final int ordersCount = source.readInt();
			final MonetaryValue potentialCredit = source.readParcelable(MonetaryValue.class.getClassLoader());
			final int progressPercent = source.readInt();
			final MonetaryValue savings = source.readParcelable(MonetaryValue.class.getClassLoader());
			final MonetaryValue shouldSpend = source.readParcelable(MonetaryValue.class.getClassLoader());
			final MonetaryValue spendRemaining = source.readParcelable(MonetaryValue.class.getClassLoader());
			final MonetaryValue totalVolume = source.readParcelable(MonetaryValue.class.getClassLoader());
			final MonetaryValue willEarn = source.readParcelable(MonetaryValue.class.getClassLoader());
			return new Loyalty(isLoyaltyEnabled, merchantWebServiceId, ordersCount, potentialCredit, progressPercent, savings, shouldSpend, spendRemaining, totalVolume, willEarn);
		}
		
		@Override
		public Loyalty[] newArray(final int size) {
			return new Loyalty[size];
		}
		
		private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Loyalty loyalty) {
			if (loyalty.isLoyaltyEnabled()) {
				dest.writeByte((byte)1);
			} else {
				dest.writeByte((byte)0);
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
	
	/**
	 * If {@link Loyalty} progression is enabled.
	 */
	@java.lang.SuppressWarnings("all")
	public boolean isLoyaltyEnabled() {
		return this.isLoyaltyEnabled;
	}
	
	/**
	 * The web service ID of merchant for this loyalty.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public Long getMerchantWebServiceId() {
		return this.merchantWebServiceId;
	}
	
	/**
	 * The number of orders the user has at the merchant.
	 */
	@java.lang.SuppressWarnings("all")
	public int getOrdersCount() {
		return this.ordersCount;
	}
	
	/**
	 * The potential credit the user can spend at the merchant.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getPotentialCredit() {
		return this.potentialCredit;
	}
	
	/**
	 * The percentage of the user's progress to the next loyalty unlock.
	 */
	@java.lang.SuppressWarnings("all")
	public int getProgressPercentage() {
		return this.progressPercentage;
	}
	
	/**
	 * The total savings the user has had at the merchant.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getSavings() {
		return this.savings;
	}
	
	/**
	 * The amount any user must spend to unlock loyalty at the merchant.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getShouldSpend() {
		return this.shouldSpend;
	}
	
	/**
	 * The amount the user still has to spend before unlocking loyalty.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getSpendRemaining() {
		return this.spendRemaining;
	}
	
	/**
	 * The total amount the user has spent at the merchant.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getTotalVolume() {
		return this.totalVolume;
	}
	
	/**
	 * The amount the user will earn once they unlock the loyalty reward.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getWillEarn() {
		return this.willEarn;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof Loyalty)) return false;
		final Loyalty other = (Loyalty)o;
		if (this.isLoyaltyEnabled() != other.isLoyaltyEnabled()) return false;
		final java.lang.Object this$merchantWebServiceId = this.getMerchantWebServiceId();
		final java.lang.Object other$merchantWebServiceId = other.getMerchantWebServiceId();
		if (this$merchantWebServiceId == null ? other$merchantWebServiceId != null : !this$merchantWebServiceId.equals(other$merchantWebServiceId)) return false;
		if (this.getOrdersCount() != other.getOrdersCount()) return false;
		final java.lang.Object this$potentialCredit = this.getPotentialCredit();
		final java.lang.Object other$potentialCredit = other.getPotentialCredit();
		if (this$potentialCredit == null ? other$potentialCredit != null : !this$potentialCredit.equals(other$potentialCredit)) return false;
		if (this.getProgressPercentage() != other.getProgressPercentage()) return false;
		final java.lang.Object this$savings = this.getSavings();
		final java.lang.Object other$savings = other.getSavings();
		if (this$savings == null ? other$savings != null : !this$savings.equals(other$savings)) return false;
		final java.lang.Object this$shouldSpend = this.getShouldSpend();
		final java.lang.Object other$shouldSpend = other.getShouldSpend();
		if (this$shouldSpend == null ? other$shouldSpend != null : !this$shouldSpend.equals(other$shouldSpend)) return false;
		final java.lang.Object this$spendRemaining = this.getSpendRemaining();
		final java.lang.Object other$spendRemaining = other.getSpendRemaining();
		if (this$spendRemaining == null ? other$spendRemaining != null : !this$spendRemaining.equals(other$spendRemaining)) return false;
		final java.lang.Object this$totalVolume = this.getTotalVolume();
		final java.lang.Object other$totalVolume = other.getTotalVolume();
		if (this$totalVolume == null ? other$totalVolume != null : !this$totalVolume.equals(other$totalVolume)) return false;
		final java.lang.Object this$willEarn = this.getWillEarn();
		final java.lang.Object other$willEarn = other.getWillEarn();
		if (this$willEarn == null ? other$willEarn != null : !this$willEarn.equals(other$willEarn)) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + (this.isLoyaltyEnabled() ? 1231 : 1237);
		final java.lang.Object $merchantWebServiceId = this.getMerchantWebServiceId();
		result = result * PRIME + ($merchantWebServiceId == null ? 0 : $merchantWebServiceId.hashCode());
		result = result * PRIME + this.getOrdersCount();
		final java.lang.Object $potentialCredit = this.getPotentialCredit();
		result = result * PRIME + ($potentialCredit == null ? 0 : $potentialCredit.hashCode());
		result = result * PRIME + this.getProgressPercentage();
		final java.lang.Object $savings = this.getSavings();
		result = result * PRIME + ($savings == null ? 0 : $savings.hashCode());
		final java.lang.Object $shouldSpend = this.getShouldSpend();
		result = result * PRIME + ($shouldSpend == null ? 0 : $shouldSpend.hashCode());
		final java.lang.Object $spendRemaining = this.getSpendRemaining();
		result = result * PRIME + ($spendRemaining == null ? 0 : $spendRemaining.hashCode());
		final java.lang.Object $totalVolume = this.getTotalVolume();
		result = result * PRIME + ($totalVolume == null ? 0 : $totalVolume.hashCode());
		final java.lang.Object $willEarn = this.getWillEarn();
		result = result * PRIME + ($willEarn == null ? 0 : $willEarn.hashCode());
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Loyalty(isLoyaltyEnabled=" + this.isLoyaltyEnabled() + ", merchantWebServiceId=" + this.getMerchantWebServiceId() + ", ordersCount=" + this.getOrdersCount() + ", potentialCredit=" + this.getPotentialCredit() + ", progressPercentage=" + this.getProgressPercentage() + ", savings=" + this.getSavings() + ", shouldSpend=" + this.getShouldSpend() + ", spendRemaining=" + this.getSpendRemaining() + ", totalVolume=" + this.getTotalVolume() + ", willEarn=" + this.getWillEarn() + ")";
	}
	
	@java.lang.SuppressWarnings("all")
	public Loyalty(final boolean isLoyaltyEnabled, @Nullable final Long merchantWebServiceId, final int ordersCount, @Nullable final MonetaryValue potentialCredit, final int progressPercentage, @Nullable final MonetaryValue savings, @Nullable final MonetaryValue shouldSpend, @Nullable final MonetaryValue spendRemaining, @Nullable final MonetaryValue totalVolume, @Nullable final MonetaryValue willEarn) {
		
		this.isLoyaltyEnabled = isLoyaltyEnabled;
		this.merchantWebServiceId = merchantWebServiceId;
		this.ordersCount = ordersCount;
		this.potentialCredit = potentialCredit;
		this.progressPercentage = progressPercentage;
		this.savings = savings;
		this.shouldSpend = shouldSpend;
		this.spendRemaining = spendRemaining;
		this.totalVolume = totalVolume;
		this.willEarn = willEarn;
	}
}