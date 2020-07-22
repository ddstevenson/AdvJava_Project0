package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.*;
import java.util.Collection;

/**
 * Writes a phonebill object to the indicated text file
 */
public class TextDumper implements PhoneBillDumper {
    protected String filename;

    /**
     * @param s name of the file to dump to
     */
    public void setFilename(String s){
        filename = s;
    }

    /**
     * @return name of the file to dump to
     */
    public String getFilename(){
        return filename;
    }

    /**
     * @param abstractPhoneBill the name of the abstractPhoneBill object to dump to a file
     * @throws IOException in the event the file cannot be accessed
     */
    @Override
    public void dump(AbstractPhoneBill abstractPhoneBill) throws IOException {
        FileOutputStream out  = new FileOutputStream(this.filename);
        out.write(abstractPhoneBill.toString().getBytes());
        out.write(System.lineSeparator().getBytes());
        for (String s : (Collection<String>) abstractPhoneBill.getPhoneCalls()){
            out.write(s.getBytes());
            out.write(System.lineSeparator().getBytes());
        }
        out.close();
    }

}
