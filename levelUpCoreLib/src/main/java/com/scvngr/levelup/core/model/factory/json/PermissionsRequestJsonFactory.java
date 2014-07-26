package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.model.PermissionsRequest;
import com.scvngr.levelup.core.model.PermissionsRequest.State;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.jcip.annotations.NotThreadSafe;

import java.io.IOException;

/**
 * Factory for loading a {@link PermissionsRequest} from JSON.
 */
@LevelUpApi(contract = Contract.DRAFT)
@NotThreadSafe
public final class PermissionsRequestJsonFactory extends GsonModelFactory<PermissionsRequest> {

    /**
     * The JSON model root.
     */
    @NonNull
    public static final String MODEL_ROOT = "permissions_request";

    /**
     * Creates a new {@link PermissionsRequest} factory.
     */
    public PermissionsRequestJsonFactory() {
        super(MODEL_ROOT, PermissionsRequest.class, true);
    }

    @Override
    protected void onBuildFactory(@NonNull final GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(PermissionsRequest.State.class,
                new TypeAdapter<PermissionsRequest.State>() {

                    @Override
                    public State read(final JsonReader reader) throws IOException {
                        return State.forString(NullUtils.nonNullContract(reader.nextString()));
                    }

                    @Override
                    public void write(final JsonWriter writer, final State value)
                            throws IOException {
                        writer.value(value.toString());
                    }
                });
    }
}
