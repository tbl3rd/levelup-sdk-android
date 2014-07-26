/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.ui.view;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;
import com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded;
import com.scvngr.levelup.core.util.EnvironmentUtil;
import com.scvngr.levelup.core.util.LogManager;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Loads and caches {@link LevelUpQrCodeImage}s using {@link AsyncTask}s.
 */
public final class AsyncTaskCodeLoader extends LevelUpCodeLoader {
    /**
     * A map of the load keys to the corresponding load from the AsyncTask. This is used to allow
     * for canceling pending tasks. This must only be modified on the UI thread.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */final HashMap<String, AsyncTask<Void, Void, LevelUpQrCodeImage>> mAsyncTasks =
            new HashMap<String, AsyncTask<Void, Void, LevelUpQrCodeImage>>();

    @Nullable
    private ExecutorService mExecutor;

    /**
     * @param qrCodeGenerator the {@link LevelUpQrCodeGenerator} used in this loader.
     * @param codeCache the {@link LevelUpCodeCache} to store the generated QR codes.
     */
    public AsyncTaskCodeLoader(@NonNull final LevelUpQrCodeGenerator qrCodeGenerator,
            @NonNull final LevelUpCodeCache codeCache) {
        super(qrCodeGenerator, codeCache);
    }

    @Override
    public void onCancelLoads() {
        for (final AsyncTask<Void, Void, LevelUpQrCodeImage> asyncTask : mAsyncTasks.values()) {
            asyncTask.cancel(true);
        }

        mAsyncTasks.clear();
    }

    @Override
    protected void onCancelLoad(@NonNull final String loadKey) {
        final AsyncTask<Void, Void, LevelUpQrCodeImage> asyncTask = mAsyncTasks.get(loadKey);

        if (null != asyncTask) {
            if (!asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }

            mAsyncTasks.remove(loadKey);
        }
    }

    @Override
    protected void onStartLoadInBackground(@NonNull final String qrCodeContents,
            @NonNull final String key,
            @Nullable final OnImageLoaded<LevelUpQrCodeImage> onImageLoaded) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new AssertionError("Must be called from the main thread.");
        }

        final AsyncTask<Void, Void, LevelUpQrCodeImage> existingLoad = mAsyncTasks.get(key);

        if (null == existingLoad || existingLoad.isCancelled()) {
            final CodeLoader codeLoader = new CodeLoader(key, qrCodeContents);
            mAsyncTasks.put(key, codeLoader);

            if (EnvironmentUtil.isSdk11OrGreater()) {
                executeOnThreadExecutor(codeLoader);
            } else {
                codeLoader.execute();
            }
        } else {
            LogManager.v("Dropping request for duplicate load of key %s.", key);
        }
    }

    /**
     * Execute the task on a thread pool executor. One will be created if needed.
     *
     * @param task the task to execute on a thread pool executor.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void executeOnThreadExecutor(
            @NonNull final AsyncTask<Void, Void, LevelUpQrCodeImage> task) {
        if (null == mExecutor) {
            mExecutor = Executors.newCachedThreadPool();
        }

        task.executeOnExecutor(mExecutor);
    }

    /**
     * An AsyncTask loader to render a QR code to an image.
     */
    private class CodeLoader extends AsyncTask<Void, Void, LevelUpQrCodeImage> {
        @NonNull
        private final String mKey;

        @NonNull
        private final String mQrCodeData;

        /**
         * @param key the loader key associated with this image.
         * @param qrCodeData the data to encode in the image.
         */
        public CodeLoader(@NonNull final String key, @NonNull final String qrCodeData) {
            mKey = key;
            mQrCodeData = qrCodeData;
        }

        @Override
        protected LevelUpQrCodeImage doInBackground(final Void... params) {
            LevelUpQrCodeImage result = null;

            final LevelUpCodeCache cache = getCodeCache();

            if (!isCancelled()) {
                // Check to see if the cache has been updated since the task was scheduled.
                result = cache.getCode(mKey);

                if (null == result) {
                    result = generateQrCode(mKey, mQrCodeData);
                }
            }

            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mAsyncTasks.remove(mKey);
        }

        @Override
        protected void onPostExecute(@Nullable final LevelUpQrCodeImage result) {
            if (null != result) {
                dispatchOnImageLoaded(mKey, result);
            } else {
                LogManager.e("CodeLoader task finished with null result.");
            }

            mAsyncTasks.remove(mKey);
        }
    }
}
