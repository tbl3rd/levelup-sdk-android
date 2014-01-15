/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.Locale;
import java.util.Map;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.AccessToken;

/**
 * Subclass of {@link LevelUpV13Request} which will format the url string to include the id
 * of the currently logged in user. Will also always append the access token if it's available.
 */
@Deprecated
@LevelUpApi(contract = Contract.INTERNAL)
public final class LevelUpV13RequestWithCurrentUser extends LevelUpV13Request {

    /**
     * Creator for parceling.
     */
    public static final Creator<LevelUpV13RequestWithCurrentUser> CREATOR =
            new Creator<LevelUpV13RequestWithCurrentUser>() {

                @Override
                public LevelUpV13RequestWithCurrentUser[] newArray(final int size) {
                    return new LevelUpV13RequestWithCurrentUser[size];
                }

                @Override
                public LevelUpV13RequestWithCurrentUser createFromParcel(final Parcel in) {
                    return new LevelUpV13RequestWithCurrentUser(in);
                }
            };

    /**
     * Constructor with default API version. See
     * {@link LevelUpV13Request#DEFAULT_API_VERSION}.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for the request.
     * @param endpoint the endpoint to hit. Must include the proper %d format string for formatting
     *        the user id in.
     * @param queryParams the list of query params.
     * @param postParams the post parameters.
     * @param retriever the access token retriever.
     * @deprecated please use the constructor which explicitly states the API version.
     */
    @Deprecated
    public LevelUpV13RequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String endpoint,
            @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> postParams,
            @Nullable final AccessTokenRetriever retriever) {
        super(context, method, endpoint, queryParams, postParams, retriever);
    }

    /**
     * Constructor.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for the request.
     * @param apiVersion the API version code.
     * @param endpoint the endpoint to hit. Must include the proper %d format string for formatting
     *        the user id in.
     * @param queryParams the list of query params.
     * @param postParams the post parameters.
     * @param retriever the access token retriever.
     */
    public LevelUpV13RequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, @NonNull final String apiVersion,
            @NonNull final String endpoint, @Nullable final Map<String, String> queryParams,
            @Nullable final Map<String, String> postParams,
            @Nullable final AccessTokenRetriever retriever) {
        super(context, method, apiVersion, endpoint, queryParams, postParams, retriever);
    }

    /**
     * Constructor.
     *
     * @param context the Application context.
     * @param method the {@link HttpMethod} for the request.
     * @param url the request URL. This can include query parameters.
     * @param postParams the post parameters.
     * @param retriever the access token retriever.
     * @throws IllegalArgumentException if the URI specified isn't an absolute URL.
     */
    public LevelUpV13RequestWithCurrentUser(@NonNull final Context context,
            @NonNull final HttpMethod method, final Uri url,
            @Nullable final Map<String, String> postParams,
            @Nullable final AccessTokenRetriever retriever) throws IllegalArgumentException {
        super(context, method, url, postParams, retriever);
    }

    /**
     * Constructor for parceling.
     *
     * @param in the parcel to read from.
     */
    public LevelUpV13RequestWithCurrentUser(final Parcel in) {
        super(in);
    }

    /**
     * {@inheritDoc}.
     *
     * Formats the user id of the current user into the url string.
     *
     * @throws BadRequestException if the user is not logged in.
     */
    @Override
    @NonNull
    public String getUrlString(@NonNull final Context context) throws BadRequestException {
        final AccessToken token = getAccessToken(context);

        if (null == token) {
            throw new BadRequestException("user is not logged in, cannot format url string"); //$NON-NLS-1$
        }

        return String.format(Locale.US, super.getUrlString(context), token.getUserId());
    }
}
