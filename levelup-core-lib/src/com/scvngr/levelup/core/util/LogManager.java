/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Locale;

import net.jcip.annotations.ThreadSafe;

import com.scvngr.levelup.core.BuildConfig;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * This is a utility class to wrap the Android {@link Log} class.
 * <p>
 * Because logging is expensive, it is recommended that clients of this class strip out logging
 * statements at compile time using ProGuard.
 */
@LevelUpApi(contract = Contract.DRAFT)
@ThreadSafe
public final class LogManager {

    /**
     * Format string for log messages.
     * <p>
     * The format is: <Thread> <Class>.<method>(): <message>
     */
    private static final String FORMAT = "%-30s%s.%s(): %s"; //$NON-NLS-1$

    /**
     * Log tag for use with {@link Log}.
     */
    private static volatile String sLogTag = "LevelUp"; //$NON-NLS-1$

    /**
     * Initializes logging.
     * <p>
     * This should be called from {@link android.app.Application#onCreate()}. (It may also need to
     * be called from {@link android.content.ContentProvider#onCreate()} due to when the provider is
     * initialized.)
     *
     * @param context Application context.
     */
    public static void init(@NonNull final Context context) {
        PreconditionUtil.assertNotNull(context, "context"); //$NON-NLS-1$

        // Removing whitespace fixes issues with logcat filters
        sLogTag = BuildUtil.getLabel(context).replace(" ", "");  //$NON-NLS-1$//$NON-NLS-2$

        if (BuildConfig.DEBUG) {
            StrictModeUtil.setStrictMode(true);
            android.support.v4.app.FragmentManager.enableDebugLogging(true);
            android.support.v4.app.LoaderManager.enableDebugLogging(true);

            if (EnvironmentUtil.isSdk11OrGreater()) {
                enableDebugLoggingHoneycomb();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void enableDebugLoggingHoneycomb() {
        android.app.FragmentManager.enableDebugLogging(true);
        android.app.LoaderManager.enableDebugLogging(true);
    }

    /**
     * Log a message.
     *
     * @param msg message to log. This message is expected to be a format string if varargs are
     *            passed in.
     * @param args optional arguments to be formatted into {@code msg}.
     */
    public static void v(@NonNull final String msg, @Nullable final Object... args) {
        logMessage(Log.VERBOSE, msg, args, null);
    }

    /**
     * Log a message.
     *
     * @param msg message to log.
     * @param err an exception that occurred, whose trace will be printed with the log message.
     */
    public static void v(@NonNull final String msg, @NonNull final Throwable err) {
        logMessage(Log.VERBOSE, msg, null, err);
    }

    /**
     * Log a message.
     *
     * @param msg message to log. This message is expected to be a format string if varargs are
     *            passed in.
     * @param args optional arguments to be formatted into {@code msg}.
     */
    public static void d(@NonNull final String msg, @Nullable final Object... args) {
        logMessage(Log.DEBUG, msg, args, null);
    }

    /**
     * Log a message.
     *
     * @param msg message to log.
     * @param err an exception that occurred, whose trace will be printed with the log message.
     */
    public static void d(@NonNull final String msg, @NonNull final Throwable err) {
        logMessage(Log.DEBUG, msg, null, err);
    }

    /**
     * Log a message.
     *
     * @param msg message to log. This message is expected to be a format string if varargs are
     *            passed in.
     * @param args optional arguments to be formatted into {@code msg}.
     */
    public static void i(@NonNull final String msg, @Nullable final Object... args) {
        logMessage(Log.INFO, msg, args, null);
    }

    /**
     * Log a message.
     *
     * @param msg message to log.
     * @param err an exception that occurred, whose trace will be printed with the log message.
     */
    public static void i(@NonNull final String msg, @NonNull final Throwable err) {
        logMessage(Log.INFO, msg, null, err);
    }

    /**
     * Log a message.
     *
     * @param msg message to log. This message is expected to be a format string if varargs are
     *            passed in.
     * @param args optional arguments to be formatted into {@code msg}.
     */
    public static void w(@NonNull final String msg, @Nullable final Object... args) {
        logMessage(Log.WARN, msg, args, null);
    }

    /**
     * Log a message.
     *
     * @param msg message to log.
     * @param err an exception that occurred, whose trace will be printed with the log message.
     */
    public static void w(@NonNull final String msg, @NonNull final Throwable err) {
        logMessage(Log.WARN, msg, null, err);
    }

    /**
     * Log a message.
     *
     * @param msg message to log. This message is expected to be a format string if varargs are
     *            passed in.
     * @param args optional arguments to be formatted into {@code msg}.
     */
    public static void e(@NonNull final String msg, @Nullable final Object... args) {
        logMessage(Log.ERROR, msg, args, null);
    }

    /**
     * Log a message.
     *
     * @param msg message to log.
     * @param err an exception that occurred, whose trace will be printed with the log message.
     */
    public static void e(@NonNull final String msg, @Nullable final Throwable err) {
        logMessage(Log.ERROR, msg, null, err);
    }

    /**
     * Helper for varargs in log messages.
     *
     * @param msg The format string.
     * @param args The format arguments.
     * @return A string formatted with the arguments.
     */
    @NonNull
    private static String formatMessage(final String msg, @Nullable final Object[] args) {
        String output = msg;

        try {
            for (final Object x : args) {
                output = String.format(Locale.US, output, x);
            }
        } catch (final Exception e) {
            output = String.format(Locale.US, msg, args);
        }
        return output;
    }

    private static void logMessage(final int logLevel, @NonNull final String message,
            @Nullable final Object[] messageFormatArgs, @Nullable final Throwable err) {
        final String preppedMessage = formatMessage(message, messageFormatArgs);

        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        final String sourceClass = trace[4].getClassName();
        final String sourceMethod = trace[4].getMethodName();

        final String logcatLogLine =
                String.format(Locale.US, FORMAT, Thread.currentThread().getName(), sourceClass,
                        sourceMethod, preppedMessage);

        switch (logLevel) {
            case Log.VERBOSE: {
                if (null == err) {
                    Log.v(sLogTag, logcatLogLine);
                } else {
                    Log.v(sLogTag, logcatLogLine, err);
                }
                break;
            }
            case Log.DEBUG: {
                if (null == err) {
                    Log.d(sLogTag, logcatLogLine);
                } else {
                    Log.d(sLogTag, logcatLogLine, err);
                }
                break;
            }
            case Log.INFO: {
                if (null == err) {
                    Log.i(sLogTag, logcatLogLine);
                } else {
                    Log.i(sLogTag, logcatLogLine, err);
                }
                break;
            }
            case Log.WARN: {
                if (null == err) {
                    Log.w(sLogTag, logcatLogLine);
                } else {
                    Log.w(sLogTag, logcatLogLine, err);
                }
                break;
            }
            case Log.ERROR: {
                if (null == err) {
                    Log.e(sLogTag, logcatLogLine);
                } else {
                    Log.e(sLogTag, logcatLogLine, err);
                }
                break;
            }
            case Log.ASSERT: {
                if (null == err) {
                    Log.wtf(sLogTag, logcatLogLine);
                } else {
                    Log.wtf(sLogTag, logcatLogLine, err);
                }
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private LogManager() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
