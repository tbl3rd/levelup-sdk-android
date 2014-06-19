package com.scvngr.levelup.core.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.PermissionsRequest.State;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.model.PermissionsRequest}.
 */
public final class PermissionsRequestTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcelling_full() {
        final PermissionsRequest fullModel = PermissionsRequestFixture.getFullModel();
        ParcelTestUtils.assertParcelableRoundtrips(fullModel);
    }

    public void testState_valid() {
        assertEquals(State.PENDING, State.forString("pending")); //$NON-NLS-1$
        assertEquals(State.ACCEPTED, State.forString("accepted")); //$NON-NLS-1$
        assertEquals(State.REJECTED, State.forString("rejected")); //$NON-NLS-1$
    }

    public void testState_invalid() {
        try {
            State.forString("Invalid"); //$NON-NLS-1$
            fail("Expected exception for invalid state."); //$NON-NLS-1$
        } catch (final IllegalArgumentException e) {
            // Expected exception.
        }

        try {
            State.forString("PENDING"); //$NON-NLS-1$
            fail("Expected exception for invalid state."); //$NON-NLS-1$
        } catch (final IllegalArgumentException e) {
            // Expected exception.
        }
    }
}
