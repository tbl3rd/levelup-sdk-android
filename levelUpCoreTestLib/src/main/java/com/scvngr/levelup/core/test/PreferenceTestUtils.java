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
package com.scvngr.levelup.core.test;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.ThreadSafe;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * A test utility class that helps test preference keys defined by classes such as
 * {@link com.scvngr.levelup.core.storage.CorePreferenceUtil}.
 */
@ThreadSafe
public final class PreferenceTestUtils {

    /**
     * Pattern that filters Java package names of a particular form such as
     * {@code com.scvngr.levelup.example.storage.preference}. To enforce consistent preference key
     * names, the package name is expected to end with the identifier {@code storage} followed
     * by {@code preference}.
     */
    @NonNull
    private static final Pattern PREFIX_PATTERN =
            Pattern.compile("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*.)+"
                    + "storage\\.preference");

    /**
     * Asserts the prefix of the preference keys defined by {@code clazz} begin with the specified
     * prefix plus an additional dot.
     *
     * @param clazz the class which defines preference keys.
     * @param prefix the prefix of the preference keys.
     */
    public static void assertKeysArePrefixed(@NonNull final Class<?> clazz,
            @NonNull final String prefix) {
        assertTrue(PREFIX_PATTERN.matcher(prefix).matches());

        final String prefixWithDot = prefix + ".";

        for (final Field field : clazz.getFields()) {
            final String key = getValueOfStringField(NullUtils.nonNullContract(field));

            assertTrue(prefixWithDot.length() < key.length());
            assertTrue(key.startsWith(prefixWithDot));
        }
    }

    /**
     * Asserts the preference keys defined by the specified class are unique.
     *
     * @param preferenceClass the class which defines preference keys.
     */
    public static void assertKeysAreUnique(@NonNull final Class<?> preferenceClass) {
        final HashSet<String> keys = new HashSet<String>();

        for (final Field field : preferenceClass.getFields()) {
            final String key = getValueOfStringField(NullUtils.nonNullContract(field));

            assertFalse(keys.contains(key));
            keys.add(key);
        }
    }

    /**
     * Get the value of a String Field.
     *
     * @param field the Field which must be a String type.
     * @return the value of the String.
     */
    @NonNull
    private static String getValueOfStringField(@NonNull final Field field) {
        try {
            assertEquals(String.class, field.getType());

            return NullUtils.nonNullContract((String) field.get(null));
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PreferenceTestUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
