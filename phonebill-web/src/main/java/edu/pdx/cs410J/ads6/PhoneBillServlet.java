package edu.pdx.cs410J.ads6;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class PhoneBillServlet extends HttpServlet
{
    static final String WORD_PARAMETER = "word";
    public static final String SESSION_ATTRIB = "PhoneBill";
    public static final String CUSTOMER_PARAM = "customer";
    public static final String CALLER_PARAM = "callerNumber";
    public static final String CALLEE_PARAM = "calleeNumber";
    public static final String START_PARAM = "start";
    public static final String END_PARAM = "end";

    private final Map<String, String> dictionary = new HashMap<>();


    /**
     * @param request the HttpServletRequest object passed in to the servlet
     * @param response the HttpServletResponse object passed in to the servlet
     * @throws ServletException
     * @throws IOException
     * @implSpec customer=name -> GET returns all calls in the phone bill formatted using the TextDumper.
     * customer = name, start=startDateTime, end=endDateTime -> GET returns all of given phone bill’s
     * calls (in the format used by TextDumper) that occurred between the start date/time and the end date/time.
     * The date/time format in the URL is the same as on the command line.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String word = getParameter( WORD_PARAMETER, request );
        if (word != null) {
            writeDefinition(word, response);

        } else {
            writeAllDictionaryEntries(response);
        }
    }


    /**
     * @param request the HttpServletRequest object passed in to the servlet
     * @param response the HttpServletResponse object passed in to the servlet
     * @throws ServletException
     * @throws IOException
     * @implSpec POST creates a new call from the HTTP request parameters customer, callerNumber,
     * calleeNumber, start, and end. If the phone bill does not exist, a new one should be
     * created.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession();
        PhoneCall call;
        PhoneBill bill;

        String name = getParameter( CUSTOMER_PARAM, request );
        String caller = getParameter( CALLER_PARAM, request );
        String callee = getParameter( CALLEE_PARAM, request );
        String start = getParameter( START_PARAM, request );
        String end = getParameter( END_PARAM, request );
        String[] args = new String[]{name, caller, callee, start ,end};

        if(name == null){
            missingRequiredParameter( response, CUSTOMER_PARAM );
        }else if(caller == null){
            missingRequiredParameter( response, CALLER_PARAM );
        }else if(callee == null){
            missingRequiredParameter( response, CALLEE_PARAM );
        }else if(start == null){
            missingRequiredParameter( response, START_PARAM );
        }else if(end == null){
            missingRequiredParameter( response, END_PARAM );
        }else if(request.getContextPath().equals("/calls")){ // Including b/c we don't know what scoring web.xml file will be

            if(session.isNew()){
                bill = new PhoneBill();
            } else {
                bill = (PhoneBill) session.getAttribute(SESSION_ATTRIB);
            }

            try {
                call = new PhoneCall(args);
                bill.addPhoneCall(call);
            } catch (Exception e){ // Probably bad args are responsible for exceptions
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Malformatted arguments in HTTP Request.");
                pw.flush();
                return;
            }

            session.setAttribute(SESSION_ATTRIB, bill);
            response.setStatus( HttpServletResponse.SC_OK);
            pw.flush();
        } else {
            response.sendError( HttpServletResponse.SC_NOT_FOUND,"The specified resource was not found.");
            pw.flush();
        }
        // Nothing should occur after final else: everything gets handled under its own if block
    }


    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @implSpec No longer deletes all sessions--only the current user's data is removed now, as per a logout
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        request.getSession().invalidate();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allDictionaryEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the definition of the given word to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatDictionaryEntry(String, String)}
     */
    private void writeDefinition(String word, HttpServletResponse response) throws IOException {
        String definition = this.dictionary.get(word);

        if (definition == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } else {
            PrintWriter pw = response.getWriter();
            pw.println(Messages.formatDictionaryEntry(word, definition));

            pw.flush();

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Writes all of the dictionary entries to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatDictionaryEntry(String, String)}
     */
    private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        Messages.formatDictionaryEntries(pw, dictionary);

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    @VisibleForTesting
    String getDefinition(String word) {
        return this.dictionary.get(word);
    }

}
