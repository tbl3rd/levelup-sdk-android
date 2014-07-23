/*
 * This is a copy of the LoaderTestCase from android. It was modified to use the support library
 * instead of the default implementation. Modifications Copyright 2013-2014 SCVNGR, Inc., D.B.A.
 * LevelUp. All rights reserved.
 *
 * CHECKSTYLE:OFF Original license:
 * Copyright (C) 2010 The Android Open Source
 * Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License
 */
package com.scvngr.levelup.core.test;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * A convenience class for testing {@link Loader}s. This test case provides a simple way to
 * synchronously get the result from a Loader making it easy to assert that the Loader returns the
 * expected result.
 */
public class SupportLoaderTestCase extends SupportAndroidTestCase {

    static {
        // Force class loading of AsyncTask on the main thread so that its handlers are tied to
        // the main thread and responses from the worker thread get delivered on the main thread.
        // The tests are run on another thread, allowing them to block waiting on a response from
        // the code running on the main thread. The main thread can't block since the AysncTask
        // results come in via the event loop.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... args) {
                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                // Do nothing.
            }
        };
    }

    /**
     * Runs a Loader synchronously and returns the result of the load. The loader will be started,
     * stopped, and destroyed by this method so it cannot be reused.
     *
     * @param loader The loader to run synchronously
     * @return The result from the loader
     */
    public <T> T getLoaderResultSynchronously(final Loader<T> loader) {
        // The test thread blocks on this queue until the loader puts its result in
        final ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<T>(1);
        final CountDownLatch latch = new CountDownLatch(1);

        // This callback runs on the "main" thread and unblocks the test thread
        // when it puts the result into the blocking queue
        final OnLoadCompleteListener<T> listener = new OnLoadCompleteListener<T>() {
            @Override
            public void onLoadComplete(final Loader<T> completedLoader, final T data) {
                // Shut the loader down
                completedLoader.unregisterListener(this);
                completedLoader.stopLoading();
                completedLoader.reset();

                // Store the result, unblocking the test thread
                if (null != data) {
                    queue.add(data);
                }
                latch.countDown();
            }
        };

        // This handler runs on the "main" thread of the process since AsyncTask
        // is documented as needing to run on the main thread and many Loaders use
        // AsyncTask
        final Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(final Message msg) {
                loader.registerListener(0, listener);
                loader.startLoading();
            }
        };

        // Ask the main thread to start the loading process
        mainThreadHandler.sendEmptyMessage(0);

        // Block on the queue waiting for the result of the load to be inserted
        T result;
        while (true) {
            try {
                latch.await();
                result = queue.peek();
                break;
            } catch (final InterruptedException e) {
                throw new RuntimeException("waiting thread interrupted", e);
            }
        }

        return result;
    }
}
// CHECKSTYLE:ON
