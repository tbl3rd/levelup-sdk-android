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

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;

import junit.framework.TestCase;

/**
 * Tests {@link com.scvngr.levelup.core.util.CryptographicHashUtil}.
 */
public final class CryptographicHashUtilTest extends TestCase {

    @SmallTest
    public void testGetHexHash_empty_sha256() {
        final String hash = CryptographicHashUtil.getHexHash("", Algorithms.SHA256);

        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }

    @SmallTest
    public void testGetHexHash_non_empty_sha256() {
        final String hash =
                CryptographicHashUtil.getHexHash(
                        "The tomato is an interesting fruit", Algorithms.SHA256);

        assertEquals("5419fda87b4000359f2ceaec82d82c8bab595ff4af763e1441bcaa4256952975", hash);
    }

    @SmallTest
    public void testGetHexHash_non_empty_sha1() {
        final String hash =
                CryptographicHashUtil.getHexHash(
                        "The tomato is an interesting fruit", Algorithms.SHA1);

        assertEquals("eb58c62add150a13e1883c52f52f63d136424d09", hash);
    }
}
