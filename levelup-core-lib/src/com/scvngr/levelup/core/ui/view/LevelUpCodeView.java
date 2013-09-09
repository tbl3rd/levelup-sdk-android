package com.scvngr.levelup.core.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
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
 * LevelUp codes are rotated 180° from standard QR codes and colorized using the LevelUp logo
 * colors. The colors used are {@link com.scvngr.levelup.core.R.color#levelup_logo_blue},
 * {@link com.scvngr.levelup.core.R.color#levelup_logo_green}, and
 * {@link com.scvngr.levelup.core.R.color#levelup_logo_orange}. To aid scanning, the colors fade
 * from bright to dim after a short duration.
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
     * The start value of the alpha channel for the color fade animation.
     */
    private static final int ANIM_FADE_COLOR_ALPHA_START = 255;

    /**
     * The end value of the alpha channel for the color fade animation.
     */
    private static final int ANIM_FADE_COLOR_ALPHA_END = 150;

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
    private int mColorAlpha = ANIM_FADE_COLOR_ALPHA_START;

    /**
     * The currently-displayed data.
     */
    @Nullable
    private String mCurrentData;

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

        mTargetBottomLeftPaint.setColor(res.getColor(R.color.levelup_logo_green));
        mTargetBottomLeftPaint.setXfermode(xferMode);

        mTargetBottomRightPaint.setColor(res.getColor(R.color.levelup_logo_blue));
        mTargetBottomRightPaint.setXfermode(xferMode);

        mTargetTopRightPaint.setColor(res.getColor(R.color.levelup_logo_orange));
        mTargetTopRightPaint.setXfermode(xferMode);
    }

    /**
     * Animate the fading of the colors after a delay. The duration of the animation, once the delay
     * has elapsed, is {@link #ANIM_FADE_DURATION_MILLIS}.
     *
     * @see #ANIM_FADE_START_DELAY_MILLIS
     */
    public void animateFadeColorsDelayed() {
        final FadeColorsAnimation animation = new FadeColorsAnimation(ANIM_FADE_DURATION_MILLIS);
        animation.setStartOffset(ANIM_FADE_START_DELAY_MILLIS);
        animation.setFillBefore(true);
        startAnimation(animation);
    }

    /**
     * Starts the color fade animation. The duration of the animation is
     * {@link #ANIM_FADE_DURATION_MILLIS}.
     */
    public void animateFadeColorsImmediately() {
        final FadeColorsAnimation animation = new FadeColorsAnimation(ANIM_FADE_DURATION_MILLIS);
        startAnimation(animation);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final boolean handled = super.onTouchEvent(event);

        if (MotionEvent.ACTION_UP == event.getAction() && null != mCurrentCode) {
            animateFadeColorsImmediately();
        }

        return handled;
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
        if (null == mCurrentData) {
            animateFadeColorsDelayed();
        }

        if (codeData.equals(mCurrentData)) {
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
        mPendingImage = codeLoader.getLevelUpCode(codeData, mOnImageLoaded);
        mPreviousIsCodeLoading = false;

        if (!mPendingImage.isLoaded()) {
            callOnCodeLoadListener(true);

            // If the image is cached, invalidate() will be called from there.
            invalidate();
        }
    }

    /**
     * @param onCodeLoadListener the code load listener.
     */
    public void setOnCodeLoadListener(final OnCodeLoadListener onCodeLoadListener) {
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

        if (null != mCurrentCode) {
            drawQrCode(canvas, mCurrentCode);
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
     * Draws the QR code to the canvas, scaling it up to fit the measured size of the view. This
     * also draws the colored target areas per
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
        drawColoredTargetAreas(canvas, levelUpQrCodeImage);

        canvas.restore();
    }

    /**
     * A listener which will be notified when a LevelUp code is being loaded.
     */
    public interface OnCodeLoadListener {
        /**
         * This will be called when a code is loading. It is only called when a change in
         * loading/non-loading state occurs, so it will not be called for subsequent loads until
         * loading has completed.
         *
         * @param isCodeLoading {@code true} if the displayed code is currently loading.
         *        {@code false} when the code has loaded.
         */
        public void onCodeLoad(boolean isCodeLoading);
    }

    /**
     * A simple animation to fade the colors of the target markers. This works around the
     * limitations of Android's old animation framework by setting {@code mColorAlpha} and
     * {@code postInvalidate()} on the host view.
     */
    private class FadeColorsAnimation extends Animation {

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
