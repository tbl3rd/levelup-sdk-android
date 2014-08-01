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
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;

/**
 * Request a list of {@link com.scvngr.levelup.core.model.Category}s from the server.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class CategoryRequestFactory extends AbstractRequestFactory {

    /**
     * The API endpoint for categories.
     */
    @NonNull
    public static final String ENDPOINT = "categories";

    /**
     * @param context the Application context.
     */
    public CategoryRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * @return a request factory to get the categories.
     */
    @NonNull
    public AbstractRequest getCategories() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, ENDPOINT, null, null);
    }
}
