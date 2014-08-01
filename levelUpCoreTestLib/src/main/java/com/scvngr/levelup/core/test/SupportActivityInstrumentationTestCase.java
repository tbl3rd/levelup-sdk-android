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

import android.app.Activity;
import android.support.annotation.NonNull;
import android.test.ActivityInstrumentationTestCase2;

/**
 * This works around a bug in Android's class field scrubbing and is otherwise identical to
 * {@link ActivityInstrumentationTestCase2}.
 *
 * @param <T> the Activity under test.
 */
public class SupportActivityInstrumentationTestCase<T extends Activity> extends
        ActivityInstrumentationTestCase2<T> {

    /**
     * @param activityClass
     * @see ActivityInstrumentationTestCase2#ActivityInstrumentationTestCase2(Class)
     */
    public SupportActivityInstrumentationTestCase(@NonNull final Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void scrubClass(final Class<?> testCaseClass) throws IllegalAccessException {
        SupportTestCaseUtils.scrubClass(this);
    }
}
