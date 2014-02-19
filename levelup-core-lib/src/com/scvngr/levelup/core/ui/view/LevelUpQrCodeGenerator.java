/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.graphics.Bitmap;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.SlowOperation;

/**
 * <p>
 * Generate a LevelUp QR code. This code is rotated 180° from most standard QR codes so that the
 * targets match the positioning of the squares in the LevelUp logo:
 * </p>
 *
 * <pre>
 *    ■
 *  ■ ■
 * </pre>
 */
public interface LevelUpQrCodeGenerator {

    /**
     * <p>
     * Generate a LevelUp QR code. This code should be as small as possible and rotated 180° from
     * the standard QR code position, such that the targets are in the bottom right corner.
     * </p>
     * <p>
     * Generally, this should not be called from the UI thread as processing can take some time.
     * </p>
     *
     * @param qrCodeData the complete contents to be encoded in the QR code.
     * @return a {@link LevelUpQrCodeImage} of the code.
     */
    @NonNull
    @SlowOperation
    public LevelUpQrCodeImage generateLevelUpQrCode(@NonNull String qrCodeData);

    /**
     * <p>
     * A bitmap of a QR code with information about the QR code targets. The code is rotated 180°
     * from the standard QR Code position.
     * </p>
     * <p>
     * The targets are the three squares-with-a-square-in-them in the corners of the QR code.
     * </p>
     */
    @Immutable
    public static final class LevelUpQrCodeImage {

        /**
         * The image itself.
         */
        @NonNull
        private final Bitmap mBitmap;

        /**
         * The whitespace around the data area of the bitmap.
         */
        private final int mCodeMargin;

        /**
         * The full height of the bitmap.
         */
        private final int mHeight;

        /**
         * Location of the target area: {@code [left, top, right, bottom]}.
         */
        @NonNull
        private final int[] mTargetBottomLeft;

        /**
         * Location of the target area: {@code [left, top, right, bottom]}.
         */
        @NonNull
        private final int[] mTargetBottomRight;

        /**
         * The width/height, in pixels, of a target in the bitmap.
         */
        private final int mTargetSize;

        /**
         * Location of the target area: {@code [left, top, right, bottom]}.
         */
        @NonNull
        private final int[] mTargetTopRight;

        /**
         * The full width of the bitmap.
         */
        private final int mWidth;

        /**
         * Create a new {@link LevelUpQrCodeImage}. Width and height are extracted from the bitmap.
         *
         * @param bitmap an immutable bitmap representing the given QR code. This should be rotated
         *        180° from the standard orientation.
         * @param targetSize the width of the target marker, in pixels. This must be the same for
         *        all target markers.
         * @param codeMargin the width between the edge of the bitmap and the first black pixel.
         *        This must be the same on all sides.
         */
        public LevelUpQrCodeImage(@NonNull final Bitmap bitmap, final int targetSize,
                final int codeMargin) {
            mBitmap = bitmap;
            mWidth = mBitmap.getWidth();
            mHeight = mBitmap.getHeight();
            mTargetSize = targetSize;
            mCodeMargin = codeMargin;

            mTargetBottomLeft =
                    new int[] { mCodeMargin, mHeight - mCodeMargin - mTargetSize,
                            mCodeMargin + mTargetSize, mHeight - mCodeMargin };

            mTargetBottomRight =
                    new int[] { mWidth - mCodeMargin - mTargetSize,
                            mHeight - mCodeMargin - mTargetSize, mWidth - mCodeMargin,
                            mHeight - mCodeMargin };

            mTargetTopRight =
                    new int[] { mWidth - mCodeMargin - mTargetSize, mCodeMargin,
                            mWidth - mCodeMargin, mCodeMargin + mTargetSize };
        }

        /**
         * @return the code image's bitmap.
         */
        @NonNull
        public Bitmap getBitmap() {
            return mBitmap;
        }

        /**
         * @return the width of the code's margin, in pixels.
         */
        public int getCodeMargin() {
            return mCodeMargin;
        }

        /**
         * Gets the position of the bottom left target marker within the bitmap.
         *
         * @return {@code [left, top, right, bottom]}, in pixels. Note: as this is a primitive, it
         *         cannot be made immutable and should therefore not be modified.
         */
        @NonNull
        public int[] getTargetBottomLeft() {
            return mTargetBottomLeft;
        }

        /**
         * Gets the position of the bottom right target marker within the bitmap.
         *
         * @return {@code [left, top, right, bottom]}, in pixels. Note: as this is a primitive, it
         *         cannot be made immutable and should therefore not be modified.
         */
        @NonNull
        public int[] getTargetBottomRight() {
            return mTargetBottomRight;
        }

        /**
         * @return the width of the target marker, in pixels.
         */
        public int getTargetSize() {
            return mTargetSize;
        }

        /**
         * Gets the position of the top right target marker within the bitmap.
         *
         * @return {@code [left, top, right, bottom]}, in pixels. Note: as this is a primitive, it
         *         cannot be made immutable and should therefore not be modified.
         */
        @NonNull
        public int[] getTargetTopRight() {
            return mTargetTopRight;
        }
    }
}
