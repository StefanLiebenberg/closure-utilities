package slieb.closureutils.resources;

import slieb.closureutils.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static slieb.closureutils.resources.Resources.getResourceInputStream;

public class ResourceDigest {

    public static String getResourceChecksum(final Resource resource)
            throws IOException {
        try (InputStream inputStream = getResourceInputStream(resource)) {
            return md5Hex(inputStream);
        }
    }

    public static Map<Resource, String>
    getResourceChecksumMap(Iterable<Resource> resources)
            throws IOException {
        Map<Resource, String> map = new Hashtable<>();
        for (Resource resource : resources) {
            map.put(resource, getResourceChecksum(resource));
        }
        return map;
    }
}
