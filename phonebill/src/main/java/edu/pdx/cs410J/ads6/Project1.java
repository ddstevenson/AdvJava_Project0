package edu.pdx.cs410J.ads6;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project1 {

  public static void main(String[] args) {
    IArgValidator val = new ArgValidator();
    String[] validated = val.validate(args);
    PhoneCall call = new PhoneCall(validated);
    PhoneBill bill = new PhoneBill();
    bill.addPhoneCall(call);

    //no output specified if args are error-free
    return;
  }

}