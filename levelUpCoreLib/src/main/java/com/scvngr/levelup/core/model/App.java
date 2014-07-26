/**
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
 * A connected app.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class App implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
    public static final Creator<App> CREATOR = new AppCreator();

    /**
     * A short, human-readable description of the app.
     */
    @NonNull
    @RequiredField
    private final String description;

    /**
     * The web service ID for the app.
     */
    private final long id;

    /**
     * A URL of an image that represents the app.
     */
    @Nullable
    private final String imageUrl;

    /**
     * The human-readable name of the app.
     */
    @NonNull
    @RequiredField
    private final String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ((AppCreator)CREATOR).writeToParcel(NullUtils.nonNullContract(dest), this);
    }

    /* package */static final class AppCreator implements Creator<App> {

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }

        @Override
        public App createFromParcel(Parcel source) {
            final String description = NullUtils.nonNullContract(source.readString());
            final long id = source.readLong();
            final String imageUrl = source.readString();
            final String name = NullUtils.nonNullContract(source.readString());

            return new App(description, id, imageUrl, name);
        }

        /* package */void writeToParcel(@NonNull final Parcel dest, @NonNull final App app) {
            dest.writeString(app.getDescription());
            dest.writeLong(app.getId());
            dest.writeString(app.getImageUrl());
            dest.writeString(app.getName());
        }
    }
}
