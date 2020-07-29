package edu.pdx.cs410J.ads6;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link PhoneBillServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhoneBillServletTest {
  private String[] data1, data2, data3;
  private PhoneBillServlet servlet = null;

  @Before
  public void setUp() throws ServletException {
    this.data1 = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/2020 5:30 am",
            "3/17/2021 03:07 am"};
    this.data2 = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/2020 6:30 am",
            "3/17/2021 03:07 am"};
    this.data3 = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/2020 7:30 am",
            "3/17/2021 03:07 am"};
    servlet = new PhoneBillServlet();
    servlet.init();
  }

  @After
  public void tearDown() {
    data1 = null;
    data2 = null;
    data3 = null;
    servlet = null;
  }

  @Test
  public void PhoneBillServlet_doGet_DatesWrongOrder_Failure() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);
    PhoneBill bill = new PhoneBill(data1[0]);
    bill.addPhoneCall(new PhoneCall(data1));
    bill.addPhoneCall(new PhoneCall(data2));
    bill.addPhoneCall(new PhoneCall(data3));

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn("10/30/1975 3:40 pm");
    // Missing Arg!
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn("10/30/1975 3:00 pm");
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(false);
    when(session.getAttribute(data1[0])).thenReturn(bill);

    servlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Malformatted or incorrect arguments in HTTP Request.");
  }

  @Test
  public void PhoneBillServlet_doGet_MissingDateArg_Failure() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);
    PhoneBill bill = new PhoneBill(data1[0]);
    bill.addPhoneCall(new PhoneCall(data1));
    bill.addPhoneCall(new PhoneCall(data2));
    bill.addPhoneCall(new PhoneCall(data3));

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn("10/30/1975 3:40 pm");
    // Missing Arg!
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(null);
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(false);
    when(session.getAttribute(data1[0])).thenReturn(bill);

    servlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The required parameter \"end\" is missing");
  }

  @Test
  public void PhoneBillServlet_doGet_goodArgsFullMatchPretty_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    addPhoneCallToServlet(new PhoneCall(data1));
    addPhoneCallToServlet(new PhoneCall(data2));
    addPhoneCallToServlet(new PhoneCall(data3));

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn("10/30/1975 3:40 pm");
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn("10/30/2021 6:30 am");

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(pw,times(7)).append(anyString()); // 3 calls + the header = 4
  }

  private void addPhoneCallToServlet(PhoneCall call) throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(call.getCaller());
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(call.getCallee());
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(call.getStartTimeString());
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(call.getEndTimeString());
    servlet.doPost(request, response);
  }

  @Test
  public void PhoneBillServlet_doGet_goodArgsPartialMatchPretty_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    addPhoneCallToServlet(new PhoneCall(data1));
    addPhoneCallToServlet(new PhoneCall(data2));
    addPhoneCallToServlet(new PhoneCall(data3));

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn("10/30/1975 3:40 pm");
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn("10/30/2020 6:30 am");

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(pw,times(5)).append(anyString()); // 2 calls plus the header = 3
  }

  @Test
  public void PhoneBillServlet_doGet_goodArgsNoMatchPretty_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    addPhoneCallToServlet(new PhoneCall(data1));
    addPhoneCallToServlet(new PhoneCall(data2));
    addPhoneCallToServlet(new PhoneCall(data3));

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn("10/30/1975 3:40 pm");
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn("10/30/1975 3:50 pm");

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(pw).append(not(contains("133-333-3333")));
    verify(pw).append(contains("Andrew's phone bill with 3 phone calls"));
  }

  @Test
  public void PhoneBillServlet_doGet_badArgsNoNameNoDates_Failure() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(null);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(null);
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(null);
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(true);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The required parameter \"customer\" is missing");
  }

  @Test
  public void PhoneBillServlet_doGet_goodArgsOldSessionNoDates_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    PhoneBill bill = new PhoneBill(data1[0]);
    PhoneCall call = new PhoneCall(data1);
    bill.addPhoneCall(call);    // for comparison below
    addPhoneCallToServlet(call);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(pw).append(contains(bill.toString()));
    verify(pw).append(contains(call.toString()));
  }

  @Test
  public void PhoneBillServlet_doGet_goodArgsNewSessionNoDates_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);
    PhoneBill bill = new PhoneBill(data1[0]);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(true);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(pw).append(bill.toString());
  }

  @Test
  public void PhoneBillServlet_doPost_MalformattedArg_Failure() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(data1[1]);
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(data1[2]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn("10/30/20 5:30");
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(data1[4]);
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(true);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Malformatted or incorrect arguments in HTTP Request.");
  }

  @Test
  public void PhoneBillServlet_doPost_MissingArg_Failure() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(data1[1]);
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(data1[2]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(data1[3]);
    // Missing arg!
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(null);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The required parameter \"end\" is missing");
  }

  @Test
  public void PhoneBillServlet_doPost_goodArgsNewSession_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(data1[1]);
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(data1[2]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(data1[3]);
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(data1[4]);
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(true);

    servlet.doPost(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
  }


  @Test
  public void PhoneBillServlet_doPost_goodArgsOldSession_Success() throws ServletException, IOException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);
    PhoneBill bill = new PhoneBill();

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data1[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(data1[1]);
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(data1[2]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(data1[3]);
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(data1[4]);
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(false);
    when(session.getAttribute(data1[0])).thenReturn(bill);

    servlet.doPost(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  // This is the original sample test that shipped with the code; it worked originally, but now
  // Exists only for my reference.
/*
  @Test
  public void initiallyServletContainsNoDictionaryEntries() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    int expectedWords = 0;
    verify(pw).println(Messages.formatWordCount(expectedWords));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }




  @Test
  public void addOneWordToDictionary() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    String word = "TEST WORD";
    String definition = "TEST DEFINITION";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("word")).thenReturn(word);
    when(request.getParameter("definition")).thenReturn(definition);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), equalTo(Messages.definedWordAs(word, definition) + System.lineSeparator()));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    assertThat(servlet.getDefinition(word), equalTo(definition));
  }
*/
}
