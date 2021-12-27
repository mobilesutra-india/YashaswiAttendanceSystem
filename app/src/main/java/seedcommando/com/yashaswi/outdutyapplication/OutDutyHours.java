package seedcommando.com.yashaswi.outdutyapplication;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.applicationstatus.ApplicationStatusActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.getreason.ReasonPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

import static seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization.pad;

/**
 * Created by commando5 on 9/6/2017.
 */

public class OutDutyHours  extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    EditText date, from_time, to_time,reason,address;
    SimpleDateFormat dateFormatter,dateFormatter1;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    String fromdate="",todate="";
    Date startDate,endDate;
    TextView calhrs;
    Button apply,cancel;
    private boolean isFragmentLoaded=false;
    public static String for_out_time="To Time should be greater than From Time";
    boolean userVisible=false,isAttached=false;
   // boolean isAttached=false;
    View rootView;
    Spinner reason1;
    ArrayList<String> reason12,reasonId,level1=null,level2=null,level3=null,level4=null;
    ArrayAdapter arrayAdapter;
    LinearLayout.LayoutParams params,params1;
    //LinearLayout.LayoutParams params1;
    LinearLayout linearLayout4, linearLayout5, linearLayout6, linearLayout7,img1,img2,img3;

    //ArrayList<String> level1=null;
   // ArrayList<String> level2=null;
    //ArrayList<String> level3=null;
    //ArrayList<String> level4=null;
    private ApiInterface apiService;
    ProgressDialog pd;


    TextInputLayout inputLayoutreason,inputLayoutindate,inputLayoutoutdate,inputLayoutdate;


    public OutDutyHours() {
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
               rootView = inflater.inflate(R.layout.out_duty_hours, container, false);


               date = rootView.findViewById(R.id.editText_date_hrs);
               from_time = rootView.findViewById(R.id.editText_in_time_hrs);
               to_time = rootView.findViewById(R.id.editText_out_time_hrs);
               address = rootView.findViewById(R.id.editText_out_date_location_hrs);
               calhrs = rootView.findViewById(R.id.txt_out_duty_cal_days);
               reason = rootView.findViewById(R.id.editText_out_date_remark);
               reason1 = rootView.findViewById(R.id.editText_wfh_reason);
               dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
               dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
               apply = this.getActivity().findViewById(R.id.apply);
               cancel = this.getActivity().findViewById(R.id.cancel);
               inputLayoutreason = rootView.findViewById(R.id.input_layout_floatout_duty_remark);
               inputLayoutindate = rootView.findViewById(R.id.input_layout_floatout_duty_from_time);
               inputLayoutdate = rootView.findViewById(R.id.input_layout_floatIn_date);
               inputLayoutoutdate = rootView.findViewById(R.id.input_layout_floatout_duty_to_time);
               from_time.addTextChangedListener(new MyTextWatcher(from_time));
               to_time.addTextChangedListener(new MyTextWatcher(to_time));
               reason.addTextChangedListener(new MyTextWatcher(reason));
               date.addTextChangedListener(new MyTextWatcher(date));
        apiService = ApiClient.getClient().create(ApiInterface.class);
       // LinearLayout formLayout = (LinearLayout)rootView.findViewById(R.id.linearout1);
       // formLayout.removeAllViews();

        linearLayout4 = rootView.findViewById(R.id.linearout);
        linearLayout5 = rootView.findViewById(R.id.linearout2);
        linearLayout6 = rootView.findViewById(R.id.linearout3);
        linearLayout7 = rootView.findViewById(R.id.linearout4);
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

            Log.e("outdutyDate", strtext);
        }
        reason1.setOnItemSelectedListener(this);


        /*if (Utilities.isNetworkAvailable(getActivity())) {
            GetWorkFlowFoRegularizationApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
            //ReasonForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

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

            String date1=(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));

            try {
                Date d=dateFormatter1.parse(date1);
                date.setText(dateFormatter.format(d));
               // indate= date1;
                //date = sdf.parse(date1);
            }catch (ParseException p){
                p.printStackTrace();
            }
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


                startDate = simpleDateFormat.parse(fromdate);
            }catch (ParseException p){
                p.printStackTrace();
            }

            if(todate.equals("")) {
                Toast.makeText(getContext(),"please select to_time",Toast.LENGTH_LONG).show();

            }else {
                if (!from_time.getText().toString().trim().isEmpty() && !to_time.getText().toString().trim().isEmpty())
                {

                    String resultcampare = CompareTime(from_time.getText().toString().trim(), to_time.getText().toString().trim());
                        if (!resultcampare.equals("1")) {

                            //EmpowerApplication.alertdialog(for_out_time, getContext());

                        }else {
                            calhrs();

                        }

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
                endDate = simpleDateFormat.parse(todate);
            }catch (ParseException p){
                p.printStackTrace();
            }

            if(fromdate.equals("")) {
                Toast.makeText(getContext(),"please select from_time",Toast.LENGTH_LONG).show();

            }else {
                if (!from_time.getText().toString().trim().isEmpty() && !to_time.getText().toString().trim().isEmpty())
                {


                    String resultcampare = CompareTime(from_time.getText().toString().trim(), to_time.getText().toString().trim());
                    if (!resultcampare.equals("1")) {


                       // EmpowerApplication.alertdialog(for_out_time, getContext());

                    }else {
                        calhrs();

                    }


                }

            }
        }
    };

    public void calhrs(){

        long difference = endDate.getTime() - startDate.getTime();
        if(difference<0)
        {
            try {
            Date dateMax = simpleDateFormat.parse("12:00");
            Date dateMin = simpleDateFormat.parse("00:00");
                difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
            }catch (ParseException p){
                p.printStackTrace();
            }

        }
        int days = (int) (difference / (1000*60*60*12));
        int hours = (int) ((difference - (1000*60*60*12*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*12*days) - (1000*60*60*hours)) / (1000*60);
        Log.i("log_tag","Hours: "+hours+", Mins: "+min);
        //calhrs.setText(Integer.toString(hours));
        String h1=null,m1=null;
        if(hours<10){
            h1="0"+hours;
        }
        else {
            h1= String.valueOf(hours);
        }
        if(min<10){
            m1="0"+min;
        }
        else {
            m1= String.valueOf(min);
        }
        calhrs.setText(h1+":"+m1);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

                case R.id.editText_date_hrs:
                    validatedate();
                    break;
                case R.id.editText_in_time_hrs:
                    validateintime();
                    break;

                case R.id.editText_out_time_hrs:
                    validateouttime();
                    break;
                case R.id.editText_out_date_remark:
                    validatereason();
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
        if (!validateouttime()) {
            return;
        }
        if (!validatereason1()) {
            return;
        }
        String resultcampare = CompareTime(from_time.getText().toString().trim(), to_time.getText().toString().trim());

        if(resultcampare.equals("1")) {
            if (address.getText().toString().length() <= 50) {
                if (reason.getText().toString().length() <= 50) {
                    if(!level1.isEmpty()) {
                   // Toast.makeText(getContext(), "Application Submitted sucessfully", Toast.LENGTH_LONG).show();
                   // EmpowerApplication.dialogForApplication("OutDuty application for hours",gethourse(),getContext());

                    if (Utilities.isNetworkAvailable(getActivity())) {
                        OutDutyApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),getFromDate(),getFromDate(),getFromTime(),getToTime(),getAddress(),getreason(),get_reason1());
                        //ReasonForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                    }else {
                        Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();

                    }
                    }else {
                        EmpowerApplication.alertdialog("No Work Flow Found For This Employee",this.getContext());


                    }

                } else {
                    reason.setError("reason should be less than 50 charactor");
                }
            } else {
                address.setError("Address should be less than 50 charactor");
            }
        }else {
            EmpowerApplication.alertdialog(for_out_time, getContext());
        }


    }
    private  boolean validatereason() {
        if (reason.getText().toString().trim().isEmpty()) {
            inputLayoutreason.setError(getString(R.string.err_msg_reason));
            reason.requestFocus();
            return false;
        } else {
            inputLayoutreason.setErrorEnabled(false);
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
            inputLayoutdate.setError(getString(R.string.err_msg_date));
            date.requestFocus();
            return false;
        } else {
            inputLayoutdate.setErrorEnabled(false);
        }

        return true;
    }
    public String CompareTime(String strTimeToCompare,String endTimeToCompare)

    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        try {

            Date TimeToCompare = sdf.parse(strTimeToCompare);


            Date TimeToCompare1 = sdf.parse(endTimeToCompare);

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



        if (isVisibleToUser && isFragmentLoaded) {
            // Load your data here or do network operations here
            userVisible=true;
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


            if (Utilities.isNetworkAvailable(getActivity())) {
                GetWorkFlowFoRegularizationApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                ReasonODApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                //ReasonForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

            }else {
                Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();

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

    public void GetWorkFlowFoRegularizationApp(String employeeId,String token) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> GetWorkFlowdata=new HashMap<String,String>();
        GetWorkFlowdata.put("employeeId",employeeId);
        GetWorkFlowdata.put("token",token);

        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();

        retrofit2.Call<FinalResponsePoJo> call = apiService.GetWorkFlowForOutDutyApp(GetWorkFlowdata);
        call.enqueue(new Callback<FinalResponsePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<FinalResponsePoJo> call, Response<FinalResponsePoJo> response) {
                pd.dismiss();
                level1.clear();
                level2.clear();
                level3.clear();
                level4.clear();
                linearLayout4.removeAllViews();
                linearLayout5.removeAllViews();
                linearLayout6.removeAllViews();
                linearLayout7.removeAllViews();
               // img1.removeAllViews();
               // img2.removeAllViews();
               // img3.removeAllViews();
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

                        //setupViewPager(viewPager);
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

            }
        });
    }
    public void addViews(){

        if(!(level1.size() ==0)){
            for(int i=0;i<level1.size();i++){

                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (getActivity());
                textView.setGravity(Gravity.CENTER);


                // Add image path from drawable folder.
                //CircleImageView.
                // imageview.setImageResource(R.drawable.em_logo);
                // imageview.setLayoutParams(params);
                // RoundedImageView.getCroppedBitmap(R.drawable.em_logo,10)

                //linearLayout.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout4.addView(textView);
                Log.e("INaddview",level1.get(i));
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
                TextView textView = new TextView (getActivity());
                textView.setGravity(Gravity.CENTER);
                // Add image path from drawable folder.
                //imageview.setImageResource(R.drawable.em_logo);
                //imageview.setLayoutParams(params);

                //linearLayout2.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout5.addView(textView);
                textView.setText(level2.get(i));
            }
        }
        if(!(level3.size() ==0)){
            ImageView imageView=rootView.findViewById(R.id.arrow2);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level3.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (getActivity());
                textView.setGravity(Gravity.CENTER);
                // Add image path from drawable folder.
                //imageview.setImageResource(R.drawable.em_logo);
                //imageview.setLayoutParams(params);

                //linearLayout3.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout6.addView(textView);
                textView.setText(level3.get(i));
            }
        }
        if(!(level4.size() ==0)){
            ImageView imageView=rootView.findViewById(R.id.arrow3);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level4.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);

                TextView textView = new TextView (getActivity());
                textView.setGravity(Gravity.CENTER);
                // Add image path from drawable folder.
                // imageview.setImageResource(R.drawable.em_logo);
                // imageview.setLayoutParams(params);

                //linearLayout4.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout7.addView(textView);
                textView.setText(level4.get(i));
            }
        }

    }

    public void OutDutyApp(String employeeId,String token,String fromdate,String todate,String fromtime,String totime,String address,String reasonDescription,String reasonid) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();

        WorkFlowdataapp.put("token",token);
        WorkFlowdataapp.put("employeeId",employeeId);
        WorkFlowdataapp.put("fromDate",fromdate);
        WorkFlowdataapp.put("toDate",todate);
        WorkFlowdataapp.put("IsODAppInHours","True");
        WorkFlowdataapp.put("fromTime",fromtime);
        WorkFlowdataapp.put("toTime",totime);
        WorkFlowdataapp.put("outDutyAddress",address);
        WorkFlowdataapp.put("reasonID",reasonid);

        WorkFlowdataapp.put("reasonDescription",reasonDescription);
        retrofit2.Call<CommanResponsePojo> call = apiService.OutDutyApplication(WorkFlowdataapp);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();

                if (response.isSuccessful()) {



                    if (response.body().getStatus().equals("1")) {
                       dialogForApplication("OD Application For Hours",calhrs.getText().toString(),getContext());


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
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(),getActivity());

            }
        });
    }
    public String getFromDate() {
        return date.getText().toString();
    }
    public String getAddress() {
        return address.getText().toString();
    }

    public String getreason() {
        return reason.getText().toString();
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
                if(HomeFragment.key.contains("HideOutDutyApp")) {
                    int index = HomeFragment.key.indexOf("HideOutDutyApp");
                    Intent intentSurvey = new Intent(getContext(), ApplicationStatusActivity.class);
                    intentSurvey.putExtra("ARG_PAGE", index);
                    startActivity(intentSurvey);
                    dialog1.dismiss();
                }
            }
        });
    }

    public void ReasonODApp(String employeeId) {

       /* pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();*/

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);

        reason12=new ArrayList<>();
        reasonId=new ArrayList<String>();

        retrofit2.Call<ReasonPoJo> call = apiService.getReasonODApp(WorkFlowdataapp);
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
    public String get_reason1(){
        return reasonId.get(reason1.getSelectedItemPosition()-1);
    }

}




