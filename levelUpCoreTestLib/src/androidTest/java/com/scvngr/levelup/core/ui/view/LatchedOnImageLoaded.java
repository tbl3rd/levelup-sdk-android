/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;
import com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded;

import java.util.concurrent.CountDownLatch;

/**
 * An {@link com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded} implementation that has a {@link java.util.concurrent.CountDownLatch} which decrements when
 * {@link #onImageLoaded(String, com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage)} is called.
 */
public final class LatchedOnImageLoaded implements OnImageLoaded<LevelUpQrCodeImage> {
    /**
     * The countdown latch, starting at 1.
     */
    public final CountDownLatch mLatch = new CountDownLatch(1);

    private final String mExpectedKey;

    /**
     * The image that is passed into {@link #onImageLoaded(String, com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage)}.
     */
    @Nullable
    public volatile LevelUpQrCodeImage mLoadedImage;

    /**
     * @param expectedKey the key that's expected to be passed to
     *        {@link #onImageLoaded(String, com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage)}. The countdown only triggers when
     *        this key is passed.
     */
    public LatchedOnImageLoaded(final String expectedKey) {
        mExpectedKey = expectedKey;
    }

    @Override
    public void
            onImageLoaded(@NonNull final String loadKey, @NonNull final LevelUpQrCodeImage image) {
        mLoadedImage = image;

        if (mExpectedKey.equals(loadKey)) {
            mLatch.countDown();
        }
    }
}
