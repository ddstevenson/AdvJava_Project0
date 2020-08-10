package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import edu.pdx.cs410j.ads6.phonebill2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCallFragment extends Fragment {

    BillDataViewModel mViewModel;

    private EditText editTextTextPersonName;
    private TextView label;
    private TextWatcher tw;

    private static final String VIEW_MODEL_STATE = "AboutFragment";

    public NewCallFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment AboutFragment.
     */
    public static NewCallFragment newInstance(String param1, String param2) {
        NewCallFragment fragment = new NewCallFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(BillDataViewModel.class);
        if(savedInstanceState != null)
            mViewModel.setName(savedInstanceState.get(VIEW_MODEL_STATE).toString());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putString(VIEW_MODEL_STATE,"test");
    }

    @Override
    public void onPause() {

        super.onPause();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextTextPersonName = view.findViewById(R.id.editCustomer);
        //label = view.findViewById(R.id.DetailLabel);
        //label.setText("blah blah blah");
        mViewModel = new ViewModelProvider(requireActivity()).get(BillDataViewModel.class);
        tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //label.setText(s.toString());
                mViewModel.setName(s.toString());
            }
        };

        editTextTextPersonName.addTextChangedListener(tw);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null)
            mViewModel.setName(savedInstanceState.get(VIEW_MODEL_STATE).toString());
        return inflater.inflate(R.layout.fragment_new_call, container, false);
    }
}