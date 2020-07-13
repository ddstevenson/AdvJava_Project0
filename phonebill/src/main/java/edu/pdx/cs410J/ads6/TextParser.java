package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple class for reading a phone bill from a text file and reverse engineer
 * the toString() methods of Phonebill and Phonecall to
 */
public class TextParser implements PhoneBillParser {
    private String filename;

    /**
     * @param filename name of file, including extension if applicable
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return name of file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return PhoneBill object with contents of the indicated text file included; if empty, returns null
     * @throws ParserException in the event that the underlying text file is unreadable for any reason
     */
    @Override
    public AbstractPhoneBill parse() throws ParserException {
        java.util.List<String> lines;
        PhoneBill retval = null;
        final String line1Match = "(.+)'s phone bill with .+ phone calls";
        final String line2Match = "Phone call from ([2-9]\\d{2}\\-\\d{3}\\-\\d{4}) to ([2-9]\\d{2}\\-\\d{3}\\-\\d{4}) from (\\d{2}\\/\\d{2}\\/\\d{4} \\d{2}:\\d{2}) to (\\d{2}\\/\\d{2}\\/\\d{4} \\d{2}:\\d{2})";
        try {
            lines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            // file doesn't exist
            return null;
        }
        for(String line : lines){
            if(retval == null){     // line 1
                Pattern p = Pattern.compile(line1Match);
                Matcher m = p.matcher(line);
                if(!m.matches()){
                    throw new ParserException(filename);
                }
                retval = new PhoneBill(m.group(1));
            } else {                // lines 2-end
                Pattern p = Pattern.compile(line2Match);
                Matcher m = p.matcher(line);
                if(!m.matches()){
                    throw new ParserException(filename);
                }
                String[] param = new String[]{retval.getCustomer(), m.group(1), m.group(2), m.group(3), m.group(4)};
                PhoneCall call = new PhoneCall(param);
                retval.addPhoneCall(call);
            }
        }
        return retval;
    }
}
