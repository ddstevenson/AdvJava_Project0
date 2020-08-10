package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.pdx.cs410j.ads6.phonebill2.R;

public class ReadmeFragment extends Fragment {
    public static ReadmeFragment newInstance() {
        ReadmeFragment fragment = new ReadmeFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_readme, container, false);
    }
}