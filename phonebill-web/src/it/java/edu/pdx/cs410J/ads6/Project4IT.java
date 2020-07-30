package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests the {@link Project4} class by invoking its main method with various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");
    private String[] data;

    @Before
    public void setUp() {
        // represents valid data - replace to test error messages
        data = new String[]{"-print", "Andrew Stevenson","122-234-2343","133-333-3333", //0,1,2,3
                "10/30/2020", "05:30", "03/17/2021", "03:57",                           //4,5,6,7
                "-README", "-textFile", "test.txt", "-pretty", "bill.txt",             //8,9,10,11,12
                "am", "pm"};                                                            //13,14
    }

    @After
    public void tearDown() {
        File file = new File(data[10]);
        file.delete();      // Intentionally ignore result; we don't care if the file existed or not
        file = new File(data[12]);
        file.delete();      // Intentionally ignore result; we don't care if the file existed or not
        data = null;
    }
    /**
     * Invokes the main method of {@link Project4} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project4.class, args );
    }

    // New tests for P4
    @Test
    public void P4IT_ValidHost_Success() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "8080", "Dave");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
  }

    @Test
    public void P4IT_invalidHost_Failure() {
        MainMethodResult result = invokeMain("-host", "localhost", "-port", "0", "Dave");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: the host \"localhost:0 could not be reached."));
    }

    @Test
    public void P4IT_forgotEndDateArg_Failure() {
        MainMethodResult result = invokeMain( "-search", "Dave");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments."));
    }

    @Test
    public void P4IT_Add2PhoneCallsPrintThenGet_Success() {
        MainMethodResult result = invokeMain("-print", "Dave", "503-245-9999", "765-389-9999", "02/27/2020",
                "8:56", "am", "02/27/2020", "10:27", "am");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Phone call from 503-245-9999 to 765-389-9999 from 02/27/2020 8:56 AM to 02/27/2020 10:27 AM"));

        result = invokeMain("-print", "Dave", "503-245-2345", "765-389-1273", "02/27/2020",
                "8:56", "am", "02/27/2020", "10:27", "am");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Phone call from 503-245-2345 to 765-389-1273 from 02/27/2020 8:56 AM to 02/27/2020 10:27 AM"));

        result = invokeMain("-search", "Dave", "02/27/2020", "8:56", "am", "02/27/2020", "10:27", "am");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Customer: Dave"));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("91"));

        result = invokeMain( "Dave");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Customer: Dave"));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("91"));
    }

    @Test
    public void P4IT_AddPhoneCallNoPrint_Success() {
        MainMethodResult result = invokeMain("Dave", "503-245-2345", "765-389-1273", "02/27/2020",
                "8:56", "am", "02/27/2020", "10:27", "am");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.is(""));
    }

    // legacy (P1 - P3) code tests
    // Begin here

    @Test
    public void Project4_Main_NoArgs_Error() {
        MainMethodResult result = invokeMain();
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project4_Main_NullArgs_Error() {
        MainMethodResult result = invokeMain(null);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing command line arguments"));
    }

    @Test
    public void Project4_Main_1Arg_Success() {
        MainMethodResult result = invokeMain("asdfasdf");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("No phone records on file for customer."));
    }

    @Test
    public void Project4_Main_1ArgREADME_Success() {
        MainMethodResult result = invokeMain(data[8]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project4_Main_8ArgMissingTime_Error() {
        MainMethodResult result = invokeMain(data[0], data[1],data[2],data[3],data[4],data[5], data[13],data[6]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments."));
    }

    @Test
    public void Project4_Main_10ArgPrettyMissingFileArg_Failure() {
        MainMethodResult result = invokeMain(data[11], data[1],data[2],data[3],data[4],data[5], data[13],data[6], data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project4_Main_10ArgTextMissingFileArg_Failure() {
        MainMethodResult result = invokeMain(data[9], data[1],data[2],data[3],data[4],data[5], data[13],data[6], data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project4_Main_9ArgNoOptions_Success() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
    }

    @Test
    public void Project4_Main_9ArgNoOptionsBackwardDates_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[6],data[7], data[14],data[4],data[5], data[13]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: the start time is before the end time"));
    }

    @Test
    public void Project4_Main_8ArgNoOptionsForgotFirstAM_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments."));
    }

    @Test
    public void Project4_Main_8ArgNoOptionsForgotSecondAM_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Missing or incorrect command line arguments."));
    }

    @Test
    public void Project4_Main_9ArgNoOptionsBadBeginTime_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],"14:13", data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project4_Main_9ArgNoOptionsBadEndTime_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5], data[13],data[6],"05:77", data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error: Incorrectly formatted end date/time."));
    }

    @Test
    public void Project4_Main_10ArgPrint_Success() {
        MainMethodResult result = invokeMain(data[0], data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString(data[2]));
    }

    @Test
    public void Project4_Main_11ArgREADME_Success() {
        MainMethodResult result = invokeMain(data[8], data[0], data[1], data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14] );
        assertThat(result.getExitCode(), CoreMatchers.equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project4_Main_10ArgPrint_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14],data[0]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Options must precede the arguments."));
    }

    @Test
    public void Project4_Main_13ArgTextFile_Success() {
        MainMethodResult result = invokeMain(data[0], data[8], data[9], data[10], data[1], data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project4_Main_15ArgPrettyButREADME_Success() {
        MainMethodResult result = invokeMain(data[0], data[8], data[9], data[10], data[11], data[12], data[1],
                data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project4_Main_BadCaller_Failure() {
        MainMethodResult result = invokeMain(data[1],"asdfasdf",data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrectly formatted caller phone number."));
    }

    @Test
    public void Project4_Main_BadCallee_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],"asdfasdf",data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrectly formatted callee phone number."));
    }

    @Test
    public void Project4_Main_BadBeginDate_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],"asdf",data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project4_Main_BadBeginTime_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],"50:30:25", "am", data[6],data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project4_Main_BadEndDate_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5], data[13],"asdfasdf",data[7], data[14]);
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrectly formatted end date/time."));
    }

    @Test
    public void Project4_Main_BadEndTime_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5], data[13],data[6],"asdfasdfasdf", "am");
        assertThat(result.getExitCode(), CoreMatchers.equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Incorrectly formatted end date/time."));
    }

}