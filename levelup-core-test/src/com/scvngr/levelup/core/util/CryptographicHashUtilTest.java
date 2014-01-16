/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;

import junit.framework.TestCase;

/**
 * Tests {@link CryptographicHashUtil}.
 */
public final class CryptographicHashUtilTest extends TestCase {

    @SmallTest
    public void testGetHexHash_empty_sha256() {
        final String hash = CryptographicHashUtil.getHexHash("", Algorithms.SHA256); //$NON-NLS-1$

        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash); //$NON-NLS-1$
    }

    @SmallTest
    public void testGetHexHash_non_empty_sha256() {
        final String hash =
                CryptographicHashUtil.getHexHash(
                        "The tomato is an interesting fruit", Algorithms.SHA256); //$NON-NLS-1$

        assertEquals("5419fda87b4000359f2ceaec82d82c8bab595ff4af763e1441bcaa4256952975", hash); //$NON-NLS-1$
    }

    @SmallTest
    public void testGetHexHash_non_empty_sha1() {
        final String hash =
                CryptographicHashUtil.getHexHash(
                        "The tomato is an interesting fruit", Algorithms.SHA1); //$NON-NLS-1$

        assertEquals("eb58c62add150a13e1883c52f52f63d136424d09", hash); //$NON-NLS-1$
    }
}
