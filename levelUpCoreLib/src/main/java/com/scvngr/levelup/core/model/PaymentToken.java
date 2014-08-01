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
 * Represents a user's payment token.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class PaymentToken implements Parcelable {

    /**
     * Implements the {@link Parcelable} interface.
     */
    @NonNull
    public static final Creator<PaymentToken> CREATOR = new PaymentCodeCreator();

    /**
     * The payment token data.
     */
    @NonNull
    @RequiredField
    private final String data;

    /**
     * The web service ID of the token.
     */
    private final long id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((PaymentCodeCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    /**
     * Class to parcel/unparcel {@link PaymentToken} objects.
     */
    private static final class PaymentCodeCreator implements Creator<PaymentToken> {

        @Override
        public PaymentToken createFromParcel(final Parcel source) {
            final String data = NullUtils.nonNullContract(source.readString());
            final long id = source.readLong();

            return new PaymentToken(data, id);
        }

        @Override
        public PaymentToken[] newArray(final int size) {
            return new PaymentToken[size];
        }

        public void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final PaymentToken code) {
            dest.writeString(code.getData());
            dest.writeLong(code.getId());
        }
    }
}
