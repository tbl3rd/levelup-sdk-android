/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.model.RequiredField;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Value;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents the LevelUp access token.
 */
@Immutable
/*
 * The constructor that includes ID is deprecated and should be avoided. It's still needed for user
 * PUTs for a limited time (enterprise-only) but won't be necessary soon.
 */
@AllArgsConstructor(suppressConstructorProperties = true, onConstructor = @__({
    @LevelUpApi(contract = Contract.ENTERPRISE) }))
@Value
@LevelUpApi(contract = Contract.PUBLIC)
public final class AccessToken implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
    public static final Creator<AccessToken> CREATOR = new AccessTokenCreator();

    /**
     * Access token.
     */
    @NonNull
    @RequiredField
    private final String accessToken;

    /**
     * User's ID on the web service.
     */
    @Nullable
    @LevelUpApi(contract = Contract.ENTERPRISE)
    private final Long userId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((AccessTokenCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    /**
     * Implements parceling/unparceling for {@link AccessToken}.
     */
    @Immutable
    private static final class AccessTokenCreator implements Creator<AccessToken> {

        @Override
        public AccessToken[] newArray(final int size) {
            return new AccessToken[size];
        }

        @Override
        @NonNull
        public AccessToken createFromParcel(final Parcel in) {
            final String accessToken = NullUtils.nonNullContract(in.readString());
            final Long userId = (Long) in.readValue(Long.class.getClassLoader());

            return new AccessToken(accessToken, userId);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final AccessToken token) {
            dest.writeString(token.getAccessToken());
            dest.writeValue(token.getUserId());
        }
    }

    /**
     * Public representation of an AccessToken.
     *
     * @param accessToken the access token.
     */
    @LevelUpApi(contract = Contract.PUBLIC)
    @SuppressWarnings("all")
    public AccessToken(@NonNull final String accessToken) {
        this(accessToken, null);
    }
}
