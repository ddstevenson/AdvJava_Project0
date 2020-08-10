package edu.pdx.cs410j.ads6.phonebill2.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.pdx.cs410j.ads6.phonebill2.PhoneBill;
import edu.pdx.cs410j.ads6.phonebill2.PhoneCall;
import edu.pdx.cs410j.ads6.phonebill2.PrettyPrinter;
import edu.pdx.cs410j.ads6.phonebill2.R;

public class SearchFragment extends Fragment {
    private BillDataViewModel mViewModel;

    private EditText editCustomer;
    private EditText editBeginDate;
    private EditText editBeginTime;
    private EditText editEndDate;
    private EditText editEndTime;
    private Switch editBeginAM;
    private Switch editEndAM;
    private TextView prettyBillLabel;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editCustomer = view.findViewById(R.id.editCustomer3);
        editBeginDate = view.findViewById(R.id.editBeginDate3);
        editBeginTime = view.findViewById(R.id.editBeginTime3);
        editBeginAM = view.findViewById(R.id.editBeginAM3);
        editEndDate = view.findViewById(R.id.editEndDate3);
        editEndTime = view.findViewById(R.id.editEndTime3);
        editEndAM = view.findViewById(R.id.editEndAM3);
        prettyBillLabel = view.findViewById(R.id.prettyBillLabel2);
        mViewModel = new ViewModelProvider(requireActivity()).get(BillDataViewModel.class);
        final View v = view;

        mViewModel.getBills().observe(requireActivity(), new Observer<Map<String, PhoneBill>>() {
            @Override
            public void onChanged(Map<String,PhoneBill> s) { // Data has changed; but must verify form status

                if(editCustomer == null || editBeginDate == null || editBeginTime == null || editEndDate == null || editEndTime == null)
                    return;

                boolean isValid = isValidCustomer(editCustomer.getText().toString());
                isValid &= isValidDate(editBeginDate.getText().toString());
                isValid &= isValidTime(editBeginTime.getText().toString());
                isValid &= isValidTime(editEndDate.getText().toString());
                isValid &= isValidTime(editEndTime.getText().toString());

                if (isValid) {
                    String[] arr = new String[]{editCustomer.getText().toString().trim(),
                            "999-999-9999",
                            "999-999-9999",
                            editBeginDate.getText().toString().trim() + " " + editBeginTime.getText().toString().trim() + ((editBeginAM.isChecked()) ? (" AM") : (" PM")),
                            editEndDate.getText().toString().trim() + " " + editEndTime.getText().toString().trim() + ((editEndAM.isChecked()) ? (" AM") : (" PM"))};
                    PhoneCall call = new PhoneCall(arr);
                    if (call.getStartTime().compareTo(call.getEndTime()) <= 0) { // if end >= start
                        Map<String, PhoneBill> map = mViewModel.getBills().getValue();
                        PhoneBill bill = map.get(editCustomer.getText().toString());
                        String setval;
                        if (bill == null)
                            setval = "No results.";
                        else
                            setval = bill.toString(call.getStartTime(),call.getEndTime());
                        prettyBillLabel.setText(PrettyPrinter.prettify(setval));
                    }
                }
                return;
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
                if(!isValidCustomer(editCustomer.getText().toString())){
                    Snackbar.make(v, "Customer must be non-empty.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if(!isValidDate(editBeginDate.getText().toString())){
                    Snackbar.make(v, "Begin date must be of the form mm/dd/yyyy.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(!isValidTime(editBeginTime.getText().toString())){
                    Snackbar.make(v, "Begin time must be of the form hh:mm.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(!isValidDate(editEndDate.getText().toString().trim())){
                    Snackbar.make(v, "End date must be of the form mm/dd/yyyy.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(!isValidTime(editEndTime.getText().toString().trim())){
                    Snackbar.make(v, "End time must be of the form hh:mm.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }  else { // valid data? But still need to compare times
                    String [] arr = new String[]{editCustomer.getText().toString().trim(),
                            "999-999-9999",
                            "999-999-9999",
                            editBeginDate.getText().toString().trim() + " " + editBeginTime.getText().toString().trim() + ((editBeginAM.isChecked()) ? (" AM") : (" PM")),
                            editEndDate.getText().toString().trim() + " " + editEndTime.getText().toString().trim() + ((editEndAM.isChecked()) ? (" AM") : (" PM"))};
                    PhoneCall call = new PhoneCall(arr);
                    if (call.getStartTime().compareTo(call.getEndTime()) > 0) { // if start > end
                        Snackbar.make(v, "The call may not begin before it ends.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else { // the call is good; now we populate the pretty bill label
                        Map<String, PhoneBill> map = mViewModel.getBills().getValue();
                        PhoneBill bill = map.get(editCustomer.getText().toString());
                        String setval;
                        if(bill == null)
                            setval = "No results.";
                        else
                            setval = bill.toString(call.getStartTime(),call.getEndTime());
                        prettyBillLabel.setText(PrettyPrinter.prettify(setval));
                    }
                }
                return;
            }
        };
        editCustomer.addTextChangedListener(tw);
        editBeginDate.addTextChangedListener(tw);
        editBeginTime.addTextChangedListener(tw);
        editEndDate.addTextChangedListener(tw);
        editEndTime.addTextChangedListener(tw);

    }

    private boolean isValidCustomer(String s){
        if(s==null){
            return false;
        }
        return !("".equals(s.trim()));
    }

    private boolean isValidDate(String s){
        if(s==null){
            return false;
        }
        Pattern p = Pattern.compile("^(\\d{1,2})/(\\d{1,2})/(\\d{4})$");
        Matcher m = p.matcher(s.trim());
        if(!m.matches() || Integer.parseInt(m.group(1)) > 12 || Integer.parseInt(m.group(2)) > 32){
            return false;
        }
        return true;
    }

    private boolean isValidTime(String s){
        if(s==null){
            return false;
        }
        Pattern p = Pattern.compile("^(\\d{1,2}):(\\d{2})$");
        Matcher m = p.matcher(s);
        if(!m.matches() || Integer.parseInt(m.group(1)) > 12 || Integer.parseInt(m.group(2)) > 60){
            return false;
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}