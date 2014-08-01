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
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.model.AccessToken;

/**
 * Test implementation of {@link AccessTokenRetriever}.
 */
public final class MockAccessTokenRetriever implements AccessTokenRetriever {
    /**
     * Implements the {@link android.os.Parcelable} interface.
     */
    public static final Creator<AccessTokenRetriever> CREATOR =
            new Creator<AccessTokenRetriever>() {

                @Override
                public AccessTokenRetriever[] newArray(final int size) {
                    return new MockAccessTokenRetriever[size];
                }

                @Override
                public AccessTokenRetriever createFromParcel(final Parcel source) {
                    final AccessToken accessToken =
                            source.readParcelable(AccessToken.class.getClassLoader());

                    return new MockAccessTokenRetriever(accessToken);
                }
            };

    @Nullable
    private final AccessToken mAccessToken;

    /**
     * Constructor which uses a basic {@link AccessToken}.
     */
    public MockAccessTokenRetriever() {
        this(new AccessToken("test_access_token", 1L));
    }

    /**
     * Constructor which uses the specified {@link AccessToken}.
     *
     * @param accessToken the desired {@link AccessToken} or null
     */
    public MockAccessTokenRetriever(@Nullable final AccessToken accessToken) {
        mAccessToken = accessToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (null == obj) {
            return false;
        }

        if (obj instanceof MockAccessTokenRetriever) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @Nullable
    public AccessToken getAccessToken(@NonNull final Context context) {
        return mAccessToken;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mAccessToken, flags);
    }
}
