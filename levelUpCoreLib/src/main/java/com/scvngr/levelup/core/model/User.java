/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Value;
import lombok.experimental.Builder;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents the user.
 */
@Immutable
@Value
@Builder
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
        @NonNull
        MALE,

        /**
         * A female-identified person.
         */
        @NonNull
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
                    LogManager.v("Gender string %s was not recognized", genderString);
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
     * Determines whether the user is connected to Facebook.
     */
    @LevelUpApi(contract = Contract.INTERNAL)
    private final boolean isConnectedToFacebook;

    /**
     * A map of custom attributes for the user.
     */
    @Nullable
    private final Map<String, String> customAttributes;

    /**
     * Determines whether the user is only allowed to link debit cards.
     */
    @LevelUpApi(contract = Contract.INTERNAL)
    private final boolean isDebitCardOnly;

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
     * @deprecated Provided for SDK backwards compatibility only. Newer code should use {@link
     * com.scvngr.levelup.core.model.User#User(String, boolean, Map, boolean, String, String,
     * Gender, MonetaryValue, long, String, int, int, String, MonetaryValue)} instead. This
     * constructor omits the {@link #isDebitCardOnly} field.
     */
    @Deprecated
    @SuppressWarnings("all")
    public User(@Nullable final String bornAt, @Nullable final Map<String, String> customAttributes,
            @Nullable final String email, @Nullable final String firstName,
            @Nullable final Gender gender, @Nullable final MonetaryValue globalCredit,
            final long id, final boolean isConnectedToFacebook, @Nullable final String lastName,
            final int merchantsVisitedCount, final int ordersCount,
            @Nullable final String termsAcceptedAt, @Nullable final MonetaryValue totalSavings) {
        this(bornAt, isConnectedToFacebook, customAttributes, false, email, firstName, gender,
                globalCredit, id, lastName, merchantsVisitedCount, ordersCount, termsAcceptedAt,
                totalSavings);
    }

    /**
     * @param bornAt the timestamp when the user was born.
     * @param isConnectedToFacebook determines whether the user is connected to Facebook.
     * @param customAttributes map of custom attributes for the user.
     * @param isDebitCardOnly determines whether the user is only allowed to link debit cards.
     * @param email the user's email address.
     * @param firstName the user's first name.
     * @param gender the user's gender.
     * @param globalCredit the amount of global credit the user has.
     * @param id the user's web service ID.
     * @param lastName the user's last name.
     * @param merchantsVisitedCount the number of merchants the user has visited.
     * @param ordersCount the user's total number of orders placed on LevelUp.
     * @param termsAcceptedAt the time the user accepted the LevelUp terms and conditions.
     * @param totalSavings the total amount the user has saved by using LevelUp.
     */
    public User(@Nullable final String bornAt, final boolean isConnectedToFacebook,
            @Nullable final Map<String, String> customAttributes, final boolean isDebitCardOnly,
            @Nullable final String email, @Nullable final String firstName,
            @Nullable final Gender gender, @Nullable final MonetaryValue globalCredit,
            final long id, @Nullable final String lastName, final int merchantsVisitedCount,
            final int ordersCount, @Nullable final String termsAcceptedAt,
            @Nullable final MonetaryValue totalSavings) {
        this.bornAt = bornAt;
        this.isConnectedToFacebook = isConnectedToFacebook;

        if (null == customAttributes) {
            this.customAttributes = null;
        } else {
            this.customAttributes =
                    Collections.unmodifiableMap(new HashMap<String, String>(customAttributes));
        }

        this.isDebitCardOnly = isDebitCardOnly;
        this.email = email;
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
        ((UserCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    /**
     * Class to parcel/unparcel {@link User} objects.
     */
    @Immutable
    private static class UserCreator implements Creator<User> {

        @NonNull
        @Override
        public User createFromParcel(final Parcel source) {
            final UserBuilder user = new UserBuilder();

            user.bornAt(source.readString());
            user.isConnectedToFacebook(1 == source.readInt());

            @SuppressWarnings("unchecked")
            final Map<String, String> customAttributes = (Map<String, String>) source.readSerializable();
            user.customAttributes(customAttributes);

            user.isDebitCardOnly(1 == source.readInt());
            user.email(source.readString());
            user.firstName(source.readString());
            user.gender(Gender.forString(source.readString()));
            user.globalCredit(source.<MonetaryValue>readParcelable(MonetaryValue.class.getClassLoader()));
            user.id(source.readLong());
            user.lastName(source.readString());
            user.merchantsVisitedCount(source.readInt());
            user.ordersCount(source.readInt());
            user.termsAcceptedAt(source.readString());
            user.totalSavings(source.<MonetaryValue>readParcelable(MonetaryValue.class.getClassLoader()));

            return NullUtils.nonNullContract(user.build());
        }

        @Override
        public User[] newArray(final int size) {
            return new User[size];
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final User user) {
            dest.writeString(user.getBornAt());
            dest.writeInt(user.isConnectedToFacebook() ? 1 : 0);
            dest.writeSerializable((Serializable) user.getCustomAttributes());
            dest.writeInt(user.isDebitCardOnly() ? 1 : 0);
            dest.writeString(user.getEmail());
            dest.writeString(user.getFirstName());
            final Gender gender = user.getGender();
            dest.writeString(null != gender ? gender.toString() : null);
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
