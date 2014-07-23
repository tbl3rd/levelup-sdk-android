/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.test.AndroidTestCase;
import android.view.View;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.NullUtils;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Methods to help with some of the intricacies of threading in the test cases.
 */
public final class TestThreadingUtils {

    /**
     * Sentinel value to pass to
     * {@link #validateFragmentAdded(Instrumentation, Activity, FragmentManager, String, int)} if no
     * parent id should be validated.
     */
    public static final int PARENT_ID_UNDEFINED = -1;

    private static final long WAIT_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(4L);

    private static final long WAIT_SLEEP_MILLIS = 20L;

    /**
     * Adds a fragment in a transaction synchronized in the main thread (tagged with the fragment's
     * class name).
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the {@link FragmentActivity} to add it to.
     * @param fragment Fragment to add.
     * @param inView adds the fragment to the view hierarchy if true.
     */
    public static void addFragmentInMainSync(@NonNull final Instrumentation instrumentation,
            @NonNull final FragmentActivity activity, @NonNull final Fragment fragment,
            final boolean inView) {
        addFragmentInMainSync(instrumentation, activity, fragment, inView,
                NullUtils.nonNullContract(fragment.getClass().getName()));
    }

    /**
     * Adds a fragment in a transaction synchronized in the main thread (tagged with the fragment's
     * class name).
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the {@link FragmentActivity} to add it to.
     * @param fragment Fragment to add.
     * @param inView adds the fragment to the view hierarchy if true.
     * @param tag the Fragment's tag (null tag will fail fast).
     */
    public static void addFragmentInMainSync(@NonNull final Instrumentation instrumentation,
            @NonNull final FragmentActivity activity, @NonNull final Fragment fragment,
            final boolean inView, final String tag) {
        if (null == tag) {
            throw new AssertionError("Cannot add fragment with null tag");
        }

        runOnMainSync(instrumentation, activity, new Runnable() {
            @Override
            public void run() {
                if (!inView) {
                    activity.getSupportFragmentManager().beginTransaction()
                            .add(fragment, fragment.getClass().getName()).commit();
                } else {
                    activity.getSupportFragmentManager().beginTransaction()
                            .add(R.id.levelup_activity_content, fragment, tag).commit();
                }

                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
    }

    /**
     * Saves a {@link Fragment} to instance state, removes it from the activity. Then creates a new
     * one of the same class, restores the instance state, and re-adds it to the activity.
     *
     * @param <T> the type of Fragment
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the {@link FragmentActivity} to add it to.
     * @param fragment Fragment to remove and re-add.
     * @return the new instance of the input fragment created using the saved/restored state.
     */
    @NonNull
    public static <T extends Fragment> T saveAndRestoreFragmentStateSync(
            @NonNull final Instrumentation instrumentation,
            @NonNull final FragmentActivity activity, @NonNull final T fragment) {
        final boolean inView = fragment.isInLayout();

        final FragmentManager fm = activity.getSupportFragmentManager();
        final SavedState savedState = fm.saveFragmentInstanceState(fragment);

        @SuppressWarnings("unchecked")
        final T newInstance = (T) Fragment.instantiate(activity, fragment.getClass().getName());
        newInstance.setInitialSavedState(savedState);

        runOnMainSync(instrumentation, activity, new Runnable() {
            @Override
            public void run() {
                fm.beginTransaction().remove(fragment).commit();
            }
        });

        addFragmentInMainSync(instrumentation, activity, newInstance, inView);

        return newInstance;
    }

    /**
     * Validates that the Activity becomes finished.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity to check.
     */
    public static void validateActivityFinished(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AndroidTestCase.assertTrue(waitForAction(instrumentation, activity, new Runnable() {

            @Override
            public void run() {
                if (activity.isFinishing()) {
                    countDownLatch.countDown();
                }
            }
        }, countDownLatch, true));
    }

    /**
     * Validates that a fragment was added successfully. Does not validate the container it was
     * added to.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was added to (null will fail
     *        validation).
     * @param tag the tag to check for (null will fail validation).
     */
    public static void validateFragmentAdded(@NonNull final Instrumentation instrumentation,
            final Activity activity, final FragmentManager fragmentManager, final String tag) {
        validateFragmentAdded(instrumentation, activity, fragmentManager, tag, PARENT_ID_UNDEFINED);
    }

    /**
     * Validates that a fragment was added successfully and validates the ID of the container it was
     * added to.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was added to (null will fail
     *        validation).
     * @param tag the tag to check for (null will fail validation).
     * @param parentId the id of the parent container the fragment is expected to be in or pass
     *        {@link #PARENT_ID_UNDEFINED} if no parent id should be validated.
     */
    public static void validateFragmentAdded(@NonNull final Instrumentation instrumentation,
            final Activity activity, final FragmentManager fragmentManager, final String tag,
            final int parentId) {
        AndroidTestCase.assertNotNull(tag);
        AndroidTestCase.assertNotNull(activity);
        AndroidTestCase.assertNotNull(fragmentManager);
        final CountDownLatch latch = new CountDownLatch(1);
        AndroidTestCase.assertTrue(String.format(Locale.US, "%s added", tag),
                waitForAction(instrumentation, NullUtils.nonNullContract(activity), new Runnable() {
                    @Override
                    public void run() {
                        final Fragment fragment = fragmentManager.findFragmentByTag(tag);

                        if (null != fragment) {
                            if (PARENT_ID_UNDEFINED != parentId) {
                                final View parent = (View) fragment.getView().getParent();
                                AndroidTestCase.assertEquals(
                                        "In the proper container", parentId,
                                        parent.getId());
                            }

                            latch.countDown();
                        }
                    }
                }, latch, true));
    }

    /**
     * Validates that a fragment was removed.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was removed from (null will fail
     *        validation).
     * @param tag The tag of the fragment (null will fail validation).
     */
    public static void validateFragmentRemoved(@NonNull final Instrumentation instrumentation,
            final Activity activity, final FragmentManager fragmentManager, final String tag) {
        AndroidTestCase.assertNotNull(tag);
        AndroidTestCase.assertNotNull(activity);
        AndroidTestCase.assertNotNull(fragmentManager);
        final CountDownLatch latch = new CountDownLatch(1);
        AndroidTestCase.assertTrue(String.format(Locale.US, "%s removed", tag),
                waitForAction(instrumentation, NullUtils.nonNullContract(activity), new Runnable() {
                    @Override
                    public void run() {
                        final Fragment fragment = fragmentManager.findFragmentByTag(tag);
                        if (null == fragment) {
                            latch.countDown();
                        }
                    }
                }, latch, true));
    }

    /**
     * Helper method to wait for an action to occur.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run.
     * @param runnable the runnable that will check the condition and signal success via the
     *        {@link CountDownLatch} passed.
     * @param latch the {@link CountDownLatch} to check for success.
     * @param isMainThreadRunnable Determine whether or not the runnable must be invoked on the main
     *        thread.
     * @return true if the action happened before the timeout, false otherwise.
     */
    public static boolean waitForAction(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final Runnable runnable,
            @NonNull final CountDownLatch latch, final boolean isMainThreadRunnable) {
        return waitForAction(instrumentation, activity, runnable, latch, WAIT_TIMEOUT_MILLIS,
                isMainThreadRunnable);
    }

    /**
     * Helper method to wait for an action to occur.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run.
     * @param runnable the runnable that will check the condition and signal success via the
     *        {@link CountDownLatch} passed.
     * @param latch the {@link CountDownLatch} to check for success.
     * @param timeoutMillis the timeout duration in milliseconds.
     * @param isMainThreadRunnable Determine whether or not the runnable must be invoked on the main
     *        thread.
     * @return true if the action happened before the timeout, false otherwise.
     */
    public static boolean waitForAction(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final Runnable runnable,
            @NonNull final CountDownLatch latch, final long timeoutMillis,
            final boolean isMainThreadRunnable) {
        final long endTime = SystemClock.elapsedRealtime() + timeoutMillis;
        boolean result = true;

        while (true) {
            if (isMainThreadRunnable) {
                runOnMainSync(instrumentation, activity, runnable);
            } else {
                runnable.run();
                instrumentation.waitForIdleSync();
            }

            if (latch.getCount() == 0) {
                break;
            }

            if (SystemClock.elapsedRealtime() >= endTime) {
                result = false;
                break;
            }

            SystemClock.sleep(WAIT_SLEEP_MILLIS);
        }

        return result;
    }

    /**
     * Runs a runnable on the main thread, but also catches any errors thrown on the main thread and
     * re-throws them on the test thread so they can be displayed more easily.
     *
     * @param instrumentation the {@link Instrumentation} for the test.
     * @param activity the {@link Activity} that this this test is running in.
     * @param runnable the runnable to run.
     */
    public static void runOnMainSync(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final Runnable runnable) {
        if (activity.getMainLooper().equals(Looper.myLooper())) {
            runnable.run();
        } else {
            final FutureAssertionError futureError = new FutureAssertionError();
            instrumentation.runOnMainSync(new Runnable() {

                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (final AssertionError e) {
                        futureError.setAssertionError(e);
                    }
                }
            });
            futureError.throwPendingAssertionError();

            instrumentation.waitForIdleSync();
        }
    }

    /**
     * This class is used to capture {@link AssertionError}s thrown inside anonymous
     * {@link Runnable}s so they can be re-thrown on the {@link Instrumentation} test thread.
     */
    /* package */ static class FutureAssertionError {

        /**
         * The {@link AssertionError} to be thrown by {@link #throwPendingAssertionError()}.
         */
        @Nullable
        private AssertionError mError;

        /**
         * Set the {@link AssertionError} to be thrown by {@link #throwPendingAssertionError()}.
         *
         * @param error The {@link AssertionError} to be thrown by
         *        {@link #throwPendingAssertionError()}.
         */
        /* package */ void setAssertionError(@NonNull final AssertionError error) {
            mError = error;
        }

        /**
         * If an {@link AssertionError} was set via {@link #setAssertionError(AssertionError)} this
         * method will throw that {@link AssertionError}, otherwise it does nothing.
         */
        /* package */ void throwPendingAssertionError() {
            if (null != mError) {
                throw mError;
            }
        }
    }

    /**
     * Private constructor prevents instantiation.
     */
    private TestThreadingUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
