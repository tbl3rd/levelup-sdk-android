package com.scvngr.levelup.core.model.util;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.util.ParcelableArrayList.ImmutableClassException;
import com.scvngr.levelup.core.test.ParcelTestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Tests {@link ParcelableArrayList} using {@link Uri}s as a known-good {@link Parcelable} object.
 */
public final class ParcelableArrayListTest extends AndroidTestCase {
    private static final Uri URI_01 = Uri.parse("foo:"); //$NON-NLS-1$
    private static final Uri URI_02 = Uri.parse("http://example.com/"); //$NON-NLS-1$
    private static final Uri URI_03 = Uri.parse("urn:12345"); //$NON-NLS-1$
    private static final Uri URI_04 = Uri.parse("urn:foo"); //$NON-NLS-1$

    private static final List<Uri> BASIC_URI_LIST = Arrays.asList(new Uri[] { URI_01, URI_02,
            URI_03 });

    /**
     * Tests {@link ParcelableArrayList}'s constructor with an empty list.
     */
    public void testConstructor_empty() {
        final List<Uri> emptyList = Collections.emptyList();
        final UriArrayList arrayList = new UriArrayList(emptyList);

        assertEquals(0, arrayList.size());
        assertEquals(emptyList, arrayList);
    }

    /**
     * Tests {@link ParcelableArrayList}'s constructor with basic content.
     */
    public void testConstructor_basic() {
        final UriArrayList uriArrayList = new UriArrayList(BASIC_URI_LIST);

        assertEquals(3, uriArrayList.size());
        assertEquals(BASIC_URI_LIST, uriArrayList);
    }

    /**
     * Tests {@link ParcelTestUtils#assertParcelableRoundtrips(android.os.Parcelable)} with a
     * known-good empty parcelable to ensure it functions properly.
     */
    public void testParcelTest_empty() {
        ParcelTestUtils.assertParcelableRoundtrips(Uri.EMPTY);
    }

    /**
     * Tests {@link ParcelTestUtils#assertParcelableRoundtrips(android.os.Parcelable)} with a
     * known-good parcelable to ensure it functions properly.
     */
    public void testParcelTest_basic() {
        ParcelTestUtils.assertParcelableRoundtrips(URI_01);
    }

    /**
     * Tests round-tripping an empty {@link ParcelableArrayList} through the parcelable system.
     */
    public void testParceling_empty() {
        final List<Uri> emptyList = Collections.emptyList();
        final UriArrayList arrayList = new UriArrayList(emptyList);
        ParcelTestUtils.assertParcelableRoundtrips(arrayList);
    }

    /**
     * Tests round-tripping a basic {@link ParcelableArrayList} through the parcelable system.
     */
    public void testParceling_basic() {
        final UriArrayList arrayList = new UriArrayList(BASIC_URI_LIST);
        ParcelTestUtils.assertParcelableRoundtrips(arrayList);
    }

    /**
     * Tests that all the methods which can modify the {@link ParcelableArrayList} throw an
     * exception.
     */
    public void testImmutable() {
        final UriArrayList arrayList = new UriArrayList(BASIC_URI_LIST);

        try {
            arrayList.add(URI_04);
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.add(0, URI_04);
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.addAll(Arrays.asList(new Uri[] { URI_04 }));
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.addAll(0, Arrays.asList(new Uri[] { URI_04 }));
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.clear();
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.ensureCapacity(100);
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);
        try {
            arrayList.remove(0);
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.remove(URI_01);
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.removeAll(Arrays.asList(new Uri[] { URI_02 }));
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.retainAll(Arrays.asList(new Uri[] { URI_02 }));
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.set(0, URI_03);
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);

        try {
            arrayList.trimToSize();
            failExpectingException();
        } catch (final ImmutableClassException e) {
            // Do nothing.
        }

        assertEquals(BASIC_URI_LIST, arrayList);
    }

    /**
     * Fail with an "expecting exception" message.
     */
    private void failExpectingException() {
        fail("Exception expected to be thrown."); //$NON-NLS-1$
    }

    /**
     * An concrete implementation of {@link ParcelableArrayList}.
     */
    public static final class UriArrayList extends ParcelableArrayList<Uri> {
        /**
         * Serializable ID.
         */
        private static final long serialVersionUID = -5075435829183218092L;

        /**
         * Parcelable creator.
         */
        public static final Creator<UriArrayList> CREATOR = new Creator<UriArrayList>() {

            @Override
            public UriArrayList[] newArray(final int size) {
                return new UriArrayList[size];
            }

            @Override
            public UriArrayList createFromParcel(final Parcel source) {
                return new UriArrayList(source);
            }
        };

        public UriArrayList(@NonNull final Parcel in) {
            super(in);
        }

        public UriArrayList(@NonNull final Collection<Uri> collection) {
            super(collection);
        }

        @Override
        protected Creator<Uri> getCreator() {
            return Uri.CREATOR;
        }
    }
}
