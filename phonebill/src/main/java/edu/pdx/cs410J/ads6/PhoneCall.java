package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneCall;

public class PhoneCall extends AbstractPhoneCall {

  final private String customer;
  final private String callerNumber;
  final private String calleeNumber;
  final private String start; // will become a date in later versions...
  final private String end;

  public PhoneCall(){
  customer = null;
  callerNumber=null;
  calleeNumber=null;
  start=null;
  end=null;
  }

  public PhoneCall(String[] s){
    customer = s[0];
    callerNumber = s[1];
    calleeNumber = s[2];
    start = s[3];
    end = s[4];
  }

  @Override
  public String getCaller() {
    return callerNumber;
  }

  @Override
  public String getCallee() {
    return calleeNumber;
  }

  @Override
  public String getStartTimeString() {
    return start;
  }

  @Override
  public String getEndTimeString() {
    return end;
  }
}
