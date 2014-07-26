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
