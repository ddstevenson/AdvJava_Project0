package edu.pdx.cs410J.ads6;


import edu.pdx.cs410J.web.HttpRequestHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Simple client for the RESTful web service exposed by PhoneBillServlet
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    private final String url;


    /**
     * Creates a client to the Phone Bil REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public PhoneBillRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }


    /**
     * @param name the name of the customer
     * @return a formatted phone bill containing the items in the customer's current bill
     * @throws IOException
     */
    public String getPhoneBill(String name) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("customer",name);
        Response response = get(this.url, map);
        throwExceptionIfNotOkayHttpStatus(response);
        PrettyPrinter printer = new PrettyPrinter();
        return printer.prettify(response.getContent());
    }


    /**
     * @param name Customer name
     * @param begin begin date to search for
     * @param end end date to seardh within
     * @return String of items in customer's bill that start between begin and end
     * @throws IOException
     */
    public String getPhoneBill(String name, String begin, String end) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("customer",name);
        map.put("start",begin);
        map.put("end",end);
        Response response = get(this.url, map);
        throwExceptionIfNotOkayHttpStatus(response);
        PrettyPrinter printer = new PrettyPrinter();
        return printer.prettify(response.getContent());
    }

    /**
     * @param customer name of the customer
     * @param call the call to add to the customer's bill
     * @throws IOException
     */
    public void addPhoneCall(String customer, PhoneCall call) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("customer", customer);
        map.put("callerNumber",call.getCaller());
        map.put("calleeNumber",call.getCallee());
        map.put("start",call.getStartTimeString());
        map.put("end",call.getEndTimeString());
        Response response = post(this.url, map);
        throwExceptionIfNotOkayHttpStatus(response);
    }

    /**
     * @throws IOException
     */
    public void removeAllPhoneCalls() throws IOException {
      Response response = delete(this.url, Map.of());
      throwExceptionIfNotOkayHttpStatus(response);
    }

    /**
     * @param response
     * @return
     */
    private Response throwExceptionIfNotOkayHttpStatus(Response response) {
      int code = response.getCode();
      if (code != HTTP_OK) {
        throw new IllegalArgumentException();
      }
      return response;
    }


}
