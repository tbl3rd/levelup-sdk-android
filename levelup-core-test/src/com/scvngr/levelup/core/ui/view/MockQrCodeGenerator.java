package com.scvngr.levelup.core.ui.view;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.SlowOperation;

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

    public static final String TEST_CONTENT1 = "foo"; //$NON-NLS-1$
    public static final String TEST_CONTENT2 = "bar"; //$NON-NLS-1$
    public static final String TEST_CONTENT3 = "baz"; //$NON-NLS-1$

    public static final int TEST_CONTENT1_COLOR = Color.RED;
    public static final int TEST_CONTENT2_COLOR = Color.GREEN;
    public static final int TEST_CONTENT3_COLOR = Color.BLUE;

    public final LevelUpQrCodeImage mTestImage1;
    public final LevelUpQrCodeImage mTestImage2;
    public final LevelUpQrCodeImage mTestImage3;

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
    public boolean isBitmapForCode(final LevelUpQrCodeImage image, final String code) {
        boolean isEqual = false;

        if (null != image && null != image.getBitmap()) {
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

    private LevelUpQrCodeImage generateTestImage(final int color) {
        final Bitmap bitmap = Bitmap.createBitmap(TEST_IMAGE_SIZE, TEST_IMAGE_SIZE, Bitmap.Config.RGB_565);

        bitmap.setPixel(TEST_COLOR_PIXEL, TEST_COLOR_PIXEL, color);
        // These are the target markers.
        bitmap.setPixel(TARGET_RIGHT, TARGET_TOP, Color.BLACK);
        bitmap.setPixel(TARGET_RIGHT, TARGET_BOTTOM, Color.BLACK);
        bitmap.setPixel(TARGET_LEFT, TARGET_BOTTOM, Color.BLACK);

        return new LevelUpQrCodeImage(Bitmap.createBitmap(bitmap), 1, 2);
    }

    /**
     * The mock version of this returns pre-rendered immutable bitmaps. These can be checked against
     * expected results using
     * {@link #isBitmapForCode(com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage, String)}
     * .
     *
     * @see com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator#generateLevelUpQrCode(java.lang.String)
     */
    @Override
    @Nullable
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
            result = null;
        }

        return result;
    }
}
