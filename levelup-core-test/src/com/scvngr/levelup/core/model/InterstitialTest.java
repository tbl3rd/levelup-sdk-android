/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.InterstitialJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link Interstitial}
 */
public final class InterstitialTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel() {
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getMinimalModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getClaimActionModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getNoActionModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getShareActionModel());
        ParcelTestUtils.assertParcelableRoundtrips(InterstitialFixture.getUrlActionModel());
    }

    @SmallTest
    public void testAction_unrecognized_or_no_action() {
        // Both unrecognized and no_action interstitials return null for getAction()
        assertNull(InterstitialFixture.getMinimalModel().getAction());
        assertNull(InterstitialFixture.getNoActionModel().getAction());
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(InterstitialJsonFactory.JsonKeys.class,
                new InterstitialJsonFactory(), InterstitialFixture.getMinimalJsonObject(),
                new String[] { "MODEL_ROOT", "ACTION" }); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotSame(InterstitialFixture.getClaimActionModel(), InterstitialFixture
                .getMinimalModel());
    }
}
