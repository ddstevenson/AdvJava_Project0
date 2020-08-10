package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.pdx.cs410j.ads6.phonebill2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrettyBillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrettyBillFragment extends Fragment {

    BillDataViewModel mViewModel;

    public PrettyBillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrettyBillFragment newInstance(String param1, String param2) {
        PrettyBillFragment fragment = new PrettyBillFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //final TextView label = view.findViewById(R.id.DetailLabel1);
        // label.setText("This is set from onViewCreated().");
        mViewModel = new ViewModelProvider(requireActivity()).get(BillDataViewModel.class);

        mViewModel.getName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
               //label.setText(s);
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retval = inflater.inflate(R.layout.fragment_pretty_bill, container, false);
        return retval;
    }
}