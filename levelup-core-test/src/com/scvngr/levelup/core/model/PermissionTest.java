package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link Permission}.
 */
public final class PermissionTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcelling_full() {
        final Permission fullModel = PermissionFixture.getFullModel1();
        ParcelTestUtils.assertParcelableRoundtrips(fullModel);
    }
}
