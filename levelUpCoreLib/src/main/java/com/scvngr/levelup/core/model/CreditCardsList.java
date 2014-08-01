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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Parcelable list of {@link com.scvngr.levelup.core.model.CreditCard}s.
 */
@LevelUpApi(contract = Contract.DRAFT)
public final class CreditCardsList extends ParcelableArrayList<CreditCard> {
    /**
     * Parcelable creator.
     */
    @NonNull
    public static final Creator<CreditCardsList> CREATOR = new Creator<CreditCardsList>() {
        @Override
        public CreditCardsList createFromParcel(final Parcel source) {
            return new CreditCardsList(NullUtils.nonNullContract(source));
        }

        @Override
        public CreditCardsList[] newArray(final int size) {
            return new CreditCardsList[size];
        }
    };

    /**
     * Serializable ID.
     */
    private static final long serialVersionUID = 4477481794055009378L;

    /**
     * Creates a new, empty list.
     */
    public CreditCardsList() {
        super(new ArrayList<CreditCard>());
    }

    /**
     * Creates a list with a copy of the given collection.
     *
     * @param cards source list.
     */
    public CreditCardsList(@NonNull final Collection<CreditCard> cards) {
        super(cards);
    }

    /**
     * Parcelable constructor.
     *
     * @param in source parcel.
     */
    public CreditCardsList(@NonNull final Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    protected Creator<CreditCard> getCreator() {
        return CreditCard.CREATOR;
    }
}
