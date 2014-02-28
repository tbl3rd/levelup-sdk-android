/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.os.Parcel;
import android.os.Parcelable;
import android.test.AndroidTestCase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.model.Category;
import com.scvngr.levelup.core.model.CategoryFixture;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.Order;
import com.scvngr.levelup.core.model.OrderFixture;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.UserFixture;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

/**
 * Tests {@link GsonModelFactory} with a few of the models. Once these are moved over to individual
 * models, this class can be removed.
 */
public final class GsonModelFactoryTest extends AndroidTestCase {

    /**
     * Tests loading a full {@link Category} from the {@link CategoryFixture}.
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
        assertEquals("category name", model.getName()); //$NON-NLS-1$

        assertEquals(CategoryFixture.getFullModel(1), model);
    }

    /**
     * Tests loading a minimal {@link Order} from the {@link OrderFixture}.
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
     * Tests loading a minimal {@link User} from the {@link UserFixture}.
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
     * Tests using {@link Product} with a minimal model.
     */
    public void testModel_fromJsonMinimal() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, false); //$NON-NLS-1$

        final Product product = factory.from(getMinimalProductFixture());

        assertNotNull(product.getCost());
        assertEquals(500, product.getCost().getAmount());
        assertNull(product.getDescription());
        assertEquals(123, product.getId());
        assertEquals(50, product.getInventoryCount());
        assertEquals("widget", product.getName()); //$NON-NLS-1$
        assertEquals("Product", product.DB_KEY);
    }

    /**
     * Tests using {@link Product} with a full model.
     */
    public void testModel_fromJsonFull() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, false); //$NON-NLS-1$

        final Product product = factory.from(getFullProductFixture());

        assertNotNull(product.getCost());
        assertEquals(500, product.getCost().getAmount());
        assertEquals("This makes widgets", product.getDescription()); //$NON-NLS-1$
        assertEquals(123, product.getId());
        assertEquals(50, product.getInventoryCount());
        assertEquals("widget", product.getName()); //$NON-NLS-1$
        assertEquals("Product", product.DB_KEY);

    }

    public void testModel_fromWrappedJsonFull() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, true); //$NON-NLS-1$
        final JsonObject obj = getFullProductFixture();
        final JsonObject container = new JsonObject();
        container.add("product", obj);
        final Product product = factory.from(container);

        assertNotNull(product.getCost());
        assertEquals(500, product.getCost().getAmount());
        assertEquals("This makes widgets", product.getDescription()); //$NON-NLS-1$
        assertEquals(123, product.getId());
        assertEquals(50, product.getInventoryCount());
        assertEquals("widget", product.getName()); //$NON-NLS-1$
        assertEquals("Product", product.DB_KEY);
    }

    /**
     * Tests using {@link Product} with an invalid model.
     */
    public void testModel_fromJsonInvalidNoName() {
        final GsonModelFactory<Product> factory =
                new GsonModelFactory<Product>("product", Product.class, false); //$NON-NLS-1$

        try {
            final JsonObject jo = getMinimalProductFixture();

            jo.remove("name"); //$NON-NLS-1$

            factory.from(jo);
            fail("Exception expected to be thrown"); //$NON-NLS-1$

        } catch (final JsonParseException e) {
            // Expected exception.
        }
    }

    @NonNull
    private JsonObject getMinimalProductFixture() {
        final JsonObject jo = new JsonObject();
        jo.addProperty("cost", 500); //$NON-NLS-1$
        jo.addProperty("id", 123); //$NON-NLS-1$
        jo.addProperty("inventory_count", 50); //$NON-NLS-1$
        jo.addProperty("name", "widget"); //$NON-NLS-1$ //$NON-NLS-2$

        return jo;
    }

    @NonNull
    private JsonObject getFullProductFixture() {
        final JsonObject jo = getMinimalProductFixture();

        jo.addProperty("description", "This makes widgets"); //$NON-NLS-1$//$NON-NLS-2$

        return jo;
    }

    /**
     * A test model.
     */
    @SuppressWarnings("javadoc")
    @Immutable
    public static final class Product implements Parcelable {

        /**
         * A test MonetaryValue.
         */
        @NonNull
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
        private final String name;

        /**
         * A static field.
         */
        @NonNull
        public static final String DB_KEY = "Product"; //$NON-NLS-1$

        /**
         * A test model that's not part of our core models. This includes some common edge cases.
         */
        public Product(@NonNull final MonetaryValue cost, @Nullable final String description,
                final long id, final long inventoryCount, @NonNull final String name) {
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
