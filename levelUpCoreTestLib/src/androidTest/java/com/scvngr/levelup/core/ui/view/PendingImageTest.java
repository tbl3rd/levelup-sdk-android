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

import android.net.Uri;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.ui.view.PendingImage.LoadCancelable;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests {@link com.scvngr.levelup.core.ui.view.PendingImage} using a class that has a good .equals() method ({@link android.net.Uri}) to make
 * comparison easier.
 */
public final class PendingImageTest extends TestCase {

    private static final String TEST_KEY = "foo";
    private static final Uri TEST_CONTENT1 = Uri.parse("urn:foo");

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
     * Tests {@link com.scvngr.levelup.core.ui.view.PendingImage#setImage(Object)}.
     */
    public void testSetImage() {
        mImage.setImage(TEST_CONTENT1);
        assertEquals(TEST_CONTENT1, mImage.getImage());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.PendingImage#cancelLoad()}.
     *
     * @throws InterruptedException
     */
    public void testOnCancel() throws InterruptedException {
        mImage.cancelLoad();
        assertTrue(mLoadCancelable.mLatch.await(1, TimeUnit.SECONDS));
    }

    /**
     * A {@link com.scvngr.levelup.core.ui.view.PendingImage.LoadCancelable} which has a {@link java.util.concurrent.CountDownLatch} on its
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
