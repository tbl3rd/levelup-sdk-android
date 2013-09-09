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
 * A location category.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.INTERNAL)
public final class Category implements Parcelable {

    /**
     * Parcelable creator.
     */
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(final Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(final int size) {
            return new Category[size];
        }
    };

    /**
     * Web service ID for the category.
     */
    private final int id;

    /**
     * Category name.
     */
    @NonNull
    private final String name;

    /**
     * Construct from a parcel.
     *
     * @param in parcel to read from.
     */
    public Category(@NonNull final Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
