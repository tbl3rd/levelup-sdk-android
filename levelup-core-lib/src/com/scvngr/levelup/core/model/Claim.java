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
import com.scvngr.levelup.core.model.Campaign;
import com.scvngr.levelup.core.model.MonetaryValue;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Represents a claim of a {@link Campaign} on the server.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class Claim implements Parcelable {
    
    /**
     * The web service ID of the {@link com.scvngr.levelup.core.model.Campaign} that was claimed.
     */
    private final long campaignId;
    
    /**
     * The code of the {@link com.scvngr.levelup.core.model.Campaign} that was claimed.
     */
    @NonNull
    private final String code;
    
    /**
     * The web service ID of this {@link com.scvngr.levelup.core.model.Claim}.
     */
    private final long id;
    
    /**
     * The amount that this {@link com.scvngr.levelup.core.model.Claim} is worth.
     */
    @Nullable
    private final MonetaryValue value;
    
    /**
     * The amount that this {@link com.scvngr.levelup.core.model.Claim} still has remaining after
     * all redemptions.
     */
    @Nullable
    private final MonetaryValue valueRemaining;
    
    /**
     * Implements the {@code Parcelable} interface.
     */
    public static final Creator<Claim> CREATOR = new ClaimCreator();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((ClaimCreator)CREATOR).writeToParcel(dest, flags, this);
    }

    @Immutable
    private static final class ClaimCreator implements Creator<Claim> {


        @Override
        public Claim[] newArray(final int size) {
            return new Claim[size];
        }

        @Override
        public Claim createFromParcel(final Parcel in) {
            final long campaignId = in.readLong();
            final String code = in.readString();
            final long id = in.readLong();
            final MonetaryValue value = in.readParcelable(MonetaryValue.class.getClassLoader());
            final MonetaryValue valueRemaining = in.readParcelable(MonetaryValue.class.getClassLoader());
            return new Claim(campaignId, code, id, value, valueRemaining);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags, @NonNull final Claim claim) {
            dest.writeLong(claim.getCampaignId());
            dest.writeString(claim.getCode());
            dest.writeLong(claim.getId());
            dest.writeParcelable(claim.getValue(), flags);
            dest.writeParcelable(claim.getValueRemaining(), flags);
        }
    }

    /**
     * The web service ID of the {@link com.scvngr.levelup.core.model.Campaign} that was claimed.
     */
    @SuppressWarnings("all")
    public long getCampaignId() {
        return this.campaignId;
    }

    /**
     * The code of the {@link com.scvngr.levelup.core.model.Campaign} that was claimed.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getCode() {
        return this.code;
    }

    /**
     * The web service ID of this {@link com.scvngr.levelup.core.model.Claim}.
     */
    @SuppressWarnings("all")
    public long getId() {
        return this.id;
    }

    /**
     * The amount that this {@link com.scvngr.levelup.core.model.Claim} is worth.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getValue() {
        return this.value;
    }

    /**
     * The amount that this {@link com.scvngr.levelup.core.model.Claim} still has remaining after
     * all redemptions.
     */
    @Nullable
    @SuppressWarnings("all")
    public MonetaryValue getValueRemaining() {
        return this.valueRemaining;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Claim)) return false;
        final Claim other = (Claim)o;
        if (this.getCampaignId() != other.getCampaignId()) return false;
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
        final Object this$valueRemaining = this.getValueRemaining();
        final Object other$valueRemaining = other.getValueRemaining();
        if (this$valueRemaining == null ? other$valueRemaining != null : !this$valueRemaining.equals(other$valueRemaining)) return false;
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 277;
        int result = 1;
        final long $campaignId = this.getCampaignId();
        result = result * PRIME + (int)($campaignId >>> 32 ^ $campaignId);
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 0 : $code.hashCode());
        final long $id = this.getId();
        result = result * PRIME + (int)($id >>> 32 ^ $id);
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 0 : $value.hashCode());
        final Object $valueRemaining = this.getValueRemaining();
        result = result * PRIME + ($valueRemaining == null ? 0 : $valueRemaining.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "Claim(campaignId=" + this.getCampaignId() + ", code=" + this.getCode() + ", id=" + this.getId() + ", value=" + this.getValue() + ", valueRemaining=" + this.getValueRemaining() + ")";
    }

    @SuppressWarnings("all")
    public Claim(final long campaignId, @NonNull final String code, final long id, @Nullable final MonetaryValue value, @Nullable final MonetaryValue valueRemaining) {
        if (code == null) {
            throw new NullPointerException("code");
        }
        this.campaignId = campaignId;
        this.code = code;
        this.id = id;
        this.value = value;
        this.valueRemaining = valueRemaining;
    }
}