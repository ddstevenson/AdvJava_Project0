package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;

import java.util.*;

/**
 * Class representing an entire phone bill. To be expanded in future iterations.
 */
public class PhoneBill extends AbstractPhoneBill{
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
    @Override
    public String getCustomer() {
        return customer;
    }

    /**
     * @param abstractPhoneCall The <class>abstractPhoneCall</class> object to add to system.
     * @implNote abstractPhoneCall is added to the collection of calls
     */
    @Override
    public void addPhoneCall(AbstractPhoneCall abstractPhoneCall) {
        calls.add((PhoneCall) abstractPhoneCall);
    }

    /**
     * @return collection of strings representing the contents of the phone calls
     */
    @Override
    public Collection<String> getPhoneCalls() {
        Collections.sort(calls);
        Collection<String> retval = new LinkedList<>();
        for(AbstractPhoneCall c : calls){
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
}
