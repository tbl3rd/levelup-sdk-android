/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * Represents a cause affiliation the server.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public class CauseAffiliation implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    public static final Creator<CauseAffiliation> CREATOR = new CauseAffiliationCreator();

    @Nullable
    private final Long mCauseWebServiceId;

    private final double mPercentDonation;

    /**
     * @param id the ID of the cause affiliation.
     * @param percentDonation the value of the percent donation.
     */
    public CauseAffiliation(@Nullable final Long id, final double percentDonation) {
        mCauseWebServiceId = id;
        mPercentDonation = percentDonation;
    }

    /**
     * @return the ID of the cause affiliation.
     */
    public Long getCauseWebServiceId() {
        return mCauseWebServiceId;
    }

    /**
     * @return the value of the percent donation.
     */
    public double getPercentDonation() {
        return mPercentDonation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(final Parcel dest, final int flags) {
        ((CauseAffiliationCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    // CHECKSTYLE:OFF eclipse generated
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result + ((mCauseWebServiceId == null) ? 0 : mCauseWebServiceId.hashCode());
        long temp;
        temp = Double.doubleToLongBits(mPercentDonation);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CauseAffiliation other = (CauseAffiliation) obj;
        if (mCauseWebServiceId == null) {
            if (other.mCauseWebServiceId != null)
                return false;
        } else if (!mCauseWebServiceId.equals(other.mCauseWebServiceId))
            return false;
        if (Double.doubleToLongBits(mPercentDonation) != Double
                .doubleToLongBits(other.mPercentDonation))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CauseAffiliation [mCauseWebServiceId=" + mCauseWebServiceId //$NON-NLS-1$
                + ", mPercentDonation=" + mPercentDonation + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    // CHECKSTYLE: ON

    @Immutable
    private static final class CauseAffiliationCreator implements Creator<CauseAffiliation> {

        @Override
        public CauseAffiliation[] newArray(final int size) {
            return new CauseAffiliation[size];
        }

        @Override
        @NonNull
        public CauseAffiliation createFromParcel(final Parcel in) {
            final Long id = (Long) in.readValue(Long.class.getClassLoader());
            final double percentDonation = in.readDouble();

            return new CauseAffiliation(id, percentDonation);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final CauseAffiliation causeAffiliation) {
            dest.writeValue(causeAffiliation.getCauseWebServiceId());
            dest.writeDouble(causeAffiliation.getPercentDonation());
        }
    }
}
