package edu.pdx.cs410J.ads6;

import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test suite for the {@link PhoneBill} class.
 */
public class PhoneBillTest {
    private String[] data;

    @Before
    public void setUp() {
        data = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/20 5:30 am",
                "3/17/21 03:07 am"};
    }

    @After
    public void tearDown() {
        data = null;
    }
    @Test
    public void PhoneBill_getCustomer_IsNull_True(){
        PhoneBill bill = new PhoneBill();
        assertSame(bill.getCustomer(),null);
    }

    @Test
    public void PhoneBill_getPhoneCalls_IsEmpty_True(){
        PhoneBill bill = new PhoneBill();
        assertTrue(bill.getPhoneCalls().isEmpty());
    }

    @Test
    public void PhoneBill_getPhoneCalls_IsSorted_True(){
        PhoneCall call1 = new PhoneCall(data);
        PhoneCall call2 = new PhoneCall(new String[]{data[0],data[1],data[2],"10/30/20 5:36 am",data[4]});
        PhoneCall call3 = new PhoneCall(new String[]{data[0],data[1],data[2],data[3],"3/17/21 03:27 am"});
        PhoneBill bill = new PhoneBill();
        bill.addPhoneCall(call3);
        bill.addPhoneCall(call1);
        bill.addPhoneCall(call2);
        ArrayList<String> l = new ArrayList(bill.getPhoneCalls());

        //correct order will be: 1,3,2
        assertThat(l.get(0), containsString(call1.toString()));
        assertThat(l.get(1), containsString(call3.toString()));
        assertThat(l.get(2), containsString(call2.toString()));
    }
}
