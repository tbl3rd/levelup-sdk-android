package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.WebLink;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.DeviceUtil;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.HashMap;

/**
 * Factory to build requests to retrieve images for {@link WebLink}s.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class WebLinkTypeRequestFactory extends AbstractRequestFactory {

    /**
     * Default icon width size in dips.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */ static final String DEFAULT_ICON_WIDTH_DIP = "25";

    /**
     * Default icon height size in dips.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */ static final String DEFAULT_ICON_HEIGHT_DIP = "25";

    /**
     * @param context the Application context.
     */
    public WebLinkTypeRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * Build a request to get the icon image for the {@link WebLink} passed.
     *
     * @param webLink the {@link WebLink} to get the image type id for.
     * @return an {@link AbstractRequest} to use to get the image for the {@link WebLink}
     *         passed.
     */
    @NonNull
    public AbstractRequest buildLinkIconImageRequest(@NonNull final WebLink webLink) {
        final String deviceDensity = DeviceUtil.getDeviceDensityString(getContext());
        final HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put(PARAM_DENSITY, deviceDensity);
        queryParams.put(PARAM_WIDTH, DEFAULT_ICON_WIDTH_DIP);
        queryParams.put(PARAM_HEIGHT, DEFAULT_ICON_HEIGHT_DIP);
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format(
                        "web_link_types/%d/image", webLink.getWebLinkTypeId()), queryParams, null);
    }
}
