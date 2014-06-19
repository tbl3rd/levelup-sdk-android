/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
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
