package com.scvngr.levelup.core.model.factory.json;

import android.net.Uri;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import net.jcip.annotations.NotThreadSafe;

import org.json.JSONArray;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.model.NonWrappable;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Loads an LevelUp model from JSON using Gson. When inflating from JSON, this ensures that the
 * model's null annotation contract is enforced. Note: to be null-checked, the model must be in or
 * below the {@code com.scvngr.levelup} package. Objects from all other packages will be ignored.
 *
 * @param <T> the type of object to load.
 */
@NotThreadSafe
@LevelUpApi(contract = Contract.INTERNAL)
public class GsonModelFactory<T> {

    /**
     * The Gson object parser.
     */
    @NonNull
    private final Gson mGson;

    /**
     * The type that this class will return.
     */
    @NonNull
    private final Class<T> mType;

    /**
     * The type key under which the JSON object can be nested under.
     *
     * @see {@link #AbstractJsonModelFactory}
     */
    @NonNull
    private final String mTypeKey;

    /**
     * Constructs a new factory.
     *
     * @param typeKey the key which the object to parse can be nested under. It will usually be the
     *        name of the object's type:
     *
     *        <pre>
     *            { "typeKey": { "field1": "test" } }
     * </pre>
     *
     *        When requesting a single object or a list of objects, the object will be nested under
     *        this type key.
     * @param type the type of object to load
     * @param wrapped if true, the input JSON must be wrapped with a JSON object that has a typeKey,
     *        as mentioned above.
     */
    public GsonModelFactory(@NonNull final String typeKey, @NonNull final Class<T> type,
            final boolean wrapped) {
        mType = type;
        mTypeKey = typeKey;

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(MonetaryValue.class, new MonetaryValueTypeAdapter());
        gsonBuilder.registerTypeAdapter(Uri.class, new UriTypeAdapter());
        gsonBuilder.registerTypeAdapterFactory(new NullCheckedTypeAdapterFactory());

        if (wrapped) {
            gsonBuilder.registerTypeAdapterFactory(new WrappedModelTypeAdapterFactory());
        }

        onBuildFactory(gsonBuilder);

        mGson = NullUtils.nonNullContract(gsonBuilder.create());
    }

    /**
     * Override this to register type adapters or anything else of that sort. The default
     * implementation is empty.
     *
     * @param gsonBuilder the Gson builder that will be used for this class.
     */
    protected void onBuildFactory(@NonNull final GsonBuilder gsonBuilder) {
        // Default implementation does nothing.
    }

    /**
     * Parse a model from the JSON object passed, un-nesting from a root element if necessary.
     *
     * @param jsonObject the JSON representation of the model to parse.
     * @return a model instance.
     */
    @NonNull
    public final T from(@NonNull final JsonObject jsonObject) {
        return createFrom(jsonObject);
    }

    /**
     * Parse a model from the JSON object passed, un-nesting from a root element if necessary.
     *
     * @param json the JSON representation of the model to parse.
     * @return a model instance.
     * @throws JsonParseException if there is a problem decoding the JSON structure.
     */
    @NonNull
    public final T from(@NonNull final String json) throws JsonParseException {
        final JsonParser p = new JsonParser();
        final JsonElement root = p.parse(json);

        if (!root.isJsonObject()) {
            throw new JsonSyntaxException(NullUtils.format(
                    "JSON data must be a JSON object. Type is '%s'.", root.getClass() //$NON-NLS-1$
                            .getSimpleName()));
        }

        return from(NullUtils.nonNullContract(root.getAsJsonObject()));
    }

    /**
     * Parse a list of models from the JSON array passed.
     *
     * @param jsonArray the {@link JSONArray} containing a list of models.
     * @return a {@link List} of model instances.
     * @throws JsonParseException if there is a problem decoding the JSON structure.
     */
    @NonNull
    public final List<T> fromList(@NonNull final JsonArray jsonArray) throws JsonParseException {

        final int count = jsonArray.size();
        final List<T> objectList = new ArrayList<T>(count);

        for (final JsonElement object : jsonArray) {
            if (object.isJsonObject()) {
                objectList.add(from(NullUtils.nonNullContract(object.getAsJsonObject())));
            } else {
                throw new JsonSyntaxException(NullUtils.format(
                        "Element in array was a '%s', not an object.", object.getClass() //$NON-NLS-1$
                                .getSimpleName()));
            }
        }

        return objectList;
    }

    /**
     * Parse a list of models from the JSON array passed.
     *
     * @param json the JSON representation of the list of models to parse.
     * @return a {@link List} of model instances.
     * @throws JsonParseException if there is a problem decoding the JSON structure.
     */
    @NonNull
    public final List<T> fromList(@NonNull final String json) throws JsonParseException {
        final JsonElement root = new JsonParser().parse(json);

        if (!root.isJsonArray()) {
            throw new JsonSyntaxException(NullUtils.format(
                    "JSON data must be a JSON array. Type is '%s'.", root.getClass() //$NON-NLS-1$
                            .getSimpleName()));
        }

        return fromList(NullUtils.nonNullContract(root.getAsJsonArray()));
    }

    /**
     * Parse an instance of the model from a {@link JsonObject}.
     *
     * @param json the JSON representation of the model to parse.
     * @return an instance of {@code T} parsed from {@code json}.
     * @throws JsonParseException If the model fails to parse.
     */
    @NonNull
    protected T createFrom(@NonNull final JsonObject json) throws JsonParseException {
        final T model = NullUtils.nonNullContract(mGson.fromJson(json, mType));

        return model;
    }

    /**
     * @return the type key which this object can be nested under.
     */
    @NonNull
    protected final String getTypeKey() {
        return mTypeKey;
    }

    /**
     * Serializer and deserializer for {@link MonetaryValue}s. Values are written and read as JSON
     * longs.
     */
    private static class MonetaryValueTypeAdapter extends TypeAdapter<MonetaryValue> {
        @Override
        public MonetaryValue read(final JsonReader reader) throws IOException {
            return new MonetaryValue(reader.nextLong());
        }

        @Override
        public void write(final JsonWriter writer, final MonetaryValue value) throws IOException {
            writer.value(value.getAmount());
        }
    }

    /**
     * A type adapter factory that only null-checks classes in the {@code com.scvngr.levelup}
     * package.
     */
    private static class NullCheckedTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T2> TypeAdapter<T2> create(final Gson gson, final TypeToken<T2> type) {
            final Class<? super T2> rawType = type.getRawType();

            if (!rawType.getName().startsWith("com.scvngr.levelup")) { //$NON-NLS-1$
                return null;
            }

            final TypeAdapter<T2> delegate =
                    NullUtils.nonNullContract(gson.getDelegateAdapter(this, type));

            return new NullCheckedWrappedTypeAdapter<T2>(delegate, type);
        }
    }

    /**
     * A type adapter factory that wraps/unwraps all {@code com.scvngr.levelup} JSON model
     * representations in a JSON Object whose key represents the model type.
     */
    private static class WrappedModelTypeAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T2> TypeAdapter<T2> create(final Gson gson, final TypeToken<T2> type) {
            final Class<? super T2> rawType = type.getRawType();

            if (rawType.isAnnotationPresent(NonWrappable.class)){
                return null;
            }

            if (!rawType.getName().startsWith("com.scvngr.levelup")) { //$NON-NLS-1$
                return null;
            }

            final TypeAdapter<T2> delegate =
                    NullUtils.nonNullContract(gson.getDelegateAdapter(this, type));

            return new WrappedModelTypeAdapter<T2>(delegate, type);
        }
    }

    /**
     * Wraps another type adapter, checking all non-static, final fields annotated with
     * {@link NonNull} for {@code null}. This works by inflating the model using the delegate
     * adapter and then accessing the generated model's fields.
     *
     * @param <T2> the model type
     */
    private static class NullCheckedWrappedTypeAdapter<T2> extends TypeAdapter<T2> {

        private final TypeAdapter<T2> mDelegate;
        private final HashSet<String> mNonNullFields = new HashSet<String>();

        public NullCheckedWrappedTypeAdapter(@NonNull final TypeAdapter<T2> delegate,
                @NonNull final TypeToken<T2> type) {
            mDelegate = delegate;

            final Class<? super T2> thisType = type.getRawType();

            for (final Field field : thisType.getDeclaredFields()) {
                final int modifiers = field.getModifiers();

                // static and transient are ignored by gson.
                if (Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)
                        || Modifier.isTransient(modifiers)) {
                    // This type of field is likely not what we should be checking
                    continue;
                }

                if (field.isAnnotationPresent(NonNull.class)) {
                    mNonNullFields.add(field.getName());
                }
            }
        }

        @Override
        public T2 read(final JsonReader reader) throws IOException {
            final T2 inspected = mDelegate.read(reader);

            if (null == inspected) {
                return null;
            }

            final Class<?> r = inspected.getClass();
            try {
                for (final String nonNullField : mNonNullFields) {
                    try {
                        final Field f = r.getDeclaredField(nonNullField);
                        try {
                            f.setAccessible(true);

                            if (null == f.get(inspected)) {
                                // This is what it's all about.
                                throw new IOException(NullUtils.format(
                                        "Field %s cannot be null", nonNullField)); //$NON-NLS-1$
                            }
                        } finally {
                            f.setAccessible(false);
                        }
                    } catch (final NoSuchFieldException e) {
                        throw new RuntimeException("Unexpected reflection exception", e); //$NON-NLS-1$
                    }
                }
            } catch (final IllegalAccessException e) {
                throw new RuntimeException("Unexpected reflection exception", e); //$NON-NLS-1$
            } catch (final IllegalArgumentException e) {
                throw new RuntimeException("Unexpected reflection exception", e); //$NON-NLS-1$
            }

            return inspected;
        }

        @Override
        public void write(final JsonWriter writer, final T2 model) throws IOException {
            mDelegate.write(writer, model);
        }
    }

    /**
     * Unwraps the given type from a JSON type container. This container is a JSON Object with one
     * key, the type name. Its value is the desired object.
     *
     * @param <T2> model type
     */
    private static class WrappedModelTypeAdapter<T2> extends TypeAdapter<T2> {
        @NonNull
        private final TypeAdapter<T2> mDelegate;

        @NonNull
        private final String mModelRoot;

        public WrappedModelTypeAdapter(@NonNull final TypeAdapter<T2> delegate,
                @NonNull final TypeToken<T2> type) {
            mDelegate = delegate;
            mModelRoot =
                    NullUtils
                            .nonNullContract(separateCamelCase(
                                    NullUtils.nonNullContract(type.getRawType().getSimpleName()),
                                    "_").toLowerCase(Locale.US)); //$NON-NLS-1$
        }

        //@formatter:off
        /*
         * Method below from Gson
         *
         * Copyright (C) 2008 Google Inc.
         *
         * Licensed under the Apache License, Version 2.0 (the "License");
         * you may not use this file except in compliance with the License.
         * You may obtain a copy of the License at
         *
         * http://www.apache.org/licenses/LICENSE-2.0
         *
         * Unless required by applicable law or agreed to in writing, software
         * distributed under the License is distributed on an "AS IS" BASIS,
         * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         * See the License for the specific language governing permissions and
         * limitations under the License.
         */
        //@formatter:on

        /**
         * Converts the field name that uses camel-case define word separation into separate words
         * that are separated by the provided {@code separatorString}.
         *
         * @param name the name to convert
         * @param separator the delimiter
         * @return the modified name.
         */
        @NonNull
        private static String separateCamelCase(@NonNull final String name,
                @NonNull final String separator) {
            final StringBuilder translation = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                final char character = name.charAt(i);
                if (Character.isUpperCase(character) && translation.length() != 0) {
                    translation.append(separator);
                }
                translation.append(character);
            }
            return NullUtils.nonNullContract(translation.toString());
        }

        @Override
        public T2 read(final JsonReader reader) throws IOException {
            reader.beginObject();
            final String name = reader.nextName();

            if (!mModelRoot.equals(name)) {
                throw new IOException(NullUtils.format(
                        "Expecting key '%s' in wrapped model, but was '%s'.", mModelRoot, name)); //$NON-NLS-1$
            }

            final T2 object = mDelegate.read(reader);
            reader.endObject();

            return object;
        }

        @Override
        public void write(final JsonWriter writer, final T2 val) throws IOException {
            writer.beginObject();
            writer.name(mModelRoot);
            mDelegate.write(writer, val);
            writer.endObject();
        }

    }

    /**
     * Type adapter for {@link Uri}s.
     */
    private static class UriTypeAdapter extends TypeAdapter<Uri> {
        @Override
        public Uri read(final JsonReader reader) throws IOException {
            return Uri.parse(reader.nextString());
        }

        @Override
        public void write(final JsonWriter writer, final Uri value) throws IOException {
            writer.value(value.toString());
        }
    }
}
