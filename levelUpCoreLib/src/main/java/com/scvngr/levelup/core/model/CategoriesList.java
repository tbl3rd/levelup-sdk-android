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
