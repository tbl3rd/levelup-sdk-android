// Generated by delombok at Mon Sep 09 14:46:10 EDT 2013
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.jcip.annotations.Immutable;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Representing a location of a merchant (which can have multiple) on the server.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class Location implements Parcelable {
	
	/**
	 * Implements the {@code Parcelable} interface.
	 */
	public static final Creator<Location> CREATOR = new LocationCreator();
	
	/**
	 * The categories that this {@link Location} is classified under.
	 */
	@NonNull
	private final Set<Integer> categories;
	
	/**
	 * The {@link Location}'s extended address.
	 */
	@Nullable
	private final String extendedAddress;
	
	/**
	 * The hours of operation for this {@link Location}.
	 */
	@Nullable
	private final String hours;
	
	/**
	 * The web service ID of this {@link Location}.
	 */
	private final long id;
	
	/**
	 * The latitude of this {@link Location}.
	 */
	private final double latitude;
	
	/**
	 * The longitude of this {@link Location}.
	 */
	private final double longitude;
	
	/**
	 * The locality (city) of this {@link Location}.
	 */
	@Nullable
	private final String locality;
	
	/**
	 * The web service ID of the location's merchant.
	 */
	private final long merchantId;
	
	/**
	 * The name of the location's merchant.
	 */
	@Nullable
	private final String merchantName;
	
	/**
	 * The phone number of this {@link Location}.
	 */
	@Nullable
	private final String phone;
	
	/**
	 * The postal code of this {@link Location}.
	 */
	@Nullable
	private final String postalCode;
	
	/**
	 * The region (state in the US) of this {@link Location}.
	 */
	@Nullable
	private final String region;
	
	/**
	 * Whether or not this location is to be shown to the user.
	 */
	private final boolean shown;
	
	/**
	 * The address of this {@link Location}.
	 */
	@Nullable
	private final String streetAddress;
	
	/**
	 * A map of URLs associated with this {@link Location}.
	 */
	@NonNull
	private final Map<String, String> urls;
	
	/**
	 * The name of the location, if different from the merchant name. This is usually a familiar
	 * name that would make this location distinct amongst a set of locations where the merchant
	 * name would otherwise be identical e.g. "North End", "Charles St."
	 */
	@Nullable
	private final String name;
	
	/**
	 * URL key for Yelp.
	 */
	public static final String URL_YELP = "yelp"; //$NON-NLS-1$
	
	/**
	 * URL key for OpenTable.
	 */
	public static final String URL_OPENTABLE = "opentable"; //$NON-NLS-1$
	
	/**
	 * URL key for Menu.
	 */
	public static final String URL_MENU = "menu"; //$NON-NLS-1$
	
	/**
	 * URL key for Twitter.
	 */
	public static final String URL_TWITTER = "twitter"; //$NON-NLS-1$
	
	/**
	 * URL key for a newsletter.
	 */
	public static final String URL_NEWSLETTER = "newsletter"; //$NON-NLS-1$
	
	/**
	 * URL key for Facebook.
	 */
	public static final String URL_FACEBOOK = "facebook"; //$NON-NLS-1$
	
	/**
	 * @param categories the categories that this {@link Location} is classified under. {@code null}
	 *        will turn into an empty set.
	 * @param extendedAddress the {@link Location}'s extended address.
	 * @param hours the hours of operation for this {@link Location}.
	 * @param id the ID on the web service of this {@link Location}.
	 * @param latitude the latitude of this {@link Location}.
	 * @param longitude the longitude of this {@link Location}.
	 * @param locality the locality (city) of this {@link Location}.
	 * @param merchantId the ID number of the location's merchant.
	 * @param merchantName The name of the location's merchant.
	 * @param name The name of the location, if different from the merchant name.
	 * @param phone the phone number for this {@link Location}.
	 * @param postalCode the postal code for this {@link Location}.
	 * @param region the region (state in the US) for this {@link Location}.
	 * @param shown whether or not this location is to be shown to the user.
	 * @param streetAddress the address of this {@link Location}.
	 * @param urls a map of URLs associated with this {@link Location}. This map will be made
	 *        unmodifiable.
	 */
	public Location(@Nullable final Set<Integer> categories, @Nullable final String extendedAddress, @Nullable final String hours, final long id, final double latitude, final double longitude, @Nullable final String locality, final long merchantId, @Nullable final String merchantName, @Nullable final String name, @Nullable final String phone, @Nullable final String postalCode, @Nullable final String region, final boolean shown, @Nullable final String streetAddress, @Nullable final Map<String, String> urls) {
		
		if (null == categories) {
			// Collections' emptySet is immutable.
			this.categories = Collections.emptySet();
		} else {
			this.categories = Collections.unmodifiableSet(categories);
		}
		this.extendedAddress = extendedAddress;
		this.hours = hours;
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.locality = locality;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.name = name;
		this.phone = phone;
		this.postalCode = postalCode;
		this.region = region;
		this.shown = shown;
		this.streetAddress = streetAddress;
		/*
         * An empty map is used to simplify parceling, so that a null value doesn't need to be
         * persisted separately from an empty hash map.
         */
		this.urls = Collections.unmodifiableMap(null == urls ? new HashMap<String, String>(0) : urls);
	}
	
	/**
	 * @param key the name of the given URL.
	 * @return a URL with the given key or {@code null} if there is no such mapping.
	 */
	@Nullable
	public String getUrl(@NonNull final String key) {
		return urls.get(key);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		((LocationCreator)CREATOR).writeToParcel(dest, flags, this);
	}
	
	/**
	 * Parcelable creator.
	 */
	@Immutable
	static class LocationCreator implements Creator<Location> {
		
		
		@Override
		public Location[] newArray(final int size) {
			return new Location[size];
		}
		
		@Override
		public Location createFromParcel(final Parcel source) {
			@SuppressWarnings("unchecked")
			final Set<Integer> categories = (Set<Integer>)source.readSerializable();
			final String extendedAddress = source.readString();
			final String hours = source.readString();
			final long id = source.readLong();
			final double lat = source.readDouble();
			final double lng = source.readDouble();
			final String locality = source.readString();
			final long merchantId = source.readLong();
			final String merchantName = source.readString();
			final String name = source.readString();
			final String phone = source.readString();
			final String postalCode = source.readString();
			final String region = source.readString();
			final boolean shown = source.readByte() != 0;
			final String streetAddress = source.readString();
			@SuppressWarnings("unchecked")
			final Map<String, String> urls = (Map<String, String>)source.readSerializable();
			return new Location(categories, extendedAddress, hours, id, lat, lng, locality, merchantId, merchantName, name, phone, postalCode, region, shown, streetAddress, urls);
		}
		
		/**
		 * Parcel writer.
		 * 
		 * @param dest the destination parcel.
		 * @param flags flags.
		 * @param location the location to persist in the parcel.
		 */
		private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Location location) {
			dest.writeSerializable((Serializable)location.getCategories());
			dest.writeString(location.getExtendedAddress());
			dest.writeString(location.getHours());
			dest.writeLong(location.getId());
			dest.writeDouble(location.getLatitude());
			dest.writeDouble(location.getLongitude());
			dest.writeString(location.getLocality());
			dest.writeLong(location.getMerchantId());
			dest.writeString(location.getMerchantName());
			dest.writeString(location.getName());
			dest.writeString(location.getPhone());
			dest.writeString(location.getPostalCode());
			dest.writeString(location.getRegion());
			dest.writeByte(location.isShown() ? (byte)1 : (byte)0);
			dest.writeString(location.getStreetAddress());
			dest.writeSerializable((Serializable)location.urls);
		}
	}
	
	/**
	 * The categories that this {@link Location} is classified under.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public Set<Integer> getCategories() {
		return this.categories;
	}
	
	/**
	 * The {@link Location}'s extended address.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getExtendedAddress() {
		return this.extendedAddress;
	}
	
	/**
	 * The hours of operation for this {@link Location}.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getHours() {
		return this.hours;
	}
	
	/**
	 * The web service ID of this {@link Location}.
	 */
	@java.lang.SuppressWarnings("all")
	public long getId() {
		return this.id;
	}
	
	/**
	 * The latitude of this {@link Location}.
	 */
	@java.lang.SuppressWarnings("all")
	public double getLatitude() {
		return this.latitude;
	}
	
	/**
	 * The longitude of this {@link Location}.
	 */
	@java.lang.SuppressWarnings("all")
	public double getLongitude() {
		return this.longitude;
	}
	
	/**
	 * The locality (city) of this {@link Location}.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getLocality() {
		return this.locality;
	}
	
	/**
	 * The web service ID of the location's merchant.
	 */
	@java.lang.SuppressWarnings("all")
	public long getMerchantId() {
		return this.merchantId;
	}
	
	/**
	 * The name of the location's merchant.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getMerchantName() {
		return this.merchantName;
	}
	
	/**
	 * The phone number of this {@link Location}.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getPhone() {
		return this.phone;
	}
	
	/**
	 * The postal code of this {@link Location}.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getPostalCode() {
		return this.postalCode;
	}
	
	/**
	 * The region (state in the US) of this {@link Location}.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getRegion() {
		return this.region;
	}
	
	/**
	 * Whether or not this location is to be shown to the user.
	 */
	@java.lang.SuppressWarnings("all")
	public boolean isShown() {
		return this.shown;
	}
	
	/**
	 * The address of this {@link Location}.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getStreetAddress() {
		return this.streetAddress;
	}
	
	/**
	 * A map of URLs associated with this {@link Location}.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public Map<String, String> getUrls() {
		return this.urls;
	}
	
	/**
	 * The name of the location, if different from the merchant name. This is usually a familiar
	 * name that would make this location distinct amongst a set of locations where the merchant
	 * name would otherwise be identical e.g. "North End", "Charles St."
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getName() {
		return this.name;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof Location)) return false;
		final Location other = (Location)o;
		final java.lang.Object this$categories = this.getCategories();
		final java.lang.Object other$categories = other.getCategories();
		if (this$categories == null ? other$categories != null : !this$categories.equals(other$categories)) return false;
		final java.lang.Object this$extendedAddress = this.getExtendedAddress();
		final java.lang.Object other$extendedAddress = other.getExtendedAddress();
		if (this$extendedAddress == null ? other$extendedAddress != null : !this$extendedAddress.equals(other$extendedAddress)) return false;
		final java.lang.Object this$hours = this.getHours();
		final java.lang.Object other$hours = other.getHours();
		if (this$hours == null ? other$hours != null : !this$hours.equals(other$hours)) return false;
		if (this.getId() != other.getId()) return false;
		if (java.lang.Double.compare(this.getLatitude(), other.getLatitude()) != 0) return false;
		if (java.lang.Double.compare(this.getLongitude(), other.getLongitude()) != 0) return false;
		final java.lang.Object this$locality = this.getLocality();
		final java.lang.Object other$locality = other.getLocality();
		if (this$locality == null ? other$locality != null : !this$locality.equals(other$locality)) return false;
		if (this.getMerchantId() != other.getMerchantId()) return false;
		final java.lang.Object this$merchantName = this.getMerchantName();
		final java.lang.Object other$merchantName = other.getMerchantName();
		if (this$merchantName == null ? other$merchantName != null : !this$merchantName.equals(other$merchantName)) return false;
		final java.lang.Object this$phone = this.getPhone();
		final java.lang.Object other$phone = other.getPhone();
		if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) return false;
		final java.lang.Object this$postalCode = this.getPostalCode();
		final java.lang.Object other$postalCode = other.getPostalCode();
		if (this$postalCode == null ? other$postalCode != null : !this$postalCode.equals(other$postalCode)) return false;
		final java.lang.Object this$region = this.getRegion();
		final java.lang.Object other$region = other.getRegion();
		if (this$region == null ? other$region != null : !this$region.equals(other$region)) return false;
		if (this.isShown() != other.isShown()) return false;
		final java.lang.Object this$streetAddress = this.getStreetAddress();
		final java.lang.Object other$streetAddress = other.getStreetAddress();
		if (this$streetAddress == null ? other$streetAddress != null : !this$streetAddress.equals(other$streetAddress)) return false;
		final java.lang.Object this$urls = this.getUrls();
		final java.lang.Object other$urls = other.getUrls();
		if (this$urls == null ? other$urls != null : !this$urls.equals(other$urls)) return false;
		final java.lang.Object this$name = this.getName();
		final java.lang.Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final java.lang.Object $categories = this.getCategories();
		result = result * PRIME + ($categories == null ? 0 : $categories.hashCode());
		final java.lang.Object $extendedAddress = this.getExtendedAddress();
		result = result * PRIME + ($extendedAddress == null ? 0 : $extendedAddress.hashCode());
		final java.lang.Object $hours = this.getHours();
		result = result * PRIME + ($hours == null ? 0 : $hours.hashCode());
		final long $id = this.getId();
		result = result * PRIME + (int)($id >>> 32 ^ $id);
		final long $latitude = java.lang.Double.doubleToLongBits(this.getLatitude());
		result = result * PRIME + (int)($latitude >>> 32 ^ $latitude);
		final long $longitude = java.lang.Double.doubleToLongBits(this.getLongitude());
		result = result * PRIME + (int)($longitude >>> 32 ^ $longitude);
		final java.lang.Object $locality = this.getLocality();
		result = result * PRIME + ($locality == null ? 0 : $locality.hashCode());
		final long $merchantId = this.getMerchantId();
		result = result * PRIME + (int)($merchantId >>> 32 ^ $merchantId);
		final java.lang.Object $merchantName = this.getMerchantName();
		result = result * PRIME + ($merchantName == null ? 0 : $merchantName.hashCode());
		final java.lang.Object $phone = this.getPhone();
		result = result * PRIME + ($phone == null ? 0 : $phone.hashCode());
		final java.lang.Object $postalCode = this.getPostalCode();
		result = result * PRIME + ($postalCode == null ? 0 : $postalCode.hashCode());
		final java.lang.Object $region = this.getRegion();
		result = result * PRIME + ($region == null ? 0 : $region.hashCode());
		result = result * PRIME + (this.isShown() ? 1231 : 1237);
		final java.lang.Object $streetAddress = this.getStreetAddress();
		result = result * PRIME + ($streetAddress == null ? 0 : $streetAddress.hashCode());
		final java.lang.Object $urls = this.getUrls();
		result = result * PRIME + ($urls == null ? 0 : $urls.hashCode());
		final java.lang.Object $name = this.getName();
		result = result * PRIME + ($name == null ? 0 : $name.hashCode());
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Location(categories=" + this.getCategories() + ", extendedAddress=" + this.getExtendedAddress() + ", hours=" + this.getHours() + ", id=" + this.getId() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", locality=" + this.getLocality() + ", merchantId=" + this.getMerchantId() + ", merchantName=" + this.getMerchantName() + ", phone=" + this.getPhone() + ", postalCode=" + this.getPostalCode() + ", region=" + this.getRegion() + ", shown=" + this.isShown() + ", streetAddress=" + this.getStreetAddress() + ", urls=" + this.getUrls() + ", name=" + this.getName() + ")";
	}
}