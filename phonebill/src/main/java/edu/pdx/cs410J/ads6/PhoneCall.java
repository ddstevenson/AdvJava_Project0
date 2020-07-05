package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneCall;
/**
 * PhoneCall class: stores and retrieves information about an individual phone call record.
 */
public class PhoneCall extends AbstractPhoneCall {
  /**
   * these fields could have been a string array, but I think we'll have to break
   * out some of these fields to other types during future iterations
   */
  final private String customer;
  final private String callerNumber;
  final private String calleeNumber;
  final private String start; // will become a date in later versions...
  final private String end;

  /**
   * Default constructor
   * @return all fields are set to null
   * @implNote these fields are final, so the default constructor will rarely be the best choice for a user
   */
  public PhoneCall(){
  customer = null;
  callerNumber=null;
  calleeNumber=null;
  start=null;
  end=null;
  }

  /**
   * Preferred constructor
   * @param s array of strings in the following format:
   * 1) Name
   * 2) Caller phone number
   * 3) Callee phone number
   * 4) Begin date and time
   * 5) End date and time
   * 6) "true" or "false" indicating whether -print option was specified NOT USED
   */
  public PhoneCall(String[] s){
    customer = s[0];
    callerNumber = s[1];
    calleeNumber = s[2];
    start = s[3];
    end = s[4];
  }

  /**
   * @return Returns the phone number of the caller.
   */
  @Override
  public String getCaller() {
    return callerNumber;
  }

  /**
   * @return Returns the phone number of the callee.
   */
  @Override
  public String getCallee() {
    return calleeNumber;
  }

  /**
   * @return Returns date and time the phone call started in the following format:
   * mm/dd/yyyy hh:mm
   */
  @Override
  public String getStartTimeString() {
    return start;
  }

  /**
   * @return Returns date and time the phone call ended in the following format:
   * mm/dd/yyyy hh:mm
   */
  @Override
  public String getEndTimeString() {
    return end;
  }
}
