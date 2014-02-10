/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.ui.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.test.ActivityUnitTestCase;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.LevelUpConnectionHelper;
import com.scvngr.levelup.core.test.R;
import com.scvngr.levelup.core.test.TestThreadingUtils;
import com.scvngr.levelup.core.util.NullUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * UnitTestCase with helpful threading and fragment methods; for situations when you don't need a
 * fully instrumented test case or want to wrap or mock contexts or other dependencies.
 * <p>
 * To test an activity , subclass this class and call {@link #startActivitySync()} to obtain the
 * Activity after it's gone through the simulated lifecycle up to onResume().
 * <p>
 * The tear down will call the simulated lifecycle's onDestroy() so that the activity doesn't live
 * past the test's scope.
 *
 * @param <T> the Activity to test.
 */
public abstract class LevelUpUnitTestCase<T extends FragmentActivity> extends
        ActivityUnitTestCase<T> {

    /**
     * Reference to the activity created in {@link #startActivitySync()} so that the tear down can
     * properly destroy the activity.
     */
    @Nullable
    /* package */ AtomicReference<FragmentActivity> mActivityReference;

    public LevelUpUnitTestCase(final Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Disable all network connections.
        LevelUpConnectionHelper.setNetworkEnabled(false);
        LevelUpConnectionHelper.clearInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (null != mActivityReference) {
            stopActivitySync();
        }

        LevelUpConnectionHelper.clearInstance();
    }

    /**
     * Validates that a fragment was added to our test activity successfully. Will fail if the
     * activity isn't a FragmentActivity, but that's the right behavior in that case.
     *
     * @param tag the tag to check for.
     */
    protected final void validateFragmentAdded(@NonNull final String tag) {
        validateFragmentAdded(tag, TestThreadingUtils.PARENT_ID_UNDEFINED);
    }

    /**
     * Validates that a fragment was added to our test activity successfully. Will fail if the
     * activity isn't a FragmentActivity, but that's the right behavior in that case.
     *
     * @param tag the tag to check for.
     */
    protected final void validateFragmentAdded(@NonNull final String tag, final int parentId) {
        TestThreadingUtils.validateFragmentAdded(getInstrumentation(), getActivity(), getActivity()
                .getSupportFragmentManager(), tag, parentId);
    }

    /**
     * Validates that a fragment was removed from our test activity successfully. Will fail if the
     * activity isn't a FragmentActivity, but that's the right behavior in that case.
     *
     * @param tag the tag to check for.
     */
    protected final void validateFragmentRemoved(@NonNull final String tag) {
        TestThreadingUtils.validateFragmentRemoved(getInstrumentation(), getActivity(),
                getActivity().getSupportFragmentManager(), tag);
    }

    /**
     * Executes a runnable on the main thread.
     *
     * @param runnable to execute.
     */
    protected final void autoSyncRunnable(@NonNull final Runnable runnable) {
        TestThreadingUtils.runOnMainSync(getInstrumentation(), getActivity(), runnable);
    }

    /**
     * Simulates a click event directly to a view for unit testing it. This is needed because
     * TouchUtils.clickView doesn't work properly with ActivityUnitTestCases (since there's no UI).
     *
     * @param fragment the fragment the view to click on is located inside.
     * @param id the ID of the view in the fragment to click.
     */
    protected final void clickViewInMainSync(@NonNull final Fragment fragment, final int id) {
        autoSyncRunnable(new Runnable() {
            @Override
            public void run() {
                fragment.getView().findViewById(id).performClick();
            }
        });
    }

    /**
     * Saves and restores a {@link Fragment} from instance state, using
     * {@link FragmentManager#saveFragmentInstanceState(Fragment)} and
     * {@link Fragment#setInitialSavedState(SavedState)}. This saves the state, removes the
     * fragment, and then calls {@link TestThreadingUtils#addFragmentInMainSync}.
     *
     * @param fragment Fragment to remove and re-add.
     * @return the new instance of the input fragment created using the saved/restored state.
     */
    @NonNull
    protected final <F extends Fragment> F
            saveAndRestoreFragmentStateSync(@NonNull final F fragment) {
        return TestThreadingUtils.saveAndRestoreFragmentStateSync(getInstrumentation(),
                getActivity(), fragment);
    }

    @NonNull
    @Override
    protected T startActivity(@NonNull final Intent intent,
            @Nullable final Bundle savedInstanceState,
            @Nullable final Object lastNonConfigurationInstance) {
        // Set a custom theme on creation to prevent ActionBarSherlock crashes on Android 2.3.
        final Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.AppTheme);
        setActivityContext(context);
        return super.startActivity(intent, savedInstanceState, lastNonConfigurationInstance);
    }

    /**
     * @return The empty host Activity. The Activity returned by this method will be resumed and
     *         ready to go.
     */
    @NonNull
    protected final FragmentActivity startActivitySync() {
        mActivityReference = new AtomicReference<FragmentActivity>(null);
        @NonNull final AtomicReference<FragmentActivity> activityReference = mActivityReference;

        // Cannot use autosyncrunnable because the activity is not created yet.
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
                final FragmentActivity activity = startActivity(new Intent(), null, null);

                getInstrumentation().callActivityOnStart(activity);
                getInstrumentation().callActivityOnPostCreate(activity, null);
                getInstrumentation().callActivityOnResume(activity);

                activityReference.set(activity);
            }
        });
        getInstrumentation().waitForIdleSync();

        return NullUtils.nonNullContract(activityReference.get());
    }

    private final void stopActivitySync() {
        // Cannot use autosyncrunnable because the activity is not created yet.
        getInstrumentation().runOnMainSync(new Runnable() {

            @Override
            public void run() {
                @SuppressWarnings("null")
                final FragmentActivity activity = mActivityReference.get();

                getInstrumentation().callActivityOnPause(activity);
                getInstrumentation().callActivityOnStop(activity);
                getInstrumentation().callActivityOnDestroy(activity);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    /**
     * Helper to wait for the data load to finish.
     *
     * @param mgr the {@link LoaderManager} to check for running loaders.
     */
    public void validateLoadersFinished(@NonNull final LoaderManager mgr) {
        final CountDownLatch latch = new CountDownLatch(1);
        assertTrue("All loaders should have finished", TestThreadingUtils.waitForAction( //$NON-NLS-1$
                getInstrumentation(), getActivity(), new Runnable() {

                    @Override
                    public void run() {
                        if (!mgr.hasRunningLoaders()) {
                            latch.countDown();
                        }
                    }
                }, latch, true));
    }

    /**
     * Helper method to wait for an action to occur (but that action must be checked on the main
     * thread).
     *
     * @param mainThreadRunnable the runnable that will check the condition and signal success via
     *        the {@link CountDownLatch} passed.
     * @param latch the {@link CountDownLatch} to check for success.
     * @return true if the action happened before the timeout, false otherwise.
     */
    protected boolean waitForActionOnMainThread(@NonNull final Runnable mainThreadRunnable,
            @NonNull final CountDownLatch latch) {
        return TestThreadingUtils.waitForAction(getInstrumentation(), getActivity(),
                mainThreadRunnable, latch, true);
    }
}
