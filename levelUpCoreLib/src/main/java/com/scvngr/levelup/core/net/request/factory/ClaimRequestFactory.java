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
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.RequiresPermission;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.JSONObjectRequestBody;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Builds requests to claim {@link com.scvngr.levelup.core.model.Campaign}s.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class ClaimRequestFactory extends AbstractRequestFactory {

    /**
     * Outer parameter key for LegacyLoyalty.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static final String OUTER_PARAM_LEGACY_LOYALTY = "legacy_loyalty";

    /**
     * Parameter key for legacy loyalty card number.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static final String PARAM_LEGACY_ID = "legacy_id";

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public ClaimRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to claim a "Legacy Loyalty" campaign. A Legacy Loyalty campaign transfers a
     * user's loyalty data from their old loyalty system to LevelUp.
     *
     * @param loyaltyCampaignId the web service ID of the legacy loyalty campaign.
     * @param loyaltyId the user's ID from their legacy loyalty program.
     * @return the {@link AbstractRequest} to use to claim the legacy loyalty campaign.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_MANAGE_USER_CAMPAIGNS)
    @AccessTokenRequired
    public AbstractRequest buildClaimLegacyLoyaltyRequest(final int loyaltyCampaignId,
            @NonNull final String loyaltyId) {
        final JSONObject object = new JSONObject();
        final JSONObject legacyLoyaltyObject = new JSONObject();

        try {
            legacyLoyaltyObject.put(PARAM_LEGACY_ID, loyaltyId);
            object.put(OUTER_PARAM_LEGACY_LOYALTY, legacyLoyaltyObject);
        } catch (final JSONException e) {
            LogManager.e("Error Building legacy Loyalty claim request", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format("loyalties/legacy/%d/claims",
                        loyaltyCampaignId), null, new JSONObjectRequestBody(object),
                getAccessTokenRetriever());
    }

    /**
     * Build a request to claim a generic campaign by its cohort code.
     *
     * @param code the code to claim.
     * @return the {@link AbstractRequest} to use to claim the
     *         {@link com.scvngr.levelup.core.model.Campaign}.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_MANAGE_USER_CAMPAIGNS)
    @AccessTokenRequired
    public AbstractRequest buildClaimCampaignRequest(@NonNull final String code) {
        String codeToClaim = code;

        try {
            codeToClaim = URLEncoder.encode(code, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            LogManager.e("Unsupported encoding when encoding code to claim", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format(
                        "codes/%s/claims", codeToClaim), null, null,
                getAccessTokenRetriever());
    }
}
