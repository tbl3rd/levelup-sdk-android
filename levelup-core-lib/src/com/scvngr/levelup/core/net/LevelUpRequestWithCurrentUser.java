/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.User;

/**
 * {@link LevelUpRequest} subclass that formats the ID of the current {@link User}
 * into the request URL at request time.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class LevelUpRequestWithCurrentUser extends LevelUpRequest {

    /**
     * Creator for parceling.
     */
    public static final Creator<LevelUpRequestWithCurrentUser> CREATOR =
            new Creator<LevelUpRequestWithCurrentUser>() {

                @Override
                public LevelUpRequestWithCurrentUser[] newArray(final int size) {
                    return new LevelUpRequestWithCurrentUser[size];
                }

                @Override
                public LevelUpRequestWithCurrentUser
                        createFromParcel(final Parcel in) {
                    return new LevelUpRequestWithCurrentUser(in);
                }
            };

    /**
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to request.
     * @param endpoint the endpoint of the web service to request.
     * @param queryParams parameters to add to the query portion of the request.
     * @param bodyParams the parameters for the body of the request.
     * @param retriever the {@link AccessTokenRetriever} subclass to use to get the
     *        {@link AccessToken} for the current user.
     * @throws IllegalArgumentException if any of the required parameters are not passed.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONArray bodyParams, @Nullable final AccessTokenRetriever retriever) {
        super(context, method, apiVersion, endpoint, queryParams, bodyParams, retriever);
    }

    /**
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to request.
     * @param endpoint the endpoint of the web service to request.
     * @param queryParams parameters to add to the query portion of the request.
     * @param bodyParams the parameters for the body of the request.
     * @throws IllegalArgumentException if any of the required parameters are not passed.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONArray bodyParams) {
        super(context, method, apiVersion, endpoint, queryParams, bodyParams);
    }

    /**
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to request.
     * @param endpoint the endpoint of the web service to request.
     * @param queryParams parameters to add to the query portion of the request.
     * @param bodyParams the parameters for the body of the request.
     * @param retriever the {@link AccessTokenRetriever} subclass to use to get the
     *        {@link AccessToken} for the current user.
     * @throws IllegalArgumentException if any of the required parameters are not passed.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONObject bodyParams, @Nullable final AccessTokenRetriever retriever) {
        super(context, method, apiVersion, endpoint, queryParams, bodyParams, retriever);
    }

    /**
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param apiVersion the version of the LevelUp web service API to request.
     * @param endpoint the endpoint of the web service to request.
     * @param queryParams parameters to add to the query portion of the request.
     * @param bodyParams the parameters for the body of the request.
     * @throws IllegalArgumentException if any of the required parameters are not passed.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final JSONObject bodyParams) {
        super(context, method, apiVersion, endpoint, queryParams, bodyParams);
    }

    /**
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param url the URL to request.
     * @param bodyParams the parameters for the body of the request.
     * @param retriever the {@link AccessTokenRetriever} subclass to use to get the
     *        {@link AccessToken} for the current user.
     * @throws IllegalArgumentException if any of the required parameters are not passed.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final JSONArray bodyParams, @Nullable final AccessTokenRetriever retriever)
            throws IllegalArgumentException {
        super(context, method, url, bodyParams, retriever);
    }

    /**
     * @param context the Application context.
     * @param method the {@link HttpMethod} for this request.
     * @param url the URL to request.
     * @param bodyParams the parameters for the body of the request.
     * @param retriever the {@link AccessTokenRetriever} subclass to use to get the
     *        {@link AccessToken} for the current user.
     * @throws IllegalArgumentException if any of the required parameters are not passed.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final Uri url,
            @Nullable final JSONObject bodyParams, @Nullable final AccessTokenRetriever retriever)
            throws IllegalArgumentException {
        super(context, method, url, bodyParams, retriever);
    }

    /**
     * @param in the {@link Parcel} to restore from.
     */
    public LevelUpRequestWithCurrentUser(@NonNull final Parcel in) {
        super(in);
    }

    /**
     * Formats the user ID of the current user into the URL string.
     *
     * @throws BadRequestException if the user is not logged in.
     */
    @Override
    @NonNull
    public String getUrlString(@NonNull final Context context) throws BadRequestException {
        final AccessToken token = getAccessToken(context);

        if (null == token) {
            throw new BadRequestException("user is not logged in, cannot format URL string"); //$NON-NLS-1$
        }

        return String.format(Locale.US, super.getUrlString(context), token.getUserId());
    }
}
