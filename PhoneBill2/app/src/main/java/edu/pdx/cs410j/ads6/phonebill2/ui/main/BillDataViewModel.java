package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BillDataViewModel extends ViewModel {
    private MutableLiveData<String> names;

    public void setName(String s){
        init();
        names.setValue(s);
    }

    public LiveData<String> getName(){
        init();
        return names;
    }

    private void init(){
        if (names == null) {
            names = new MutableLiveData<>("");
        }
    }
}