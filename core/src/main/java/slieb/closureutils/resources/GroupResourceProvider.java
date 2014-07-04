package slieb.closureutils.resources;


import com.google.common.collect.Iterators;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class GroupResourceProvider implements ResourceProvider {

    private final URI uri;

    private final List<ResourceProvider> resourceProviders;

    public GroupResourceProvider(URI uri,
                                 List<ResourceProvider> resourceProviders) {
        this.uri = uri;
        this.resourceProviders = resourceProviders;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public Iterable<Resource> getResources() {
        return new GroupIterable(resourceProviders, uri);
    }

    private static class GroupIterable implements Iterable<Resource> {

        private final List<ResourceProvider> resourceProviders;

        private final URI uri;

        private GroupIterable(List<ResourceProvider> resourceProviders,
                              URI uri) {
            this.resourceProviders = resourceProviders;
            this.uri = uri;
        }

        @Override
        public Iterator<Resource> iterator() {
            Iterator<Resource> iterator = null;
            for (ResourceProvider resourceProvider : resourceProviders) {
                Iterator<Resource> nextIterator = resourceProvider
                        .getResources().iterator();
                Iterator<Resource> resolvingIterator = new GroupEntryIterator
                        (nextIterator, uri);
                if (iterator == null) {
                    iterator = resolvingIterator;
                } else {
                    iterator = Iterators.concat(iterator, resolvingIterator);
                }
            }
            return iterator;
        }
    }

    private static class GroupEntryIterator implements Iterator<Resource> {

        private final Iterator<Resource> parentIterator;
        private final URI parentURI;

        private GroupEntryIterator(Iterator<Resource> parentIterator,
                                   URI parentURI) {
            this.parentIterator = parentIterator;
            this.parentURI = parentURI;
        }

        @Override
        public boolean hasNext() {
            return parentIterator.hasNext();
        }

        @Override
        public Resource next() {
            final Resource resource = parentIterator.next();
            final URI resourceUri = resource.getUri();
            final URI resolved = parentURI.resolve(resourceUri);
            return new URIReferenceResource(resource, resolved);
        }

        @Override
        public void remove() {
            parentIterator.remove();
        }
    }


}
