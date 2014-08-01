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
package com.scvngr.levelup.core.ui.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;

import net.jcip.annotations.ThreadSafe;

/**
 * A cache for LevelUp payment codes. This is used by {@link LevelUpCodeLoader}. This cache must be
 * thread safe.
 */
@ThreadSafe
public interface LevelUpCodeCache {

    /**
     * @param key the key under which to store the code. This should be unique for each code.
     * @param image the code image. This should be the smallest-possible code image.
     */
    void putCode(@NonNull final String key, @NonNull final LevelUpQrCodeImage image);

    /**
     * @param key the key under which the image is stored.
     * @return the cached code image or {@code null} if there is no entry.
     */
    @Nullable
    LevelUpQrCodeImage getCode(@NonNull final String key);

    /**
     * @param key the key under which the image is stored.
     * @return true if the cache has a mapping for the given key.
     */
    boolean hasCode(@NonNull final String key);
}
