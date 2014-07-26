/**
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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.val;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * A single permission of a {@link PermissionsRequest}.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Permission implements Parcelable {

    /**
     * {@link Parcelable} creator.
     */
    @NonNull
    public static final Creator<Permission> CREATOR = new PermissionCreator();

    /**
     * A short, human-readable description of this permission.
     */
    @NonNull
    @RequiredField
    String description;

    /**
     * The unique identifier for this type of permission.
     */
    @NonNull
    @RequiredField
    String keyname;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ((PermissionCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), this);
    }

    /* package */static final class PermissionCreator implements Creator<Permission> {

        @Override
        public Permission[] newArray(int size) {
            return new Permission[size];
        }

        @Override
        public Permission createFromParcel(Parcel source) {
            val description = NullUtils.nonNullContract(source.readString());
            val keyname = NullUtils.nonNullContract(source.readString());

            return new Permission(description, keyname);
        }

        /* package */void writeToParcel(@NonNull final Parcel dest,
                @NonNull final Permission permission) {
            dest.writeString(permission.getDescription());
            dest.writeString(permission.getKeyname());
        }
    }
}
