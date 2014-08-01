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
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.model.RequiredField;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Value;

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
    @RequiredField
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
