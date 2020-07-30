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
        int num_opt = 0;
        int num_args = 0;
        boolean bPrint = false;
        boolean bPort = false;
        boolean bHost = false;
        boolean bSearch = false;

        if (args == null)
            exit_error("Missing command line arguments.", true);

        // First we need to count & acknowledge which options & args were specified
        for(int x = 0; x < args.length; ++x){ //max total 7 option strings in args[]
            if(args[x] == null){
                exit_error("Null command line arguments.", true);
            } else if(args[x].equals("-README")){
                exit_readme();
            } else if (args[x].equals("-print")){
                if(num_args != 0)
                    exit_error("Options must precede the arguments.", true);
                bPrint = true;
                ++num_opt;
            } else if (args[x].equals("-port")){
                if(num_args != 0)
                    exit_error("Options must precede the arguments.", true);
                bPort = true;
                ++num_opt;
            } else if (args[x].equals("-host")){
                if(num_args != 0)
                    exit_error("Options must precede the arguments.", true);
                bHost = true;
                ++num_opt;
            } else if (args[x].equals("-search")){
                if(num_args != 0)
                    exit_error("Options must precede the arguments.", true);
                bSearch = true;
                ++num_opt;
            } else if ((x > 0 && (args[x - 1].equals("-port") || args[x - 1].equals("-host")))){
                if(num_args != 0)
                    exit_error("Options must precede the arguments.", true);
                ++num_opt;
            } else {
                ++num_args;
            }
        }

        if(bHost ^ bPort)
            exit_error("Either both host and port, or neither host nor port must be specified.", true);

        // magic number 1: 1 + 1 + 1 + 3 +3 = 9
        // magic number 2: 1 + 3 + 3 = 7
        // magic number 3: 1 = 1 (name only)
        boolean isValid = (num_args == 1 && !bSearch || num_args == 9 || (num_args == 7 && bSearch));
        if(!isValid) {
            exit_error("Missing or incorrect command line arguments.", true);
        }

        // Probably correct # of args are present, let's put them in retval buckets
        for(int args_index = 0; args_index < args.length; ++args_index){
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
            } else { // only on first arg should this be triggered; we'll take care of everything at once
                retval[0] = args[args_index];
                if(num_args == 9){
                    retval[1] = args[args_index+1]; // caller
                    retval[2] = args[args_index+2]; // calle3

                    retval[3] = args[args_index+3] + " " + args[args_index+4] + " " +  args[args_index+5]; // caller
                    retval[4] = args[args_index+6] +  " " + args[args_index+7] +  " " + args[args_index+8]; // calle3
                } else if(num_args == 7){
                    retval[3] = args[args_index+1] + " " +  args[args_index+2] +  " " + args[args_index+3]; // caller
                    retval[4] = args[args_index+4] +  " " + args[args_index+5] + " " +  args[args_index+6]; // calle3
                } else {
                    assert num_args == 1 : "Assert failed: invalid arguments in ArgValidator.";
                }
                break;
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
                    if(retval[ret_index] == null)
                        exit_error("Error: customer name is required.", true);
                    break;
                case 1: // caller
                    if(retval[ret_index] == null)
                        continue;
                    if(!pattern_match("^\\d{3}-\\d{3}-\\d{4}$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted caller phone number.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: ddd-ddd-dddd");
                        exit(1);
                    }
                    break;
                case 2: // callee
                    if(retval[ret_index] == null)
                        continue;
                    if(!pattern_match("^\\d{3}-\\d{3}-\\d{4}$",retval[ret_index])){
                        System.err.println("Error: Incorrectly formatted callee phone number.");
                        System.err.println("Was: " + retval[ret_index]);
                        System.err.println("Expected: ddd-ddd-dddd");
                        exit(1);
                    }
                    break;
                case 3: // begin date/time string
                    if(retval[ret_index] == null)
                        continue;
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
                    if(retval[ret_index] == null)
                        continue;
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
                    try {
                        if(retval[ret_index] != null && !retval[ret_index].equals(""))
                            Integer.parseInt(retval[ret_index]);
                    } catch(Exception e) {
                        exit_error("Port number must be a numeric value.", true);
                    }
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
                "\targs are (in this order):" + System.lineSeparator() + 
                "\t\tcustomer \t\tPerson whose phone bill we’re modeling" + System.lineSeparator() +
                "\t\tcallerNumber \tPhone number of caller" + System.lineSeparator() + 
                "\t\tcalleeNumber \tPhone number of person who was called" + System.lineSeparator() + 
                "\t\tstart \t\t\tDate and time call began" + System.lineSeparator() +
                "\t\tend \t\t\tDate and time call ended" + System.lineSeparator() +
                "\toptions are (options may appear in any order):" + System.lineSeparator() + 
                "\t\t-host \t\t\thostname Host computer on which the server runs" + System.lineSeparator() +
                "\t\t-port \t\t\tport Port on which the server is listening" + System.lineSeparator() +
                "\t\t-search \t\tPhone calls should be searched for" + System.lineSeparator() +
                "\t\t-print \t\t\tPrints a description of the new phone call" + System.lineSeparator() +
                "\t\t-README \t\tPrints a README for this project and exits");
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
