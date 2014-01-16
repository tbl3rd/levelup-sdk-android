/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;

import net.jcip.annotations.ThreadSafe;

/**
 * Fixture for {@link MonetaryValue}.
 */
@ThreadSafe
public final class MonetaryValueFixture {
    /**
     * Ten dollars, in cents.
     */
    public static final int TEN_DOLLARS = 1000;

    /**
     * @return a full monetary value object, set to {@link #TEN_DOLLARS} in US dollars.
     */
    @NonNull
    public static MonetaryValue getFullModel() {
        return new MonetaryValue(TEN_DOLLARS);
    }
}
