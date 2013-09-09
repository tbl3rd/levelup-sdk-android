package com.scvngr.levelup.core.test;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

/**
 * Context wrapper that redirects all getSharedPreferences calls to have a ".testsafe" prefix, so we
 * don't bork our login data between tests.
 */
public final class PrefsPreservingContextWrapper extends ContextWrapper {
    /**
     * Simple constructor.
     *
     * @param base Base context to be wrapped
     */
    public PrefsPreservingContextWrapper(final Context base) {
        super(base);
    }

    @Override
    public SharedPreferences getSharedPreferences(final String name, final int mode) {
        return super.getSharedPreferences(name + ".testsafe", mode); //$NON-NLS-1$
    }
}
