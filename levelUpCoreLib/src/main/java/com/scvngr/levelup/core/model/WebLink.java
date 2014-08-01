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

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.model.RequiredField;
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
    @RequiredField
    private String title;

    /**
     * The links's enumerated type ID to be used when retreiving the image associated with the link.
     */
    private long webLinkTypeId;

    /**
     * The web URL associated with the link.
     */
    @NonNull
    @RequiredField
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
