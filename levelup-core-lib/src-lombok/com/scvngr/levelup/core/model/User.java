/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Value;
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
@Value
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
    public User(@Nullable final String bornAt,
            @Nullable final Map<String, String> customAttributes, @Nullable final String email,
            @Nullable final String firstName, @Nullable final Gender gender,
            @Nullable final MonetaryValue globalCredit, final long id,
            final boolean isConnectedToFacebook, @Nullable final String lastName,
            final int merchantsVisitedCount, final int ordersCount,
            @Nullable final String termsAcceptedAt, @Nullable final MonetaryValue totalSavings) {
        this.bornAt = bornAt;

        if (null == customAttributes) {
            this.customAttributes = null;
        } else {
            this.customAttributes =
                    Collections.unmodifiableMap(new HashMap<String, String>(customAttributes));
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
        ((UserCreator) CREATOR).writeToParcel(dest, flags, this);
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
            final Map<String, String> customAttributes =
                    (Map<String, String>) source.readSerializable();
            final String email = source.readString();
            final boolean isConnectedToFacebook = 1 == source.readInt();
            final String firstName = source.readString();
            final Gender gender = Gender.forString(source.readString());
            final MonetaryValue globalCredit =
                    source.readParcelable(MonetaryValue.class.getClassLoader());
            final long id = source.readLong();
            final String lastName = source.readString();
            final int merchantsVisitedCount = source.readInt();
            final int ordersCount = source.readInt();
            final String termsAcceptedAt = source.readString();
            final MonetaryValue totalSavings =
                    source.readParcelable(MonetaryValue.class.getClassLoader());

            return new User(bornAt, customAttributes, email, firstName, gender, globalCredit, id,
                    isConnectedToFacebook, lastName, merchantsVisitedCount, ordersCount,
                    termsAcceptedAt, totalSavings);
        }

        @Override
        public User[] newArray(final int size) {
            return new User[size];
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final User user) {
            dest.writeString(user.getBornAt());
            dest.writeSerializable((Serializable) user.getCustomAttributes());
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
}
