package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class AboutFragmentViewModel extends ViewModel {
    private MutableLiveData<List<String>> names;
    // TODO: Implement the ViewModel
    public LiveData<List<String>> getNames() {
        if (names == null) {
            names = new MutableLiveData<List<String>>();
            loadNames();
        }
        return names;
    }

    private void loadNames() {
        // from where?
    }
}