package edu.pdx.cs410j.ads6.phonebill2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PhoneCall class: stores and retrieves information about an individual phone call record.
 */
public class PhoneCall implements Comparable<PhoneCall>{

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
    callerNumber = s[1];
    calleeNumber = s[2];
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    try {
      start = format.parse(s[3]);
      end = format.parse(s[4]);
    } catch (ParseException p){
      //assert false : "Error: Date strings passed to PhoneCall should be pre-validated.\nStart Time: " + s[3] + "\nEnd Time: " + s[4];
      throw new IllegalArgumentException("A date string passed to PhoneCall(String) was not pre-validated.\nStart Time: " + s[3] + "\nEnd Time: " + s[4]);
    }
  }

  /**
   * @return Returns the phone number of the caller.
   */
  public String getCaller() {
    return callerNumber;
  }

  /**
   * @return Returns the phone number of the callee.
   */
  public String getCallee() {
    return calleeNumber;
  }

  /**
   * @return Returns date and time the phone call started in the following format:
   * mm/dd/yyyy hh:mm
   */
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
  public String getEndTimeString() {
    if(end==null){
      return null;
    }
    DateFormat tformat = DateFormat.getTimeInstance(DateFormat.SHORT);
    DateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
    return dformat.format(end)+ " " + tformat.format(end);
  }

  /**
   * @return begin time
   */
  public Date getStartTime(){
    return start;
  }

  /**
   * @return end time
   */
  public Date getEndTime(){
    return end;
  }

  /**
   * @param o the right operand to which to compare this
   * @return negative if o is greater; 0 if same; positive otherwise
   *  decide first based on begin date; if same, then switch to end date
   */
  @Override
  public int compareTo(PhoneCall o) {
    int st = this.start.compareTo(o.start);
    return (st != 0) ? st : this.end.compareTo(o.end);
  }
}
