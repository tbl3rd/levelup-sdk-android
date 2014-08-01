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
        assertEquals(State.PENDING, State.forString("pending"));
        assertEquals(State.ACCEPTED, State.forString("accepted"));
        assertEquals(State.REJECTED, State.forString("rejected"));
    }

    public void testState_invalid() {
        try {
            State.forString("Invalid");
            fail("Expected exception for invalid state.");
        } catch (final IllegalArgumentException e) {
            // Expected exception.
        }

        try {
            State.forString("PENDING");
            fail("Expected exception for invalid state.");
        } catch (final IllegalArgumentException e) {
            // Expected exception.
        }
    }
}
