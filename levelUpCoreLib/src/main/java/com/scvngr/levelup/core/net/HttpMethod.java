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
package com.scvngr.levelup.core.net;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

/**
 * Enum for HTTP method types.
 */
@LevelUpApi(contract = Contract.DRAFT)
public enum HttpMethod {
    /**
     * GET request.
     */
    @NonNull
    GET,

    /**
     * POST request.
     */
    @NonNull
    POST,

    /**
     * PUT request.
     */
    @NonNull
    PUT,

    /**
     * DELETE request.
     */
    @NonNull
    DELETE
}
