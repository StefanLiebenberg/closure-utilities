package com.github.stefanliebenberg.utilities;


import com.google.common.base.Function;
import com.google.common.collect.*;

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

    public static <A> ImmutableSet<A> set(final Collection<A> collection) {
        return ImmutableSet.copyOf(collection);
    }

    public static <A, B> ImmutableMap<A, B> map(final Map<A, B> map) {
        return ImmutableMap.copyOf(map);
    }

    public static <A, B, C> ImmutableMap<A, C> map(
            final Map<A, B> map,
            final Maps.EntryTransformer<A, B, C> transformer) {
        return map(transformMapValues(map, transformer));
    }

    public static <A, B> ImmutableList<B> list(
            final Collection<A> collection,
            final Function<? super A, B> transformFunction) {
        return list(transform(collection, transformFunction));
    }


    public static <A, B> ImmutableSet<B> set(final Collection<A> collection,
                                             final Function<A, B> transform) {
        return set(Collections2.transform(collection, transform));
    }

    public static String[] stringArray(Collection<String> collection) {
        final String[] list = new String[collection.size()];
        return collection.toArray(list);
    }
}
