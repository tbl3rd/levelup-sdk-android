/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.build;

import android.app.Application;

import com.scvngr.levelup.core.util.LogManager;

/**
 * Application subclass for the core lib build application.
 */
public final class CoreLibBuildApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogManager.init(getApplicationContext());
    }
}
