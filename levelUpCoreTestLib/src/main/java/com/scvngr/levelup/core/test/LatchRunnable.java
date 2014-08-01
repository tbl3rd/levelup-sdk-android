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

package com.scvngr.levelup.core.test;

import android.support.annotation.NonNull;

import java.util.concurrent.CountDownLatch;

/**
 * An abstract {@link Runnable} that has a {@link CountDownLatch}.
 */
public abstract class LatchRunnable implements Runnable {

    @NonNull
    private final CountDownLatch mLatch;

    /**
     * Constructor that initializes the latch with a count of 1.
     */
    public LatchRunnable() {
        this(1);
    }

    /**
     * Constructor that initializes the latch with a count of {@code count}.
     *
     * @param count the initial count for the latch.
     */
    public LatchRunnable(final int count) {
        this(new CountDownLatch(count));
    }

    /**
     * Constructs with the given latch.
     *
     * @param latch the {@link CountDownLatch}.
     */
    public LatchRunnable(@NonNull final CountDownLatch latch) {
        mLatch = latch;
    }

    /**
     * Convenience for {@link CountDownLatch#countDown}.
     */
    public final void countDown() {
        mLatch.countDown();
    }

    /**
     * @return the result from {@link CountDownLatch#getCount}.
     */
    public final long getCount() {
        return mLatch.getCount();
    }

    /**
     * @return the {@link CountDownLatch}.
     */
    @NonNull
    public final CountDownLatch getLatch() {
        return mLatch;
    }
}
