/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.util.ParcelableArrayList;

/**
 * Parcelable list of {@link Order}s.
 */
public final class OrdersList extends ParcelableArrayList<Order> {

    /**
     * Parcelable creator.
     */
    public static Creator<OrdersList> CREATOR = new Creator<OrdersList>() {
        @Override
        public OrdersList createFromParcel(final Parcel source) {
            return new OrdersList(source);
        }

        @Override
        public OrdersList[] newArray(final int size) {
            return new OrdersList[size];
        }
    };

    /**
     * Serializable ID.
     */
    private static final long serialVersionUID = -6895560135837129219L;

    /**
     * Creates a new, empty list.
     */
    public OrdersList() {
        super(new ArrayList<Order>());
    }

    /**
     * Creates a list with a copy of the given collection.
     *
     * @param orders source list.
     */
    public OrdersList(@NonNull final Collection<Order> orders) {
        super(orders);
    }

    /**
     * Parcelable constructor.
     *
     * @param in source parcel.
     */
    public OrdersList(@NonNull final Parcel in) {
        super(in);
    }

    @NonNull
    @Override
    protected Creator<Order> getCreator() {
        return Order.CREATOR;
    }
}
