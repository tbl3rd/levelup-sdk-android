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
import com.scvngr.levelup.core.annotation.model.RequiredField;

import net.jcip.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Value;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Model representing a registration. Provides details necessary for a user to register.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.INTERNAL)
public final class Registration implements Parcelable {

    /**
     * Implements the {@link android.os.Parcelable} interface.
     */
    public static final Creator<Registration> CREATOR = new RegistrationCreator();

    /**
     * The name of the application.
     */
    @NonNull
    @RequiredField
    private final String appName;

    /**
     * The description of the registration.
     */
    @Nullable
    private final String description;

    /**
     * If this user is connected to Facebook or not.
     */
    private final boolean facebook;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((RegistrationCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    /**
     * Handles parceling for {@link com.scvngr.levelup.core.model.Registration}.
     */
    @Immutable
    private static final class RegistrationCreator implements Creator<Registration> {

        @NonNull
        @Override
        public Registration createFromParcel(final Parcel source) {
            final String appName = source.readString();
            final String description = source.readString();
            final boolean facebook = 1 == source.readInt();

            return new Registration(appName, description, facebook);
        }

        @NonNull
        @Override
        public Registration[] newArray(final int size) {
            return new Registration[size];
        }

        @SuppressWarnings("unused")
        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Registration registration) {
            dest.writeString(registration.appName);
            dest.writeString(registration.description);
            dest.writeInt(registration.facebook ? 1 : 0);
        }
    }
}
