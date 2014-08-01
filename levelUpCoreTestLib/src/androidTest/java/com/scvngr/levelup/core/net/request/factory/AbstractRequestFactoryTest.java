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
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.AbstractRequestFactory}.
 */
public final class AbstractRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        {
            final RequestFactoryUnderTest factory = new RequestFactoryUnderTest(getContext(), null);
            assertEquals(getContext().getApplicationContext(), factory.getContext());
            assertNull(factory.getAccessTokenRetriever());
        }

        {
            final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
            final RequestFactoryUnderTest factory =
                    new RequestFactoryUnderTest(getContext(), retriever);
            assertEquals(getContext().getApplicationContext(), factory.getContext());
            assertEquals(retriever, factory.getAccessTokenRetriever());
        }
    }

    /**
     * Test implementation of a {@link com.scvngr.levelup.core.net.request.factory.AbstractRequestFactory}.
     */
    public static final class RequestFactoryUnderTest extends AbstractRequestFactory {

        public RequestFactoryUnderTest(@NonNull final Context context,
                @Nullable final MockAccessTokenRetriever retriever) {
            super(context, retriever);
        }
    }
}
