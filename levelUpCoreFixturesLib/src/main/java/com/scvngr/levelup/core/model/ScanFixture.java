/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

import net.jcip.annotations.ThreadSafe;

/**
 * Fixture for {@link Scan}s.
 */
@ThreadSafe
public final class ScanFixture {

    /**
     * @return a fully populated {@link Scan}.
     */
    @NonNull
    public static Scan getFullModel() {
        return new Scan("data");
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ScanFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
