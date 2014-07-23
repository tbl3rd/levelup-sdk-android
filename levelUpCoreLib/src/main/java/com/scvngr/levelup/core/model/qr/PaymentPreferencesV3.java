/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.qr;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.model.tip.Tip;
import com.scvngr.levelup.core.util.LogManager;

import net.jcip.annotations.Immutable;

import java.math.BigInteger;
import java.util.Locale;

/**
 * <p>
 * This class may be used to encode and parse version 3 payment preferences. In version 3, payment
 * preferences are represented as an 8 character code which consists of four sections, each of which
 * is encoded in base 36. A typical payment preferences code:
 * </p>
 * <blockquote>{@code 030002LU}</blockquote>
 * <p>
 * Each section has a specific length prescribed to it. When necessary, leading zeros may be added
 * to a section to ensure that it conforms to this length. The following is a description of each
 * section, using {@code 030002LU} as an example:
 * </p>
 * <ul>
 * <li>[03] The version of the payment preference code. (length 2)</li>
 * <li>[000] The value of the tip, either as a percentage or US cents. Within this section, each tip
 * value type is assigned to an exclusive range of base 36 values. (length 3)</li>
 * <li>[2] The glow color index preference. (length 1)</li>
 * <li>[LU] The sentinel value which is always encoded as "LU". (length 2)</li>
 * </ul>
 * <p>
 * Percentage tip values are assigned to base 36 values ranging from 000 through 0ZZ. This range
 * can represent percentage values from 0% through 1295%.
 * </p>
 * <p>
 * US cent tip values are assigned to base 36 values ranging from 100 through ZZZ. This range
 * can represent US cent values from $0.00 through $453.59.
 * </p>
 */
@Immutable
public final class PaymentPreferencesV3 extends PaymentPreferences {

    /**
     * The payment preferences version.
     */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* package */static final int VERSION = 3;

    /**
     * The length of the payment preferences version when encoded.
     */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* package */static final int VERSION_LENGTH = 2;

    /**
     * The length of the tip when encoded.
     */
    private static final int TIP_LENGTH = 3;

    /**
     * The length of the color when encoded.
     */
    private static final int COLOR_LENGTH = 1;

    /**
     * The sentinel string.
     */
    private static final String SENTINEL = "LU";

    /**
     * The length of the sentinel when encoded.
     */
    private static final int SENTINEL_LENGTH = SENTINEL.length();

    /**
     * The length of the encoded v3 payment preferences string.
     */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* package */static final int TOTAL_ENCODED_LENGTH =
            VERSION_LENGTH + TIP_LENGTH + COLOR_LENGTH + SENTINEL_LENGTH;

    /**
     * @param paymentPreferences the preference string.
     */
    protected PaymentPreferencesV3(@NonNull final String paymentPreferences) {
        super(paymentPreferences);
    }

    @Override
    protected int getColor() {
        int color = COLOR_UNKNOWN;

        /*
         * The color is the final character before the sentinel, as expected by scanner hardware.
         * The sentinel is the last two characters of the QR code and must be "LU". As long as the
         * preferences are at least 3 characters long and end with "LU", this method will parse the
         * third character from the right as the color.
         */

        final int qrLength = mPaymentPreferences.length();
        final int sentinelStart = qrLength - SENTINEL.length();
        final int colorStart = sentinelStart - COLOR_LENGTH;

        if (qrLength >= (COLOR_LENGTH + SENTINEL_LENGTH)
                && SENTINEL.equals(mPaymentPreferences.substring(sentinelStart, qrLength))) {
            final String colorCode = mPaymentPreferences.substring(colorStart, sentinelStart);

            try {
                color = new BigInteger(colorCode, Character.MAX_RADIX).intValue();
            } catch (final NumberFormatException nfe) {
                LogManager.i("Could not parse the color", nfe);
            }
        }

        return color;
    }

    /**
     * {@inheritDoc} Encodes the payment preferences into a base 36 string consisting of the
     * version, tip, color, and sentinel.
     */
    @Override
    @NonNull
    String encode(final int color, final Tip<?> tip) {
        // Validate tip string size
        final String tipStr = Integer.toString(tip.getEncodedValue(), Character.MAX_RADIX);
        if (tipStr.length() > TIP_LENGTH) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "Tip can only be %d characters", TIP_LENGTH));
        }

        final StringBuilder builder = new StringBuilder(TOTAL_ENCODED_LENGTH);

        // Add the preference version (must be padded).
        builder.append(CodeVersionUtils.leftPadWithZeros(Integer.toString(VERSION,
                Character.MAX_RADIX), VERSION_LENGTH));

        // Append the tip value (must be padded).
        builder.append(CodeVersionUtils.leftPadWithZeros(tipStr, TIP_LENGTH));

        // Append the color.
        final String colorStr = Integer.toString(color, Character.MAX_RADIX);

        if (COLOR_LENGTH == colorStr.length()) {
            builder.append(colorStr);
        } else {
            builder.append(CodeVersionUtils.leftPadWithZeros(null, COLOR_LENGTH));
        }

        // Append the sentinel (LU).
        builder.append(SENTINEL);

        return builder.toString().toUpperCase(Locale.US);
    }
}
