/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
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
