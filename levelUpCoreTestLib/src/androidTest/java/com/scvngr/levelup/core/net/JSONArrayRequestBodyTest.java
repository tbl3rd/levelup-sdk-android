/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import com.scvngr.levelup.core.annotation.NonNull;

import org.json.JSONArray;

/**
 * Tests {@link com.scvngr.levelup.core.net.JSONArrayRequestBody}.
 */
public final class JSONArrayRequestBodyTest extends AbstractRequestBodyTest<JSONArrayRequestBody> {

    @Override
    @NonNull
    protected JSONArrayRequestBody getFixture() {
        return new JSONArrayRequestBody(getArray());
    }

    @NonNull
    private JSONArray getArray() {
        final JSONArray jo = new JSONArray();
        jo.put("foo"); //$NON-NLS-1$
        jo.put("bar"); //$NON-NLS-1$

        return jo;
    }
}
