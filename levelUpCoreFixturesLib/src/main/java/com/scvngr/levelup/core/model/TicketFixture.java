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
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

/**
 * Fixture for {@link Ticket}s.
 */
public final class TicketFixture {

    /**
     * The message for {@link Ticket#getBody}.
     */
    @NonNull
    public static final String MESSAGE_FIXTURE = "How is babby formed?";

    /**
     * @return a fully-populated {@link Ticket}.
     */
    @NonNull
    public static Ticket getModel() {
        return new Ticket(MESSAGE_FIXTURE);
    }

    /**
     * @return a full {@link Ticket} model.
     */
    @NonNull
    public static JsonObject getJsonModel() {
        return getJsonModel(MESSAGE_FIXTURE);
    }

    /**
     * @param message The message for {@link Ticket#getBody}.
     * @return a full {@link Ticket} model with a specific message.
     */
    @NonNull
    public static JsonObject getJsonModel(@NonNull final String message) {
        final JsonObject ticket = new JsonObject();
        ticket.addProperty("body", message);
        final JsonObject expected = new JsonObject();
        expected.add("ticket", ticket);

        return expected;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private TicketFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
