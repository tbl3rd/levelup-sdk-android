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
package com.scvngr.levelup.core.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
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
    public static void startService(@NonNull final Context context,
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
