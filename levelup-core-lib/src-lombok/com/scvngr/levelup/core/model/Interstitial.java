/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Builder;
import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.util.NullUtils;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Model representing a receipt interstitial. An interstitial is a callout that is shown on a
 * receipt which offers a call to action to the user. The call to action can be to claim more credit
 * at a merchant, invite their friends to try the merchant, or some other type.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.INTERNAL)
public final class Interstitial implements Parcelable {

    /**
     * Implements the {@link Parcelable} interface.
     */
    @NonNull
    public static final Creator<Interstitial> CREATOR = new InterstitialCreator();

    /**
     * The type returned from {@link #getType()} if this is a claim Interstitial.
     */
    @NonNull
    public static final String TYPE_CLAIM = "claim"; //$NON-NLS-1$

    /**
     * The type returned from {@link #getType()} if this is a no_action Interstitial. A No_Action
     * interstitial requires no action from the user, but provides information.
     */
    @NonNull
    public static final String TYPE_NO_ACTION = "no_action"; //$NON-NLS-1$

    /**
     * The type returned from {@link #getType()} if this is a share Interstitial.
     */
    @NonNull
    public static final String TYPE_SHARE = "share"; //$NON-NLS-1$

    /**
     * The type returned from {@link #getType()} if this is a URL Interstitial.
     */
    @NonNull
    public static final String TYPE_URL = "url"; //$NON-NLS-1$

    /**
     * The parsed action.
     */
    @Nullable
    final InterstitialAction action;

    /**
     * The text to prompt the user to view this interstitial.
     */
    @NonNull
    private final String calloutText;

    /**
     * The description (encoded in HTML) of the interstitial.
     */
    @NonNull
    private final String descriptionHtml;

    /**
     * The base URL to the image for this interstitial.
     */
    @NonNull
    private final String imageUrl;

    /**
     * The title of the interstitial.
     */
    @NonNull
    private final String title;

    /**
     * The type of the interstitial.
     * <p>
     * NOTE: This could return any of the recognized types (or a currently unsupported type). This
     * is mostly intended to be used to determine what type will be returned from
     * {@link #getAction}.
     * </p>
     * 
     * @see #TYPE_CLAIM
     * @see #TYPE_NO_ACTION
     * @see #TYPE_SHARE
     * @see #TYPE_URL
     */
    @NonNull
    private final String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((InterstitialCreator)CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }

    /**
     * Handles parceling for {@link Interstitial}.
     */
    @Immutable
    private static final class InterstitialCreator implements Creator<Interstitial> {

        @NonNull
        @Override
        public Interstitial createFromParcel(final Parcel source) {
            final InterstitialAction action =
                    source.readParcelable(InterstitialAction.class.getClassLoader());
            final String calloutText = NullUtils.nonNullContract(source.readString());
            final String descriptionHtml = NullUtils.nonNullContract(source.readString());
            final String imageUrl = NullUtils.nonNullContract(source.readString());
            final String title = NullUtils.nonNullContract(source.readString());
            final String type = NullUtils.nonNullContract(source.readString());

            return new Interstitial(action, calloutText, descriptionHtml, imageUrl, title, type);
        }

        @Override
        public Interstitial[] newArray(final int size) {
            return new Interstitial[size];
        }

        private void writeToParcel(final Parcel dest, final int flags,
                @NonNull final Interstitial interstitial) {
            dest.writeParcelable(interstitial.getAction(), flags);
            dest.writeString(interstitial.getCalloutText());
            dest.writeString(interstitial.getDescriptionHtml());
            dest.writeString(interstitial.getImageUrl());
            dest.writeString(interstitial.getTitle());
            dest.writeString(interstitial.getType());
        }
    }

    /**
     * Interface for the different types of actions.
     */
    public interface InterstitialAction extends Parcelable {
        // This space intentionally left blank.
    }

    /**
     * Represents an interstitial that can be claimed by the user.
     */
    @Immutable
    @Value
    @AllArgsConstructor(suppressConstructorProperties = true)
    @LevelUpApi(contract = Contract.INTERNAL)
    public static final class ClaimAction implements InterstitialAction {

        /**
         * Implements Parcelable.
         */
        @NonNull
        public static final Creator<ClaimAction> CREATOR = new Creator<Interstitial.ClaimAction>() {

            @Override
            public ClaimAction[] newArray(int size) {
                return new ClaimAction[size];
            }

            @Override
            public ClaimAction createFromParcel(Parcel source) {
                return new ClaimAction(NullUtils.nonNullContract(source.readString()));
            }
        };

        /**
         * The code that should be claimed for this action.
         */
        @NonNull
        private final String code;

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeString(code);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    /**
     * Represents an interstitial that can be shared by the user.
     */
    @Immutable
    @Value
    @AllArgsConstructor(suppressConstructorProperties = true)
    @LevelUpApi(contract = Contract.INTERNAL)
    @Builder
    public static final class ShareAction implements InterstitialAction {
        /**
         * Implements Parcelable.
         */
        @NonNull
        public static final Creator<ShareAction> CREATOR = new Creator<Interstitial.ShareAction>() {

            @Override
            public ShareAction[] newArray(int size) {
                return new ShareAction[size];
            }

            @Override
            public ShareAction createFromParcel(Parcel source) {
                final ShareActionBuilder builder = new ShareActionBuilder();
                builder.messageForEmailBody(NullUtils.nonNullContract(source.readString()));
                builder.messageForEmailSubject(NullUtils.nonNullContract(source.readString()));
                builder.messageForFacebook(NullUtils.nonNullContract(source.readString()));
                builder.messageForTwitter(NullUtils.nonNullContract(source.readString()));
                builder.shareUrlEmail(NullUtils.nonNullContract(source.readString()));
                builder.shareUrlFacebook(NullUtils.nonNullContract(source.readString()));
                builder.shareUrlTwitter(NullUtils.nonNullContract(source.readString()));

                return builder.build();
            }
        };

        /**
         * The message for the body of the email for sharing.
         */
        @NonNull
        private final String messageForEmailBody;

        /**
         * The message for the subject of the email for sharing.
         */
        @NonNull
        private final String messageForEmailSubject;

        /**
         * The message to display for Facebook sharing.
         */
        @NonNull
        private final String messageForFacebook;

        /**
         * The message to display for Twitter sharing.
         */
        @NonNull
        private final String messageForTwitter;

        /**
         * The URL to use for email sharing.
         */
        @NonNull
        private final String shareUrlEmail;

        /**
         * The URL to use for Facebook sharing.
         */
        @NonNull
        private final String shareUrlFacebook;

        /**
         * The URL to use for Twitter sharing.
         */
        @NonNull
        private final String shareUrlTwitter;

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeString(messageForEmailBody);
            dest.writeString(messageForEmailSubject);
            dest.writeString(messageForFacebook);
            dest.writeString(messageForTwitter);
            dest.writeString(shareUrlEmail);
            dest.writeString(shareUrlFacebook);
            dest.writeString(shareUrlTwitter);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

    /**
     * Represents an interstitial that contains a URL that can be visited by the user.
     */
    @Immutable
    @Value
    @AllArgsConstructor(suppressConstructorProperties = true)
    @LevelUpApi(contract = Contract.INTERNAL)
    public static final class UrlAction implements InterstitialAction {

        @VisibleForTesting(visibility = Visibility.PRIVATE)
        static final String URL = "url"; //$NON-NLS-1$

        /**
         * Implements Parcelable.
         */
        @NonNull
        public static final Creator<UrlAction> CREATOR = new Creator<Interstitial.UrlAction>() {

            @Override
            public UrlAction[] newArray(int size) {
                return new UrlAction[size];
            }

            @Override
            public UrlAction createFromParcel(Parcel source) {
                return new UrlAction(NullUtils.nonNullContract(source.readString()));
            }
        };

        /**
         * The URL to display for this interstitial.
         */
        @NonNull
        private final String url;

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeString(url);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}
