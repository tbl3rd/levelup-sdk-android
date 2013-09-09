package com.scvngr.levelup.core.model.qr;

import java.math.BigInteger;
import java.util.Locale;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.util.LogManager;

/**
 * <p>
 * Class to encompass V2 version of the payment preferences.
 * </p>
 *
 * <p>
 * V2 Preferences look like:
 * </p>
 *
 * <blockquote>{@code 021002LU}</blockquote>
 * <p>
 * Each section below is individually base 36 encoded and zero-padded.
 * </p>
 * <ul>
 * <li>Where the first 2 characters are the version.</li>
 * <li>The next character (position 3) is tip type. Currently only {@link #TIP_TYPE_PERCENTAGE} is
 * supported.</li>
 * <li>The 4th-5th characters are the tip value. For percentage tips, this is an integer between 0
 * and 100 (decimal).</li>
 * <li>The 6th character is the color, followed by the sentinel "LU".</li>
 * </ul>
 */
@Immutable
public final class PaymentPreferencesV2 extends PaymentPreferences {

    private static final int COLOR_INDEX_FROM_RIGHT = 3;

    /**
     * The length of v2 preferences.
     */
    public static final int PREFERENCES_LENGTH = 8;

    /**
     * The numeric code for percentage tips.
     */
    public static final int TIP_TYPE_PERCENTAGE = 0;

    private static final int PREFERENCES_VERSION = 2;
    private static final int VERSION_INDEX_BEGIN = 0;
    private static final int VERSION_INDEX_END_EXCLUSIVE = 2;
    private static final int TIP_LENGTH = 2;
    private static final int COLOR_LENGTH = 1;
    private static final String SENTINEL = "LU"; //$NON-NLS-1$

    /**
     * @param paymentPreferences the preference string.
     */
    protected PaymentPreferencesV2(@NonNull final String paymentPreferences) {
        super(paymentPreferences);
    }

    /**
     * @param prefs the string preference from the QR code.
     * @return true if these preferences are v2.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static boolean isValidPreference(@NonNull final String prefs) {
        return CodeVersionUtils.isValidCode(prefs, PREFERENCES_LENGTH, VERSION_INDEX_BEGIN,
                VERSION_INDEX_END_EXCLUSIVE, PREFERENCES_VERSION);
    }

    @Override
    protected int getColor() {
        int color = COLOR_UNKNOWN;

        /**
         * The color is now (until eternity) expected to be the final character before the sentinel.
         * The sentinel is the last two characters of the QR code and they must be "LU". As long as
         * the preferences are at least 3 characters long and end with "LU", we will parse the third
         * character from the right as the color.
         */
        final int qrLength = mPaymentPreferences.length();
        if (qrLength >= COLOR_INDEX_FROM_RIGHT
                && SENTINEL.equals(mPaymentPreferences.substring(qrLength - 2, qrLength))) {
            final String colorCode = mPaymentPreferences.substring(qrLength - 3, qrLength - 2);
            try {
                color = new BigInteger(colorCode, Character.MAX_RADIX).intValue();
            } catch (final NumberFormatException nfe) {
                LogManager.i("Could not parse the color", nfe); //$NON-NLS-1$
            }
        }

        return color;
    }

    /**
     * {@inheritDoc} Encodes the payment preferences as follows (all are individually base 36
     * encoded) 2 characters for version 3 characters for tip (1 character for type, 2 for value) 1
     * character for color.
     */
    @Override
    @NonNull
    String encode(final int color, final int tip) {
        // Validate tip string size
        final String tipStr = Integer.toString(tip, Character.MAX_RADIX);
        if (tipStr.length() > TIP_LENGTH) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "Tip can only be %d characters", TIP_LENGTH)); //$NON-NLS-1$
        }

        final StringBuilder builder = new StringBuilder(PREFERENCES_LENGTH);

        // Add the preference version (must be padded to 2 characters).
        builder.append(CodeVersionUtils.leftPadWithZeros(
                Integer.toString(PREFERENCES_VERSION, Character.MAX_RADIX), 2));

        /*
         * Append the tip type. Currently we don't support anything but percentage.
         */
        builder.append(TIP_TYPE_PERCENTAGE);

        // Append the tip value: 2 characters.
        builder.append(CodeVersionUtils.leftPadWithZeros(tipStr, 2));

        String colorStr = Integer.toString(color, Character.MAX_RADIX);
        if (colorStr.length() > COLOR_LENGTH) {
            colorStr = "0"; //$NON-NLS-1$
        }

        // Append the color: one character.
        builder.append(colorStr);

        // Append the sentinel (LU).
        builder.append(SENTINEL);

        return builder.toString().toUpperCase(Locale.US);
    }
}
