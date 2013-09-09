package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

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
    public static final Creator<Scan> CREATOR = new ScanCreator();

    /**
     * The data that were scanned.
     */
    @NonNull
    private final String data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((ScanCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    /**
     * Handles parceling for {@link Scan}.
     */
    @Immutable
    private static final class ScanCreator implements Creator<Scan> {

        @NonNull
        @Override
        public Scan createFromParcel(final Parcel source) {
            final String data = source.readString();

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
