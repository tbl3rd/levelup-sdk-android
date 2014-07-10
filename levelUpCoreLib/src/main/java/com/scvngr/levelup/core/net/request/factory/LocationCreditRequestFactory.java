package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.RequiresPermission;
import com.scvngr.levelup.core.annotation.SlowOperation;
import com.scvngr.levelup.core.model.Location;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

/**
 * Class to build requests to interact with the endpoint that deals with getting credit at a
 * specified location.
 */
@Immutable
@LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
public final class LocationCreditRequestFactory extends AbstractRequestFactory {

    /**
     * Web service endpoint.
     */
    @NonNull
    protected static final String ENDPOINT = "locations/%s/credit"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link com.scvngr.levelup.core.net
     * .AccessTokenRetriever} to use to get the {@link com.scvngr.levelup.core.model.User}'s {@link
     * com.scvngr.levelup.core.model.AccessToken}.
     */
    public LocationCreditRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to request credit at a given location. If an access token is available, it
     * will be used to show the current user's credit at a location and will require the
     * annotated permission. If no access token is available, the permission is ignored,
     * and returns back available credit for new users.
     *
     * @param location The location to request credit for.
     * @return {@link com.scvngr.levelup.core.net.AbstractRequest} representing a location credit
     * request.
     */
    @NonNull
    @LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
    @RequiresPermission(value = Permissions.PERMISSION_MANAGE_USER_CAMPAIGNS)
    @SlowOperation
    public AbstractRequest buildLocationCreditRequest(@NonNull final Location location) {

        final String endpoint =
                NullUtils.nonNullContract(String.format(ENDPOINT, Long.toString(location.getId())));

        /*
        * TODO: Remove the check for if the AccessToken exists once this ticket gets resolved:
        * https://scvngr.unfuddle.com/a#/projects/36980/tickets/by_number/7826
        * Also remove the SlowOperation annotation.
        */
        String apiVersion = getAccessTokenRetriever() != null &&
                getAccessTokenRetriever().getAccessToken(getContext()) != null ?
                LevelUpRequest.API_VERSION_CODE_V15 : LevelUpRequest.API_VERSION_CODE_V14;

        return new LevelUpRequest(getContext(), HttpMethod.GET, apiVersion, endpoint, null, null,
                getAccessTokenRetriever());
    }
}
