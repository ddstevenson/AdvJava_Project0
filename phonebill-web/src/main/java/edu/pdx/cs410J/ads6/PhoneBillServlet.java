package edu.pdx.cs410J.ads6;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class PhoneBillServlet extends HttpServlet
{
    static final String CUSTOMER_PARAM = "customer";
    static final String CALLER_PARAM = "callerNumber";
    static final String CALLEE_PARAM = "calleeNumber";
    static final String START_PARAM = "start";
    static final String END_PARAM = "end";
    static final String CONTENT_TYPE = "text/plain";

    protected  Map<String, PhoneBill> billSet = null;       // Can't be final b/c init() isn't a constructor

    @Override
    public void init() throws ServletException{
        billSet = new HashMap<>();
    }


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
        response.setContentType( CONTENT_TYPE );
        PrintWriter pw = response.getWriter();
        PhoneBill bill;

        String name = getParameter( CUSTOMER_PARAM, request );
        String start = getParameter( START_PARAM, request );
        String end = getParameter( END_PARAM, request );
        Boolean bPretty = (start != null && end != null);

        if(name == null){
            missingRequiredParameter( response, CUSTOMER_PARAM );
        }else if (start == null ^ end == null){
            missingRequiredParameter( response, (start==null) ?  START_PARAM : END_PARAM);
        } else {

            if(billSet.containsKey(name)){
                bill = billSet.get(name);
            } else {
                bill = new PhoneBill(name);
            }

            if(bPretty){
                PhoneCall call = new PhoneCall(new String[]{"","999-999-9999","999-999-9999",start,end});   // Using PhoneCall to parse the input datetime strings
                // Error out if the calls are mis-ordered
                if(call.getStartTime().compareTo(call.getEndTime()) > 0){
                    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Malformatted or incorrect arguments in HTTP Request.");
                    pw.flush();
                    return;
                }
                PrettyPrinter printer = new PrettyPrinter();
                printer.filteredStreamDump(bill,call.getStartTime(),call.getEndTime(),pw);
            } else{
                pw.append(bill.toString());
                for (String s : bill.getPhoneCalls()){
                    pw.append(System.lineSeparator());
                    pw.append(s);
                }

            }

            response.setStatus( HttpServletResponse.SC_OK);
            pw.flush();
        }
        // Nothing should occur after final else: everything gets handled under its own if block
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
        response.setContentType( CONTENT_TYPE );
        PrintWriter pw = response.getWriter();
        PhoneCall call = null;
        PhoneBill bill = null;

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
        }else{

            if(billSet.containsKey(name)){
                bill = billSet.get(name);
            } else {
                bill = new PhoneBill(name);
            }

            try {
                call = new PhoneCall(args);
                 if(call.getStartTime().compareTo(call.getEndTime()) > 0){
                    throw new Exception();
                }
                bill.addPhoneCall(call);
                billSet.put(name, bill);
                response.setStatus( HttpServletResponse.SC_OK);
                pw.flush();
            } catch (Exception e){ // Probably bad args are responsible for exceptions
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Malformatted or incorrect arguments in HTTP Request.");
                pw.flush();
                return;
            }
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

        billSet = new HashMap<>();

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
}
