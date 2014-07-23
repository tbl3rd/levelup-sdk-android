package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.net.Uri;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.storage.CorePreferenceUtil;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.HashMap;
import java.util.Map;

/**
 * A list of locations for a specific application ID. This list is paged using the Link header. See
 * {@link com.scvngr.levelup.core.util.WebLinkParser}.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class AppLocationListRequestFactory extends AbstractPagingRequestFactory {

    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /*package*/ static final String PARAM_LAT = "lat";

    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /*package*/ static final String PARAM_LNG = "lng";

    @Nullable
    private final android.location.Location mLocation;

    /**
     * The target app's web service ID.
     */
    private final long mAppId;

    /**
     * Constructor.
     *
     * @param context the context.
     * @param retriever the access token retriever.
     * @param pageCacheRetriever the retriever to use to get the last retrieved page from the cache.
     * @param appId The target app's web service ID associated with the paging locations request
     * @param location Optional location to make requests on for paging.
     */
    public AppLocationListRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever,
            @NonNull final PageCacheRetriever pageCacheRetriever, final long appId,
            @Nullable final android.location.Location location) {
        super(context, retriever, pageCacheRetriever,
                CorePreferenceUtil.KEY_STRING_LAST_APP_ID_LOCATION_PAGE);
        mAppId = appId;
        mLocation = location;
    }

    @Override
    @NonNull
    public AbstractRequest getFirstPageRequest() {
        final Map<String, String> queryParams = buildQueryParams(mLocation);

        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14,
                NullUtils.format("apps/%d/locations", mAppId), queryParams, null);
    }

    @Override
    @Nullable
    public AbstractRequest getPageRequest(@NonNull final Uri page) {
        try {
            return new LevelUpRequest(getContext(), HttpMethod.GET, page, null,
                    getAccessTokenRetriever());
        } catch (final IllegalArgumentException e) {
            LogManager.e("error parsing URL for next page", e);
            return null;
        }
    }

    @Nullable
    private Map<String, String>
            buildQueryParams(@Nullable final android.location.Location location) {
        Map<String, String> queryParams = null;
        if (null != location) {
            queryParams = new HashMap<String, String>();

            queryParams.put(PARAM_LAT, Double.toString(location.getLatitude()));
            queryParams.put(PARAM_LNG, Double.toString(location.getLongitude()));
        }

        return queryParams;
    }
}
