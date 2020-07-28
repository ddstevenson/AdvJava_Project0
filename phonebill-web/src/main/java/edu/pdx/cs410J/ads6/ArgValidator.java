package edu.pdx.cs410J.ads6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.min;
import static java.lang.System.exit;

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
     * 7) name of the file to be read from / written to if -textFile specified; empty otherwise
     * 8) name of the file to be pretty printed to if -pretty option specified; empty otherwise
     * @implNote This method has multiple side effects, since it exits and prints to
     * stderr and stdout depending on the content of the command line args.
     */
    public String[]  validate(String[] args){
        String[] retval = new String[8];
        boolean bPrint = false;
        boolean bWrite = false;
        boolean bPretty = false;

        if (args == null){
            System.err.println("Missing or incorrect command line arguments.");
            print_usage();
            exit(1);
        }

        for(int x = 0; x < min(args.length,6); ++x){ //max total 6 option strings in args[]
            if(args[x] == null){
                break; // catch this outcome below
            } else if(args[x].equals("-README")){
                print_readme();
                exit(0);
            } else if (args[x].equals("-print")){
                bPrint = true;
            } else if (args[x].equals("-textFile")){
                bWrite = true;
            } else if (args[x].equals("-pretty")){
                bPretty = true;
            } else if ( !(x > 0 && (args[x - 1].equals("-textFile") || args[x - 1].equals("-pretty")))){
                break;  // once we hit something that's not an option, we're done
            }
        }

        //ensure correct # of args
        int num_opt_args = (bPrint) ? (1) : (0);
        num_opt_args += (bWrite) ? (2) : (0);
        num_opt_args += (bPretty) ? (2) : (0);
        if(args.length != (9 + num_opt_args)) { // magic number: 1 + 1 + 1 + 3 +3 = 9
            System.err.println("Missing or incorrect command line arguments.");
            print_usage();
            exit(1);
        }

        //put the right args in the correct retval buckets
        for(int ret_index = 0, args_index = 0; ret_index < 5; ++args_index){
            if(args[args_index].equals("-print") ||
                    args[args_index].equals("-textFile") ||
                    args[args_index].equals("-pretty")) {
                continue;
            }
            if(args_index != 0 && args[args_index - 1].equals("-textFile")){
                retval[6] = args[args_index];
            }  else if(args_index != 0 && args[args_index - 1].equals("-pretty")) {
                retval[7] = args[args_index];
            } else if(ret_index == 3 || ret_index == 4){ // compile dates & times into single field
                if(retval[ret_index] == null){  // field is empty; set to date field
                    retval[ret_index]=args[args_index];
                } else if(args[args_index].length() > 2){ // process time field
                    retval[ret_index] += (" " + args[args_index]);
                } else { // process am/pm
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
            Pattern p;
            Matcher m;
            switch(ret_index){
                case 0: // name
                    // nothing to do here
                    break;
                case 1: // caller
                    if(!pattern_match("^\\d{3}-\\d{3}-\\d{4}$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted caller phone number.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: ddd-ddd-dddd");
                        exit(1);
                    }
                    break;
                case 2: // callee
                    if(!pattern_match("^\\d{3}-\\d{3}-\\d{4}$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted callee phone number.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: ddd-ddd-dddd");
                        exit(1);
                    }
                    break;
                case 3: // begin date/time string
                    p = Pattern.compile("^(\\d{1,2})/(\\d{1,2})/(\\d{4}) (\\d{1,2}):(\\d{2}) ([AaPp][Mm])$");
                    m = p.matcher(retval[ret_index]);
                    if(!m.matches() || Integer.valueOf(m.group(4)) > 11 || Integer.valueOf(m.group(5)) > 60){
                        System.err.println("Error: Incorrectly formatted begin date/time.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: mm/dd/yyyy hh:mm am");
                        exit(1);
                    }
                    break;
                case 4: // end date/time string
                    p = Pattern.compile("^(\\d{1,2})/(\\d{1,2})/(\\d{4}) (\\d{1,2}):(\\d{2}) ([AaPp][Mm])$");
                    m = p.matcher(retval[ret_index]);
                    if(!m.matches() || Integer.valueOf(m.group(4)) > 11 || Integer.valueOf(m.group(5)) > 60){
                        System.err.println("Error: Incorrectly formatted end date/time.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: mm/dd/yyyy hh:mm am");
                        exit(1);
                    }
                    break;
                case 5: // boolean print value
                    // Nothing to do here
                    break;
                case 6: // Filename, if applicable
                    // Do nothing
                    break;
                case 7: // Pretty filename, if applicable
                    // Do nothing
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
        System.out.println("Project 4" + System.lineSeparator() + 
                "By Andrew Stevenson, for Advanced Programming in Java at Portland State University." + System.lineSeparator() + 
                "\tA more complex command line utility that accepts as input the record of a single phone call. " + System.lineSeparator() +
                "\tData is received from a remote REST web service. " + System.lineSeparator() +
                "\tIf the input arguments are valid, no message is returned; otherwise appropriate usage and error information will be displayed." + System.lineSeparator() +
                "\tOptionally, this program may also reads and writes to a file where an individual's phone call records are maintained in the form of a phone bill." + System.lineSeparator() +
                "\tCall this program without command-line arguments to view usage instructions.");
    }

    /**
     * Prints usage blurb to assist user in choosing correct arguments.
     */
    private void print_usage(){
        System.out.println("usage: java edu.pdx.cs410J.ads6.Project4 [options] <args>" + System.lineSeparator() + 
                "\\targs are (in this order):" + System.lineSeparator() + 
                "\\t\\tcustomer \\tPerson whose phone bill we’re modeling" + System.lineSeparator() + 
                "\\t\\tcallerNumber \\tPhone number of caller" + System.lineSeparator() + 
                "\\t\\tcalleeNumber \\tPhone number of person who was called" + System.lineSeparator() + 
                "\\t\\tstart \\tDate and time call began" + System.lineSeparator() + 
                "\\tend \\tDate and time call ended" + System.lineSeparator() + 
                "\\toptions are (options may appear in any order):" + System.lineSeparator() + 
                "\\t\\t-host \\thostname Host computer on which the server runs" + System.lineSeparator() + 
                "\\t\\t-port \\tport Port on which the server is listening" + System.lineSeparator() + 
                "\\t\\t-search \\tPhone calls should be searched for" + System.lineSeparator() + 
                "\\t\\t-print \\tPrints a description of the new phone call" + System.lineSeparator() + 
                "\\t\\t-README \\tPrints a README for this project and exits");
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
