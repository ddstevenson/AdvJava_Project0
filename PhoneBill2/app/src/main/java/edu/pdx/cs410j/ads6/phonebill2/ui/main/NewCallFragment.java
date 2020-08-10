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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.pdx.cs410j.ads6.phonebill2.PhoneBill;
import edu.pdx.cs410j.ads6.phonebill2.PhoneCall;
import edu.pdx.cs410j.ads6.phonebill2.R;

import static java.lang.System.exit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCallFragment extends Fragment {

    private BillDataViewModel mViewModel;

    private EditText editCustomer;
    private EditText editCaller;
    private EditText editCallee;
    private EditText editBeginDate;
    private EditText editBeginTime;
    private EditText editEndDate;
    private EditText editEndTime;
    private Switch editBeginAM;
    private Switch editEndAM;

    private Button submitButton;
    private Button cancelButton;

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
        // if(savedInstanceState != null)
            // mViewModel.setName(savedInstanceState.get(VIEW_MODEL_STATE).toString());
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

        editCustomer = view.findViewById(R.id.editCustomer);
        editCaller = view.findViewById(R.id.editCaller);
        editCallee = view.findViewById(R.id.editCallee);
        editBeginDate = view.findViewById(R.id.editBeginDate);
        editBeginTime = view.findViewById(R.id.editBeginTime);
        editBeginAM = view.findViewById(R.id.editBeginAM);
        editEndDate = view.findViewById(R.id.editEndDate);
        editEndTime = view.findViewById(R.id.editEndTime);
        editEndAM = view.findViewById(R.id.editEndAM);
        submitButton = view.findViewById(R.id.submitButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        mViewModel = new ViewModelProvider(requireActivity()).get(BillDataViewModel.class);

        // Implement the SUBMIT button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!isValidCustomer(editCustomer.getText().toString())){
                    Snackbar.make(v, "Customer must be non-empty.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if(!isValidPhone(editCaller.getText().toString().trim())){
                    Snackbar.make(v, "Caller phone number must contain exactly ten digits.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(!isValidPhone(editCallee.getText().toString().trim())){
                    Snackbar.make(v, "Callee phone number must contain exactly ten digits.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(!isValidDate(editBeginDate.getText().toString().trim())){
                    Snackbar.make(v, "Begin date must be of the form mm/dd/yyyy.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if(!isValidTime(editBeginTime.getText().toString().trim())){
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
                            editCaller.getText().toString().trim(),
                            editCallee.getText().toString().trim(),
                            editBeginDate.getText().toString().trim() + " " + editBeginTime.getText().toString().trim() + ((editBeginAM.isChecked()) ? (" AM") : (" PM")),
                            editEndDate.getText().toString().trim() + " " + editEndTime.getText().toString().trim() + ((editEndAM.isChecked()) ? (" AM") : (" PM"))};
                    PhoneCall call = new PhoneCall(arr);
                    if (call.getStartTime().compareTo(call.getEndTime()) > 0) { // if start > end
                        Snackbar.make(v, "The call may not begin before it ends.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else { // the call is good; enter it and refresh the form
                        Map<String, PhoneBill> map = mViewModel.getBills().getValue();
                        PhoneBill bill = map.get(arr[0]);
                        if (bill == null){
                            bill = new PhoneBill(arr[0]);
                        }
                        bill.addPhoneCall(call);
                        mViewModel.addBill(arr[0],bill);
                        // these are only cleared if the call is good
                        editCustomer.setText("");
                        editCaller.setText("");
                        editCallee.setText("");
                        Snackbar.make(v, "Call to " + arr[2] + " on " +
                                editBeginDate.getText().toString().trim() + " successfully recorded.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    editBeginDate.setText("");
                    editBeginTime.setText("");
                    editEndDate.setText("");
                    editEndTime.setText("");
                    editBeginAM.setChecked(true);
                    editEndAM.setChecked(true);

                }
                return;
            }
        });

        // Implement the CANCEL button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editCustomer.setText("");
                editCaller.setText("");
                editCallee.setText("");
                editBeginDate.setText("");
                editBeginTime.setText("");
                editEndDate.setText("");
                editEndTime.setText("");
                editBeginAM.setChecked(true);
                editEndAM.setChecked(true);
                return;
            }
        });
        return;
    }

    private boolean isValidCustomer(String s){
        if(s==null){
            return false;
        }
        return !("".equals(s.trim()));
    }

    private boolean isValidPhone(String s){
        if(s==null){
            return false;
        }
        Pattern p = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
        Matcher m = p.matcher(s.trim());
        if(!m.matches()){
            return false;
        }
        return true;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //if(savedInstanceState != null)
            //mViewModel.setName(savedInstanceState.get(VIEW_MODEL_STATE).toString());
        return inflater.inflate(R.layout.fragment_new_call, container, false);
    }
}