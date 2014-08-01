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
 * A support ticket.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.INTERNAL)
public final class Ticket implements Parcelable {

    /**
     * Parcelable creator.
     */
    @NonNull
    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(final Parcel source) {
            return new Ticket(NullUtils.nonNullContract(source));
        }

        @Override
        public Ticket[] newArray(final int size) {
            return new Ticket[size];
        }
    };

    /**
     * Ticket body.
     */
    @NonNull
    @RequiredField
    private final String body;

    /**
     * Construct from a parcel.
     *
     * @param in parcel to read from.
     */
    public Ticket(@NonNull final Parcel in) {
        body = NullUtils.nonNullContract(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(body);
    }
}
