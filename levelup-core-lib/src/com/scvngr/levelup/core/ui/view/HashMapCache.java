package com.scvngr.levelup.core.ui.view;

import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;

/**
 * A simple in-memory cache of the codes. The cache is static, so it'll be shared across all
 * instances of this class.
 */
@ThreadSafe
public final class HashMapCache implements LevelUpCodeCache {
    private static final ConcurrentHashMap<String, LevelUpQrCodeImage> sCache =
            new ConcurrentHashMap<String, LevelUpQrCodeImage>();

    @Override
    public void putCode(@NonNull final String key, @NonNull final LevelUpQrCodeImage image) {
        sCache.put(key, image);
    }

    @Override
    @Nullable
    public LevelUpQrCodeImage getCode(@NonNull final String key) {
        return sCache.get(key);
    }

    @Override
    public boolean hasCode(@NonNull final String key) {
        return sCache.containsKey(key);
    }

    /**
     * Clears the cache.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */void clear() {
        sCache.clear();
    }
}
