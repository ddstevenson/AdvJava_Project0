package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;

import java.util.Collection;
import java.util.HashSet;

/**
 * Class representing an entire phone bill. To be expanded in future iterations.
 */
public class PhoneBill extends AbstractPhoneBill{
    private Collection<AbstractPhoneCall> calls;
    private String customer;

    public PhoneBill(){
        calls = new HashSet<AbstractPhoneCall>();
    }

    /**
     * @param c new customer id/name
     */
    public PhoneBill(String c){
        calls = new HashSet<AbstractPhoneCall>();
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
        calls.add(abstractPhoneCall);
    }

    /**
     * @return collection of strings representing the contents of the phone calls
     */
    @Override
    public Collection<String> getPhoneCalls() {
        Collection<String> retval = new HashSet<>();
        for(AbstractPhoneCall c : calls){
            retval.add(c.toString());
        }
        return retval;
    }
}
