// Generated by delombok at Fri Sep 27 13:34:02 EDT 2013
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
import com.scvngr.levelup.core.model.MonetaryValue;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a campaign (which can be claimed for credit) on the server.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class Campaign implements Parcelable {
	
	/**
	 * Whether or not this campaign's value is eligible to be spent at all merchants on LevelUp.
	 */
	private final boolean appliesToAllMerchants;
	
	/**
	 * The text to show when a successful claim of this campaign occurs.
	 */
	@NonNull
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
	private final String name;
	
	/**
	 * The text describing this campaign.
	 */
	@NonNull
	private final String offerHtml;
	
	/**
	 * The type of campaign this is.
	 */
	@NonNull
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
	 * The URL to share via facbeook.
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
	private final MonetaryValue value;
	
	/**
	 * Implements the {@code Parcelable} interface.
	 */
	public static final Creator<Campaign> CREATOR = new CampaignCreator();
	
	@Override
	public final int describeContents() {
		return 0;
	}
	
	@Override
	public final void writeToParcel(final Parcel dest, final int flags) {
		((CampaignCreator)CREATOR).writeToParcel(dest, flags, this);
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
			builder.appliesToAllMerchants(in.readByte() == (byte)1);
			builder.confirmationHtml(in.readString()).id(in.readLong());
			builder.messageForEmailBody(in.readString()).messageForEmailSubject(in.readString());
			builder.messageForFacebook(in.readString());
			builder.messageForTwitter(in.readString());
			builder.name(in.readString());
			builder.offerHtml(in.readString());
			builder.shareable(in.readByte() == (byte)1);
			builder.shareUrlEmail(in.readString());
			builder.shareUrlFacebook(in.readString());
			builder.shareUrlTwitter(in.readString());
			builder.sponsor(in.readString());
			builder.type(in.readString());
			builder.value((MonetaryValue)in.readParcelable(MonetaryValue.class.getClassLoader()));
			return builder.build();
		}
		
		public final void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Campaign campaign) {
			dest.writeByte(campaign.appliesToAllMerchants ? (byte)1 : (byte)0);
			dest.writeString(campaign.confirmationHtml);
			dest.writeLong(campaign.id);
			dest.writeString(campaign.messageForEmailBody);
			dest.writeString(campaign.messageForEmailSubject);
			dest.writeString(campaign.messageForFacebook);
			dest.writeString(campaign.messageForTwitter);
			dest.writeString(campaign.name);
			dest.writeString(campaign.offerHtml);
			dest.writeByte(campaign.shareable ? (byte)1 : (byte)0);
			dest.writeString(campaign.shareUrlEmail);
			dest.writeString(campaign.shareUrlFacebook);
			dest.writeString(campaign.shareUrlTwitter);
			dest.writeString(campaign.sponsor);
			dest.writeString(campaign.type);
			dest.writeParcelable(campaign.value, flags);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	public static class CampaignBuilder {
		private boolean appliesToAllMerchants;
		private String confirmationHtml;
		private long id;
		private String messageForEmailBody;
		private String messageForEmailSubject;
		private String messageForTwitter;
		private String messageForFacebook;
		private String name;
		private String offerHtml;
		private String type;
		private boolean shareable;
		private String shareUrlEmail;
		private String shareUrlFacebook;
		private String shareUrlTwitter;
		private String sponsor;
		private MonetaryValue value;
		
		@java.lang.SuppressWarnings("all")
		CampaignBuilder() {
			
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder appliesToAllMerchants(final boolean appliesToAllMerchants) {
			this.appliesToAllMerchants = appliesToAllMerchants;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder confirmationHtml(final String confirmationHtml) {
			this.confirmationHtml = confirmationHtml;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder id(final long id) {
			this.id = id;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder messageForEmailBody(final String messageForEmailBody) {
			this.messageForEmailBody = messageForEmailBody;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder messageForEmailSubject(final String messageForEmailSubject) {
			this.messageForEmailSubject = messageForEmailSubject;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder messageForTwitter(final String messageForTwitter) {
			this.messageForTwitter = messageForTwitter;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder messageForFacebook(final String messageForFacebook) {
			this.messageForFacebook = messageForFacebook;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder name(final String name) {
			this.name = name;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder offerHtml(final String offerHtml) {
			this.offerHtml = offerHtml;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder type(final String type) {
			this.type = type;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder shareable(final boolean shareable) {
			this.shareable = shareable;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder shareUrlEmail(final String shareUrlEmail) {
			this.shareUrlEmail = shareUrlEmail;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder shareUrlFacebook(final String shareUrlFacebook) {
			this.shareUrlFacebook = shareUrlFacebook;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder shareUrlTwitter(final String shareUrlTwitter) {
			this.shareUrlTwitter = shareUrlTwitter;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder sponsor(final String sponsor) {
			this.sponsor = sponsor;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public CampaignBuilder value(final MonetaryValue value) {
			this.value = value;
			return this;
		}
		
		@java.lang.SuppressWarnings("all")
		public Campaign build() {
			return new Campaign(appliesToAllMerchants, confirmationHtml, id, messageForEmailBody, messageForEmailSubject, messageForTwitter, messageForFacebook, name, offerHtml, type, shareable, shareUrlEmail, shareUrlFacebook, shareUrlTwitter, sponsor, value);
		}
		
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "Campaign.CampaignBuilder(appliesToAllMerchants=" + this.appliesToAllMerchants + ", confirmationHtml=" + this.confirmationHtml + ", id=" + this.id + ", messageForEmailBody=" + this.messageForEmailBody + ", messageForEmailSubject=" + this.messageForEmailSubject + ", messageForTwitter=" + this.messageForTwitter + ", messageForFacebook=" + this.messageForFacebook + ", name=" + this.name + ", offerHtml=" + this.offerHtml + ", type=" + this.type + ", shareable=" + this.shareable + ", shareUrlEmail=" + this.shareUrlEmail + ", shareUrlFacebook=" + this.shareUrlFacebook + ", shareUrlTwitter=" + this.shareUrlTwitter + ", sponsor=" + this.sponsor + ", value=" + this.value + ")";
		}
	}
	
	@java.lang.SuppressWarnings("all")
	public static CampaignBuilder builder() {
		return new CampaignBuilder();
	}
	
	/**
	 * Whether or not this campaign's value is eligible to be spent at all merchants on LevelUp.
	 */
	@java.lang.SuppressWarnings("all")
	public boolean isAppliesToAllMerchants() {
		return this.appliesToAllMerchants;
	}
	
	/**
	 * The text to show when a successful claim of this campaign occurs.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public String getConfirmationHtml() {
		return this.confirmationHtml;
	}
	
	/**
	 * The ID of this Campaign on the web service.
	 */
	@java.lang.SuppressWarnings("all")
	public long getId() {
		return this.id;
	}
	
	/**
	 * The message to use as a body for an email share of this campaign.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getMessageForEmailBody() {
		return this.messageForEmailBody;
	}
	
	/**
	 * The message to use as a the subject for an email share of this campaign.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getMessageForEmailSubject() {
		return this.messageForEmailSubject;
	}
	
	/**
	 * The message to use for a Twitter share of this campaign.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getMessageForTwitter() {
		return this.messageForTwitter;
	}
	
	/**
	 * The message to use for a Facebook share of this campaign.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getMessageForFacebook() {
		return this.messageForFacebook;
	}
	
	/**
	 * The name of this campaign.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public String getName() {
		return this.name;
	}
	
	/**
	 * The text describing this campaign.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public String getOfferHtml() {
		return this.offerHtml;
	}
	
	/**
	 * The type of campaign this is.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public String getType() {
		return this.type;
	}
	
	/**
	 * If this Campaign is eligible to be shared to others.
	 */
	@java.lang.SuppressWarnings("all")
	public boolean isShareable() {
		return this.shareable;
	}
	
	/**
	 * The URL to share via email.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getShareUrlEmail() {
		return this.shareUrlEmail;
	}
	
	/**
	 * The URL to share via facbeook.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getShareUrlFacebook() {
		return this.shareUrlFacebook;
	}
	
	/**
	 * The URL to share via twitter.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getShareUrlTwitter() {
		return this.shareUrlTwitter;
	}
	
	/**
	 * The name of the sponsor of this campaign.
	 */
	@Nullable
	@java.lang.SuppressWarnings("all")
	public String getSponsor() {
		return this.sponsor;
	}
	
	/**
	 * The amount of credit the user would get for claiming this campaign.
	 */
	@NonNull
	@java.lang.SuppressWarnings("all")
	public MonetaryValue getValue() {
		return this.value;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof Campaign)) return false;
		final Campaign other = (Campaign)o;
		if (this.isAppliesToAllMerchants() != other.isAppliesToAllMerchants()) return false;
		final java.lang.Object this$confirmationHtml = this.getConfirmationHtml();
		final java.lang.Object other$confirmationHtml = other.getConfirmationHtml();
		if (this$confirmationHtml == null ? other$confirmationHtml != null : !this$confirmationHtml.equals(other$confirmationHtml)) return false;
		if (this.getId() != other.getId()) return false;
		final java.lang.Object this$messageForEmailBody = this.getMessageForEmailBody();
		final java.lang.Object other$messageForEmailBody = other.getMessageForEmailBody();
		if (this$messageForEmailBody == null ? other$messageForEmailBody != null : !this$messageForEmailBody.equals(other$messageForEmailBody)) return false;
		final java.lang.Object this$messageForEmailSubject = this.getMessageForEmailSubject();
		final java.lang.Object other$messageForEmailSubject = other.getMessageForEmailSubject();
		if (this$messageForEmailSubject == null ? other$messageForEmailSubject != null : !this$messageForEmailSubject.equals(other$messageForEmailSubject)) return false;
		final java.lang.Object this$messageForTwitter = this.getMessageForTwitter();
		final java.lang.Object other$messageForTwitter = other.getMessageForTwitter();
		if (this$messageForTwitter == null ? other$messageForTwitter != null : !this$messageForTwitter.equals(other$messageForTwitter)) return false;
		final java.lang.Object this$messageForFacebook = this.getMessageForFacebook();
		final java.lang.Object other$messageForFacebook = other.getMessageForFacebook();
		if (this$messageForFacebook == null ? other$messageForFacebook != null : !this$messageForFacebook.equals(other$messageForFacebook)) return false;
		final java.lang.Object this$name = this.getName();
		final java.lang.Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		final java.lang.Object this$offerHtml = this.getOfferHtml();
		final java.lang.Object other$offerHtml = other.getOfferHtml();
		if (this$offerHtml == null ? other$offerHtml != null : !this$offerHtml.equals(other$offerHtml)) return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
		if (this.isShareable() != other.isShareable()) return false;
		final java.lang.Object this$shareUrlEmail = this.getShareUrlEmail();
		final java.lang.Object other$shareUrlEmail = other.getShareUrlEmail();
		if (this$shareUrlEmail == null ? other$shareUrlEmail != null : !this$shareUrlEmail.equals(other$shareUrlEmail)) return false;
		final java.lang.Object this$shareUrlFacebook = this.getShareUrlFacebook();
		final java.lang.Object other$shareUrlFacebook = other.getShareUrlFacebook();
		if (this$shareUrlFacebook == null ? other$shareUrlFacebook != null : !this$shareUrlFacebook.equals(other$shareUrlFacebook)) return false;
		final java.lang.Object this$shareUrlTwitter = this.getShareUrlTwitter();
		final java.lang.Object other$shareUrlTwitter = other.getShareUrlTwitter();
		if (this$shareUrlTwitter == null ? other$shareUrlTwitter != null : !this$shareUrlTwitter.equals(other$shareUrlTwitter)) return false;
		final java.lang.Object this$sponsor = this.getSponsor();
		final java.lang.Object other$sponsor = other.getSponsor();
		if (this$sponsor == null ? other$sponsor != null : !this$sponsor.equals(other$sponsor)) return false;
		final java.lang.Object this$value = this.getValue();
		final java.lang.Object other$value = other.getValue();
		if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
		return true;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = result * PRIME + (this.isAppliesToAllMerchants() ? 1231 : 1237);
		final java.lang.Object $confirmationHtml = this.getConfirmationHtml();
		result = result * PRIME + ($confirmationHtml == null ? 0 : $confirmationHtml.hashCode());
		final long $id = this.getId();
		result = result * PRIME + (int)($id >>> 32 ^ $id);
		final java.lang.Object $messageForEmailBody = this.getMessageForEmailBody();
		result = result * PRIME + ($messageForEmailBody == null ? 0 : $messageForEmailBody.hashCode());
		final java.lang.Object $messageForEmailSubject = this.getMessageForEmailSubject();
		result = result * PRIME + ($messageForEmailSubject == null ? 0 : $messageForEmailSubject.hashCode());
		final java.lang.Object $messageForTwitter = this.getMessageForTwitter();
		result = result * PRIME + ($messageForTwitter == null ? 0 : $messageForTwitter.hashCode());
		final java.lang.Object $messageForFacebook = this.getMessageForFacebook();
		result = result * PRIME + ($messageForFacebook == null ? 0 : $messageForFacebook.hashCode());
		final java.lang.Object $name = this.getName();
		result = result * PRIME + ($name == null ? 0 : $name.hashCode());
		final java.lang.Object $offerHtml = this.getOfferHtml();
		result = result * PRIME + ($offerHtml == null ? 0 : $offerHtml.hashCode());
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 0 : $type.hashCode());
		result = result * PRIME + (this.isShareable() ? 1231 : 1237);
		final java.lang.Object $shareUrlEmail = this.getShareUrlEmail();
		result = result * PRIME + ($shareUrlEmail == null ? 0 : $shareUrlEmail.hashCode());
		final java.lang.Object $shareUrlFacebook = this.getShareUrlFacebook();
		result = result * PRIME + ($shareUrlFacebook == null ? 0 : $shareUrlFacebook.hashCode());
		final java.lang.Object $shareUrlTwitter = this.getShareUrlTwitter();
		result = result * PRIME + ($shareUrlTwitter == null ? 0 : $shareUrlTwitter.hashCode());
		final java.lang.Object $sponsor = this.getSponsor();
		result = result * PRIME + ($sponsor == null ? 0 : $sponsor.hashCode());
		final java.lang.Object $value = this.getValue();
		result = result * PRIME + ($value == null ? 0 : $value.hashCode());
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Campaign(appliesToAllMerchants=" + this.isAppliesToAllMerchants() + ", confirmationHtml=" + this.getConfirmationHtml() + ", id=" + this.getId() + ", messageForEmailBody=" + this.getMessageForEmailBody() + ", messageForEmailSubject=" + this.getMessageForEmailSubject() + ", messageForTwitter=" + this.getMessageForTwitter() + ", messageForFacebook=" + this.getMessageForFacebook() + ", name=" + this.getName() + ", offerHtml=" + this.getOfferHtml() + ", type=" + this.getType() + ", shareable=" + this.isShareable() + ", shareUrlEmail=" + this.getShareUrlEmail() + ", shareUrlFacebook=" + this.getShareUrlFacebook() + ", shareUrlTwitter=" + this.getShareUrlTwitter() + ", sponsor=" + this.getSponsor() + ", value=" + this.getValue() + ")";
	}
	
	@java.lang.SuppressWarnings("all")
	public Campaign(final boolean appliesToAllMerchants, @NonNull final String confirmationHtml, final long id, @Nullable final String messageForEmailBody, @Nullable final String messageForEmailSubject, @Nullable final String messageForTwitter, @Nullable final String messageForFacebook, @NonNull final String name, @NonNull final String offerHtml, @NonNull final String type, final boolean shareable, @Nullable final String shareUrlEmail, @Nullable final String shareUrlFacebook, @Nullable final String shareUrlTwitter, @Nullable final String sponsor, @NonNull final MonetaryValue value) {
		
		if (confirmationHtml == null) {
			throw new java.lang.NullPointerException("confirmationHtml");
		}
		if (name == null) {
			throw new java.lang.NullPointerException("name");
		}
		if (offerHtml == null) {
			throw new java.lang.NullPointerException("offerHtml");
		}
		if (type == null) {
			throw new java.lang.NullPointerException("type");
		}
		if (value == null) {
			throw new java.lang.NullPointerException("value");
		}
		this.appliesToAllMerchants = appliesToAllMerchants;
		this.confirmationHtml = confirmationHtml;
		this.id = id;
		this.messageForEmailBody = messageForEmailBody;
		this.messageForEmailSubject = messageForEmailSubject;
		this.messageForTwitter = messageForTwitter;
		this.messageForFacebook = messageForFacebook;
		this.name = name;
		this.offerHtml = offerHtml;
		this.type = type;
		this.shareable = shareable;
		this.shareUrlEmail = shareUrlEmail;
		this.shareUrlFacebook = shareUrlFacebook;
		this.shareUrlTwitter = shareUrlTwitter;
		this.sponsor = sponsor;
		this.value = value;
	}
}