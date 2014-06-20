/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.text.format.DateUtils;

import com.scvngr.levelup.core.BuildConfig;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

import net.jcip.annotations.ThreadSafe;

/**
 * This is a utility class containing global constants for the app.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class CoreLibConstants {
    /**
     * Flag to turn object representation invariant checking on/off.
     */
    public static final boolean IS_CHECKREP_ENABLED = BuildConfig.DEBUG;

    /**
     * Flag to turn method parameter checking on/off.
     */
    public static final boolean IS_PARAMETER_CHECKING_ENABLED = BuildConfig.DEBUG;

    /**
     * Flag to enable slow asynchronous accesses.
     */
    public static final boolean IS_SLOW_ASYNC_ENABLED = false;

    /**
     * Version of the SDK, as per <a href="http://semver.org">Semantic Versioning</a>.
     */
    @NonNull
    public static final String SDK_VERSION = "1.4.0"; //$NON-NLS-1$

    /**
     * Delay in milliseconds when slow async operations are enabled.
     *
     * @see #IS_SLOW_ASYNC_ENABLED
     */
    public static final long SLOW_ASYNC_DELAY_MILLIS = 2 * DateUtils.SECOND_IN_MILLIS;

    private CoreLibConstants() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
