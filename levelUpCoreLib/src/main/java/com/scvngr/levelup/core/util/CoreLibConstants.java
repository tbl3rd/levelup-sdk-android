/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.text.format.DateUtils;

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
     * This is set by {@link #setNotProguarded()} in non-proguarded builds; in properly proguarded
     * builds that method will not be called. {@link #PROGUARDED} is set based on this field.
     */
    private static boolean sProguarded = true;

    /**
     * Whether the library is being built into a production app or not (when being compiled with the
     * final project and proguarded at the end). This may also be true for .AAR releases even when
     * built into debug apps, and will be false for prod builds if proguard is off. This is intended
     * to work around <a href=https://code.google.com/p/android/issues/detail?id=52962>this AOSP
     * bug</a> that causes all library projects to be built in release mode when built as part of a
     * dependent project, even when the end build is a debug build itself.
     */
    public static final boolean PROGUARDED;

    static {
        setNotProguarded(); // This method call changes sProguarded, but is stripped by Proguard.
        PROGUARDED = sProguarded;
    }

    /**
     * Flag to turn object representation invariant checking on/off.
     */

    public static final boolean IS_CHECKREP_ENABLED = !PROGUARDED;

    /**
     * Flag to turn method parameter checking on/off.
     */
    public static final boolean IS_PARAMETER_CHECKING_ENABLED = !PROGUARDED;

    /**
     * Flag to enable slow asynchronous accesses.
     */
    public static final boolean IS_SLOW_ASYNC_ENABLED = false;

    /**
     * Version of the SDK, as per <a href="http://semver.org">Semantic Versioning</a>.
     */
    @NonNull
    public static final String SDK_VERSION = "2.0.1.SNAPSHOT"; //$NON-NLS-1$

    /**
     * Delay in milliseconds when slow async operations are enabled.
     *
     * @see #IS_SLOW_ASYNC_ENABLED
     */
    public static final long SLOW_ASYNC_DELAY_MILLIS = 2 * DateUtils.SECOND_IN_MILLIS;

    private CoreLibConstants() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }

    /**
     * Calls to this method should be stripped from release builds by Proguard. This method should
     * only be used in this class's static initializers to set the value of
     * {@link CoreLibConstants#PROGUARDED} and should never be used externally.
     * <p/>
     * Requires the following line in your proguard config or an included one (like the
     * proguard-project.txt that ships with this SDK):
     * <p/>
     * <code>
     * <p/>
     * -assumenosideeffects public class com.scvngr.levelup.core.util.CoreLibConstants
     * {
     * private static void setNotProguarded()
     * }
     * </code>
     */
    private static void setNotProguarded() {
        LogManager.d("This is a debugging build and should not be shipped without using Proguard " +
                "and including the LevelUp SDK's proguard configuration.");
        // This relies on lying to Proguard about the existence of side effects to this method.
        sProguarded = false;
    }
}
