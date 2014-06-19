/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.CauseAffiliation;
import com.scvngr.levelup.core.model.CauseAffiliationFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.CauseAffiliationJsonFactory}.
 */
public final class CauseAffiliationJsonFactoryTest extends SupportAndroidTestCase {
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        final CauseAffiliation causeAffiliation =
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject());
        assertEquals(
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject()),
                causeAffiliation);
        assertEquals(CauseAffiliationFixture.getFullModel().getCauseWebServiceId(),
                causeAffiliation.getCauseWebServiceId());
        assertEquals(CauseAffiliationFixture.getFullModel().getPercentDonation(),
                causeAffiliation.getPercentDonation());
    }

    @SmallTest
    public void testJsonParse_nullId() throws JSONException {
        final CauseAffiliation causeAffiliation =
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject(null));
        assertEquals(
                new CauseAffiliationJsonFactory().from(CauseAffiliationFixture.getFullJsonObject(null)),
                causeAffiliation);
        assertEquals(CauseAffiliationFixture.getFullModel(null).getCauseWebServiceId(),
                causeAffiliation.getCauseWebServiceId());
        assertEquals(CauseAffiliationFixture.getFullModel(null).getPercentDonation(),
                causeAffiliation.getPercentDonation());
    }
}
