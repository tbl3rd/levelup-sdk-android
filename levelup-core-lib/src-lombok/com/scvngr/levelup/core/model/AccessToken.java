/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents the LevelUp access token.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class AccessToken implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    public static final Creator<AccessToken> CREATOR = new AccessTokenCreator();

    /**
     * Access token.
     */
    @NonNull
    private final String accessToken;

    /**
     * User's ID on the web service.
     */
    private final long userId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((AccessTokenCreator) CREATOR).writeToParcel(dest, flags, this);
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
            final String accessToken = in.readString();
            final long userId = in.readLong();

            return new AccessToken(accessToken, userId);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final AccessToken token) {
            dest.writeString(token.getAccessToken());
            dest.writeLong(token.getUserId());
        }
    }
}
