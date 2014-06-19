/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;

import com.google.gson.JsonObject;

/**
 * Fixture for {@link Ticket}s.
 */
public final class TicketFixture {

    /**
     * The message for {@link Ticket#getBody}.
     */
    @NonNull
    public static final String MESSAGE_FIXTURE = "How is babby formed?"; //$NON-NLS-1$

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
        ticket.addProperty("body", message); //$NON-NLS-1$
        final JsonObject expected = new JsonObject();
        expected.add("ticket", ticket); //$NON-NLS-1$

        return expected;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private TicketFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
