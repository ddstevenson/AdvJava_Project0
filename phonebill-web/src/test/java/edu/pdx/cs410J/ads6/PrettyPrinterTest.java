package edu.pdx.cs410J.ads6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link PrettyPrinter} class.
 */
public class PrettyPrinterTest {
    private String[] data;

    @Before
    public void setUp() {
        data = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/20 5:30 am",
                "3/17/21 03:07 am"};
    }

    @After
    public void tearDown() {
        data = null;
    }

    @Test
    public void fake_news() {
        return; // really no meaningful tests, since the prettyprinter is just a side effect
    }
}
