package com.scvngr.levelup.core.net;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

/**
 * Enum for translating generic network responses (and exceptions) into more broad categories for
 * use within client apps.
 */
@LevelUpApi(contract = Contract.DRAFT)
public enum LevelUpStatus {
    /**
     * Server responded with success code.
     */
    OK,

    /**
     * The request was bad before sending.
     */
    ERROR_BAD_REQUEST,

    /**
     * Server responded that the resource requested couldn't be found.
     */
    ERROR_NOT_FOUND,

    /**
     * Server responded with JSON that we failed to parse.
     */
    ERROR_PARSING,

    /**
     * Server responded with an error code.
     */
    ERROR_SERVER,

    /**
     * The server is down for maintenance.
     */
    ERROR_MAINTENANCE,

    /**
     * Was unable to get to the server.
     */
    ERROR_NETWORK,

    /**
     * The response from the server was too big to read.
     */
    ERROR_RESPONSE_TOO_LARGE,

    /**
     * No response passed and no error thrown, unknown problem.
     */
    ERROR_UNKNOWN,

    /**
     * The client must be logged in to complete the request.
     */
    LOGIN_REQUIRED,

    /**
     * The server instructed the client that an upgrade is required.
     */
    UPGRADE
}
