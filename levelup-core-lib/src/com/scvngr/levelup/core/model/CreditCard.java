// Generated by delombok at Fri Sep 27 13:34:02 EDT 2013
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
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Model representing a Credit Card.
 */
@Immutable
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
		((CreditCardCreator)CREATOR).writeToParcel(dest, flags, this);
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
			final Long bin = (Long)source.readValue(Long.class.getClassLoader());
			return new CreditCard(description, expirationMonth, expirationYear, id, last4, promoted, type, bin);
		}
		
		/**
		 * Write the {@link CreditCard} to a parcel.
		 *
		 * @param dest the {@link Parcel} to write to.
		 * @param flag the flags to use to write with.
		 * @param card the object to write to the parcel.
		 */
		private void writeToParcel(@NonNull final Parcel dest, final int flag, @NonNull final CreditCard card) {
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
	
	/**
	 * The description of the card.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * The month that this card expires. In the format "MM", so January would return "01".
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getExpirationMonth() {
		return this.expirationMonth;
	}
	
	/**
	 * The year that this card expires. In the format "yyyy", i.e. "2012".
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getExpirationYear() {
		return this.expirationYear;
	}
	
	/**
	 * The web service ID of this credit card.
	 */
	@java.lang.SuppressWarnings("all")
	public long getId() {
		return this.id;
	}
	
	/**
	 * The last 4 digits of the card number.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getLast4() {
		return this.last4;
	}
	
	/**
	 * If this card is the user's main card or not.
	 */
	@java.lang.SuppressWarnings("all")
	public boolean isPromoted() {
		return this.promoted;
	}
	
	/**
	 * The type of card that this is.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getType() {
		return this.type;
	}
	
	/**
	 * The BIN of the card.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public Long getBin() {
		return this.bin;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof CreditCard)) return false;
		final CreditCard other = (CreditCard)o;
		final java.lang.Object this$description = this.getDescription();
		final java.lang.Object other$description = other.getDescription();
		if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
		final java.lang.Object this$expirationMonth = this.getExpirationMonth();
		final java.lang.Object other$expirationMonth = other.getExpirationMonth();
		if (this$expirationMonth == null ? other$expirationMonth != null : !this$expirationMonth.equals(other$expirationMonth)) return false;
		final java.lang.Object this$expirationYear = this.getExpirationYear();
		final java.lang.Object other$expirationYear = other.getExpirationYear();
		if (this$expirationYear == null ? other$expirationYear != null : !this$expirationYear.equals(other$expirationYear)) return false;
		if (this.getId() != other.getId()) return false;
		final java.lang.Object this$last4 = this.getLast4();
		final java.lang.Object other$last4 = other.getLast4();
		if (this$last4 == null ? other$last4 != null : !this$last4.equals(other$last4)) return false;
		if (this.isPromoted() != other.isPromoted()) return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
		final java.lang.Object this$bin = this.getBin();
		final java.lang.Object other$bin = other.getBin();
		if (this$bin == null ? other$bin != null : !this$bin.equals(other$bin)) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final java.lang.Object $description = this.getDescription();
		result = result * PRIME + ($description == null ? 0 : $description.hashCode());
		final java.lang.Object $expirationMonth = this.getExpirationMonth();
		result = result * PRIME + ($expirationMonth == null ? 0 : $expirationMonth.hashCode());
		final java.lang.Object $expirationYear = this.getExpirationYear();
		result = result * PRIME + ($expirationYear == null ? 0 : $expirationYear.hashCode());
		final long $id = this.getId();
		result = result * PRIME + (int)($id >>> 32 ^ $id);
		final java.lang.Object $last4 = this.getLast4();
		result = result * PRIME + ($last4 == null ? 0 : $last4.hashCode());
		result = result * PRIME + (this.isPromoted() ? 1231 : 1237);
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 0 : $type.hashCode());
		final java.lang.Object $bin = this.getBin();
		result = result * PRIME + ($bin == null ? 0 : $bin.hashCode());
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "CreditCard(description=" + this.getDescription() + ", expirationMonth=" + this.getExpirationMonth() + ", expirationYear=" + this.getExpirationYear() + ", id=" + this.getId() + ", last4=" + this.getLast4() + ", promoted=" + this.isPromoted() + ", type=" + this.getType() + ", bin=" + this.getBin() + ")";
	}
	
	@java.lang.SuppressWarnings("all")
	public CreditCard(@Nullable final String description, @Nullable final String expirationMonth, @Nullable final String expirationYear, final long id, @Nullable final String last4, final boolean promoted, @Nullable final String type, @Nullable final Long bin) {
		
		this.description = description;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.id = id;
		this.last4 = last4;
		this.promoted = promoted;
		this.type = type;
		this.bin = bin;
	}
}