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
