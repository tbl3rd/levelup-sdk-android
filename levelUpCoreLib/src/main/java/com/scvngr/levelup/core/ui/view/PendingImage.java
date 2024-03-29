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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * An image that can be loaded later. This contains a reference to an image of an arbitrary type
 * that can be set later using {@link #setImage(Object)}.
 *
 * @param <T> the type of the image.
 */
@ThreadSafe
public final class PendingImage<T> {

    /**
     * The image.
     */
    @Nullable
    @GuardedBy("mImageLock")
    private T mImage;

    /**
     * Lock object for mImage access.
     */
    private final Object[] mImageLock = new Object[0];

    /**
     * The class which is able to cancel the load of this image.
     */
    @NonNull
    private final LoadCancelable mLoader;

    /**
     * The key under which the load request was made.
     */
    @NonNull
    private final String mLoadKey;

    /**
     * @param loadCancelable the class capable of canceling the load of this image. This will be
     *        called by {@link #cancelLoad()}.
     * @param loadKey the key associated with this load. This must be unique for each image.
     */
    public PendingImage(@NonNull final LoadCancelable loadCancelable, @NonNull final String loadKey) {
        mLoader = loadCancelable;
        mLoadKey = loadKey;
    }

    /**
     * Cancels the load of this image.
     */
    public void cancelLoad() {
        mLoader.cancelLoad(mLoadKey);
    }

    /**
     * @return the image or null if it has not been loaded yet.
     */
    @Nullable
    public T getImage() {
        synchronized (mImageLock) {
            return mImage;
        }
    }

    /**
     * @return the key under which the load request was made.
     */
    @NonNull
    public String getLoadKey() {
        return mLoadKey;
    }

    /**
     * @return true if the image has been loaded.
     */
    public boolean isLoaded() {
        synchronized (mImageLock) {
            return null != mImage;
        }
    }

    /**
     * @param image sets the image.
     */
    public void setImage(@NonNull final T image) {
        synchronized (mImageLock) {
            mImage = image;
        }
    }

    /**
     * A class that can cancel a load, given a key.
     */
    public interface LoadCancelable {
        /**
         * Cancel the load associated with the given key.
         *
         * @param loadKey the key.
         */
        void cancelLoad(@NonNull final String loadKey);
    }

    /**
     * Callback to call when an image has been loaded.
     *
     * @param <T2> the type of image to load.
     */
    public interface OnImageLoaded<T2> {
        /**
         * Called on the UI thread when the image has been loaded.
         *
         * @param loadKey the key by which the image was loaded.
         * @param image the loaded image.
         */
        void onImageLoaded(@NonNull final String loadKey, @NonNull final T2 image);
    }
}
