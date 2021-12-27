package seedcommando.com.yashaswi.workfromhomeapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import seedcommando.com.yashaswi.outdutyapplication.DatePickerFragment;
import seedcommando.com.yashaswi.outdutyapplication.TimePickerFragment;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.getreason.ReasonPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

import static seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization.pad;

/**
 * Created by commando4 on 3/12/2018.
 */

public class WorkFromHomeInHrs  extends Fragment {

    EditText date, from_time, to_time,reason,address,to_date;
    SimpleDateFormat dateFormatter;
    SimpleDateFormat simpleDateFormat11 = new SimpleDateFormat("hh:mm aa");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat h_mm_a   = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat hh_mm_ss = new SimpleDateFormat("HH:mm");
    String fromdate="",todate="";
    Date startDate,endDate,startDate1,endDate2;
    TextView calhrs;
    Spinner reason1;
    Button apply,cancel;
    private boolean isFragmentLoaded=false;
    public static String for_out_time="To Time should be greater than From Time";
    boolean userVisible=false;
    boolean isAttached=false;
    View rootView;
    Boolean ischecked=false;
    // Date difference cannot be more than One Day

    private ApiInterface apiService;

    ArrayList<String> level1=null,level2=null,level3=null,level4=null;
    //ArrayList<String> level2=null;
   // ArrayList<String> level3=null;
   // ArrayList<String> level4=null;
    LinearLayout.LayoutParams params,params1;
    //LinearLayout.LayoutParams params1;
    LinearLayout linearLayout,linearLayout2,linearLayout3,linearLayout4,img1,img2,img3;
    ProgressDialog pd;
     List<String>reason12, reasonId;
    //List<String> reasonId;
    private ArrayAdapter<String> arrayAdapter = null;
    CheckBox checkBox;

    TextInputLayout inputLayoutreason,inputLayoutindate,inputLayoutoutdate,inputLayoutdate,inputLayoutTodate;


    public WorkFromHomeInHrs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFragmentLoaded=true;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.work_from_home_in_hrs, container, false);


        date = rootView.findViewById(R.id.editText_date_hrs);
       to_date = rootView.findViewById(R.id.editText_to_date_hrs);
        from_time = rootView.findViewById(R.id.editText_in_time_hrs);
        to_time = rootView.findViewById(R.id.editText_out_time_hrs);
        address = rootView.findViewById(R.id.editText_out_date_location_hrs);
        calhrs = rootView.findViewById(R.id.txt_out_duty_cal_days);
        reason = rootView.findViewById(R.id.editText_out_date_remark);
        reason1=rootView.findViewById(R.id.editText_wfh_reason);
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        apply = this.getActivity().findViewById(R.id.apply);
        cancel = this.getActivity().findViewById(R.id.cancel);
        checkBox= rootView.findViewById(R.id.wfhhrs_checkox);
        inputLayoutreason = rootView.findViewById(R.id.input_layout_floatout_duty_remark);
        inputLayoutindate = rootView.findViewById(R.id.input_layout_floatout_duty_from_time);
        inputLayoutdate = rootView.findViewById(R.id.input_layout_floatIn_date);
        inputLayoutTodate = rootView.findViewById(R.id.input_layout_floatIn_to_date);
        inputLayoutoutdate = rootView.findViewById(R.id.input_layout_floatout_duty_to_time);
        from_time.addTextChangedListener(new MyTextWatcher(from_time));
        to_time.addTextChangedListener(new MyTextWatcher(to_time));
        reason.addTextChangedListener(new MyTextWatcher(reason));
        date.addTextChangedListener(new MyTextWatcher(date));
        apiService = ApiClient.getClient().create(ApiInterface.class);

        linearLayout=rootView.findViewById(R.id.linearout);
        linearLayout2=rootView.findViewById(R.id.linearout2);
        linearLayout3=rootView.findViewById(R.id.linearout3);
        linearLayout4=rootView.findViewById(R.id.linearout4);
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
        if(getActivity().getIntent().getExtras()!=null){
            String strtext = getActivity().getIntent().getExtras().getString("Date");
            date.setText(strtext);
           to_date.setText(strtext);
            try {
                startDate1 = dateFormatter.parse(strtext);
                endDate2=dateFormatter.parse(strtext);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        /*if(Utilities.isNetworkAvailable(getActivity())) {


            GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
        }else {
            Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();
        }*/



        return rootView;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePicker();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker1();
            }
        });
        from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePicker();
            }
        });
        to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePicker1();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ischecked=true;
                    if(!to_date.getText().toString().isEmpty() && !from_time.getText().toString().isEmpty() && !to_time.getText().toString().isEmpty()&& !date.getText().toString().isEmpty()){
                        if (Utilities.isNetworkAvailable(getActivity())) {

                            WorkFromHomeAppCalHrs(date.getText().toString()+" "+from_time.getText().toString(),to_date.getText().toString()+" "+to_time.getText().toString(),String.valueOf(ischecked),"False");
                            // GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                        } else {
                            Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }

                    }

                }else {
                    ischecked=false;
                    if(!to_date.getText().toString().isEmpty() && !from_time.getText().toString().isEmpty() && !to_time.getText().toString().isEmpty()&& !date.getText().toString().isEmpty()){
                        if (Utilities.isNetworkAvailable(getActivity())) {

                            WorkFromHomeAppCalHrs(date.getText().toString()+" "+from_time.getText().toString(),to_date.getText().toString()+" "+to_time.getText().toString(),String.valueOf(ischecked),"False");
                            // GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                        } else {
                            Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }

                    }


                }
            }
        });

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */

        date.setCallBack(ondate);


        //date.show(getFragmentManager(), "Date Picker");
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar newDate = Calendar.getInstance();

            newDate.set(year, monthOfYear, dayOfMonth);
            fromdate = dateFormatter.format(newDate.getTime());
            date.setText(fromdate);
            to_date.setText(fromdate);
            dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                startDate1=dateFormatter.parse(date.getText().toString());
               endDate2=dateFormatter.parse(date.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(!to_date.getText().toString().isEmpty() && !from_time.getText().toString().isEmpty() && !to_time.getText().toString().isEmpty()){
                if (Utilities.isNetworkAvailable(getActivity())) {

                    WorkFromHomeAppCalHrs(date.getText().toString()+" "+from_time.getText().toString(),to_date.getText().toString()+" "+to_time.getText().toString(),String.valueOf(ischecked),"False");
                   // GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                }

            }

            //conversion of one date format to other...
            dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

/*
            date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(year));
           to_date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(year));*/
        }
    };


    private void showDatePicker1() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */

        date.setCallBack(ondate1);


        //date.show(getFragmentManager(), "Date Picker");
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar newDate = Calendar.getInstance();

            newDate.set(year, monthOfYear, dayOfMonth);
            fromdate = dateFormatter.format(newDate.getTime());
            //date.setText(fromdate);
            to_date.setText(fromdate);
            try {
                endDate2=dateFormatter.parse(to_date.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!date.getText().toString().isEmpty() && !from_time.getText().toString().isEmpty() && !to_time.getText().toString().isEmpty()){
                if (Utilities.isNetworkAvailable(getActivity())) {


                    WorkFromHomeAppCalHrs(date.getText().toString()+" "+from_time.getText().toString(),to_date.getText().toString()+" "+to_time.getText().toString(),String.valueOf(ischecked),"False");
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                }

            }

            //conversion of one date format to other...
            dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        }
    };

    private void showTimePicker()
    {
        //DatePickerFragment date = new DatePickerFragment();
        TimePickerFragment time= new TimePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", Calendar.HOUR_OF_DAY);
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("minute", calender.get(Calendar.MINUTE));
        time.setArguments(args);


        time.setCallBack(ontime);
        time.show(getFragmentManager(), "Time Picker");

    }

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub

            String am_pm = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (datetime
                    .get(Calendar.HOUR) == 0) ? "12"
                    : datetime.get(Calendar.HOUR) + "";

            from_time.setText(strHrsToShow + ":"
                    + pad(datetime.get(Calendar.MINUTE))
                    + " " + am_pm);

            // from_time.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

            //fromdate= from_time.getText().toString().trim();
            fromdate=(strHrsToShow+":"+pad(datetime.get(Calendar.MINUTE)));

            try {
                Date d1 = h_mm_a.parse(from_time.getText().toString());
                 String data1=hh_mm_ss.format(d1);

                startDate = hh_mm_ss.parse(data1);
            }catch (ParseException p){
                p.printStackTrace();
            }

            if(todate.equals("")) {
                Toast.makeText(getContext(),"please select to_time",Toast.LENGTH_LONG).show();

            }else {
                if (!from_time.getText().toString().trim().isEmpty() && !to_time.getText().toString().trim().isEmpty())
                {

                   /* String resultcampare = CompareTime(from_time.getText().toString().trim(), to_time.getText().toString().trim());
                    if (!resultcampare.equals("1")) {

                        //EmpowerApplication.alertdialog(for_out_time, getContext());

                    }else {*/
                        //calhrs();
                        if(!to_date.getText().toString().isEmpty() && !date.getText().toString().isEmpty() && !to_time.getText().toString().isEmpty()){
                            if (Utilities.isNetworkAvailable(getActivity())) {

                                WorkFromHomeAppCalHrs(date.getText().toString()+" "+from_time.getText().toString(),to_date.getText().toString()+" "+to_time.getText().toString(),String.valueOf(ischecked),"False");
                                //GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                            } else {
                                Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                            }

                        }

                   // }

                }
                // calhrs();
            }
        }
    };


    private void showTimePicker1()
    {
        //DatePickerFragment date = new DatePickerFragment();
        TimePickerFragment time= new TimePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("hour", Calendar.HOUR_OF_DAY);
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("minute", calender.get(Calendar.MINUTE));
        time.setArguments(args);


        time.setCallBack(ontime1);
        time.show(getFragmentManager(), "Time Picker");

    }

    TimePickerDialog.OnTimeSetListener ontime1 = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String am_pm = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (datetime
                    .get(Calendar.HOUR) == 0) ? "12"
                    : datetime.get(Calendar.HOUR) + "";

            to_time.setText(strHrsToShow + ":"
                    + pad(datetime.get(Calendar.MINUTE))
                    + " " + am_pm);


            //to_time.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            //todate= to_time.getText().toString().trim();
            todate=(strHrsToShow+":"+pad(datetime.get(Calendar.MINUTE))).trim();
            try {
                Date d1 = h_mm_a.parse(to_time.getText().toString());
                String data1=hh_mm_ss.format(d1);

                endDate = hh_mm_ss.parse(data1);
                //endDate = simpleDateFormat.parse(todate);
            }catch (ParseException p){
                p.printStackTrace();
            }

            if(fromdate.equals("")) {
                Toast.makeText(getContext(),"please select from_time",Toast.LENGTH_LONG).show();

            }else {
                if (!from_time.getText().toString().trim().isEmpty() && !to_time.getText().toString().trim().isEmpty())
                {


                   /* String resultcampare = CompareTime(from_time.getText().toString().trim(), to_date.getText().toString().trim());
                    if (!resultcampare.equals("1")) {


                        //EmpowerApplication.alertdialog(for_out_time, getContext());

                    }else {*/
                        //calhrs();
                        if(!to_date.getText().toString().isEmpty() && !from_time.getText().toString().isEmpty() && !date.getText().toString().isEmpty()){
                            if (Utilities.isNetworkAvailable(getActivity())) {
                                WorkFromHomeAppCalHrs(date.getText().toString()+" "+from_time.getText().toString(),to_date.getText().toString()+" "+to_time.getText().toString(),String.valueOf(ischecked),"False");

                                //GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                            } else {
                                Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                            }

                        }

                   // }


                }

            }
        }
    };

    public void calhrs(){



            if (date.getText().toString().equals(to_date.getText().toString())){

                    //outdutyflag=true;
                    // getNoOfDayWorkFromHomeApp(From_date.getText().toString().trim(),To_date.getText().toString().trim(),String.valueOf(firsthalf),String.valueOf(secondhalf));


                    long difference = endDate.getTime() - startDate.getTime();
                //long difference = simpleDateFormat.format(simpleDateFormat11.parse(to_time.getText().toString())) - startDate.getTime();
               // date24Format.format(date12Format.parse(time))
                    if (difference < 0) {
                        try {
                            Date dateMax = simpleDateFormat.parse("24:00");
                            Date dateMin = simpleDateFormat.parse("00:00");
                            difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
                        } catch (ParseException p) {
                            p.printStackTrace();
                        }

                    }
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);
                    //calhrs.setText(Integer.toString(hours));

                if(hours<10){
                    hours=Integer.parseInt("0"+""+String.valueOf(hours));

                }if(min<10) {
                    min=Integer.parseInt("0"+""+String.valueOf(min));

                }


                    calhrs.setText(hours + ":" + min);
                  } else {
                Log.d("", "please enter valid start and end date");
                EmpowerApplication.alertdialog("Date difference cannot be more than One Day", getContext());
               // Toast.makeText(getContext(), "please enter valid start and end date", Toast.LENGTH_LONG).show();

            }
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

                case R.id.editText_date_hrs:
                    validatedate();
                    break;
                case R.id.editText_in_time_hrs:
                    validateintime();
                    break;

                case R.id.editText_out_time_hrs:
                    validateouttime();
                    break;
                /*case R.id.editText_out_date_remark:
                    validatereason();
                    break;*/
                case R.id.editText_to_date_hrs:
                    validatetodate();
                    break;

            }
        }
    }


    public void onSubmit(View view){

        submitCheck();
    }
    public  void submitCheck() {

        if (!validatedate()) {
            return;
        }

        if (!validateintime()) {
            return;
        }
        if (!validatetodate()) {
            return;
        }
        if (!validateouttime()) {
            return;
        }
        if (!validatereason1()) {
            return;
        }
       /* if (!validatereason()) {
            return;
        }*/
       Log.e("enddate", String.valueOf(endDate2.getTime()));
        Log.e("startdate", String.valueOf(startDate1.getTime()));
       // String resultcampare = CompareTime(from_time.getText().toString().trim(), to_time.getText().toString().trim());
        if(!endDate2.before( startDate1)) {
        if(true) {
           /* if (address.getText().toString().length() <= 50) {*/
                if (reason.getText().toString().length() <= 50) {
                    if(Utilities.isNetworkAvailable(getActivity())) {


                        WorkFromHomeAppInHrs(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),getFrom_Date(),getTo_date(),get_Fromtime(),get_Totime(),"True",get_reason1(),get_reason(),String.valueOf(ischecked));
                    }else {
                        Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();
                    }



                   // Toast.makeText(getContext(), "Application Submitted sucessfully", Toast.LENGTH_LONG).show();

                } else {
                    reason.setError("reason should be less than 50 charactor");
                }
           /* } else {
                address.setError("Address should be less than 50 charactor");
            }*/
        }else {
            from_time.requestFocus();
            to_time.requestFocus();
            EmpowerApplication.alertdialog(for_out_time, getContext());
        }
        }else {
            to_date.requestFocus();
            date.requestFocus();
            EmpowerApplication.alertdialog("To date should be greater than or Equal to From date", getContext());

        }



    }
   /* private  boolean validatereason() {
        if (reason.getText().toString().trim().isEmpty()) {
            inputLayoutreason.setError(getString(R.string.err_msg_reason));
            reason.requestFocus();
            return false;
        } else {
            inputLayoutreason.setErrorEnabled(false);
        }

        return true;
    }
*/


    private boolean validateintime() {
        if (from_time.getText().toString().trim().isEmpty()) {
            inputLayoutindate.setError(getString(R.string.err_msg_intime_od));
            from_time.requestFocus();
            return false;
        } else {
            inputLayoutindate.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateouttime() {
        if (to_time.getText().toString().trim().isEmpty()) {
            inputLayoutoutdate.setError(getString(R.string.err_msg_outtime_od));
            to_time.requestFocus();
            return false;
        } else {
            inputLayoutoutdate.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatedate() {
        if (date.getText().toString().trim().isEmpty()) {
            inputLayoutdate.setError(getString(R.string.err_msg_fromdate));
            date.requestFocus();
            return false;
        } else {
            inputLayoutdate.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatetodate() {
        if (to_date.getText().toString().trim().isEmpty()) {
            inputLayoutTodate.setError(getString(R.string.err_msg_todate));
            to_date.requestFocus();
            return false;
        } else {
            inputLayoutTodate.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatereason1() {
        if (reason1.getSelectedItem().toString().trim().equals("Select")) {
            setSpinnerError(reason1,"Select Reason type ");
            reason1.requestFocus();
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
    public String CompareTime(String strTimeToCompare,String endTimeToCompare)

    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        try {

            Date TimeToCompare = sdf.parse(strTimeToCompare);


            Date TimeToCompare1 = sdf1.parse(endTimeToCompare);

            if(TimeToCompare1.after(TimeToCompare))

            {

                return "1";

            }

            if (TimeToCompare1.before(TimeToCompare))

            {

                return "2";

            }

            if (TimeToCompare1.equals(TimeToCompare))

            {

                return "3";

            }

        } catch (ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return "4";

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

       if(isVisible()) {

           if (isVisibleToUser && isFragmentLoaded) {
               // Load your data here or do network operations here


               userVisible = true;
               apply.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       submitCheck();
                   /* if (!from_time.getText().toString().trim().isEmpty() && !to_time.getText().toString().trim().isEmpty())
                    {


                        String resultcampare = CompareTime(from_time.getText().toString().trim(), to_time.getText().toString().trim());
                        if (!resultcampare.equals("1")) {


                            EmpowerApplication.alertdialog(for_out_time, getContext());

                        }else {
                        calhrs();

                    }


                    }*/


                   }
               });
               cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       getActivity().finish();
                   }
               });

               if(WorkFromHomeActivity.position==1) {

                   if (Utilities.isNetworkAvailable(getActivity())) {


                       GetWorkFlowForWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                   } else {
                       Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                   }
               }
           }
       }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentLoaded=false;



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
        userVisible=false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentLoaded=false;
        userVisible=false;

       /* FragmentManager fm = getActivity().getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached=true;


        isFragmentLoaded=true;
    }

    public String getFromTime(){
        return  from_time.getText().toString().trim();
    }
    public String getToTime(){
        return  to_time.getText().toString().trim();
    }
    public String gethourse(){
        return  calhrs.getText().toString().trim();
    }


    public void GetWorkFlowForWorkFromHomeApp(String employeeId,String token) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> GetWorkFlowdata=new HashMap<String,String>();
        GetWorkFlowdata.put("employeeId",employeeId);
        GetWorkFlowdata.put("token",token);

        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();
        final ArrayList<String> list=new ArrayList<>();

        retrofit2.Call<FinalResponsePoJo> call = apiService.GetWorkFlowForWorkFromHomeApp(GetWorkFlowdata);
        call.enqueue(new Callback<FinalResponsePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<FinalResponsePoJo> call, Response<FinalResponsePoJo> response) {
                pd.dismiss();

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
            }

            @Override
            public void onFailure(retrofit2.Call<FinalResponsePoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

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


    public void WorkFromHomeAppInHrs(String employeeId,String token,String fromdate,String todate,String fromtime,String totime,String WFHAppInHours,String reasonID,String reasonDescription,String isConsiderTimeCross) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();

        WorkFlowdataapp.put("token",token);
        WorkFlowdataapp.put("employeeId",employeeId);
        WorkFlowdataapp.put("fromDate",fromdate);
        WorkFlowdataapp.put("toDate",todate);
        WorkFlowdataapp.put("isFirstHalf","");
        WorkFlowdataapp.put("isSecondHalf","");
        WorkFlowdataapp.put("isWFHAppInHours",WFHAppInHours);
        WorkFlowdataapp.put("fromTime",fromtime);
        WorkFlowdataapp.put("toTime",totime);
        WorkFlowdataapp.put("reasonID",reasonID);
        WorkFlowdataapp.put("isConsiderTimeCross",isConsiderTimeCross);
        Log.e("reasonID",reasonID);//isConsiderTimeCross
        WorkFlowdataapp.put("reasonDescription",reasonDescription);
        retrofit2.Call<WorkFromHomePoJo> call = apiService.WorkFromHomeApp(WorkFlowdataapp);
        call.enqueue(new Callback<WorkFromHomePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<WorkFromHomePoJo> call, Response<WorkFromHomePoJo> response) {
                pd.dismiss();

                if (response.isSuccessful()) {



                    if (response.body().getStatus().equals("1")) {
                        dialogForApplication("WFH application for hours",gethourse(),getContext());


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


    public String getFrom_Date(){
        return date.getText().toString().trim();
    }
    public String getTo_date(){
        return to_date.getText().toString().trim();
    }
    public String get_reason(){
        return reason.getText().toString().trim();
    }
    public String get_Fromtime(){
        return from_time.getText().toString().trim();
    }
    public String get_Totime(){
        return to_time.getText().toString().trim();
    }
    public String get_reason1(){
        return WorkFromHomeInDays.reasonId.get(reason1.getSelectedItemPosition()-1);
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(Utilities.isNetworkAvailable(getActivity())) {


            ReasonWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        }else {
            Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();
        }


    }

    public void ReasonWorkFromHomeApp(String employeeId) {

       /* pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();*/

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);

        reason12=new ArrayList<>();
        reasonId=new ArrayList<String>();

        retrofit2.Call<ReasonPoJo> call = apiService.getReasonWorkFromHomeApp(WorkFlowdataapp);
        call.enqueue(new Callback<ReasonPoJo>() {
            @Override
            public void onResponse(retrofit2.Call<ReasonPoJo> call, Response<ReasonPoJo> response) {
                // pd.dismiss();
                reason12.clear();
                reasonId.clear();

                if (response.isSuccessful()) {
                    reason12.add("Select");

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            reasonId.add(response.body().getData().get(i).getReasonID());
                            reason12.add(response.body().getData().get(i).getReasonTitle());
                        }

                        // Creating adapter for spinner
                        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,reason12);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reason1.setAdapter(arrayAdapter);

                        //EmpowerApplication.alertdialog(response.body().getMessage(), WorkFromHomeActivity.this);

                    }else {
                        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,reason12);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reason1.setAdapter(arrayAdapter);
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
                //HideOutDutyApp
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


    public void WorkFromHomeAppCalHrs(String fromDateTime,String toDateTime,String isConsiderTimeCross,String AppForMultipleDays) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();

        //WorkFlowdataapp.put("token",token);
        WorkFlowdataapp.put("fromDateTime",fromDateTime);
        WorkFlowdataapp.put("toDateTime",toDateTime);
        WorkFlowdataapp.put("isConsiderTimeCross",isConsiderTimeCross);
        WorkFlowdataapp.put("AppForMultipleDays",AppForMultipleDays);
        //WorkFlowdataapp.put("isSecondHalf","");
       // WorkFlowdataapp.put("isWFHAppInHours",WFHAppInHours);
       // WorkFlowdataapp.put("fromTime",fromtime);
       // WorkFlowdataapp.put("toTime",totime);
       // WorkFlowdataapp.put("reasonID",reasonID);
        Log.e("reasonID1",fromDateTime);
        Log.e("reasonID2",toDateTime);
        Log.e("reasonID3",isConsiderTimeCross);
        Log.e("reasonID4",AppForMultipleDays);
        //WorkFlowdataapp.put("reasonDescription",reasonDescription);
        retrofit2.Call<CommanResponsePojo> call = apiService.WFHMulDayInHrs(WorkFlowdataapp);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();

                if (response.isSuccessful()) {



                    if (response.body().getStatus().equals("1")) {
                        calhrs.setText(response.body().getData());
                       // dialogForApplication("WFH application for hours",gethourse(),getContext());


                        //EmpowerApplication.alertdialog(response.body().getMessage(), getContext());

                    }else {
                        calhrs.setText("");
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
                pd.dismiss();
                Log.e("TAG", t.toString());

            }
        });
    }

}

