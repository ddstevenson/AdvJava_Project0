package edu.pdx.cs410J.ads6;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;
import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit test suite for the {@link TextParser} class.
 */
public class TextParserTest {
    private String[] data;

    @Before
    public void setUp() {
        // represents valid data - replace to test error messages
        data = new String[]{"-print", "-textFile", "filename", "Andrew","322-234-2343","333-333-3333",
                "10/30/2020", "05:30", "03/17/2021", "23:67"};
    }

    @After
    public void tearDown() {
        data = null;
    }

    @Test
    public void TextParser_getSet_TextIsText_True() {
        TextParser t = new TextParser();
        t.setFilename("bob");
        assertSame(t.getFilename(), "bob");
    }

    @Test(expected = NullPointerException.class)
    public void TextDumper_dump_NullFilenameThrowsException_True() throws ParserException {
        TextParser t = new TextParser(); // intentionally not setting filename
        AbstractPhoneCall pc = mock(AbstractPhoneCall.class);
        PhoneBill pb = new PhoneBill();
        pb.addPhoneCall(pc);
        t.parse();
    }
}