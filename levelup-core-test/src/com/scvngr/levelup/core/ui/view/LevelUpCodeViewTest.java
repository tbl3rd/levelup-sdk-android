package com.scvngr.levelup.core.ui.view;

import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.ui.view.LevelUpCodeView.OnCodeLoadListener;
import com.scvngr.levelup.ui.activity.FragmentTestActivity;

import java.util.concurrent.CountDownLatch;

/**
 * Tests {@link LevelUpCodeView}.
 */
@SuppressWarnings("javadoc")
public final class LevelUpCodeViewTest extends
        ActivityInstrumentationTestCase2<FragmentTestActivity> {

    private HashMapCache mCache;
    private LevelUpCodeView mLevelUpCodeView;
    private LevelUpCodeLoaderUnderTest mLoader;
    private ActivityMonitor mMonitor;
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

        final ActivityResult result = new ActivityResult(0, null);
        mMonitor = new ActivityMonitor(FragmentTestActivity.class.getName(), result, false);
        getInstrumentation().addMonitor(mMonitor);
        final Activity activity = getActivity();
        mMonitor.waitForActivity();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                final ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);

                final LinearLayout innerContent = (LinearLayout) content.getChildAt(0);
                mLevelUpCodeView = new LevelUpCodeView(innerContent.getContext());
                innerContent.addView(mLevelUpCodeView, new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        });

        getInstrumentation().waitForIdleSync();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        getInstrumentation().removeMonitor(mMonitor);
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

        /*
         * The callback should only be called once with loading=false, so progress bars and such can
         * be handled properly.
         */
        assertEquals(1, onCodeLoadListener.isCodeLoadingFalseCount);
        assertEquals(0, onCodeLoadListener.isCodeLoadingTrueCount);

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

        assertEquals(0, onCodeLoadListener.isCodeLoadingFalseCount);
        assertEquals(1, onCodeLoadListener.isCodeLoadingTrueCount);

        final boolean[] result = new boolean[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // Now simulate that the code loaded in the background.
                result[0] = mLoader.dispatchOnImageLoaded(key1, mQrCodeGenerator.mTestImage1);
            }
        });

        assertTrue("dispatchOnImageLoaded's callback was not called", result[0]); //$NON-NLS-1$

        assertEquals(1, onCodeLoadListener.isCodeLoadingFalseCount);
        assertEquals(1, onCodeLoadListener.isCodeLoadingTrueCount);
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

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setDrawingCacheEnabled(true);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        mQrCodeGenerator.isBitmapForCode(mLevelUpCodeView.mCurrentCode,
                MockQrCodeGenerator.TEST_CONTENT1);

        Bitmap drawingCache = mLevelUpCodeView.getDrawingCache();
        assertTestColorPixelEquals(drawingCache, MockQrCodeGenerator.TEST_CONTENT1_COLOR);
        // Now change the code.

        final String key2 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT2);
        mCache.putCode(key2, mQrCodeGenerator.mTestImage2);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT2, mLoader);
            }
        });

        drawingCache = mLevelUpCodeView.getDrawingCache();
        assertTestColorPixelEquals(drawingCache, MockQrCodeGenerator.TEST_CONTENT2_COLOR);
    }

    /**
     * Tests that {@link LevelUpCodeView}'s colorizing of the QR code matches the expected color
     * pattern.
     */
    @MediumTest
    public void testShowCode_colorizing() {
        final String key1 = mLoader.getKey(MockQrCodeGenerator.TEST_CONTENT1);

        mCache.putCode(key1, mQrCodeGenerator.mTestImage1);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLevelUpCodeView.setDrawingCacheEnabled(true);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        final Bitmap drawingCache = mLevelUpCodeView.getDrawingCache();

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
                mLevelUpCodeView.setDrawingCacheEnabled(true);
                // Disable colorization
                mLevelUpCodeView.setColorize(false);
                mLevelUpCodeView.setLevelUpCode(MockQrCodeGenerator.TEST_CONTENT1, mLoader);
            }
        });

        final Bitmap drawingCache = mLevelUpCodeView.getDrawingCache();

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
     * Tests that {@link LevelUpCodeView}'s color fading is producing an animation.
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

        final Animation animation = mLevelUpCodeView.getAnimation();
        assertNotNull(animation);
        assertEquals(LevelUpCodeView.FadeColorsAnimation.class, animation.getClass());
        // It's delayed.
        assertFalse(animation.hasStarted());
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

        final Animation animation = mLevelUpCodeView.getAnimation();
        assertNull(animation);
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

        final Animation animation = mLevelUpCodeView.getAnimation();
        assertNull(animation);
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
     * Asserts that the given pixel in the provided bitmap is a given color, given the coordinates
     * of the pre-scaled-up pixels.
     *
     * @param bitmap the full-size bitmap.
     * @param x pre-scaled x.
     * @param y pre-scaled y.
     * @param miniSize size of the pre-scaled bitmap.
     * @param expectedColor the expected color.
     */
    private void assertScaledPixelIsColor(final Bitmap bitmap, final int x, final int y,
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
    private void assertTestColorPixelEquals(final Bitmap bitmap, final int expectedColor) {
        assertScaledPixelIsColor(bitmap, MockQrCodeGenerator.TEST_COLOR_PIXEL,
                MockQrCodeGenerator.TEST_COLOR_PIXEL, MockQrCodeGenerator.TEST_IMAGE_SIZE,
                expectedColor);
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
