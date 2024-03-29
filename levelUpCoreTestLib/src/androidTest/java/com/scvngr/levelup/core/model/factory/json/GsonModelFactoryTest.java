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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.model.RequiredField;
import com.scvngr.levelup.core.model.Category;
import com.scvngr.levelup.core.model.CategoryFixture;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.Order;
import com.scvngr.levelup.core.model.OrderFixture;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.UserFixture;
import com.scvngr.levelup.core.util.NullUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.jcip.annotations.Immutable;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.GsonModelFactory} with a few of the models. Once these are moved over to individual
 * models, this class can be removed.
 */
public final class GsonModelFactoryTest extends AndroidTestCase {

    /**
     * Tests loading a full {@link com.scvngr.levelup.core.model.Category} from the {@link CategoryFixture}.
     */
    public void testGsonModel_category() {
        final GsonModelFactory<Category> modelFactory =
                new GsonModelFactory<Category>(CategoryJsonFactory.JsonKeys.MODEL_ROOT,
                        Category.class, false);
        final Category model =
                modelFactory.from(NullUtils.nonNullContract(CategoryFixture.getFullJsonObject()
                        .toString()));

        assertNotNull(model);

        assertEquals(1, model.getId());
        assertEquals("category name", model.getName());

        assertEquals(CategoryFixture.getFullModel(1), model);
    }

    /**
     * Tests loading a minimal {@link com.scvngr.levelup.core.model.Order} from the {@link OrderFixture}.
     */
    public void testGsonModel_orderMinimal() {
        final GsonModelFactory<Order> modelFactory =
                new GsonModelFactory<Order>(OrderJsonFactory.JsonKeys.MODEL_ROOT, Order.class,
                        false);
        final Order order =
                modelFactory.from(NullUtils.nonNullContract(OrderFixture.getMinimalJsonObject()
                        .toString()));

        assertNotNull(order);

        assertNull(order.getBundleClosedAt());
        assertEquals(OrderFixture.DATE_TIME_FIXTURE_1, order.getCreatedAt());
        assertEquals(OrderFixture.UUID_FIXTURE_1, order.getUuid());
        assertNull(order.getRefundedAt());
        assertNull(order.getTransactedAt());

        assertEquals(OrderFixture.getMinimalModel(), order);
    }

    /**
     * Tests loading a minimal {@link com.scvngr.levelup.core.model.User} from the {@link UserFixture}.
     */
    public void testGsonModel_userMinimal() {
        final GsonModelFactory<User> modelFactory =
                new GsonModelFactory<User>(UserJsonFactory.JsonKeys.MODEL_ROOT, User.class, false);
        final User model =
                modelFactory.from(NullUtils.nonNullContract(UserFixture.getMinimalJsonObject()
                        .toString()));

        assertNotNull(model);

        assertEquals(1, model.getId());

        assertEquals(UserFixture.getMinimalModel(), model);
    }

    /**
     * Tests using {@link com.scvngr.levelup.core.model.factory.json.GsonModelFactoryTest.Product} with a minimal model.
     */
    public void testModel_fromJsonMinimal() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, false);

        final Product product = factory.from(getMinimalProductFixture());

        assertNotNull(product.getCost());
        assertEquals(500, product.getCost().getAmount());
        assertNull(product.getDescription());
        assertEquals(123, product.getId());
        assertEquals(50, product.getInventoryCount());
        assertEquals("widget", product.getName());
        assertEquals("Product", Product.DB_KEY);
    }

    /**
     * Tests using {@link com.scvngr.levelup.core.model.factory.json.GsonModelFactoryTest.Product} with a full model.
     */
    public void testModel_fromJsonFull() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, false);

        final Product product = factory.from(getFullProductFixture());

        assertNotNull(product.getCost());
        assertEquals(500, product.getCost().getAmount());
        assertEquals("This makes widgets", product.getDescription());
        assertEquals(123, product.getId());
        assertEquals(50, product.getInventoryCount());
        assertEquals("widget", product.getName());
        assertEquals("Product", Product.DB_KEY);

    }

    public void testModel_fromWrappedJsonFull() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, true);
        final JsonObject obj = getFullProductFixture();
        final JsonObject container = new JsonObject();
        container.add("product", obj);
        final Product product = factory.from(container);

        assertNotNull(product.getCost());
        assertEquals(500, product.getCost().getAmount());
        assertEquals("This makes widgets", product.getDescription());
        assertEquals(123, product.getId());
        assertEquals(50, product.getInventoryCount());
        assertEquals("widget", product.getName());
        assertEquals("Product", Product.DB_KEY);
    }

    /**
     * Tests using {@link com.scvngr.levelup.core.model.factory.json.GsonModelFactoryTest.Product} with an invalid model.
     */
    public void testModel_fromJsonInvalidNoName() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, false);

        try {
            final JsonObject jo = getMinimalProductFixture();

            jo.remove("name");

            factory.from(jo);
            fail("Exception expected to be thrown");

        } catch (final JsonParseException e) {
            // Expected exception.
        }
    }

    @NonNull
    private JsonObject getMinimalProductFixture() {
        final JsonObject jo = new JsonObject();
        jo.addProperty("cost", 500);
        jo.addProperty("id", 123);
        jo.addProperty("inventory_count", 50);
        jo.addProperty("name", "widget");

        return jo;
    }

    @NonNull
    private JsonObject getFullProductFixture() {
        final JsonObject jo = getMinimalProductFixture();

        jo.addProperty("description", "This makes widgets");

        return jo;
    }

    /**
     * A test model.
     */
    @Immutable
    public static final class Product implements Parcelable {

        /**
         * A test MonetaryValue.
         */
        @NonNull
        @RequiredField
        private final MonetaryValue cost;

        /**
         * An optional field.
         */
        @Nullable
        private final String description;

        /**
         * Basic long.
         */
        private final long id;

        /**
         * A test item with camelCase.
         */
        private final long inventoryCount;

        /**
         * Basic required String.
         */
        @NonNull
        @RequiredField
        private final String name;

        /**
         * A static field.
         */
        @NonNull
        public static final String DB_KEY = "Product";

        /**
         * A test model that's not part of our core models. This includes some common edge cases.
         */
        //CHECKSTYLE:OFF don't warn about field hiding for this sample model constructor.
        public Product(@NonNull final MonetaryValue cost, @Nullable final String description,
                final long id, final long inventoryCount, @NonNull final String name) {
            //CHECKSTYLE:ON
            this.cost = cost;
            this.description = description;
            this.id = id;
            this.inventoryCount = inventoryCount;
            this.name = name;
        }

        public static final Creator<Product> CREATOR = null;

        @NonNull
        public MonetaryValue getCost() {
            return cost;
        }

        @Nullable
        public String getDescription() {
            return description;
        }

        public long getId() {
            return id;
        }

        public long getInventoryCount() {
            return inventoryCount;
        }

        @NonNull
        public String getName() {
            return name;
        }

        @Override
        public int describeContents() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            throw new UnsupportedOperationException();
        }
    }
}
