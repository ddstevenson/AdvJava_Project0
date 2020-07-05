package edu.pdx.cs410J.ads6;

import org.junit.*;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneBillTest {

    @Test
    public void PhoneBill_PhoneBill_getCustomer_null(){
        PhoneBill bill = new PhoneBill();
        assertSame(bill.getCustomer(),null);
    }

    @Test
    public void PhoneBill_PhoneBill_getPhoneCalls_null(){
        PhoneBill bill = new PhoneBill();
        assertSame(bill.getPhoneCalls(),null);
    }
}
