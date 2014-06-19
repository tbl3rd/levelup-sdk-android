/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
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
