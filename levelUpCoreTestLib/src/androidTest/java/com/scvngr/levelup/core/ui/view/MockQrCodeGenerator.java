/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.SlowOperation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Mock implementation of {@link com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator} for test purposes.
 */
public final class MockQrCodeGenerator implements LevelUpQrCodeGenerator {
    /**
     * The x & y coordinate of the test color pixel.
     */
    public static final int TEST_COLOR_PIXEL = 3;

    public static final int TEST_IMAGE_SIZE = 7;

    public static final int TARGET_TOP = 2;
    public static final int TARGET_BOTTOM = 4;
    public static final int TARGET_LEFT = 2;
    public static final int TARGET_RIGHT = 4;

    public static final String TEST_CONTENT1 = "foo";
    public static final String TEST_CONTENT2 = "bar";
    public static final String TEST_CONTENT3 = "baz";

    public static final int TEST_CONTENT1_COLOR = Color.RED;
    public static final int TEST_CONTENT2_COLOR = Color.GREEN;
    public static final int TEST_CONTENT3_COLOR = Color.BLUE;

    public final LevelUpQrCodeImage mTestImage1;
    public final LevelUpQrCodeImage mTestImage2;
    public final LevelUpQrCodeImage mTestImage3;

    public CountDownLatch mGenerateDelayLatch;

    private static final int GENERATE_LATCH_WAIT_SECONDS = 4;

    public MockQrCodeGenerator() {
        mTestImage1 = generateTestImage(TEST_CONTENT1_COLOR);
        mTestImage2 = generateTestImage(TEST_CONTENT2_COLOR);
        mTestImage3 = generateTestImage(TEST_CONTENT3_COLOR);
    }

    /**
     * @param image the image to check.
     * @param code the code to check.
     * @return true if the given bitmap is the one representing the given code.
     */
    public static boolean isBitmapForCode(@Nullable final LevelUpQrCodeImage image,
            @Nullable final String code) {
        boolean isEqual = false;

        if (null != image) {
            final int color = image.getBitmap().getPixel(TEST_COLOR_PIXEL, TEST_COLOR_PIXEL);

            switch (color) {
                case TEST_CONTENT1_COLOR:
                    isEqual = TEST_CONTENT1.equals(code);
                    break;
                case TEST_CONTENT2_COLOR:
                    isEqual = TEST_CONTENT2.equals(code);
                    break;
                case TEST_CONTENT3_COLOR:
                    isEqual = TEST_CONTENT3.equals(code);
                    break;
                default:
                    isEqual = false;
            }
        }

        return isEqual;
    }

    /**
     * Generate a {@link com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage}
     * for the specified color.
     *
     * @param color the color to generate the image from
     * @return the {@link com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage}
     *         .
     */
    @NonNull
    private LevelUpQrCodeImage generateTestImage(final int color) {
        final Bitmap bitmap =
                Bitmap.createBitmap(TEST_IMAGE_SIZE, TEST_IMAGE_SIZE, Bitmap.Config.RGB_565);

        bitmap.setDensity(Bitmap.DENSITY_NONE);

        bitmap.setPixel(TEST_COLOR_PIXEL, TEST_COLOR_PIXEL, color);
        // These are the target markers.
        bitmap.setPixel(TARGET_RIGHT, TARGET_TOP, Color.BLACK);
        bitmap.setPixel(TARGET_RIGHT, TARGET_BOTTOM, Color.BLACK);
        bitmap.setPixel(TARGET_LEFT, TARGET_BOTTOM, Color.BLACK);

        final Bitmap immutableBitmap = Bitmap.createBitmap(bitmap);

        if (bitmap != immutableBitmap) {
            bitmap.recycle();
        }

        return new LevelUpQrCodeImage(immutableBitmap, 1, 2);
    }

    /**
     * The mock version of this returns pre-rendered immutable bitmaps. These can be checked against
     * expected results using {@link #isBitmapForCode}.
     *
     * @param qrCodeData the QR code data string
     * @return the test
     *         {@link com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage}.
     * @see com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator#generateLevelUpQrCode
     */
    @Override
    @NonNull
    @SlowOperation
    public LevelUpQrCodeImage generateLevelUpQrCode(@NonNull final String qrCodeData) {
        LevelUpQrCodeImage result;

        if (TEST_CONTENT1.equals(qrCodeData)) {
            result = mTestImage1;
        } else if (TEST_CONTENT2.equals(qrCodeData)) {
            result = mTestImage2;
        } else if (TEST_CONTENT3.equals(qrCodeData)) {
            result = mTestImage3;
        } else {
            // This is an error condition that will probably never occur in real life.
            throw new AssertionError();
        }

        if (null != mGenerateDelayLatch) {
            try {
                mGenerateDelayLatch.await();
                if (!mGenerateDelayLatch.await(GENERATE_LATCH_WAIT_SECONDS, TimeUnit.SECONDS)) {
                    AndroidTestCase.fail("latch timeout exceeded");
                }
            } catch (final InterruptedException e) {
                AndroidTestCase.fail("latch was interrupted");
            }

            // As the latch has expired, remove it.
            mGenerateDelayLatch = null;
        }

        return result;
    }

    /**
     * Adds a {@link java.util.concurrent.CountDownLatch} starting at 1 to the {@link #generateLevelUpQrCode}
     * method to allow testing classes to forcibly delay the background task. Make sure to call this
     * method before calling {@link #generateLevelUpQrCode} or anything that may call it.
     *
     * @return the latch
     */
    public CountDownLatch addCountdownLatch() {
        mGenerateDelayLatch = new CountDownLatch(1);

        return mGenerateDelayLatch;
    }
}
