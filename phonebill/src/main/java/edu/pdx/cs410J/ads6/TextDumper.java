package edu.pdx.cs410J.ads6;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.*;
import java.util.Collection;

public class TextDumper implements PhoneBillDumper {
    private String filename;

    public void setFilename(String s){
        filename = s;
    }

    public String getFilename(){
        return filename;
    }

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
