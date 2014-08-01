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
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.util.ParcelableArrayList;
import com.scvngr.levelup.core.util.NullUtils;

import java.util.Collection;

/**
 * An immutable {@link java.util.ArrayList} of {@link WebLink}s.
 */
@LevelUpApi(contract = Contract.DRAFT)
public final class WebLinkList extends ParcelableArrayList<WebLink> {
    /**
     * Parcelable creator.
     */
    @NonNull
    public static final Creator<WebLinkList> CREATOR = new Creator<WebLinkList>() {

        @Override
        public WebLinkList[] newArray(final int size) {
            return new WebLinkList[size];
        }

        @Override
        public WebLinkList createFromParcel(final Parcel source) {
            return new WebLinkList(NullUtils.nonNullContract(source));
        }
    };

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 8413279761360446965L;

    /**
    *
    * @param in the parcel to read from.
    */
   public WebLinkList(@NonNull final Parcel in) {
       super(in);
   }

   /**
    * @param source the source collection to copy from.
    */
   public WebLinkList(final Collection<WebLink> source) {
       super(source);
   }

   @Override
   @NonNull
   protected Creator<WebLink> getCreator() {
       return WebLink.CREATOR;
   }
}
