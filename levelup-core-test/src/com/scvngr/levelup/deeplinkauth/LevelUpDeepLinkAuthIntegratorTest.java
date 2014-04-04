package com.scvngr.levelup.deeplinkauth;

import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.scvngr.levelup.ui.activity.FragmentTestActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests {@link LevelUpDeepLinkIntegrator}.
 */
public final class LevelUpDeepLinkAuthIntegratorTest
        extends ActivityInstrumentationTestCase2<FragmentTestActivity> {
    private static final int APP_ID_FIXTURE = 5;
    private static final String ACCESS_TOKEN_FIXTURE = "test_access_token";
    private static final List<String> PERMISSIONS_FIXTURE =
            Collections.unmodifiableList(Arrays.asList("test_permission", "test_permission_ii"));

    public LevelUpDeepLinkAuthIntegratorTest() {
        super(FragmentTestActivity.class);
    }

    /**
     * Tests that the integrator creates the correct launch intent.
     */
    @MediumTest
    public void testRequestPermissions() {
        final FragmentTestActivity activity = getActivity();
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
        expectedResultIntent
                .putExtra(DeepLinkAuthUtil.EXTRA_STRING_ACCESS_TOKEN, ACCESS_TOKEN_FIXTURE);

        final ActivityResult expectedResult =
                new ActivityResult(Activity.RESULT_OK, expectedResultIntent);
        final ActivityMonitor monitor =
                getInstrumentation().addMonitor(permissionsRequestFilter, expectedResult, true);

        assertTrue("did not successfully launch activity",
                integrator.requestPermissions(PERMISSIONS_FIXTURE));
        assertNotNull(activity);

        assertEquals(1, monitor.getHits());
        final ActivityResult actualResult = monitor.getResult();
        assertEquals(expectedResult.getResultCode(), actualResult.getResultCode());
        assertEquals(expectedResultIntent.getAction(), actualResult.getResultData().getAction());
    }
}
