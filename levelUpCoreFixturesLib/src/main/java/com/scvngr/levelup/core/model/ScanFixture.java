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
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

import net.jcip.annotations.ThreadSafe;

/**
 * Fixture for {@link Scan}s.
 */
@ThreadSafe
public final class ScanFixture {

    /**
     * @return a fully populated {@link Scan}.
     */
    @NonNull
    public static Scan getFullModel() {
        return new Scan("data");
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ScanFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
