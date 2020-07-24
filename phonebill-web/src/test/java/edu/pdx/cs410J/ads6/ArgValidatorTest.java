package edu.pdx.cs410J.ads6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test suite for the {@link ArgValidator} class.
 */
public class ArgValidatorTest  {
    private String[] data;

    @Before
    public void setUp() {
        // represents valid data - replace to test error messages
        data = new String[]{"-print", "-textFile", "filename", "Andrew","122-234-2343","133-333-3333",
                "10/30/2020", "05:30", "pm", "03/17/2021", "03:07", "pm"};
    }

    @After
    public void tearDown() {
        data = null;
    }

    @Test
    public void ArgValidator_validate_nameIsName_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[0],data[3]);
    }

    @Test
    public void ArgValidator_validate_callerIsCaller_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[1],data[4]);
    }

    @Test
    public void ArgValidator_validate_calleeIsCallee_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[2],data[5]);
    }

    @Test
    public void ArgValidator_validate_beginIsBegin_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[3],data[6] + " " + data[7] + " " + data[8]);
    }

    @Test
    public void ArgValidator_validate_endIsEnd_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[4],data[9] + " " + data[10]+ " " + data[11]);
    }

    @Test
    public void ArgValidator_validate_printIsPrint_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[5],"true");
    }


}

