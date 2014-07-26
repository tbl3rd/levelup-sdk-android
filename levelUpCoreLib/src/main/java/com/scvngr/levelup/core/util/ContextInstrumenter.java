/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;

import java.util.concurrent.atomic.AtomicReference;

/**
 * An instrumenter of {@link Context}s for test purposes. Activity classes may pass the base context
 * in {@link android.app.Activity#attachBaseContext} to {@link #from} to allow themselves to be
 * instrumented. This technique supports instrumentation tests and unit tests.
 */
public final class ContextInstrumenter {

    /**
     * A reference to a {@link Provider} which instruments a {@link Context}. {@link #from} has no
     * effect if this reference is null.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */ static AtomicReference<Provider> mProvider = new AtomicReference<Provider>();

    /**
     * @param context the {@link Context} to instrument.
     * @return the instrumented {@link Context} or {@code context} itself if a {@link Provider} is
     * not configured.
     */
    public static Context from(@NonNull final Context context) {
        final Provider provider = mProvider.get();

        return null != provider ? provider.get(context) : context;
    }

    /**
     * An provider of instrumented {@link Context}s. Tests are expected to implement this interface.
     */
    public interface Provider {

        /**
         * @param context the {@link Context} being instrumented.
         * @return the instrumented {@link Context}, may be {@code context} itself.
         */
        @NonNull
        Context get(@NonNull final Context context);
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ContextInstrumenter() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
