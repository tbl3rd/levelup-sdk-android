/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.util;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import net.jcip.annotations.Immutable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An ArrayList which is also Parcelable. Subclasses must implement part of the Parcelable
 * interface: add a static CREATOR which implements {@link android.os.Parcelable.Creator}.
 *
 * @param <E> the type of object this array holds.
 */
@SuppressLint("ParcelCreator")
@Immutable
public abstract class ParcelableArrayList<E extends Parcelable> extends ArrayList<E> implements
        Parcelable {

    /**
     * Serializable ID.
     */
    private static final long serialVersionUID = -1446873132782056704L;

    /**
     * True when the array is being loaded from the constructor. This is needed, as
     * {@link Parcel#readTypedList(java.util.List, android.os.Parcelable.Creator)} uses the
     * {@link #add(Parcelable)} methods to populate the array and otherwise this class must be
     * immutable.
     */
    private Boolean mIsBeingReadFromParcel = false;

    /**
     * Creates a new ArrayList, copying from the source collection.
     *
     * @param source the source collection to copy from.
     */
    /*
     * It is intentional that this isn't Collection<? extends E> like the superclass's constructor,
     * as readTypedList requires a homogeneous list.
     */
    public ParcelableArrayList(final Collection<E> source) {
        super(source);
    }

    /**
     * @param in the parcel to read from.
     */
    public ParcelableArrayList(@NonNull final Parcel in) {
        synchronized (mIsBeingReadFromParcel) {
            mIsBeingReadFromParcel = true;
            in.readTypedList(this, getCreator());
            mIsBeingReadFromParcel = false;
        }
    }

    @Override
    public final boolean add(final E object) {
        synchronized (mIsBeingReadFromParcel) {
            boolean result;

            if (mIsBeingReadFromParcel) {
                result = super.add(object);
            } else {
                throw new ImmutableClassException();
            }

            return result;
        }
    }

    @Override
    public final void add(final int index, final E object) {
        synchronized (mIsBeingReadFromParcel) {

            if (mIsBeingReadFromParcel) {
                super.add(index, object);
            } else {
                throw new ImmutableClassException();
            }
        }
    }

    @Override
    public final boolean addAll(final Collection<? extends E> collection) {
        synchronized (mIsBeingReadFromParcel) {
            boolean result;

            if (mIsBeingReadFromParcel) {
                result = super.addAll(collection);
            } else {
                throw new ImmutableClassException();
            }

            return result;
        }
    }

    @Override
    public final boolean
            addAll(final int index, final java.util.Collection<? extends E> collection) {
        synchronized (mIsBeingReadFromParcel) {
            boolean result;

            if (mIsBeingReadFromParcel) {
                result = super.addAll(index, collection);
            } else {
                throw new ImmutableClassException();
            }

            return result;
        }
    }

    @Override
    public final void clear() {
        throw new ImmutableClassException();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public final void ensureCapacity(final int minimumCapacity) {
        throw new ImmutableClassException();
    }

    @Override
    public final E remove(final int index) {
        throw new ImmutableClassException();
    }

    @Override
    public final boolean remove(final Object object) {
        throw new ImmutableClassException();
    }

    @Override
    public final boolean removeAll(final Collection<?> collection) {
        throw new ImmutableClassException();
    }

    @Override
    public final boolean retainAll(final Collection<?> collection) {
        throw new ImmutableClassException();
    }

    @Override
    public final E set(final int index, final E object) {
        throw new ImmutableClassException();
    }

    @Override
    public final void trimToSize() {
        throw new ImmutableClassException();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(this);
    }

    /**
     * Subclasses must implement this, returning the CREATOR of the array's Parcelable item.
     *
     * @return the Parcelable item's creator.
     */
    @NonNull
    protected abstract Creator<E> getCreator();

    @Override
    protected final void removeRange(final int fromIndex, final int toIndex) {
        throw new ImmutableClassException();
    }

    /**
     * This class is immutable.
     */
    public static final class ImmutableClassException extends UnsupportedOperationException {
        /**
         * Serializable ID.
         */
        private static final long serialVersionUID = 7301994074859654557L;

        /**
         * Default message.
         */
        public ImmutableClassException() {
            super("This class is immutable, operation not permitted.");
        }
    }
}
