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
package com.scvngr.levelup.core.storage;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
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
