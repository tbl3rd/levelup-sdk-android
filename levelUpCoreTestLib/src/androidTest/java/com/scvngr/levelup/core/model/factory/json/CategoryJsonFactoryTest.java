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

import com.scvngr.levelup.core.model.Category;
import com.scvngr.levelup.core.model.CategoryFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.CategoryJsonFactory}.
 */
public final class CategoryJsonFactoryTest extends SupportAndroidTestCase {

    private static final int ID_FIXTURE = 5;

    /**
     * Tests basic JSON parsing.
     *
     * @throws org.json.JSONException on parse errors.
     */
    public void testJsonParse_basic() throws JSONException {
        final Category category =
                new CategoryJsonFactory().from(CategoryFixture.getFullJsonObject(ID_FIXTURE));
        assertEquals(ID_FIXTURE, category.getId());
        assertEquals("category name", category.getName());
    }
}
