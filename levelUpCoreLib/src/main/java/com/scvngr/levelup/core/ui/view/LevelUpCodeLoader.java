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
package com.scvngr.levelup.core.ui.view;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.SlowOperation;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.ui.view.LevelUpQrCodeGenerator.LevelUpQrCodeImage;
import com.scvngr.levelup.core.ui.view.PendingImage.LoadCancelable;
import com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded;
import com.scvngr.levelup.core.util.CryptographicHashUtil;
import com.scvngr.levelup.core.util.CryptographicHashUtil.Algorithms;

import java.util.HashMap;

/**
 * Load a LevelUp payment QR code. Extend this to implement the asynchronous loading of QR codes. As
 * this class inherently interacts with multiple threads, thread requirements are noted on the
 * individual methods.
 */
@LevelUpApi(contract = LevelUpApi.Contract.PUBLIC)
public abstract class LevelUpCodeLoader implements LoadCancelable {
    /**
     * A map of load keys to the callbacks that need to be called when the image ready.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* protected */final HashMap<String, OnImageLoaded<LevelUpQrCodeImage>> mLoaderCallbacks =
            new HashMap<String, PendingImage.OnImageLoaded<LevelUpQrCodeImage>>();

    /* protected */final HashMap<String, PendingImage<LevelUpQrCodeImage>> mPendingImages =
            new HashMap<String, PendingImage<LevelUpQrCodeImage>>();

    /**
     * The cache that will store the code once it's been generated. This has a one-to-one mapping
     * with the cached data (it fully represents it) so it will never need to be invalidated.
     */
    @NonNull
    private final LevelUpCodeCache mCodeCache;

    /**
     * The generator/renderer of the QR code itself. This will be called on a background thread.
     */
    @NonNull
    private final LevelUpQrCodeGenerator mQrCodeGenerator;

    /**
     * @param qrCodeGenerator the QR code generator to render the codes.
     * @param codeCache the cache to use for this code loader.
     */
    public LevelUpCodeLoader(@NonNull final LevelUpQrCodeGenerator qrCodeGenerator,
            @NonNull final LevelUpCodeCache codeCache) {
        mQrCodeGenerator = qrCodeGenerator;
        mCodeCache = codeCache;
    }

    @Override
    public final void cancelLoad(@NonNull final String loadKey) {
        unregisterOnImageLoadedCallback(loadKey);
        onCancelLoad(loadKey);
    }

    /**
     * Cancel all loads. To cancel an individual load, call {@link #cancelLoad(String)} or
     * {@link PendingImage#cancelLoad()}. This must be called on the main thread.
     */
    public final void cancelLoads() {
        mLoaderCallbacks.clear();

        onCancelLoads();
    }

    /**
     * Gets a minimally-small bitmap from the cache whose contents encode {@code qrCodeContents}.
     * This returns a {@link PendingImage}, which will either contain the resulting image if the
     * code has been cached or will be loaded eventually and the {@code onImageLoaded} callback will
     * be called. This must be called on the main thread.
     *
     * @param qrCodeContents the data to display to the user. This is the raw string to encode in
     *        the QR code.
     * @param onImageLoaded callback that gets called when the image is loaded. This will always be
     *        called on the UI thread and is called even if this class returns a cached result.
     * @return a minimally-small bitmap representing the given code data.
     */
    @NonNull
    public final PendingImage<LevelUpQrCodeImage> getLevelUpCode(
            @NonNull final String qrCodeContents,
            @Nullable final OnImageLoaded<LevelUpQrCodeImage> onImageLoaded) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new AssertionError("Must be called from the main thread.");
        }

        final String key = getKey(qrCodeContents);

        final PendingImage<LevelUpQrCodeImage> pendingImage =
                new PendingImage<LevelUpQrCodeImage>(this, key);

        final LevelUpQrCodeImage code = mCodeCache.getCode(key);

        if (null != code) {
            pendingImage.setImage(code);

            if (null != onImageLoaded) {
                onImageLoaded.onImageLoaded(key, code);
            }
        } else {
            mPendingImages.put(key, pendingImage);

            startLoadInBackground(qrCodeContents, key, onImageLoaded);
        }

        return pendingImage;
    }

    /**
     * Generate and cache the code image. This is the same as
     * {@link #getLevelUpCode(String, com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded)},
     * but does not return a result to the user. This can be used for pre-caching codes. This must
     * be called on the main thread.
     *
     * @param codeData the data to display to the user.
     */
    public final void loadLevelUpCode(@NonNull final String codeData) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new AssertionError("Must be called from the main thread.");
        }

        startLoadInBackground(codeData, getKey(codeData), null);
    }

    /**
     * Dispatches calls to a previously registered {@link OnImageLoaded}. The {@link OnImageLoaded}
     * will be unregistered. This must be called after loading a QR code in the background. This
     * must be called on the main thread.
     *
     * @param key the key under which the QR code is tracked.
     * @param image the image of the QR code.
     * @return true if the callback was called.
     */
    protected final boolean dispatchOnImageLoaded(@NonNull final String key,
            @NonNull final LevelUpQrCodeImage image) {
        final OnImageLoaded<LevelUpQrCodeImage> imageLoaded = mLoaderCallbacks.get(key);

        final PendingImage<LevelUpQrCodeImage> pendingImage = mPendingImages.get(key);

        if (null != pendingImage) {
            if (!pendingImage.isLoaded()) {
                pendingImage.setImage(image);
            }

            mPendingImages.remove(key);
        }

        boolean callbackCalled = false;

        if (null != imageLoaded) {
            imageLoaded.onImageLoaded(key, image);
            unregisterOnImageLoadedCallback(key);
            callbackCalled = true;
        }

        return callbackCalled;
    }

    /**
     * Generates the QR code using the supplied generator and caches the result. This should be
     * called from a worker thread.
     *
     * @param key the key under which this QR code is tracked.
     * @param qrCodeContents the contents of the QR code.
     * @return the generated image.
     */
    @NonNull
    @SlowOperation
    protected final LevelUpQrCodeImage generateQrCode(@NonNull final String key,
            @NonNull final String qrCodeContents) {
        final LevelUpQrCodeImage result = mQrCodeGenerator.generateLevelUpQrCode(qrCodeContents);

        mCodeCache.putCode(key, result);

        return result;
    }

    /**
     * @return the code cache.
     */
    @NonNull
    protected final LevelUpCodeCache getCodeCache() {
        return mCodeCache;
    }

    /**
     * Creates a key that's unique to the given contents for use with all image load tracking.
     *
     * @param qrCodeContents the contents to create a key for.
     * @return a unique key for the given contents.
     */
    @NonNull
    protected final String getKey(@NonNull final String qrCodeContents) {
        return CryptographicHashUtil.getHexHash(qrCodeContents, Algorithms.SHA1);
    }

    /**
     * @return the QR code generator.
     */
    @NonNull
    protected final LevelUpQrCodeGenerator getQrCodeGenerator() {
        return mQrCodeGenerator;
    }

    /**
     * Implement this to handle any load cancellation.
     *
     * @param loadKey the key under which the QR code is tracked.
     */
    protected abstract void onCancelLoad(@NonNull String loadKey);

    /**
     * Cancel all loads. Implement this to stop any pending loads.
     */
    protected abstract void onCancelLoads();

    /**
     * <p>
     * Implement this to start the loading of the given QR code into an image in the background. If
     * an existing request for the same content has been made, the second request will be dropped
     * unless the previous request has been cancelled. This must be called on the main thread.
     * </p>
     * <p>
     * Subclasses shouldn't call this method directly, instead call
     * {@link #startLoadInBackground(String, String,
     *       com.scvngr.levelup.core.ui.view.PendingImage.OnImageLoaded)} )}.
     * </p>
     *
     * @param qrCodeContents the contents to render into a QR code.
     * @param key the key under which the QR code is tracked.
     * @param onImageLoaded called when the image has been loaded.
     */
    protected abstract void onStartLoadInBackground(@NonNull final String qrCodeContents,
            @NonNull String key, @Nullable OnImageLoaded<LevelUpQrCodeImage> onImageLoaded);

    /**
     * Register an {@link OnImageLoaded} callback with the given key. This must be called on the
     * main thread.
     *
     * @param key the key under which the image is tracked.
     * @param onImageLoaded the callback to register.
     */
    protected final void registerOnImageLoadedCallback(@NonNull final String key,
            @NonNull final OnImageLoaded<LevelUpQrCodeImage> onImageLoaded) {
        mLoaderCallbacks.put(key, onImageLoaded);
    }

    /**
     * <p>
     * Start the loading of the given QR code into an image in the background. If an existing
     * request for the same content has been made, the second request will be dropped unless the
     * previous request has been cancelled. This must be called on the main thread.
     * </p>
     *
     * @param qrCodeContents the contents to render into a QR code.
     * @param key the key under which the QR code is tracked.
     * @param onImageLoaded called when the image has been loaded. Existing callbacks registered for
     *        this key will be overwritten unless this parameter is {@code null}.
     */
    protected void startLoadInBackground(@NonNull final String qrCodeContents,
            @NonNull final String key,
            @Nullable final OnImageLoaded<LevelUpQrCodeImage> onImageLoaded) {
        if (null != onImageLoaded) {
            registerOnImageLoadedCallback(key, onImageLoaded);
        }

        onStartLoadInBackground(qrCodeContents, key, onImageLoaded);
    }

    /**
     * Remove the given {@link OnImageLoaded} callbacks. This must be called on the main thread.
     *
     * @param key the key under which the image is tracked.
     */
    protected final void unregisterOnImageLoadedCallback(@NonNull final String key) {
        mLoaderCallbacks.remove(key);
    }
}
