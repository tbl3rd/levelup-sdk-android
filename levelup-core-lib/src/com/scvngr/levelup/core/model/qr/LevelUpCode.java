package com.scvngr.levelup.core.model.qr;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.util.LogManager;

/**
 * <p>
 * Class to represent a QR code. Note: this is not a representation of the web service model.
 * </p>
 *
 * <p>
 * The primary goal of this class is to enable the parsing of colors from the QR codes scanned by
 * the merchant. This does not validate codes, it attempts to determine code version for the sole
 * purpose of parsing colors. The server will always be the final validator of codes.
 * </p>
 *
 * <p>
 * Callers should not instantiate this class, they should just call
 * {@link LevelUpCode#parseColor(Resources, String)} and it will return the color parsed or the
 * default color.
 * </p>
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public abstract class LevelUpCode {
    /**
     * The QR code data.
     */
    @NonNull
    protected final String mData;

    /**
     * @param qrData the data read from the QR code
     */
    protected LevelUpCode(@NonNull final String qrData) {
        mData = qrData;
    }

    /**
     * Parse the color parameter from this QR code. Try all supported versions of the QR code,
     * falling back to the default dock scan color if no color can be parsed.
     *
     * @param resources the resources to use to get color resources.
     * @param qrData the QR code string scanned.
     * @return the color the dock should display.
     */
    public static int parseColor(@NonNull final Resources resources, @NonNull final String qrData) {
        int dockColor = PaymentPreferences.COLOR_UNKNOWN;

        if (!TextUtils.isEmpty(qrData)) {
            final LevelUpCode code = getFullPaymentTokenVersion(qrData);

            dockColor = code.getColor(resources);
        }

        if (PaymentPreferences.COLOR_UNKNOWN == dockColor) {
            // If we couldn't find anything, then use the default.
            dockColor = resources.getColor(R.color.dock_default_scan_color);
        }

        return dockColor;
    }

    /**
     * Encodes the payment preferences to the end of the QR code string passed.
     *
     * @param data the QR code data to append the preferences to.
     * @param color the color to encode. This value is between 0 and 9, indexing into a list of
     *        preset colors.
     * @param tip the tip to encode.
     * @return the String with the payment preferences encoded into it.
     */
    @NonNull
    public static String encodeLevelUpCode(@NonNull final String data, final int color,
            final int tip) {
        String encodedQr = data;

        if (!TextUtils.isEmpty(data)) {
            final LevelUpCode code = getPaymentTokenVersion(data);

            int constrainedColor = color;
            if (color < 0) {
                constrainedColor = 0;
            }

            encodedQr = code.encodePaymentPreferences(constrainedColor, tip);
        }

        return encodedQr;
    }

    /**
     * Subclasses must implement this to encode the payment preferences into the LevelUp code.
     *
     * @param color the color to encode. This is an index between 0 and 9, inclusive.
     * @param tip the tip to encode. This is a percentage between 0 and 100, inclusive.
     * @return the full LevelUp code with the payment preferences encoded into it
     */
    @NonNull
    /* package */abstract String encodePaymentPreferences(int color, int tip);

    /**
     * Get the color from this LevelUp code.
     *
     * @param resources the resources to use to resolve the color.
     * @return the int color or {@link PaymentPreferences#COLOR_UNKNOWN} if none were found in QR
     * code.
     */
    protected abstract int getColor(@NonNull final Resources resources);

    /**
     * Detect the version of the full LevelUp code (including tip and color) passed.
     *
     * @param data the data string scanned
     * @return {@link LevelUpCode} for the version detected.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static LevelUpCode getFullPaymentTokenVersion(@NonNull final String data) {
        return new PaymentTokenV2(data);
    }

    /**
     * Detect the version of the Payment Token portion of the QR code passed.
     *
     * @param data the data string scanned
     * @return {@link LevelUpCode} for the version detected.
     */
    @NonNull
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static LevelUpCode getPaymentTokenVersion(@NonNull final String data) {
        return new PaymentTokenV2(data);
    }

    /**
     * @param position the position in the color array that was parsed from the QR code data.
     * @param resources Application resources.
     * @return The color decoded from {@code colorToDecode} or
     *         {@link PaymentPreferences#COLOR_UNKNOWN} if {@code colorToDecode} couldn't be
     *         decoded.
     */
    protected static int decodeColor(final int position, @NonNull final Resources resources) {
        int color = PaymentPreferences.COLOR_UNKNOWN;
        String name = null;
        TypedArray array = null;

        try {
            array = resources.obtainTypedArray(R.array.levelup_dock_colors);

            if (0 <= position && array.length() > position) {
                name = resources.getResourceName(array.getResourceId(position, -1));
                color = array.getColor(position, -1);
            }
        } finally {
            if (null != array) {
                array.recycle();
                array = null;
            }
        }

        LogManager.d("Scanned color position=%d %s=%x", position, name, color); //$NON-NLS-1$
        return color;
    }
}
