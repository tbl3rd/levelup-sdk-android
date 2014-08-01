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
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.util.ParcelableArrayList;
import com.scvngr.levelup.core.util.NullUtils;

import java.util.Collection;

/**
 * An immutable {@link java.util.ArrayList} of {@link com.scvngr.levelup.core.model.Location}s.
 */
@LevelUpApi(contract = Contract.DRAFT)
public final class LocationList extends ParcelableArrayList<Location> {
    /**
     * Parcelable creator.
     */
    @NonNull
    public static final Creator<LocationList> CREATOR = new Creator<LocationList>() {

        @Override
        public LocationList[] newArray(final int size) {
            return new LocationList[size];
        }

        @Override
        public LocationList createFromParcel(final Parcel source) {
            return new LocationList(NullUtils.nonNullContract(source));
        }
    };

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 3475771534815493208L;

    /**
     *
     * @param in the parcel to read from.
     */
    public LocationList(@NonNull final Parcel in) {
        super(in);
    }

    /**
     * @param source the source collection to copy from.
     */
    public LocationList(final Collection<Location> source) {
        super(source);
    }

    @Override
    @NonNull
    protected Creator<Location> getCreator() {
        return Location.CREATOR;
    }
}
