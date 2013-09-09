package com.scvngr.levelup.core.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.test.AndroidTestCase;
import android.text.format.DateUtils;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * Methods to help with some of the intricacies of threading in the test cases.
 */
public final class TestThreadingUtils {

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
        runOnMainSync(instrumentation, activity, new Runnable() {
            @Override
            public void run() {
                if (!inView) {
                    activity.getSupportFragmentManager().beginTransaction().add(fragment,
                            fragment.getClass().getName()).commit();
                } else {
                    activity.getSupportFragmentManager().beginTransaction().add(
                            android.R.id.content, fragment, fragment.getClass().getName()).commit();
                }

                activity.getSupportFragmentManager().executePendingTransactions();
            }
        });
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
     * Validates that a fragment was added successfully.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run.
     * @param fragmentManager the fragment manager the fragment was added to.
     * @param tag the tag to check for.
     */
    public static void validateFragmentAdded(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final FragmentManager fragmentManager,
            @NonNull final String tag) {
        final CountDownLatch latch = new CountDownLatch(1);
        AndroidTestCase.assertTrue(String.format(Locale.US, "%s added", tag), //$NON-NLS-1$
                waitForAction(instrumentation, activity, new Runnable() {
                    @Override
                    public void run() {
                        final Fragment fragment = fragmentManager.findFragmentByTag(tag);
                        if (null != fragment) {
                            latch.countDown();
                        }
                    }
                }, latch, true));
    }

    /**
     * Validates that a fragment was removed.
     *
     * @param instrumentation the test {@link Instrumentation}.
     * @param activity the activity for the test being run.
     * @param fragmentManager the fragment manager the fragment was removed from.
     * @param tag The tag of the fragment.
     */
    public static void validateFragmentRemoved(@NonNull final Instrumentation instrumentation,
            @NonNull final Activity activity, @NonNull final FragmentManager fragmentManager,
            @NonNull final String tag) {
        final CountDownLatch latch = new CountDownLatch(1);
        AndroidTestCase.assertTrue(String.format(Locale.US, "%s removed", tag), //$NON-NLS-1$
                waitForAction(instrumentation, activity, new Runnable() {
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
        final long endTime = SystemClock.elapsedRealtime() + (4 * DateUtils.SECOND_IN_MILLIS);
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

            SystemClock.sleep(20);
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
        if (activity.getMainLooper() == Looper.myLooper()) {
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
     * Private constructor prevents instantiation.
     */
    private TestThreadingUtils() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }

    /**
     * This class is used to capture {@link AssertionError}s thrown inside anonymous
     * {@link Runnable}s so they can be re-thrown on the {@link Instrumentation} test thread.
     */
    private static class FutureAssertionError {

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
        private void setAssertionError(@NonNull final AssertionError error) {
            mError = error;
        }

        /**
         * If an {@link AssertionError} was set via {@link #setAssertionError(AssertionError)} this
         * method will throw that {@link AssertionError}, otherwise it does nothing.
         */
        private void throwPendingAssertionError() {
            if (null != mError) {
                throw mError;
            }
        }
    }
}
