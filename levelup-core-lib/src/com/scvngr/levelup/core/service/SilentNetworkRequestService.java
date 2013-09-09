package com.scvngr.levelup.core.service;

import android.content.Context;
import android.content.Intent;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.LevelUpResponse;

/**
 * Intent Service to send requests in the background that we do not care to read the response of.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class SilentNetworkRequestService extends AbstractNetworkRequestService {

    /**
     * Starts the {@link SilentNetworkRequestService} with the request passed.
     *
     * @param context the application context.
     * @param request the {@link AbstractRequest} to send.
     */
    public static final void startService(@NonNull final Context context,
            @NonNull final AbstractRequest request) {
        final Intent intent = new Intent(context, SilentNetworkRequestService.class);
        intent.putExtra(EXTRA_PARCELABLE_REQUEST, request);
        intent.putExtra(EXTRA_STRING_TOKEN, getToken());
        context.startService(intent);
    }

    @Override
    protected boolean handleResponse(@NonNull final Context context,
            @NonNull final LevelUpResponse response) {
        return true;
    }
}
