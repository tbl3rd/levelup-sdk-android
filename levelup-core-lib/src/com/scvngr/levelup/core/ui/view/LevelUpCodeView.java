/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Looper;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;
import com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * <p>
 * A view that displays a LevelUp QR code. LevelUp codes are loaded asynchronously using a
 * {@link LevelUpCodeLoader}.
 * </p>
 * <p>
 * To use, simply call {@link #setLevelUpCode(String, LevelUpCodeLoader)}. Code load status can be
 * monitored by registering an {@link OnCodeLoadListener} using
 * {@link #setOnCodeLoadListener(OnCodeLoadListener)} before calling
 * {@link #setLevelUpCode(String, LevelUpCodeLoader)}.
 * </p>
 * <p>
 * LevelUp codes are rotated 180° from standard QR codes and are optionally colorized using the
 * LevelUp logo colors. The colors used are
 * {@link com.scvngr.levelup.core.R.color#levelup_logo_blue},
 * {@link com.scvngr.levelup.core.R.color#levelup_logo_green}, and
 * {@link com.scvngr.levelup.core.R.color#levelup_logo_orange}. To aid scanning, the colors can be
 * set to automatically fade from bright to dim after a short duration.
 * </p>
 * <p>
 * Colorizing can be disabled by setting the XML property
 * {@link com.scvngr.levelup.core.R.attr#colorize} to false. The automatic fading of the colors can
 * be disabled by setting {@link com.scvngr.levelup.core.R.attr#fade_colors}. The colors can still
 * be manually faded by calling {@link #animateFadeColorsDelayed()} or
 * {@link #animateFadeColorsImmediately()}.
 * </p>
 */
public final class LevelUpCodeView extends View {
    /**
     * The duration of the color fade animation, in milliseconds.
     */
    public static final int ANIM_FADE_DURATION_MILLIS = 5000;

    /**
     * The delay before starting the color fade animation, as used by
     * {@link #animateFadeColorsDelayed()}.
     */
    public static final int ANIM_FADE_START_DELAY_MILLIS = 1500;

    /**
     * The end value of the alpha channel for the color fade animation.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final int ANIM_FADE_COLOR_ALPHA_END = 150;

    /**
     * The start value of the alpha channel for the color fade animation.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final int ANIM_FADE_COLOR_ALPHA_START = 255;

    /**
     * The default value for {@link #mIsColorizeSet}.
     */
    private static final boolean COLORIZE_DEFAULT = true;

    /**
     * The default of whether or not the colors should fade.
     */
    private static final boolean FADE_COLORS_DEFAULT = true;

    /**
     * The currently-displayed QR code.
     */
    @Nullable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */LevelUpQrCodeImage mCurrentCode;

    /**
     * Scaling matrix used for biggering the code to fill the view.
     */
    private final Matrix mCodeScalingMatrix = new Matrix();

    /**
     * The current alpha value of the colored target areas.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */int mColorAlpha = ANIM_FADE_COLOR_ALPHA_START;

    /**
     * The currently-displayed data.
     */
    @Nullable
    private String mCurrentData;

    private GestureDetectorCompat mGestureDetector;

    /**
     * A little easter-egg that lets you restart the fade animation by double-tapping on the code.
     * Only enabled when the code is colorized and automatic fading is turned on. This shouldn't
     * interfere with touch events, as it doesn't claim to handle any events.
     */
    private final GestureDetector.SimpleOnGestureListener mGestureDetectorCallbacks =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTap(final MotionEvent e) {
                    animateFadeColorsImmediately();
                    return false;
                }
            };

    /**
     * If true, the QR code will be colorized.
     */
    private boolean mIsColorizeSet = COLORIZE_DEFAULT;

    /**
     * Whether or not to fade the colors.
     */
    private boolean mIsFadeColorsSet = FADE_COLORS_DEFAULT;

    /**
     * The listener that this view will call when a code is loaded. See
     * {@link #callOnCodeLoadListener(boolean)}.
     */
    @Nullable
    private OnCodeLoadListener mOnCodeLoadListener;

    /**
     * Listener which updates the QR code image once it's loaded.
     */
    private final OnImageLoaded<LevelUpQrCodeImage> mOnImageLoaded =
            new OnImageLoaded<LevelUpQrCodeGenerator.LevelUpQrCodeImage>() {

                @Override
                public void onImageLoaded(@NonNull final String loadKey,
                        @NonNull final LevelUpQrCodeImage image) {
                    mCurrentCode = image;
                    callOnCodeLoadListener(false);
                    invalidate();
                }
            };

    /**
     * The QR code image, possibly not loaded yet.
     */
    @Nullable
    private PendingImage<LevelUpQrCodeImage> mPendingImage;

    /**
     * The previous value for isCodeLoading, used to keep track of changes for
     * {@link #callOnCodeLoadListener(boolean)}.
     */
    private boolean mPreviousIsCodeLoading = false;

    /**
     * The paint for drawing the QR code bitmap. It's important that it's initialized with 0,
     * otherwise it becomes blurry.
     */
    private final Paint mQrCodePaint = new Paint(0);

    /**
     * Bottom left colored target paint.
     */
    private final Paint mTargetBottomLeftPaint = new Paint();

    /**
     * Bottom right colored target paint.
     */
    private final Paint mTargetBottomRightPaint = new Paint();

    /**
     * Top right colored target paint.
     */
    private final Paint mTargetTopRightPaint = new Paint();

    /**
     * @param context activity context.
     */
    public LevelUpCodeView(@NonNull final Context context) {
        super(context);
        init(context);
    }

    /**
     * @param context activity context.
     * @param attrs attributes.
     */
    public LevelUpCodeView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context);
        loadAttributes(context, attrs, R.attr.levelup_code_view_style);
    }

    /**
     * @param context activity context.
     * @param attrs attributes.
     * @param defStyle default style.
     */
    public LevelUpCodeView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        loadAttributes(context, attrs, defStyle);
    }

    /**
     * Animate the fading of the colors after a delay. The delay is
     * {@link #ANIM_FADE_START_DELAY_MILLIS}.
     *
     * @see #animateFadeColorsImmediately()
     */
    public void animateFadeColorsDelayed() {
        if (mIsColorizeSet) {
            final FadeColorsAnimation animation =
                    new FadeColorsAnimation(ANIM_FADE_DURATION_MILLIS);
            animation.setStartOffset(ANIM_FADE_START_DELAY_MILLIS);
            animation.setFillBefore(true);
            startAnimation(animation);
        }
    }

    /**
     * Starts the color fade animation. The duration of the animation is
     * {@link #ANIM_FADE_DURATION_MILLIS}.
     */
    public void animateFadeColorsImmediately() {
        if (mIsColorizeSet) {
            final FadeColorsAnimation animation =
                    new FadeColorsAnimation(ANIM_FADE_DURATION_MILLIS);
            startAnimation(animation);
        }
    }

    /**
     * @return if the LevelUp code has colorized target markers.
     */
    public boolean isColorizeSet() {
        return mIsColorizeSet;
    }

    /**
     * @return true if the color fading flag is set.
     */
    public boolean isFadeColorsSet() {
        return mIsFadeColorsSet;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final boolean handled = super.onTouchEvent(event);

        if (mIsColorizeSet && mIsFadeColorsSet) {
            mGestureDetector.onTouchEvent(event);
        }

        return handled;
    }

    /**
     * <p>
     * Enables/disables the colorization of the LevelUp code.
     * </p>
     * <p>
     * This must be called from the UI thread.
     * </p>
     *
     * @param isColorized if true, the LevelUp code's target markers will be colorized.
     */
    public void setColorize(final boolean isColorized) {
        if (mIsColorizeSet != isColorized) {
            mIsColorizeSet = isColorized;
            invalidate();
        }
    }

    /**
     * @param fadeColors if true, the colors will automatically fade after a period of time the
     *        first time the code is set with {@link #setLevelUpCode(String, LevelUpCodeLoader)}.
     */
    public void setFadeColors(final boolean fadeColors) {
        mIsFadeColorsSet = fadeColors;
    }

    /**
     * <p>
     * Sets the code that will be displayed.
     * </p>
     * <p>
     * This must be called on the UI thread.
     * </p>
     *
     * @param codeData the raw content to be displayed in the QR code.
     * @param codeLoader the loader by which to load the QR code.
     */
    public void setLevelUpCode(@NonNull final String codeData,
            @NonNull final LevelUpCodeLoader codeLoader) {

        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new AssertionError("Must be called from the main thread."); //$NON-NLS-1$
        }

        if (null == mCurrentData && mIsFadeColorsSet) {
            animateFadeColorsDelayed();
        }

        if (codeData.equals(mCurrentData)) {
            final OnCodeLoadListener codeLoadListener = mOnCodeLoadListener;

            if (null != codeLoadListener) {
                if (null != mPendingImage && mPendingImage.isLoaded()) {
                    codeLoadListener.onCodeLoad(false);
                }
            }
            return;
        }

        if (mPendingImage != null) {
            mPendingImage.cancelLoad();
        }

        mCurrentData = codeData;

        /*
         * The current code needs to be cleared so that it isn't displayed while the new code is
         * loading.
         */
        mCurrentCode = null;

        // Fake the loading flag so that cached results get a single isLoading(false) call.
        mPreviousIsCodeLoading = true;
        final PendingImage<LevelUpQrCodeImage> pendingImage =
                codeLoader.getLevelUpCode(codeData, mOnImageLoaded);
        mPendingImage = pendingImage;
        mPreviousIsCodeLoading = false;

        if (!pendingImage.isLoaded()) {
            callOnCodeLoadListener(true);

            // If the image is cached, invalidate() will be called from there.
            invalidate();
        }
    }

    /**
     * @param onCodeLoadListener the code load listener.
     */
    public void setOnCodeLoadListener(@Nullable final OnCodeLoadListener onCodeLoadListener) {
        this.mOnCodeLoadListener = onCodeLoadListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // Cancel and clear any pending images or loaders.
        if (null != mPendingImage) {
            mPendingImage.cancelLoad();
            mPendingImage = null;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final LevelUpQrCodeImage currentCode = mCurrentCode;

        if (null != currentCode) {
            drawQrCode(NullUtils.nonNullContract(canvas), currentCode);
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // Sizes both width and height to the smallest dimension in order to create a square view.
        width = Math.min(height, width);
        height = Math.min(height, width);

        // Enforces the minimum-suggested sizes.
        final int minSize = Math.max(getSuggestedMinimumHeight(), getSuggestedMinimumWidth());
        width = Math.max(minSize, width);
        height = Math.max(minSize, height);

        setMeasuredDimension(width, height);
    }

    /**
     * Calls the registered {@link OnCodeLoadListener}, if there is one. Subsequent calls with the
     * same {@code isCodeLoading} value will not deliver updates. This only delivers changes in the
     * supplied parameter to the listener.
     *
     * @param isCodeLoading true if the code is being loaded; false if the load has completed.
     */
    private void callOnCodeLoadListener(final boolean isCodeLoading) {
        if (null != mOnCodeLoadListener && (mPreviousIsCodeLoading != isCodeLoading)) {
            mOnCodeLoadListener.onCodeLoad(isCodeLoading);
            mPreviousIsCodeLoading = isCodeLoading;
        }
    }

    /**
     * Draw the colored target areas to the canvas using sizing information provided by
     * {@code codeBitmapWithTarget}.
     *
     * @param canvas the canvas to draw on.
     * @param codeBitmapWithTarget the code to draw.
     */
    private void drawColoredTargetAreas(@NonNull final Canvas canvas,
            @NonNull final LevelUpQrCodeImage codeBitmapWithTarget) {

        int[] target;
        target = codeBitmapWithTarget.getTargetTopRight();
        mTargetTopRightPaint.setAlpha(mColorAlpha);
        canvas.drawRect(target[0], target[1], target[2], target[3], mTargetTopRightPaint);

        target = codeBitmapWithTarget.getTargetBottomRight();
        mTargetBottomRightPaint.setAlpha(mColorAlpha);
        canvas.drawRect(target[0], target[1], target[2], target[3], mTargetBottomRightPaint);

        target = codeBitmapWithTarget.getTargetBottomLeft();
        mTargetBottomLeftPaint.setAlpha(mColorAlpha);
        canvas.drawRect(target[0], target[1], target[2], target[3], mTargetBottomLeftPaint);
    }

    /**
     * Draws the QR code to the canvas, scaling it up to fit the measured size of the view. If
     * enabled, this also draws the colored target areas per
     * {@link #drawColoredTargetAreas(Canvas, LevelUpQrCodeImage)}.
     *
     * @param canvas the drawing canvas.
     * @param levelUpQrCodeImage the image of the QR code with target marker information.
     */
    private void drawQrCode(@NonNull final Canvas canvas,
            @NonNull final LevelUpQrCodeImage levelUpQrCodeImage) {
        final Bitmap codeBitmap = levelUpQrCodeImage.getBitmap();
        /*
         * The code is cached in the smallest size and must be scaled before being displayed. It is
         * necessary to draw it directly onto a canvas and scale it in the same operation for
         * efficiency (so we do not have any perceivable lag when switching tip values).
         */
        mCodeScalingMatrix.setScale((float) getMeasuredWidth() / codeBitmap.getWidth(),
                (float) getMeasuredHeight() / codeBitmap.getHeight());

        // Save the canvas without the scaling matrix.
        canvas.save();

        canvas.concat(mCodeScalingMatrix);
        canvas.drawBitmap(codeBitmap, 0, 0, mQrCodePaint);

        if (mIsColorizeSet) {
            drawColoredTargetAreas(canvas, levelUpQrCodeImage);
        }

        canvas.restore();
    }

    /**
     * Initialize the view.
     *
     * @param context view context.
     */
    private void init(@NonNull final Context context) {
        setWillNotDraw(false);
        setClickable(true);

        final Resources res = context.getResources();
        final Xfermode xferMode = new PorterDuffXfermode(Mode.SCREEN);

        if (!isInEditMode()) {
            mTargetBottomLeftPaint.setColor(res.getColor(R.color.levelup_logo_green));
            mTargetBottomRightPaint.setColor(res.getColor(R.color.levelup_logo_blue));
            mTargetTopRightPaint.setColor(res.getColor(R.color.levelup_logo_orange));
        } else {
            mTargetBottomLeftPaint.setColor(Color.GREEN);
            mTargetBottomRightPaint.setColor(Color.BLUE);
            mTargetTopRightPaint.setColor(Color.YELLOW);
        }

        mTargetBottomLeftPaint.setXfermode(xferMode);
        mTargetBottomRightPaint.setXfermode(xferMode);
        mTargetTopRightPaint.setXfermode(xferMode);

        mGestureDetector = new GestureDetectorCompat(context, mGestureDetectorCallbacks);
    }

    /**
     * Loads the XML attributes, passed in from the constructor.
     *
     * @param context view context.
     * @param attrs optional attribute set.
     * @param defaultStyle optional default style.
     * @see com.scvngr.levelup.core.R.styleable#LevelUpCodeView
     */
    private void loadAttributes(@NonNull final Context context, @Nullable final AttributeSet attrs,
            final int defaultStyle) {

        final TypedArray attributes =
                context.obtainStyledAttributes(attrs, R.styleable.LevelUpCodeView, defaultStyle, 0);
        try {
            mIsColorizeSet =
                    attributes.getBoolean(R.styleable.LevelUpCodeView_colorize, COLORIZE_DEFAULT);
            mIsFadeColorsSet =
                    attributes.getBoolean(R.styleable.LevelUpCodeView_fade_colors,
                            FADE_COLORS_DEFAULT);

        } finally {
            attributes.recycle();
        }
    }

    /**
     * A listener which will be notified when a LevelUp code is being loaded.
     */
    public interface OnCodeLoadListener {
        /**
         * This will be called when a code begins or ends loading. It is only called when a change
         * in loading/non-loading state occurs. It will not be called more than once per load
         * operation except when an identical code is given to {@link #setLevelUpCode}. In that
         * case, this callback will be called again if that code has already completed loading.
         *
         * @param isCodeLoading {@code true} if the displayed code is currently loading.
         *        {@code false} when the code has loaded.
         */
        public void onCodeLoad(final boolean isCodeLoading);
    }

    /**
     * A simple animation to fade the colors of the target markers. This works around the
     * limitations of Android's old animation framework by setting {@code mColorAlpha} and
     * {@code postInvalidate()} on the host view.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */class FadeColorsAnimation extends Animation {

        /**
         * @param durationMillis the duration of the animation, in milliseconds.
         */
        public FadeColorsAnimation(final int durationMillis) {
            setInterpolator(new DecelerateInterpolator());
            setDuration(durationMillis);
        }

        @Override
        public boolean willChangeBounds() {
            return false;
        }

        @Override
        public boolean willChangeTransformationMatrix() {
            return false;
        }

        @Override
        protected void applyTransformation(final float interpolatedTime, final Transformation t) {
            /*
             * This is not actually how applyTransformation is intended to be used, but the
             * Transformation object is too limited, even for this relatively simple use case.
             */
            mColorAlpha =
                    (int) ((ANIM_FADE_COLOR_ALPHA_END - ANIM_FADE_COLOR_ALPHA_START)
                            * interpolatedTime + ANIM_FADE_COLOR_ALPHA_START);
            postInvalidate();
        }
    }
}
