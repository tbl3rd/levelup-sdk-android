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
package com.scvngr.levelup.core.util;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

import net.jcip.annotations.ThreadSafe;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This is a utility class to generate cryptographic hashes.
 */
@LevelUpApi(contract = Contract.INTERNAL)
@ThreadSafe
public final class CryptographicHashUtil {

    /*
     * Text encoding for UTF-8.
     */
    @NonNull
    private static final String UTF8 = "UTF-8";

    /**
     * Sorted array of hex characters.
     * <p>
     * Useful for converting from decimal to hexadecimal.
     */
    @NonNull
    private static final char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Set of cryptographic hashing algorithms that can be used with
     * {@link CryptographicHashUtil#getHexHash(String, Algorithms)}.
     */
    @ThreadSafe
    public static enum Algorithms {

        /**
         * SHA-256 algorithm.
         */
        @NonNull
        SHA256,

        /**
         * SHA1 algorithm.
         */
        @NonNull
        SHA1
    }

    /**
     * Takes any string and returns a cryptographic hash of that string in hexadecimal.
     *
     * @param toHash String to hash.
     * @param algorithm The hashing algorithm to use.
     * @return cryptographic hash of {@code toHash} in hexadecimal.
     */
    @NonNull
    public static String getHexHash(@NonNull final String toHash,
            @NonNull final Algorithms algorithm) {
        PreconditionUtil.assertNotNull(toHash, "toHash");
        PreconditionUtil.assertNotNull(toHash, "algorithm");

        String hash = "";
        try {
            final byte[] data =
                    NullUtils.nonNullContract(MessageDigest.getInstance(algorithm.name()).digest(
                            toHash.getBytes(UTF8)));

            hash = convertBytesToHex(data);
        } catch (final UnsupportedEncodingException e) {
            // this should never occur
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            // this should never occur
            throw new RuntimeException(e);
        }

        return hash;
    }

    /**
     * Helper method to convert a byte[] into a hexadecimal string.
     *
     * @param data data to convert.
     * @return A hex version of the input.
     */
    @NonNull
    private static String convertBytesToHex(@NonNull final byte[] data) {
        final int length = data.length;
        final char[] chars = new char[2 * length];

        for (int x = 0; x < length; x++) {
            // CHECKSTYLE:OFF hex math, not really magic numbers.
            // Shift the masked bits into the lower positions so we can index the array.
            chars[2 * x] = HEXDIGITS[(data[x] & 0xF0) >>> 4];
            chars[2 * x + 1] = HEXDIGITS[data[x] & 0x0F];
            // CHECKSTYLE:ON
        }

        return NullUtils.nonNullContract(String.valueOf(chars));
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private CryptographicHashUtil() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
