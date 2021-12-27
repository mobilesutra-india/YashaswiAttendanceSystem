package seedcommando.com.yashaswi.compoffapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.ExceptionHandler;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.applicationstatus.ApplicationStatusActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;
import seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails.CompOffDetails;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.getreasonpojo.Reason;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 9/14/2017.
 */

public class CompoffActivity extends AppCompatActivity {
    //ListView listView;
    TextView totalCompOff;
    ArrayList<CompoffPOJO> arrayList;
    ArrayList<String> dates;
    EditText editText_date, editText, reason;
    TextInputLayout inputLayoutagainstdate, inputLayoutdate, inputLayoutreason;
    public static boolean firsthalf = false, secondhalf = false, calculatedays = false;
    Button apply, cancel;
    private static final String DATE_FORMAT = "dd MMM yyyy";
    SimpleDateFormat   dateFormatter1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    SimpleDateFormat   dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    // current displayed month
    private Calendar currentDate = Calendar.getInstance();
    RadioGroup rg;
    boolean datevalidflag = false;
    private ApiInterface apiService;
    ProgressDialog pd;
    Spinner reasontype;
    static  boolean flag=false;

    ArrayList<String> level1=null,level2=null,level3=null,level4=null,reason12=null,reasonId=null;
    //ArrayList<String> level2=null;
    //ArrayList<String> level3=null;
    //ArrayList<String> level4=null;
    //ArrayList<String> reason12=null;
    //ArrayList<String> reasonId=null;
    //ArrayList<String> shift=null;
    //ArrayList<String> shiftId=null;
    LinearLayout.LayoutParams params,params1;
    //LinearLayout.LayoutParams params1;
    LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.compoff_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("comp-off Application");
        toolbar.setLogo(R.drawable.yashaswi_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = findViewById(R.id.Comp_off_Against_Date);
        reason = findViewById(R.id.reason);
        totalCompOff= findViewById(R.id.totalcompoff);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        disableSoftKeyboard(editText);
        editText_date = findViewById(R.id.Comp_off_date);
        disableSoftKeyboard(editText_date);
        imm.hideSoftInputFromInputMethod(editText_date.getWindowToken(), 0);
        // EditTextEx editTextEx=new EditTextEx();
        apply = findViewById(R.id.apply);
        cancel = findViewById(R.id.cancel);
        inputLayoutagainstdate = findViewById(R.id.Comp_off_Against_Date1);
        inputLayoutdate = findViewById(R.id.Comp_off_date1);
        inputLayoutreason = findViewById(R.id.reason1);
        reasontype= findViewById(R.id.editText_wfh_reason);
        editText.addTextChangedListener(new MyTextWatcher(editText));
        editText_date.addTextChangedListener(new MyTextWatcher(editText_date));
        reason.addTextChangedListener(new MyTextWatcher(reason));
        rg = findViewById(R.id.radiogp);
        ((RadioButton) findViewById(R.id.radio_fullday)).setChecked(true);
        radiocheckedlisner();


    Bundle  extras = getIntent().getExtras();

    String value;
        if (extras != null) {
        value = extras.getString("Date");
            //editText_date.setText(value);
            try {
                Date d1=dateFormatter.parse(value);
                editText_date.setText(dateFormatter1.format(d1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //in_date.setText(value);

    }

        dates = new ArrayList<String>();
        apiService = ApiClient.getClient().create(ApiInterface.class);


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
            GetWorkFlowForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
            getTotalCompoff( EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

            // ReasonForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

        }else {
            Toast.makeText(CompoffActivity.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

        }



        //rg.addTextChangedListener(new MyTextWatcher(rg));


        /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);*/


        //assignClickHandlers();
        // listView = (ListView)findViewById(R.id.list);
        //Log.e("data", MainActivity.arrayList.toString());
        //setUpExpList();
        // CompoffAdapter adapter = new CompoffAdapter(this, arrayList);
        // listView.setAdapter(adapter);
        // add button listener
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editText_date.getText().toString().trim().isEmpty()) {
                    CompoffAgainstdialog();
                   /* if (!editText_date.getText().toString().equals("")) {
                        comparedate();
                    } else {
                        Toast.makeText(CompoffActivity.this, "please select Comp-off date", Toast.LENGTH_LONG).show();
                    }*/
                }else {
                    inputLayoutdate.setError(getString(R.string.err_msg_date));
                    editText_date.requestFocus();

                   // Toast.makeText(CompoffActivity.this,"Select Date",Toast.LENGTH_LONG).show();
                }


            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if(!editText_date.getText().toString().trim().isEmpty()) {
                        CompoffAgainstdialog();
                   /* if (!editText_date.getText().toString().equals("")) {
                        comparedate();
                    } else {
                        Toast.makeText(CompoffActivity.this, "please select Comp-off date", Toast.LENGTH_LONG).show();
                    }*/
                    }else {
                        inputLayoutdate.setError(getString(R.string.err_msg_date));
                        editText_date.requestFocus();

                       // Toast.makeText(CompoffActivity.this,"Select Date",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CompoffActivity.this, date, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editText_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(CompoffActivity.this, date, currentDate
                            .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                            currentDate.get(Calendar.DAY_OF_MONTH)).show();

                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!level1.isEmpty()) {

                if (rg.getCheckedRadioButtonId() == -1) {
                    EmpowerApplication.alertdialog("Select Radio Button to proceed ", CompoffActivity.this);
                }
                if (!validatedate()) {
                    return;
                }

                if (!validateagainstdate()) {
                    return;
                }



                if (!reasontype.getSelectedItem().toString().equals("Select")) {
                    /*if (!validatereason()) {
                        return;
                    }*/
                    if (reason.getText().toString().length() <= 50) {
                        //Toast.makeText(CompoffActivity.this, "Application Submitted sucessfully", Toast.LENGTH_LONG).show();

                        if (!editText.getText().toString().trim().isEmpty() && !editText_date.getText().toString().trim().isEmpty()) {
                            /*comparedate();
                            if (datevalidflag) {*/

                            if (Utilities.isNetworkAvailable(CompoffActivity.this)) {
                                CompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),editText_date.getText().toString(),String.valueOf(firsthalf),String.valueOf(firsthalf),reason.getText().toString(),getReasontypeId(),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("compOffAgainstDetails")));
                                // ReasonForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                            }else {
                                Toast.makeText(CompoffActivity.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

                            }



                            // }
                        }
                    } else {
                        reason.setError("reason should be less than 50 charactor");
                    }
                }else {

                    TextView errorText = (TextView) reasontype.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select Reason Type");
                    errorText.requestFocus();
                   // Toast.makeText(CompoffActivity.this,"please select Comp-off Against Date",Toast.LENGTH_LONG).show();
                }
                }else {
                    EmpowerApplication.alertdialog("No Work Flow Found For This Employee", CompoffActivity.this);


                }

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpExpList() {
        arrayList = new ArrayList<CompoffPOJO>();
        CompoffPOJO compoffPOJO = new CompoffPOJO();
        compoffPOJO.setDate("10 Apr 2017");
        compoffPOJO.setAvailable_units("1/1");
        compoffPOJO.setExpiry_date("22 Jun 17");
        compoffPOJO.setWork_hrs("9.0");
        compoffPOJO.setUsed_compoff("NA");

        arrayList.add(compoffPOJO);
        CompoffPOJO compoffPOJO1 = new CompoffPOJO();
        compoffPOJO1.setDate("2 Apr 2017");
        compoffPOJO1.setAvailable_units("1/1");
        compoffPOJO1.setExpiry_date("12 Jun 17");
        compoffPOJO1.setWork_hrs("10.0");
        compoffPOJO1.setUsed_compoff("NA");

        arrayList.add(compoffPOJO1);
        CompoffPOJO compoffPOJO2 = new CompoffPOJO();
        compoffPOJO2.setDate("2 Apr 2017");
        compoffPOJO2.setAvailable_units("1/1");
        compoffPOJO2.setExpiry_date("12 Jun 17");
        compoffPOJO2.setWork_hrs("10.0");
        compoffPOJO2.setUsed_compoff("NA");
        arrayList.add(compoffPOJO2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setVisible(false);
        return true;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            currentDate.set(year, monthOfYear, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        editText_date.setText(sdf.format(currentDate.getTime()));
        /*if (!editText.getText().toString().equals("")) {
            comparedate();
        } else {
            Toast.makeText(CompoffActivity.this, "please select Comp-off Against Date", Toast.LENGTH_LONG).show();
        }*/

        // comparedate();
    }

    private void assignClickHandlers() {
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new DatePickerDialog(CompoffActivity.this, date, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /*editText_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Toast.makeText(getBaseContext(),
                        ((EditText) v).getId() + " has focus - " + hasFocus,
                        Toast.LENGTH_LONG).show();
                new DatePickerDialog(CompoffActivity.this, date, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

    }


    //Back press................
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyBoard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void disableSoftKeyboard(final EditText v) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setRawInputType(InputType.TYPE_CLASS_TEXT);
            v.setTextIsSelectable(true);
        } else {
            v.setRawInputType(InputType.TYPE_NULL);
            v.setFocusable(true);
        }
    }

    public void CompoffAgainstdialog() {
        if (Utilities.isNetworkAvailable(this)) {
            getAgaintCompOffForCompoffApp( EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            // EmpowerApplication.dialogForApplication("Leave application for day",String.valueOf(no_of_days),Leave_Application.this);
            //SendLeaveData((int) leavecode, 2, no_of_days, fromdate, todate, Reason, 3);
        } else {
            //((TextView) reasontype.getSelectedView()).setError("Select Leave Type");
            Toast.makeText(CompoffActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
        }

    }

    private boolean validateagainstdate() {
        if (editText.getText().toString().trim().isEmpty()) {
            inputLayoutagainstdate.setError(getString(R.string.err_msg_againstdate));
            editText.requestFocus();
            return false;
        } else {
            inputLayoutagainstdate.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatedate() {
        if (editText_date.getText().toString().trim().isEmpty()) {
            inputLayoutdate.setError(getString(R.string.err_msg_date));
            editText_date.requestFocus();
            return false;
        } else {
            inputLayoutdate.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatereason() {
        if (reason.getText().toString().trim().isEmpty()) {
            inputLayoutreason.setError(getString(R.string.err_msg_reason));
            reason.requestFocus();
            return false;
        } else {
            inputLayoutreason.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                case R.id.Comp_off_Against_Date:
                    validateagainstdate();
                    break;

                case R.id.Comp_off_date:
                    validatedate();
                    break;
                case R.id.reason:
                    validatedate();
                    break;
            }
        }
    }


   /* public void comparedate() {


        for (int i = 0; i < dates.size(); i++) {

            if (parsedateString(dates.get(i)).before(parsedateString(editText_date.getText().toString()))) {
                // In between
                datevalidflag = true;

            } else {
                datevalidflag = false;
            }

        }
       *//* if (!datevalidflag) {
            EmpowerApplication.alertdialog("Comp_off Date cannot be less than or equal to Comp_off Against Date", CompoffActivity.this);
        }*//*


    }*/

   /* public Date parsedateString(String date1) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Date date123 = new Date();
        try {
            date123 = format.parse(date1);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date123;

    }*/


    public void GetWorkFlowForCompoffApp(String employeeId,String token) {

        pd = new ProgressDialog(CompoffActivity.this);
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> GetWorkFlowdata=new HashMap<String,String>();
        GetWorkFlowdata.put("employeeId",employeeId);
        GetWorkFlowdata.put("token",token);

        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();

        retrofit2.Call<FinalResponsePoJo> call = apiService.GetWorkFlowForCompoffApp(GetWorkFlowdata);
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

                        if (Utilities.isNetworkAvailable(CompoffActivity.this)) {
                            //GetWorkFlowForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                            ReasonForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                        }else {
                            Toast.makeText(CompoffActivity.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

                        }
                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(),CompoffActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", CompoffActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", CompoffActivity.this);

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
                TextView textView = new TextView (CompoffActivity.this);
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
                TextView textView = new TextView (CompoffActivity.this);
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
                TextView textView = new TextView (CompoffActivity.this);
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

                TextView textView = new TextView (CompoffActivity.this);
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
    public void ReasonForCompoffApp(String employeeId) {

       /* pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();*/

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);

        reason12=new ArrayList<>();
        reasonId=new ArrayList<String>();

        retrofit2.Call<Reason> call = apiService.getReasonCompoffApp(WorkFlowdataapp);
        call.enqueue(new Callback<Reason>() {
            @Override
            public void onResponse(retrofit2.Call<Reason> call, Response<Reason> response) {
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
                        ArrayAdapter arrayAdapter = new ArrayAdapter(CompoffActivity.this, android.R.layout.simple_spinner_item,reason12);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reasontype.setAdapter(arrayAdapter);

                        //EmpowerApplication.alertdialog(response.body().getMessage(), WorkFromHomeActivity.this);

                    }else {
                        // Creating adapter for spinner
                        ArrayAdapter arrayAdapter = new ArrayAdapter(CompoffActivity.this, android.R.layout.simple_spinner_item,reason12);

                        // Drop down layout style - list view with radio button
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reasontype.setAdapter(arrayAdapter);
                        EmpowerApplication.alertdialog(response.body().getMessage(), CompoffActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", CompoffActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<Reason> call, Throwable t) {
                // Log error here since request faile
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), CompoffActivity.this);


            }
        });
    }

    public void getAgaintCompOffForCompoffApp(String employeeId) {

        pd = new ProgressDialog(CompoffActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);
        arrayList = new ArrayList<CompoffPOJO>();


        retrofit2.Call<CompOffDetails> call = apiService.getAgainstCompoffApp(WorkFlowdataapp);
        call.enqueue(new Callback<CompOffDetails>() {
            @Override
            public void onResponse(retrofit2.Call<CompOffDetails> call, Response<CompOffDetails> response) {
                 pd.dismiss();
               // reason12.clear();
               // reasonId.clear();
                arrayList.clear();


                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("1")) {
                        if(response.body().getData().size()!=0) {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                                SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yy");

                                CompoffPOJO compoffPOJO = new CompoffPOJO();

                                try {
                                    Date date = format.parse(response.body().getData().get(i).getShiftDate());
                                    compoffPOJO.setDate(format1.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                compoffPOJO.setCompoffid(response.body().getData().get(i).getCoompOffId());

                                compoffPOJO.setAvailable_units(response.body().getData().get(i).getAvailableUnit());
                                SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                                SimpleDateFormat format3 = new SimpleDateFormat("dd MMM yy");
                                try {
                                    Date date = format2.parse(response.body().getData().get(i).getExpiredDate());
                                    compoffPOJO.setExpiry_date(format3.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                compoffPOJO.setWork_hrs(response.body().getData().get(i).getWorkingHours());
                                compoffPOJO.setUsed_compoff(response.body().getData().get(i).getUsedCompOffDetails());
                                String data1 = String.valueOf(GetCommaData(response.body().getData().get(i).getPendingAppDetails()));
                                compoffPOJO.setPendingunit(data1);
                                compoffPOJO.setEarnunit(String.valueOf(Double.parseDouble(response.body().getData().get(i).getEarnedUnit())));

                                arrayList.add(compoffPOJO);
                            }

                            final Dialog dialog = new Dialog(CompoffActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.comp_off_popup);
                            final ListView listView = dialog.findViewById(R.id.list_pop_up);
                            // setUpExpList();
                            final CompoffAdapter adapter = new CompoffAdapter(CompoffActivity.this, arrayList);
                            listView.setAdapter(adapter);
                            dialog.show();
                            Button declineButton = dialog.findViewById(R.id.button_ok);
                            // Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);

                            // if decline button is clicked, close the custom dialog
                            declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //long posion= listView.getCheckedItemPosition();
                                    String data1 = "";
                                    //int position=listView.getCheckedItemPosition();
                                    int checkedcont = adapter.getSelectedString().size();
                                    int count = listView.getCheckedItemCount();
                                    int count1 = listView.getChildCount();
                                    if(flag) {

                                        if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("compOffDates")).equals("0")) {
                                            if (checkedcont != 0) {
                                                for (int i = 0; i < checkedcont; i++) {
                                                    String data = adapter.getSelectedString().get(i);
                                                    dates.add(data);

                                                    if (data1.equals("")) {
                                                        data1 = data;

                                                    } else {
                                                        data1 = data1+";" + data;
                                                    }


                                                }
                                                editText.setText(data1);
                                                dialog.dismiss();


                                            } else {

                                                dialog.dismiss();
                                                // EmpowerApplication.alertdialog("select Against Date", CompoffActivity.this);
                                            }
                                        } else {
                                            editText.setText("");
                                            dialog.dismiss();
                                            // EmpowerApplication.alertdialog("select Against Date", CompoffActivity.this);
                                        }
                                    }else {
                                        editText.setText("");
                                        dialog.dismiss();
                                    }


                                }
                            });
                        }else {
                            EmpowerApplication.alertdialog("CompOff Not Available", CompoffActivity.this);


                        }


                       /* cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });*/

                       //
                        //EmpowerApplication.alertdialog(response.body().getMessage(), WorkFromHomeActivity.this);

                    }else {

                       EmpowerApplication.alertdialog(response.body().getMessage(), CompoffActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", CompoffActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<CompOffDetails> call, Throwable t) {
                // Log error here since request faile
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), CompoffActivity.this);


            }
        });
    }


    public void CompoffApp(String token,String employeeId,String takencompoffdate,String isfirsthalf,String issecondhalf,String reasonDescription,String reasonid,String compoffagainstdetails) {

        pd = new ProgressDialog(CompoffActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("token",token);
        WorkFlowdataapp.put("employeeId",employeeId);
        WorkFlowdataapp.put("takenCompOffDate",takencompoffdate);
        WorkFlowdataapp.put("isFirstHalfCompOff",isfirsthalf);
        WorkFlowdataapp.put("isSecondHalfCompOff",issecondhalf);
        WorkFlowdataapp.put("reasonDescription",reasonDescription);
        WorkFlowdataapp.put("reasonID",reasonid);
        WorkFlowdataapp.put("compOffAgainstDetails",compoffagainstdetails);
        arrayList = new ArrayList<CompoffPOJO>();


        retrofit2.Call<CommanResponsePojo> call = apiService.CompoffApp(WorkFlowdataapp);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();
                // reason12.clear();
                // reasonId.clear();
                arrayList.clear();


                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("1")) {

                        final Dialog dialog = new Dialog(CompoffActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dailog_comp_off);
                        Window window = dialog.getWindow();
                        TextView date=dialog.findViewById(R.id.button4);
                        date.setText(editText_date.getText().toString());
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                        Button declineButton = dialog.findViewById(R.id.button_ok);
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(HomeFragment.key.contains("HideCompOffApp")) {
                                    int index = HomeFragment.key.indexOf("HideCompOffApp");
                                    Intent intentSurvey = new Intent(CompoffActivity.this, ApplicationStatusActivity.class);
                                    intentSurvey.putExtra("ARG_PAGE", index);
                                    startActivity(intentSurvey);
                                    dialog.dismiss();
                                }
                            }
                        });

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), CompoffActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", CompoffActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request faile
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), CompoffActivity.this);


            }
        });
    }

    public void radiocheckedlisner() {

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                editText.setText("");

                int selectedID = rg.getCheckedRadioButtonId();
                Log.i("tag", "msg" + selectedID);

                if (selectedID == (R.id.radio_firsthalf)) {

                    Log.d("", "In radio:first half");
                    firsthalf = true;
                    secondhalf = false;



                } else if (selectedID == (R.id.radio_secondhalf)) {

                    Log.d("", "In radio:second half");
                    secondhalf = true;
                    firsthalf = false;


                } else {
                    firsthalf = false;
                    secondhalf = false;

                    Log.d("", "radio:full day");



                }


            }
        });
    }
    public String getReasontypeId() {
        return reasonId.get(reasontype.getSelectedItemPosition()-1);
    }

    public void getTotalCompoff(String employeeId) {

       // pd = new ProgressDialog(CompoffActivity.this);
       // pd.setMessage("Loading....");
       // pd.setCanceledOnTouchOutside(false);
       // pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeId",employeeId);
        arrayList = new ArrayList<CompoffPOJO>();


        retrofit2.Call<CompOffDetails> call = apiService.getAgainstCompoffApp(WorkFlowdataapp);
        call.enqueue(new Callback<CompOffDetails>() {
            @Override
            public void onResponse(retrofit2.Call<CompOffDetails> call, Response<CompOffDetails> response) {
               // pd.dismiss();
                // reason12.clear();
                // reasonId.clear();
                arrayList.clear();
                float sum=0;


                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                        sum=sum+Float.parseFloat(response.body().getData().get(i).getAvailableUnit());
                        }
                        totalCompOff.setText(String.valueOf(sum));

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), CompoffActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", CompoffActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", CompoffActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<CompOffDetails> call, Throwable t) {
                // Log error here since request faile
                //pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), CompoffActivity.this);


            }
        });
    }


    public double GetCommaData( String srcSource)
    {
        double Answer = 0;
        try
        {
            //String srcSource = "14-Mar-18(1.5) , \r15-Mar-18(1.5)";
            String[] Arr1 = srcSource.split(",");
            if (Arr1.length > 0)
            {
                String Temp_answer = "";
                String strSearch = "";
                double DummyVariable = 0;
                for(int i=0;i<Arr1.length;i++)
                {
                    strSearch = Arr1[i].trim();
                    if (strSearch.contains("(") && strSearch.contains(")"))
                    {
                        Temp_answer = strSearch.replace(strSearch.substring(0, strSearch.indexOf('(') + 1), "").replace(")", "");
                           DummyVariable=Double.parseDouble(Temp_answer);
                            Answer = Answer + DummyVariable;

                    }
                }
            }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        return Answer;
    }


}


