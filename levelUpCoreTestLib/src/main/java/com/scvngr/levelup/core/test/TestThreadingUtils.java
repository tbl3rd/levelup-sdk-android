/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.test.AndroidTestCase;
import android.view.View;

import com.scvngr.levelup.core.util.NullUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
                            .add(fragment, tag).commit();
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
        final LatchRunnable latchRunnable = new LatchRunnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    countDown();
                }
            }
        };

        AndroidTestCase.assertTrue(waitForAction(instrumentation, activity, latchRunnable, true));
    }

    /**
     * Validates that a fragment was added successfully. Does not validate the container it was
     * added to.
     *
     * @param <V> the type of Fragment.
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was added to (null will fail
     *        validation).
     * @param tag the tag to check for (null will fail validation).
     * @return the Fragment that is added.
     */
    @NonNull
    public static <V extends Fragment> V validateFragmentAdded(
            @NonNull final Instrumentation instrumentation, final Activity activity,
            final FragmentManager fragmentManager, final String tag) {
        return validateFragmentAdded(instrumentation, activity, fragmentManager, tag,
                PARENT_ID_UNDEFINED);
    }

    /**
     * Validates that a fragment was added successfully and validates the ID of the container it was
     * added to.
     *
     * @param <V> the type of Fragment.
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was added to (null will fail
     *        validation).
     * @param tag the tag to check for (null will fail validation).
     * @param parentId the id of the parent container the fragment is expected to be in or pass
     *        {@link #PARENT_ID_UNDEFINED} if no parent id should be validated.
     * @return the Fragment that is added.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <V extends Fragment> V validateFragmentAdded(
            @NonNull final Instrumentation instrumentation, final Activity activity,
            final FragmentManager fragmentManager, final String tag, final int parentId) {
        AndroidTestCase.assertNotNull(tag);
        AndroidTestCase.assertNotNull(activity);
        AndroidTestCase.assertNotNull(fragmentManager);

        final AtomicReference<Fragment> reference = new AtomicReference<Fragment>();
        final LatchRunnable latchRunnable = new LatchRunnable() {
            @Override
            public void run() {
                final Fragment fragment = fragmentManager.findFragmentByTag(tag);

                if (null != fragment) {
                    if (fragment.isAdded()) {
                        if (PARENT_ID_UNDEFINED != parentId) {
                            final View parent = (View) fragment.getView().getParent();
                            AndroidTestCase.assertEquals("In the proper container",
                                    parentId, parent.getId());
                        }

                        reference.set(fragment);
                        countDown();
                    }
                }
            }
        };

        AndroidTestCase.assertTrue(String.format(Locale.US, "%s added", tag),
                waitForAction(instrumentation, NullUtils.nonNullContract(activity), latchRunnable,
                true));

        return (V) reference.get();
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

        final LatchRunnable latchRunnable = new LatchRunnable() {
            @Override
            public void run() {
                final Fragment fragment = fragmentManager.findFragmentByTag(tag);
                if (null == fragment) {
                    countDown();
                }
            }
        };

        AndroidTestCase.assertTrue(String.format(Locale.US, "%s removed", tag),
                waitForAction(instrumentation, NullUtils.nonNullContract(activity), latchRunnable,
                true));
    }

    /**
     * Validates that a fragment is visible to the user.
     *
     * @param <V> the type of Fragment.
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was added to (null will fail
     *        validation).
     * @param tag the tag to check for (null will fail validation).
     * @return the Fragment that is visible to the user.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <V extends Fragment> V validateFragmentIsVisible(
            @NonNull final Instrumentation instrumentation, final Activity activity,
            final FragmentManager fragmentManager, final String tag) {
        AndroidTestCase.assertNotNull(tag);
        AndroidTestCase.assertNotNull(activity);
        AndroidTestCase.assertNotNull(fragmentManager);

        final AtomicReference<Fragment> reference = new AtomicReference<Fragment>();
        final LatchRunnable latchRunnable = new LatchRunnable() {
            @Override
            public void run() {
                final Fragment fragment = fragmentManager.findFragmentByTag(tag);

                if (null != fragment) {
                    if (fragment.isVisible()) {
                        reference.set(fragment);
                        countDown();
                    }
                }
            }
        };

        AndroidTestCase.assertTrue(String.format(Locale.US, "%s is visible", tag),
                waitForAction(instrumentation, activity, latchRunnable, true));

        return (V) reference.get();
    }

    /**
     * Validates that a fragment is either not managed by the fragment manager or is not visible to
     * the user.
     *
     * @param <V> the type of Fragment.
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run (null will fail validation).
     * @param fragmentManager the fragment manager the fragment was added to (null will fail
     *        validation).
     * @param tag the tag to check for (null will fail validation).
     * @return the Fragment that is not visible to the user.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <V extends Fragment> V validateFragmentIsNotVisible(
            @NonNull final Instrumentation instrumentation, final Activity activity,
            final FragmentManager fragmentManager, final String tag) {
        AndroidTestCase.assertNotNull(tag);
        AndroidTestCase.assertNotNull(activity);
        AndroidTestCase.assertNotNull(fragmentManager);

        final AtomicReference<Fragment> reference = new AtomicReference<Fragment>();
        final LatchRunnable latchRunnable = new LatchRunnable() {
            @Override
            public void run() {
                final Fragment fragment = fragmentManager.findFragmentByTag(tag);

                if (null == fragment || !fragment.isVisible()) {
                    reference.set(fragment);
                    countDown();
                }
            }
        };

        AndroidTestCase.assertTrue(String.format(Locale.US, "%s is not visible", tag),
                waitForAction(instrumentation, activity, latchRunnable, true));

        return (V) reference.get();
    }

    /**
     * Helper method to wait for an action to occur.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run.
     * @param latchRunnable the runnable that will check the condition and signal success via its
     * {@link java.util.concurrent.CountDownLatch}.
     * @param isMainThreadRunnable Determine whether or not the runnable must be invoked on the main
     * thread.
     * @return true if the action happened before the timeout, false otherwise.
     */
    public static boolean waitForAction(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final LatchRunnable latchRunnable,
            final boolean isMainThreadRunnable) {
        return waitForAction(instrumentation, activity, latchRunnable, WAIT_TIMEOUT_MILLIS,
                isMainThreadRunnable);
    }

    /**
     * Helper method to wait for an action to occur.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run.
     * @param latchRunnable the runnable that will check the condition and signal success via its
     * {@link java.util.concurrent.CountDownLatch}.
     * @param timeoutMillis the timeout duration in milliseconds.
     * @param isMainThreadRunnable Determine whether or not the runnable must be invoked on the main
     * thread.
     * @return true if the action happened before the timeout, false otherwise.
     */
    public static boolean waitForAction(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final LatchRunnable latchRunnable,
            final long timeoutMillis, final boolean isMainThreadRunnable) {
        final long endTime = SystemClock.elapsedRealtime() + timeoutMillis;
        boolean result = true;

        while (true) {
            if (isMainThreadRunnable) {
                runOnMainSync(instrumentation, activity, latchRunnable);
            } else {
                latchRunnable.run();
                instrumentation.waitForIdleSync();
            }

            if (latchRunnable.getCount() == 0) {
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
