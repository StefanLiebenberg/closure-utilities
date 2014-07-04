package slieb.closureutils.tools;



import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class URIUtils {

    private URIUtils() {}

    public static URI getRelativeURISafe(URI base, URI reference) {
        try {
            return getRelativeURI(base, reference);
        } catch (URISyntaxException e) {
            return reference;
        }
    }

    public static URI getRelativeURI(URI base, URI reference)
            throws URISyntaxException {
        if (hasSameLocation(base, reference)) {
            Path basePath = Paths.get(base.getPath());
            if (!basePath.endsWith("/")) {
                basePath = basePath.getParent();
            }
            Path referencePath = Paths.get(reference.getPath());
            return new URI(basePath.relativize(referencePath).toString());
        }
        return reference;
    }

    private static boolean isSame(Object a, Object b) {
        return a != null ? a.equals(b) : b != null;
    }

    public static boolean hasSameScheme(URI a, URI b) {
        return isSame(a.getScheme(), b.getScheme());
    }


    public static boolean hasSameHost(URI a, URI b) {
        return isSame(a.getHost(), b.getHost());
    }


    public static boolean hasSamePort(URI a, URI b) {
        return isSame(a.getPort(), b.getPort());
    }

    public static boolean hasSameLocation(URI uriA, URI uriB) {
        return hasSameScheme(uriA, uriB) &&
                hasSameHost(uriA, uriB) &&
                hasSamePort(uriA, uriB);
    }
}

