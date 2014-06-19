/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.util.ParcelableArrayList;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An immutable list of categories. This is needed to make the list parcelable.
 */
@ThreadSafe
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class CategoriesList extends ParcelableArrayList<Category> {
    /**
     * Parcelable creator.
     */
    public static final Creator<CategoriesList> CREATOR = new Creator<CategoriesList>() {
        @Override
        public CategoriesList createFromParcel(final Parcel source) {
            return new CategoriesList(NullUtils.nonNullContract(source));
        }

        @Override
        public CategoriesList[] newArray(final int size) {
            return new CategoriesList[size];
        }
    };

    /**
     * Serializable ID.
     */
    private static final long serialVersionUID = 3026455786382850447L;

    /**
     * Creates a new, empty list.
     */
    public CategoriesList() {
        super(new ArrayList<Category>());
    }

    /**
     * Creates a list with a copy of the given collection.
     *
     * @param categories source list.
     */
    public CategoriesList(@NonNull final Collection<Category> categories) {
        super(categories);
    }

    /**
     * Parcelable constructor.
     *
     * @param in source parcel.
     */
    public CategoriesList(@NonNull final Parcel in) {
        super(in);
    }

    @Override
    @NonNull
    protected Creator<Category> getCreator() {
        return Category.CREATOR;
    }
}
