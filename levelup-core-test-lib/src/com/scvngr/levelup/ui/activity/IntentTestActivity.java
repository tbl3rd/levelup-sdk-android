/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.ui.activity;

import android.app.Instrumentation;
import android.content.Intent;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.LogManager;

/**
 * An activity for testing Intents.
 */
public class IntentTestActivity extends SherlockFragmentActivity {
    @Nullable
    private Instrumentation.ActivityResult mActivityResult;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        LogManager.v("Got activity result: %s %s", resultCode, data);
        mActivityResult = new Instrumentation.ActivityResult(resultCode, data);
    }

    @Nullable
    public final Instrumentation.ActivityResult getActivityResult() {
        return mActivityResult;
    }
}

