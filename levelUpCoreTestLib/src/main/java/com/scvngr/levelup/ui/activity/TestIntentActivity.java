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
package com.scvngr.levelup.ui.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.scvngr.levelup.core.util.LogManager;

import junit.framework.AssertionFailedError;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * An activity for testing Intents.
 */
public class TestIntentActivity extends FragmentActivity {
    private static final long RESULT_WAIT_TIMEOUT_SECONDS = 4;

    @Nullable
    private Instrumentation.ActivityResult mActivityResult;

    /**
     * A latch that counts down when {@link #onActivityResult} is called.
     */
    @NonNull
    private final CountDownLatch mOnActivityResultLatch = new CountDownLatch(1);

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        LogManager.v("Got activity result: %s %s", resultCode, data);
        mActivityResult = new Instrumentation.ActivityResult(resultCode, data);
        mOnActivityResultLatch.countDown();
    }

    /**
     * Gets the result of an {@link #startActivityForResult} as delivered to
     * {@link #onActivityResult}.
     *
     * @return the result that was delivered from the other activity.
     */
    @Nullable
    public final Instrumentation.ActivityResult getActivityResult() {
        return mActivityResult;
    }

    /**
     * Waits for {@link #onActivityResult} to be called, then returns the result.
     *
     * @return the result that was delivered from the other activity.
     * @throws AssertionFailedError if the callback isn't called or is interrupted.
     */
    @Nullable
    public final Instrumentation.ActivityResult waitForActivityResult()
            throws AssertionFailedError {
        try {
            if (!mOnActivityResultLatch.await(RESULT_WAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new AssertionFailedError("onActivityResult was not called.");
            }
        } catch (final InterruptedException e) {
            final AssertionFailedError assertionFailed =
                    new AssertionFailedError("Latch interrupted.");
            assertionFailed.initCause(e);

            throw assertionFailed;
        }

        return mActivityResult;
    }
}

