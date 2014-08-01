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

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.CategoryJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.ParcelTestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.Category}.
 */
public final class CategoryTest extends SupportAndroidTestCase {

    /**
     * Tests that {@link com.scvngr.levelup.core.model.Category} implements {@link android.os.Parcelable}.
     */
    @SmallTest
    public void testParceling() {
        final Category category = CategoryFixture.getFullModel(5);
        ParcelTestUtils.assertParcelableRoundtrips(category);
    }

    /**
     * Test differences across variations based on all JSON keys.
     *
     * @throws org.json.JSONException never
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(CategoryJsonFactory.JsonKeys.class,
                new CategoryJsonFactory(), CategoryFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" });
    }
}
