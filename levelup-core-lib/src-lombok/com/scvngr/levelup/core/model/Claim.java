package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;
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
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
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
        ((ClaimCreator) CREATOR).writeToParcel(dest, flags, this);
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
            final MonetaryValue valueRemaining =
                    in.readParcelable(MonetaryValue.class.getClassLoader());

            return new Claim(campaignId, code, id, value, valueRemaining);
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Claim claim) {
            dest.writeLong(claim.getCampaignId());
            dest.writeString(claim.getCode());
            dest.writeLong(claim.getId());
            dest.writeParcelable(claim.getValue(), flags);
            dest.writeParcelable(claim.getValueRemaining(), flags);
        }
    }
}
