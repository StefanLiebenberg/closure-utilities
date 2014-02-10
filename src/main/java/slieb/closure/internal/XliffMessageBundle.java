package slieb.closure.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.GoogleJsMessageIdGenerator;
import com.google.javascript.jscomp.JsMessage;
import com.google.javascript.jscomp.MessageBundle;
import org.xml.sax.*;

import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;


public class XliffMessageBundle implements MessageBundle {

    private static final SecureEntityResolver NOOP_RESOLVER
            = new SecureEntityResolver();

    private final Map<String, JsMessage> messages;

    private final JsMessage.IdGenerator idGenerator;

    public XliffMessageBundle(InputStream xliffStream,
                              @Nullable String projectId,
                              @SuppressWarnings("unused") boolean unused) {
        this(xliffStream, projectId);
    }

    /**
     * Creates an instance and initializes it with the messages in an XTB file.
     *
     * @param xtb       the XTB file as a byte stream
     * @param projectId the translation console project id (i.e. name)
     */
    public XliffMessageBundle(InputStream xtb, @Nullable String projectId) {
        Preconditions.checkState(!"".equals(projectId));
        this.messages = Maps.newHashMap();
        this.idGenerator = new GoogleJsMessageIdGenerator(projectId);

        try {
            // Use a SAX parser for speed and less memory usage.
            SAXParser parser = createSAXParser();
            XMLReader reader = parser.getXMLReader();
            Handler contentHandler = new Handler();
            reader.setContentHandler(contentHandler);
            reader.parse(new InputSource(xtb));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Inlined from guava-internal.
    private SAXParser createSAXParser()
            throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setXIncludeAware(false);
        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false);
        factory.setFeature(
                "http://apache.org/xml/features/nonvalidating/load-external" +
                        "-dtd",
                false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        SAXParser parser = factory.newSAXParser();
        XMLReader xmlReader = parser.getXMLReader();
        xmlReader.setEntityResolver(NOOP_RESOLVER);
        return parser;
    }

    @Override
    public JsMessage getMessage(String id) {
        return messages.get(id);
    }

    @Override
    public JsMessage.IdGenerator idGenerator() {
        return idGenerator;
    }

    @Override
    public Iterable<JsMessage> getAllMessages() {
        return Iterables.unmodifiableIterable(messages.values());
    }

    /**
     * A {@link org.xml.sax.ContentHandler} that creates a {@link JsMessage}
     * for each message
     * parsed from an XML Translation Bundle (XTB) file.
     */
    private class Handler implements ContentHandler {
        private static final String FILE_UNIT_NAME = "file";
        private static final String FILE_UNIT_LANG_ATTR_NAME =
                "target-language";
        private static final String TRANS_UNIT_NAME = "trans-unit";
        private static final String TRANS_UNIT_ID_ATTR_NAME = "id";
        private static final String TARGET_UNIT_NAME = "target";

        String lang;
        JsMessage.Builder msgBuilder;

        private boolean isInTarget = false;

        @Override
        public void setDocumentLocator(Locator locator) {}

        @Override
        public void startDocument() {}

        @Override
        public void endDocument() {}

        @Override
        public void startPrefixMapping(String prefix, String uri) {}

        @Override
        public void endPrefixMapping(String prefix) {}

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes atts) {
            switch (qName) {
                case FILE_UNIT_NAME:
                    Preconditions.checkState(lang == null);
                    lang = atts.getValue(FILE_UNIT_LANG_ATTR_NAME);
                    Preconditions.checkState(lang != null && !lang.isEmpty());
                    break;
                case TRANS_UNIT_NAME:
                    Preconditions.checkState(msgBuilder == null);
                    String id = atts.getValue(TRANS_UNIT_ID_ATTR_NAME);
                    Preconditions.checkState(id != null && !id.isEmpty());
                    msgBuilder = new JsMessage.Builder(id);
                    break;
                case TARGET_UNIT_NAME:
                    Preconditions.checkState(msgBuilder != null);
                    Preconditions.checkState(!isInTarget);
                    isInTarget = true;
//                    String phRef = atts.getValue(PLACEHOLDER_NAME_ATT_NAME);
//                    phRef = JsMessageVisitor
// .toLowerCamelCaseWithNumericSuffixes
//                            (phRef);
//                    msgBuilder.appendPlaceholderReference(phRef);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            switch (qName) {
                case TARGET_UNIT_NAME:
                    Preconditions.checkState(msgBuilder != null);
                    Preconditions.checkState(isInTarget);
                    isInTarget = false;
                    break;
                case TRANS_UNIT_NAME:
                    Preconditions.checkState(msgBuilder != null);
                    if (!msgBuilder.hasParts()) {
                        msgBuilder.appendStringPart("");
                    }
                    String key = msgBuilder.getKey();
                    messages.put(key, msgBuilder.build());
                    msgBuilder = null;
                    break;
            }
        }

        public void appendString(String value) {
            String stringRef, placeHolderRef;
            int length = value.length();
            int prevEnd = 0;
            int nextStart = value.indexOf("{$", prevEnd);
            while (nextStart != -1) {
                int nextEnd = value.indexOf("}", nextStart + 2);
                stringRef = value.substring(prevEnd, nextStart);
                placeHolderRef = value.substring(nextStart + 2, nextEnd);
                msgBuilder.appendStringPart(stringRef);
                msgBuilder.appendPlaceholderReference(placeHolderRef);
                prevEnd = nextEnd + 1;
                nextStart = value.indexOf("{$", prevEnd);
            }

            if (prevEnd < length) {
                stringRef = value.substring(prevEnd, length);
                msgBuilder.appendStringPart(stringRef);
            }
        }

        @Override
        public void characters(char ch[], int start, int length) {
            if (msgBuilder != null && isInTarget) {
                appendString(String.valueOf(ch, start, length));
            }
        }

        @Override
        public void ignorableWhitespace(char ch[], int start, int length) {
            if (msgBuilder != null && isInTarget) {
                // Preserve whitespace in messages.
                msgBuilder.appendStringPart(String.valueOf(ch, start, length));
            }
        }

        @Override
        public void processingInstruction(String target, String data) {}

        @Override
        public void skippedEntity(String name) {}
    }

    /**
     * A secure EntityResolver that returns an empty string in response to
     * any attempt to resolve an external entity. The class is used by our
     * secure version of the internal saxon SAX parser.
     */
    private static final class SecureEntityResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }
    }
}
