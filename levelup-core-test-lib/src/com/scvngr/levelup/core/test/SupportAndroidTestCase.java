/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.content.Context;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.NonNull;
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
}
