package slieb.closure.tools;


import javax.annotation.Nonnull;
import java.util.*;

public class IteratorCollector<A> {

    private final Iterator<A> iterator;

    public IteratorCollector(@Nonnull final Iterator<A> iterator) {
        this.iterator = iterator;
    }

    public void collect(Collection<A> collection) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    public List<A> getList() {
        List<A> list = new ArrayList<>();
        collect(list);
        return list;
    }

    public Set<A> getSet() {
        Set<A> aSet = new HashSet<>();
        collect(aSet);
        return aSet;
    }

    public static <A> void collect(Iterator<A> iterator,
                                   Collection<A> collection) {
        new IteratorCollector<A>(iterator).collect(collection);
    }
}
