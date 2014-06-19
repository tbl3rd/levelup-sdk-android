/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.Ticket;

/**
 * A JSON factory to create a support {@link Ticket}.
 */
@LevelUpApi(contract = LevelUpApi.Contract.INTERNAL)
public final class TicketJsonFactory extends GsonModelFactory<Ticket> {

    @NonNull
    private static final String TYPE_KEY = "ticket"; //$NON-NLS-1$

    /**
     * Constructor.
     */
    public TicketJsonFactory() {
        super(TYPE_KEY, Ticket.class, true);
    }
}
