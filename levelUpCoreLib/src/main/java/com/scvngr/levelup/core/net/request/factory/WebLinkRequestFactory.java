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

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.WebLink;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

/**
 * Factory to build requests to interact with the {@link WebLink} related web service end points.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class WebLinkRequestFactory extends AbstractRequestFactory {

    /**
     * The endpoint for a given location's {@link WebLink}.
     */
    @NonNull
    private static final String ENDPOINT_WEB_LINKS_FORMAT = "locations/%s/web_links";

    /**
     * @param context the Application context.
     */
    public WebLinkRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * @param locationWebServiceId the web service ID of the location to load the {@link WebLink}
     *        for.
     * @return an {@link AbstractRequest} to get the {@link WebLink}s for the location.
     */
    @NonNull
    public AbstractRequest buildGetWebLinksForLocationRequest(final long locationWebServiceId) {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, NullUtils.format(ENDPOINT_WEB_LINKS_FORMAT,
                        locationWebServiceId), null, null,
                getAccessTokenRetriever());
    }
}
