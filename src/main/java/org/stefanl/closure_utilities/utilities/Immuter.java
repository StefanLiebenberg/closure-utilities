package org.stefanl.closure_utilities.utilities;


import com.google.common.base.Function;
import com.google.common.collect.*;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class Immuter {

    @Nonnull
    public static <A, B> Collection<B> transform(
            @Nonnull final Collection<A> aCollection,
            @Nonnull final Function<? super A, B> transformFunction
    ) {
        return Collections2.transform(aCollection, transformFunction);
    }

    @Nonnull
    public static <K, A, B> Map<K, B> transformMapValues(
            @Nonnull final Map<K, A> inputMap,
            @Nonnull final Maps.EntryTransformer<K, A, B> transformer) {
        return Maps.transformEntries(inputMap, transformer);
    }

    @Nonnull
    public static <A> ImmutableList<A> list(
            @Nonnull final Collection<A> collection) {
        return ImmutableList.copyOf(collection);
    }

    @Nonnull
    public static <A> ImmutableSet<A> set(
            @Nonnull final Collection<A> collection) {
        return ImmutableSet.copyOf(collection);
    }

    @Nonnull
    public static <A, B> ImmutableMap<A, B> map(
            @Nonnull final Map<A, B> map) {
        return ImmutableMap.copyOf(map);
    }

    @Nonnull
    public static <A, B, C> ImmutableMap<A, C> map(
            @Nonnull final Map<A, B> map,
            @Nonnull final Maps.EntryTransformer<A, B, C> transformer) {
        return map(transformMapValues(map, transformer));
    }

    @Nonnull
    public static <A, B> ImmutableList<B> list(
            @Nonnull final Collection<A> collection,
            @Nonnull final Function<? super A, B> transformFunction) {
        return list(transform(collection, transformFunction));
    }

    @Nonnull
    public static <A, B> ImmutableSet<B> set(
            @Nonnull final Collection<A> collection,
            @Nonnull final Function<A, B> transform) {
        return set(Collections2.transform(collection, transform));
    }

    @Nonnull
    public static String[] stringArray(
            @Nonnull final Collection<String> collection) {
        final String[] list = new String[collection.size()];
        return collection.toArray(list);
    }
}
