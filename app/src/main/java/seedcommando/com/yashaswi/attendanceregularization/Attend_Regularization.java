package seedcommando.com.yashaswi.attendanceregularization;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.google.gson.Gson;

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
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.daystatus.Data;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.daystatus.DayStatusReg;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.Regularization;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.getreasonpojo.Reason;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.realtimedata.RealTime;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.shift.ShiftType;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

import static android.R.layout.simple_spinner_item;

/**
 * Created by commando5 on 8/30/2017.
 */

public class Attend_Regularization extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
   // private static String for_in_date="In Date should be greater than or equal to Date";
   // private static String for_out_date="Out Date should be greater than or equal to Date";
    private static String for_out_time="Out Time should be greater than or equal to InTime";
    Spinner reason_type,shift_type;
    EditText date,in_date,out_date,in_time,out_time,remark,realintime,realouttime;
    private DatePickerDialog inDatePickerDialog,toDatePickerDialog,reg_DatePickerDialog;
    //private DatePickerDialog toDatePickerDialog;
   // private DatePickerDialog reg_DatePickerDialog;



    private SimpleDateFormat dateFormatter;

    //for time picker dailogue........
    //private int hour;
    private int minute, hour;
    private TimePicker timePicker1;
    static final int TIME_DIALOG_ID_From = 999;
    static final int TIME_DIALOG_ID_To = 888;
    Button apply,cancel;
    TextInputLayout inputLayoutdate,inputLayoutintime,inputLayoutindate,inputLayoutouttime,inputLayoutoutdate;
    //.................................


    private ApiInterface apiService;
    ProgressDialog pd;
    static ArrayList<String> daystatus=null;


    ArrayList<String> level1=null,level2=null,level3=null,level4=null,reason=null,reasonId=null,shift=null,shiftId=null;
    //ArrayList<String> level2=null;
    //ArrayList<String> level3=null;
    //ArrayList<String> level4=null;
   // ArrayList<String> reason=null;
    //ArrayList<String> reasonId=null;
    //ArrayList<String> shift=null;
    //ArrayList<String> shiftId=null;

    LinearLayout.LayoutParams params,params1;
    //LinearLayout.LayoutParams params1;
    Spinner day_status_spinner;
    String DayStatusValues = null;
    LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4,shiftlayout , DayStatuslayout;
    private ArrayList<Data> DayStatusID;
    String DayStatusId;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_regularization);
        reason_type = findViewById(R.id.reason_type_spineer);
        shift_type = findViewById(R.id.shift_type_spineer);
        in_date = findViewById(R.id.editText_in_date);
        date = findViewById(R.id.editText_date);
        shiftlayout = findViewById(R.id.shiftlayout);
        DayStatuslayout =findViewById(R.id.DayStatuslayout);

        out_date = findViewById(R.id.editText_out_date);
        in_time = findViewById(R.id.editText_in_time);
        realouttime = findViewById(R.id.editText_out_real_time);
        realintime = findViewById(R.id.editText_in_real_time);
        out_time = findViewById(R.id.editText_out_time);
         day_status_spinner = findViewById(R.id.day_status_spinner);
        remark = findViewById(R.id.editText_attd_reg_remark);
        apply = findViewById(R.id.apply);
        cancel = findViewById(R.id.cancel);
        inputLayoutdate = findViewById(R.id.input_layout_float_date);
        inputLayoutindate = findViewById(R.id.input_layout_floatIn_date);
        inputLayoutintime = findViewById(R.id.input_layout_floain_time);
        inputLayoutoutdate = findViewById(R.id.input_layout_floaout_date);
        inputLayoutouttime = findViewById(R.id.input_layout_floatout_time);

        date.addTextChangedListener(new MyTextWatcher(date));
        in_date.addTextChangedListener(new MyTextWatcher(in_date));
        in_time.addTextChangedListener(new MyTextWatcher(in_time));
        out_date.addTextChangedListener(new MyTextWatcher(out_date));
        out_time.addTextChangedListener(new MyTextWatcher(out_time));
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //shift_type.addTextChangedListener(new MyTextWatcher(shift_type));
        // out_time.addTextChangedListener(new MyTextWatcher(out_time));

        //Toolbar declaration............

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendance Reg Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);

        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowShifSelectionInAttendanceRegularizationApp")).equals("0")){
          shiftlayout.setVisibility(View.GONE);
        }else {
            shiftlayout.setVisibility(View.VISIBLE);
        }

        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("DisplayStatusOptionInAttRegApp")).equals("0")){
            DayStatuslayout.setVisibility(View.GONE);
            Toast.makeText(Attend_Regularization.this, ""+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("DisplayStatusOptionInAttRegApp")), Toast.LENGTH_LONG).show();

        }else {
            DayStatuslayout.setVisibility(View.VISIBLE);
            Toast.makeText(Attend_Regularization.this, ""+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("DisplayStatusOptionInAttRegApp")), Toast.LENGTH_LONG).show();

        }


            if (Utilities.isNetworkAvailable(Attend_Regularization.this)) {
                // RegAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                DayStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

            } else {
                Toast.makeText(Attend_Regularization.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
            }




        linearLayout = findViewById(R.id.linearout);
        linearLayout2 = findViewById(R.id.linearout2);
        linearLayout3 = findViewById(R.id.linearout3);
        linearLayout4 = findViewById(R.id.linearout4);
        params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params1 = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        params.setMargins(0, 20, 0, 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (Utilities.isNetworkAvailable(this)) {
            GetWorkFlowFoRegularizationApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
            ReasonForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));



        }else {
            Toast.makeText(Attend_Regularization.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

        }
      Bundle  extras = getIntent().getExtras();

        String value;
        if (extras != null) {
            value = extras.getString("Date");
           date.setText(value);
           in_date.setText(value);
           out_date.setText(value);
            if (Utilities.isNetworkAvailable(Attend_Regularization.this)) {
                ShiftForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),date.getText().toString());


                RealinTime(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),date.getText().toString());

            }else {
                Toast.makeText(Attend_Regularization.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

            }

        }
        //................................


        //for From_date and To-date edit text..............
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        setDateTimeField();
        // Spinner click listener for for reason_type and shift_type................................................
        reason_type.setOnItemSelectedListener(this);
      //  loadspinnerdata_reason_type(); // Spinner Drop down elements for reson types
        //  shift_type.setOnClickListener(this);
       // loadspinnerdata_shift_type(); //load spinner drop down list for shift_types


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCheck();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        day_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
                DayStatusValues =day_status_spinner.getSelectedItem().toString();

                if(index > 0) {
                    try {
                        DayStatusId = DayStatusID.get(index-1).getDayStatusID();
                      //  Toast.makeText(Attend_Regularization.this, ""+DayStatusId, Toast.LENGTH_SHORT).show();

                    }catch (IndexOutOfBoundsException i) {

                    }
                }





            }



            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //in_time............

        in_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // showDialog(TIME_DIALOG_ID_From);

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(
                        Attend_Regularization.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                String am_pm = "";

                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                                datetime.set(Calendar.MINUTE, selectedMinute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";

                                String strHrsToShow = (datetime
                                        .get(Calendar.HOUR) == 0) ? "12"
                                        : datetime.get(Calendar.HOUR) + "";

                                in_time.setText(strHrsToShow + ":"
                                        + pad(datetime.get(Calendar.MINUTE))
                                        + " " + am_pm);
//                                realintime.setText(strHrsToShow + ":"
//                                        + pad(datetime.get(Calendar.MINUTE))
//                                        + " " + am_pm);

                                /*if (out_time.getText().toString().trim().equals("")) {

                                } else {
                                    if (!in_date.getText().toString().trim().isEmpty() && !out_date.getText().toString().trim().isEmpty()) {
                                        if (in_date.getText().toString().trim().equals(out_date.getText().toString().trim())) {
                                            String resultcampare = CompareTime(in_time.getText().toString().trim(), out_time.getText().toString().trim());
                                            if (!resultcampare.equals("1")) {

                                                EmpowerApplication.alertdialog(for_out_time, Attend_Regularization.this);

                                            }
                                        }
                                    } else {
                                        if (!validateindate())
                                            return;
                                        if (!validateoutdate())
                                            return;
                                    }
                                }*/
                                // edtxt_time.setTextColor(Color.parseColor("#000000"));

                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }
        });

        out_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // showDialog(TIME_DIALOG_ID_To);

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(
                        Attend_Regularization.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                String am_pm = "";

                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                                datetime.set(Calendar.MINUTE, selectedMinute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";

                                String strHrsToShow = (datetime
                                        .get(Calendar.HOUR) == 0) ? "12"
                                        : datetime.get(Calendar.HOUR) + "";

                                out_time.setText(strHrsToShow + ":"
                                        + pad(datetime.get(Calendar.MINUTE))
                                        + " " + am_pm);
//                                realouttime.setText(strHrsToShow + ":"
//                                        + pad(datetime.get(Calendar.MINUTE))
//                                        + " " + am_pm);

                                /*if (in_time.getText().toString().trim().equals("")) {
                                    *//*if(!validateintime())
                                        return;*//*

                                } else {
                                    if (!in_date.getText().toString().trim().isEmpty() && !out_date.getText().toString().trim().isEmpty()) {
                                        if (in_date.getText().toString().trim().equals(out_date.getText().toString().trim())) {
                                            String resultcampare = CompareTime(in_time.getText().toString().trim(), out_time.getText().toString().trim());
                                            if (!resultcampare.equals("1")) {

                                                EmpowerApplication.alertdialog(for_out_time, Attend_Regularization.this);

                                            }
                                        }
                                    } else {
                                        if (!validateindate())
                                            return;
                                        if (!validateoutdate())
                                            return;
                                    }
                                }*/

                                // edtxt_time.setTextColor(Color.parseColor("#000000"));

                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID_From:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerFromListener, hour,
                        minute, false);

            case TIME_DIALOG_ID_To:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerToListener, hour,
                        minute, false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerFromListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
            in_time.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));

           String In_time = in_time.getText().toString();
           // Toast.makeText(getApplicationContext(),"In Time:"+In_time,Toast.LENGTH_LONG).show();



            // set current time into timepicker
            timePicker1.setCurrentHour(hour);
            timePicker1.setCurrentMinute(minute);

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerToListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
        out_time.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));

           String Out_time =out_time.getText().toString();
           // Toast.makeText(getApplicationContext(),"In Time:"+Out_time,Toast.LENGTH_LONG).show();



            // set current time into timepicker
            timePicker1.setCurrentHour(hour);
            timePicker1.setCurrentMinute(minute);

        }
    };

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void setDateTimeField() {
        in_date.setOnClickListener(this);
        out_date.setOnClickListener(this);
        date.setOnClickListener(this);
//        date.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        inDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                in_date.setText(dateFormatter.format(newDate.getTime()));

               /* if (date.getText().toString().trim().equals("")) {
                    Toast.makeText(Attend_Regularization.this, "please select Date", Toast.LENGTH_LONG).show();
                    date.requestFocus();
                } else {
                   boolean status3= comparedate(date.getText().toString().trim(), in_date.getText().toString().trim());
                    if(!status3){
                        EmpowerApplication.alertdialog(for_in_date, Attend_Regularization.this);

                    }
                }

                if (!in_time.getText().toString().trim().isEmpty() && !out_time.getText().toString().trim().isEmpty())
                {

                        if (in_date.getText().toString().trim().equals(out_date.getText().toString().trim())) {
                            String resultcampare = CompareTime(in_time.getText().toString().trim(), out_time.getText().toString().trim());
                            if (!resultcampare.equals("1")) {

                                EmpowerApplication.alertdialog(for_out_time, Attend_Regularization.this);

                            }

                    }
                }*/
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                out_date.setText(dateFormatter.format(newDate.getTime()));
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
               /* if(date.getText().toString().trim().equals("")){
                    Toast.makeText(Attend_Regularization.this,"please select Date",Toast.LENGTH_LONG).show();
                    date.requestFocus();
                }
                else {
                boolean status2=comparedate(date.getText().toString().trim(),out_date.getText().toString().trim());
                if(!status2){
                    EmpowerApplication.alertdialog(for_out_date, Attend_Regularization.this);

                }
                }*/
               /* if (!in_time.getText().toString().trim().isEmpty() && !out_time.getText().toString().trim().isEmpty())
                {

                    if (in_date.getText().toString().trim().equals(out_date.getText().toString().trim())) {
                        String resultcampare = CompareTime(in_time.getText().toString().trim(), out_time.getText().toString().trim());
                        if (!resultcampare.equals("1")) {

                          //  EmpowerApplication.alertdialog(for_out_time, Attend_Regularization.this);

                        }

                    }
                }*/

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));




        //for date field ..................
        reg_DatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
              //  in_date.setText(dateFormatter.format(newDate.getTime()));
              //  out_date.setText(dateFormatter.format(newDate.getTime()));

//                realintime.setText("");
//                in_time.setText("");
//                in_time.setClickable(true);
//                in_time.setEnabled(true);
//                realouttime.setText("");
//                out_time.setText("");
//                out_time.setClickable(true);
//                out_time.setEnabled(true);

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");

                if(!date.getText().toString().trim().isEmpty()) {
                    if (Utilities.isNetworkAvailable(Attend_Regularization.this)) {
                        ShiftForRegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),date.getText().toString());
                        RealinTime(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),date.getText().toString());



                    }else {
                        Toast.makeText(Attend_Regularization.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

                    }

                    String checkdate = comparedate1(date.getText().toString().trim(), format1.format(c.getTime()));
                   /*if(checkdate.equals("0")) {


                        if(in_date.getText().toString().trim().equals("")){
                            //Toast.makeText(Attend_Regularization.this,"please select Date",Toast.LENGTH_LONG).show();
                            //date.requestFocus();
                            if(out_date.getText().toString().trim().equals("")) {

                            }else {
                                boolean status=  comparedate(date.getText().toString().trim(), out_date.getText().toString().trim());
                                if (!status){
                                    EmpowerApplication.alertdialog(for_out_date, Attend_Regularization.this);

                                }

                            }
                        }
                        else {
                            boolean status1= comparedate(date.getText().toString().trim(), in_date.getText().toString().trim());
                            if (!status1){
                                EmpowerApplication.alertdialog(for_in_date, Attend_Regularization.this);

                            }
                        }

                    }*/

                }




                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        reg_DatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());

        //.................................

    }



    public void onClick(View view) {
        if(view == in_date) {
            inDatePickerDialog.show();
        }
        else if(view == out_date) {
            toDatePickerDialog.show();
        }
        else if(view == date) {
            reg_DatePickerDialog.show();
        }
    }
    //calendar end..................................


/*public void loadspinnerdata_reason_type(){

    // Spinner Drop down elements for reson types
    List  categories_reason_type = new ArrayList();
    categories_reason_type.add("select");
    categories_reason_type.add("Forget to punch");
    categories_reason_type.add("Card not working");
    categories_reason_type.add("Device not working");
    categories_reason_type.add("Other");

// ArrayAdapter<String> dataAdapter_reason_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_reason_type);
ArrayAdapter dataAdapter_reason_type= new ArrayAdapter (this,android.R.layout.simple_spinner_item,categories_reason_type);
    // Drop down layout style - list view with radio button
    dataAdapter_reason_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // attaching data adapter to spinner
    reason_type.setAdapter(dataAdapter_reason_type);






}*/

    /*public void loadspinnerdata_shift_type(){

        // Spinner Drop down elements for shift type
        List  categories_shift_type = new ArrayList();
        categories_shift_type .add("select");
        categories_shift_type .add("Morning");
        categories_shift_type .add("Night");
        categories_shift_type .add("Flex");
        categories_shift_type .add("Afternoon");
        categories_shift_type .add("other");



       *//* for shift type.........*//*


       // ArrayAdapter dataAdapter_shift_type = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories_shift_type);

        // Drop down layout style - list view with radio button
         //dataAdapter_shift_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
       // shift_type.setAdapter(dataAdapter_shift_type);


    }*/


    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();



    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //spinner end..............................


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setVisible(false);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                case R.id.editText_date:
                    validatedate();
                    break;

                case R.id.editText_in_date:
                    validateindate();
                    break;
                case R.id.editText_in_time:
                    validateintime();
                    break;
                case R.id.editText_out_date:
                    validateoutdate();
                    break;
                case R.id.editText_out_time:
                    validateouttime();
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

        if (!validateindate()) {
            return;
        }
        if (!validateintime()) {
            return;
        }
        if (!validateoutdate()) {
            return;
        }
        if (!validateouttime()) {
            return;
        }
        if (!validatereason()) {
            return;
        }
 //validateDayStatus()

        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("DisplayStatusOptionInAttRegApp")).equals("0")){

        }else {
            if (!validateDayStatus()) {
                return;
            }
        }

        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowShifSelectionInAttendanceRegularizationApp")).equals("0")){
            //shiftlayout.setVisibility(View.GONE);
        }else {
            if (!validateshift()) {
                return;
            }
        }

     // String date1=  comparedate(date.getText().toString().trim(), in_date.getText().toString().trim(),for_in_date);
          Calendar c=Calendar.getInstance();
        String checkdate = comparedate1(date.getText().toString().trim(), dateFormatter.format(c.getTime()));
        if(checkdate.equals("1")){
            EmpowerApplication.alertdialog("Attendance Regularization for future date is not allowed",Attend_Regularization.this);
        }else {
            if (checkdate.equals("2")) {
                EmpowerApplication.alertdialog("Attendance Regularization for current date is not allowed", Attend_Regularization.this);
            }else {
                boolean status3= comparedate(date.getText().toString().trim(), in_date.getText().toString().trim());
                long status4= DateDifferent(date.getText().toString().trim(), in_date.getText().toString().trim());
                if(status3 && status4<2){
                    boolean datediff=comparedate(in_date.getText().toString().trim(),out_date.getText().toString().trim());

                if (in_date.getText().toString().trim().equals(out_date.getText().toString().trim())||datediff) {


                    String resultcampare = CompareTime(in_time.getText().toString().trim(), out_time.getText().toString().trim());
                    if (!resultcampare.equals("1")) {

                        EmpowerApplication.alertdialog(for_out_time, Attend_Regularization.this);

                    } else {
                        int timediff=TimeDifference(in_time.getText().toString(),out_time.getText().toString());
                        if(timediff<24) {
                            if (remark.getText().toString().length() <= 500) {
                                if (!level1.isEmpty()) {
                                    //Toast.makeText(this, "Application Submitted sucessfully", Toast.LENGTH_LONG).show();
                                    if (Utilities.isNetworkAvailable(this)) {
                                        RegApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")), "1", getDate(), getDate(), getInDate(), getOutDate(), getInTime(), getOutTime(), getReasonId(), getShiftId(), getReason(), getRealInTime(), getRealOutTime());
                                    } else {
                                        Toast.makeText(Attend_Regularization.this, "No Internet Connection...", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    EmpowerApplication.alertdialog("No Work Flow Found For This Employee", Attend_Regularization.this);


                                }

                            } else {
                                remark.setError("Remark should be less than 50 charactor");
                            }
                        }else {
                            EmpowerApplication.alertdialog("Out Time and InTime difference should be greater than or equal 24 Hours", Attend_Regularization.this);


                        }

                    }


                }else {
                    EmpowerApplication.alertdialog("Out Date should be greater than or equal to InDate", Attend_Regularization.this);

                   /* if (remark.getText().toString().length() <= 500) {
                        Toast.makeText(this, "Application Submitted sucessfully", Toast.LENGTH_LONG).show();

                    } else {
                        remark.setError("Remark should be less than 50 charactor");
                    }*/

                }
            }else{
                    EmpowerApplication.alertdialog("Difference between Date and In-Date cannot be more than 24 hours", Attend_Regularization.this);


                }

            }
            /*}else {*/

        }



    }
    private  boolean validatedate() {
        if (date.getText().toString().trim().isEmpty()) {
            inputLayoutdate.setError(getString(R.string.err_msg_date));
            date.requestFocus();
            return false;
        } else {
            inputLayoutdate.setErrorEnabled(false);
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
    private boolean validateintime() {
        if (in_time.getText().toString().trim().isEmpty()) {
            inputLayoutintime.setError(getString(R.string.err_msg_intime));
            in_time.requestFocus();
            return false;
        } else {
            inputLayoutintime.setErrorEnabled(false);
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
    private boolean validateouttime() {
        if (out_time.getText().toString().trim().isEmpty()) {
            inputLayoutouttime.setError(getString(R.string.err_msg_outtime));
           out_time.requestFocus();
            return false;
        } else {
            inputLayoutouttime.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateshift() {
        if (shift_type.getSelectedItem().toString().trim().equals("Select")) {
           setSpinnerError(shift_type,"select shift type ");
           shift_type.requestFocus();
            return false;
        } /*else {
            shift_type.setErrorEnabled(false);
        }*/

        return true;
    }
    private boolean validatereason() {
        if (reason_type.getSelectedItem().toString().trim().equals("Select")) {
            setSpinnerError(reason_type,"select Reason type ");
            reason_type.requestFocus();
            return false;
        } /*else {
            inputLayoutouttime.setErrorEnabled(false);
        }*/

        return true;
    }


    private boolean validateDayStatus() {
        if (day_status_spinner.getSelectedItem().toString().trim().equals("Select")) {
            setSpinnerError(day_status_spinner,"select Day Status type ");
            day_status_spinner.requestFocus();
            return false;
        } /*else {
            inputLayoutouttime.setErrorEnabled(false);
        }*/

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    public boolean comparedate(String date1,String date2) {

        boolean datevalidflag=false;

        if(parsedateString(date1).before(parsedateString(date2))||parsedateString(date1).equals(parsedateString(date2))) {
                // In between
                datevalidflag=true;
                return true;
            }
               // datevalidflag=false;

            return false;

       /* if(!datevalidflag){
            EmpowerApplication.alertdialog(msg0, Attend_Regularization.this);
        }*/
    }
    public String comparedate1(String date1,String date2) {




        if(parsedateString(date1).after(parsedateString(date2))) {
            // In between
          return "1";

        }else if(parsedateString(date1).equals(parsedateString(date2)))
        {
            return "2";

        }
        return "0";


        /*if(!datevalidflag){
            EmpowerApplication.alertdialog(msg0, Attend_Regularization.this);
        }*/



    }
    public Date parsedateString(String date1){
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        Date date123=new Date();
        try {
            date123 = format.parse(date1);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date123;

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


    public void GetWorkFlowFoRegularizationApp(String employeeId,String token) {

        pd = new ProgressDialog(Attend_Regularization.this);
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> GetWorkFlowdata=new HashMap<String,String>();
        GetWorkFlowdata.put("employeeId",employeeId);
        GetWorkFlowdata.put("token",token);

        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();

        retrofit2.Call<FinalResponsePoJo> call = apiService.GetWorkFlowForRegularizationApp(GetWorkFlowdata);
        call.enqueue(new Callback<FinalResponsePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<FinalResponsePoJo> call, Response<FinalResponsePoJo> response) {
                pd.dismiss();
                level1.clear();
                level2.clear();
                level3.clear();
                level4.clear();
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
                        EmpowerApplication.alertdialog(response.body().getMessage(),Attend_Regularization.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attend_Regularization.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attend_Regularization.this);

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
                TextView textView = new TextView (Attend_Regularization.this);
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
            ImageView imageView=findViewById(R.id.arrow1);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level2.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (Attend_Regularization.this);
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
            ImageView imageView=findViewById(R.id.arrow2);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level3.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (Attend_Regularization.this);
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
            ImageView imageView=findViewById(R.id.arrow3);
            imageView.setImageResource(R.drawable.ic_action_name);
            for(int i=0;i<level4.size();i++){

                //ImageView imageview = new ImageView(MainActivity.this);
                //CircleImageView imageview = new CircleImageView(MainActivity.this);

                TextView textView = new TextView (Attend_Regularization.this);
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



    public void ReasonForRegApp(String employeeId) {

       // pd = new ProgressDialog(Attend_Regularization.this);
       // pd.setMessage("Loading....");
        //pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);

        reason=new ArrayList<>();
        reasonId=new ArrayList<String>();

        retrofit2.Call<Reason> call = apiService.getReasonRegApp(WorkFlowdataapp);
        call.enqueue(new Callback<Reason>() {
            @Override
            public void onResponse(retrofit2.Call<Reason> call, Response<Reason> response) {

                reason.clear();
                reasonId.clear();

                if (response.isSuccessful()) {
                   // pd.dismiss();
                   reason.add("Select");

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            reasonId.add(response.body().getData().get(i).getReasonID());
                            reason.add(response.body().getData().get(i).getReasonTitle());
                        }

                        // Creating adapter for spinner
                     ArrayAdapter   arrayAdapter = new ArrayAdapter(Attend_Regularization.this, android.R.layout.simple_spinner_item,reason);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reason_type.setAdapter(arrayAdapter);

                        //EmpowerApplication.alertdialog(response.body().getMessage(), WorkFromHomeActivity.this);

                    }else {
                        // Creating adapter for spinner
                        ArrayAdapter   arrayAdapter = new ArrayAdapter(Attend_Regularization.this, android.R.layout.simple_spinner_item,reason);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reason_type.setAdapter(arrayAdapter);

                        EmpowerApplication.alertdialog(response.body().getMessage(), Attend_Regularization.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attend_Regularization.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<Reason> call, Throwable t) {
                // Log error here since request failed
                //pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), Attend_Regularization.this);

                Log.e("TAG", t.toString());

            }
        });
    }

    public void RegApp(String employeeId,String token,String NoOfDays,String ShiftDateFrom,String ShiftDateTo,String InDate,String OutDate,String InTime,String OutTime,String ReasonID,String shiftID,String Remark,String RealInDateTime,String RealOutDateTime) {

        pd = new ProgressDialog(Attend_Regularization.this);
        pd.setMessage("Loading....");
        pd.show();


        Map<String,String> Regapp=new HashMap<String,String>();
        Regapp.put("token",token);
        Regapp.put("NoOfDays",NoOfDays);
        Regapp.put("ShiftDateFrom",ShiftDateFrom);
        Regapp.put("ShiftDateTo",ShiftDateTo);
        Regapp.put("InDate",InDate);
        Regapp.put("OutDate",OutDate);
        Regapp.put("InTime",InTime);
        Regapp.put("OutTime",OutTime);
        Regapp.put("ReasonID",ReasonID);
        Regapp.put("employeeId",employeeId);
        Regapp.put("shiftID",shiftID);
        Regapp.put("Remark",Remark);
        Regapp.put("RealInDateTime",RealInDateTime);
        Regapp.put("RealOutDateTime",RealOutDateTime);
        if(DayStatusValues == null){
          //  Regapp.put("RequestedDayStatus","null");

        }else{
            Regapp.put("RequestedDayStatus",DayStatusId);

        }

        //Log.d("","Shift ID1: " Regapp );
        Log.d("", "RegApp: "+ Regapp);

        retrofit2.Call<Regularization> call = apiService.RegApp(Regapp);
        call.enqueue(new Callback<Regularization>() {
            @Override
            public void onResponse(retrofit2.Call<Regularization> call, Response<Regularization> response) {
                Log.d("", "onResponseRegulation: "+ new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.isSuccessful()) {

                    Log.d("", "onResponseRegulation: "+ new Gson().toJson(response.body()));
                    if (response.body().getStatus().equals("1")) {

                        dialogForRegApplication(date.getText().toString(),Attend_Regularization.this);

                       /* final Dialog dialog = new Dialog(Attend_Regularization.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dilogrel);
                        Window window = dialog.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView date = (TextView) dialog.findViewById(R.id.button4);
                        date.setText(date.getText().toString());
                        dialog.show();

                        Button declineButton = (Button) dialog.findViewById(R.id.button_ok);
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });*/


                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Attend_Regularization.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attend_Regularization.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<Regularization> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(),Attend_Regularization.this);

            }
        });
    }

    private String getReasonId(){
        return reasonId.get(reason_type.getSelectedItemPosition()-1);
    }

    private String getDate(){
        return date.getText().toString();
    }
    private String getInDate(){
        return in_date.getText().toString();
    }
    private String getOutDate(){
        return out_date.getText().toString();
    }
    private String getInTime(){
        return in_time.getText().toString();
    }
    private String getOutTime(){
        return out_time.getText().toString();
    }

    private String getShiftId(){
        Log.e("TAG", shiftId.toString() + " "+shift_type.getSelectedItemPosition());
       // String shift ="";
        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowShifSelectionInAttendanceRegularizationApp")).equals("0")){
            return "";
        }else {
            return shiftId.get(shift_type.getSelectedItemPosition()-1);
        }

    }
    private String getRealInTime(){
        return realintime.getText().toString();
    }
    private String getRealOutTime(){
        return realouttime.getText().toString();
    }
    private String getReason(){
        return remark.getText().toString();
    }




    public  void dialogForRegApplication(String msg,Context context) {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dilogrel);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = dialog1.findViewById(R.id.button4);
        title.setText(msg);
        Button btn_ok = dialog1.findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               if(HomeFragment.key.contains("HideRegularizationApp")) {
                 int index=  HomeFragment.key.indexOf("HideRegularizationApp");
                    Intent intentSurvey = new Intent(Attend_Regularization.this, ApplicationStatusActivity.class);
                    intentSurvey.putExtra("ARG_PAGE", index);
                    startActivity(intentSurvey);
                    dialog1.dismiss();
                }
               // dialog1.dismiss();
            }
        });
    }

    public int TimeDifference(String time1,String time2){
        int hours=0;
        try
        {

            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            long mills = date1.getTime() - date2.getTime();
            Log.v("Data1", ""+date1.getTime());
            Log.v("Data2", ""+date2.getTime());
             hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) (mills/(1000*60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return hours;
    }

    public long DateDifferent(String date1,String date2){



            String dateStart = "01/14/2012 09:29:58";
            String dateStop = "01/15/2012 10:31:48";

            //HH converts hour in 24 hours format (0-23), day calculation
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

            Date d1 = null;
            Date d2 = null;
        long diffDays=0;

            try {
                d1 = format.parse(date1);
                d2 = format.parse(date2);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                 diffDays = diff / (24 * 60 * 60 * 1000);

                System.out.print(diffDays + " days, ");
                System.out.print(diffHours + " hours, ");
                System.out.print(diffMinutes + " minutes, ");
                System.out.print(diffSeconds + " seconds.");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return diffDays;

        }

    public void RealinTime(String employeeId,String date) {

        // pd = new ProgressDialog(Attend_Regularization.this);
        // pd.setMessage("Loading....");
        // pd.show();

        Map<String,String> shiftdata=new HashMap<String,String>();
        shiftdata.put("employeeId",employeeId);
        shiftdata.put("shiftDate",date);
      //  Log.d("inout map :" ,shiftdata.toString());

        retrofit2.Call<RealTime> call = apiService.getRealTimeData(shiftdata);
        call.enqueue(new Callback<RealTime>() {
            @Override
            public void onResponse(retrofit2.Call<RealTime> call, Response<RealTime> response) {

                if (response.isSuccessful()) {
                    // pd.dismiss();
                  //  Log.d("realtime data: ", new Gson().toJson(response.body()));

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {


                            try {
                                if(!response.body().getData().get(i).getIntime().equals("")) {
                                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa",Locale.US);

                                    SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa",Locale.US);
                                    Date date = format.parse(response.body().getData().get(i).getIntime());
                                    Date dt = new Date(String.valueOf(date));
                                    realintime.setText(format1.format(dt));
                                    in_time.setText(format1.format(dt));
                                  //  in_time.setClickable(false);
                                  //  in_time.setEnabled(false);

                                    Log.e("Intime",format1.format(dt));
                                    Log.e("Intime1",response.body().getData().get(i).getIntime());
                                   // Log.e("outtime",response.body().getData().get(i).getOutTime());
                                }
                                /*if(!response.body().getData().get(i).getOutTime().equals("")) {
                                    SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

                                    SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");
                                    Date date1 = format2.parse(response.body().getData().get(i).getOutTime());
                                    realouttime.setText(format3.format(date1));
                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{
                                if(!response.body().getData().get(i).getOutTime().equals("")) {
                                    SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa",Locale.US);
                                    //Log.e("outtime",response.body().getData().get(i).toString());


                                    SimpleDateFormat format3 = new SimpleDateFormat("hh:mm aa",Locale.US);
                                    Date date1 = format2.parse(response.body().getData().get(i).getOutTime());
                                    realouttime.setText(format3.format(date1));
                                    out_time.setText(format3.format(date1));
                                   // out_time.setClickable(false);
                                  //  out_time.setEnabled(false);
                                    Log.e("outtime",format3.format(date1));
                                    Log.e("outtime1",response.body().getData().get(i).getOutTime());
                                }

                            } catch (Exception e) {
                            e.printStackTrace();
                        }


                        }


                    }else {
//                        realintime.setText("");
//                        in_time.setText("");
//                        in_time.setClickable(true);
//                        in_time.setEnabled(true);
//                        realouttime.setText("");
//                        out_time.setText("");
//                        out_time.setClickable(true);
//                        out_time.setEnabled(true);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attend_Regularization.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<RealTime> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                //pd.dismiss();

                EmpowerApplication.alertdialog(t.getMessage(),Attend_Regularization.this);

            }
        });
    }
    public void ShiftForRegApp(String employeeId,String date) {

        // pd = new ProgressDialog(Attend_Regularization.this);
        // pd.setMessage("Loading....");
        // pd.show();

        Map<String,String> shiftdata=new HashMap<String,String>();
        shiftdata.put("employeeId",employeeId);
        shiftdata.put("shiftDate",date);
        Log.d("Shift ID1: ", employeeId + ' '+date);

        shift=new ArrayList<>();
        shiftId=new ArrayList<String>();

        retrofit2.Call<ShiftType> call = apiService.getShiftType(shiftdata);
        call.enqueue(new Callback<ShiftType>() {
            @Override
            public void onResponse(retrofit2.Call<ShiftType> call, Response<ShiftType> response) {

                shift.clear();
                shiftId.clear();

                if (response.isSuccessful()) {
                    // pd.dismiss();
                    shift.add("Select");
                    Log.d("Shift ID1: ", new Gson().toJson(response.body()));

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            shiftId.add(response.body().getData().get(i).getShiftID());
                            shift.add(response.body().getData().get(i).getShiftName());
                        }

                        // Creating adapter for spinner
                        ArrayAdapter   arrayAdapter = new ArrayAdapter(Attend_Regularization.this, android.R.layout.simple_spinner_item,shift);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        shift_type.setAdapter(arrayAdapter);

                        //EmpowerApplication.alertdialog(response.body().getMessage(), WorkFromHomeActivity.this);

                    }else {
                        // Creating adapter for spinner
                        ArrayAdapter   arrayAdapter = new ArrayAdapter(Attend_Regularization.this, android.R.layout.simple_spinner_item,shift);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        shift_type.setAdapter(arrayAdapter);
                        EmpowerApplication.alertdialog(response.body().getMessage(), Attend_Regularization.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attend_Regularization.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<ShiftType> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                //pd.dismiss();

                EmpowerApplication.alertdialog(t.getMessage(),Attend_Regularization.this);

            }
        });
    }

    public void DayStatus(String employeeId) {

        // pd = new ProgressDialog(Attend_Regularization.this);
        // pd.setMessage("Loading....");
        // pd.show();

        Map<String,String> shiftdata=new HashMap<String,String>();
        shiftdata.put("employeeId",employeeId);


        daystatus=new ArrayList<>();

        retrofit2.Call<DayStatusReg> call = apiService.DayStatusReg1(shiftdata);
        call.enqueue(new Callback<DayStatusReg>() {
            @Override
            public void onResponse(retrofit2.Call<DayStatusReg> call, Response<DayStatusReg> response) {


                if (response.isSuccessful()) {

                    DayStatusID = response.body().getData();

                    // pd.dismiss();
                    Log.d("status ID1 respo: ", new Gson().toJson(response.body()));
                    daystatus.add("Select");

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            daystatus.add(response.body().getData().get(i).getDisplayCode());
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Attend_Regularization.this, simple_spinner_item, daystatus);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        day_status_spinner.setAdapter(spinnerArrayAdapter);





                    }else {

                        EmpowerApplication.alertdialog(response.body().getMessage(), Attend_Regularization.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attend_Regularization.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attend_Regularization.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<DayStatusReg> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                //pd.dismiss();

                EmpowerApplication.alertdialog(t.getMessage(),Attend_Regularization.this);

            }
        });
    }



}
