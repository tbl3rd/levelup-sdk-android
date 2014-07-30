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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

/**
 * This is a utility class to use {@link android.os.StrictMode} without worrying about backwards
 * compatibility. Calls to this class from unsupported SDKs have no effect.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class StrictModeUtil {

    /**
     * Notes a slow method call with {@link StrictMode}. This method has no effect prior to
     * {@link Build.VERSION_CODES#HONEYCOMB}.
     *
     * @param name tag for the slow call.
     */
    public static void noteSlowCall(@NonNull final String name) {
        PreconditionUtil.assertNotNull(name, "name");

        if (EnvironmentUtil.isSdk11OrGreater()) {
            noteSlowCallHoneycomb(name);
        }
    }

    /**
     * Note a slow call using APIs introduced in {@link Build.VERSION_CODES#HONEYCOMB}.
     *
     * @param name the name of the slow call to note.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void noteSlowCallHoneycomb(@NonNull final String name) {
        StrictMode.noteSlowCall(name);
    }

    /**
     * Sets StrictMode on/off. This method has no effect prior to
     * {@link Build.VERSION_CODES#GINGERBREAD}.
     *
     * @param state true to enable strict mode, false to disable strict mode
     */
    /* package */static void setStrictMode(final boolean state) {
        if (EnvironmentUtil.isSdk9OrGreater()) {
            setStrictModeGingerbread(state);
        }
    }

    /**
     * Sets StrictMode on/off using APIs introduced in {@link Build.VERSION_CODES#GINGERBREAD}.
     *
     * @param state true to enable strict mode, false to disable strict mode
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void setStrictModeGingerbread(final boolean state) {
        if (state) {
            StrictMode.setThreadPolicy(new ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new VmPolicy.Builder().detectAll().penaltyLog().build());
        } else {
            StrictMode.setThreadPolicy(android.os.StrictMode.ThreadPolicy.LAX);
            StrictMode.setVmPolicy(android.os.StrictMode.VmPolicy.LAX);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private StrictModeUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
