// Generated by delombok at Wed Sep 18 09:35:28 EDT 2013
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
 * Represents an error from the server.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class Error implements Parcelable {
	
	/**
	 * Creator for parceling.
	 */
	@NonNull
	public static final Creator<Error> CREATOR = new ErrorCreator();
	
	/**
	 * The message for the error that occurred.
	 */
	@NonNull
	private final String message;
	
	/**
	 * The object that this error is for. This object is scoped to the context
	 * of the web service.
	 */
	@Nullable
	private final String object;
	
	/**
	 * The property of the object that this error is for.
	 */
	@Nullable
	private final String property;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		((ErrorCreator)CREATOR).writeToParcel(dest, flags, this);
	}
	
	/**
	 * Creator for creating {@link Error}s from parcels as well as writing {@link Error}s to
	 * parcels.
	 */
	@Immutable
	private static final class ErrorCreator implements Creator<Error> {
		
		
		@Override
		public Error[] newArray(final int size) {
			return new Error[size];
		}
		
		@NonNull
		@Override
		public Error createFromParcel(final Parcel in) {
			final String message = in.readString();
			final String object = in.readString();
			final String property = in.readString();
			return new Error(message, object, property);
		}
		
		private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Error error) {
			dest.writeString(error.getMessage());
			dest.writeString(error.getObject());
			dest.writeString(error.getProperty());
		}
	}
	
	/**
	 * The message for the error that occurred.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * The object that this error is for. This object is scoped to the context
	 * of the web service.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getObject() {
		return this.object;
	}
	
	/**
	 * The property of the object that this error is for.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getProperty() {
		return this.property;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof Error)) return false;
		final Error other = (Error)o;
		final java.lang.Object this$message = this.getMessage();
		final java.lang.Object other$message = other.getMessage();
		if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
		final java.lang.Object this$object = this.getObject();
		final java.lang.Object other$object = other.getObject();
		if (this$object == null ? other$object != null : !this$object.equals(other$object)) return false;
		final java.lang.Object this$property = this.getProperty();
		final java.lang.Object other$property = other.getProperty();
		if (this$property == null ? other$property != null : !this$property.equals(other$property)) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final java.lang.Object $message = this.getMessage();
		result = result * PRIME + ($message == null ? 0 : $message.hashCode());
		final java.lang.Object $object = this.getObject();
		result = result * PRIME + ($object == null ? 0 : $object.hashCode());
		final java.lang.Object $property = this.getProperty();
		result = result * PRIME + ($property == null ? 0 : $property.hashCode());
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Error(message=" + this.getMessage() + ", object=" + this.getObject() + ", property=" + this.getProperty() + ")";
	}
	
	@java.lang.SuppressWarnings("all")
	public Error(@NonNull final String message, @Nullable final String object, @Nullable final String property) {
		
		if (message == null) {
			throw new java.lang.NullPointerException("message");
		}
		this.message = message;
		this.object = object;
		this.property = property;
	}
}