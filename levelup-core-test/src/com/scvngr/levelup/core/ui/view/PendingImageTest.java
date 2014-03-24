/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.net.Uri;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.ui.view.PendingImage.LoadCancelable;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests {@link PendingImage} using a class that has a good .equals() method ({@link Uri}) to make
 * comparison easier.
 */
public final class PendingImageTest extends TestCase {

    private static final String TEST_KEY = "foo"; //$NON-NLS-1$
    private static final Uri TEST_CONTENT1 = Uri.parse("urn:foo"); //$NON-NLS-1$

    private LatchingLoadCancelable mLoadCancelable;
    private PendingImage<Uri> mImage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mLoadCancelable = new LatchingLoadCancelable(TEST_KEY);
        mImage = new PendingImage<Uri>(mLoadCancelable, TEST_KEY);
    }

    /**
     * Tests the constructor and its initial condition.
     */
    public void testConstructor() {
        assertEquals(TEST_KEY, mImage.getLoadKey());
        assertNull(mImage.getImage());
    }

    /**
     * Tests {@link PendingImage#setImage(Object)}.
     */
    public void testSetImage() {
        mImage.setImage(TEST_CONTENT1);
        assertEquals(TEST_CONTENT1, mImage.getImage());
    }

    /**
     * Tests {@link PendingImage#cancelLoad()}.
     *
     * @throws InterruptedException
     */
    public void testOnCancel() throws InterruptedException {
        mImage.cancelLoad();
        assertTrue(mLoadCancelable.mLatch.await(1, TimeUnit.SECONDS));
    }

    /**
     * A {@link LoadCancelable} which has a {@link CountDownLatch} on its
     * {@link #cancelLoad(String)}.
     */
    private static class LatchingLoadCancelable implements LoadCancelable {

        public final CountDownLatch mLatch = new CountDownLatch(1);
        @NonNull
        private final String mExpectedKey;

        /**
         * @param expectedKey the key that is expected in the callback. Only this will trigger the
         *        countdown.
         */
        public LatchingLoadCancelable(@NonNull final String expectedKey) {
            mExpectedKey = expectedKey;
        }

        @Override
        public void cancelLoad(@NonNull final String loadKey) {
            if (mExpectedKey.equals(loadKey)) {
                mLatch.countDown();
            }
        }
    }
}
