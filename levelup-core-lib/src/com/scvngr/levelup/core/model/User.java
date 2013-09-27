// Generated by delombok at Fri Sep 27 13:34:02 EDT 2013
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.jcip.annotations.Immutable;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.LogManager;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents the user.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class User implements Parcelable {
	
	/**
	 * Implements the {@link Parcelable} interface.
	 */
	@NonNull
	public static final Creator<User> CREATOR = new UserCreator();
	
	/**
	 * Represents selectable options for the user's gender (an optional field). If these ordinal
	 * values change any relevant label resources must also be updated.
	 */
	public enum Gender {
		
		/**
		 * A male-identified person.
		 */
		MALE,
		
		/**
		 * A female-identified person.
		 */
		FEMALE;
		
		
		/**
		 * @param genderString "male" or "female".
		 * @return {@link #MALE} or {@link #FEMALE}, or null if {@code genderString} was neither.
		 */
		@Nullable
		public static Gender forString(@Nullable final String genderString) {
			if (MALE.toString().equals(genderString)) {
				return MALE;
			} else if (FEMALE.toString().equals(genderString)) {
				return FEMALE;
			} else {
				if (null != genderString) {
					LogManager.v("Gender string %s was not recognized", genderString); //$NON-NLS-1$
				}
				return null;
			}
		}
		
		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.US);
		}
	}
	
	/**
	 * The timestamp when the user was born.
	 */
	@Nullable
	private final String bornAt;
	
	/**
	 * A map of custom attributes for the user.
	 */
	@Nullable
	private final Map<String, String> customAttributes;
	
	/**
	 * The user's email address.
	 */
	@Nullable
	private final String email;
	
	/**
	 * The user's first name.
	 */
	@Nullable
	private final String firstName;
	
	/**
	 * The user's gender.
	 */
	@Nullable
	private final Gender gender;
	
	/**
	 * The amount of global credit the user has.
	 */
	@Nullable
	private final MonetaryValue globalCredit;
	
	/**
	 * The user's web service ID.
	 */
	private final long id;
	
	/**
	 * True if the user is connected to Facebook. False if the user is not connected to Facebook.
	 */
	private final boolean isConnectedToFacebook;
	
	/**
	 * The user's last name.
	 */
	@Nullable
	private final String lastName;
	
	/**
	 * The number of merchants the user has visited. Parsed with a 0 default.
	 */
	private final int merchantsVisitedCount;
	
	/**
	 * The user's total number of orders placed on LevelUp. Parsed with a 0 default.
	 */
	private final int ordersCount;
	
	/**
	 * The time the user accepted the LevelUp terms and conditions.
	 */
	@Nullable
	@LevelUpApi(contract = Contract.INTERNAL)
	private final String termsAcceptedAt;
	
	/**
	 * The total amount the user has saved by using LevelUp.
	 */
	@Nullable
	private final MonetaryValue totalSavings;
	
	/**
	 * @param bornAt the timestamp when the user was born.
	 * @param customAttributes map of custom attributes for the user.
	 * @param email the user's email address.
	 * @param firstName the user's first name.
	 * @param gender the user's gender.
	 * @param globalCredit the amount of global credit the user has.
	 * @param id the user's web service ID.
	 * @param isConnectedToFacebook True if the user is connected to Facebook. False if the user is
	 *        not connected to Facebook.
	 * @param lastName the user's last name.
	 * @param merchantsVisitedCount the number of merchants the user has visited.
	 * @param ordersCount the user's total number of orders placed on LevelUp.
	 * @param termsAcceptedAt the time the user accepted the LevelUp terms and conditions.
	 * @param totalSavings the total amount the user has saved by using LevelUp.
	 */
	public User(@Nullable final String bornAt, @Nullable final Map<String, String> customAttributes, @Nullable final String email, @Nullable final String firstName, @Nullable final Gender gender, @Nullable final MonetaryValue globalCredit, final long id, final boolean isConnectedToFacebook, @Nullable final String lastName, final int merchantsVisitedCount, final int ordersCount, @Nullable final String termsAcceptedAt, @Nullable final MonetaryValue totalSavings) {
		
		this.bornAt = bornAt;
		if (null == customAttributes) {
			this.customAttributes = null;
		} else {
			this.customAttributes = Collections.unmodifiableMap(new HashMap<String, String>(customAttributes));
		}
		this.email = email;
		this.isConnectedToFacebook = isConnectedToFacebook;
		this.firstName = firstName;
		this.gender = gender;
		this.globalCredit = globalCredit;
		this.id = id;
		this.lastName = lastName;
		this.merchantsVisitedCount = merchantsVisitedCount;
		this.ordersCount = ordersCount;
		this.termsAcceptedAt = termsAcceptedAt;
		this.totalSavings = totalSavings;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		((UserCreator)CREATOR).writeToParcel(dest, flags, this);
	}
	
	/**
	 * Class to parcel/unparcel {@link User} objects.
	 */
	@Immutable
	private static class UserCreator implements Creator<User> {
		
		
		@NonNull
		@Override
		public User createFromParcel(final Parcel source) {
			final String bornAt = source.readString();
			@SuppressWarnings("unchecked")
			final Map<String, String> customAttributes = (Map<String, String>)source.readSerializable();
			final String email = source.readString();
			final boolean isConnectedToFacebook = 1 == source.readInt();
			final String firstName = source.readString();
			final Gender gender = Gender.forString(source.readString());
			final MonetaryValue globalCredit = source.readParcelable(MonetaryValue.class.getClassLoader());
			final long id = source.readLong();
			final String lastName = source.readString();
			final int merchantsVisitedCount = source.readInt();
			final int ordersCount = source.readInt();
			final String termsAcceptedAt = source.readString();
			final MonetaryValue totalSavings = source.readParcelable(MonetaryValue.class.getClassLoader());
			return new User(bornAt, customAttributes, email, firstName, gender, globalCredit, id, isConnectedToFacebook, lastName, merchantsVisitedCount, ordersCount, termsAcceptedAt, totalSavings);
		}
		
		@Override
		public User[] newArray(final int size) {
			return new User[size];
		}
		
		private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final User user) {
			dest.writeString(user.getBornAt());
			dest.writeSerializable((Serializable)user.getCustomAttributes());
			dest.writeString(user.getEmail());
			dest.writeInt(user.isConnectedToFacebook() ? 1 : 0);
			dest.writeString(user.getFirstName());
			dest.writeString(null != user.getGender() ? user.getGender().toString() : null);
			dest.writeParcelable(user.getGlobalCredit(), flags);
			dest.writeLong(user.getId());
			dest.writeString(user.getLastName());
			dest.writeInt(user.getMerchantsVisitedCount());
			dest.writeInt(user.getOrdersCount());
			dest.writeString(user.getTermsAcceptedAt());
			dest.writeParcelable(user.getTotalSavings(), flags);
		}
	}
	
	/**
	 * The timestamp when the user was born.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getBornAt() {
		return this.bornAt;
	}
	
	/**
	 * A map of custom attributes for the user.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public Map<String, String> getCustomAttributes() {
		return this.customAttributes;
	}
	
	/**
	 * The user's email address.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * The user's first name.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * The user's gender.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public Gender getGender() {
		return this.gender;
	}
	
	/**
	 * The amount of global credit the user has.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getGlobalCredit() {
		return this.globalCredit;
	}
	
	/**
	 * The user's web service ID.
	 */
	@java.lang.SuppressWarnings("all")
	public long getId() {
		return this.id;
	}
	
	/**
	 * True if the user is connected to Facebook. False if the user is not connected to Facebook.
	 */
	@java.lang.SuppressWarnings("all")
	public boolean isConnectedToFacebook() {
		return this.isConnectedToFacebook;
	}
	
	/**
	 * The user's last name.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getLastName() {
		return this.lastName;
	}
	
	/**
	 * The number of merchants the user has visited. Parsed with a 0 default.
	 */
	@java.lang.SuppressWarnings("all")
	public int getMerchantsVisitedCount() {
		return this.merchantsVisitedCount;
	}
	
	/**
	 * The user's total number of orders placed on LevelUp. Parsed with a 0 default.
	 */
	@java.lang.SuppressWarnings("all")
	public int getOrdersCount() {
		return this.ordersCount;
	}
	
	/**
	 * The time the user accepted the LevelUp terms and conditions.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getTermsAcceptedAt() {
		return this.termsAcceptedAt;
	}
	
	/**
	 * The total amount the user has saved by using LevelUp.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getTotalSavings() {
		return this.totalSavings;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) return false;
		final User other = (User)o;
		final java.lang.Object this$bornAt = this.getBornAt();
		final java.lang.Object other$bornAt = other.getBornAt();
		if (this$bornAt == null ? other$bornAt != null : !this$bornAt.equals(other$bornAt)) return false;
		final java.lang.Object this$customAttributes = this.getCustomAttributes();
		final java.lang.Object other$customAttributes = other.getCustomAttributes();
		if (this$customAttributes == null ? other$customAttributes != null : !this$customAttributes.equals(other$customAttributes)) return false;
		final java.lang.Object this$email = this.getEmail();
		final java.lang.Object other$email = other.getEmail();
		if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
		final java.lang.Object this$firstName = this.getFirstName();
		final java.lang.Object other$firstName = other.getFirstName();
		if (this$firstName == null ? other$firstName != null : !this$firstName.equals(other$firstName)) return false;
		final java.lang.Object this$gender = this.getGender();
		final java.lang.Object other$gender = other.getGender();
		if (this$gender == null ? other$gender != null : !this$gender.equals(other$gender)) return false;
		final java.lang.Object this$globalCredit = this.getGlobalCredit();
		final java.lang.Object other$globalCredit = other.getGlobalCredit();
		if (this$globalCredit == null ? other$globalCredit != null : !this$globalCredit.equals(other$globalCredit)) return false;
		if (this.getId() != other.getId()) return false;
		if (this.isConnectedToFacebook() != other.isConnectedToFacebook()) return false;
		final java.lang.Object this$lastName = this.getLastName();
		final java.lang.Object other$lastName = other.getLastName();
		if (this$lastName == null ? other$lastName != null : !this$lastName.equals(other$lastName)) return false;
		if (this.getMerchantsVisitedCount() != other.getMerchantsVisitedCount()) return false;
		if (this.getOrdersCount() != other.getOrdersCount()) return false;
		final java.lang.Object this$termsAcceptedAt = this.getTermsAcceptedAt();
		final java.lang.Object other$termsAcceptedAt = other.getTermsAcceptedAt();
		if (this$termsAcceptedAt == null ? other$termsAcceptedAt != null : !this$termsAcceptedAt.equals(other$termsAcceptedAt)) return false;
		final java.lang.Object this$totalSavings = this.getTotalSavings();
		final java.lang.Object other$totalSavings = other.getTotalSavings();
		if (this$totalSavings == null ? other$totalSavings != null : !this$totalSavings.equals(other$totalSavings)) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final java.lang.Object $bornAt = this.getBornAt();
		result = result * PRIME + ($bornAt == null ? 0 : $bornAt.hashCode());
		final java.lang.Object $customAttributes = this.getCustomAttributes();
		result = result * PRIME + ($customAttributes == null ? 0 : $customAttributes.hashCode());
		final java.lang.Object $email = this.getEmail();
		result = result * PRIME + ($email == null ? 0 : $email.hashCode());
		final java.lang.Object $firstName = this.getFirstName();
		result = result * PRIME + ($firstName == null ? 0 : $firstName.hashCode());
		final java.lang.Object $gender = this.getGender();
		result = result * PRIME + ($gender == null ? 0 : $gender.hashCode());
		final java.lang.Object $globalCredit = this.getGlobalCredit();
		result = result * PRIME + ($globalCredit == null ? 0 : $globalCredit.hashCode());
		final long $id = this.getId();
		result = result * PRIME + (int)($id >>> 32 ^ $id);
		result = result * PRIME + (this.isConnectedToFacebook() ? 1231 : 1237);
		final java.lang.Object $lastName = this.getLastName();
		result = result * PRIME + ($lastName == null ? 0 : $lastName.hashCode());
		result = result * PRIME + this.getMerchantsVisitedCount();
		result = result * PRIME + this.getOrdersCount();
		final java.lang.Object $termsAcceptedAt = this.getTermsAcceptedAt();
		result = result * PRIME + ($termsAcceptedAt == null ? 0 : $termsAcceptedAt.hashCode());
		final java.lang.Object $totalSavings = this.getTotalSavings();
		result = result * PRIME + ($totalSavings == null ? 0 : $totalSavings.hashCode());
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "User(bornAt=" + this.getBornAt() + ", customAttributes=" + this.getCustomAttributes() + ", email=" + this.getEmail() + ", firstName=" + this.getFirstName() + ", gender=" + this.getGender() + ", globalCredit=" + this.getGlobalCredit() + ", id=" + this.getId() + ", isConnectedToFacebook=" + this.isConnectedToFacebook() + ", lastName=" + this.getLastName() + ", merchantsVisitedCount=" + this.getMerchantsVisitedCount() + ", ordersCount=" + this.getOrdersCount() + ", termsAcceptedAt=" + this.getTermsAcceptedAt() + ", totalSavings=" + this.getTotalSavings() + ")";
	}
}