package com.scvngr.levelup.ui.test;

import net.jcip.annotations.Immutable;

/**
 * Constants used by the tests.
 */
@Immutable
public final class Constants {

    /**
     * Example URL used for tests.
     */
    public static final String URL_EXAMPLE = "http://www.example.com/"; //$NON-NLS-1$

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private Constants() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
