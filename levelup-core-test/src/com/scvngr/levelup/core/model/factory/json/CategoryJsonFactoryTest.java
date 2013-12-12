package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;

import com.scvngr.levelup.core.model.Category;
import com.scvngr.levelup.core.model.CategoryFixture;

import org.json.JSONException;

/**
 * Tests {@link CategoryJsonFactory}.
 */
public final class CategoryJsonFactoryTest extends AndroidTestCase {

    private static final int ID_FIXTURE = 5;

    /**
     * Tests basic JSON parsing.
     *
     * @throws JSONException on parse errors.
     */
    public void testJsonParse_basic() throws JSONException {
        final Category category =
                new CategoryJsonFactory().from(CategoryFixture.getFullJsonObject(ID_FIXTURE));
        assertEquals(ID_FIXTURE, category.getId());
        assertEquals("category name", category.getName()); //$NON-NLS-1$
    }
}
