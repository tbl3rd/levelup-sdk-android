package com.scvngr.levelup.core.model.qr;

import android.content.res.Resources;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.NonNull;

/**
 * <p>Class to encompass the logic to parse v2 Payment tokens.</p>
 *
 * <p>Looks like (base 36 encoded):</p>
 *
 * <blockquote>{@code LU02012346712345678901234567010012LU}</blockquote>
 */
@Immutable
public final class PaymentTokenV2 extends LevelUpCode {

    private static final int CODE_LENGTH = 32;
    /* package */static final int CODE_VERSION = 2;
    /* package */static final int VERSION_INDEX_BEGIN = 0;
    /* package */static final int VERSION_INDEX_END_EXCLUSIVE = 2;
    /* package */static final int TOKEN_LENGTH = 24;

    /**
     * Constructor.
     *
     * @param qrData the QR code data scanned.
     */
    protected PaymentTokenV2(@NonNull final String qrData) {
        super(qrData);
    }

    /**
     * Returns whether or not the QR code is a v1 code.
     *
     * @param qrData the data scanned from the QR code.
     * @return true if it is a v1 code, false otherwise.
     */
    public static boolean isValidCode(@NonNull final String qrData) {
        final int idx = qrData.indexOf('-');
        boolean valid = false;

        if (idx == -1) {
            valid =
                    CodeVersionUtils.isValidCode(qrData, CODE_LENGTH, VERSION_INDEX_BEGIN,
                            VERSION_INDEX_END_EXCLUSIVE, CODE_VERSION);
        }

        return valid;
    }

    /**
     * Parses a V1 version of the code to try to get the user's selected color. IE:
     *
     * @param resources the resources to use to get color resources with
     * @return a valid color value if successfully parsed or
     * {@link PaymentPreferences#COLOR_UNKNOWN} if not.
     */
    @Override
    protected int getColor(@NonNull final Resources resources) {
        int parsedColor = PaymentPreferences.COLOR_UNKNOWN;

        final int colorPreference = PaymentPreferences.getColorPreference(mData);

        if (PaymentPreferences.COLOR_UNKNOWN != colorPreference) {
            parsedColor = decodeColor(colorPreference, resources);
        }

        return parsedColor;
    }

    /**
     * Checks for validity of the token in v1 format (must have a v1 marker at the beginning and be
     * 26 characters long).
     *
     * @param qrData data to check.
     * @return true if it's valid in v0; false otherwise.
     */
    public static boolean isValidToken(final String qrData) {
        return CodeVersionUtils.isValidCode(qrData, PaymentTokenV2.TOKEN_LENGTH,
                PaymentTokenV2.VERSION_INDEX_BEGIN, PaymentTokenV2.VERSION_INDEX_END_EXCLUSIVE,
                PaymentTokenV2.CODE_VERSION);
    }

    @Override
    @NonNull
    String encodePaymentPreferences(final int color, final int tip) {
        final StringBuilder builder =
                new StringBuilder(mData.length() + PaymentPreferencesV2.PREFERENCES_LENGTH);
        builder.append(mData);
        builder.append(new PaymentPreferencesV2(null).encode(color, tip));
        return builder.toString();
    }
}
