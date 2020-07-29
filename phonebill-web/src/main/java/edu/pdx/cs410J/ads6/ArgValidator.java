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
     * @implSpec Check the args, sort them into a predictable order, and exit program w/ error if invalid
     * @param args The raw input from the command line as passed in to main().
     * @return String array with the following elements:
     * 1) Name
     * 2) Caller phone number
     * 3) Callee phone number
     * 4) Begin date and time
     * 5) End date and time
     * 6) "true" or "false" indicating whether -print option was specified
     * 7) "true" or "false" indicating whether -search option was specified
     * 8) host name, if supplied
     * 9) port number, if host name was specified
     * @implNote This method has multiple side effects, since it exits and prints to
     * stderr and stdout depending on the content of the command line args.
     */
    public String[]  validate(String[] args){
        String[] retval = new String[9];
        boolean bPrint = false;
        boolean bPort = false;
        boolean bHost = false;
        boolean bSearch = false;

        if (args == null)
            exit_error("Missing command line arguments.", true);

        // First we need to count & acknowledge which options were specified
        for(int x = 0; x < min(args.length,7); ++x){ //max total 7 option strings in args[]
            if(args[x] == null){
                break; // catch this outcome below
            } else if(args[x].equals("-README")){
                exit_readme();
            } else if (args[x].equals("-print")){
                bPrint = true;
            } else if (args[x].equals("-port")){
                bPort = true;
            } else if (args[x].equals("-host")){
                bHost = true;
            } else if (args[x].equals("-search")){
                bSearch = true;
            } else if ( !(x > 0 && (args[x - 1].equals("-port") || args[x - 1].equals("-host")))){
                break;  // once we hit something that's not an option, we're done
            }
        }

        if(bHost ^ bPort)
            exit_error("Either both host and port, or neither host nor port must be specified.", true);

        // Now that we know which options were specified, we can verify the correct number of args were submitted
        int num_opt_args = (bPrint) ? (1) : (0);
        num_opt_args += (bPort) ? (4) : (0);            // Both or neither, since we're checking xor above
        num_opt_args += (bSearch) ? (1) : (0);
        if(args.length != (9 + num_opt_args)) { // magic number: 1 + 1 + 1 + 3 +3 = 9
            exit_error("Missing or incorrect command line arguments.", true);
        }

        // Exactly right # of args are present, let's put the right args in the correct retval buckets
        for(int ret_index = 0, args_index = 0; ret_index < 5; ++args_index){
            if(args[args_index].equals("-print") ||
                    (args[args_index].equals("-host")) ||
                    (args[args_index].equals("-port")) ||
                    args[args_index].equals("-search")) {
                continue;
            }
            if(args_index != 0 && args[args_index - 1].equals("-port")){
                retval[8] = args[args_index];
            }  else if(args_index != 0 && args[args_index - 1].equals("-host")) {
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
        retval[5] = String.valueOf(bPrint);
        retval[6] = String.valueOf(bSearch);

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
                case 6: // boolean search value
                    // Do nothing
                    break;
                case 7: // host name
                    // Do nothing
                    break;
                case 8: // port number
                    // Do nothing
                    break;
                default: assert false: "Invalid index for retval in validate()";
            }
        }

        return retval;
    }

    /**
     * @param e error message to display on err.out
     * @param usage true if we will print usage info; false otherwise
     * @implSpec This function displays the supplied string and optionally prints usage information.
     */
    private void exit_error(String e, boolean usage){
        System.err.println(e);
        if(usage)
            exit_usage(1);
        else
            exit(1);
    }

    /**
     * Prints readme contents for user.
     */
    private void exit_readme(){
        System.out.println("Project 4" + System.lineSeparator() + 
                "By Andrew Stevenson, for Advanced Programming in Java at Portland State University." + System.lineSeparator() + 
                "\tA more complex command line utility that accepts as input the record of a single phone call. " + System.lineSeparator() +
                "\tData is received from a remote REST web service. " + System.lineSeparator() +
                "\tIf the input arguments are valid, no message is returned; otherwise appropriate usage and error information will be displayed." + System.lineSeparator() +
                "\tOptionally, this program may also reads and writes to a file where an individual's phone call records are maintained in the form of a phone bill." + System.lineSeparator() +
                "\tCall this program without command-line arguments to view usage instructions.");
        exit(0);
    }

    /**
     * Prints usage blurb to assist user in choosing correct arguments.
     */
    private void exit_usage(int code){
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
        exit(code);
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
