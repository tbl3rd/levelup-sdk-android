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
 * Model for representing a Scan. Currently, the {@link Scan} can be a code scanned using the camera
 * or a code HTTP hyperlink.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.INTERNAL)
public final class Scan implements Parcelable {

    /**
     * Implements the {@link Parcelable} interface.
     */
    @NonNull
    public static final Creator<Scan> CREATOR = new ScanCreator();

    /**
     * The data from the scan.
     */
    @NonNull
    @RequiredField
    private final String data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((ScanCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    /**
     * Handles parceling for {@link Scan}.
     */
    @Immutable
    private static final class ScanCreator implements Creator<Scan> {

        @NonNull
        @Override
        public Scan createFromParcel(final Parcel source) {
            final String data = NullUtils.nonNullContract(source.readString());

            return new Scan(data);
        }

        @Override
        public Scan[] newArray(final int size) {
            return new Scan[size];
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Scan scan) {
            dest.writeString(scan.getData());
        }
    }
}
