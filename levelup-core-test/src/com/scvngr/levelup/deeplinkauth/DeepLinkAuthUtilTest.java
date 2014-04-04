/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.deeplinkauth;

import android.content.Intent;
import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.deeplinkauth.DeepLinkAuthUtil.LevelUpNotInstalledException;

import java.util.Arrays;
import java.util.List;

/**
 * Tests {@link DeepLinkAuthUtil}.
 */
public final class DeepLinkAuthUtilTest extends SupportAndroidTestCase {
    private static final int APP_ID_FIXTURE = 5;

    /**
     * The signature of the package under test. This must match the key that's used to sign the
     * test package.
     */
    /* package */static final String APP_SIGNATURE_THIS =
            "A3:87:3F:FE:AC:A7:FB:34:2C:63:6D:90:F6:05:61:8F:B0:6F:9D:C3;com.scvngr.levelup.core.build";

    /**
     * This must match the other fixtures declared in this class.
     */
    private static final Uri DATA_FIXTURE = Uri.parse(
            "lutest://authorization?permission=create_orders&permission=read_user_basic_info&app_id=5");

    /**
     * This must match the AndroidManifest in the build project.
     */
    private static final Intent INTENT_FIXTURE_TEST =
            new Intent(DeepLinkAuthUtil.ACTION_REQUEST_PERMISSIONS, DATA_FIXTURE);

    /**
     * This must match the other fixtures declared in this class.
     */
    private static final List<String> PERMISSIONS_FIXTURE =
            Arrays.asList("create_orders", "read_user_basic_info");

    /**
     * Tests {@link DeepLinkAuthUtil#convertBytesToHex} with a delimiter.
     */
    @SmallTest
    public void testConvertBytesToHex_delimiter() {
        assertEquals("", DeepLinkAuthUtil.convertBytesToHex(new byte[] { }, ':'));
        assertEquals("00", DeepLinkAuthUtil.convertBytesToHex(new byte[] { 0x00 }, ':'));
        assertEquals("00:01",
                DeepLinkAuthUtil.convertBytesToHex(new byte[] { (byte) 0x00, 0x01 }, ':'));
        assertEquals("da:1a",
                DeepLinkAuthUtil.convertBytesToHex(new byte[] { (byte) 0xDA, 0x1A }, ':'));
        assertEquals("01:02:03",
                DeepLinkAuthUtil.convertBytesToHex(new byte[] { (byte) 0x01, 0x02, 0x03 }, ':'));
        assertEquals("09:0a:0b:0f:10:11:12", DeepLinkAuthUtil.convertBytesToHex(
                        new byte[] { (byte) 0x09, 0x0A, 0x0B, 0x0F, 0x10, 0x11, 0x12 }, ':')
        );
    }

    /**
     * Tests {@link DeepLinkAuthUtil#convertBytesToHex} without a delimiter.
     */
    @SmallTest
    public void testConvertBytesToHex_noDelimiter() {
        assertEquals("", DeepLinkAuthUtil.convertBytesToHex(new byte[] { }, null));
        assertEquals("00", DeepLinkAuthUtil.convertBytesToHex(new byte[] { 0x00 }, null));
        assertEquals("0001",
                DeepLinkAuthUtil.convertBytesToHex(new byte[] { (byte) 0x00, 0x01 }, null));
        assertEquals("da1a",
                DeepLinkAuthUtil.convertBytesToHex(new byte[] { (byte) 0xDA, 0x1A }, null));
        assertEquals("010203",
                DeepLinkAuthUtil.convertBytesToHex(new byte[] { (byte) 0x01, 0x02, 0x03 }, null));
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getPackageSignature} with a test signature.
     */
    @SmallTest
    public void testGetPackageSignature_byIntent() {
        final String signature =
                DeepLinkAuthUtil.getPackageSignature(getContext(), INTENT_FIXTURE_TEST);

        assertEquals(APP_SIGNATURE_THIS, signature);
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getRequestPermissionsIntent} without a valid signature.
     */
    @SmallTest
    public void testRequestPermissions_invalid() {
        try {
            DeepLinkAuthUtil
                    .getRequestPermissionsIntent(getContext(), APP_ID_FIXTURE, PERMISSIONS_FIXTURE,
                            new String[] { /* intentionally blank */ });
            fail("Expected exception");
        } catch (final LevelUpNotInstalledException e) {
            // Expected exception.
        }
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getRequestPermissionsIntent} with a valid signature.
     */
    @SmallTest
    public void testRequestPermissions_valid() throws LevelUpNotInstalledException {
        final Intent intent = DeepLinkAuthUtil
                .getRequestPermissionsIntent(getContext(), APP_ID_FIXTURE, PERMISSIONS_FIXTURE,
                        new String[] { APP_SIGNATURE_THIS });

        assertEquals(DeepLinkAuthUtil.ACTION_REQUEST_PERMISSIONS, intent.getAction());
        assertEquals(DATA_FIXTURE, intent.getData());
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getPackageSignature} with an intent that will match a bunch of
     * packages. Because it matches many packages, the intent will resolve to a chooser, whose
     * package should start with "android".
     */
    @SmallTest
    public void testGetPackageSignature_byIntent_multiMatch() {
        final String signature =
                DeepLinkAuthUtil.getPackageSignature(getContext(), new Intent(Intent.ACTION_VIEW));
        assertTrue("must end in 'android'", signature.endsWith("android"));
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getPackageSignature} with an intent that should match no
     * packages.
     */
    @SmallTest
    public void testGetPackageSignature_byIntent_noMatch() {
        final String signature =
                DeepLinkAuthUtil.getPackageSignature(getContext(), new Intent("x-do-nothing"));

        assertNull(signature);
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getPackageSignature} with this class.
     */
    @SmallTest
    public void testGetPackageSignature_byPackage() {
        final String signature =
                DeepLinkAuthUtil.getPackageSignature(getContext(), getContext().getPackageName());

        assertEquals(APP_SIGNATURE_THIS, signature);
    }

    /**
     * Tests {@link DeepLinkAuthUtil#getPackageSignature} with an invalid package name.
     */
    @SmallTest
    public void testGetPackageSignature_byPackage_fail() {
        try {
            DeepLinkAuthUtil.getPackageSignature(getContext(), "");
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            // Exception expected.
        }
    }

    /**
     * Tests {@link DeepLinkAuthUtil#isAuthenticLevelUp} with no signatures.
     */
    @SmallTest
    public void testIsAuthenticLevelUp_empty() {
        assertFalse(DeepLinkAuthUtil.isAuthenticLevelUp(getContext(), INTENT_FIXTURE_TEST,
                new String[] {}));
    }

    /**
     * Tests {@link DeepLinkAuthUtil#isAuthenticLevelUp} with no matching signatures.
     */
    @SmallTest
    public void testIsAuthenticLevelUp_nope() {
        assertFalse(DeepLinkAuthUtil
                .isAuthenticLevelUp(getContext(), INTENT_FIXTURE_TEST, new String[] {
                                "none of these", APP_SIGNATURE_THIS + "_different",
                                "ABCDEFG;com.example.one.two.three"
                        }
                ));
    }

    /**
     * Tests {@link DeepLinkAuthUtil#isAuthenticLevelUp} with a valid signature amongst some
     * invalid ones.
     */
    @SmallTest
    public void testIsAuthenticLevelUp_theRealThing() {
        assertTrue(DeepLinkAuthUtil.isAuthenticLevelUp(getContext(), INTENT_FIXTURE_TEST,
                new String[] { "Something else", APP_SIGNATURE_THIS, "another item" }));
    }

    /**
     * Tests {@link DeepLinkAuthUtil#toIntent} with fixture data.
     */
    @SmallTest
    public void testToIntent_withIntent() {
        final Intent intent =
                DeepLinkAuthUtil.toIntent(getContext(), APP_ID_FIXTURE, PERMISSIONS_FIXTURE);
        assertEquals(DeepLinkAuthUtil.ACTION_REQUEST_PERMISSIONS, intent.getAction());
        assertEquals(DATA_FIXTURE, intent.getData());
        assertNull(intent.getCategories());
        assertNull(intent.getStringExtra(DeepLinkAuthUtil.EXTRA_STRING_ACCESS_TOKEN));
    }
}
