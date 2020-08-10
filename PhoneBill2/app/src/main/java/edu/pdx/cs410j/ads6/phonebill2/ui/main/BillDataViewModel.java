package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410j.ads6.phonebill2.PhoneBill;

public class BillDataViewModel extends ViewModel {
    private MutableLiveData<Map<String, PhoneBill>> bills;

    public void addBill(String s, PhoneBill bill){
        init();
        Map<String, PhoneBill> setval = bills.getValue();
        assert setval != null;
        setval.put(s,bill);
        bills.setValue(setval);
    }

    public LiveData<Map<String, PhoneBill>> getBills(){
        init();
        return bills;
    }

    private void init(){
        if (bills == null) {
            Map<String, PhoneBill> data = new HashMap<>();
            bills = new MutableLiveData<>(data);
        }
    }
}