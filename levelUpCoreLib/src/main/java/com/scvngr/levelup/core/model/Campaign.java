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
import lombok.experimental.Builder;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a campaign (which can be claimed for credit) on the server.
 */
@Immutable
@Value
@LevelUpApi(contract = Contract.DRAFT)
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
public final class Campaign implements Parcelable {

    /**
     * Whether or not this campaign's value is eligible to be spent at all merchants on LevelUp.
     */
    private final boolean appliesToAllMerchants;

    /**
     * The text to show when a successful claim of this campaign occurs.
     */
    @NonNull
    @RequiredField
    private final String confirmationHtml;

    /**
     * The ID of this Campaign on the web service.
     */
    private final long id;

    /**
     * The message to use as a body for an email share of this campaign.
     */
    @Nullable
    private final String messageForEmailBody;

    /**
     * The message to use as a the subject for an email share of this campaign.
     */
    @Nullable
    private final String messageForEmailSubject;

    /**
     * The message to use for a Twitter share of this campaign.
     */
    @Nullable
    private final String messageForTwitter;

    /**
     * The message to use for a Facebook share of this campaign.
     */
    @Nullable
    private final String messageForFacebook;

    /**
     * The name of this campaign.
     */
    @NonNull
    @RequiredField
    private final String name;

    /**
     * The text describing this campaign.
     */
    @NonNull
    @RequiredField
    private final String offerHtml;

    /**
     * The type of campaign this is.
     */
    @NonNull
    @RequiredField
    private final String type;

    /**
     * If this Campaign is eligible to be shared to others.
     */
    private final boolean shareable;

    /**
     * The URL to share via email.
     */
    @Nullable
    private final String shareUrlEmail;

    /**
     * The URL to share via Facebook.
     */
    @Nullable
    private final String shareUrlFacebook;

    /**
     * The URL to share via twitter.
     */
    @Nullable
    private final String shareUrlTwitter;

    /**
     * The name of the sponsor of this campaign.
     */
    @Nullable
    private final String sponsor;

    /**
     * The amount of credit the user would get for claiming this campaign.
     */
    @NonNull
    @RequiredField
    private final MonetaryValue value;

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
    public static final Creator<Campaign> CREATOR = new CampaignCreator();

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(final Parcel dest, final int flags) {
        ((CampaignCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    @Immutable
    private static class CampaignCreator implements Creator<Campaign> {

        @Override
        public Campaign[] newArray(final int size) {
            return new Campaign[size];
        }

        @NonNull
        @Override
        public Campaign createFromParcel(final Parcel in) {
            final CampaignBuilder builder = Campaign.builder();

            builder.appliesToAllMerchants(in.readByte() == (byte) 1);
            builder.confirmationHtml(in.readString()).id(in.readLong());
            builder.messageForEmailBody(in.readString()).messageForEmailSubject(in.readString());
            builder.messageForFacebook(in.readString());
            builder.messageForTwitter(in.readString());
            builder.name(in.readString());
            builder.offerHtml(in.readString());
            builder.shareable(in.readByte() == (byte) 1);
            builder.shareUrlEmail(in.readString());
            builder.shareUrlFacebook(in.readString());
            builder.shareUrlTwitter(in.readString());
            builder.sponsor(in.readString());
            builder.type(in.readString());
            builder.value((MonetaryValue) in.readParcelable(MonetaryValue.class.getClassLoader()));

            return NullUtils.nonNullContract(builder.build());
        }

        public final void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Campaign campaign) {
            dest.writeByte(campaign.appliesToAllMerchants ? (byte) 1 : (byte) 0);
            dest.writeString(campaign.confirmationHtml);
            dest.writeLong(campaign.id);
            dest.writeString(campaign.messageForEmailBody);
            dest.writeString(campaign.messageForEmailSubject);
            dest.writeString(campaign.messageForFacebook);
            dest.writeString(campaign.messageForTwitter);
            dest.writeString(campaign.name);
            dest.writeString(campaign.offerHtml);
            dest.writeByte(campaign.shareable ? (byte) 1 : (byte) 0);
            dest.writeString(campaign.shareUrlEmail);
            dest.writeString(campaign.shareUrlFacebook);
            dest.writeString(campaign.shareUrlTwitter);
            dest.writeString(campaign.sponsor);
            dest.writeString(campaign.type);
            dest.writeParcelable(campaign.value, flags);
        }
    }
}
