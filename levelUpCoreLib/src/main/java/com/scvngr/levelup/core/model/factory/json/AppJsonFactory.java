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
