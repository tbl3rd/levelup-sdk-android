package com.scvngr.levelup.deeplinkauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import com.scvngr.levelup.core.test.SupportActivityInstrumentationTestCase;
import com.scvngr.levelup.ui.activity.TestFragmentActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests {@link LevelUpDeepLinkIntegrator}.
 */
public final class LevelUpDeepLinkAuthIntegratorTest extends
        SupportActivityInstrumentationTestCase<TestFragmentActivity> {
    private static final int APP_ID_FIXTURE = 5;
    private static final String ACCESS_TOKEN_FIXTURE = "test_access_token";
    private static final List<String> PERMISSIONS_FIXTURE = Collections.unmodifiableList(Arrays
            .asList("test_permission", "test_permission_ii"));

    public LevelUpDeepLinkAuthIntegratorTest() {
        super(TestFragmentActivity.class);
    }

    /**
     * Tests that the integrator creates the correct launch intent.
     *
     * @throws Throwable
     */
    @MediumTest
    public void testRequestPermissions() throws Throwable {
        final TestFragmentActivity activity = getActivity();
        final LevelUpDeepLinkIntegrator integrator =
                new LevelUpDeepLinkIntegrator(activity, APP_ID_FIXTURE);
        integrator
                .setLevelUpAppSignatures(new String[] { DeepLinkAuthUtilTest.APP_SIGNATURE_THIS });

        // This intent filter needs to match the one defined in the AndroidManifest
        final IntentFilter permissionsRequestFilter =
                new IntentFilter(DeepLinkAuthUtil.ACTION_REQUEST_PERMISSIONS);
        permissionsRequestFilter.addCategory(Intent.CATEGORY_DEFAULT);
        permissionsRequestFilter.addDataScheme("lutest");
        permissionsRequestFilter.addDataAuthority("authorization", null);

        final Intent expectedResultIntent =
                DeepLinkAuthUtil.toIntent(activity, APP_ID_FIXTURE, PERMISSIONS_FIXTURE);
        expectedResultIntent.putExtra(DeepLinkAuthUtil.EXTRA_STRING_ACCESS_TOKEN,
                ACCESS_TOKEN_FIXTURE);

        final ActivityResult expectedResult =
                new ActivityResult(Activity.RESULT_OK, expectedResultIntent);
        final ActivityMonitor monitor =
                getInstrumentation().addMonitor(permissionsRequestFilter, expectedResult, true);

        final AtomicReference<AlertDialog> ad = new AtomicReference<AlertDialog>();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ad.set(integrator.requestPermissions(PERMISSIONS_FIXTURE));
            }
        });

        assertNull(ad.get());

        final ActivityResult actualResult = monitor.getResult();
        assertTrue(getInstrumentation().checkMonitorHit(monitor, 1));
        assertEquals(expectedResult.getResultCode(), actualResult.getResultCode());
        assertEquals(expectedResultIntent.getAction(), actualResult.getResultData().getAction());
    }

    @MediumTest
    public void testRequestPermissions_notInstalled_install() throws Throwable {
        final TestFragmentActivity activity = getActivity();
        final LevelUpDeepLinkIntegrator integrator =
                new LevelUpDeepLinkIntegrator(activity, APP_ID_FIXTURE);

        integrator.setLevelUpAppSignatures(new String[] { /* intentionally empty */});

        final CountDownLatch dismissLatch = new CountDownLatch(1);

        integrator.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                dismissLatch.countDown();
            }
        });

        final IntentFilter installIntentFilter = new IntentFilter(Intent.ACTION_VIEW);
        installIntentFilter.addDataScheme("market");
        installIntentFilter.addDataAuthority("details", null);
        installIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        getInstrumentation().addMonitor(installIntentFilter, null, true);

        final AtomicReference<AlertDialog> ad = new AtomicReference<AlertDialog>();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ad.set(integrator.requestPermissions(PERMISSIONS_FIXTURE));
            }
        });

        assertNotNull(ad.get());
        assertNull(integrator.getLastStartedIntent());

        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Button positive = ad.get().getButton(DialogInterface.BUTTON_POSITIVE);

                assertTrue(positive.performClick());
            }
        });

        assertTrue(dismissLatch.await(4, TimeUnit.SECONDS));

        final Intent startedIntent = integrator.getLastStartedIntent();
        assertNotNull(startedIntent);
        final Uri startedIntentData = startedIntent.getData();

        assertNotNull(startedIntentData);
        assertEquals(startedIntentData.getQueryParameter("id"), "com.scvngr.levelup.app");
    }

    @MediumTest
    public void testRequestPermissions_notInstalled_cancel() throws Throwable {
        final TestFragmentActivity activity = getActivity();
        final LevelUpDeepLinkIntegrator integrator =
                new LevelUpDeepLinkIntegrator(activity, APP_ID_FIXTURE);
        integrator.setLevelUpAppSignatures(new String[] { /* intentionally empty */});

        final CountDownLatch dismissLatch = new CountDownLatch(1);

        integrator.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                dismissLatch.countDown();
            }
        });

        final AtomicReference<AlertDialog> ad = new AtomicReference<AlertDialog>();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ad.set(integrator.requestPermissions(PERMISSIONS_FIXTURE));
            }
        });

        assertNotNull(ad.get());
        final IntentFilter installIntentFilter = new IntentFilter(Intent.ACTION_VIEW);
        installIntentFilter.addDataScheme("market");
        installIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        final ActivityMonitor monitor =
                getInstrumentation().addMonitor(installIntentFilter, null, true);
        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Button negative = ad.get().getButton(DialogInterface.BUTTON_NEGATIVE);

                assertTrue(negative.performClick());
            }
        });

        assertTrue(dismissLatch.await(4, TimeUnit.SECONDS));

        // Double-negative here, as getHits() isn't threadsafe and there's no checkMonitorNotHit()
        assertFalse(getInstrumentation().checkMonitorHit(monitor, 1));
    }
}
