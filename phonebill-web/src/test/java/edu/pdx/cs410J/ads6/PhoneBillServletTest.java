package edu.pdx.cs410J.ads6;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link PhoneBillServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class PhoneBillServletTest {
  private String[] data;

  @Before
  public void setUp() {
    this.data = new String[]{"Andrew","122-234-2343","133-333-3333","10/30/20 5:30 am",
            "3/17/21 03:07 am"};
  }

  @After
  public void tearDown() {
    data = null;
  }

  @Test
  public void PhoneBillServlet_doPost_goodArgsNewSession_Success() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(data[1]);
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(data[2]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(data[3]);
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(data[4]);
    when(request.getContextPath()).thenReturn("/calls");
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(true);

    servlet.doPost(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(session).setAttribute(anyString(), anyObject());
  }


  @Test
  public void PhoneBillServlet_doPost_goodArgsOldSession_Success() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    PrintWriter pw = mock(PrintWriter.class);
    PhoneBill bill = new PhoneBill();

    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter(PhoneBillServlet.CUSTOMER_PARAM)).thenReturn(data[0]);
    when(request.getParameter(PhoneBillServlet.CALLER_PARAM)).thenReturn(data[1]);
    when(request.getParameter(PhoneBillServlet.CALLEE_PARAM)).thenReturn(data[2]);
    when(request.getParameter(PhoneBillServlet.START_PARAM)).thenReturn(data[3]);
    when(request.getParameter(PhoneBillServlet.END_PARAM)).thenReturn(data[4]);
    when(request.getContextPath()).thenReturn("/calls");
    when(request.getSession()).thenReturn(session);

    when(session.isNew()).thenReturn(false);
    when(session.getAttribute(PhoneBillServlet.SESSION_ATTRIB)).thenReturn(bill);

    servlet.doPost(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(session).setAttribute(anyString(), anyObject());
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
