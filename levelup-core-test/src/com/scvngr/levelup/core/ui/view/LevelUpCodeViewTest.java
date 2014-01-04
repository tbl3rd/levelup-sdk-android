/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.test.R;
import com.scvngr.levelup.core.test.TestThreadingUtils;
import com.scvngr.levelup.core.ui.view.LevelUpCodeView.OnCodeLoadListener;
import com.scvngr.levelup.core.util.EnvironmentUtil;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.ui.activity.FragmentTestActivity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests {@link LevelUpCodeView}.
 */
@SuppressWarnings("javadoc")
public final class LevelUpCodeViewTest extends
        ActivityInstrumentationTestCase2<FragmentTestActivity> {

    private HashMapCache mCache;
    private LevelUpCodeView mLevelUpCodeView;
    private LevelUpCodeLoaderUnderTest mLoader;
    private MockQrCodeGenerator mQrCodeGenerator;

    public LevelUpCodeViewTest() {
        super(FragmentTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mCache = new HashMapCache();
        mCache.clear();
        mQrCodeGenerator = new MockQrCodeGenerator();
        mLoader = new LevelUpCodeLoaderUnderTest(mQrCodeGenerator, mCache);

        final FragmentTestActivity activity = getActivity();
        getInstrumentation().waitForIdleSync();

        TestThreadingUtils.runOnMainSync(getInstrumentation(), activity, new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView = new LevelUpCodeView(activity);

                final int layoutSize;

                if (EnvironmentUtil.isSdk11OrGreater()) {
                    layoutSize = LayoutParams.MATCH_PARENT;
                } else {
                    /*
                     * Pre-Honeycomb devices do not fare well when rapidly allocating large bitmaps
                     * during tests. Choose a small layout size that is large enough to validate
                     * pixel scaling.
                     */
                    layoutSize = MockQrCodeGenerator.TEST_IMAGE_SIZE * 3;
                }

                ((ViewGroup) activity.findViewById(R.id.levelup_activity_content)).addView(
                        mLevelUpCodeView, new LinearLayout.LayoutParams(layoutSize, layoutSize));
            }
        });
    }

    @Override
    protected void tearDown() throws Exception {
        /*
         * Manage Memory on Android 2.3.3 and Lower
         * https://developer.android.com/training/displaying-bitmaps/manage-memory.html#recycle
         */
        if (!EnvironmentUtil.isSdk11OrGreater()) {
            TestThreadingUtils.runOnMainSync(getInstrumentation(), getActivity(), new Runnable() {
                @Override
                public void run() {
                    mLevelUpCodeView.destroyDrawingCache();
                }
            });
        }

        super.tearDown();
    }

    /**
     * Tests that the test view is set up properly.
     */
    @SmallTest
    public void testSetUpCorrectly() {
        assertNotNull(mLevelUpCodeView);
        assertTrue(mLevelUpCodeView.isShown());
    }

    /**
     * Tests {@link LevelUpCodeView#setLevelUpCode(String, LevelUpCodeLoader)} with a cache hit.
     *
     * @throws InterruptedException
     */
    @MediumTest
    public void testShowCode_cacheHit() throws InterruptedException {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        final LatchingOnCodeLoadListener onCodeLoadListener = new LatchingOnCodeLoadListener();
        mLevelUpCodeView.setOnCodeLoadListener(onCodeLoadListener);

        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        /*
         * The callback should only be called once with loading=false, so progress bars and such can
         * be handled properly.
         */
        assertOnCodeLoaded(onCodeLoadListener, false, 1);
        assertOnCodeLoaded(onCodeLoadListener, true, 0);

        mQrCodeGenerator.isBitmapForCode(mLevelUpCodeView.mCurrentCode,
                MockQrCodeGenerator.TEST_CONTENT1);
    }

    /**
     * Tests {@link LevelUpCodeView#setLevelUpCode(String, LevelUpCodeLoader)} with a cache miss.
     *
     * @throws InterruptedException
     */
    @MediumTest
    public void testShowCode_cacheMiss() throws InterruptedException {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        final LatchingOnCodeLoadListener onCodeLoadListener = new LatchingOnCodeLoadListener();
        mLevelUpCodeView.setOnCodeLoadListener(onCodeLoadListener);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertOnCodeLoaded(onCodeLoadListener, false, 0);
        assertOnCodeLoaded(onCodeLoadListener, true, 1);

        final boolean[] result = new boolean[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // Now simulate that the code loaded in the background.
                result[0] = mLoader.dispatchOnImageLoaded(key1, mQrCodeGenerator.mTestImage1);
            }
        });

        assertTrue("dispatchOnImageLoaded's callback was not called", result[0]); //$NON-NLS-1$

        assertOnCodeLoaded(onCodeLoadListener, false, 1);
        assertOnCodeLoaded(onCodeLoadListener, true, 1);
    }

    /**
     * Tests {@link LevelUpCodeView#setLevelUpCode(String, LevelUpCodeLoader)} being called twice,
     * to change the code.
     *
     * @throws InterruptedException
     */
    @MediumTest
    public void testShowCode_changing() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);
        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        final LatchingOnCodeLoadListener onCodeLoadListener = new LatchingOnCodeLoadListener();
        mLevelUpCodeView.setOnCodeLoadListener(onCodeLoadListener);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        mQrCodeGenerator.isBitmapForCode(mLevelUpCodeView.mCurrentCode,
                MockQrCodeGenerator.TEST_CONTENT1);

        assertTestColorPixelEquals(getLevelUpCodeViewDrawingCache(),
                MockQrCodeGenerator.TEST_CONTENT1_COLOR);

        assertOnCodeLoaded(onCodeLoadListener, false, 1);
        assertOnCodeLoaded(onCodeLoadListener, true, 0);

        // Now change the code.

        final String key2 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2);
        mCache.putCode(key2, mQrCodeGenerator.mTestImage2);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT2, mLoader);
            }
        });

        mQrCodeGenerator.isBitmapForCode(mLevelUpCodeView.mCurrentCode,
                MockQrCodeGenerator.TEST_CONTENT2);

        assertTestColorPixelEquals(getLevelUpCodeViewDrawingCache(),
                MockQrCodeGenerator.TEST_CONTENT2_COLOR);

        assertOnCodeLoaded(onCodeLoadListener, false, 2);
        assertOnCodeLoaded(onCodeLoadListener, true, 0);
    }

    /**
     * Tests {@link LevelUpCodeView#setLevelUpCode(String, LevelUpCodeLoader)} being called twice,
     * with the same code.
     *
     * @throws InterruptedException
     */
    @MediumTest
    public void testShowCode_unchanged() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);
        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        final LatchingOnCodeLoadListener onCodeLoadListener = new LatchingOnCodeLoadListener();
        mLevelUpCodeView.setOnCodeLoadListener(onCodeLoadListener);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        mQrCodeGenerator.isBitmapForCode(mLevelUpCodeView.mCurrentCode,
                MockQrCodeGenerator.TEST_CONTENT1);

        assertTestColorPixelEquals(getLevelUpCodeViewDrawingCache(),
                MockQrCodeGenerator.TEST_CONTENT1_COLOR);

        assertOnCodeLoaded(onCodeLoadListener, false, 1);
        assertOnCodeLoaded(onCodeLoadListener, true, 0);

        // Set the same code again.

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertTestColorPixelEquals(getLevelUpCodeViewDrawingCache(),
                MockQrCodeGenerator.TEST_CONTENT1_COLOR);

        assertOnCodeLoaded(onCodeLoadListener, false, 2);
        assertOnCodeLoaded(onCodeLoadListener, true, 0);
    }

    /**
     * Tests that {@link LevelUpCodeView}'s colorizing of the QR code matches the expected color
     * pattern.
     */
    @MediumTest
    public void testShowCode_colorizing() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);
        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        final LatchingOnCodeLoadListener onCodeLoadListener = new LatchingOnCodeLoadListener();
        mLevelUpCodeView.setOnCodeLoadListener(onCodeLoadListener);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setFadeColors(false);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        mQrCodeGenerator.isBitmapForCode(mLevelUpCodeView.mCurrentCode,
                MockQrCodeGenerator.TEST_CONTENT1);

        final Bitmap drawingCache = getLevelUpCodeViewDrawingCache();

        final Resources resources = getActivity().getResources();
        final int orange = resources.getColor(R.color.levelup_logo_orange);
        final int blue = resources.getColor(R.color.levelup_logo_blue);
        final int green = resources.getColor(R.color.levelup_logo_green);

        assertScaledPixelIsColor(drawingCache, MockQrCodeGenerator.TARGET_RIGHT,
                MockQrCodeGenerator.TARGET_BOTTOM, MockQrCodeGenerator.TEST_IMAGE_SIZE, blue);
        assertScaledPixelIsColor(drawingCache, MockQrCodeGenerator.TARGET_RIGHT,
                MockQrCodeGenerator.TARGET_TOP, MockQrCodeGenerator.TEST_IMAGE_SIZE, orange);
        assertScaledPixelIsColor(drawingCache, MockQrCodeGenerator.TARGET_LEFT,
                MockQrCodeGenerator.TARGET_BOTTOM, MockQrCodeGenerator.TEST_IMAGE_SIZE, green);
    }

    /**
     * Tests that {@link LevelUpCodeView}'s colorizing of the QR code is disabled when
     * {@link LevelUpCodeView#setColorize(boolean)} is false.
     */
    @MediumTest
    public void testShowCode_notColorizing() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // Disable colorization
                mLevelUpCodeView.setColorize(false);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        final Bitmap drawingCache = getLevelUpCodeViewDrawingCache();

        final Resources resources = getActivity().getResources();
        final int black = resources.getColor(android.R.color.black);

        assertScaledPixelIsColor(drawingCache, MockQrCodeGenerator.TARGET_RIGHT,
                MockQrCodeGenerator.TARGET_BOTTOM, MockQrCodeGenerator.TEST_IMAGE_SIZE, black);
        assertScaledPixelIsColor(drawingCache, MockQrCodeGenerator.TARGET_RIGHT,
                MockQrCodeGenerator.TARGET_TOP, MockQrCodeGenerator.TEST_IMAGE_SIZE, black);
        assertScaledPixelIsColor(drawingCache, MockQrCodeGenerator.TARGET_LEFT,
                MockQrCodeGenerator.TARGET_BOTTOM, MockQrCodeGenerator.TEST_IMAGE_SIZE, black);
    }

    /**
     * Tests that {@link LevelUpCodeView}'s color fading is animating the color alpha.
     */
    @MediumTest
    public void testShowCode_fading() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        final long timeoutMillis = LevelUpCodeView.ANIM_FADE_START_DELAY_MILLIS
                + LevelUpCodeView.ANIM_FADE_DURATION_MILLIS + TimeUnit.SECONDS.toMillis(4);
        final CountDownLatch latch = new CountDownLatch(1);
        assertTrue(TestThreadingUtils.waitForAction(getInstrumentation(), getActivity(),
                new Runnable() {
                    @Override
                    public void run() {
                        if (LevelUpCodeView.ANIM_FADE_COLOR_ALPHA_END
                                == mLevelUpCodeView.mColorAlpha) {
                            latch.countDown();
                        }
                    }
                }, latch, timeoutMillis, true));
    }

    /**
     * Tests that {@link LevelUpCodeView}'s color fading is disabled when calling
     * {@link LevelUpCodeView#setFadeColors(boolean)}.
     */
    @MediumTest
    public void testShowCode_notFading() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setFadeColors(false);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        final Animation animation = mLevelUpCodeView.getAnimation();
        assertNull(animation);
        assertEquals(LevelUpCodeView.ANIM_FADE_COLOR_ALPHA_START, mLevelUpCodeView.mColorAlpha);
    }

    /**
     * Tests that {@link LevelUpCodeView} isn't doing a fade animation when the colorization is
     * disabled.
     */
    @MediumTest
    public void testShowCode_notFadingBecauseNotColorized() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // Disable colorization
                mLevelUpCodeView.setColorize(false);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        getInstrumentation().waitForIdleSync();

        final Animation animation = mLevelUpCodeView.getAnimation();
        assertNull(animation);
        assertEquals(LevelUpCodeView.ANIM_FADE_COLOR_ALPHA_START, mLevelUpCodeView.mColorAlpha);
    }

    /**
     * Tests that setLevelUpCode enforces being called from the correct thread.
     */
    @MediumTest
    public void testShowCode_wrongThread() {
        try {
            mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            fail("Expected exception not thrown."); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception.
        }
    }

    /**
     * Tests that the {@link LevelUpCodeView} is square and not 0px wide.
     */
    @SmallTest
    public void testSizing() {
        assertTrue(0 < mLevelUpCodeView.getWidth());
        // The view should be square.
        assertEquals(mLevelUpCodeView.getWidth(), mLevelUpCodeView.getHeight());
    }

    /**
     * Asserts the number of calls to {@link LevelUpCodeView.OnCodeLoadListener#onCodeLoad} that
     * were performed with the specified loading state.
     *
     * @param listener The {@link LatchingOnCodeLoadListener}.
     * @param expectedIsCodeLoading The expected isCodeLoading state that should have been delivered
     * the listener.
     * @param expectedCount The expected number of times the callback has been invoked with
     * the specified loading state.
     */
    private void assertOnCodeLoaded(@NonNull final LatchingOnCodeLoadListener listener,
            final boolean expectedIsCodeLoading, final int expectedCount) {
        final int expectedLatchCount = expectedCount == 0 ? 1 : 0;
        final CountDownLatch latch = new CountDownLatch(1);
        assertTrue(TestThreadingUtils.waitForAction(getInstrumentation(), getActivity(), new Runnable() {
            @Override
            public void run() {
                if (expectedIsCodeLoading) {
                    if (expectedLatchCount == listener.isCodeLoadingTrueLatch.getCount()) {
                        latch.countDown();
                    }
                } else {
                    if (expectedLatchCount == listener.isCodeLoadingFalseLatch.getCount()) {
                        latch.countDown();
                    }
                }
            }
        }, latch, false));

        if (expectedIsCodeLoading) {
            assertEquals(expectedCount, listener.isCodeLoadingTrueCount);
        } else {
            assertEquals(expectedCount, listener.isCodeLoadingFalseCount);
        }
    }

    /**
     * Asserts that the given pixel in the provided bitmap is a given color, given the coordinates
     * of the pre-scaled-up pixels.
     *
     * @param bitmap the full-size bitmap.
     * @param x pre-scaled x.
     * @param y pre-scaled y.
     * @param miniSize size of the pre-scaled bitmap.
     * @param expectedColor the expected color.
     */
    private void assertScaledPixelIsColor(@NonNull final Bitmap bitmap, final int x, final int y,
            final int miniSize, final int expectedColor) {
        final int largeSize = bitmap.getWidth();
        final float scaleFactor = (float) largeSize / miniSize;
        assertEquals(expectedColor, bitmap.getPixel((int) (scaleFactor * x + scaleFactor / 2),
                (int) (scaleFactor * y + scaleFactor / 2)));
    }

    /**
     * Asserts that the pixel used to test bitmap equality is a given color.
     *
     * @param bitmap the bitmap to check.
     * @param expectedColor the expected color.
     */
    private void assertTestColorPixelEquals(@NonNull final Bitmap bitmap, final int expectedColor) {
        assertScaledPixelIsColor(bitmap, MockQrCodeGenerator.TEST_COLOR_PIXEL,
                MockQrCodeGenerator.TEST_COLOR_PIXEL, MockQrCodeGenerator.TEST_IMAGE_SIZE,
                expectedColor);
    }

    /**
     * Get the drawing cache from {@link #mLevelUpCodeView}.
     *
     * @return the drawing cache.
     */
    @NonNull
    private Bitmap getLevelUpCodeViewDrawingCache() {
        if (EnvironmentUtil.isSdk11OrGreater()) {
            return NullUtils.nonNullContract(getLevelUpCodeViewDrawingCacheHelper());
        }

        /*
         * Attempt to build the drawing cache multiple times on older devices. This can take a while
         * due to the unpredictable nature of native memory management.
         */

        final long endTime = SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(20);

        while (true) {
            getInstrumentation().waitForIdleSync();

            final Bitmap bitmap = getLevelUpCodeViewDrawingCacheHelper();

            if (null != bitmap) {
                return NullUtils.nonNullContract(bitmap);
            }

            assertTrue("Timed out getting the code view drawing cache.", //$NON-NLS-1$
                    SystemClock.elapsedRealtime() < endTime);

            SystemClock.sleep(500);
        }
    }

    /**
     * Helper to obtain the drawing cache bitmap. This may temporarily return null on older devices.
     * Clients should use {@link #getLevelUpCodeViewDrawingCache} instead of this method.
     *
     * @return the drawing cache bitmap or null if the drawing cache could not be built.
     */
    @Nullable
    private Bitmap getLevelUpCodeViewDrawingCacheHelper() {
        final AtomicReference<Bitmap> drawingCacheReference = new AtomicReference<Bitmap>();

        TestThreadingUtils.runOnMainSync(getInstrumentation(), getActivity(), new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.destroyDrawingCache();
                mLevelUpCodeView.buildDrawingCache();

                drawingCacheReference.set(mLevelUpCodeView.getDrawingCache());
            }
        });

        return drawingCacheReference.get();
    }

    /**
     * An {@link OnCodeLoadListener} that tallies calls to its callback.
     */
    private static class LatchingOnCodeLoadListener implements OnCodeLoadListener {
        public int isCodeLoadingFalseCount = 0;
        public final CountDownLatch isCodeLoadingFalseLatch = new CountDownLatch(1);
        public int isCodeLoadingTrueCount = 0;
        public final CountDownLatch isCodeLoadingTrueLatch = new CountDownLatch(1);

        @Override
        public void onCodeLoad(final boolean isCodeLoading) {
            if (isCodeLoading) {
                isCodeLoadingTrueLatch.countDown();
                isCodeLoadingTrueCount++;
            } else {
                isCodeLoadingFalseLatch.countDown();
                isCodeLoadingFalseCount++;
            }
        }
    }
}
