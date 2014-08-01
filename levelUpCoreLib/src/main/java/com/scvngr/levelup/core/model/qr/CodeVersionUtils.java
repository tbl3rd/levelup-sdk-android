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
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.util.CoreLibConstants;

import java.math.BigInteger;

/**
 * Helper class to provide utilities for checking the version of QR code and its sub-codes.
 */
@LevelUpApi(contract = LevelUpApi.Contract.INTERNAL)
public final class CodeVersionUtils {

    /**
     * Utility method to check validity of the provided code. The version must be base 36 encoded.
     *
     * @param code the code string to check.
     * @param expectedLength the length the code must be.
     * @param codeVersionStartIndex the start index (INCLUSIVE) to get the code version from.
     * @param codeVersionEndIndex the end index (EXCLUSIVE) to get the code version from.
     * @param expectedVersion the version code that the code's version should be checked against.
     * @return true if the code passes the expected length and version checks.
     */
    public static boolean isValidCode(@NonNull final String code, final int expectedLength,
            final int codeVersionStartIndex, final int codeVersionEndIndex,
            final int expectedVersion) {
        boolean isValid = true;

        if (code.length() != expectedLength) {
            isValid = false;
        } else {
            final String versionCode = code.substring(codeVersionStartIndex, codeVersionEndIndex);
            final BigInteger i = new BigInteger(versionCode, Character.MAX_RADIX);

            if (i.intValue() != expectedVersion) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Left pads a string with zeros.
     *
     * @param toPad the string to pad with zeros.
     * @param finalSize the required size of the string. (cannot be negative)
     * @return a string padded with zeros on the left.
     *
     * @throws IllegalArgumentException if finalSize is negative.
     */
    @NonNull
    public static String leftPadWithZeros(@Nullable final String toPad, final int finalSize) {
        final StringBuilder temp = new StringBuilder();

        if (CoreLibConstants.IS_PARAMETER_CHECKING_ENABLED) {
            if (finalSize < 0) {
                throw new IllegalArgumentException("finalSize cannot be negative");
            }
        }

        if (null != toPad) {
            temp.append(toPad);
        }

        if (temp.length() < finalSize) {
            do {
                temp.insert(0, '0');
            } while (finalSize != temp.length());
        }

        return temp.toString();
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private CodeVersionUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
