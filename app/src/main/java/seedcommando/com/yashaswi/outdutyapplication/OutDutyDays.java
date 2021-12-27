package seedcommando.com.yashaswi.outdutyapplication;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

/**
 * Created by commando5 on 9/6/2017.
 */

public class OutDutyDays  extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    EditText in_date, out_date,reason,address;
    TextView no_of_days;
    String indate="",outdate="";
    Date date,now;
    Button apply,cancel;
    View rootView;
   public static boolean  outdutyflag=false,isAttach=false,isFragmentLoaded=false;
   //boolean isAttach=false;
    Context context1;
    Spinner reason1;
   // boolean isFragmentLoaded=false;
    LinearLayout.LayoutParams params,params1;
    //LinearLayout.LayoutParams params1;
    ArrayList<String> reason12,reasonId,level1=null,level2=null,level3=null,level4=null;
    ArrayAdapter arrayAdapter;
    LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4,img1,img2,img3;

    TextInputLayout inputLayoutreason,inputLayoutindate,inputLayoutoutdate;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-yyyy-MM");
    //ArrayList<String> level1=null;
    //ArrayList<String> level2=null;
   // ArrayList<String> level3=null;
   // ArrayList<String> level4=null;

    private ApiInterface apiService;
    ProgressDialog pd;
    private SimpleDateFormat dateFormatter12 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);




    //declaration for datepicker.........

   // private DatePickerDialog fromDatePickerDialog;
   // private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter,dateFormatter1;

    public OutDutyDays() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.out_duty_days_fragment, container, false);

        in_date = rootView.findViewById(R.id.editText_in_date_days);
        reason = rootView.findViewById(R.id.editText_out_duty_remark);
        reason1 = rootView.findViewById(R.id.editText_wfh_reason);

        out_date = rootView.findViewById(R.id.editText_out_date_days);
        address= rootView.findViewById(R.id.editText_out_duty_location);

        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        no_of_days = rootView.findViewById(R.id.txt_out_duty_cal_days);
        inputLayoutreason = rootView.findViewById(R.id.input_layout_floaout_duty_remark);
        inputLayoutindate = rootView. findViewById(R.id.input_layout_floatIn_date);
        inputLayoutoutdate = rootView. findViewById(R.id.input_layout_floaout_date);
        in_date.addTextChangedListener(new MyTextWatcher(in_date));
        out_date.addTextChangedListener(new MyTextWatcher(out_date));
        reason.addTextChangedListener(new MyTextWatcher(reason));

        apply= this.getActivity().findViewById(R.id.apply);
        cancel= this.getActivity().findViewById(R.id.cancel);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //LinearLayout formLayout = (LinearLayout)rootView.findViewById(R.id.linearout1);
        //formLayout.removeAllViews();
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

        if(getArguments()!=null){
            String strtext = getArguments().getString("Date");
            in_date.setText(strtext);
            out_date.setText(strtext);
            Log.e("outdutyDate",strtext);

            try {
                Date d=dateFormatter.parse(strtext);
                //in_date.setText(dateFormatter.format(d));
                indate= strtext;
                date = dateFormatter1.parse(sdf.format(d));
               // out_date.setText(dateFormatter.format(d));
                outdate=strtext;
                now = dateFormatter1.parse(sdf.format(d));
                caldays();
            }catch (ParseException p){
               p.printStackTrace();
           }

        }
        reason1.setOnItemSelectedListener(this);


        if (Utilities.isNetworkAvailable(getActivity())) {
            GetWorkFlowFoRegularizationApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
            ReasonODApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

            //ReasonForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

        }else {
            Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();

        }

       /* if(getArguments()!=null){
            level1= getArguments().getStringArrayList("array1");
            level2= getArguments().getStringArrayList("array2");
            level3= getArguments().getStringArrayList("array3");
            level4= getArguments().getStringArrayList("array4");

            addViews();


        }
*/

        return rootView;

    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        in_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePicker();
            }
        });
       out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePicker1();

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCheck();
               /* if(!indate.isEmpty()&&!outdate.isEmpty()) {
                    caldays();
                    if(outdutyflag){

                    }
                }else {
                    Toast.makeText(getContext(),"please select in_date",Toast.LENGTH_LONG).show();
                }*/



            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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
        date.show(getFragmentManager(),"Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

           String date1=(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));

         try {
             Date d=dateFormatter1.parse(date1);
             in_date.setText(dateFormatter.format(d));
             indate= date1;
             date = sdf.parse(date1);
         }catch (ParseException p){
             p.printStackTrace();
         }

            if(outdate.equals("")) {
                Toast.makeText(getContext(),"please select out_date",Toast.LENGTH_LONG).show();

            }else {
                caldays();

            }
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
        date.show(getFragmentManager(),"Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

           // out_date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(year));

            String date1=(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));

            try {
                Date d=dateFormatter1.parse(date1);
                out_date.setText(dateFormatter.format(d));
                outdate=date1;
                now = sdf.parse(date1);
            }catch (ParseException p){
                p.printStackTrace();
            }
            if(!indate.equals("")) {
                caldays();
            }else {
                Toast.makeText(getContext(),"please select in_date",Toast.LENGTH_LONG).show();
            }
        }
    };

    public void caldays(){

        if (now.getTime() >= date.getTime()) {
            outdutyflag=true;
            if(Utilities.isNetworkAvailable(getActivity())) {
                getNo_Of_DaysData(getFromDate(),gettoDate());
            }else {
                Toast.makeText(getActivity(),"No Internet Connection...",Toast.LENGTH_LONG).show();
            }

            // long days = getDateDiff(date, now, TimeUnit.DAYS);
           // no_of_days.setText(Long.toString(days+1));
        } else {
            //Log.d("", "please enter valid start and end date");
            //EmpowerApplication.alertdialog("To Date should be greater Than From Date",getContext());
            Toast.makeText(getContext(), "please enter valid start and end date", Toast.LENGTH_LONG).show();

        }

        //return "ERROR";
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onClick(View v) {

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


                case R.id.editText_in_date_days:
                    validateindate();
                    break;

                case R.id.editText_out_date_days:
                    validateoutdate();
                    break;
                case R.id.editText_out_duty_remark:
                    validatereason();
                    break;

            }
        }
    }


    public void onSubmit(View view){

        submitCheck();
    }
    public  void submitCheck() {



        if (!validateindate()) {
            return;
        }
        if (!validateoutdate()) {
            return;
        }
        /*if (!validatereason()) {
            return;
        }*/
        if (!validatereason1()) {
            return;
        }
        if(now.getTime() >= date.getTime()) {
            if (address.getText().toString().length() <= 50) {
                if (reason.getText().toString().length() <= 50) {
                    if(!level1.isEmpty()) {
                    //Toast.makeText(getContext(), "Application Submitted sucessfully", Toast.LENGTH_LONG).show();
                    if (Utilities.isNetworkAvailable(getActivity())) {
                        OutDutyApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),getFromDate(),gettoDate(),getAddress(),getreason(),get_reason1());
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
            EmpowerApplication.alertdialog("To Date should be greater Than From Date",getContext());

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



    private boolean validateindate() {
        if (in_date.getText().toString().trim().isEmpty()) {
            inputLayoutindate.setError(getString(R.string.err_msg_indate));
            in_date.requestFocus();
            return false;
        } else {
            inputLayoutindate.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateoutdate() {
        if (out_date.getText().toString().trim().isEmpty()) {
            inputLayoutoutdate.setError(getString(R.string.err_msg_outdate));
            out_date.requestFocus();
            return false;
        } else {
            inputLayoutoutdate.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFragmentLoaded) {
            // Load your data here or do network operations here
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitCheck();
                    if(!indate.isEmpty()&&!outdate.isEmpty()) {
                        caldays();
                        if(outdutyflag){

                        }
                    }else {
                        Toast.makeText(getContext(),"please select in_date",Toast.LENGTH_LONG).show();
                    }



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
    public void onAttach(Context context) {
        super.onAttach(context);

        isAttach=true;
        isFragmentLoaded=true;


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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentLoaded=false;


    }
    public String getNo_of_days(){
        return no_of_days.getText().toString().trim();

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
                linearLayout.removeAllViews();
                linearLayout2.removeAllViews();
                linearLayout3.removeAllViews();
                linearLayout4.removeAllViews();
                //img1.removeAllViews();
                //img2.removeAllViews();
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
                TextView textView = new TextView (getActivity());
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
                TextView textView = new TextView (getActivity());
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

                TextView textView = new TextView (getActivity());
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

    public void getNo_Of_DaysData( String fromdate,String todate) {
        //pd = new ProgressDialog(Leave_Application.this);
        // pd.setMessage("Loading....");
        // pd.show();
        Map<String,String> noofdaysdata=new HashMap<String,String>();

        noofdaysdata.put("fromDate",fromdate);
        noofdaysdata.put("toDate",todate);


        final ArrayList<String> arrayList=new ArrayList<>();
        //leavetypecode=new ArrayList<>();

        retrofit2.Call<CommanResponsePojo> call = apiService.getNoOfDaysOutDuty(noofdaysdata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                // pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());
                //arrayList.clear();

                if(response.isSuccessful()) {
                    Log.d("Usererererrrrre: ", response.body().getMessage());
                    if (response.body().getStatus().equals("1")) {
                        //txt_no_of_days.setText(response.body().getData());
                        no_of_days.setText(response.body().getData());



                    } else {
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
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(),getActivity());

            }
        });
    }

    public String getFromDate() {
        return in_date.getText().toString();
    }

    public String gettoDate() {
        return out_date.getText().toString();
    }
    public String getAddress() {
        return address.getText().toString();
    }

    public String getreason() {
        return reason.getText().toString();
    }


    public void OutDutyApp(String employeeId,String token,String fromdate,String todate,String address,String reasonDescription,String reasonid) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();

        WorkFlowdataapp.put("token",token);
        WorkFlowdataapp.put("employeeId",employeeId);
        WorkFlowdataapp.put("fromDate",fromdate);
        WorkFlowdataapp.put("toDate",todate);
        WorkFlowdataapp.put("IsODAppInHours","False");
        WorkFlowdataapp.put("fromTime","");
        WorkFlowdataapp.put("toTime","");
        WorkFlowdataapp.put("outDutyAddress",address);//reasonID
        WorkFlowdataapp.put("reasonID",reasonid);

        WorkFlowdataapp.put("reasonDescription",reasonDescription);
        retrofit2.Call<CommanResponsePojo> call = apiService.OutDutyApplication(WorkFlowdataapp);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();

                if (response.isSuccessful()) {



                    if (response.body().getStatus().equals("1")) {
                        dialogForApplication("OD Application For Day",getNo_of_days(),getContext());


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
                if(HomeFragment.key.contains("HideOutDutyApp")) {
                    int index = HomeFragment.key.indexOf("HideOutDutyApp");
                    Intent intentSurvey = new Intent(getContext(), ApplicationStatusActivity.class);
                    intentSurvey.putExtra("ARG_PAGE", index );
                    startActivity(intentSurvey);
                    dialog1.dismiss();
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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






