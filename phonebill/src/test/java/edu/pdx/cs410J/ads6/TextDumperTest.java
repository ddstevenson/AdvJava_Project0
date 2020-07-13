package edu.pdx.cs410J.ads6;
import org.junit.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import edu.pdx.cs410J.AbstractPhoneCall;

import java.io.IOException;

/**
 * Unit test suite for the {@link TextDumper} class.
 */
public class TextDumperTest {
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
    public void TextDumper_getSet_TextIsText_True() {
        TextDumper t = new TextDumper();
        t.setFilename("bob");
        assertSame(t.getFilename(), "bob");
    }

    @Test(expected = NullPointerException.class)
    public void TextDumper_dump_NullFilenameThrowsException_True() throws IOException {
        TextDumper t = new TextDumper(); // intentionally not setting filename
        AbstractPhoneCall pc = mock(AbstractPhoneCall.class);
        PhoneBill pb = new PhoneBill();
        pb.addPhoneCall(pc);
        t.dump(pb);
    }

}
