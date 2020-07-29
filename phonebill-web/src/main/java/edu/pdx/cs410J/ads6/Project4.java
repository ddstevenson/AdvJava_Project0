package edu.pdx.cs410J.ads6;

import static java.lang.System.exit;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    public static void main(String... args) {
        IArgValidator val = new ArgValidator();
        String[] validated = val.validate(args);
        PhoneCall call = new PhoneCall(validated);

        // This validation isn't done in Validator
        // because we don't convert the strings to
        // dates until PhoneCall is instantiated
        if(call.getStartTime().compareTo(call.getEndTime()) > 0){ // if start > end
            System.err.println("Error: the start time is before the end time.");
            System.err.println("Start Time: " + call.getStartTimeString());
            System.err.println("End Time: " + call.getEndTimeString());
            exit(1);
        }

        PhoneBillRestClient client;
        if(!isPresent(validated[7]))
            client = new PhoneBillRestClient("localhost", 8080);
        else
            client = new PhoneBillRestClient(validated[7],Integer.parseInt(validated[8]));

        boolean isEntry = isPresent(validated[0]);
        isEntry &= isPresent(validated[1]);
        isEntry &= isPresent(validated[2]);
        isEntry &= isPresent(validated[3]);
        isEntry &= isPresent(validated[4]);
        try {
            if (isEntry) {  // new phone bill is being added
                client.addPhoneCall(validated[0], call);
                if(str2bln(validated[5]))       // we only use the -print arg if new call
                    System.out.println(call.toString());
            } else { // searching for matching records
                if(!isPresent(validated[3])){   // If there's no date, search by customer only
                    System.out.print(client.getPhoneBill(validated[0]));
                } else {    // Otherwise, we'll use the date-boxed search
                    System.out.print(client.getPhoneBill(validated[0], validated[3], validated[4]));
                }
            }
        }catch (Exception e){ // assume any io error is a problem with the host
            System.err.println("Error: the host \"" + validated[7] + ":" + validated[8] + " could not be reached.");
            exit(1);
        }
    }

    private static boolean isPresent(String str){
        return str != null && !str.equals("");
    }

    private static Boolean str2bln(String str){
        if(str == null)
            return false;
        if(str.equals(""))
            return false;
        if(!(str.equals("true") || str.equals("false")))
            return false;
        return Boolean.parseBoolean(str);
    }

}