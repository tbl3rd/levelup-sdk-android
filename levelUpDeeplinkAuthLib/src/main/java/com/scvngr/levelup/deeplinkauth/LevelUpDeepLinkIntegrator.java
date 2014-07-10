package com.scvngr.levelup.deeplinkauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.scvngr.levelup.core.deeplinkauth.R;

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
 * or to access their transaction history. {@link Permissions} has definitions for
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
        if (requestCode != REQUEST_CODE) {
            return null;
        }

        String accessToken = null;
        Uri requestUri = null;

        if (data != null) {
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
    private volatile OnDismissListener mDismissListener;

    /**
     * The last launched intent; for testing purposes.
     */
    private Intent mLastIntent;
    private final OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    onPositiveButton(dialog);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    onNegativeButton(dialog);
                    break;

                default:
                    throw new UnsupportedOperationException("Unhandled button");
            }

        }
    };
    /**
     * The install dialog message.
     */
    private CharSequence mMessage;

    /**
     * Allow the acceptable LevelUp signatures to be overridden, for testing purposes.
     */
    private String[] mSignatureOverride;

    /**
     * The install dialog title.
     */
    private CharSequence mTitle;

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
     * Permissions}.
     * @return the dialog box that was shown to the user with a link to download LevelUp if one was
     *         shown, otherwise null.
     */
    public final AlertDialog requestPermissions(final Collection<String> permissionKeys) {
        AlertDialog installDialog = null;
        final Activity activity = mActivityWeakReference.get();

        if (activity != null) {
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
                startActivityForResult(activity, startIntent, REQUEST_CODE);
            } catch (final DeepLinkAuthUtil.LevelUpNotInstalledException e) {
                installDialog = onLevelUpNotInstalled(activity);
            }
        }

        return installDialog;
    }

    /**
     * Send a request to the LevelUp app to prompt the user to grant your app the requested
     * permissions. You must make sure to call {@link #parseActivityResult} from {@link
     * android.app.Activity#onActivityResult} in order to get the response from LevelUp.
     *
     * @param permissionKeys the set of permissions that you wish to request. See {@link
     * Permissions}.
     * @return the dialog box that was shown to the user with a link to download LevelUp if one was
     *         shown, otherwise null.
     */
    public final AlertDialog requestPermissions(final String... permissionKeys) {
        return requestPermissions(Arrays.asList(permissionKeys));
    }

    /**
     * Sets the message that's displayed when the LevelUp app isn't installed on the device.
     *
     * @param message the body text.
     * @see com.scvngr.levelup.core.deeplinkauth.R.string#levelup_deep_link_auth_install_message
     */
    public final void setInstallDialogMessage(final CharSequence message) {
        mMessage = message;
    }

    /**
     * Sets the title of the dialog box that's displayed when the LevelUp app isn't installed on the device.
     *
     * @param title the dialog box's title.
     * @see com.scvngr.levelup.core.deeplinkauth.R.string#levelup_deep_link_auth_install_title
     */
    public final void setInstallDialogTitle(final CharSequence title) {
        mTitle = title;
    }

    /**
     * Constructs an {@link AlertDialog} which prompts the user to download LevelUp. Alternative
     * text can be set using {@link #setInstallDialogMessage} and {@link #setInstallDialogTitle} or
     * by overlaying the associated strings resources.
     *
     * @param activity the activity that is responsible for the dialog (your activity).
     * @return a created, but not shown dialog with two buttons.
     */
    protected AlertDialog getInstallDialog(final Activity activity) {
        final AlertDialog.Builder ab = new Builder(activity);

        ab.setCancelable(true);

        if (mTitle != null) {
            ab.setTitle(mTitle);
        } else {
            ab.setTitle(R.string.levelup_deep_link_auth_install_title);
        }

        if (mMessage != null) {
            ab.setMessage(mMessage);
        } else {
            ab.setMessage(R.string.levelup_deep_link_auth_install_message);
        }

        ab.setIcon(R.drawable.levelup_ic_levelup);

        ab.setPositiveButton(R.string.levelup_deep_link_auth_install_positive_button, mListener);
        ab.setNegativeButton(R.string.levelup_deep_link_auth_install_negative_button, mListener);

        return ab.create();
    }

    /**
     * Retrieves the Google Play link for the given package name.
     *
     * @param context application context.
     * @param packageName the package to display.
     * @return a URL that can be resolved on an Android device to show the given Google Play details
     *         page.
     */
    protected Uri getPlayLink(final Context context, final String packageName) {
        return Uri.parse("market://details").buildUpon().appendQueryParameter("id", packageName)
                .build();
    }

    /**
     * Called when LevelUp is not installed.
     *
     * @param activity
     * @return the {@link AlertDialog} that was shown to the user.
     */
    protected AlertDialog onLevelUpNotInstalled(final Activity activity) {
        final AlertDialog installDialog = getInstallDialog(activity);
        installDialog.setOnDismissListener(mDismissListener);
        installDialog.show();

        return installDialog;
    }

    /**
     * Called when the negative action button (Cancel) is pressed on the install dialog.
     *
     * @param dialog
     */
    protected void onNegativeButton(final DialogInterface dialog) {
        dialog.cancel();
    }

    /**
     * Called when the positive action button (install LevelUp) is pressed in the install dialog.
     *
     * @param dialog
     */
    protected void onPositiveButton(final DialogInterface dialog) {
        Activity activity = mActivityWeakReference.get();

        if (activity == null) {
            final AlertDialog d2 = (AlertDialog) dialog;
            activity = d2.getOwnerActivity();
        }

        if (activity != null) {
            final Uri playLink =
                    getPlayLink(activity,
                            activity.getString(R.string.levelup_deep_link_auth_install_package));

            startActivity(activity, new Intent(Intent.ACTION_VIEW, playLink));
        } else {
            Log.d("LevelUpDeepLinkIntegrator", "Lost reference to Activity");
        }

        dialog.dismiss();
    }

    /**
     * This is intended for testing only.
     *
     * @return the last intent that this class started with either startActivity or
     *         startActivityForResult.
     */
    /* package */Intent getLastStartedIntent() {
        return mLastIntent;
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
     * Allows adding an on dismiss listener to the install prompt dialog, for testing.
     *
     * @param dismissListener
     */
    /* package */void setOnDismissListener(final OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    /**
     * Broken out to capture the intent for testing.
     *
     * @param activity
     * @param intent
     * @see Activity#startActivity(Intent)
     */
    private void startActivity(final Activity activity, final Intent intent) {
        mLastIntent = intent;
        activity.startActivity(intent);
    }

    /**
     * Broken out to capture the intent for testing.
     *
     * @param activity
     * @param intent
     * @param requestCode
     * @see Activity#startActivityForResult(Intent, int)
     */
    private void startActivityForResult(final Activity activity, final Intent intent,
            final int requestCode) {
        mLastIntent = intent;
        activity.startActivityForResult(intent, requestCode);
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
            return mResultCode == Activity.RESULT_OK;
        }
    }
}
