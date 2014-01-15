/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.os.HandlerThread;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

/**
 * This is a utility class for thread management on Android.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class ThreadUtil {
    /**
     * Obtains a new {@link HandlerThread} that is started.
     *
     * @param name Name to give to the HandlerThread. Useful for debugging, as the thread name is
     *            shown in DDMS.
     * @param priority Priority of the thread. One of the {@link android.os.Process} priority
     *            constants.
     * @return HandlerThread whose {@link HandlerThread#start()} method has already been called.
     */
    @NonNull
    public static HandlerThread getHandlerThread(@NonNull final String name, final int priority) {
        PreconditionUtil.assertNotNull(name, "name"); //$NON-NLS-1$

        final HandlerThread thread = new HandlerThread(name, priority);

        thread.start();

        return thread;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ThreadUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
