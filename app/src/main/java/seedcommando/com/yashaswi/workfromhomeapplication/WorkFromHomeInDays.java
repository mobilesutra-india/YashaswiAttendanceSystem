package seedcommando.com.yashaswi.workfromhomeapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.applicationstatus.ApplicationStatusActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.getreason.ReasonPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 3/12/2018.
 */

public class WorkFromHomeInDays extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    RadioGroup radioGroup;
    EditText From_date, To_date, no_of_days, reason_description;
    Button apply, cancel;
    Spinner reason;
    RadioButton radiofirsthalf, radiosecondhalf;
    //declaration for datepicker.........
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    Date startdate, enddate;
    String fromdate, todate, Work_from_home_Days = "";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    public static boolean firsthalf = false, secondhalf = false, calculatedays = false,isAttach=false,isFragmentLoaded=false;
    Double dayss;
    TextInputLayout inputLayoutfromdate, inputLayouttodate, inputLayoutreason;
    ProgressDialog pd;
    private ApiInterface apiService;

    ArrayList<String> level1=null,level2=null,level3=null,level4=null;
    //ArrayList<String> level2=null;
    //ArrayList<String> level3=null;
    //ArrayList<String> level4=null;
    LinearLayout.LayoutParams params,params1;
    //LinearLayout.LayoutParams params1;
    LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4,img1,img2,img3;
    View rootView;
    //boolean isAttach=false,isFragmentLoaded=false;
    Context context1;
    //boolean isFragmentLoaded=false;
    //static List<String>reason1;
    static List<String> reasonId,reason1;
    private ArrayAdapter<String> arrayAdapter = null;

    //TextInputLayout inputLayoutindate, inputLayoutoutdate, inputLayoutdate, inputLayoutTodate;


    public WorkFromHomeInDays() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firsthalf=false;
        secondhalf=false;




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.work_from_home_day, container, false);


        //ScrollView scrollView=rootView.findViewById(R.id.scroll1);
        //scrollView.fullScroll(View.SCROLL_AXIS_VERTICAL);
        From_date = rootView.findViewById(R.id.editText_wfh_from_date);

        To_date = rootView.findViewById(R.id.editText_wfh_to_date);
        reason_description = rootView.findViewById(R.id.editText_wfh_reason_description);
        apply = getActivity().findViewById(R.id.apply);
        cancel = getActivity().findViewById(R.id.cancel);
        radioGroup = rootView.findViewById(R.id.radiogp);
        ((RadioButton) rootView.findViewById(R.id.radio_fullday)).setChecked(true);
        radiofirsthalf = rootView.findViewById(R.id.radio_firsthalf);
        radiosecondhalf = rootView.findViewById(R.id.radio_secondhalf);
        no_of_days = rootView.findViewById(R.id.work_from_home_no_of_days);
        reason = rootView.findViewById(R.id.editText_wfh_reason);
        inputLayoutfromdate = rootView.findViewById(R.id.input_layout_float_wfh_from_date);
        inputLayouttodate = rootView.findViewById(R.id.input_layout_float_wfhto_date);
        //inputLayoutreason = (TextInputLayout) rootView.findViewById(R.id.input_layout_float_wfh_reason);
        From_date.addTextChangedListener(new MyTextWatcher(From_date));
        To_date.addTextChangedListener(new MyTextWatcher(To_date));
        apiService = ApiClient.getClient().create(ApiInterface.class);
        if(getArguments()!=null){
            String strtext = getArguments().getString("Date");
            From_date.setText(strtext);
            To_date.setText(strtext);
            try {
                startdate = dateFormatter.parse(strtext);
                enddate=dateFormatter.parse(strtext);
                caldays();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //reason.addTextChangedListener(new MyTextWatcher(reason));

        // Spinner click listener for leave type................................................
        reason.setOnItemSelectedListener(this);

        // Spinner Drop down elements
       /* List categories = new ArrayList();
        for(int i=0;i<WorkFromHomeActivity.reason.size();i++) {
            categories.add(WorkFromHomeActivity.reason.get(i));
        }*/
       if(WorkFromHomeActivity.position==0) {
           if (Utilities.isNetworkAvailable(getActivity())) {
               GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
               ReasonWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
           } else {
               Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
           }
       }


         linearLayout = rootView.findViewById(R.id.linearout);
        linearLayout2 = rootView.findViewById(R.id.linearout2);
        linearLayout3 = rootView.findViewById(R.id.linearout3);
         linearLayout4 = rootView.findViewById(R.id.linearout4);
        img1 = rootView.findViewById(R.id.img1);
        img2 = rootView.findViewById(R.id.img2);
        img3 = rootView.findViewById(R.id.img3);
        params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params1 = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        params.setMargins(0, 20, 0, 0);
       // GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //setDateTimeField();
       // radiocheckedlisner();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                firsthalf=false;
                secondhalf=false;

                int selectedID = radioGroup.getCheckedRadioButtonId();
                Log.i("tag", "msg" + selectedID);
                if(checkedId == (R.id.radio_fullday)) {
                    firsthalf = false;
                    secondhalf = false;

                    Log.d("", "radio:full day");
                    cal();


                }


              else   if (checkedId == (R.id.radio_firsthalf)) {

                    Log.d("", "In radio:first half");
                    firsthalf = true;
                    secondhalf = false;
                    cal();


                } else if (checkedId== (R.id.radio_secondhalf)) {

                    Log.d("", "In radio:second half");

                    firsthalf = false;
                    secondhalf = true;
                    cal();

                }

            }
        });






        return rootView;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        From_date.setOnClickListener(this);
        To_date.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                fromdate = dateFormatter.format(newDate.getTime());
                From_date.setText(fromdate);

                //conversion of one date format to other...
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                startdate = null;
                try {
                    startdate = dateFormatter.parse(fromdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("", "startdate" + startdate);
                if (To_date.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "please select out_date", Toast.LENGTH_LONG).show();

                } else {
                    caldays();

                }

                //...............................................

                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }


        }
                , newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate = dateFormatter.format(newDate.getTime());
                To_date.setText(todate);

                //conversion of one date format to other...
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                enddate = null;
                try {
                    enddate = dateFormatter.parse(todate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("", "enddate" + enddate);

                if (!From_date.getText().toString().trim().isEmpty()) {
                    caldays();
                } else {
                    Toast.makeText(getContext(), "please select in_date", Toast.LENGTH_LONG).show();
                }
               /* if (From_date.getText().toString().trim().length() != 0 && To_date.getText().toString().trim().length() != 0) {
                    oncalculatedays();


                }*/
                //........................


                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }


        }
                , newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        //for calculation of days.....

        // calculateddays();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCheck();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == From_date) {

            fromDatePickerDialog.show();
            calculatedays = false;
            no_of_days.setText("");

        } else if (view == To_date) {
            toDatePickerDialog.show();
            calculatedays = false;
            no_of_days.setText("");


        }
    }

    public void cal() {
        if (From_date.getText().toString().trim().length() != 0 && To_date.getText().toString().trim().length() != 0) {
            caldays();

        }
    }

    public void radiocheckedlisner() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedID = radioGroup.getCheckedRadioButtonId();
                Log.i("tag", "msg" + selectedID);

                if (selectedID == (R.id.radio_firsthalf)) {

                    Log.d("", "In radio:first half");
                    firsthalf = true;
                    secondhalf = false;
                    cal();


                } else if (selectedID == (R.id.radio_secondhalf)) {

                    Log.d("", "In radio:second half");
                    secondhalf = true;
                    firsthalf = false;
                    cal();

                } else if(selectedID == (R.id.radio_fullday)) {
                    firsthalf = false;
                    secondhalf = false;

                    Log.d("", "radio:full day");
                    cal();


                }


            }
        });
    }


    public void submitCheck() {

        if (!validateFromdate()) {
            return;
        }

        if (!validateTodate()) {
            return;
        }
        if (!validatereason()) {
            return;
        }
       /* if (!validateToReason()) {
            return;
        }*/

        if (enddate.getTime() >= startdate.getTime()) {


            if (reason_description.getText().toString().length() <= 50) {
                if(!level1.isEmpty()) {

                    if (Utilities.isNetworkAvailable(getContext())) {
                        WorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),getFrom_Date(),getTo_date(),get_isFirstHalf(),get_isSecondHalf(),"false",get_reason1(),get_reason(),"False");
                        // EmpowerApplication.dialogForApplication("Leave application for day",String.valueOf(no_of_days),Leave_Application.this);
                        //SendLeaveData((int) leavecode, 2, no_of_days, fromdate, todate, Reason, 3);
                    } else {
                        //((TextView) reasontype.getSelectedView()).setError("Select Leave Type");
                        Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                //Toast.makeText(getContext(), "Application Submitted sucessfully", Toast.LENGTH_LONG).show();

                }else {
                    EmpowerApplication.alertdialog("No Work Flow Found For This Employee", getContext());


                }

            } else {
                EmpowerApplication.alertdialog("reason should be less than 50 charactor",getActivity());

                //reason_description.setError("reason should be less than 50 charactor");
            }
        } else {
            EmpowerApplication.alertdialog("To date should be greater than From date",getActivity());
            //inputLayouttodate.setError("To date should be greater than From date");
            //To_date.requestFocus();
        }
    }

    private boolean validateFromdate() {
        if (From_date.getText().toString().trim().isEmpty()) {
            inputLayoutfromdate.setError(getString(R.string.err_msg_fromdate));
            From_date.requestFocus();
            return false;
        } else {
            inputLayoutfromdate.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateTodate() {
        if (To_date.getText().toString().trim().isEmpty()) {
            inputLayouttodate.setError(getString(R.string.err_msg_todate));
            To_date.requestFocus();
            return false;
        } else {
            inputLayouttodate.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatereason() {
        if (reason.getSelectedItem().toString().trim().equals("Select")) {
            setSpinnerError(reason,"Select Reason type ");
            reason.requestFocus();
            return false;
        } /*else {
            inputLayoutouttime.setErrorEnabled(false);
        }*/

        return true;
    }
    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            //spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    /*private boolean validateToReason() {
        if (reason_description.getText().toString().trim().isEmpty()) {
            inputLayoutreason.setError(getString(R.string.err_msg_reason1));
            reason_description.requestFocus();
            return false;
        } else {
            inputLayoutreason.setErrorEnabled(false);
        }

        return true;
    }*/

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_wfh_from_date:
                    validateFromdate();
                    break;

                case R.id.editText_wfh_to_date:
                    validateTodate();
                    break;
               /* case R.id.editText_wfh_reason_description:
                    validateToReason();
                    break;*/
            }
        }
    }


    public void caldays() {

        if (enddate.getTime() >= startdate.getTime()) {
            //outdutyflag=true;
            if(Utilities.isNetworkAvailable(getContext())) {
                getNoOfDayWorkFromHomeApp(From_date.getText().toString().trim(),To_date.getText().toString().trim(),String.valueOf(firsthalf),String.valueOf(secondhalf));
                Log.e("getdata",String.valueOf(firsthalf)+String.valueOf(secondhalf));
            }else {
                Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();
            }


           /* if (firsthalf == true || secondhalf == true) {

                long diff1 = enddate.getTime() - startdate.getTime();
                float days = (float) diff1 / 1000 / 60 / 60 / 24;
                //dayss=((int) Math.ceil(days) + 1-0.5);
                no_of_days.setText(String.valueOf((int) Math.ceil(days) + 1 - 0.5));
                dayss = Double.parseDouble(no_of_days.getText().toString());
                Work_from_home_Days = no_of_days.getText().toString();
            } else {
                long diff = Math.abs(enddate.getTime() - startdate.getTime());
                float days = (float) diff / 1000 / 60 / 60 / 24;
                no_of_days.setText(String.valueOf((int) Math.ceil(days) + 1));
                Log.d("", "textnodays" + no_of_days);
            }
            calculatedays = true;*/


           /* long days = getDateDiff(startdate, enddate, TimeUnit.DAYS);
            no_of_days.setText(Long.toString(days + 1));*/
        } else {
            Log.d("", "please enter valid start and end date");
            //EmpowerApplication.alertdialog("To Date cannot be less Than From Date", getContext());
            Toast.makeText(getContext(), "please enter valid start and end date", Toast.LENGTH_LONG).show();

        }

        //return "ERROR";
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser&&isFragmentLoaded) {
            // Load your data here or do network operations here
            //userVisible=true;
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitCheck();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            if(Utilities.isNetworkAvailable(getActivity())) {
                GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                ReasonWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            }else {
                Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();
            }
        }

    }



    public void GetWorkFlowForWorkFromHomeApp(String employeeId,String token) {

       // pd = new ProgressDialog(getContext());
        //pd.setMessage("Loading....");
       // pd.show();

        Map<String,String> GetWorkFlowdata=new HashMap<String,String>();
        GetWorkFlowdata.put("employeeId",employeeId);
        GetWorkFlowdata.put("token",token);

        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();

        retrofit2.Call<FinalResponsePoJo> call = apiService.GetWorkFlowForWorkFromHomeApp(GetWorkFlowdata);
        call.enqueue(new Callback<FinalResponsePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<FinalResponsePoJo> call, Response<FinalResponsePoJo> response) {
              //  pd.dismiss();
                level1.clear();
                level2.clear();
                level3.clear();
                level4.clear();
                linearLayout.removeAllViews();
                linearLayout2.removeAllViews();
                linearLayout3.removeAllViews();
                linearLayout4.removeAllViews();
                if (response.isSuccessful()) {

                    if(response.body().getStatus().equals("1")) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (i == 0) {

                                if (response.body().getData().get(0).getLevelNo().equals("1")) {
                                    if (!(response.body().getData().get(0).getEmployeeID1Name() == null) && !response.body().getData().get(0).getEmployeeID1Name().equals(""))
                                        level1.add(response.body().getData().get(0).getEmployeeID1Name());
                                    if (!(response.body().getData().get(0).getManager1() == null) && !response.body().getData().get(0).getManager1().equals(""))
                                        level1.add(response.body().getData().get(0).getManager1());
                                    if (!(response.body().getData().get(0).getManager2() == null) && !response.body().getData().get(0).getManager2().equals(""))
                                        level1.add(response.body().getData().get(0).getManager2());
                                    if (!(response.body().getData().get(0).getManager3() == null) && !response.body().getData().get(0).getManager3().equals(""))
                                        level1.add(response.body().getData().get(0).getManager3());
                                    if (!(response.body().getData().get(0).getManager4() == null) && !response.body().getData().get(0).getManager4().equals(""))
                                        level1.add(response.body().getData().get(0).getManager4());
                                    if (!(response.body().getData().get(0).getHRName() == null) && !response.body().getData().get(0).getHRName().equals(""))
                                        level1.add(response.body().getData().get(0).getHRName());
                                    if (!(response.body().getData().get(0).getDivisionHeadName() == null) && !response.body().getData().get(0).getDivisionHeadName().equals(""))
                                        level1.add(response.body().getData().get(0).getDivisionHeadName());
                                    if (!(response.body().getData().get(0).getEmployeeID2Name() == null) && !response.body().getData().get(0).getEmployeeID2Name().equals(""))
                                        level1.add(response.body().getData().get(0).getEmployeeID2Name());
                                    if (!(response.body().getData().get(0).getSubSidiaryHeadName() == null) && !response.body().getData().get(0).getSubSidiaryHeadName().equals(""))
                                        level1.add(response.body().getData().get(0).getSubSidiaryHeadName());
                                    if (!(response.body().getData().get(0).getDepartMentHeadName() == null) && !response.body().getData().get(0).getDepartMentHeadName().equals(""))
                                        level1.add(response.body().getData().get(0).getDepartMentHeadName());


                                }
                            }
                            if (i == 1) {
                                if (response.body().getData().get(1).getLevelNo().equals("2")) {
                                    if (!(response.body().getData().get(1).getEmployeeID1Name() == null) && !response.body().getData().get(1).getEmployeeID1Name().equals(""))

                                        level2.add(response.body().getData().get(1).getEmployeeID1Name());
                                    if (!(response.body().getData().get(1).getManager1() == null) && !response.body().getData().get(1).getManager1().equals(""))

                                        level2.add(response.body().getData().get(1).getManager1());
                                    if (!(response.body().getData().get(1).getManager2() == null) && !response.body().getData().get(1).getManager2().equals(""))

                                        level2.add(response.body().getData().get(1).getManager2());
                                    if (!(response.body().getData().get(1).getManager3() == null) && !response.body().getData().get(1).getManager3().equals(""))

                                        level2.add(response.body().getData().get(1).getManager3());
                                    if (!(response.body().getData().get(1).getManager4() == null) && !response.body().getData().get(1).getManager4().equals(""))

                                        level2.add(response.body().getData().get(1).getManager4());
                                    if (!(response.body().getData().get(1).getHRName() == null) && !response.body().getData().get(1).getHRName().equals(""))

                                        level2.add(response.body().getData().get(1).getHRName());
                                    if (!(response.body().getData().get(1).getDivisionHeadName() == null) && !response.body().getData().get(1).getDivisionHeadName().equals(""))

                                        level2.add(response.body().getData().get(1).getDivisionHeadName());
                                    if (!(response.body().getData().get(1).getEmployeeID2Name() == null) && !response.body().getData().get(1).getEmployeeID2Name().equals(""))

                                        level2.add(response.body().getData().get(1).getEmployeeID2Name());
                                    if (!(response.body().getData().get(1).getSubSidiaryHeadName() == null) && !response.body().getData().get(1).getSubSidiaryHeadName().equals(""))

                                        level2.add(response.body().getData().get(1).getSubSidiaryHeadName());
                                    if (!(response.body().getData().get(1).getDepartMentHeadName() == null) && !response.body().getData().get(1).getDepartMentHeadName().equals(""))

                                        level2.add(response.body().getData().get(1).getDepartMentHeadName());


                                }
                            }
                            if (i == 2) {
                                if (response.body().getData().get(2).getLevelNo().equals("3")) {
                                    if (!(response.body().getData().get(2).getEmployeeID1Name() == null) && !response.body().getData().get(2).getEmployeeID1Name().equals(""))

                                        level3.add(response.body().getData().get(2).getEmployeeID1Name());
                                    if (!(response.body().getData().get(2).getManager1() == null) && !response.body().getData().get(2).getManager1().equals(""))

                                        level3.add(response.body().getData().get(2).getManager1());
                                    if (!(response.body().getData().get(2).getManager2() == null) && !response.body().getData().get(2).getManager2().equals(""))

                                        level3.add(response.body().getData().get(2).getManager2());
                                    if (!(response.body().getData().get(2).getManager3() == null) && !response.body().getData().get(2).getManager3().equals(""))

                                        level3.add(response.body().getData().get(2).getManager3());
                                    if (!(response.body().getData().get(2).getManager4() == null) && !response.body().getData().get(2).getManager4().equals(""))

                                        level3.add(response.body().getData().get(2).getManager4());
                                    if (!(response.body().getData().get(2).getHRName() == null) && !response.body().getData().get(2).getHRName().equals(""))

                                        level3.add(response.body().getData().get(2).getHRName());
                                    if (!(response.body().getData().get(2).getDivisionHeadName() == null) && !response.body().getData().get(2).getDivisionHeadName().equals(""))

                                        level3.add(response.body().getData().get(2).getDivisionHeadName());
                                    if (!(response.body().getData().get(2).getEmployeeID2Name() == null) && !response.body().getData().get(2).getEmployeeID2Name().equals(""))

                                        level3.add(response.body().getData().get(2).getEmployeeID2Name());
                                    if (!(response.body().getData().get(2).getSubSidiaryHeadName() == null) && !response.body().getData().get(2).getSubSidiaryHeadName().equals(""))

                                        level3.add(response.body().getData().get(2).getSubSidiaryHeadName());
                                    if (!(response.body().getData().get(2).getDepartMentHeadName() == null) && !response.body().getData().get(2).getDepartMentHeadName().equals(""))

                                        level3.add(response.body().getData().get(2).getDepartMentHeadName());



                                }
                            }
                            if (i == 3) {
                                if (response.body().getData().get(3).getLevelNo().equals("4")) {
                                    if (!(response.body().getData().get(3).getEmployeeID1Name() == null) && !response.body().getData().get(3).getEmployeeID1Name().equals(""))

                                        level4.add(response.body().getData().get(1).getEmployeeID1Name());
                                    if (!(response.body().getData().get(3).getManager1() == null) && !response.body().getData().get(3).getManager1().equals(""))

                                        level4.add(response.body().getData().get(3).getManager1());
                                    if (!(response.body().getData().get(3).getManager2() == null) && !response.body().getData().get(3).getManager2().equals(""))

                                        level4.add(response.body().getData().get(3).getManager2());
                                    if (!(response.body().getData().get(3).getManager3() == null) && !response.body().getData().get(3).getManager3().equals(""))

                                        level4.add(response.body().getData().get(3).getManager3());
                                    if (!(response.body().getData().get(3).getManager4() == null) && !response.body().getData().get(3).getManager4().equals(""))

                                        level4.add(response.body().getData().get(3).getManager4());
                                    if (!(response.body().getData().get(3).getHRName() == null) && !response.body().getData().get(3).getHRName().equals(""))

                                        level4.add(response.body().getData().get(3).getHRName());
                                    if (!(response.body().getData().get(3).getDivisionHeadName() == null) && !response.body().getData().get(3).getDivisionHeadName().equals(""))

                                        level4.add(response.body().getData().get(3).getDivisionHeadName());
                                    if (!(response.body().getData().get(3).getEmployeeID2Name() == null) && !response.body().getData().get(3).getEmployeeID2Name().equals(""))

                                        level4.add(response.body().getData().get(3).getEmployeeID2Name());
                                    if (!(response.body().getData().get(3).getSubSidiaryHeadName() == null) && !response.body().getData().get(3).getSubSidiaryHeadName().equals(""))

                                        level4.add(response.body().getData().get(3).getSubSidiaryHeadName());
                                    if (!(response.body().getData().get(3).getDepartMentHeadName() == null) && !response.body().getData().get(3).getDepartMentHeadName().equals(""))

                                        level4.add(response.body().getData().get(3).getDepartMentHeadName());



                                }
                            }
                        }
                        addViews();
                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(),getActivity());
                    }
                    }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getActivity());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                //Log.d("User ID1: ", response.body().toString());
                //if(response.body().getStatus().equals("1")) {
                // SharedPreferences.Editor e = sp.edit();
                // e.putString("data",response.body().getData());
                //e.putString("password", "sa");
                //e.commit();
                //EmpowerApplication.set_session("data",response.body().getData());
                //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

               /* }
                else {
                    EmpowerApplication.alertdialog(response.body().getMessage(),WorkFromHomeActivity.this);

                }*/

            }

            @Override
            public void onFailure(retrofit2.Call<FinalResponsePoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(),getContext());


            }
        });
    }

    public void addViews(){

        if(!(level1.size() ==0)){
            for(int i=0;i<level1.size();i++){

                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (getContext());
                textView.setGravity(Gravity.CENTER);


                // Add image path from drawable folder.
                //CircleImageView.
                // imageview.setImageResource(R.drawable.em_logo);
                // imageview.setLayoutParams(params);
                // RoundedImageView.getCroppedBitmap(R.drawable.em_logo,10)

                //linearLayout.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout.addView(textView);
                textView.setText(level1.get(i));

                //linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }

        if(!(level2.size() ==0)){
            ImageView imageView=rootView.findViewById(R.id.arrow1);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level2.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (getContext());
                textView.setGravity(Gravity.CENTER);
                // Add image path from drawable folder.
                //imageview.setImageResource(R.drawable.em_logo);
                //imageview.setLayoutParams(params);

                //linearLayout2.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout2.addView(textView);
                textView.setText(level2.get(i));
            }
        }
        if(!(level3.size() ==0)){
            ImageView imageView=rootView.findViewById(R.id.arrow2);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level3.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (getContext());
                textView.setGravity(Gravity.CENTER);
                // Add image path from drawable folder.
                //imageview.setImageResource(R.drawable.em_logo);
                //imageview.setLayoutParams(params);

                //linearLayout3.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout3.addView(textView);
                textView.setText(level3.get(i));
            }
        }
        if(!(level4.size() ==0)){
            ImageView imageView=rootView.findViewById(R.id.arrow3);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level4.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);

                TextView textView = new TextView (getContext());
                textView.setGravity(Gravity.CENTER);
                // Add image path from drawable folder.
                // imageview.setImageResource(R.drawable.em_logo);
                // imageview.setLayoutParams(params);

                //linearLayout4.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout4.addView(textView);
                textView.setText(level4.get(i));
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        isAttach=true;
        isFragmentLoaded=true;
        firsthalf=false;
        secondhalf=false;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentLoaded=false;
        firsthalf=false;
        secondhalf=false;



    }

    /* @Override
     public void onStart() {
         super.onStart();
         isFragmentLoaded=true;
     }
 */
    @Override
    public void onStop() {
        super.onStop();
        isFragmentLoaded=false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentLoaded=false;


    }


    public void WorkFromHomeApp(String employeeId,String token,String fromdate,String todate,String FirstHalf,String SecondHalf,String WFHAppInHours,String reasonID,String reasonDescription,String isConsiderTimeCross) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();

        WorkFlowdataapp.put("token",token);
        WorkFlowdataapp.put("employeeId",employeeId);
        WorkFlowdataapp.put("fromDate",fromdate);
        WorkFlowdataapp.put("toDate",todate);
        WorkFlowdataapp.put("isFirstHalf",FirstHalf);
        WorkFlowdataapp.put("isSecondHalf",SecondHalf);
        WorkFlowdataapp.put("isWFHAppInHours",WFHAppInHours);
        WorkFlowdataapp.put("fromTime","");
        WorkFlowdataapp.put("toTime","");
        WorkFlowdataapp.put("reasonID",reasonID);
        WorkFlowdataapp.put("isConsiderTimeCross",isConsiderTimeCross);
        Log.e("reasonID",reasonID);
        WorkFlowdataapp.put("reasonDescription",reasonDescription);
        retrofit2.Call<WorkFromHomePoJo> call = apiService.WorkFromHomeApp(WorkFlowdataapp);
        call.enqueue(new Callback<WorkFromHomePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<WorkFromHomePoJo> call, Response<WorkFromHomePoJo> response) {
                pd.dismiss();

                if (response.isSuccessful()) {



                    if (response.body().getStatus().equals("1")) {
                        dialogForApplication("WFH application for Day",no_of_days.getText().toString(),getContext());


                        //EmpowerApplication.alertdialog(response.body().getMessage(), getContext());

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), getContext());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getContext());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<WorkFromHomePoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public void getNoOfDayWorkFromHomeApp(String fromdate,String todate,String FirstHalf,String SecondHalf) {

        //pd = new ProgressDialog(getContext());
       // pd.setMessage("Loading....");
       // pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();


        WorkFlowdataapp.put("fromDate",fromdate);
        WorkFlowdataapp.put("toDate",todate);
        WorkFlowdataapp.put("isFirstHalf",FirstHalf);
        WorkFlowdataapp.put("isSecondHalf",SecondHalf);

        retrofit2.Call<CommanResponsePojo> call = apiService.getNoOfDayWorkFromHomeApp(WorkFlowdataapp);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
              //  pd.dismiss();

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("1")) {
                        Log.e("noofdays",response.body().getData());

                     no_of_days.setText(response.body().getData());

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), getContext());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getContext());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }



    public void ReasonWorkFromHomeApp(String employeeId) {

       /* pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();*/

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);

        reason1=new ArrayList<>();
        reasonId=new ArrayList<String>();

        retrofit2.Call<ReasonPoJo> call = apiService.getReasonWorkFromHomeApp(WorkFlowdataapp);
        call.enqueue(new Callback<ReasonPoJo>() {
            @Override
            public void onResponse(retrofit2.Call<ReasonPoJo> call, Response<ReasonPoJo> response) {
               // pd.dismiss();
                reason1.clear();
                reasonId.clear();

                if (response.isSuccessful()) {
                    reason1.add("Select");

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            reasonId.add(response.body().getData().get(i).getReasonID());
                            reason1.add(response.body().getData().get(i).getReasonTitle());
                        }

                        // Creating adapter for spinner
                         arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,reason1);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reason.setAdapter(arrayAdapter);

                        //EmpowerApplication.alertdialog(response.body().getMessage(), WorkFromHomeActivity.this);

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), getActivity());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getActivity());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<ReasonPoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }


    public String getFrom_Date(){
        return From_date.getText().toString().trim();
    }
    public String getTo_date(){
        return To_date.getText().toString().trim();
    }
    public String get_reason(){
        return reason_description.getText().toString().trim();
    }
    public String get_isFirstHalf(){
        return String.valueOf(firsthalf);
    }
    public String get_isSecondHalf(){
        return String.valueOf(secondhalf);
    }
    public String get_reason1(){
        return reasonId.get(reason.getSelectedItemPosition()-1);
    }

    public  void dialogForApplication(String msg, String days,Context context) {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dailog_application);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = dialog1.findViewById(R.id.textView_dialog_mark_attendance);
        title.setText(msg);
        TextView day_textview = dialog1.findViewById(R.id.button4);
        day_textview.setText(days);
        Button btn_ok = dialog1.findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(HomeFragment.key.contains("HideWFHApp")) {
                    int index = HomeFragment.key.indexOf("HideWFHApp");
                    Intent intentSurvey = new Intent(getContext(), ApplicationStatusActivity.class);
                    intentSurvey.putExtra("ARG_PAGE", index);
                    startActivity(intentSurvey);
                    dialog1.dismiss();
                }
            }
        });
    }




}

