/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.ui.test;

import android.app.Instrumentation;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.scvngr.levelup.core.test.TestThreadingUtils;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.ui.activity.FragmentTestActivity;

/**
 * Test case with an empty host Activity for testing Fragments.
 * <p>
 * To test a Fragment, subclass this class and call {@link #startActivitySync()} to obtain the
 * Activity to host the Fragment under test.
 */
public class FragmentUnitTestCase extends LevelUpUnitTestCase<FragmentTestActivity> {

    /**
     * Constructor.
     */
    public FragmentUnitTestCase() {
        super(FragmentTestActivity.class);
    }

    /**
     * Adds a fragment in a transaction synchronized in the main thread (tagged with the fragment's
     * class name). Does not add to the view hierarchy.
     *
     * @param fragment Fragment to add.
     */
    protected final void addFragmentInMainSync(@NonNull final Fragment fragment) {
        addFragmentInMainSync(fragment, false);
    }

    /**
     * Adds a fragment in a transaction synchronized in the main thread (tagged with the fragment's
     * class name).
     *
     * @param fragment Fragment to add.
     * @param inView adds the fragment to the view hierarchy if true.
     */
    protected final void addFragmentInMainSync(@NonNull final Fragment fragment,
            final boolean inView) {
        TestThreadingUtils.addFragmentInMainSync(getInstrumentation(), getActivity(), fragment,
                inView);
    }

    /**
     * Adds a fragment in a transaction synchronized in the main thread (tagged with the fragment's
     * class name).
     *
     * @param fragment Fragment to add.
     * @param inView adds the fragment to the view hierarchy if true.
     * @param tag the Fragment's tag for tracking in the FragmentManager.
     */
    protected final void addFragmentInMainSync(@NonNull final Fragment fragment,
            final boolean inView, @NonNull final String tag) {
        TestThreadingUtils.addFragmentInMainSync(getInstrumentation(), getActivity(), fragment,
                inView, tag);
    }

    @Override
    @NonNull
    public FragmentTestActivity getActivity() {
        return NullUtils.nonNullContract(super.getActivity());
    }

    @Override
    @NonNull
    public Instrumentation getInstrumentation() {
        return NullUtils.nonNullContract(super.getInstrumentation());
    }
}
