package com.scvngr.levelup.deeplinkauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p>Deep Link Authorization allows third party apps to request an access token for a LevelUp user
 * with {@link Intent}s. The intent launches the LevelUp app and the user will be presented with
 * a dialog describing the request for permissions. Upon choosing to accept or reject the request,
 * the user will return to the requesting app which will be granted an access token if the request
 * is accepted.</p>
 *
 * <p>A request includes a list of permissions, such as the ability to create orders for the user
 * or to access their transaction history. {@link DeepLinkAuthUtil.Permissions} has definitions for
 * some of <a href="http://developer.thelevelup.com/getting-started/permissions-list/">the full
 * list of available permissions</a>.</p>
 *
 * <p>To make an authorization request, first hook up the response handler. In your Activity's
 * {@code onActivityResult()}, call {@link #parseActivityResult}:</p>
 *
 * <pre>
 * {@code
 *     @Override
 *     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 *         // This pulls out your result from the data Intent.
 *         PermissionsRequestResult result = LevelUpDeepLinkIntegrator.parseActivityResult(requestCode,
 * resultCode, data);
 *
 *         if (result != null) {
 *             if (result.isSuccessful()) {
 *                 // Result will contain your access token.
 *             } else {
 *                 // The user declined the request or there was as error.
 *             }
 *         } else {
 *             // You can handle your own startActivityForResult results here.
 *         }
 *     }
 * }
 *     </pre>
 *
 * <p>When you are ready to request permissions, create an instance of {@link
 * LevelUpDeepLinkIntegrator} from your Activity and call {@link #requestPermissions} with a list
 * of desired permissions:</p>
 *
 * <pre>
 * {@code
 *     if (LevelUpDeepLinkIntegrator.isLevelUpInstalled()) {
 *         LevelUpDeepLinkIntegrator integrator = new LevelUpDeepLinkIntegrator(yourActivity,
 * yourAppId);
 *         integrator.requestPermissions(DeepLinkAuthUtil.Permissions.PERMISSION_CREATE_ORDERS);
 *     } else {
 *         // If you get here, LevelUp isn't installed or the installed version doesn't support
 *         // deep link auth. This might be a good place to suggest someone download LevelUp!
 *     }
 * }
 * </pre>
 */
@SuppressWarnings("unused")
public final class LevelUpDeepLinkIntegrator {
    /**
     * The requestCode used to start the permissions request activity.
     */
    private static final int REQUEST_CODE = 9635; // LEVELUP on phone keypad, truncated to 16 bits.

    /**
     * @param context application context.
     * @return true if a version of LevelUp that supports deep link authorization is installed and
     * available. If false, you can handle the situation however you prefer, however we recommend
     * that your app launches an intent that opens up LevelUp in Google Play so your users can
     * download it.
     */
    public static boolean isLevelUpAvailable(final Context context) {
        return DeepLinkAuthUtil.isLevelUpAvailable(context);
    }

    /**
     * Call this from {@link android.app.Activity#onActivityResult}.
     *
     * @param requestCode the requestCode from onActivityResult
     * @param resultCode the resultCode from onActivityResult
     * @param data the data from onActivityResult
     * @return the interpreted result. If the request code doesn't match, this returns null.
     */
    public static PermissionsRequestResult parseActivityResult(final int requestCode,
            final int resultCode, final Intent data) {
        if (REQUEST_CODE != requestCode) {
            return null;
        }

        String accessToken = null;
        Uri requestUri = null;

        if (null != data) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // user accepted request
                    accessToken = data.getStringExtra(DeepLinkAuthUtil.EXTRA_STRING_ACCESS_TOKEN);
                    requestUri = data.getData();
                    break;
                case Activity.RESULT_CANCELED:
                    // errorMessage = data.getStringExtra(name)
                    // user rejected request
                    break;
                default:
                    // do nothing
            }
        }

        return new PermissionsRequestResult(accessToken, requestUri, resultCode);
    }

    private final WeakReference<Activity> mActivityWeakReference;
    private final int mAppId;
    private String[] mSignatureOverride;

    /**
     * This can be instantiated in {@link android.app.Activity#onCreate}. This class keeps a weak
     * reference to your activity, so you don't need to worry about it being leaked.
     *
     * @param activity your activity.
     * @param appId your app's web service ID.
     */
    public LevelUpDeepLinkIntegrator(final Activity activity, final int appId) {
        mActivityWeakReference = new WeakReference<Activity>(activity);
        mAppId = appId;
    }

    /**
     * Send a request to the LevelUp app to prompt the user to grant your app the requested
     * permissions. You must make sure to call {@link #parseActivityResult} from {@link
     * android.app.Activity#onActivityResult} in order to get the response from LevelUp.
     *
     * @param permissionKeys the set of permissions that you wish to request. See {@link
     * DeepLinkAuthUtil.Permissions}.
     * @return true if the activity was successfully started. False if a version of LevelUp that
     * supports deep link auth isn't installed or there was a validation issue making the request.
     */
    public boolean requestPermissions(final Collection<String> permissionKeys) {
        boolean success = false;
        final Activity activity = mActivityWeakReference.get();

        if (null != activity) {
            try {
                final Intent startIntent;
                if (mSignatureOverride != null) {
                    startIntent = DeepLinkAuthUtil
                            .getRequestPermissionsIntent(activity, mAppId, permissionKeys,
                                    mSignatureOverride);
                } else {
                    startIntent = DeepLinkAuthUtil
                            .getRequestPermissionsIntent(activity, mAppId, permissionKeys);
                }
                activity.startActivityForResult(startIntent, REQUEST_CODE);
                success = true;
            } catch (final DeepLinkAuthUtil.LevelUpNotInstalledException e) {
                Toast.makeText(activity,
                        "Sorry, it appears that you don't have a compatible version of LevelUp installed.",
                        Toast.LENGTH_LONG).show();
            }
        }

        return success;
    }

    /**
     * Send a request to the LevelUp app to prompt the user to grant your app the requested
     * permissions. You must make sure to call {@link #parseActivityResult} from {@link
     * android.app.Activity#onActivityResult} in order to get the response from LevelUp.
     *
     * @param permissionKeys the set of permissions that you wish to request. See {@link
     * DeepLinkAuthUtil.Permissions}.
     * @return true if the activity was successfully started. False if a version of LevelUp that
     * supports deep link auth isn't installed or there was a validation issue making the request.
     */
    public boolean requestPermissions(final String... permissionKeys) {
        return requestPermissions(Arrays.asList(permissionKeys));
    }

    /**
     * Allows overriding of the allowable signatures for testing.
     *
     * @param signatureOverride the allowed signatures.
     */
    /* package */void setLevelUpAppSignatures(final String[] signatureOverride) {
        mSignatureOverride = signatureOverride;
    }

    /**
     * The result of a call to {@link #requestPermissions}.
     */
    public static final class PermissionsRequestResult {
        private final String mAccessToken;
        private final Uri mRequestUri;
        private final int mResultCode;

        /**
         * Private constructor, as only the outer class should be instantiating this.
         */
        private PermissionsRequestResult(final String accessToken, final Uri requestUri,
                final int resultCode) {
            mRequestUri = requestUri;
            mAccessToken = accessToken;
            mResultCode = resultCode;
        }

        /**
         * @return the access token, if the permissions request was successful.
         */
        public String getAccessToken() {
            return mAccessToken;
        }

        /**
         * @return the original URI that was sent in the request. This is an encoding of your
         * request.
         */
        public Uri getRequestUri() {
            return mRequestUri;
        }

        /**
         * @return true if the permissions request was accepted by the user.
         */
        public boolean isSuccessful() {
            return Activity.RESULT_OK == mResultCode;
        }
    }
}
