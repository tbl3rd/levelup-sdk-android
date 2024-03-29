/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.model.qr;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.model.tip.Tip;

import net.jcip.annotations.Immutable;

/**
 * Class to encompass the PaymentPreferences at the end of QR codes.
 */
@Immutable
@LevelUpApi(contract = LevelUpApi.Contract.INTERNAL)
public abstract class PaymentPreferences {
    /**
     * Return code for when a color cannot be parsed or is otherwise missing.
     */
    public static final int COLOR_UNKNOWN = -1;

    /**
     * The payment preferences string passed in from the constructor.
     */
    @NonNull
    protected final String mPaymentPreferences;

    /**
     * @param paymentPreferences the paymentPreferences string from the QR code.
     */
    protected PaymentPreferences(@NonNull final String paymentPreferences) {
        mPaymentPreferences = paymentPreferences;
    }

    /**
     * Subclasses must implement this to return the color index parsed or {@link #COLOR_UNKNOWN}.
     *
     * @return the color index (in the array) or {@link #COLOR_UNKNOWN} if none could be parsed.
     */
    protected abstract int getColor();

    /**
     * Encode the color and tip passed into a valid payment preference.
     *
     * @param color the color to encode.
     * @param tip the tip to encode.
     * @return String with the encoded color/tip values.
     */
    @NonNull
    /* package */abstract String encode(final int color, final Tip<?> tip);

    /**
     * Gets the color index of the provided {@code preferenceData}.
     *
     * @param preferenceData the preference string from the QR code.
     * @return the color from the QR code or {@link #COLOR_UNKNOWN} if parsing failed.
     */
    public static int getColorPreference(@NonNull final String preferenceData) {
        int color = COLOR_UNKNOWN;

        if (!TextUtils.isEmpty(preferenceData)) {
            color = getPreferenceVersion(preferenceData).getColor();
        }

        return color;
    }

    /**
     * Get the version of the payment preferences passed.
     *
     * @param prefs the payment preferences passed
     * @return {@link PaymentPreferences} for the version of prefs passed. Will default to the
     *         newest version that we support if we cannot detect the preference type.
     */
    @NonNull
    /* package */static PaymentPreferences getPreferenceVersion(@NonNull final String prefs) {
        return new PaymentPreferencesV3(prefs);
    }
}
