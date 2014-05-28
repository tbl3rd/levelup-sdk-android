package com.scvngr.levelup.core.test;

import com.scvngr.levelup.core.annotation.NonNull;

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
