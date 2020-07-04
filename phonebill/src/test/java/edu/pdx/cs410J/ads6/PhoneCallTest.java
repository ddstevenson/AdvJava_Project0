package edu.pdx.cs410J.ads6;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link PhoneCall} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class PhoneCallTest {
  private String[] data;

  @Before
  public void setUp() {
    data = new String[]{"Andrew","322-234-2343","333-333-3333","10/30/20 5:30",
            "3/17/21 23:67"};
  }
  @Test
  public void PhoneCall_getCaller_nullIsNull_True() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getCaller(), is(nullValue()));
  }

  @Test
  public void PhoneCall_getCaller_StringIsString_True() {
    PhoneCall call = new PhoneCall(data);
    assertSame(call.getCaller(), data[1]);
  }

  @Test
  public void PhoneCall_getCallee_nullIsNull_True() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getCallee(), is(nullValue()));
  }

  @Test
  public void PhoneCall_getCallee_StringIsString_True() {
    PhoneCall call = new PhoneCall(data);
    assertSame(call.getCallee(), data[2]);
  }

  @Test
  public void PhoneCall_getStartTimeString_nullIsNull_True() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getStartTimeString(), is(nullValue()));
  }

  @Test
  public void PhoneCall_getStartTimeString_StringIsString_True() {
    PhoneCall call = new PhoneCall(data);
    assertSame(call.getStartTimeString(), data[3]);
  }

  @Test
  public void PhoneCall_getEndTime_nullIsNull_True() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getEndTimeString(), is(nullValue()));
  }

  @Test
  public void PhoneCall_getEndTime_StringIsString_True() {
    PhoneCall call = new PhoneCall(data);
    assertSame(call.getEndTimeString(), data[4]);
  }
}
