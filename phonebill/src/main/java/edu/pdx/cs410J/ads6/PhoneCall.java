package edu.pdx.cs410J.ads6;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import edu.pdx.cs410J.AbstractPhoneCall;
/**
 * PhoneCall class: stores and retrieves information about an individual phone call record.
 */
public class PhoneCall extends AbstractPhoneCall implements java.lang.Comparable<PhoneCall>{

  final private String customer;
  final private String callerNumber;
  final private String calleeNumber;
  private Date start;
  private Date end;

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
  public PhoneCall(String[] s) {
    customer = s[0];
    callerNumber = s[1];
    calleeNumber = s[2];
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    try {
      start = format.parse(s[3]);
      end = format.parse(s[4]);
    } catch (ParseException p){
      assert false : "Error: Date strings passed to PhoneCall should be pre-validated.\nStart Time: " + s[3] + "\nEnd Time: " + s[4];
    }
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
    if(start==null){
      return null;
    }
    DateFormat tformat = DateFormat.getTimeInstance(DateFormat.SHORT);
    DateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
    return dformat.format(start)+ " " + tformat.format(start);
  }

  /**
   * @return Returns date and time the phone call ended in the following format:
   * mm/dd/yyyy hh:mm
   */
  @Override
  public String getEndTimeString() {
    if(end==null){
      return null;
    }
    DateFormat tformat = DateFormat.getTimeInstance(DateFormat.SHORT);
    DateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
    return dformat.format(end)+ " " + tformat.format(end);
  }

  @Override
  public java.util.Date getStartTime(){
    return start;
  }

  @Override
  public java.util.Date getEndTime(){
    return end;
  }

  @Override
  public int compareTo(PhoneCall o) {
    int st = this.start.compareTo(o.start);
    return (st != 0) ? st : this.end.compareTo(o.end);
  }
}
