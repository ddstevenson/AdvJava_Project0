package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

import java.io.IOException;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project2 {

  public static void main(String[] args) {
    IArgValidator val = new ArgValidator();
    String[] validated = val.validate(args);
    PhoneCall call = new PhoneCall(validated);
    AbstractPhoneBill bill = new PhoneBill(validated[0]);
    AbstractPhoneBill bill2;

    // Check vs. the null case for the filename string
    boolean isFile = (validated[6] != null);
    isFile = (isFile) ? (!validated[6].isEmpty()) : (isFile);

    if(isFile){
      TextParser parser = new TextParser();
      parser.setFilename(validated[6]);
      try {
          bill2 = parser.parse(); // not yet implemented
      } catch(ParserException e){
        // TODO: add exception handling
      }
    }
    bill.addPhoneCall(call);

    if(validated[5].equals("true")){
      System.out.println(call.toString());
    }

    if(isFile){
      TextDumper dumper = new TextDumper();
      dumper.setFilename(validated[6]);
      try {
        dumper.dump(bill);
      } catch(IOException e){
        System.err.println("Error: " + e.getMessage());
      }
    }
    //no screen output specified if args are error-free
  }

}