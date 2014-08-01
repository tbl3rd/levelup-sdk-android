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
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Value;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents an error from the server.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class Error implements Parcelable {

    /**
     * Creator for parceling.
     */
    @NonNull
    public static final Creator<Error> CREATOR = new ErrorCreator();

    /**
     * A machine-readable description of the error.
     */
    @Nullable
    private final String code;

    /**
     * The message for the error that occurred.
     */
    @NonNull
    @RequiredField
    private final String message;

    /**
     * The object that this error is for. This object is scoped to the context
     * of the web service.
     */
    @Nullable
    private final String object;

    /**
     * The property of the object that this error is for.
     */
    @Nullable
    private final String property;

    /**
     * @deprecated Provided for SDK backwards compatibility only. Newer code should use {@link
     * com.scvngr.levelup.core.model.Error#Error(String, String, String, String)} instead. This
     * constructor omits the {@link #code} field.
     */
    @Deprecated
    @SuppressWarnings("all")
    public Error(@NonNull final String message, @Nullable final String object,
            @Nullable final String property) {
        this(null, message, object, property);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((ErrorCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    /**
     * Creator for creating {@link Error}s from parcels as well as writing {@link Error}s to
     * parcels.
     */
    @Immutable
    private static final class ErrorCreator implements Creator<Error> {

        @Override
        public Error[] newArray(final int size) {
            return new Error[size];
        }

        @NonNull
        @Override
        public Error createFromParcel(final Parcel in) {
            final String code = in.readString();
            final String message = NullUtils.nonNullContract(in.readString());
            final String object = in.readString();
            final String property = in.readString();

            return new Error(code, message, object, property);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Error error) {
            dest.writeString(error.getCode());
            dest.writeString(error.getMessage());
            dest.writeString(error.getObject());
            dest.writeString(error.getProperty());
        }
    }
}
