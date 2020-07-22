package edu.pdx.cs410J.ads6;

import org.junit.*;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link PhoneCall} class.
 */
public class PhoneCallTest {
  private String[] data;

  @Before
  public void setUp() {
    data = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/2020 5:30 am",
            "3/17/2021 03:07 am"};
  }

  @After
  public void tearDown() {
    data = null;
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
  public void PhoneCall_compareTo_BeginTimeALessThanB_True() {
    PhoneCall call = new PhoneCall(data);
    PhoneCall call2 = new PhoneCall(new String[]{data[0],data[1],data[2],"10/30/2020 5:36 am",data[4]});
    assertTrue(call.compareTo(call2) < 0);
  }

  @Test
  public void PhoneCall_compareTo_BeginTimeAGreaterThanB_True() {
    PhoneCall call = new PhoneCall(data);
    PhoneCall call2 = new PhoneCall(new String[]{data[0],data[1],data[2],"10/30/2020 5:36 am",data[4]});
    assertTrue(call2.compareTo(call) > 0);
  }

  @Test
  public void PhoneCall_compareTo_EndTimeALessThanB_True() {
    PhoneCall call = new PhoneCall(data);
    PhoneCall call2 = new PhoneCall(new String[]{data[0],data[1],data[2],data[3],"03/17/2021 03:27 am"});
    assertTrue(call.compareTo(call2) < 0);
  }

  @Test
  public void PhoneCall_compareTo_EndTimeAGreaterThanB_True() {
    PhoneCall call = new PhoneCall(data);
    PhoneCall call2 = new PhoneCall(new String[]{data[0],data[1],data[2],data[3],"03/17/2021 03:27 am"});
    assertTrue(call2.compareTo(call) > 0);
  }

  @Test
  public void PhoneCall_compareTo_BothTimesAEqualsB_True() {
    PhoneCall call = new PhoneCall(data);
    PhoneCall call2 = new PhoneCall(data);
    assertTrue(call2.compareTo(call) == 0);
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
    assertThat(call.getStartTimeString(), containsString("10/30/2020 5:30 AM"));
  }

  @Test
  public void PhoneCall_getEndTime_nullIsNull_True() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getEndTimeString(), is(nullValue()));
  }

  @Test
  public void PhoneCall_getEndTime_StringIsString_True() {
    PhoneCall call = new PhoneCall(data);
    assertThat(call.getEndTimeString(), containsString("03/17/2021 3:07 AM"));
  }
}
