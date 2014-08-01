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
