package com.github.stefanliebenberg.utilities;


import com.google.common.base.Function;
import com.google.common.collect.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class Immuter {

    public static <A, B> Collection<B> transform(
            final Collection<A> aCollection,
            final Function<? super A, B> transformFunction
    ) {
        return Collections2.transform(aCollection, transformFunction);
    }

    public static <K, A, B> Map<K, B> transformMapValues(
            final Map<K, A> inputMap,
            final Maps.EntryTransformer<K, A, B> transformer) {
        return Maps.transformEntries(inputMap, transformer);
    }

    public static <A> ImmutableList<A> list(final Collection<A> collection) {
        return ImmutableList.copyOf(collection);
    }

    @Nullable
    public static <A> ImmutableSet<A> set(
            @Nullable final Collection<A> collection) {
        if (collection != null) {
            return ImmutableSet.copyOf(collection);
        } else {
            return null;
        }
    }

    @Nullable
    public static <A, B> ImmutableMap<A, B> map(
            @Nullable final Map<A, B> map) {
        if (map != null) {
            return ImmutableMap.copyOf(map);
        } else {
            return null;
        }
    }

    @Nullable
    public static <A, B, C> ImmutableMap<A, C> map(
            @Nullable final Map<A, B> map,
            @Nonnull final Maps.EntryTransformer<A, B, C> transformer) {
        if (map != null) {
            return map(transformMapValues(map, transformer));
        } else {
            return null;
        }
    }

    @Nullable
    public static <A, B> ImmutableList<B> list(
            @Nullable final Collection<A> collection,
            @Nonnull final Function<? super A, B> transformFunction) {
        if (collection != null) {
            return list(transform(collection, transformFunction));
        } else {
            return null;
        }
    }

    @Nullable
    public static <A, B> ImmutableSet<B> set(final Collection<A> collection,
                                             final Function<A, B> transform) {

        return set(Collections2.transform(collection, transform));
    }

    public static String[] stringArray(Collection<String> collection) {
        final String[] list = new String[collection.size()];
        return collection.toArray(list);
    }
}
