package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrettyPrinter extends TextDumper {
    final private String line1Match = "(.+)'s phone bill with .+ phone calls";
    final private String line2Match = "Phone call from (\\d{3}\\-\\d{3}\\-\\d{4}) to (\\d{3}\\-\\d{3}\\-\\d{4}) from (\\d{1,2}\\/\\d{2}\\/\\d{4} \\d{1,2}:\\d{2} [AaPp][Mm]) to (\\d{1,2}\\/\\d{2}\\/\\d{4} \\d{1,2}:\\d{2} [AaPp][Mm])";
    final private String[] cols = new String[]{"%10s","%20s","%20s","%25s","%25s","%10s"};
    final private String[] headers = new String[]{"No.", "Date", "Time", "Caller", "Callee", "Minutes"};

    /**
    ** @implNote this function must convert from text, back to phone call data. Inefficient.
    **/
    @Override
    public void dump(AbstractPhoneBill abstractPhoneBill) throws IOException {
        FileOutputStream out  = new FileOutputStream(this.filename);
        out.write(ConvertFirstLine(abstractPhoneBill.toString()).getBytes());
        ArrayList<String> l = new ArrayList<String>(abstractPhoneBill.getPhoneCalls());

        for (String s : l){
            out.write(ConvertSecondLine(s,l.indexOf(s)).getBytes());
        }
        out.close();
    }

    private String ConvertFirstLine(String s){
        Pattern p = Pattern.compile(line1Match);
        Matcher m = p.matcher(s);
        assert m.matches() : "Assert Failed: invalid string passed to ConvertFirstLine()";
        StringBuilder retval = new StringBuilder();
        retval.append("Customer: ").append(m.group(1)).append(System.lineSeparator()+System.lineSeparator());
        for(int x = 0; x < cols.length; ++x){
            retval.append(String.format(cols[x],headers[x]));
        }
        retval.append(System.lineSeparator());
        return retval.toString();
    }

    private String ConvertSecondLine(String s, int num){
        Pattern p = Pattern.compile(line2Match);
        Matcher m = p.matcher(s);
        assert m.matches() : "Assert Failed: invalid string passed to ConvertSecondLine()";
        String[] param = new String[]{"temp", m.group(1), m.group(2),
                m.group(3).replace(",",""), m.group(4).replace(",","")};
        PhoneCall call = new PhoneCall(param);
        DateFormat dformat = DateFormat.getDateInstance(DateFormat.LONG);
        DateFormat tformat = DateFormat.getTimeInstance(DateFormat.LONG);
        StringBuilder retval = new StringBuilder();
        retval.append(String.format(cols[0], Integer.toString(num)));
        retval.append(String.format(cols[1], dformat.format(call.getStartTime())));
        retval.append(String.format(cols[2], tformat.format(call.getStartTime())));
        retval.append(String.format(cols[3], m.group(1)));
        retval.append(String.format(cols[4], m.group(2)));
        retval.append(String.format(cols[5], (call.getEndTime().getTime() - call.getStartTime().getTime())/(1000*60) ));
        retval.append(System.lineSeparator());
        return retval.toString();
    }
}
