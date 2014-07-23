/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
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
