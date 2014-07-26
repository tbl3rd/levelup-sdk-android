/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.test.SupportInstrumentationTestCase;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader}.
 */
public final class LevelUpCodeLoaderTest extends SupportInstrumentationTestCase {
    private HashMapCache mCache;
    private LevelUpCodeLoaderUnderTest mLoader;
    private LatchedOnImageLoaded mOnImageLoaded;
    private MockQrCodeGenerator mQrCodeGenerator;
    private String mTestKey1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mQrCodeGenerator = new MockQrCodeGenerator();
        mCache = new HashMapCache();
        mCache.clear();
        mLoader = new LevelUpCodeLoaderUnderTest(mQrCodeGenerator, mCache);
        mTestKey1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);
        mOnImageLoaded = new LatchedOnImageLoaded(mTestKey1);
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader#dispatchOnImageLoaded(String, com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage)}.
     *
     * @throws InterruptedException from {@link java.util.concurrent.CountDownLatch#await}
     */
    public void testDispatchOnImageLoaded() throws InterruptedException {
        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);
        // When dispatch is called, it will call any registered OnImageLoaded callbacks with the
        // image provided.
        mLoader.dispatchOnImageLoaded(mTestKey1, mQrCodeGenerator.mTestImage1);

        assertTrue(
                "countdown did not reach expected amount", mOnImageLoaded.mLatch.await(2, TimeUnit.SECONDS));

        assertTrue(MockQrCodeGenerator.isBitmapForCode(mOnImageLoaded.mLoadedImage,
                MockQrCodeGenerator.TEST_CONTENT1));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader#getKey(String)}.
     */
    public void testGetKey() {
        // Some things that are the same.
        assertTrue(mLoader.getKey("").equals(mLoader.getKey("")));
        assertTrue(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1)));
        assertTrue(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2)));

        // Some things that are different.
        assertFalse(mLoader.getKey("").equals(mLoader.getKey(" ")));
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(mLoader.getKey("")));
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2)));
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT3)));
        assertFalse(mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2).equals(
                mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT3)));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader#getLevelUpCode} with a cache hit.
     */
    public void testGetLevelUpCode_cacheHit() {
        mCache.putCode(mTestKey1, mQrCodeGenerator.mTestImage1);

        final PendingImage<LevelUpQrCodeImage> levelUpCode = getPendingImageOnMainThread();

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertTrue(MockQrCodeGenerator.isBitmapForCode(levelUpCode.getImage(),
                MockQrCodeGenerator.TEST_CONTENT1));

        // Check the loader state.
        assertEquals(0, mLoader.mScheduleLoadKeys.size());
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader#getLevelUpCode} with a cache miss.
     */
    public void testGetLevelUpCode_cacheMiss() {
        final PendingImage<LevelUpQrCodeImage> levelUpCode = getPendingImageOnMainThread();

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertNull(levelUpCode.getImage());
        assertFalse(levelUpCode.isLoaded());

        // Check the loader state.
        assertEquals(1, mLoader.mScheduleLoadKeys.size());
        assertEquals(mTestKey1, mLoader.mScheduleLoadKeys.get(0));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader#registerOnImageLoadedCallback}.
     */
    public void testRegisterOnImageLoad() {
        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);

        assertTrue(mLoader.mLoaderCallbacks.containsKey(mTestKey1));
        assertSame(mOnImageLoaded, mLoader.mLoaderCallbacks.get(mTestKey1));
    }

    /**
     * Tests that {@link LevelUpCodeLoaderUnderTest#startLoadInBackground} works as expected.
     */
    public void testScheduleLoad() {
        final LatchedOnImageLoaded onImageLoaded = new LatchedOnImageLoaded(mTestKey1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLoader.startLoadInBackground(MockQrCodeGenerator.TEST_CONTENT1, mTestKey1,
                        onImageLoaded);
            }
        });

        assertTrue(mLoader.mScheduleLoadKeys.contains(mTestKey1));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.ui.view.LevelUpCodeLoader#unregisterOnImageLoadedCallback(String)}.
     */
    public void testUnregisterOnImageLoad() {

        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);
        mLoader.unregisterOnImageLoadedCallback(mTestKey1);

        assertFalse(mLoader.mLoaderCallbacks.containsKey(mTestKey1));
    }

    /**
     * Gets the {@link com.scvngr.levelup.core.ui.view.PendingImage} from the main thread.
     *
     * @return the {@link com.scvngr.levelup.core.ui.view.PendingImage}
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
