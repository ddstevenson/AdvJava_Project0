package edu.pdx.cs410j.ads6.phonebill2;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.cs410j.ads6.phonebill2.ui.main.BillDataViewModel;
import edu.pdx.cs410j.ads6.phonebill2.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private File directory;
    private File file;
    final private String FILENAME = "saved.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        directory = getFilesDir();
        file = new File(directory, FILENAME);
        getState();
    }

    @Override
    public void onResume() {
        getState();
        super.onResume();
    }

    @Override
    public void onPause() {
        putState();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        putState();
        super.onDestroy();
    }

    private void getState(){
        BillDataViewModel vm = new ViewModelProvider(this).get(BillDataViewModel.class);
        HashMap<String, PhoneBill> in;

        if(!file.exists()){
            return;
        }
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            in = (HashMap<String, PhoneBill>) ois.readObject();
            ois.close();
            fis.close();
        } catch( Exception e){
            return;
        }
        vm.addMap(in);
        file.delete();
    }

    private void putState(){
        BillDataViewModel vm = new ViewModelProvider(this).get(BillDataViewModel.class);
        if(!vm.hasState()){
            return;
        }
        HashMap<String, PhoneBill> map = (HashMap<String, PhoneBill>) vm.getBills().getValue();
        file.delete();
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
        } catch (Exception e){
            return;
        }
    }

}