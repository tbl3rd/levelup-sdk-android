package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link App}.
 */
public final class AppTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcelling_full() {
        final App fullModel = AppFixture.getFullModel();
        ParcelTestUtils.assertParcelableRoundtrips(fullModel);
    }

    @SmallTest
    public void testParcelling_minimal() {
        final App minimalModel = AppFixture.getMinimalModel();
        ParcelTestUtils.assertParcelableRoundtrips(minimalModel);
    }
}
