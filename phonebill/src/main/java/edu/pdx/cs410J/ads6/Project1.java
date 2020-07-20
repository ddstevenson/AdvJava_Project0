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
public class Project1 {

  public static void main(String[] args) {
    IArgValidator val = new ArgValidator();
    String[] validated = val.validate(args);
    PhoneCall call = new PhoneCall(validated);
    AbstractPhoneBill bill = new PhoneBill(validated[0]);

    // Check vs. the null case for the filename string
    boolean isFile = (validated[6] != null);
    isFile = (isFile) ? (!validated[6].isEmpty()) : (isFile);

    // read from file
    if(isFile && new File(validated[6]).isFile()){
      TextParser parser = new TextParser();
      parser.setFilename(validated[6]);
      try {
          bill = parser.parse(); // not yet implemented
      } catch(ParserException e){
        System.err.println("Error: malformatted file " + e.getMessage());
        exit(1);
      }
      if(!validated[0].equals(bill.getCustomer())){
        System.err.println("Error: the indicated customer is not the customer in file " + validated[6]);
        exit(1);
      }
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
        System.err.println("Error: unable to write to " + e.getMessage());
        exit(1);
      }
    }
    //no screen output specified if args are error-free
  }

}