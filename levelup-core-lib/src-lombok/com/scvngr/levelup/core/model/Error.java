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
import com.scvngr.levelup.core.annotation.Nullable;

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
     * The message for the error that occurred.
     */
    @NonNull
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((ErrorCreator) CREATOR).writeToParcel(dest, flags, this);
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
            final String message = in.readString();
            final String object = in.readString();
            final String property = in.readString();

            return new Error(message, object, property);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Error error) {
            dest.writeString(error.getMessage());
            dest.writeString(error.getObject());
            dest.writeString(error.getProperty());
        }
    }
}
