package edu.pdx.cs410J.ads6;

import org.junit.*;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
        return;
    }
}
