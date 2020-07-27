package edu.pdx.cs410J.ads6;


import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration test that tests the REST calls made by {@link PhoneBillRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhoneBillRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");
  private PhoneBillRestClient client = null;

  private PhoneBillRestClient newPhoneBillRestClient() {
    int port = Integer.parseInt(PORT);
    return new PhoneBillRestClient(HOSTNAME, port);
  }

  private String[] data1, data2, data3;

  @Before
  public void setUp() throws IOException {
    this.data1 = new String[]{"Andrew","122-234-2343","133-333-3331","10/30/2020 05:30 AM",
            "3/17/2021 03:07 am"};
    this.data2 = new String[]{"Andrew","122-234-2343","133-333-3332","10/30/2020 06:30 AM",
            "3/17/2021 03:07 am"};
    this.data3 = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/2020 07:30 AM",
            "3/17/2021 03:07 am"};
    client = newPhoneBillRestClient();
    client.removeAllPhoneCalls();
  }

  @After
  public void tearDown() {
    data1 = null;
    data2 = null;
    data3 = null;
    client = null;
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_goodArgsSingleMatchPretty_Success() throws IOException {
    PhoneCall call = new PhoneCall(data1);
    PhoneBill bill = new PhoneBill(data1[0]);
    bill.addPhoneCall(call);
    client.addPhoneCall(data1[0], call);
    String s = client.getPhoneBill(data1[0], "10/30/2020 5:29 am", "10/30/2020 5:31 am");
    assertThat(s, containsString("Customer: Andrew"));
    assertThat(s, containsString(data1[2]));
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_goodArgsPartialMatchBottomPretty_Success() throws IOException {
    PhoneCall call1 = new PhoneCall(data1);
    PhoneCall call2 = new PhoneCall(data2);
    PhoneCall call3 = new PhoneCall(data3);
    client.addPhoneCall(data1[0], call1);
    client.addPhoneCall(data1[0], call2);
    client.addPhoneCall(data1[0], call3);
    String s = client.getPhoneBill(data1[0], "10/30/2020 5:29 am", "10/30/2020 6:31 am");
    assertThat(s, containsString("Customer: Andrew"));
    assertThat(s, containsString(data1[2]));
    assertThat(s, containsString(data2[2]));
    assertThat(s, not(containsString(data3[2])));
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_goodArgsPartialMatchTopPretty_Success() throws IOException {
    PhoneCall call1 = new PhoneCall(data1);
    PhoneCall call2 = new PhoneCall(data2);
    PhoneCall call3 = new PhoneCall(data3);
    client.addPhoneCall(data1[0], call1);
    client.addPhoneCall(data1[0], call2);
    client.addPhoneCall(data1[0], call3);
    String s = client.getPhoneBill(data1[0], "10/30/2020 6:29 am", "10/30/2020 9:31 am");
    assertThat(s, containsString("Customer: Andrew"));
    assertThat(s, not(containsString(data1[2])));
    assertThat(s, containsString(data2[2]));
    assertThat(s, containsString(data3[2]));
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_goodArgsFullMatchPretty_Success() throws IOException {
    PhoneCall call1 = new PhoneCall(data1);
    PhoneCall call2 = new PhoneCall(data2);
    PhoneCall call3 = new PhoneCall(data3);
    client.addPhoneCall(data1[0], call1);
    client.addPhoneCall(data1[0], call2);
    client.addPhoneCall(data1[0], call3);
    String s = client.getPhoneBill(data1[0], "10/30/2020 5:29 am", "10/30/2020 9:31 am");
    assertThat(s, containsString("Customer: Andrew"));
    assertThat(s, containsString(data1[2]));
    assertThat(s, containsString(data2[2]));
    assertThat(s, containsString(data3[2]));
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_goodArgsSingle_Success() throws IOException {
    PhoneCall call = new PhoneCall(data1);
    PhoneBill bill = new PhoneBill(data1[0]);
    bill.addPhoneCall(call);
    client.addPhoneCall(data1[0], call);
    String s = client.getPhoneBill(data1[0]);
    assertThat(s, containsString(bill.toString()));
    assertThat(s, containsString(call.toString()));
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_goodArgsMultiple_Success() throws IOException {
    PhoneCall call1 = new PhoneCall(data1);
    PhoneCall call2 = new PhoneCall(data2);
    PhoneCall call3 = new PhoneCall(data3);
    PhoneBill bill = new PhoneBill(data1[0]);
    bill.addPhoneCall(call1);
    bill.addPhoneCall(call2);
    bill.addPhoneCall(call3);
    client.addPhoneCall(data1[0], call1);
    client.addPhoneCall(data1[0], call2);
    client.addPhoneCall(data1[0], call3);
    String s = client.getPhoneBill(data1[0]);
    assertThat(s, containsString(bill.toString()));
    assertThat(s, containsString(call1.toString()));
    assertThat(s, containsString(call2.toString()));
    assertThat(s, containsString(call3.toString()));
  }

  @Test
  public void PhoneBillRestClient_getPhoneBill_noState_Success() throws IOException {
    PhoneBill bill = new PhoneBill(data1[0]);
    String s = client.getPhoneBill(data1[0]);
    assertThat(s, containsString(bill.toString()));
  }

  // The following were part of original test suite; they're being kept for future reference

  /*
  @Test
  public void test0RemoveAllDictionaryEntries() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    client.removeAllDictionaryEntries();
  }

  @Test
  public void test1EmptyServerContainsNoDictionaryEntries() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    Map<String, String> dictionary = client.getAllDictionaryEntries();
    assertThat(dictionary.size(), equalTo(0));
  }

  @Test
  public void test2DefineOneWord() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    String testWord = "TEST WORD";
    String testDefinition = "TEST DEFINITION";
    client.addDictionaryEntry(testWord, testDefinition);

    String definition = client.getDefinition(testWord);
    assertThat(definition, equalTo(testDefinition));
  }

  @Test
  public void test4MissingRequiredParameterReturnsPreconditionFailed() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    HttpRequestHelper.Response response = client.postToMyURL(Map.of());
    assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("word")));
    assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
  }

   */

}
