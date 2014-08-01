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
