/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.Scan}.
 */
public final class ScanTest extends SupportAndroidTestCase {

    @SmallTest
    public void testConstructor() {
        new Scan("test");

        try {
            new Scan(null);
            fail("exception should be thrown");
        } catch (final NullPointerException e) {
            // Expected exception.
        }
    }

    @SmallTest
    public void testGetData() {
        final Scan scan = new Scan("scan_data");
        assertEquals("scan_data", scan.getData());
    }

    @SmallTest
    public void testParcel() {
        final Scan scan = new Scan("scan_data");
        final Parcel parcel = Parcel.obtain();
        scan.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        final Scan parceled = Scan.CREATOR.createFromParcel(parcel);

        assertEquals(scan, parceled);
    }

    @SmallTest
    public void testEqualsAndHashCode() {
        final Scan scan1 = new Scan("scan1");
        MoreAsserts.checkEqualsAndHashCodeMethods(scan1, scan1, true);

        final Scan scan2 = new Scan("scan2");
        MoreAsserts.checkEqualsAndHashCodeMethods(scan1, scan2, false);
    }
}
