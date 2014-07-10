/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.DeviceUtil;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.HashMap;

/**
 * Builds requests that interact with the Campaigns endpoint.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class CampaignRequestFactory extends AbstractRequestFactory {

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public CampaignRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to get the details of a campaign.
     *
     * @param campaignId the ID of the campaign to get the details of.
     * @return {@link AbstractRequest} to use to get the details of the campaign.
     */
    @NonNull
    public AbstractRequest buildGetCampaignRequest(final int campaignId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14,
                NullUtils.format("campaigns/%d", campaignId), null, null, //$NON-NLS-1$
                getAccessTokenRetriever());
    }

    /**
     * @param context the Application context.
     * @param campaignWebServiceId the web service ID of the
     *        {@link com.scvngr.levelup.core.model.Campaign} to get the image for.
     * @return an {@link AbstractRequest} that represents a request to the web service to get the
     *         image for a {@link com.scvngr.levelup.core.model.Campaign} with the web service ID
     *         passed.
     */
    @NonNull
    public AbstractRequest buildGetCampaignImageRequest(@NonNull final Context context,
            final long campaignWebServiceId) {
        final String deviceDensity = DeviceUtil.getDeviceDensityString(context);
        final HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(PARAM_DENSITY, deviceDensity);
        queryParams.put(PARAM_WIDTH, DEFAULT_WIDTH);
        queryParams.put(PARAM_HEIGHT, DEFAULT_HEIGHT);
        return new LevelUpRequest(context, HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V14,
                NullUtils.format("campaigns/%d/image", campaignWebServiceId), //$NON-NLS-1$
                queryParams, null);
    }

    /**
     * @param campaignWebServiceId the web service ID of the
     *        {@link com.scvngr.levelup.core.model.Campaign} to get the merchants for.
     * @return an {@link AbstractRequest} that represents a request to the web service to get the
     *         list of merchant IDs that its credit is valid at.
     */
    @NonNull
    public AbstractRequest buildGetCampaignMerchantsRequest(final long campaignWebServiceId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, NullUtils.format(
                        "campaigns/%d/merchants", campaignWebServiceId), null, null); //$NON-NLS-1$
    }
}
