/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.util.NullUtils;

//The code below will be machine-processed.
//CHECKSTYLE:OFF

/**
 * Represents a feedback for an {@link com.scvngr.levelup.core.model.Order} which a user can
 * submit.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
public final class Feedback implements Parcelable {
    /**
     * Optional comments a user can leave for an order.
     */
    @Nullable
    private final String comment;

    /**
     * The question text displayed to the user associated with the comments the user filled out.
     */
    @NonNull
    private final String questionText;

    /**
     * The 1-5 star rating the user gives for an order.
     */
    private final int rating;

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
    public static final Parcelable.Creator<Feedback> CREATOR = new FeedbackCreator();

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(final Parcel dest, final int flags) {
        ((FeedbackCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    @Immutable
    private static class FeedbackCreator implements Parcelable.Creator<Feedback> {

        @Override
        public Feedback[] newArray(final int size) {
            return new Feedback[size];
        }

        @NonNull
        @Override
        public Feedback createFromParcel(final Parcel in) {
            return new Feedback(in.readString(), in.readString(), in.readInt());
        }

        public final void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Feedback feedback) {
            dest.writeString(feedback.getComment());
            dest.writeString(feedback.getQuestionText());
            dest.writeInt(feedback.getRating());
        }
    }
}
