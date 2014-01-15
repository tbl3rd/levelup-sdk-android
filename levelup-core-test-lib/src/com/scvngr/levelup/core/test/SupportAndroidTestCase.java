package com.scvngr.levelup.core.test;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Test cases that need access to Resources or depend on Activity context should extend this class.
 * <p/>
 * This class implements a workaround in {@link #setContext} to avoid a race condition which causes
 * {@link Context#getApplicationContext} to temporarily return null.
 *
 * @see SupportTestCaseUtils#waitForApplicationContextIfNecessary
 */
public class SupportAndroidTestCase extends AndroidTestCase {

    @Override
    public void setContext(final Context context) {
        super.setContext(context);

        SupportTestCaseUtils.waitForApplicationContextIfNecessary(context);
    }
}