package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import edu.pdx.cs410j.ads6.phonebill2.PhoneBill;
import edu.pdx.cs410j.ads6.phonebill2.PrettyPrinter;
import edu.pdx.cs410j.ads6.phonebill2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrettyBillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrettyBillFragment extends Fragment {

    BillDataViewModel mViewModel;
    EditText editCustomer2;
    TextView prettyBillLabel;

    public PrettyBillFragment() {
        // Required empty public constructor
    }

    public static PrettyBillFragment newInstance() {
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
        editCustomer2 = view.findViewById(R.id.editCustomer2);
        prettyBillLabel = view.findViewById(R.id.prettyBillLabel);
        mViewModel = new ViewModelProvider(requireActivity()).get(BillDataViewModel.class);

        mViewModel.getBills().observe(requireActivity(), new Observer<Map<String,PhoneBill>>() {
            @Override
            public void onChanged(Map<String,PhoneBill> s) {
                PhoneBill bill = s.get(editCustomer2.getText().toString());
                if (bill == null)
                    prettyBillLabel.setText("No results.");
                else
                    prettyBillLabel.setText(PrettyPrinter.prettify(bill.toString()));
            }

        });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Map<String, PhoneBill> map = mViewModel.getBills().getValue();
                PhoneBill bill = map.get(editCustomer2.getText().toString());
                String setval;
                if(bill == null)
                    setval = "No results.";
                else
                    setval = bill.toString();
                prettyBillLabel.setText(PrettyPrinter.prettify(setval));
            }
        };

        editCustomer2.addTextChangedListener(tw);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View retval = inflater.inflate(R.layout.fragment_pretty_bill, container, false);
        return retval;
    }
}