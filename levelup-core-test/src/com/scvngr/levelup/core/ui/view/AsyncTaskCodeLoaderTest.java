/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.test.SupportInstrumentationTestCase;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests {@link AsyncTaskCodeLoader} using a {@link MockQrCodeGenerator}, {@link HashMapCache}, and
 * {@link LatchedOnImageLoaded}.
 */
public final class AsyncTaskCodeLoaderTest extends SupportInstrumentationTestCase {

    private MockQrCodeGenerator mQrCodeGenerator;
    private HashMapCache mCache;
    private AsyncTaskCodeLoader mLoader;
    private String mTestKey1;
    private LatchedOnImageLoaded mOnImageLoaded;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mQrCodeGenerator = new MockQrCodeGenerator();
        mCache = new HashMapCache();
        // The cache uses a static map
        mCache.clear();
        mLoader = new AsyncTaskCodeLoader(mQrCodeGenerator, mCache);
        mTestKey1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);
        mOnImageLoaded = new LatchedOnImageLoaded(mTestKey1);
    }

    /**
     * Tests {@link LevelUpCodeLoader#dispatchOnImageLoaded(String, LevelUpQrCodeImage)}.
     *
     * @throws InterruptedException upon interruption
     */
    public void testDispatchOnImageLoaded() throws InterruptedException {
        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);
        // When dispatch is called, it will call any registered OnImageLoaded callbacks with the
        // image provided.
        mLoader.dispatchOnImageLoaded(mTestKey1, mQrCodeGenerator.mTestImage1);

        assertTrue("countdown did not reach expected amount", //$NON-NLS-1$
                mOnImageLoaded.mLatch.await(2, TimeUnit.SECONDS));

        assertTrue(MockQrCodeGenerator.isBitmapForCode(mOnImageLoaded.mLoadedImage,
                MockQrCodeGenerator.TEST_CONTENT1));
    }

    /**
     * Tests {@link LevelUpCodeLoader#getKey(String)}.
     */
    public void testGetKey() {
        // Some things that are the same.
        assertTrue(mLoader.getKey("").equals(mLoader.getKey(""))); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1)));
        assertTrue(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2)));

        // Some things that are different.
        assertFalse(mLoader.getKey("").equals(mLoader.getKey(" "))); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(mLoader.getKey(""))); //$NON-NLS-1$
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2)));
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT3)));
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT3)));
    }

    /**
     * Tests {@link LevelUpCodeLoader#getLevelUpCode} with a cache hit.
     */
    public void testGetLevelUpCode_cacheHit() {
        final CountDownLatch latch = mQrCodeGenerator.addCountdownLatch();
        mCache.putCode(mTestKey1, mQrCodeGenerator.mTestImage1);
        final PendingImage<LevelUpQrCodeImage> levelUpCode = getPendingImageOnMainThread();

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertTrue(MockQrCodeGenerator.isBitmapForCode(levelUpCode.getImage(),
                MockQrCodeGenerator.TEST_CONTENT1));

        // Check the loader state.
        assertEquals(0, mLoader.mAsyncTasks.size());
        latch.countDown();
    }

    /**
     * Tests {@link LevelUpCodeLoader#getLevelUpCode} with a cache miss.
     */
    public void testGetLevelUpCode_cacheMiss() {
        final CountDownLatch latch = mQrCodeGenerator.addCountdownLatch();
        final PendingImage<LevelUpQrCodeImage> levelUpCode = getPendingImageOnMainThread();

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertFalse(levelUpCode.isLoaded());
        assertNull(levelUpCode.getImage());

        // Check the loader state.
        assertEquals(1, mLoader.mAsyncTasks.size());
        assertTrue(mLoader.mAsyncTasks.containsKey(mTestKey1));

        latch.countDown();
    }

    /**
     * Tests loading a code in the background.
     *
     * @throws InterruptedException upon interruption
     */
    public void testGetLevelUpCode_background() throws InterruptedException {
        final CountDownLatch latch = mQrCodeGenerator.addCountdownLatch();
        final PendingImage<LevelUpQrCodeImage> levelUpCode = getPendingImageOnMainThread();

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertFalse(levelUpCode.isLoaded());
        assertNull(levelUpCode.getImage());

        // Check the loader state.
        assertEquals(1, mLoader.mAsyncTasks.size());
        assertTrue(mLoader.mAsyncTasks.containsKey(mTestKey1));

        latch.countDown();

        if (!mOnImageLoaded.mLatch.await(4, TimeUnit.SECONDS)) {
            fail("latch timeout exceeded"); //$NON-NLS-1$
        }

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertNotNull(levelUpCode.getImage());
        assertTrue(MockQrCodeGenerator.isBitmapForCode(levelUpCode.getImage(),
                MockQrCodeGenerator.TEST_CONTENT1));

        assertEquals(levelUpCode.getImage(), mOnImageLoaded.mLoadedImage);

        getInstrumentation().waitForIdleSync();

        // Check the loader state.
        assertEquals(0, mLoader.mAsyncTasks.size());
        assertFalse(mLoader.mAsyncTasks.containsKey(mTestKey1));
    }

    /**
     * Tests {@link LevelUpCodeLoader#registerOnImageLoadedCallback}.
     *
     * @throws InterruptedException upon interruption
     */
    public void testRegisterOnImageLoad() throws InterruptedException {
        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);

        assertTrue(mLoader.mLoaderCallbacks.containsKey(mTestKey1));
        assertSame(mOnImageLoaded, mLoader.mLoaderCallbacks.get(mTestKey1));
    }

    /**
     * Tests that {@link AsyncTaskCodeLoader#startLoadInBackground} works as expected.
     */
    public void testScheduleLoad() {
        final LatchedOnImageLoaded onImageLoaded = new LatchedOnImageLoaded(mTestKey1);
        final CountDownLatch latch = mQrCodeGenerator.addCountdownLatch();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLoader.startLoadInBackground(MockQrCodeGenerator.TEST_CONTENT1, mTestKey1,
                        onImageLoaded);
            }
        });

        assertTrue(mLoader.mAsyncTasks.containsKey(mTestKey1));
        latch.countDown();
    }

    /**
     * Tests {@link LevelUpCodeLoader#unregisterOnImageLoadedCallback(String)}.
     *
     * @throws InterruptedException upon interruption.
     */
    public void testUnregisterOnImageLoad() throws InterruptedException {
        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);
        mLoader.unregisterOnImageLoadedCallback(mTestKey1);

        assertFalse(mLoader.mLoaderCallbacks.containsKey(mTestKey1));
    }

    /**
     * Gets the {@link PendingImage} from the main thread.
     *
     * @return the {@link PendingImage}
     */
    @NonNull
    private PendingImage<LevelUpQrCodeImage> getPendingImageOnMainThread() {
        final AtomicReference<PendingImage<LevelUpQrCodeImage>> levelUpCodeReference =
                new AtomicReference<PendingImage<LevelUpQrCodeImage>>();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                levelUpCodeReference.set(mLoader.getLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1,
                        mOnImageLoaded));
            }
        });

        final PendingImage<LevelUpQrCodeImage> levelUpCode = levelUpCodeReference.get();
        assertNotNull(levelUpCode);

        return levelUpCode;
    }
}
