/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

import net.jcip.annotations.ThreadSafe;

/**
 * This is a utility class to use {@link android.os.StrictMode} without worrying about backwards
 * compatibility. Calls to this class from unsupported SDKs have no effect.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class StrictModeUtil {

    /**
     * Notes a slow method call with {@link StrictMode}.
     *
     * @param name tag for the slow call.
     */
    public static void noteSlowCall(@NonNull final String name) {
        PreconditionUtil.assertNotNull(name, "name"); //$NON-NLS-1$

        if (EnvironmentUtil.isSdk11OrGreater()) {
            noteSlowCallHoneycomb(name);
        }
    }

    /**
     * Note a slow call using APIs introduced in Android 3.0.
     *
     * @param name the name of the slow call to note.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void noteSlowCallHoneycomb(@NonNull final String name) {
        StrictMode.noteSlowCall(name);
    }

    /**
     * Sets StrictMode on/off.
     *
     * @param state true to enable strict mode, false to disable strict mode
     */
    /* package */static void setStrictMode(final boolean state) {
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
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
