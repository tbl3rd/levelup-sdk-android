/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.TicketFixture;

/**
 * Tests {@link JsonElementRequestBody}.
 */
public final class JsonElementRequestBodyTest extends AbstractRequestBodyTest<JsonElementRequestBody> {

    @Override
    @NonNull
    protected JsonElementRequestBody getFixture() {
        return new JsonElementRequestBody(TicketFixture.getJsonModel());
    }
}
