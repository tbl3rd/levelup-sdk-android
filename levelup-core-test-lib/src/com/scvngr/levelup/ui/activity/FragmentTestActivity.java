/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.ui.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import com.scvngr.levelup.core.test.R;

/**
 * Activity to host fragments for testing. Gross.
 */
public class FragmentTestActivity extends SherlockFragmentActivity {

    /**
     * Helper to track if the actionbar progress indicator is indeterminate or not. This is needed
     * because the super classes do not expose a getter.
     */
    public boolean mIsProgressBarIndeterminate = false;

    /**
     * Helper to track if the actionbar progress indicator is visible or not. This is needed because
     * the super classes do not expose a getter.
     */
    public boolean mIsProgressBarIndeterminateVisible = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.levelup_activity_fragment_test);
    }

    @Override
    public final void setSupportProgressBarIndeterminate(final boolean indeterminate) {
        mIsProgressBarIndeterminate = indeterminate;
        super.setSupportProgressBarIndeterminate(indeterminate);
    }

    @Override
    public final void setSupportProgressBarIndeterminateVisibility(final boolean visible) {
        mIsProgressBarIndeterminateVisible = visible;
        super.setSupportProgressBarIndeterminateVisibility(visible);
    }
}
