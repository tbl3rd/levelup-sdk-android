/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;
import com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded;

import java.util.ArrayList;

/**
 * A mock implementation of the code loader. Calls are tallied as public fields. The QR code
 * generator is not called.
 */
public final class LevelUpCodeLoaderUnderTest extends LevelUpCodeLoader {
    public final ArrayList<String> mOnCancelLoadKeys = new ArrayList<String>();
    public int mOnCancelLoads = 0;
    public final ArrayList<String> mScheduleLoadKeys = new ArrayList<String>();

    public LevelUpCodeLoaderUnderTest(final LevelUpQrCodeGenerator qrCodeGenerator,
            final LevelUpCodeCache codeCache) {
        super(qrCodeGenerator, codeCache);
    }

    @Override
    protected void onCancelLoad(@NonNull final String loadKey) {
        mOnCancelLoadKeys.add(loadKey);
    }

    @Override
    protected void onCancelLoads() {
        mOnCancelLoads++;
    }

    @Override
    protected void onStartLoadInBackground(@NonNull final String qrCodeContents,
            @NonNull final String key,
            @Nullable final OnImageLoaded<LevelUpQrCodeImage> onImageLoaded) {
        mScheduleLoadKeys.add(key);
    }
}
