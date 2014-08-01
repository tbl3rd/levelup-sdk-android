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
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Request factory for fetching a {@link com.scvngr.levelup.core.model.Registration} from the web
 * service.
 */
@LevelUpApi(contract = LevelUpApi.Contract.ENTERPRISE)
public final class RegistrationRequestFactory extends AbstractRequestFactory {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static final String ENDPOINT_REGISTRATION = "registration";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static final String PARAM_EMAIL = "email";

    /**
     * @param context the Application context.
     */
    public RegistrationRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * Build a request to get the {@link com.scvngr.levelup.core.model.Registration} for the email
     * passed.
     *
     * @param email the user's email address.
     * @return an {@link AbstractRequest} to use to get the
     *         {@link com.scvngr.levelup.core.model.Registration} for the email passed.
     */
    @NonNull
    @LevelUpApi(contract = LevelUpApi.Contract.ENTERPRISE)
    public AbstractRequest buildRegistrationRequest(@NonNull final String email) {
        PreconditionUtil.assertNotNull(email, "email");

        final Map<String, String> params = new HashMap<String, String>();
        RequestUtils.addApiKeyToRequestQueryParams(getContext(), params);
        params.put(PARAM_EMAIL, email);

        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, ENDPOINT_REGISTRATION, params, null);
    }
}
