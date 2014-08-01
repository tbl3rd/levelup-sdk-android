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

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.model.tip.Tip;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

/**
 * <p>Class to encompass the logic to encode V2 payment tokens combined with
 * {@link PaymentPreferencesV3} data.</p>
 *
 * <p>Looks like (base 36 encoded):</p>
 *
 * <blockquote>{@code LU0201234671234567890123030002LU}</blockquote>
 */
@Immutable
@LevelUpApi(contract = LevelUpApi.Contract.INTERNAL)
public final class PaymentTokenV2 extends LevelUpCode {

    /**
     * Constructor.
     *
     * @param qrData the QR code data scanned.
     */
    protected PaymentTokenV2(@NonNull final String qrData) {
        super(qrData);
    }

    /**
     * Parses a V2 version of the code to try to get the user's selected color. IE:
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

    @Override
    @NonNull
    String encodePaymentPreferences(final int color, final Tip<?> tip) {
        return NullUtils.format("%s%s", mData,
                PaymentPreferences.getPreferenceVersion(mData).encode(color, tip));
    }
}
