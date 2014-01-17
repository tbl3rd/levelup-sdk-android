// Generated by delombok at Fri Jan 17 11:33:45 EST 2014
/*
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
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Model representing a receipt interstitial. An interstitial is a callout that is shown on a
 * receipt which offers a call to action to the user. The call to action can be to claim more credit
 * at a merchant, invite their friends to try the merchant, or some other type.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class Interstitial implements Parcelable {
    
    /**
     * Implements the {@link Parcelable} interface.
     */
    public static final InterstitialCreator CREATOR = new InterstitialCreator();
    
    /**
     * The type returned from {@link #getType()} if this is a claim Interstitial.
     */
    public static final String TYPE_CLAIM = "claim"; //$NON-NLS-1$
    
    /**
     * The type returned from {@link #getType()} if this is a no_action Interstitial. A No_Action
     * interstitial requires no action from the user, but provides information.
     */
    public static final String TYPE_NO_ACTION = "no_action"; //$NON-NLS-1$
    
    /**
     * The type returned from {@link #getType()} if this is a share Interstitial.
     */
    public static final String TYPE_SHARE = "share"; //$NON-NLS-1$
    
    /**
     * The type returned from {@link #getType()} if this is a URL Interstitial.
     */
    public static final String TYPE_URL = "url"; //$NON-NLS-1$
    
    /**
     * The parsed action.
     */
    @Nullable
    private final InterstitialAction action;
    
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
        CREATOR.writeToParcel(dest, flags, this);
    }

    /**
     * Handles parceling for {@link Interstitial}.
     */
    @Immutable
    private static final class InterstitialCreator implements Creator<Interstitial> {


        @NonNull
        @Override
        public Interstitial createFromParcel(final Parcel source) {
            final InterstitialAction action = source.readParcelable(InterstitialAction.class.getClassLoader());
            final String calloutText = source.readString();
            final String descriptionHtml = source.readString();
            final String imageUrl = source.readString();
            final String title = source.readString();
            final String type = source.readString();
            return new Interstitial(action, calloutText, descriptionHtml, imageUrl, title, type);
        }

        @Override
        public Interstitial[] newArray(final int size) {
            return new Interstitial[size];
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Interstitial interstitial) {
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
    @LevelUpApi(contract = Contract.INTERNAL)
    public static final class ClaimAction implements InterstitialAction {
        
        /**
         * Implements Parcelable.
         */
        public static final Creator<ClaimAction> CREATOR = new Creator<Interstitial.ClaimAction>(){


            @Override
            public ClaimAction[] newArray(int size) {
                return new ClaimAction[size];
            }

            @Override
            public ClaimAction createFromParcel(Parcel source) {
                return new ClaimAction(source.readString());
            }
        };
        
        /**
         * The code that should be claimed for this action.
         */
        @NonNull
        private final String code;

        @Override
        public void writeToParcel(@NonNull final Parcel dest, final int flags) {
            dest.writeString(code);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * The code that should be claimed for this action.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getCode() {
            return this.code;
        }

        @Override
        @SuppressWarnings("all")
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Interstitial.ClaimAction)) return false;
            final ClaimAction other = (ClaimAction)o;
            final Object this$code = this.getCode();
            final Object other$code = other.getCode();
            if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
            return true;
        }

        @Override
        @SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 277;
            int result = 1;
            final Object $code = this.getCode();
            result = result * PRIME + ($code == null ? 0 : $code.hashCode());
            return result;
        }

        @Override
        @SuppressWarnings("all")
        public String toString() {
            return "Interstitial.ClaimAction(code=" + this.getCode() + ")";
        }

        @SuppressWarnings("all")
        public ClaimAction(@NonNull final String code) {
            if (code == null) {
                throw new NullPointerException("code");
            }
            this.code = code;
        }
    }

    /**
     * Represents an interstitial that can be shared by the user.
     */
    @Immutable
    @LevelUpApi(contract = Contract.INTERNAL)
    public static final class ShareAction implements InterstitialAction {
        
        /**
         * Implements Parcelable.
         */
        public static final Creator<ShareAction> CREATOR = new Creator<Interstitial.ShareAction>(){


            @Override
            public ShareAction[] newArray(int size) {
                return new ShareAction[size];
            }

            @Override
            public ShareAction createFromParcel(Parcel source) {
                return new ShareAction(source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString());
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
        public void writeToParcel(@NonNull final Parcel dest, final int flags) {
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

        /**
         * The message for the body of the email for sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getMessageForEmailBody() {
            return this.messageForEmailBody;
        }

        /**
         * The message for the subject of the email for sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getMessageForEmailSubject() {
            return this.messageForEmailSubject;
        }

        /**
         * The message to display for Facebook sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getMessageForFacebook() {
            return this.messageForFacebook;
        }

        /**
         * The message to display for Twitter sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getMessageForTwitter() {
            return this.messageForTwitter;
        }

        /**
         * The URL to use for email sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getShareUrlEmail() {
            return this.shareUrlEmail;
        }

        /**
         * The URL to use for Facebook sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getShareUrlFacebook() {
            return this.shareUrlFacebook;
        }

        /**
         * The URL to use for Twitter sharing.
         */
        @NonNull
        @SuppressWarnings("all")
        public String getShareUrlTwitter() {
            return this.shareUrlTwitter;
        }

        @Override
        @SuppressWarnings("all")
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Interstitial.ShareAction)) return false;
            final ShareAction other = (ShareAction)o;
            final Object this$messageForEmailBody = this.getMessageForEmailBody();
            final Object other$messageForEmailBody = other.getMessageForEmailBody();
            if (this$messageForEmailBody == null ? other$messageForEmailBody != null : !this$messageForEmailBody.equals(other$messageForEmailBody)) return false;
            final Object this$messageForEmailSubject = this.getMessageForEmailSubject();
            final Object other$messageForEmailSubject = other.getMessageForEmailSubject();
            if (this$messageForEmailSubject == null ? other$messageForEmailSubject != null : !this$messageForEmailSubject.equals(other$messageForEmailSubject)) return false;
            final Object this$messageForFacebook = this.getMessageForFacebook();
            final Object other$messageForFacebook = other.getMessageForFacebook();
            if (this$messageForFacebook == null ? other$messageForFacebook != null : !this$messageForFacebook.equals(other$messageForFacebook)) return false;
            final Object this$messageForTwitter = this.getMessageForTwitter();
            final Object other$messageForTwitter = other.getMessageForTwitter();
            if (this$messageForTwitter == null ? other$messageForTwitter != null : !this$messageForTwitter.equals(other$messageForTwitter)) return false;
            final Object this$shareUrlEmail = this.getShareUrlEmail();
            final Object other$shareUrlEmail = other.getShareUrlEmail();
            if (this$shareUrlEmail == null ? other$shareUrlEmail != null : !this$shareUrlEmail.equals(other$shareUrlEmail)) return false;
            final Object this$shareUrlFacebook = this.getShareUrlFacebook();
            final Object other$shareUrlFacebook = other.getShareUrlFacebook();
            if (this$shareUrlFacebook == null ? other$shareUrlFacebook != null : !this$shareUrlFacebook.equals(other$shareUrlFacebook)) return false;
            final Object this$shareUrlTwitter = this.getShareUrlTwitter();
            final Object other$shareUrlTwitter = other.getShareUrlTwitter();
            if (this$shareUrlTwitter == null ? other$shareUrlTwitter != null : !this$shareUrlTwitter.equals(other$shareUrlTwitter)) return false;
            return true;
        }

        @Override
        @SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 277;
            int result = 1;
            final Object $messageForEmailBody = this.getMessageForEmailBody();
            result = result * PRIME + ($messageForEmailBody == null ? 0 : $messageForEmailBody.hashCode());
            final Object $messageForEmailSubject = this.getMessageForEmailSubject();
            result = result * PRIME + ($messageForEmailSubject == null ? 0 : $messageForEmailSubject.hashCode());
            final Object $messageForFacebook = this.getMessageForFacebook();
            result = result * PRIME + ($messageForFacebook == null ? 0 : $messageForFacebook.hashCode());
            final Object $messageForTwitter = this.getMessageForTwitter();
            result = result * PRIME + ($messageForTwitter == null ? 0 : $messageForTwitter.hashCode());
            final Object $shareUrlEmail = this.getShareUrlEmail();
            result = result * PRIME + ($shareUrlEmail == null ? 0 : $shareUrlEmail.hashCode());
            final Object $shareUrlFacebook = this.getShareUrlFacebook();
            result = result * PRIME + ($shareUrlFacebook == null ? 0 : $shareUrlFacebook.hashCode());
            final Object $shareUrlTwitter = this.getShareUrlTwitter();
            result = result * PRIME + ($shareUrlTwitter == null ? 0 : $shareUrlTwitter.hashCode());
            return result;
        }

        @Override
        @SuppressWarnings("all")
        public String toString() {
            return "Interstitial.ShareAction(messageForEmailBody=" + this.getMessageForEmailBody() + ", messageForEmailSubject=" + this.getMessageForEmailSubject() + ", messageForFacebook=" + this.getMessageForFacebook() + ", messageForTwitter=" + this.getMessageForTwitter() + ", shareUrlEmail=" + this.getShareUrlEmail() + ", shareUrlFacebook=" + this.getShareUrlFacebook() + ", shareUrlTwitter=" + this.getShareUrlTwitter() + ")";
        }

        @SuppressWarnings("all")
        public ShareAction(@NonNull final String messageForEmailBody, @NonNull final String messageForEmailSubject, @NonNull final String messageForFacebook, @NonNull final String messageForTwitter, @NonNull final String shareUrlEmail, @NonNull final String shareUrlFacebook, @NonNull final String shareUrlTwitter) {
            if (messageForEmailBody == null) {
                throw new NullPointerException("messageForEmailBody");
            }
            if (messageForEmailSubject == null) {
                throw new NullPointerException("messageForEmailSubject");
            }
            if (messageForFacebook == null) {
                throw new NullPointerException("messageForFacebook");
            }
            if (messageForTwitter == null) {
                throw new NullPointerException("messageForTwitter");
            }
            if (shareUrlEmail == null) {
                throw new NullPointerException("shareUrlEmail");
            }
            if (shareUrlFacebook == null) {
                throw new NullPointerException("shareUrlFacebook");
            }
            if (shareUrlTwitter == null) {
                throw new NullPointerException("shareUrlTwitter");
            }
            this.messageForEmailBody = messageForEmailBody;
            this.messageForEmailSubject = messageForEmailSubject;
            this.messageForFacebook = messageForFacebook;
            this.messageForTwitter = messageForTwitter;
            this.shareUrlEmail = shareUrlEmail;
            this.shareUrlFacebook = shareUrlFacebook;
            this.shareUrlTwitter = shareUrlTwitter;
        }
    }

    /**
     * Represents an interstitial that contains a URL that can be visited by the user.
     */
    @Immutable
    @LevelUpApi(contract = Contract.INTERNAL)
    public static final class UrlAction implements InterstitialAction {
        @VisibleForTesting(visibility = Visibility.PRIVATE)
        private static final String URL = "url"; //$NON-NLS-1$
        /**
         * Implements Parcelable.
         */
        public static final Creator<UrlAction> CREATOR = new Creator<Interstitial.UrlAction>(){


            @Override
            public UrlAction[] newArray(int size) {
                return new UrlAction[size];
            }

            @Override
            public UrlAction createFromParcel(Parcel source) {
                return new UrlAction(source.readString());
            }
        };
        /**
         * The URL to display for this interstitial.
         */
        @NonNull
        private final String url;

        @Override
        public void writeToParcel(@NonNull final Parcel dest, final int flags) {
            dest.writeString(url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @NonNull
        @SuppressWarnings("all")
        public String getUrl() {
            return this.url;
        }

        @Override
        @SuppressWarnings("all")
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Interstitial.UrlAction)) return false;
            final UrlAction other = (UrlAction)o;
            final Object this$url = this.getUrl();
            final Object other$url = other.getUrl();
            if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
            return true;
        }

        @Override
        @SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 277;
            int result = 1;
            final Object $url = this.getUrl();
            result = result * PRIME + ($url == null ? 0 : $url.hashCode());
            return result;
        }

        @Override
        @SuppressWarnings("all")
        public String toString() {
            return "Interstitial.UrlAction(url=" + this.getUrl() + ")";
        }

        @SuppressWarnings("all")
        public UrlAction(@NonNull final String url) {
            if (url == null) {
                throw new NullPointerException("url");
            }
            this.url = url;
        }
    }

    /**
     * The parsed action.
     */
    @Nullable
    @SuppressWarnings("all")
    public InterstitialAction getAction() {
        return this.action;
    }

    /**
     * The text to prompt the user to view this interstitial.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getCalloutText() {
        return this.calloutText;
    }

    /**
     * The description (encoded in HTML) of the interstitial.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getDescriptionHtml() {
        return this.descriptionHtml;
    }

    /**
     * The base URL to the image for this interstitial.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getImageUrl() {
        return this.imageUrl;
    }

    /**
     * The title of the interstitial.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getTitle() {
        return this.title;
    }

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
    @SuppressWarnings("all")
    public String getType() {
        return this.type;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Interstitial)) return false;
        final Interstitial other = (Interstitial)o;
        final Object this$action = this.getAction();
        final Object other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) return false;
        final Object this$calloutText = this.getCalloutText();
        final Object other$calloutText = other.getCalloutText();
        if (this$calloutText == null ? other$calloutText != null : !this$calloutText.equals(other$calloutText)) return false;
        final Object this$descriptionHtml = this.getDescriptionHtml();
        final Object other$descriptionHtml = other.getDescriptionHtml();
        if (this$descriptionHtml == null ? other$descriptionHtml != null : !this$descriptionHtml.equals(other$descriptionHtml)) return false;
        final Object this$imageUrl = this.getImageUrl();
        final Object other$imageUrl = other.getImageUrl();
        if (this$imageUrl == null ? other$imageUrl != null : !this$imageUrl.equals(other$imageUrl)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 277;
        int result = 1;
        final Object $action = this.getAction();
        result = result * PRIME + ($action == null ? 0 : $action.hashCode());
        final Object $calloutText = this.getCalloutText();
        result = result * PRIME + ($calloutText == null ? 0 : $calloutText.hashCode());
        final Object $descriptionHtml = this.getDescriptionHtml();
        result = result * PRIME + ($descriptionHtml == null ? 0 : $descriptionHtml.hashCode());
        final Object $imageUrl = this.getImageUrl();
        result = result * PRIME + ($imageUrl == null ? 0 : $imageUrl.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 0 : $title.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 0 : $type.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "Interstitial(action=" + this.getAction() + ", calloutText=" + this.getCalloutText() + ", descriptionHtml=" + this.getDescriptionHtml() + ", imageUrl=" + this.getImageUrl() + ", title=" + this.getTitle() + ", type=" + this.getType() + ")";
    }

    @SuppressWarnings("all")
    public Interstitial(@Nullable final InterstitialAction action, @NonNull final String calloutText, @NonNull final String descriptionHtml, @NonNull final String imageUrl, @NonNull final String title, @NonNull final String type) {
        if (calloutText == null) {
            throw new NullPointerException("calloutText");
        }
        if (descriptionHtml == null) {
            throw new NullPointerException("descriptionHtml");
        }
        if (imageUrl == null) {
            throw new NullPointerException("imageUrl");
        }
        if (title == null) {
            throw new NullPointerException("title");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }
        this.action = action;
        this.calloutText = calloutText;
        this.descriptionHtml = descriptionHtml;
        this.imageUrl = imageUrl;
        this.title = title;
        this.type = type;
    }
}