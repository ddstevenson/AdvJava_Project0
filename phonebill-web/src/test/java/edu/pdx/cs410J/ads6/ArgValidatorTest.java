package edu.pdx.cs410J.ads6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test suite for the {@link ArgValidator} class.
 * @implNote The CUT issues side effects in the event of bad input; these cases
 * must be tested from the integration side, not the class side.
 */
public class ArgValidatorTest  {
    private String[] data;
    private String[] data2;

    @Before
    public void setUp() {
        // represents valid data
        data = new String[]{"-print", "-host", "the_name_of_the_host", "-port",
                "8080", "-search", "Andrew","122-234-2343","133-333-3333",
                "10/30/2020", "05:30", "pm", "03/17/2021", "03:07", "pm"};

        // valid data with no options
        data2 = new String[]{"Andrew","122-234-2343","133-333-3333",
                "10/30/2020", "05:30", "pm", "03/17/2021", "03:07", "pm"};
    }

    @After
    public void tearDown() {
        data = null;
        data2 = null;
    }

    @Test
    public void ArgValidator_validate_nameIsName_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[0],data[6]);
    }

    @Test
    public void ArgValidator_validate_callerIsCaller_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[1],data[7]);
    }

    @Test
    public void ArgValidator_validate_calleeIsCallee_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[2],data[8]);
    }

    @Test
    public void ArgValidator_validate_beginIsBegin_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[3],data[9] + " " + data[10] + " " + data[11]);
    }

    @Test
    public void ArgValidator_validate_endIsEnd_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[4],data[12] + " " + data[13]+ " " + data[14]);
    }

    @Test
    public void ArgValidator_validate_printIsPrint_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[5],"true");
    }

    @Test
    public void ArgValidator_validate_searchIsSearch_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[6],"true");
    }

    @Test
    public void ArgValidator_validate_printIsPrint_false() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data2);
        assertEquals(validated[5],"false");
    }

    @Test
    public void ArgValidator_validate_searchIsSearch_false() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data2);
        assertEquals(validated[6],"false");
    }

    @Test
    public void ArgValidator_validate_hostIsHost_false() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[7],data[2]);
    }

    @Test
    public void ArgValidator_validate_hostIsHost_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data2);
        assertNull(validated[7]);
    }

    @Test
    public void ArgValidator_validate_portIsPort_false() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[8],data[4]);
    }

    @Test
    public void ArgValidator_validate_portIsPort_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data2);
        assertNull(validated[8]);
    }


}

