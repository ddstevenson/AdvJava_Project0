package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;
import static java.lang.System.exit;

import java.io.File;
import java.io.IOException;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project3 {

  public static void main(String[] args) {
    IArgValidator val = new ArgValidator();
    String[] validated = val.validate(args);
    PhoneCall call = new PhoneCall(validated);
    AbstractPhoneBill bill = new PhoneBill(validated[0]);

    // Check vs. the null case for the filename string
    boolean isFile = (validated[6] != null);
    isFile = (isFile) ? (!validated[6].isEmpty()) : (isFile);
    boolean isPretty = (validated[7] != null);
    isPretty = (isPretty) ? (!validated[7].isEmpty()) : (isPretty);

    // read from file
    if(isFile && new File(validated[6]).isFile()){
      TextParser parser = new TextParser();
      parser.setFilename(validated[6]);
      try {
          bill = parser.parse();
      } catch(ParserException e){
        System.err.println("Error: malformatted file " + e.getMessage());
        exit(1);
      }
      if(!validated[0].equals(bill.getCustomer())){
        System.err.println("Error: the indicated customer is not the customer in file " + validated[6]);
        exit(1);
      }
    }
    if(call.getStartTime().compareTo(call.getEndTime()) > 0){ // if start > end
      System.err.println("Error: the start time is before the end time.");
      System.err.println("Start Time: " + call.getStartTimeString());
      System.err.println("End Time: " + call.getEndTimeString());
      exit(1);
    }
    bill.addPhoneCall(call);

    // check for -print
    if(validated[5].equals("true")){
      System.out.println(call.toString());
    }

    // write to file
    if(isFile){
      TextDumper dumper = new TextDumper();
      dumper.setFilename(validated[6]);
      try {
        dumper.dump(bill);
      } catch(IOException e){
        System.err.println("Error: unable to write to text file " + e.getMessage());
        exit(1);
      }
    }

    // write to pretty file
    if(isPretty){
      PrettyPrinter dumper = new PrettyPrinter();
      dumper.setFilename(validated[7]);
      try {
        dumper.dump(bill);
      } catch(IOException e){
        System.err.println("Error: unable to write to pretty file " + e.getMessage());
        exit(1);
      }
    }
    //no screen output specified if args are error-free
  }

}