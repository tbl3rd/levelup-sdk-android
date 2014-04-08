/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.scvngr.levelup.core.annotation.NonNull;

/**
 * Test cases that need access to Resources or depend on Activity context should extend this class.
 * <p/>
 * This class implements a workaround in {@link #setContext} to avoid a race condition which causes
 * {@link Context#getApplicationContext} to temporarily return null.
 *
 * @param <T> the type of {@link Application}.
 * @see SupportTestCaseUtils#waitForApplicationContext
 */
public class SupportApplicationTestCase<T extends Application> extends ApplicationTestCase<T> {

    /**
     * Constructor.
     *
     * @param applicationClass the Application class.
     */
    public SupportApplicationTestCase(@NonNull final Class<T> applicationClass) {
        super(applicationClass);
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
