/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.scvngr.levelup.core.test.R;

/**
 * Activity to host fragments for testing.
 */
public class TestFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.levelup_activity_test);
    }
}

