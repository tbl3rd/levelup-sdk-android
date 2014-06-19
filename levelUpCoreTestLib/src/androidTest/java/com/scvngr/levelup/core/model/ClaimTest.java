/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.ClaimJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.Claim}.
 */
public final class ClaimTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel_full_object() throws JSONException {
        final Claim claim = ClaimFixture.getFullModel(1);
        final Claim parceled = ParcelTestUtils.feedThroughParceling(claim);

        assertEquals(claim, parceled);
    }

    @SmallTest
    public void testParcel_minimal_object() throws JSONException {
        final Claim claim = ClaimFixture.getMinimalModel(1);

        final Claim parceled = ParcelTestUtils.feedThroughParceling(claim);
        assertEquals(claim, parceled);
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(ClaimJsonFactory.JsonKeys.class,
                new ClaimJsonFactory(), ClaimFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT", }); //$NON-NLS-1$
    }
}
