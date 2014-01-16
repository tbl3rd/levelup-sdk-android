/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

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
    GET,

    /**
     * POST request.
     */
    POST,

    /**
     * PUT request.
     */
    PUT,

    /**
     * DELETE request.
     */
    DELETE
}
