package edu.pdx.cs410J.ads6;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
public class Project3IT extends InvokeMainTestCase {
    private String[] data;
    private String fileContents, badFileContents;

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project3.class, args );
    }

    @Before
    public void setUp() {
        // represents valid data - replace to test error messages
        data = new String[]{"-print", "Andrew Stevenson","122-234-2343","133-333-3333", //0,1,2,3
                "10/30/2020", "05:30", "03/17/2021", "03:57",                           //4,5,6,7
                "-README", "-textFile", "test.txt", "-pretty", "bill.txt",             //8,9,10,11,12
                "am", "pm"};                                                            //13,14
        
        fileContents = "Andrew Stevenson's phone bill with 2 phone calls\n" +
                "Phone call from 666-666-6666 to 777-777-7777 from 10/30/1983, 05:30 pm to 03/17/1984, 3:07 am\n"
                + "Phone call from 555-555-5555 to 444-444-4444 from 10/30/1983, 05:30 pm to 03/17/1984, 03:07 am\n";

        badFileContents = "Andrew Blarg's phone bill with 2 phone calls\n" +
                "Phone call from 666-666-6666 to 777-777-7777 from 10/30/1983, 05:30 pm to 03/17/1984, 03:07 am\n" +
                "Phone call from 555-555-5555 to 444-444-4444 from 10/30/1983, 05:30 pm to 03/17/1984, 03:07 am\n";
    }

    @After
    public void tearDown() {
        File file = new File(data[10]);
        file.delete();      // Intentionally ignore result; we don't care if the file existed or not
        file = new File(data[12]);
        file.delete();      // Intentionally ignore result; we don't care if the file existed or not
        data = null;
        fileContents = null;
        badFileContents = null;
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
    public void Project1_Main_8ArgMissingTime_Error() {
        MainMethodResult result = invokeMain(data[0], data[1],data[2],data[3],data[4],data[5], data[13],data[6]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    // TODO: add file validation after implementation
    @Test
    public void Project1_Main_11ArgPrettyPrint_Success() {
        MainMethodResult result = invokeMain(data[11], data[12], data[1],data[2],data[3],data[4],data[5], data[13], data[6], data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), is(""));
    }

    @Test
    public void Project1_Main_10ArgPrettyMissingFileArg_Failure() {
        MainMethodResult result = invokeMain(data[11], data[1],data[2],data[3],data[4],data[5], data[13],data[6], data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_10ArgTextMissingFileArg_Failure() {
        MainMethodResult result = invokeMain(data[9], data[1],data[2],data[3],data[4],data[5], data[13],data[6], data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_9ArgNoOptions_Success() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));
    }

    @Test
    public void Project1_Main_8ArgNoOptionsForgotFirstAM_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_8ArgNoOptionsForgotSecondAM_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_9ArgNoOptionsBadBeginTime_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],"14:13", data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Error: Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project1_Main_9ArgNoOptionsBadEndTime_Failure() {
        MainMethodResult result = invokeMain( data[1],data[2],data[3],data[4],data[5], data[13],data[6],"05:77", data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Error: Incorrectly formatted end date/time."));
    }


    @Test
    public void Project1_Main_11ArgFileNameCustomerIsCustomer_Success() throws IOException {
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(new String(Files.readAllBytes(Paths.get(data[10]))),containsString(data[2]));
    }

    @Test
    public void Project1_Main_11ArgFileNameReadAndRewritten_Success() throws IOException {
        FileOutputStream out  = new FileOutputStream(data[10]);
        out.write(fileContents.getBytes());
        out.close();
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(new String(Files.readAllBytes(Paths.get(data[10]))),containsString("Phone call from 122-234-2343 to 133-333-3333 from 10/30/20, 5:30 AM to 3/17/21, 3:57 PM"));
    }

    @Test
    public void Project1_Main_11ArgFileNameReadAndRewrittenTwice_Success() throws IOException {
        FileOutputStream out  = new FileOutputStream(data[10]);
        out.write(fileContents.getBytes());
        out.close();
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));

        out  = new FileOutputStream(data[10]);
        out.write(fileContents.getBytes());
        out.close();
        result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));

        assertThat(new String(Files.readAllBytes(Paths.get(data[10]))),containsString("Phone call from 122-234-2343 to 133-333-3333 from 10/30/20, 5:30 AM to 3/17/21, 3:57 PM"));
    }

    @Test
    public void Project1_Main_11ArgFileNameCustomerMismatch_Failure() throws IOException {
        FileOutputStream out  = new FileOutputStream(data[10]);
        out.write(badFileContents.getBytes());
        out.close();
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Error: the indicated customer is not the customer in file"));
    }

    @Test
    public void Project1_Main_11ArgFileNameRead_Failure() throws IOException {
        FileOutputStream out  = new FileOutputStream(data[10]);
        out.write("monkey see monkey do".getBytes());
        out.close();
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Error: malformatted file "));
    }

    @Test
    public void Project1_Main_11ArgFileNameCallerIsCaller_Success() throws IOException {
        MainMethodResult result = invokeMain(data[9], data[10],data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(new String(Files.readAllBytes(Paths.get(data[10]))),containsString(data[1]));
    }

    @Test
    public void Project1_Main_10ArgPrint_Success() {
        MainMethodResult result = invokeMain(data[0], data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(null));
        assertThat(result.getTextWrittenToStandardOut(), containsString(data[2]));
    }

    @Test
    public void Project1_Main_11ArgREADME_Success() {
        MainMethodResult result = invokeMain(data[8], data[0], data[1], data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14] );
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project1_Main_10ArgPrint_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14],data[0]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_11ArgREADMEOutOfOrder_Failure() {
        MainMethodResult result = invokeMain(data[0], data[1], data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14], data[8]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_13ArgTextFile_Success() {
        MainMethodResult result = invokeMain(data[0], data[8], data[9], data[10], data[1], data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    // TODO: add file validation after implementation
    @Test
    public void Project1_Main_15ArgPretty_Success() {
        MainMethodResult result = invokeMain(data[0], data[8], data[9], data[10], data[11], data[12], data[1],
                data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("By Andrew Stevenson, for Advanced Programming"));
    }

    @Test
    public void Project1_Main_13ArgTextFileOutOfOrder_Failure() {
        MainMethodResult result = invokeMain(data[0], data[1], data[2],data[3],data[4],data[5], data[13],data[6],data[7], data[14], data[8], data[9], data[10]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing or incorrect command line arguments"));
    }

    @Test
    public void Project1_Main_BadCaller_Failure() {
        MainMethodResult result = invokeMain(data[1],"asdfasdf",data[3],data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted caller phone number."));
    }

    @Test
    public void Project1_Main_BadCallee_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],"asdfasdf",data[4],data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted callee phone number."));
    }

    @Test
    public void Project1_Main_BadBeginDate_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],"asdf",data[5], data[13],data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project1_Main_BadBeginTime_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],"50:30:25", "am", data[6],data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted begin date/time."));
    }

    @Test
    public void Project1_Main_BadEndDate_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5], data[13],"asdfasdf",data[7], data[14]);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted end date/time."));
    }

    @Test
    public void Project1_Main_BadEndTime_Failure() {
        MainMethodResult result = invokeMain(data[1],data[2],data[3],data[4],data[5], data[13],data[6],"asdfasdfasdf", "am");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Incorrectly formatted end date/time."));
    }

}