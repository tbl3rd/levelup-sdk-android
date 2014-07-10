/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.deeplinkauth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Log;

import com.scvngr.levelup.core.deeplinkauth.BuildConfig;
import com.scvngr.levelup.core.deeplinkauth.R;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * Utilities to support deep link authorization. This class is not intended to be used directly by
 * 3rd party apps for integration; instead please see {@link LevelUpDeepLinkIntegrator}.
 */
public final class DeepLinkAuthUtil {
    private static final String INTENT_PREFIX = "com.scvngr.levelup.core.";
    /**
     * Intent action for a permissions request.
     */
    public static final String ACTION_REQUEST_PERMISSIONS =
            INTENT_PREFIX + "ACTION_REQUEST_PERMISSIONS";

    /**
     * Intent extra containing the access token.
     */
    public static final String EXTRA_STRING_ACCESS_TOKEN =
            INTENT_PREFIX + "EXTRA_STRING_ACCESS_TOKEN";

    /**
     * The authorization request query parameter containing the web service ID of the app
     * requesting the permissions.
     */
    public static final String URI_QUERY_PARAMETER_APP_ID = "app_id";

    /**
     * The authorization request query parameter containing one of the permissions to be requested.
     * The URI can contain multiple of these parameters in order to encode a request for multiple
     * permissions.
     */
    public static final String URI_QUERY_PARAMETER_PERMISSION = "permission";

    /**
     * A list of production package signatures that we're comfortable sending a permissions
     * request to.
     */
    private static final String[] LEVELUP_SIGNATURES_PRODUCTION = {
            "5F:41:E0:40:8A:54:AA:2F:61:C6:CD:E0:CA:12:12:4D:9E:4A:6B:B5;com.scvngr.levelup.app",
    };

    /**
     * A list of development package signatures that we're comfortable sending a permissions
     * request to.
     */
    private static final String[] LEVELUP_SIGNATURES_DEVELOPMENT = {
            "5F:41:E0:40:8A:54:AA:2F:61:C6:CD:E0:CA:12:12:4D:9E:4A:6B:B5;com.scvngr.levelup.app",
            "A3:87:3F:FE:AC:A7:FB:34:2C:63:6D:90:F6:05:61:8F:B0:6F:9D:C3;com.scvngr.levelup.app.development",
            "A3:87:3F:FE:AC:A7:FB:34:2C:63:6D:90:F6:05:61:8F:B0:6F:9D:C3;com.scvngr.levelup.app",
    };

    /**
     * A list of package signatures that we're comfortable sending a permissions request to.
     */
    private static final String[] LEVELUP_SIGNATURES =
            BuildConfig.DEBUG ? LEVELUP_SIGNATURES_DEVELOPMENT : LEVELUP_SIGNATURES_PRODUCTION;

    /**
     * Log tag.
     */
    private static final String TAG = "DeepLinkAuthUtil";

    /**
     * @param context an Application context.
     * @param request the intent in question.
     * @return the Google Maps-style signature for the package that this intent resolves to or null
     * if one could not be resolved.
     * @see #getPackageSignature(android.content.Context, String)
     */
    public static String getPackageSignature(final Context context, final Intent request) {
        String signature = null;

        final PackageManager pm = context.getPackageManager();

        if (pm == null) {
            throw new IllegalArgumentException("Provided context does not have a package manager");
        }

        final ActivityInfo activityInfo =
                request.resolveActivityInfo(pm, PackageManager.GET_SIGNATURES);

        if (activityInfo != null) {
            final String packageName = activityInfo.packageName;

            if (packageName != null) {
                signature = getPackageSignature(context, packageName);
            }
        }

        return signature;
    }

    /**
     * Given a package, retrieves the Google Maps-style API key signature. This is in the form of
     * {@code SIGNATURE;PACKAGE} where {@code SIGNATURE} is the signature of the package, in upper
     * case hexadecimal characters, grouped in bytes by ":". {@code PACKAGE} is the provided
     * package name.
     *
     * @param context an Application context.
     * @param packageName the name of the package.
     * @return the package's signature.
     * @throws IllegalArgumentException if the provided package has no signatures or is otherwise
     *                                  unusable.
     */
    public static String getPackageSignature(final Context context, final String packageName)
            throws IllegalArgumentException {
        final PackageManager pm = context.getPackageManager();

        if (pm == null) {
            throw new IllegalArgumentException("Provided context does not have a package manager");
        }

        try {
            final PackageInfo packageInfo =
                    pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            final Signature[] signatures = packageInfo.signatures;

            if (signatures == null || signatures.length == 0) {
                throw new IllegalArgumentException("Provided package does not have any signatures");
            }

            final Signature signature = signatures[0];
            final CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            final X509Certificate certificate = (X509Certificate) certificateFactory
                    .generateCertificate(new ByteArrayInputStream(signature.toByteArray()));
            final String hexHash = getHexHash(certificate.getEncoded(), ':').toUpperCase(Locale.US);

            final String signatureString = hexHash + ';' + packageName;

            if (BuildConfig.DEBUG) {
                Log.i(TAG, String.format(Locale.US, "Signature of package %s", signatureString));
            }

            return signatureString;
        } catch (final PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException("Package name not found", e);
        } catch (final CertificateException e) {
            throw new IllegalArgumentException("Error with provided certificate", e);
        }
    }

    /**
     * @param context application context.
     * @return true if a version of LevelUp that supports deep link authorization is installed and
     * available.
     */
    public static boolean isLevelUpAvailable(final Context context) {
        return isAuthenticLevelUp(context, toIntent(context, 0, Arrays.asList("test")),
                LEVELUP_SIGNATURES);
    }

    /**
     * Makes a request for permissions, verifying that the target of the Intent is authentically
     * LevelUp.
     *
     * @param context application context.
     * @param appId the web service ID of the app requesting the permissions.
     * @param permissions the set of permissions.
     * @return an intent intended to be sent with {@code startActivityForResult()}.
     * @throws LevelUpNotInstalledException if LevelUp is not installed.
     */
    public static Intent getRequestPermissionsIntent(final Context context, final int appId,
            final Collection<String> permissions) throws LevelUpNotInstalledException {
        return getRequestPermissionsIntent(context, appId, permissions, LEVELUP_SIGNATURES);
    }

    /**
     * Makes a request for permissions, verifying that the target of the Intent is authentically
     * LevelUp.
     *
     * @param context application context.
     * @param appId the web service ID of the app requesting the permissions.
     * @param permissions the set of permissions.
     * @param authenticLevelUpSignatures the list of signatures that are allowed to handle the
     * intent.
     * @return an intent intended to be sent with {@code startActivityForResult()}.
     * @throws LevelUpNotInstalledException if LevelUp is not installed.
     */
    /* package */
    static Intent getRequestPermissionsIntent(final Context context, final int appId,
            final Collection<String> permissions, final String[] authenticLevelUpSignatures)
            throws LevelUpNotInstalledException {
        final Intent request = toIntent(context, appId, permissions);

        if (!isAuthenticLevelUp(context, request, authenticLevelUpSignatures)) {
            throw new LevelUpNotInstalledException();
        }

        return request;
    }

    /**
     * @param context application context.
     * @param appId the web service ID of the app requesting the permissions.
     * @param permissions the set of permissions.
     * @return an Intent that will load a permissions request.
     */
    /* package */
    static Intent toIntent(final Context context, final int appId,
            final Collection<String> permissions) {

        final Uri.Builder builder = new Uri.Builder();
        builder.scheme(context.getText(R.string.levelup_app_url_scheme).toString());
        builder.authority(context.getText(R.string.levelup_app_url_host_authorization).toString());

        for (final String permission : permissions) {
            builder.appendQueryParameter(URI_QUERY_PARAMETER_PERMISSION, permission);
        }

        builder.appendQueryParameter(URI_QUERY_PARAMETER_APP_ID, String.valueOf(appId));

        return new Intent(ACTION_REQUEST_PERMISSIONS, builder.build());
    }

    /**
     * @param data the input data.
     * @param byteDelimiter a character to separate bytes or null if none is desired.
     * @return the data as a hex string, optionally delimited by the byteDelimiter.
     */
    /* package */
    static String convertBytesToHex(final byte[] data, final Character byteDelimiter) {
        final StringBuilder sb = new StringBuilder();
        boolean needsSeparator = false;

        for (final byte datum : data) {
            if (needsSeparator && byteDelimiter != null) {
                sb.append(byteDelimiter);
            }

            // get the unsigned portion
            final int v = datum & 0xFF;

            if (v < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
            needsSeparator = true;
        }

        return sb.toString();
    }

    /**
     * @param context an Application context.
     * @param request the intent in question.
     * @param authenticSignatures a list of signatures that match builds of LevelUp.
     * @return returns true if the intent will resolve to one of the known LevelUp applications in
     * {@link #LEVELUP_SIGNATURES}.
     */
    /* package */
    static boolean isAuthenticLevelUp(final Context context, final Intent request,
            final String[] authenticSignatures) {

        final String candidate = getPackageSignature(context, request);

        return Arrays.asList(authenticSignatures).contains(candidate);
    }

    /**
     * @param data the data to hash
     * @param byteDelimiter an optional delimiter between bytes.
     * @return a sha1 digest of the data, as a string with bytes delimited by byteDelimiter
     */
    private static String getHexHash(final byte[] data, final Character byteDelimiter) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA1").digest(data);

            return convertBytesToHex(hash, byteDelimiter);
        } catch (final NoSuchAlgorithmException e) {
            // this should never occur
            throw new RuntimeException(e);
        }
    }

    /**
     * Thrown if LevelUp is not installed.
     */
    public static final class LevelUpNotInstalledException extends Exception {
        private static final long serialVersionUID = 9096442582651996712L;

        public LevelUpNotInstalledException() {
            super("The package trying to handle a LevelUp permissions request isn't a known LevelUp app");
        }
    }

    /**
     * This is a utility class and cannot be instantiated.
     */
    private DeepLinkAuthUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
