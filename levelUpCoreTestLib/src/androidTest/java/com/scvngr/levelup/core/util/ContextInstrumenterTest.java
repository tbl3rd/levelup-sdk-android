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
package com.scvngr.levelup.core.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

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
