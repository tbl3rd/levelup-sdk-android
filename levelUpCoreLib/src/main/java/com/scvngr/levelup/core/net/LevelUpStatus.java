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
