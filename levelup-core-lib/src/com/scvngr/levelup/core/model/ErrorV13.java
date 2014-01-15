/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.PreconditionUtil;

/**
 * Representing an error from the server. This object is deprecated in favor of the new way of doing
 * errors.
 */
@Immutable
@Deprecated
@LevelUpApi(contract = Contract.INTERNAL)
public final class ErrorV13 implements Parcelable {

    /**
     * Creator for parceling.
     */
    @NonNull
    public static final Creator<ErrorV13> CREATOR = new ErrorCreator();

    @NonNull
    private final String mError;

    @Nullable
    private final String mErrorDescription;

    @Nullable
    private final String mSponsorEmail;

    /**
     * @param error the error type.
     * @param errorDescription the error message.
     * @param sponsorEmail the sponsor email (optional, usually null).
     */
    public ErrorV13(@NonNull final String error, @Nullable final String errorDescription,
            @Nullable final String sponsorEmail) {
        PreconditionUtil.assertNotNull(error, "error"); //$NON-NLS-1$
        mError = error;
        mErrorDescription = errorDescription;
        mSponsorEmail = sponsorEmail;
    }

    /**
     * @return the error.
     */
    @NonNull
    public String getError() {
        return mError;
    }

    /**
     * @return Human-readable, localized description of the error. Note that if the system locale
     *         changes after the error was delivered from the API, it will not be correctly
     *         localized.
     */
    @Nullable
    public String getErrorDescription() {
        return mErrorDescription;
    }

    /**
     * @return The most descriptive message for the error. Human-readable, localized description of
     *         the error. Note that if the system locale changes after the error was delivered from
     *         the API, it will not be correctly localized. If there is an error description
     *         present, then it will be returned, otherwise, it will fallback to error.
     */
    @Nullable
    public String getErrorDescriptionOrError() {
        String ret = mError;

        if (null != mErrorDescription) {
            ret = mErrorDescription;
        }

        return ret;
    }

    /**
     * @return the sponsor email (usually null).
     */
    @Nullable
    public String getSponsorEmail() {
        return mSponsorEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((ErrorCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((null == mError) ? 0 : mError.hashCode());
        result = prime * result + ((null == mErrorDescription) ? 0 : mErrorDescription.hashCode());
        result = prime * result + ((null == mSponsorEmail) ? 0 : mSponsorEmail.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj) {
            return false;
        }

        if (!(obj instanceof ErrorV13)) {
            return false;
        }

        final ErrorV13 other = (ErrorV13) obj;
        if (null == mError) {
            if (null != other.mError) {
                return false;
            }
        } else if (!mError.equals(other.mError)) {
            return false;
        }

        if (null == mErrorDescription) {
            if (null != other.mErrorDescription) {
                return false;
            }
        } else if (!mErrorDescription.equals(other.mErrorDescription)) {
            return false;
        }

        if (null == mSponsorEmail) {
            if (null != other.mSponsorEmail) {
                return false;
            }
        } else if (!mSponsorEmail.equals(other.mSponsorEmail)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        // CHECKSTYLE:OFF long formatting string
        return String.format(Locale.US,
                "Error [mError=%s, mErrorDescription=%s, mSponsorEmail=%s]", //$NON-NLS-1$
                // CHECKSTYLE:ON
                mError, mErrorDescription, mSponsorEmail);
    }

    /**
     * Creator for creating {@link ErrorV13}s from parcels as well as writing {@link ErrorV13}s to
     * parcels.
     */
    @Immutable
    private static final class ErrorCreator implements Creator<ErrorV13> {

        @Override
        public ErrorV13[] newArray(final int size) {
            return new ErrorV13[size];
        }

        @NonNull
        @Override
        public ErrorV13 createFromParcel(final Parcel in) {
            final String error = in.readString();
            final String errorDescription = in.readString();
            final String sponsorEmail = in.readString();

            return new ErrorV13(error, errorDescription, sponsorEmail);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final ErrorV13 error) {
            dest.writeString(error.getError());
            dest.writeString(error.getErrorDescription());
            dest.writeString(error.getSponsorEmail());
        }
    }
}
