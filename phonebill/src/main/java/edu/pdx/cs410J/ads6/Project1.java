package edu.pdx.cs410J.ads6;

/**
 * The main class for the CS410J Phone Bill Project
 */
public class Project1 {

  public static void main(String[] args) {
    IValidator val = new Validator(args);
    val.validate();
    PhoneCall call = new PhoneCall(args); // won't work - date strings need to be compacted
    PhoneBill bill = new PhoneBill();
    bill.addPhoneCall(call);
  }

}