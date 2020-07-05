package edu.pdx.cs410J.ads6;
import org.junit.*;
import static org.junit.Assert.*;
/**
 * Unit test suite for the {@link ArgValidator} class.
 */
public class ArgValidatorTest  {
    private String[] data;

    @Before
    public void setUp() {
        // represents valid data - replace to test error messages
        data = new String[]{"-print", "Andrew","322-234-2343","333-333-3333",
                "10/30/2020", "05:30", "03/17/2021", "23:67"};
    }

    @After
    public void tearDown() {
        data = null;
    }

    @Test
    public void ArgValidator_validate_nameIsName_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[0],data[1]);
    }

    @Test
    public void ArgValidator_validate_callerIsCaller_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[1],data[2]);
    }

    @Test
    public void ArgValidator_validate_calleeIsCallee_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[2],data[3]);
    }

    @Test
    public void ArgValidator_validate_beginIsBegin_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[3],data[4] + " " + data[5]);
    }

    @Test
    public void ArgValidator_validate_endIsEnd_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[4],data[6] + " " + data[7]);
    }

    @Test
    public void ArgValidator_validate_printIsPrint_true() {
        ArgValidator val = new ArgValidator();
        String [] validated = val.validate(data);
        assertEquals(validated[5],"true");
    }

}

