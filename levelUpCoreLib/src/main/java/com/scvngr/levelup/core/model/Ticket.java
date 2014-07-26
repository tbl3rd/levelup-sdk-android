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
