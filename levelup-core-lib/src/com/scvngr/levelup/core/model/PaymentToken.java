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
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a user's payment token.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class PaymentToken implements Parcelable {
	
	/**
	 * Implements the {@link Parcelable} interface.
	 */
	public static final Creator<PaymentToken> CREATOR = new PaymentCodeCreator();
	
	/**
	 * The payment token data.
	 */
	@NonNull
	private final String data;
	
	/**
	 * The web service ID of the token.
	 */
	private final long id;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		((PaymentCodeCreator)CREATOR).writeToParcel(dest, flags, this);
	}
	
	/**
	 * Class to parcel/unparcel {@link PaymentToken} objects.
	 */
	private static final class PaymentCodeCreator implements Creator<PaymentToken> {
		
		
		@Override
		public PaymentToken createFromParcel(final Parcel source) {
			final String data = source.readString();
			final long id = source.readLong();
			return new PaymentToken(data, id);
		}
		
		@Override
		public PaymentToken[] newArray(final int size) {
			return new PaymentToken[size];
		}
		
		public void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final PaymentToken code) {
			dest.writeString(code.getData());
			dest.writeLong(code.getId());
		}
	}
	
	/**
	 * The payment token data.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public String getData() {
		return this.data;
	}
	
	/**
	 * The web service ID of the token.
	 */
	@java.lang.SuppressWarnings("all")
	public long getId() {
		return this.id;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof PaymentToken)) return false;
		final PaymentToken other = (PaymentToken)o;
		final java.lang.Object this$data = this.getData();
		final java.lang.Object other$data = other.getData();
		if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
		if (this.getId() != other.getId()) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final java.lang.Object $data = this.getData();
		result = result * PRIME + ($data == null ? 0 : $data.hashCode());
		final long $id = this.getId();
		result = result * PRIME + (int)($id >>> 32 ^ $id);
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PaymentToken(data=" + this.getData() + ", id=" + this.getId() + ")";
	}
	
	@java.lang.SuppressWarnings("all")
	public PaymentToken(@NonNull final String data, final long id) {
		
		if (data == null) {
			throw new java.lang.NullPointerException("data");
		}
		this.data = data;
		this.id = id;
	}
}