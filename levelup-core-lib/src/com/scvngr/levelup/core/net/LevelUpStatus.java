/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

/**
 * Enum for translating generic network responses (and exceptions) into more broad categories for
 * use within client apps.
 */
@LevelUpApi(contract = Contract.DRAFT)
public enum LevelUpStatus {
    /**
     * Server responded with success code.
     */
    @NonNull
    OK,

    /**
     * The request was bad before sending.
     */
    @NonNull
    ERROR_BAD_REQUEST,

    /**
     * Server responded that the resource requested couldn't be found.
     */
    @NonNull
    ERROR_NOT_FOUND,

    /**
     * Server responded with JSON that we failed to parse.
     */
    @NonNull
    ERROR_PARSING,

    /**
     * Server responded with an error code.
     */
    @NonNull
    ERROR_SERVER,

    /**
     * The server is down for maintenance.
     */
    @NonNull
    ERROR_MAINTENANCE,

    /**
     * Was unable to get to the server.
     */
    @NonNull
    ERROR_NETWORK,

    /**
     * The response from the server was too big to read.
     */
    @NonNull
    ERROR_RESPONSE_TOO_LARGE,

    /**
     * No response passed and no error thrown, unknown problem.
     */
    @NonNull
    ERROR_UNKNOWN,

    /**
     * The client must be logged in to complete the request.
     */
    @NonNull
    LOGIN_REQUIRED,

    /**
     * The server instructed the client that an upgrade is required.
     */
    @NonNull
    UPGRADE
}
