/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.LocationCreditJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;

import org.json.JSONException;

/**
 * Tests {@link LocationCredit}
 */
public final class LocationCreditTest extends AndroidTestCase {
    @SmallTest
    public void testParcel() throws JSONException {
        final LocationCredit locationCredit = LocationCreditFixture.getFullModel(5);
        ParcelTestUtils.assertParcelableRoundtrips(locationCredit);
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(LocationCreditJsonFactory.JsonKeys.class,
                new LocationCreditJsonFactory(), LocationCreditFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }
}
