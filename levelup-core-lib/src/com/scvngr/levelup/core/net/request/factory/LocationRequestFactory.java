/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.jcip.annotations.Immutable;

import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.Location;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.DeviceUtil;

/**
 * Factory to build requests to interact with the {@link Location} related web service endpoints.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class LocationRequestFactory extends AbstractRequestFactory {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    static final String PARAM_LAT = "lat"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    static final String PARAM_LNG = "lng"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     */
    public LocationRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * @param appId the ID of the App on the web service.
     * @param location the location of the user. Used to sort the returned locations by distance.
     * @return the list of locations associated with the app.
     */
    @NonNull
    public AbstractRequest buildGetAppLocationsListRequest(final long appId,
            @Nullable final android.location.Location location) {
        final Map<String, String> queryParams = new HashMap<String, String>();

        if (null != location) {
            queryParams.put(PARAM_LAT, Double.toString(location.getLatitude()));
            queryParams.put(PARAM_LNG, Double.toString(location.getLongitude()));
        }

        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "apps/%d/locations", appId), queryParams, (JSONObject) null); //$NON-NLS-1$
    }

    /**
     * @param locationWebServiceId the ID of the {@link Location} on the web service.
     * @return an {@link AbstractRequest} that represents a request to the web service to get the
     *         details of the {@link Location} with the ID passed.
     */
    @NonNull
    public AbstractRequest buildGetLocationDetailsRequest(final long locationWebServiceId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "locations/%d", locationWebServiceId), null, (JSONObject) null); //$NON-NLS-1$
    }

    /**
     * @param context the Application context.
     * @param locationWebServiceId the web service ID of the {@link Location} to get the image for.
     * @return an {@link AbstractRequest} that represents a request to the web service to get the
     *         image for a {@link Location} with the web service ID passed.
     */
    @NonNull
    public AbstractRequest buildGetLocationImageRequest(@NonNull final Context context,
            final long locationWebServiceId) {
        final String deviceDensity = DeviceUtil.getDeviceDensityString(context);
        final HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(PARAM_DENSITY, deviceDensity);
        queryParams.put(PARAM_WIDTH, DEFAULT_WIDTH);
        queryParams.put(PARAM_HEIGHT, DEFAULT_HEIGHT);
        return new LevelUpRequest(
                context,
                HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14,
                String.format(Locale.US, "locations/%d/image", locationWebServiceId), queryParams, (JSONObject) null); //$NON-NLS-1$
    }
}
