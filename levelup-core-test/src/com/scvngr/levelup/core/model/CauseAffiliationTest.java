/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.CauseAffiliationJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link CauseAffiliation}.
 */
public final class CauseAffiliationTest extends SupportAndroidTestCase {

    @SmallTest
    public void testConstructor_basic() throws JSONException {
        CauseAffiliationFixture.getFullModel();
    }

    @SmallTest
    public void testParcel() {
        final CauseAffiliation causeAffiliation = CauseAffiliationFixture.getFullModel();

        final Parcel parcel = Parcel.obtain();
        causeAffiliation.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final CauseAffiliation causeAffiliation2 = CauseAffiliation.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals(causeAffiliation, causeAffiliation2);
    }

    @SmallTest
    public void testParcel_nullId() {
        final CauseAffiliation causeAffiliation = CauseAffiliationFixture.getFullModel(null);

        final Parcel parcel = Parcel.obtain();
        causeAffiliation.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final CauseAffiliation causeAffiliation2 = CauseAffiliation.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        assertEquals(causeAffiliation, causeAffiliation2);
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(CauseAffiliationJsonFactory.JsonKeys.class,
                new CauseAffiliationJsonFactory(), CauseAffiliationFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }
}
