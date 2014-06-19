/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.ui.activity;

import android.app.Instrumentation;
import android.content.Intent;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.LogManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import junit.framework.AssertionFailedError;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * An activity for testing Intents.
 */
public class IntentTestActivity extends SherlockFragmentActivity {
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

