/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
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
