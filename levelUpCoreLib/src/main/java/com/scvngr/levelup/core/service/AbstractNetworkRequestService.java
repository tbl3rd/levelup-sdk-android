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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.LevelUpConnection;
import com.scvngr.levelup.core.net.LevelUpResponse;
import com.scvngr.levelup.core.util.LogManager;

import java.util.UUID;

/**
 * Abstract base class to be used for services that do network requests in the background and may or
 * may not want to send results back to the clients that triggered the request. Subclasses should
 * make sure that any requests to start this service include a unique token passed in
 * {@link #EXTRA_STRING_TOKEN}. Subclasses may override {@code getRequest(Intent)} to not require a
 * request to be passed in the start Intent.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public abstract class AbstractNetworkRequestService extends IntentService {

    /**
     * Intent action broadcast via {@link LocalBroadcastManager} when the service completes its
     * work.
     * <p>
     * The Intent will contain {@link #EXTRA_PARCELABLE_RESPONSE}, {@link #EXTRA_STRING_TOKEN}, and
     * {@link #EXTRA_BOOLEAN_IS_REQUEST_SUCCESSFUL}.
     */
    public static final String ACTION_REQUEST_FINISHED = AbstractNetworkRequestService.class
            .getName() + ".intent.action.request_finished";

    /**
     * Type: {@code AbstractRequest}.
     * <p>
     * Key mapping to a AbstractRequest that the service will perform. This extra is sent in the
     * Intent to start the service.
     */
    /* package */static final String EXTRA_PARCELABLE_REQUEST = AbstractNetworkRequestService.class
            .getName() + ".extra.PARCELABLE_REQUEST";

    /**
     * Type: {@code Response}.
     * <p>
     * Key mapping to a response from the request the service performed.
     */
    public static final String EXTRA_PARCELABLE_RESPONSE = AbstractNetworkRequestService.class
            .getName() + ".extra.PARCELABLE_RESPONSE";

    /**
     * Type: {@code String}.
     * <p>
     * Key mapping to a token uniquely identifying the original request to the service.
     */
    public static final String EXTRA_STRING_TOKEN = AbstractNetworkRequestService.class.getName()
            + ".extra.STRING_TOKEN";

    /**
     * Type: {@code boolean}.
     * <p>
     * Key mapping to a boolean indicating whether the request was successful (e.g. the response
     * doesn't contain an error).
     */
    public static final String EXTRA_BOOLEAN_IS_REQUEST_SUCCESSFUL =
            AbstractNetworkRequestService.class.getName() + ".extra.BOOLEAN_IS_REQUEST_SUCCESSFUL";

    /**
     * Constructs a new instance of the service.
     */
    public AbstractNetworkRequestService() {
        super(AbstractNetworkRequestService.class.getSimpleName());
        setIntentRedelivery(false);
    }

    /**
     * Handle the response from the server.
     *
     * @param context the Application context.
     * @param response the {@link LevelUpResponse} received from the server.
     * @return true if the response indicated a successful request.
     */
    protected abstract boolean handleResponse(@NonNull final Context context,
            @NonNull final LevelUpResponse response);

    @Override
    public void onHandleIntent(final Intent intent) {
        performRequest(getApplicationContext(), intent);
    }

    /**
     * Performs the request to the server, handles the response, and sends a broadcast when the
     * process is done.
     *
     * @param context the Application context.
     * @param intent the intent that was used to start the service.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */void performRequest(@NonNull final Context context, @NonNull final Intent intent) {
        final AbstractRequest request = getRequest(intent);

        if (null != request) {
            LogManager.v("Sending request in the background: %s", request);

            final LevelUpResponse response =
                    LevelUpConnection.newInstance(context).send(request);

            LogManager.v("Response from background request %s", response.getStatus());
            final boolean success = handleResponse(context, response);
            onRequestFinished(context, intent, response, success);
        } else {
            LogManager.w("No request passed");
        }
    }

    /**
     * Get the request to send with this service. Default implementation gets the request from the
     * intent (stored in key {@link #EXTRA_PARCELABLE_REQUEST}). Subclasses can override this to
     * build a request differently.
     *
     * @param intent the intent used to start the service.
     * @return {@link AbstractRequest} to send with the service.
     */
    @Nullable
    protected AbstractRequest getRequest(@NonNull final Intent intent) {
        return intent.getParcelableExtra(EXTRA_PARCELABLE_REQUEST);
    }

    /**
     * Gets a unique token that identifies a request. Subclasses should make sure to add a token to
     * the Intent sent to any instances of this class because receivers use the tokens to determine
     * if a result is intended for them.
     *
     * @return the token to use to identify a request.
     */
    @NonNull
    protected static String getToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Called when the request finished. Takes the response, success flag and request token and
     * broadcasts them to the {@link android.content.BroadcastReceiver}s listening for the action
     * {@link #ACTION_REQUEST_FINISHED}.
     *
     * @param context the Application context.
     * @param intent the intent to get the request token from.
     * @param response the {@link LevelUpResponse} received during the request.
     * @param success true if the request was successful, false otherwise.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static void onRequestFinished(@NonNull final Context context,
            @NonNull final Intent intent, @NonNull final LevelUpResponse response,
            final boolean success) {
        final Intent resultIntent = new Intent(ACTION_REQUEST_FINISHED);
        resultIntent.putExtra(EXTRA_STRING_TOKEN, intent.getStringExtra(EXTRA_STRING_TOKEN));
        resultIntent.putExtra(EXTRA_BOOLEAN_IS_REQUEST_SUCCESSFUL, success);
        resultIntent.putExtra(EXTRA_PARCELABLE_RESPONSE, response);
        LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
    }
}
