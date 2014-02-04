/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Value;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.NullUtils;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Representing a location of a merchant (which can have multiple) on the server.
 */
@Immutable
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class Location implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
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
     * URL key for Foodler.
     */
    public static final String URL_FOODLER = "foodler"; //$NON-NLS-1$

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
    public Location(@Nullable final Set<Integer> categories,
            @Nullable final String extendedAddress, @Nullable final String hours, final long id,
            final double latitude, final double longitude, @Nullable final String locality,
            final long merchantId, @Nullable final String merchantName,
            @Nullable final String name, @Nullable final String phone,
            @Nullable final String postalCode, @Nullable final String region, final boolean shown,
            @Nullable final String streetAddress, @Nullable final Map<String, String> urls) {

        if (null == categories) {
            // Collections' emptySet is immutable.
            this.categories = NullUtils.nonNullContract(Collections.<Integer> emptySet());
        } else {
            this.categories =
                    NullUtils.nonNullContract(Collections.<Integer> unmodifiableSet(categories));
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
        this.urls =
                NullUtils.nonNullContract(Collections
                        .unmodifiableMap(null == urls ? new HashMap<String, String>(0) : urls));
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
        ((LocationCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
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
            final Set<Integer> categories = (Set<Integer>) source.readSerializable();
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
            final Map<String, String> urls = (Map<String, String>) source.readSerializable();

            return new Location(categories, extendedAddress, hours, id, lat, lng, locality,
                    merchantId, merchantName, name, phone, postalCode, region, shown,
                    streetAddress, urls);
        }

        /**
         * Parcel writer.
         * 
         * @param dest the destination parcel.
         * @param flags flags.
         * @param location the location to persist in the parcel.
         */
        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Location location) {
            dest.writeSerializable((Serializable) location.getCategories());
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
            dest.writeByte(location.isShown() ? (byte) 0x1 : (byte) 0x0);
            dest.writeString(location.getStreetAddress());
            dest.writeSerializable((Serializable) location.urls);
        }
    }
}
