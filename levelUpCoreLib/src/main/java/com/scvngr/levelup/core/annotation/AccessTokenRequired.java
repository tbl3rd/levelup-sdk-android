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
package com.scvngr.levelup.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a LevelUp web request that requires an {@link com.scvngr.levelup.core.model.AccessToken}.
 * Callers should ensure that they pass a non-null
 * {@link com.scvngr.levelup.core.net.AccessTokenRetriever} to the Request factory that is creating
 * the {@link com.scvngr.levelup.core.net.AbstractRequest}.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface AccessTokenRequired {
    // This space intentionally left blank.
}
