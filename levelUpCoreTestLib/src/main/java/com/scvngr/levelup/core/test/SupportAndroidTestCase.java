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
package com.scvngr.levelup.core.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.util.NullUtils;

/**
 * Test cases that need access to Resources or depend on Activity context should extend this class.
 * <p/>
 * This class implements a workaround in {@link #setContext} to avoid a race condition which causes
 * {@link Context#getApplicationContext} to temporarily return null.
 *
 * @see SupportTestCaseUtils#waitForApplicationContext
 */
public class SupportAndroidTestCase extends AndroidTestCase {

    @Override
    @NonNull
    public Context getContext() {
        return NullUtils.nonNullContract(super.getContext());
    }

    @Override
    public void setContext(final Context context) {
        super.setContext(context);

        SupportTestCaseUtils.waitForApplicationContext(context);
    }

    @Override
    protected void scrubClass(final Class<?> testCaseClass) throws IllegalAccessException {
        SupportTestCaseUtils.scrubClass(this);
    }
}
