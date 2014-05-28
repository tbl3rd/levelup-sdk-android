/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link ContextInstrumenter}.
 */
public final class ContextInstrumenterTest extends SupportAndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ContextInstrumenterHelper.clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        ContextInstrumenterHelper.clear();
    }

    /**
     * Test {@link ContextInstrumenter#from} returns instrumented contexts when a
     * {@link ContextInstrumenter.Provider} is configured.
     */
    @SmallTest
    public void testFrom_withProvider() {
        ContextInstrumenterHelper.set(new ContextInstrumenter.Provider() {
            @NonNull
            @Override
            public Context get(@NonNull final Context context) {
                return new TestContextWrapper(context);
            }
        });

        final Context context = getContext();
        final Context instrumentedContext = ContextInstrumenter.from(context);

        assertNotSame(context, instrumentedContext);
        assertTrue(instrumentedContext instanceof TestContextWrapper);
        assertSame(context, ((TestContextWrapper) instrumentedContext).getBaseContext());
    }

    /**
     * Test {@link ContextInstrumenter#from} returns the context as-is when a
     * {@link ContextInstrumenter.Provider} is not configured.
     */
    @SmallTest
    public void testFrom_withoutProvider() {
        final Context context = getContext();
        final Context instrumentedContext = ContextInstrumenter.from(context);

        assertSame(context, instrumentedContext);
    }

    /**
     * Test implementation of {@link ContextWrapper}.
     */
    private static final class TestContextWrapper extends ContextWrapper {

        public TestContextWrapper(@NonNull final Context context) {
            super(context);
        }
    }
}
