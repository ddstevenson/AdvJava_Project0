package edu.pdx.cs410J.ads6;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project2 {

  public static void main(String[] args) {
    IArgValidator val = new ArgValidator();
    String[] validated = val.validate(args);
    PhoneCall call = new PhoneCall(validated);
    PhoneBill bill = new PhoneBill();
    bill.addPhoneCall(call);
    if(validated[5].equals("true")){
      System.out.println(call.toString());
    }
    //no output specified if args are error-free
  }

}