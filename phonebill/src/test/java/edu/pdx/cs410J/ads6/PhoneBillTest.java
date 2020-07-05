package edu.pdx.cs410J.ads6;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit test suite for the {@link PhoneBill} class.
 */
public class PhoneBillTest {

    @Test
    public void PhoneBill_getCustomer_IsNull_True(){
        PhoneBill bill = new PhoneBill();
        assertSame(bill.getCustomer(),null);
    }

    @Test
    public void PhoneBill_getPhoneCalls_IsNull_True(){
        PhoneBill bill = new PhoneBill();
        assertSame(bill.getPhoneCalls(),null);
    }
}
