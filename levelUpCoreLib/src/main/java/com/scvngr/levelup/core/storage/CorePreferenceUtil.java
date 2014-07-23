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
    /* package */static final String KEY_PREFIX = "com.scvngr.levelup.core.storage.preference";

    /**
     * Type: {@code String}.
     * <p/>
     * The key used to cache the last location page.
     */
    @NonNull
    public static final String KEY_STRING_LAST_LOCATION_PAGE = KEY_PREFIX
            + ".string_last_location_page";

    /**
     * Type: {@code String}.
     * <p/>
     * The key used to cache the last location page by target app's web service ID.
     */
    @NonNull
    public static final String KEY_STRING_LAST_APP_ID_LOCATION_PAGE = KEY_PREFIX
            + ".string_last_app_id_location_page";

    /**
     * Type: {@code Long}.
     * <p/>
     * The key used to cache the current user's ID.
     */
    @NonNull
    public static final String KEY_STRING_USER_ID = KEY_PREFIX
            + ".long_user_id";

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private CorePreferenceUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
