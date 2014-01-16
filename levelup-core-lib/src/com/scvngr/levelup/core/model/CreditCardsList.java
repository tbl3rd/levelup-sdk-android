/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.util.ParcelableArrayList;

/**
 * Parcelable list of {@link CreditCard}s.
 */
@LevelUpApi(contract=Contract.DRAFT)
public final class CreditCardsList extends ParcelableArrayList<CreditCard> {

    /**
     * Parcelable creator.
     */
    public static Creator<CreditCardsList> CREATOR = new Creator<CreditCardsList>() {
        @Override
        public CreditCardsList createFromParcel(final Parcel source) {
            return new CreditCardsList(source);
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
