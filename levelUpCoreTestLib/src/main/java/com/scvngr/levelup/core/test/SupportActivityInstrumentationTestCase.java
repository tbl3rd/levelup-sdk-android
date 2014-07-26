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
