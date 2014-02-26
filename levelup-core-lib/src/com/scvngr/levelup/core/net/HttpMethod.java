/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

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
