package seedcommando.com.yashaswi.leaveapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Locale;

import seedcommando.com.yashaswi.R;

public class LeaveApp extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {



    //..................................

    ImageView imageviewright, imageviewleft;
    HorizontalScrollView hsv;
    Spinner leave_type_spineer;
    EditText From_date,To_date,reason;
    RadioGroup radiogroup;
    RadioButton radiofirsthalf,radiosecondhalf,radiofullday;

    Date startdate,enddate;

    Button calculate;
    public static  boolean firsthalf=false,secondhalf=false,calculatedays=false;

    String fromdate,todate,leave_Days="",formatted_from_Date,formatted_to_Date;

    Double dayss;
    TextView txt_no_of_days;


    //declaration for datepicker.........
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_application);
        imageviewright = findViewById(R.id.next);
        imageviewleft = findViewById(R.id.prev);
        hsv = findViewById(R.id.horizental_scroll);
        leave_type_spineer= findViewById(R.id.leave_type_spineer);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        From_date= findViewById(R.id.editText_from_date);
        txt_no_of_days= findViewById(R.id.txt_no_of_days);
        imm.hideSoftInputFromWindow(From_date.getWindowToken(), 0);
        disableSoftKeyboard(From_date);

        To_date= findViewById(R.id.editText_to_date);

        disableSoftKeyboard(To_date);
        imm.hideSoftInputFromInputMethod(To_date.getWindowToken(),0);

        reason= findViewById(R.id.editText_to_reason);
      /*  calculate=(Button)findViewById(R.id.calculate_days);*/
        radiogroup= findViewById(R.id.radiogp);
        radiofirsthalf= findViewById(R.id.radio_firsthalf);
        radiosecondhalf= findViewById(R.id.radio_secondhalf);
        radiofullday= findViewById(R.id.radio_fullday);




        //Toolbar declaration............


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leave Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);

        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        setDateTimeField();
        // Spinner click listener for leave type................................................
        leave_type_spineer.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List  categories = new ArrayList();
        categories.add("PL");
        categories.add("SL");
        categories.add("Medical");
        categories.add("PL");

        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        leave_type_spineer.setAdapter(dataAdapter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




        // calculate days..................................................

 /*       calculate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {





                    if (enddate.getTime() >= startdate.getTime()) {


                        if (firsthalf == true || secondhalf == true) {

                            long diff1 = enddate.getTime() - startdate.getTime();
                            float days = (float) diff1 / 1000 / 60 / 60 / 24;
                            //dayss=((int) Math.ceil(days) + 1-0.5);
                            txt_no_of_days.setText(String.valueOf((int) Math.ceil(days) + 1 - 0.5));
                            dayss = Double.parseDouble(txt_no_of_days.getText().toString());
                            leave_Days = txt_no_of_days.getText().toString();
                        } else {
                            long diff = Math.abs(enddate.getTime() - startdate.getTime());
                            float days = (float) diff / 1000 / 60 / 60 / 24;
                            txt_no_of_days.setText(String.valueOf((int) Math.ceil(days) + 1));
                            Log.d("", "textnodays" + txt_no_of_days);
                        }
                    } else {
                        Log.d("", "please enter valid start and end date");
                        Toast.makeText(getApplicationContext(), "please enter valid start and end date", Toast.LENGTH_LONG).show();

                    }



            }




        });*/

        radiocheckedlisner();
       // cal();

//...................................................................
    }


    public void oncalculatedays(){
        if (enddate.getTime() >= startdate.getTime()) {


            if (firsthalf == true || secondhalf == true) {

                long diff1 = enddate.getTime() - startdate.getTime();
                float days = (float) diff1 / 1000 / 60 / 60 / 24;
                //dayss=((int) Math.ceil(days) + 1-0.5);
                txt_no_of_days.setText(String.valueOf((int) Math.ceil(days) + 1 - 0.5));
                dayss = Double.parseDouble(txt_no_of_days.getText().toString());
                leave_Days = txt_no_of_days.getText().toString();
            }
            else {
                long diff = Math.abs(enddate.getTime() - startdate.getTime());
                float days = (float) diff / 1000 / 60 / 60 / 24;
                txt_no_of_days.setText(String.valueOf((int) Math.ceil(days) + 1));
                Log.d("", "textnodays" + txt_no_of_days);
            }
            calculatedays=true;
        }
        else {
            Log.d("", "please enter valid start and end date");
            Toast.makeText(getApplicationContext(), "please enter valid start and end date", Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }

//spineer end...............................................................

    //for date picker/calendar.............

    private void setDateTimeField() {
        From_date.setOnClickListener(this);
        To_date.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                fromdate= dateFormatter.format(newDate.getTime());
                From_date.setText(fromdate);

                //conversion of one date format to other...
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                startdate = null;
                try {
                    startdate = dateFormatter.parse(fromdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("","startdate"+startdate);

                //...............................................

                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }


        }
                ,newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate= dateFormatter.format(newDate.getTime());
                To_date.setText(todate);

                //conversion of one date format to other...
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                enddate = null;
                try {
                    enddate = dateFormatter.parse(todate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("","enddate"+enddate);
                if(From_date.getText().toString().trim().length() != 0 && To_date.getText().toString().trim().length() != 0)
                {
                    oncalculatedays();


                }
                //........................


                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }




        }
                ,newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        //for calculation of days.....

           // calculateddays();



    }

//.........................................................



    public void onClick(View view) {
        if(view == From_date) {

      fromDatePickerDialog.show();
            calculatedays=false;
            txt_no_of_days.setText("");

        }
        else if(view == To_date) {
            toDatePickerDialog.show();
            calculatedays=false;
            txt_no_of_days.setText("");



        }

    }

    public void cal(){
        if(From_date.getText().toString().trim().length() != 0 && To_date.getText().toString().trim().length() != 0)
        {
            oncalculatedays();

        }
    }

    public void radiocheckedlisner(){

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                int selectedID = radiogroup.getCheckedRadioButtonId();
                Log.i("tag", "msg" + selectedID);

                if (selectedID == (R.id.radio_firsthalf)) {

                 Log.d("","In radio:first half");
                    firsthalf=true;
                    secondhalf=false;
                    cal();


                }


                else if (selectedID == (R.id.radio_secondhalf)) {

              Log.d("","In radio:second half");
                    secondhalf=true;
                    firsthalf=false;
                    cal();

                }

                else {
                    firsthalf=false;
                    secondhalf=false;

                    Log.d("","radio:full day");
                    cal();


                }


            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return  true;

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

    //code change...
    public static void disableSoftKeyboard(final EditText v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setRawInputType(InputType.TYPE_CLASS_TEXT);
            v.setTextIsSelectable(true);
        } else {
            v.setRawInputType(InputType.TYPE_NULL);
            v.setFocusable(true);
        }
    }


    //leave Daologue....

    public void dialog_leave_balance(String msg) {

        final Dialog dialog1 = new Dialog(getApplicationContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.leavevalid_dailog);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = dialog1.findViewById(R.id.title);
        title.setText(msg);
        Button btn_yes = dialog1.findViewById(R.id.button_ok);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
    }
    //.....................
}



