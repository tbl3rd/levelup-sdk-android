/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.Location;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Builder;

/**
 * Represents external link information about a {@link Location}.
 */
@Immutable
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.PUBLIC)
public class WebLink implements Parcelable {

    /**
     * Implements the {@code Parcelable} interface.
     */
    @NonNull
    public static final Creator<WebLink> CREATOR = new WebLinkCreator();

    /**
     * The link's displayable text.
     */
    @NonNull
    private String title;

    /**
     * The links's enumerated type ID to be used when retreiving the image associated with the link.
     */
    private long webLinkTypeId;

    /**
     * The web URL associated with the link.
     */
    @NonNull
    private String webUrl;

    /**
     * Implements parceling/unparceling for {@link WebLink}.
     */
    @Immutable
    private static final class WebLinkCreator implements Creator<WebLink> {

        @Override
        public WebLink[] newArray(final int size) {
            return new WebLink[size];
        }

        @Override
        @NonNull
        public WebLink createFromParcel(final Parcel in) {
            final ClassLoader loader = getClass().getClassLoader();

            final WebLinkBuilder builder = WebLink.builder();

            builder.title(in.readString());
            builder.webLinkTypeId(in.readLong());
            builder.webUrl(in.readString());

            return NullUtils.nonNullContract(builder.build());
        }

        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final WebLink webLink) {
            dest.writeString(webLink.title);
            dest.writeLong(webLink.webLinkTypeId);
            dest.writeString(webLink.webUrl);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((WebLinkCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), flags, this);
    }
}
