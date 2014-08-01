/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.util;

import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

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
        PreconditionUtil.assertNotNull(name, "name");

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
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
