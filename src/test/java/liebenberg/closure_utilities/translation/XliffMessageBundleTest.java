package liebenberg.closure_utilities.translation;

import com.google.javascript.jscomp.JsMessage;
import com.google.javascript.jscomp.MessageBundle;
import com.google.javascript.jscomp.XtbMessageBundle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;


public class XliffMessageBundleTest {

    InputStream inputStream;
    MessageBundle messageBundle;

    InputStream xtbInputStream;
    XtbMessageBundle xtbMessageBundle;

    @Before
    public void setUp() throws Exception {
        inputStream = getClass().getResourceAsStream
                ("/app/src/messages/company-af.xlif");
        messageBundle = new XliffMessageBundle(inputStream, "company");

        xtbInputStream = getClass().getResourceAsStream
                ("/app/src/messages/company-af.xtb");
        xtbMessageBundle = new XtbMessageBundle(xtbInputStream, "company");
    }

    @After
    public void tearDown() throws Exception {
        inputStream.close();
        messageBundle = null;

        xtbInputStream.close();
        xtbMessageBundle = null;
    }

    @Test
    public void testGetMessage() throws Exception {
        JsMessage message, xtbMessage;

        message = messageBundle.getMessage("123123");
        Assert.assertNull(message);

        message = messageBundle.getMessage("1358941337827109422");
        xtbMessage = xtbMessageBundle.getMessage("1358941337827109422");

        Assert.assertNotNull(message);
        Assert.assertEquals(xtbMessage.isEmpty(), message.isEmpty());
        Assert.assertEquals(xtbMessage, message);
    }
}
