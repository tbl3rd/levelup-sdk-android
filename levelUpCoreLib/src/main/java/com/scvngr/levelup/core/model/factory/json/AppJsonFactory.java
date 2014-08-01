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

package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.App;

import net.jcip.annotations.NotThreadSafe;

/**
 * JSON factory to inflate an {@link App}.
 */
@LevelUpApi(contract = Contract.DRAFT)
@NotThreadSafe
public final class AppJsonFactory extends GsonModelFactory<App> {
    /**
     * The model root.
     */
    @NonNull
    public static final String MODEL_ROOT = "app";

    /**
     * Factory constructor.
     */
    public AppJsonFactory() {
        super(MODEL_ROOT, App.class, true);
    }
}
