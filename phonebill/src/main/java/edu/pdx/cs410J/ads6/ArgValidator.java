package edu.pdx.cs410J.ads6;

import static java.lang.System.exit;
import java.util.regex.*;

/**
 * Simple class with one method to parse the input from the command line.
 */
public class ArgValidator implements IArgValidator{

    /**
     * @param args The raw input from the command line as passed in to main().
     * @return String array with the following elements:
     * 1) Name
     * 2) Caller phone number
     * 3) Callee phone number
     * 4) Begin date and time
     * 5) End date and time
     * 6) "true" or "false" indicating whether -print option was specified
     * @implNote This method has multiple side effects, since it exits and prints to
     * stderr and stdout depending on the content of the command line args.
     */
    public String[]  validate(String[] args){
        String[] retval = new String[6];
        boolean bPrint = false;
        boolean bWrite = false;

        if (args == null){
            System.err.println("Missing or incorrect command line arguments.");
            print_usage();
            exit(1);
        }

        for(int x = 0; x < 4; ++x){ //max total 4 option strings in args[]
            if(args.length == 0){
                break; // catch this outcome below
            } else if(args[x].equals("-README")){
                print_readme();
                exit(0);
            } else if (args[x].equals("-print")){
                bPrint = true;
            } else if (args[x].equals("-textFile")){
                bWrite = true;
            } else if ( !(x > 0 && args[x - 1].equals("-textFile"))){
                break;  // once we hit something that's not an option, we're done
            }
        }

        //ensure correct # of args
        int num_opt_args = (bPrint) ? (1) : (0);
        num_opt_args += (bWrite) ? (2) : (0);
        if(args.length != (7 + num_opt_args)) {
            System.err.println("Missing or incorrect command line arguments.");
            print_usage();
            exit(1);
        }

        //put the right args in the correct retval buckets
        for(int ret_index = 0, args_index = 0; ret_index < 5; ++args_index){
            if(args[args_index].equals("-print") ||
                    args[args_index].equals("-textFile") ||
                    (args_index != 0 && args[args_index - 1].equals("-textFile"))) {
                continue;
            }
            if(ret_index == 3 || ret_index == 4){ // compile dates & times into single field
                if(retval[ret_index] == null){
                    retval[ret_index]=args[args_index];
                } else {
                    retval[ret_index] += (" " + args[args_index]);
                    ++ret_index;
                }
            } else {
                retval[ret_index]=args[args_index];
                ++ret_index;
            }
        }
        retval[5] = String.valueOf(bPrint); // print command must be dispatched from main()

        //Finally validate the contents of the arguments
        for(int ret_index = 0; ret_index < retval.length; ++ret_index){
            switch(ret_index){
                case 0: // name
                    // nothing to do here
                    break;
                case 1: // caller
                    if(!pattern_match("^[2-9]\\d{2}-\\d{3}-\\d{4}$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted caller phone number.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: ddd-ddd-dddd");
                        exit(1);
                    }
                    break;
                case 2: // callee
                    if(!pattern_match("^[2-9]\\d{2}-\\d{3}-\\d{4}$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted callee phone number.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: ddd-ddd-dddd");
                        exit(1);
                    }
                    break;
                case 3: // begin date/time string
                    if(!pattern_match("^(\\d{2})/(\\d{2})/(\\d{4}) (\\d{2}):(\\d{2})$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted begin date/time.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: mm/dd/yyyy hh:mm");
                        exit(1);
                    }
                    break;
                case 4: // end date/time string
                    if(!pattern_match("^(\\d{2})/(\\d{2})/(\\d{4}) (\\d{2}):(\\d{2})$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted end date/time.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: mm/dd/yyyy hh:mm");
                        exit(1);
                    }
                    break;
                case 5: // boolean print value
                    // Nothing to do here
                    break;
                default: assert false: "Invalid index for retval in validate()";
            }
        }

        return retval;
    }

    /**
     * Prints readme contents for user.
     */
    private void print_readme(){
        System.out.println("Project 2\n" +
                "By Andrew Stevenson, for Advanced Programming in Java at Portland State University.\n" +
                "A simple command line parser that accepts as input the record of a single phone call. \n" +
                "If the input arguments are valid, no message is returned; otherwise appropriate usage and error information will be displayed.\n" +
                "Optionally, this program may also reads and writes to a file where an individual's phone call records are maintained in the form of a phone bill." +
                "Call this program without command-line arguments to view usage instructions.");
    }

    /**
     * Prints usage blurb to assist user in choosing correct arguments.
     */
    private void print_usage(){
        System.out.println("usage: java edu.pdx.cs410J.<login-id>.Project1 [options] <args>\n" +
                "\targs are (in this order):\n" +
                "\t\tcustomer Person whose phone bill we’re modeling\n" +
                "\t\tcallerNumber Phone number of caller\n" +
                "\t\tcalleeNumber Phone number of person who was called\n" +
                "\t\tstart Date and time call began (24-hour time)\n" +
                "\t\tend Date and time call ended (24-hour time)\n" +
                "\toptions are (options may appear in any order):\n" +
                "\t\t-textFile file Where to read/write the phone bill\n" +
                "\t\t-print Prints a description of the new phone call\n" +
                "\t\t-README Prints a README for this project and exits\n" +
                "\tDate and time should be in the format: mm/dd/yyyy hh:mm\n");
    }

    /**
     * Returns true if the pattern has a match within target; false otherwise.
     * @param pattern regex expression to match
     * @param target string within to search for a match
     * @return boolean indicating whether match was found
     */
    private boolean pattern_match(String pattern, String target){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(target);
        return m.matches();
    }

}

/**
 * Simple contract to keep validator class methods in check.
 */
interface IArgValidator{
    /**
     * @param args Raw input args from the command line call.
     * @return Implementation-dependent parsed array of strings.
     */
    String[] validate(String[] args);
}
