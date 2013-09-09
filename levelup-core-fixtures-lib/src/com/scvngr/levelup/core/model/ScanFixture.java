package com.scvngr.levelup.core.model;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.NonNull;

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
        return new Scan("data"); //$NON-NLS-1$
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ScanFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
