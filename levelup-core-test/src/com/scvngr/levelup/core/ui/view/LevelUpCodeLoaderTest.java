package com.scvngr.levelup.core.ui.view;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;

import java.util.concurrent.TimeUnit;

/**
 * Tests {@link LevelUpCodeLoader}.
 */
@SuppressWarnings("javadoc")
public final class LevelUpCodeLoaderTest extends SupportAndroidTestCase {
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
     * Tests {@link LevelUpCodeLoader#dispatchOnImageLoaded(String, LevelUpQrCodeImage)}.
     *
     * @throws InterruptedException from {@link java.util.concurrent.CountDownLatch#await}
     */
    public void testDispatchOnImageLoaded() throws InterruptedException {
        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);
        // When dispatch is called, it will call any registered OnImageLoaded callbacks with the
        // image provided.
        mLoader.dispatchOnImageLoaded(mTestKey1, mQrCodeGenerator.mTestImage1);

        assertTrue(
                "countdown did not reach expected amount", mOnImageLoaded.mLatch.await(2, TimeUnit.SECONDS)); //$NON-NLS-1$

        assertTrue(mQrCodeGenerator.isBitmapForCode(mOnImageLoaded.mLoadedImage,
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
        mCache.putCode(mTestKey1, mQrCodeGenerator.mTestImage1);

        final PendingImage<LevelUpQrCodeImage> levelUpCode =
                mLoader.getLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mOnImageLoaded);

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertTrue(mQrCodeGenerator.isBitmapForCode(levelUpCode.getImage(),
                MockQrCodeGenerator.TEST_CONTENT1));

        // Check the loader state.
        assertEquals(0, mLoader.mScheduleLoadKeys.size());
    }

    /**
     * Tests {@link LevelUpCodeLoader#getLevelUpCode} with a cache miss.
     */
    public void testGetLevelUpCode_cacheMiss() {
        final PendingImage<LevelUpQrCodeImage> levelUpCode =
                mLoader.getLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mOnImageLoaded);

        // Check the PendingImage
        assertEquals(mTestKey1, levelUpCode.getLoadKey());
        assertNull(levelUpCode.getImage());
        assertFalse(levelUpCode.isLoaded());

        // Check the loader state.
        assertEquals(1, mLoader.mScheduleLoadKeys.size());
        assertEquals(mTestKey1, mLoader.mScheduleLoadKeys.get(0));
    }

    /**
     * Tests {@link LevelUpCodeLoader#registerOnImageLoadedCallback}.
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

        mLoader.startLoadInBackground(MockQrCodeGenerator.TEST_CONTENT1, mTestKey1, onImageLoaded);
        assertTrue(mLoader.mScheduleLoadKeys.contains(mTestKey1));
    }

    /**
     * Tests {@link LevelUpCodeLoader#unregisterOnImageLoadedCallback(String)}.
     */
    public void testUnregisterOnImageLoad() {

        mLoader.registerOnImageLoadedCallback(mTestKey1, mOnImageLoaded);
        mLoader.unregisterOnImageLoadedCallback(mTestKey1);

        assertFalse(mLoader.mLoaderCallbacks.containsKey(mTestKey1));
    }
}
