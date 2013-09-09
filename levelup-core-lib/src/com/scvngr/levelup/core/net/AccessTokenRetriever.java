package com.scvngr.levelup.core.net;

import android.content.Context;
import android.os.Parcelable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.AccessToken;

/**
 * Interface for a class that can be used to retrieve the currently logged in
 * {@link com.scvngr.levelup.core.model.User}'s {@link AccessToken}.
 */
@LevelUpApi(contract = Contract.DRAFT)
public interface AccessTokenRetriever extends Parcelable {

    /**
     * @param context the Application context.
     * @return the {@link AccessToken} for the {@link com.scvngr.levelup.core.model.User}.
     */
    @Nullable
    public AccessToken getAccessToken(@NonNull final Context context);
}
