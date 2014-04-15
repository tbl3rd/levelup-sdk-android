/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.storage;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;

import net.jcip.annotations.ThreadSafe;

/**
 * This class is a place to keep preference keys for the {@code core} library.
 */
@ThreadSafe
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public final class CorePreferenceUtil {

    /**
     * Hardcoded prefix avoids names that may be obfuscated by ProGuard.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String KEY_PREFIX = "com.scvngr.levelup.core.storage.preference"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p/>
     * The key used to cache the last location page.
     */
    @NonNull
    public static final String KEY_STRING_LAST_LOCATION_PAGE = KEY_PREFIX
            + ".string_last_location_page"; //$NON-NLS-1$

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private CorePreferenceUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
