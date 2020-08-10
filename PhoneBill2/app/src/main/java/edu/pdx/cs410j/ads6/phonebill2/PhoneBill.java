package edu.pdx.cs410j.ads6.phonebill2;


import java.util.*;

/**
 * Class representing an entire phone bill. To be expanded in future iterations.
 */
public class PhoneBill{
    private List<PhoneCall> calls;
    private String customer;

    public PhoneBill(){
        calls = new LinkedList<>();
    }

    /**
     * @param c new customer id/name
     */
    public PhoneBill(String c){
        calls = new LinkedList<>();
        customer = c;
    }

    /**
     * @return string equal to customer's id number or name (field usage is ambiguous.)
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * @param abstractPhoneCall The <class>abstractPhoneCall</class> object to add to system.
     * @implNote abstractPhoneCall is added to the collection of calls
     */
    public void addPhoneCall(PhoneCall abstractPhoneCall) {
        calls.add((PhoneCall) abstractPhoneCall);
    }

    /**
     * @return collection of strings representing the contents of the phone calls
     */
    public Collection<String> getPhoneCalls() {
        Collections.sort(calls);
        Collection<String> retval = new LinkedList<>();
        for(PhoneCall c : calls){
            retval.add(c.toString());
        }
        return retval;
    }

    /**
     * @param b begin time
     * @param e end time
     * @return list of all phone calls with start times between b and e, inclusive
     */
    public List<String> getPhoneCalls(Date b, Date e){
        Collections.sort(calls);
        List<String> retval = new LinkedList<>();
        for(PhoneCall c: calls){
            if(b.compareTo(c.getStartTime()) <= 0 && e.compareTo(c.getStartTime()) >= 0){
                //emit
                retval.add(c.toString());
            } // else do not emit
        }
        return retval;
    }

    @Override
    public String toString(){
        Collection<String> calls = getPhoneCalls();
        StringBuilder retval = new StringBuilder(getCustomer()+"'s phone bill with "+ calls.size() + " calls.");
        for (String line : calls){
            retval.append(System.lineSeparator());
            retval.append(line);
        }
        return retval.toString();
    }

    public String toString(Date b, Date e){
        Collection<String> calls = getPhoneCalls(b,e);
        StringBuilder retval = new StringBuilder(getCustomer()+"'s phone bill with "+ calls.size() + " calls.");
        for (String line : calls){
            retval.append(System.lineSeparator());
            retval.append(line);
        }
        return retval.toString();
    }
}
