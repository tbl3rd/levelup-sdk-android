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
package com.scvngr.levelup.ui.test;

import net.jcip.annotations.Immutable;

/**
 * Constants used by the tests.
 */
@Immutable
public final class Constants {

    /**
     * Example URL used for tests.
     */
    public static final String URL_EXAMPLE = "http://www.example.com/";

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private Constants() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
