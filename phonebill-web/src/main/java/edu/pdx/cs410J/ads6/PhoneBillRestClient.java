package edu.pdx.cs410J.ads6;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
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


    public String getPhoneBill(String name) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("customer",name);
        Response response = get(this.url, map);
        throwExceptionIfNotOkayHttpStatus(response);
        return response.getContent();
    }


    public String getPhoneBill(String name, String begin, String end) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("customer",name);
        map.put("start",begin);
        map.put("end",end);
        Response response = get(this.url, map);
        throwExceptionIfNotOkayHttpStatus(response);
        return response.getContent();
    }

    public String addPhoneCall(String customer, PhoneCall call) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("customer", customer);
        map.put("callerNumber",call.getCaller());
        map.put("calleeNumber",call.getCallee());
        map.put("start",call.getStartTimeString());
        map.put("end",call.getEndTimeString());
        Response response = post(this.url, map);
        throwExceptionIfNotOkayHttpStatus(response);
        return response.getContent();
    }

    /**
     * Returns all dictionary entries from the server
     */
    public Map<String, String> getAllDictionaryEntries() throws IOException {
      Response response = get(this.url, Map.of());
      return Messages.parseDictionary(response.getContent());
    }

    /**
     * Returns the definition for the given word
     */
    public String getDefinition(String word) throws IOException {
      Response response = get(this.url, Map.of("word", word));
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      return Messages.parseDictionaryEntry(content).getValue();
    }

    public void addDictionaryEntry(String word, String definition) throws IOException {
      Response response = postToMyURL(Map.of("word", word, "definition", definition));
      throwExceptionIfNotOkayHttpStatus(response);
    }

    @VisibleForTesting
    Response postToMyURL(Map<String, String> dictionaryEntries) throws IOException {
      return post(this.url, dictionaryEntries);
    }

    public void removeAllPhoneCalls() throws IOException {
      Response response = delete(this.url, Map.of());
      throwExceptionIfNotOkayHttpStatus(response);
    }

    private Response throwExceptionIfNotOkayHttpStatus(Response response) {
      int code = response.getCode();
      if (code != HTTP_OK) {
        throw new PhoneBillRestException(code);
      }
      return response;
    }

    @VisibleForTesting
    class PhoneBillRestException extends RuntimeException {
      PhoneBillRestException(int httpStatusCode) {
        super("Got an HTTP Status Code of " + httpStatusCode);
      }
    }

}
