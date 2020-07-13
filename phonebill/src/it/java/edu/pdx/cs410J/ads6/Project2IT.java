package edu.pdx.cs410J.ads6;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

/**
 * Tests the functionality in the {@link Project2} main class.
 */
public class Project2IT extends InvokeMainTestCase {
    private String[] data;

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project2.class, args );
    }

    @Before
    public void setUp() {
        // represents valid data - replace to test error messages
        data = new String[]{"-print", "Andrew Stevenson","322-234-2343","333-333-3333", //0,1,2,3
                "10/30/2020", "05:30", "03/17/2021", "23:67",                           //4,5,6,7
                "-README", "-textFile", "test.txt"};                                    //8,9,10
    }

    @After
    public void tearDown() {
        File file = new File(data[10]);
        file.delete();      // Intentionally ignore result; we don't care if the file existed or not
        data = null;
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
    @Test
    public void Project1_Main_NoArgs_Error() {
        MainMethodResult result = invokeMain();
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_NullArgs_Error() {
        MainMethodResult result = invokeMain(null);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_1Arg_Error() {
        MainMethodResult result = invokeMain(data[1]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_1ArgREADME_Success() {
        MainMethodResult result = invokeMain(data[8]);
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project1_Main_7Arg_Error() {
        MainMethodResult result = invokeMain(data[0], data[1],data[2],data[3],data[4],data[5],data[6]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_7Arg_Success() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(null));
    }

    @Test
    public void Project1_Main_9ArgFileNameCustomerIsCustomer_Success() throws IOException {
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(new String(Files.readAllBytes(Paths.get(data[10]))),containsString(data[2]));
    }

    @Test
    public void Project1_Main_9ArgFileNameCallerIsCaller_Success() throws IOException {
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(new String(Files.readAllBytes(Paths.get(data[10]))),containsString(data[1]));
    }

    @Test
    public void Project1_Main_8ArgPrint_Success() {
        MainMethodResult result = invokeMain(data[0], data[1],data[2],data[3],data[4],data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), containsString(data[2]));
    }

    @Test
    public void Project1_Main_9ArgREADME_Success() {
        MainMethodResult result = invokeMain(data[8], data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7] );
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project1_Main_8ArgPrint_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[0]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_9ArgREADME_Failure() {
        MainMethodResult result = invokeMain(data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7], data[8]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_11ArgTextFile_Failure() {
        MainMethodResult result = invokeMain(data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7], data[8], data[9], data[10]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_BadCaller_Failure() {
        MainMethodResult result = invokeMain(data[1],"asdfasdf",data[3],data[4],data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted caller phone number."));
    }

    @Test
    public void Project1_Main_BadCallee_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],"asdfasdf",data[4],data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted callee phone number."));
    }

    @Test
    public void Project1_Main_BadBeginDate_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],"asdf",data[5],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project1_Main_BadBeginTime_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],"50:30:25",data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project1_Main_BadEndDate_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5],"asdfasdf",data[7]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted end date/time."));
    }

    @Test
    public void Project1_Main_BadEndTime_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5],data[6],"asdfasdfasdf");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted end date/time."));
    }

}