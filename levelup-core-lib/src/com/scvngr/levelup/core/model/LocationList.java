package com.scvngr.levelup.core.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.util.ParcelableArrayList;

/**
 * An immutable {@link ArrayList} of {@link Location}s.
 */
@LevelUpApi(contract = Contract.DRAFT)
public final class LocationList extends ParcelableArrayList<Location> {
    /**
     * Parcelable creator.
     */
    public static final Creator<LocationList> CREATOR = new Creator<LocationList>() {

        @Override
        public LocationList[] newArray(final int size) {
            return new LocationList[size];
        }

        @Override
        public LocationList createFromParcel(@NonNull final Parcel source) {
            return new LocationList(source);
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
    public LocationList(final Parcel in) {
        super(in);
    }

    /**
     * @param source the source collection to copy from.
     */
    public LocationList(final Collection<Location> source) {
        super(source);
    }

    @Override
    protected Creator<Location> getCreator() {
        return Location.CREATOR;
    }
}
