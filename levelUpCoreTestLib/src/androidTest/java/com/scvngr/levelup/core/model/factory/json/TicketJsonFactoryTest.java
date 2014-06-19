/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.TicketFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.TicketJsonFactory}.
 */
public final class TicketJsonFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests serialization.
     */
    @SmallTest
    public void testBasic() {
        assertEquals(TicketFixture.getJsonModel(),
                new TicketJsonFactory().toJsonElement(TicketFixture.getModel()));
    }
}
