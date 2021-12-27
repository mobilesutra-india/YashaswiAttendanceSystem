package seedcommando.com.yashaswi.leaveapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.applicationstatus.ApplicationStatusActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;

import seedcommando.com.yashaswi.pojos.documentpojo.fileUpload;
import seedcommando.com.yashaswi.pojos.documentpojo.getConfigData;
import seedcommando.com.yashaswi.pojos.leavepojo.Leave;
import seedcommando.com.yashaswi.pojos.leavepojo.leavetypecode.LeaveCode;
import seedcommando.com.yashaswi.pojos.leavepojo.reasonmaster.Reason;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando5 on 8/28/2017.
 */

public class Leave_Application extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private ImageView imageviewright, imageviewleft;
    private HorizontalScrollView hsv;
    private Spinner leave_type_spineer,reasontype;
    private EditText From_date, To_date, reason,docName;
    private Button apply, cancel;
    private Date startdate, enddate;
    private String fromdate, todate, leave_Days = "",filePath="";
    private Double dayss;
    private TextView txt_no_of_days;//,pl,sl,ml,cl;
    private RadioButton radiofirsthalf, radiosecondhalf, radiofullday;
    private Button calculate;
    public static boolean firsthalf = false, secondhalf = false, calculatedays = false;
    //declaration for datepicker.........
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextInputLayout inputLayoutfromdate, inputLayouttodate, inputLayoutreason;
    private RadioGroup radioGroup;
    ApiInterface apiService;
    ProgressDialog pd;
    int k=0;
    ArrayList<String> leavetypecode,leavetypereason;
    ArrayList<String> level1;
    ArrayList<String> level2;
    ArrayList<String> level3;
    ArrayList<String> level4;
    ArrayList<String> reasonId;
    ArrayList<Integer> arrayList0;
    Bundle extras;
    LinearLayout.LayoutParams params,params0,params10;
    LinearLayout.LayoutParams params1;
    LinearLayout linearLayout12;
    ViewStub stub;
    TextView textView;
    ArrayList<String> arrayList;
    View inflatedLayout;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLARY = 2;
    static final int REQUEST_Document = 3;
    boolean isDocmentRequired=false;
    int isRequiredDays= 0;
    private String mCurrentPhotoPath;
    String imageFileName,serverfilename;
    LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4,img1,img2,img3,scroll,documentlayout;
    //..................................
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.leave_application);
       final ScrollView sv = findViewById(R.id.scrol);
        //sv.smoothScrollTo(0,0);

        imageviewright = findViewById(R.id.next);
        imageviewleft = findViewById(R.id.prev);
        hsv = findViewById(R.id.horizental_scroll);
        leave_type_spineer = findViewById(R.id.leave_type_spineer);
        reasontype = findViewById(R.id._reason);
        apply = findViewById(R.id.apply);
        cancel = findViewById(R.id.cancel);
        From_date = findViewById(R.id.editText_from_date);
        To_date = findViewById(R.id.editText_to_date);
        txt_no_of_days = findViewById(R.id.txt_no_of_days);
        //pl = (TextView) findViewById(R.id.pl);
       // cl = (TextView) findViewById(R.id.cl);
       // ml = (TextView) findViewById(R.id.Ml);
        //sl = (TextView) findViewById(R.id.sl);
        reason = findViewById(R.id.editText_to_reason);
        radioGroup = findViewById(R.id.radiogp);
        radiofirsthalf = findViewById(R.id.radio_firsthalf);
        radiosecondhalf = findViewById(R.id.radio_secondhalf);
        radiofullday = findViewById(R.id.radio_fullday);
        inputLayoutfromdate = findViewById(R.id.input_layout_floatfrom_date);
        inputLayouttodate = findViewById(R.id.input_layout_floatto_date);
        inputLayoutreason = findViewById(R.id.input_layout_floatto_reason);
        From_date.addTextChangedListener(new MyTextWatcher(From_date));
        To_date.addTextChangedListener(new MyTextWatcher(To_date));
        reason.addTextChangedListener(new MyTextWatcher(reason));
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);


        linearLayout = findViewById(R.id.linearout);
        linearLayout2 = findViewById(R.id.linearout2);
        linearLayout3 = findViewById(R.id.linearout3);
        linearLayout4 = findViewById(R.id.linearout4);
        documentlayout = findViewById(R.id.documentview);

        arrayList=new ArrayList<String>();
        arrayList.add(".bmp");
        arrayList.add(".gif");
        arrayList.add(".png");
        arrayList.add(".jpg");
        arrayList.add(".jpeg");
        arrayList.add(".doc");
        arrayList.add(".docx");
        arrayList.add(".pdf");
        arrayList.add(".txt");
        arrayList.add(".xls");
        arrayList.add(".xlsx");
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        scroll = findViewById(R.id.leavecode);
         arrayList0=new ArrayList<>();
        arrayList0.add(R.drawable.leave_type_blue);
        arrayList0.add(R.drawable.button_shape_leave);
        arrayList0.add(R.drawable.leave_type_yellow);
        arrayList0.add(R.drawable.leave_type_green);
        linearLayout12 = findViewById(R.id.linearlayout10);
        //stub = (ViewStub) findViewById(R.id.layout_stub);
        params0 = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params0.gravity = Gravity.CENTER;
        params10 = new LinearLayout
                .LayoutParams(120, LinearLayout.LayoutParams.WRAP_CONTENT);
        params10.gravity = Gravity.CENTER;
        params10.setMargins(20, 0, 20, 0);

        params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params1 = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        params.setMargins(0, 20, 0, 0);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        if(Utilities.isNetworkAvailable(Leave_Application.this)) {
            LeaveBalanceData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            //LeaveTypeCodeData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        }else {
            Toast.makeText(Leave_Application.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
        }

        ((RadioButton) findViewById(R.id.radio_fullday)).setChecked(true);

         extras = getIntent().getExtras();

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leave Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);
        //for From_date and To-date edit text..............
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        setDateTimeField();
        leave_type_spineer.setOnItemSelectedListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        radiocheckedlisner();

        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_UP);
            }
        });


        //sv.scrollTo(sv.getTop(), sv.getBottom());

    }


    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if (Utilities.isNetworkAvailable(this)) {
            if(!leave_type_spineer.getSelectedItem().toString().equals("Select")){
            LeaveTypeReasonData(leavetypecode.get(parent.getSelectedItemPosition()-1));


            GetWorkFlowForLeaveApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),leavetypecode.get(parent.getSelectedItemPosition()-1));
                getLeavePolicyData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), leavetypecode.get(parent.getSelectedItemPosition()-1),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("Gender")));
            }
            if (getFromDate().trim().length() != 0 && gettoDate().trim().length() != 0) {
                oncalculatedays();
            }

        }else {
            Toast.makeText(Leave_Application.this,"No Internet Connection...",Toast.LENGTH_LONG).show();

        }

        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }

    private void setDateTimeField() {
        From_date.setOnClickListener(this);
        To_date.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


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
                Log.d("", "enddate" + enddate);
                if (getFromDate().trim().length() != 0 && gettoDate().trim().length() != 0) {
                    oncalculatedays();


                }

                //...............................................

                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }


        }
                , newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate = dateFormatter.format(newDate.getTime());
                To_date.setText(todate);

                //conversion of one date format to other...

                enddate = null;
                try {
                    enddate = dateFormatter.parse(todate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("", "enddate" + enddate);
                if (getFromDate().trim().length() != 0 && gettoDate().trim().length() != 0) {
                    oncalculatedays();


                }
                //........................


                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }


        }
                , newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        //for calculation of days.....

        // calculateddays();


    }

    public void onClick(View view) {
        if (view == From_date) {

            fromDatePickerDialog.show();
            calculatedays = false;
            //txt_no_of_days.setText("");
            setNo_of_days("");

        } else if (view == To_date) {
            toDatePickerDialog.show();
            calculatedays = false;
            //txt_no_of_days.setText("");
            setNo_of_days("");


        }
    }

    public void cal() {
        if (getFromDate().trim().length() != 0 && gettoDate().trim().length() != 0) {
            oncalculatedays();

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

                } else {
                    firsthalf = false;
                    secondhalf = false;

                    Log.d("", "radio:full day");
                    cal();


                }


            }
        });
    }


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
                case R.id.editText_from_date:
                    validateFromdate();
                    break;

                case R.id.editText_to_date:
                    validateTodate();
                    break;
               /* case R.id.editText_to_reason:
                    validateToReason();
                    break;*/
            }
        }
    }
    public void onSubmit(View view) {

        submitCheck();
    }

    public void submitCheck() {


            if (!leave_type_spineer.getSelectedItem().toString().equals("Select")) {

                if (!validateFromdate()) {
                    return;
                }

                if (!validateTodate()) {
                    return;
                }
               /* if (!validateToReason()) {
                    return;
                }*/
                if (enddate.getTime() >= startdate.getTime()) {

                    if (!reasontype.getSelectedItem().toString().equals("Select")) {

                        if (getReason().length() <= 50) {
                            if(!level1.isEmpty()) {
                            //Toast.makeText(this, "Application Submitted sucessfully", Toast.LENGTH_LONG).show();

                            String fromdate = getFromDate().trim();
                            String todate = gettoDate().trim();
                            String Reason = getReason().trim();
                            float no_of_days = Float.parseFloat(getNo_of_days().trim());//Invalid file format
                            long leavecode = leave_type_spineer.getSelectedItemId();
                            if (Utilities.isNetworkAvailable(this)) {
                                //if("SL".equals(leave_type_spineer.getSelectedItem().toString())) {
                                //Double.parseDouble(txt_no_of_days.getText().toString()) > 3.0
                                    if (isDocmentRequired && Double.parseDouble(getNo_of_days().trim())>=isRequiredDays) {
                                        if (!textView.getText().toString().equals("Choose file!") && ! textView.getText().toString().equals("Invalid file format") ) {
                                            SendLeaveData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), getFromDate(), gettoDate(), getisFirstHalf(), getisSecondHalf(), getLeavetypeId(), getNo_of_days(), getReasonId(), getReasonDescription(),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("filename")));
                                        } else {
                                            alertdialog("Please upload document", Leave_Application.this);
                                        }
                                    } else {
                                        SendLeaveData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), getFromDate(), gettoDate(), getisFirstHalf(), getisSecondHalf(), getLeavetypeId(), getNo_of_days(), getReasonId(), getReasonDescription(),"");

                                    }
                               // }
                                // EmpowerApplication.dialogForApplication("Leave application for day",String.valueOf(no_of_days),Leave_Application.this);
                                //SendLeaveData((int) leavecode, 2, no_of_days, fromdate, todate, Reason, 3);
                            } else {
                                //((TextView) reasontype.getSelectedView()).setError("Select Leave Type");
                                Toast.makeText(Leave_Application.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                            } }else {
                                EmpowerApplication.alertdialog("No Work Flow Found For This Employee", Leave_Application.this);


                            }


                        } else {
                            reason.setError("reason should be less than 50 charactor");
                        }
                    } else {
                        TextView errorText = (TextView) reasontype.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Select Reason Type");
                        errorText.requestFocus();
                        //((TextView)reasontype.getSelectedView()).setError("Select Leave Type");
                        //EmpowerApplication.alertdialog("Select Reason Type", Leave_Application.this);


                    }
                } else {
                    EmpowerApplication.alertdialog("To Date should be greater than From Date", Leave_Application.this);

                }
            } else {
                TextView errorText = (TextView) leave_type_spineer.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Select Leave Type");//changes the selected item text to this
                errorText.requestFocus();

                //((TextView)leave_type_spineer.getSelectedView()).setError("Select Leave Type");
                //EmpowerApplication.alertdialog("Select Leave Type", Leave_Application.this);

               // Toast.makeText(Leave_Application.this, "Select Leave Type", Toast.LENGTH_LONG).show();

            }

    }

    private boolean validateFromdate() {
        if (getFromDate().trim().isEmpty()) {
            inputLayoutfromdate.setError(getString(R.string.err_msg_fromdate));
            From_date.requestFocus();
            return false;
        } else {
            inputLayoutfromdate.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateTodate() {
        if (gettoDate().trim().isEmpty()) {
            inputLayouttodate.setError(getString(R.string.err_msg_todate));
            To_date.requestFocus();
            return false;
        } else {
            inputLayouttodate.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateToReason() {
        if (getReason().trim().isEmpty()) {
            inputLayoutreason.setError(getString(R.string.err_msg_reason1));
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

    public void oncalculatedays() {

        if (enddate.getTime() >= startdate.getTime()) {
            if(Utilities.isNetworkAvailable(Leave_Application.this)) {
                if(!leave_type_spineer.getSelectedItem().toString().equals("Select")) {
                    getNo_Of_DaysData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), getLeavetypeId(), getFromDate(), gettoDate(), String.valueOf(firsthalf), String.valueOf(secondhalf));
                }else {
                    //((TextView)leave_type_spineer.getSelectedView()).setError("Select Leave Type");
                    //leave_type_spineer.setE
                   // EmpowerApplication.alertdialog("Sorry!! You don't have any Leave allocated. Contact Administrator", Leave_Application.this);

                   // Toast.makeText(Leave_Application.this,"Select Leave Type",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(Leave_Application.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
            }

        } /*else {
            //Log.d("", "please enter valid start and end date");
           //alertdialog("To Date should be Greater than or Equal to From Date", this);
            //Toast.makeText(getApplicationContext(), "please enter valid start and end date", Toast.LENGTH_LONG).show();

        }*/

    }

    public void SendLeaveData(String token, String employeeid, String from_date, String to_date,String isfirsthalf,String issecondhalf, String leavetypeid,String noofdays,String reason,String resonDescription ,String Docname) {
        pd = new ProgressDialog(Leave_Application.this);
        pd.setMessage("Loading....");
        pd.show();


        // Parsing any Media type file
       // File file = new File(mediaPath);
       // RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
       // MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
       // RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Map<String,String> leave=new HashMap<String,String>();
        leave.put("token",token);
        leave.put("employeeId",employeeid);
        leave.put("fromDate",from_date);
        leave.put("toDate",to_date);
        leave.put("isFirstHalfLeave",isfirsthalf);
        leave.put("isSecondHalfLeave",issecondhalf);
        leave.put("leaveTypeID",leavetypeid);
        leave.put("noOfLeaveDays",noofdays);
        leave.put("reasonID",reason);
        leave.put("reasonDescription",resonDescription);
        leave.put("documentFileName",Docname);

        retrofit2.Call<Leave> call = apiService.LeaveApp(leave);
        call.enqueue(new Callback<Leave>() {
            @Override
            public void onResponse(retrofit2.Call<Leave> call, Response<Leave> response) {
                  pd.dismiss();
                //Log.d("User ID: ", response.body().getReponse_status());
                if (response.isSuccessful()) {

                    if(response.body().getStatus().equals("1")) {

                        dialogForApplication("Leave application for day",getNo_of_days(),Leave_Application.this);
                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Leave_Application.this);

                    }

                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);
                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);
                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }



            }

            @Override
            public void onFailure(retrofit2.Call<Leave> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public String getFromDate() {
        return From_date.getText().toString();
    }

    public String gettoDate() {
        return To_date.getText().toString();
    }

    public String getisFirstHalf() {
        return String.valueOf(firsthalf);
    }
    public String getisSecondHalf() {
        return String.valueOf(secondhalf);
    }
    public String getLeavetypeId() {
        return leavetypecode.get(leave_type_spineer.getSelectedItemPosition()-1);
    }
    public String getReasonId() {
        return leavetypereason.get(reasontype.getSelectedItemPosition()-1);
    }
    public String getReasonDescription() {
        return reason.getText().toString();
    }
    public String getNo_of_days() {
        return txt_no_of_days.getText().toString();
    }

    public String getReason() {
        return reason.getText().toString();
    }

    public void setNo_of_days(String no_of_day) {
        txt_no_of_days.setText(no_of_day);
    }

    public void LeaveBalanceData( String empid) {
        pd = new ProgressDialog(Leave_Application.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        Map<String,String> GetLeaveBalancedata=new HashMap<String,String>();
        GetLeaveBalancedata.put("employeeId",empid);

        retrofit2.Call<JsonObject> call = apiService.getLeaveBalanceData(GetLeaveBalancedata);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                 pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());

                if(response.isSuccessful()) {
                    ArrayList<String> arrayList1=new ArrayList<>();
                    ArrayList<String> arrayList2=new ArrayList<>();

                    String jsonstring=response.body().toString();
                    //Log.d("Usererererrrrre: ", response.body().getMessage());
                    try {
                        JSONObject reader = new JSONObject(jsonstring);

                        if (reader.get("status").equals("1")) {
                            Log.e("data",response.body().toString());

                            // Getting JSON Array node
                            JSONArray Data = reader.getJSONArray("data");

                            // looping through All Contacts
                            for (int i = 0; i < Data.length(); i++) {
                                JSONObject c = Data.getJSONObject(i);
                              String balance=  c.getString("CurrentBalance");
                                JSONObject leavetype = c.getJSONObject("LeaveType");
                                 String leavecode=leavetype.getString("LeaveTypeCode");
                                 arrayList1.add(balance);
                                 arrayList2.add(leavecode);

                                /*if(leavecode.equals("PL")){
                                    pl.setText(String.format("%.2f",Double.parseDouble(balance)));
                                }
                                if(leavecode.equals("CL")){
                                    cl.setText(String.format("%.2f",Double.parseDouble(balance)));
                                }
                                if(leavecode.equals("ML1")){
                                    ml.setText(String.format("%.2f",Double.parseDouble(balance)));

                                }
                                if(leavecode.equals("SL")){
                                    sl.setText(String.format("%.2f",Double.parseDouble(balance)));
                                }*/

                            }


                            for(int i=0;i<arrayList1.size();i++){
                                LinearLayout linearLayout=new LinearLayout(Leave_Application.this);
                                linearLayout.setLayoutParams( params0);
                                // linearLayout.
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                TextView textView = new TextView (Leave_Application.this);
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(Color.WHITE);
                                textView.setTextSize(14);
                                //for(int j=0;j<arrayList1.size();j++) {

                                textView.setBackgroundResource(arrayList0.get(k));
                                k++;
                                if(k==3){
                                    k=0;
                                }
                                //  break;
                                //  }
                                TextView textView1 = new TextView (Leave_Application.this);
                                textView1.setGravity(Gravity.CENTER);
                                textView1.setTextColor(Color.WHITE);
                                textView1.setTextSize(16);

                                textView.setLayoutParams(params10);
                                textView1.setLayoutParams(params10);
                                textView1.setSingleLine(true);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);
                                textView1.setText(arrayList2.get(i));
                                textView.setText(String.format("%.2f",Double.parseDouble(arrayList1.get(i))));
                                linearLayout12.addView(linearLayout);




                            }


                            if(Utilities.isNetworkAvailable(Leave_Application.this)) {
                                //LeaveBalanceData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                                LeaveTypeCodeData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                            }else {
                                Toast.makeText(Leave_Application.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if(Utilities.isNetworkAvailable(Leave_Application.this)) {
                                //LeaveBalanceData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                                LeaveTypeCodeData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                            }else {
                                Toast.makeText(Leave_Application.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
                            }
                            LinearLayout linearLayout=new LinearLayout(Leave_Application.this);
                            linearLayout.setLayoutParams( params0);
                            // linearLayout.
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView textView = new TextView (Leave_Application.this);
                            textView.setGravity(Gravity.CENTER);
                            textView.setTextColor(Color.RED);
                            textView.setTextSize(14);
                            textView.setText("Leave balance is not available");

                            //
                            EmpowerApplication.alertdialog(reader.getString("message"), Leave_Application.this);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }


    public void LeaveTypeCodeData( String empid) {
        //pd = new ProgressDialog(Leave_Application.this);
       // pd.setMessage("Loading....");
       // pd.show();
        Map<String,String> GetLeaveCodedata=new HashMap<String,String>();
        GetLeaveCodedata.put("employeeId",empid);

        final ArrayList<String> arrayList=new ArrayList<>();
        leavetypecode=new ArrayList<>();

        retrofit2.Call<LeaveCode> call = apiService.getLeaveCodeData(GetLeaveCodedata);
        call.enqueue(new Callback<LeaveCode>() {
            @Override
            public void onResponse(retrofit2.Call<LeaveCode> call, Response<LeaveCode> response) {
               // pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());
                arrayList.clear();
                leavetypecode.clear();


                if(response.isSuccessful()) {
                    arrayList.add("Select");
                    //leavetypecode.add("");
                    Log.d("Usererererrrrre: ", response.body().getMessage());
                    if (response.body().getStatus().equals("1")) {

                        for (int i=0;i<response.body().getData().size();i++){
                            arrayList.add(response.body().getData().get(i).getLeaveTypeCode());
                            leavetypecode.add(response.body().getData().get(i).getLeaveTypeID());
                        }

                        // Creating adapter for spinner
                        ArrayAdapter dataAdapter = new ArrayAdapter(Leave_Application.this, android.R.layout.simple_spinner_item, arrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                        // attaching data adapter to spinner
                        leave_type_spineer.setAdapter(dataAdapter);
                       // leave_type_spineer.setPrompt("Select");

                        String value;
                        if (extras != null) {
                            value = extras.getString("Date");
                            From_date.setText(value);
                            To_date.setText(value);
                            try {
                                startdate = dateFormatter.parse(value);
                                enddate=dateFormatter.parse(value);
                                oncalculatedays();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }


                    } else {
                        // Creating adapter for spinner
                        ArrayAdapter dataAdapter = new ArrayAdapter(Leave_Application.this, android.R.layout.simple_spinner_item, arrayList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                        // attaching data adapter to spinner
                        leave_type_spineer.setAdapter(dataAdapter);
                        EmpowerApplication.alertdialog(response.body().getMessage(), Leave_Application.this);
                        //leave_type_spineer.setPrompt("Select");

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<LeaveCode> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }
    public void getNo_Of_DaysData( String empid,String leavetyecode,String fromdate,String todate,String firsthalf,String Secondhalf) {
        //pd = new ProgressDialog(Leave_Application.this);
        // pd.setMessage("Loading....");
        // pd.show();
        Map<String,String> noofdaysdata=new HashMap<String,String>();
        noofdaysdata.put("employeeID",empid);
        noofdaysdata.put("leavetypeID",leavetyecode);
        noofdaysdata.put("fromDate",fromdate);
        noofdaysdata.put("toDate",todate);
        noofdaysdata.put("isFirstHalf",firsthalf);
        noofdaysdata.put("isSecondHalf",Secondhalf);



        retrofit2.Call<CommanResponsePojo> call = apiService.getNoOfDaysLeave(noofdaysdata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                if(response.isSuccessful()) {
                    documentlayout.removeAllViews();
                    Log.d("Usererererrrrre: ", response.body().getData());
                    Log.d("ResponseAttendance: ", response.body().toString());

                    if (response.body().getStatus().equals("1")) {
                        //txt_no_of_days.setText(response.body().getData());
                        setNo_of_days(response.body().getData());
                        //int i=10;
                        //if("SL".equals(leave_type_spineer.getSelectedItem().toString()))
                        if(isDocmentRequired) {
                            if (Double.parseDouble(response.body().getData()) >= isRequiredDays) {
                                LayoutInflater inflater = LayoutInflater.from(Leave_Application.this);
                                //to get the MainLayout
                                //View view = inflater.inflate(documentview, null);
                                inflatedLayout = inflater.inflate(R.layout.demo, (ViewGroup) findViewById(R.id.documentview), false);
                                documentlayout.addView(inflatedLayout);
                                textView = findViewById(R.id.filename);//editText_to_filename
                                docName = findViewById(R.id.editText_to_filename);
                                // documentlayout.
                            }/*else {
                                isDocmentRequired=false;
                            }*/
                        }

                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Leave_Application.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(),Leave_Application.this);

            }
        });
    }
    public void getLeavePolicyData( String empid,String leavetyecode,String token,String gender) {
        //pd = new ProgressDialog(Leave_Application.this);
        // pd.setMessage("Loading....");
        // pd.show();
        Map<String,String> leavePolicy=new HashMap<String,String>();
        leavePolicy.put("employeeID",empid);
        leavePolicy.put("leavetypeID",leavetyecode);
        leavePolicy.put("token",token);
        leavePolicy.put("gender",gender);


        Log.d("", "getLeavePolicyData11111: "+leavePolicy);

        retrofit2.Call<getConfigData> call = apiService.getLeaveConfigData1(leavePolicy);
        call.enqueue(new Callback<getConfigData>() {
            @Override
            public void onResponse(retrofit2.Call<getConfigData> call, Response<getConfigData> response) {
                isDocmentRequired=false;
                if(response.isSuccessful()) {
                    //documentlayout.removeAllViews();
                    Log.d("Usererererrrrre: ", response.body().getMessage());
                    if (response.body().getStatus().equals("1")) {
                        Log.e("data",response.body().getData().getIsDocumentRequired());
                        isDocmentRequired = Boolean.parseBoolean(response.body().getData().getIsDocumentRequired());

                        if((response.body().getData().getMinDaysForDocumentRequired())!= null) {
                            isRequiredDays = Integer.parseInt((response.body().getData().getMinDaysForDocumentRequired()));

                        }
                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Leave_Application.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<getConfigData> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(),Leave_Application.this);
                Toast.makeText(Leave_Application.this, "Exception", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void LeaveTypeReasonData( String leavetypecode) {
        //pd = new ProgressDialog(Leave_Application.this);
        // pd.setMessage("Loading....");
        // pd.show();
        Map<String,String> GetLeaveCodedata=new HashMap<String,String>();
        GetLeaveCodedata.put("leaveTypeID",leavetypecode);

        final ArrayList<String> arrayList1=new ArrayList<>();
        leavetypereason=new ArrayList<>();

        retrofit2.Call<Reason> call = apiService.getLeaveReasonData(GetLeaveCodedata);
        call.enqueue(new Callback<Reason>() {
            @Override
            public void onResponse(retrofit2.Call<Reason> call, Response<Reason> response) {
                // pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());
                arrayList1.clear();
                leavetypereason.clear();

                if(response.isSuccessful()) {
                    arrayList1.add("Select");
                   // leavetypereason.add("we12");
                    Log.d("Usererererrrrre: ", response.body().getMessage());
                    if (response.body().getStatus().equals("1")) {
                        for (int i=0;i<response.body().getData().size();i++){
                            arrayList1.add(response.body().getData().get(i).getReasonTitle());
                            leavetypereason.add(response.body().getData().get(i).getReasonID());
                        }

                        // Creating adapter for spinner
                        ArrayAdapter dataAdapter = new ArrayAdapter(Leave_Application.this, android.R.layout.simple_spinner_item, arrayList1);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reasontype.setAdapter(dataAdapter);


                    } else {
                        // Creating adapter for spinner
                        ArrayAdapter dataAdapter = new ArrayAdapter(Leave_Application.this, android.R.layout.simple_spinner_item, arrayList1);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        reasontype.setAdapter(dataAdapter);

                        EmpowerApplication.alertdialog(response.body().getMessage(), Leave_Application.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<Reason> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }



    public void UpLoadFileorDoc( String filepath) {
        pd = new ProgressDialog(Leave_Application.this);
        pd.setMessage("File uploading....");
         pd.show();

       // Map<String,String> GetLeaveCodedata=new HashMap<String,String>();
       // GetLeaveCodedata.put("employeeId",empid);
       // GetLeaveCodedata.put("leaveTypeID",leavetypecode);
        //GetLeaveCodedata.put("token",token);
       // GetLeaveCodedata.put("gender",gender);
        //Parsing any Media type file
         File file = new File(filepath);
         RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
         MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
         //RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


        retrofit2.Call<fileUpload> call = apiService.uploadLeaveDoc(fileToUpload);
        call.enqueue(new Callback<fileUpload>() {
            @Override
            public void onResponse(retrofit2.Call<fileUpload> call, Response<fileUpload> response) {
                  if(pd.isShowing()) {
                      pd.dismiss();
                  }
                if(response.isSuccessful()) {
                    Log.e("data",response.body().toString());
                    if (response.body().getStatus().equals("1")) {

                      String  json=  response.body().getData();
                        Log.e("data",json);

                        JSONObject jsonObject1= null;
                        try {
                            jsonObject1 = new JSONObject(json);
                            String data=   jsonObject1.get("Result").toString();

                            EmpowerApplication.set_session("filename",jsonObject1.get("FileName").toString());
                            docName.setText(jsonObject1.get("FileName").toString());
                            //Toast.makeText(Leave_Application.this,jsonObject1.get("FileName").toString(),Toast.LENGTH_LONG).show();
                           // Log.e("data123",data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Leave_Application.this);


                    }

                   // try {
                       // String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());
                     // String data=  readString( response.body().string());
                     // Log.e("data",data);
                       // String jobject=response.toString();
                       // JSONObject jsonObject= new JSONObject("{"+jobject+"}");

                         // Log.e("hiii",jsonObject.toString());

                   /* if (jsonObject.get("status").equals("1")) {

                        JSONObject jsonObject1= jsonObject.getJSONObject("data");

                       String data= jsonObject1.get("Result").toString();



                    } else {
                        EmpowerApplication.alertdialog(jsonObject.get("message").toString(), Leave_Application.this);


                    }*/
                   /* } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<fileUpload> call, Throwable t) {
                // Log error here since request failed
                if(pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("TAG", t.toString());

            }
        });
    }

   /* public String readString(String result){
        //Try to get response body
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(result.getBytes().in));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String result = sb.toString();
    }*/

    public void GetWorkFlowForLeaveApp(String employeeId,String token,String leavetypeid) {



        Map<String,String> GetWorkFlowdata=new HashMap<String,String>();
        GetWorkFlowdata.put("employeeId",employeeId);
        GetWorkFlowdata.put("token",token);
        GetWorkFlowdata.put("leaveTypeId",leavetypeid);

        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();

        retrofit2.Call<FinalResponsePoJo> call = apiService.getWorkFlowForLeave(GetWorkFlowdata);
        call.enqueue(new Callback<FinalResponsePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<FinalResponsePoJo> call, Response<FinalResponsePoJo> response) {

                level1.clear();
                Log.e("Arraysize1", String.valueOf(level1.size()));
                level2.clear();
                Log.e("Arraysize2", String.valueOf(level2.size()));
                level3.clear();
                Log.e("Arraysize3", String.valueOf(level3.size()));
                level4.clear();
                Log.e("Arraysize4", String.valueOf(level4.size()));

                linearLayout.removeAllViews();
                linearLayout2.removeAllViews();
                linearLayout3.removeAllViews();
                linearLayout4.removeAllViews();
                img1.removeAllViews();
                img2.removeAllViews();
                img3.removeAllViews();
                if (response.isSuccessful()) {


                    Log.d("ForLeave ", new Gson().toJson(response.body()));

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
                        EmpowerApplication.alertdialog(response.body().getMessage(),Leave_Application.this);
                        Toast.makeText(Leave_Application.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Leave_Application.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Leave_Application.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Leave_Application.this);

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
                Log.e("viewArraysize1", String.valueOf(level1.size()));


                //CircleImageView imageview = new CircleImageView(MainActivity.this);
                TextView textView = new TextView (Leave_Application.this);


                //linearLayout.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout.addView(textView);
                textView.setText(level1.get(i));

            }
        }

        if(!(level2.size() ==0)){
            //ImageView imageView=findViewById(R.id.arrow1);
            ImageView imageView=new ImageView(Leave_Application.this);
            imageView.setImageResource(R.drawable.ic_action_name);
            img1.addView(imageView);
            for(int i=0;i<level2.size();i++){
                Log.e("viewArraysize2", String.valueOf(level2.size()));

                TextView textView = new TextView (Leave_Application.this);
                textView.setGravity(Gravity.CENTER);

                textView.setLayoutParams(params);
                linearLayout2.addView(textView);
                textView.setText(level2.get(i));
            }
        }
        if(!(level3.size() ==0)){
            //ImageView imageView=findViewById(R.id.arrow2);
            ImageView imageView=new ImageView(Leave_Application.this);
            imageView.setImageResource(R.drawable.ic_action_name);
            img2.addView(imageView);
            for(int i=0;i<level3.size();i++){


                TextView textView = new TextView (Leave_Application.this);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(params);
                linearLayout3.addView(textView);
                textView.setText(level3.get(i));
            }
        }
        if(!(level4.size() ==0)){
            //ImageView imageView=findViewById(R.id.arrow3);
            ImageView imageView=new ImageView(Leave_Application.this);
            imageView.setImageResource(R.drawable.ic_action_name);
            img3.addView(imageView);
            for(int i=0;i<level4.size();i++){
                TextView textView = new TextView (Leave_Application.this);
                textView.setGravity(Gravity.CENTER);

                //linearLayout4.addView(imageview);
                textView.setLayoutParams(params);
                linearLayout4.addView(textView);
                textView.setText(level4.get(i));
            }
        }

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
                // HomeFragment.key.contains("")
                if (HomeFragment.key.contains("HideLeaveApp")) {
                    int index = HomeFragment.key.indexOf("HideLeaveApp");
                    Intent intentSurvey = new Intent(Leave_Application.this, ApplicationStatusActivity.class);
                    intentSurvey.putExtra("ARG_PAGE", index);
                    startActivity(intentSurvey);
                    dialog1.dismiss();
                }
            }
        });
    }
    public  void alertdialog(String msg, final Context context) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



   /* private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file*//*");//file*//*//*///*
        // Valid File Format : bmp, gif, png, jpg, jpeg, doc, docx, pdf, txt, xls, xlsx
       // intent.setType("image*//*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.addCategory(Intent.Ca);
        /*try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Leave_Application.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedFileURI = data.getData();
                String uriString = selectedFileURI.toString();
                File file = new File(selectedFileURI.getPath().toString());
                Log.d("", "File : " + file.getName());
                String displayName = null;
                String uploadedFileName = file.getName().toString();
                // tokens = new StringTokenizer(uploadedFileName, ":");
                // String first = tokens.nextToken();
                //String file_1 = tokens.nextToken().trim();
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(selectedFileURI, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String displayName1 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            String fileExt = displayName1.substring(displayName1.lastIndexOf('.'));
                            if (arrayList.indexOf(fileExt) < 0) {
                                displayName="Invalid file format";

                            }else {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    String fileExt = uploadedFileName.substring(uploadedFileName.lastIndexOf('.'));
                    if (arrayList.indexOf(fileExt) < 0) {
                        displayName="Invalid file format";

                    }else {
                        displayName = file.getName();                    }
                    //displayName = file.getName();
                }
                textView.setText(displayName);
            }
        }
    }

    public void OnBrowse(View view) {
        showFileChooser();
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        filePath="";
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedFileURI = data.getData();
              //String data1=  getRealPathFromURI(selectedFileURI);
                String uriString = selectedFileURI.toString();
                File file = new File(selectedFileURI.getPath().toString());
               //UpLoadFileorDoc(data1);
                //filePath=selectedFileURI.getPath();
                Log.d("", "File : " + file.getName());
                String displayName = null;
                String uploadedFileName = file.getName().toString();
                // tokens = new StringTokenizer(uploadedFileName, ":");
                // String first = tokens.nextToken();
                //String file_1 = tokens.nextToken().trim();



                String  thePath="";

                if (uriString.startsWith("content://"))
                {
                    String data1=  getRealPathFromURI(selectedFileURI);
                    Cursor cursor = null;
                    try {
                        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
                        cursor = getContentResolver().query(selectedFileURI, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String displayName1 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            String fileExt = displayName1.substring(displayName1.lastIndexOf('.'));
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                              thePath = cursor.getString(columnIndex);
                            if (arrayList.indexOf(fileExt) < 0) {
                                displayName="Invalid file format";

                            }else {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                UpLoadFileorDoc(data1);
                            }
                        }
                    } finally {
                        cursor.close();
                    }
                }if (uriString.startsWith("file://")  ) {
                    try {


                        String fileExt = uploadedFileName.substring(uploadedFileName.lastIndexOf('.'));
                        if (arrayList.indexOf(fileExt) < 0) {
                            displayName = "Invalid file format";

                        } else {
                            displayName = file.getName();
                            UpLoadFileorDoc(selectedFileURI.getPath());
                        }
                        //displayName = file.getName();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
               // UpLoadFileorDoc(thePath);
                textView.setText(displayName);
            }
        }

          if (requestCode == REQUEST_IMAGE_CAPTURE) {
              if (resultCode == Activity.RESULT_OK) {
                String dirName="/Favorite/";
                Bitmap image = (Bitmap) data.getExtras().get("data");
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                String fileName = "fav" + timeStamp + ".JPG";


                File direct = new File(Environment.getExternalStorageDirectory() + dirName);

                if (!direct.exists()) {
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + dirName);
                    wallpaperDirectory.mkdirs();
                }

                File file = new File(new File(Environment.getExternalStorageDirectory() + dirName), fileName);
                  filePath=Environment.getExternalStorageDirectory() + dirName+fileName;
                 // UpLoadFileorDoc(Environment.getExternalStorageDirectory() + dirName+fileName);
                if (file.exists()) {
                    file.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);

                    //Bitmap bitmap = BitmapFactory.decodeFile(imagesPathArrayList.get(pos));
                    image.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                  //Uri selectedFileURI = data.getExtras(MediaStore.EXTRA_OUTPUT);
                 // File file = new File(mCurrentPhotoPath.getPath().toString());
                  UpLoadFileorDoc(Environment.getExternalStorageDirectory() + dirName+fileName);
                textView.setText(fileName);
                // do whatever you want with the image now
            }
        }

        if (requestCode == REQUEST_Document) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedFileURI = data.getData();
                String uriString = selectedFileURI.toString();

                File file = new File(selectedFileURI.getPath().toString());

                filePath=selectedFileURI.getPath();
                Log.d("", "File : " + file.getName());
                String displayName = null;
                String uploadedFileName = file.getName().toString();




               // if( ! uploadedFileName.substring(uploadedFileName.lastIndexOf('.')).equalsIgnoreCase(".mp4")){


                if (uriString.startsWith("file://")  ) {
                    try {


                        String fileExt = uploadedFileName.substring(uploadedFileName.lastIndexOf('.'));
                        if (arrayList.indexOf(fileExt) < 0) {
                            displayName = "Invalid file format";

                        } else {
                            displayName = file.getName();
                            UpLoadFileorDoc(selectedFileURI.getPath());
                        }
                        //displayName = file.getName();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    //UpLoadFileorDoc(selectedFileURI.getPath());
                }else if(uriString.startsWith("content://")) {
                    Cursor cursor = null;
                   /* String extension = path.substring(path.lastIndexOf("."));
                    if(arrayList.indexOf(extension) <0) {
                        displayName = "Invalid file format";

                    }else {*/
                    // Pattern pattern = Pattern.compile("(content://media/*)");
                    // Matcher matcher = pattern.matcher(selectedFileURI.getPath());

                   /* if (matcher.find()) {
                        String path = getRealPathFromURI(selectedFileURI);
*/
                    if (uriString.contains(".mp4")) {
                       // String extension = filePath.substring(filePath.lastIndexOf("."));
                       // if (arrayList.indexOf(extension) < 0) {
                            displayName = "Invalid file format";
                            docName.setText("");


                       // }
                        //String path = getRealPathFromURI(selectedFileURI);
                    } else {
                        String path = getRealPathFromURI(selectedFileURI);
                        try {
                            // String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
                            cursor = getContentResolver().query(selectedFileURI, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                String displayName1 = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                String fileExt = displayName1.substring(displayName1.lastIndexOf('.'));
                                // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                // thePath = cursor.getString(columnIndex);
                                if (arrayList.indexOf(fileExt) < 0) {
                                    displayName = "Invalid file format";

                                } else {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                    //String path = getRealPathFromURI(selectedFileURI);
                                    UpLoadFileorDoc(path);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            cursor.close();
                        }
                    /*}else {
                        displayName = "Invalid file format";

                    }*/
                    }
                    //String path=  getRealPathFromURI(selectedFileURI);
                    //UpLoadFileorDoc(path);

                    // }
                /*}else {
                    displayName = "Invalid file format";

                }*/
                }
                textView.setText(displayName);
            }
        }
    }

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Window window = dialog.getWindow();
        //WindowManager.LayoutParams wlp = window.getAttributes();

        //wlp.gravity = Gravity.BOTTOM;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.attachmentpopup);

        ImageButton imageButton= dialog.findViewById(R.id.gallery);
        ImageButton imageButton1= dialog.findViewById(R.id.camera);
        ImageButton imageButton2= dialog.findViewById(R.id.camera1);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            REQUEST_GALLARY );
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Leave_Application.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

               /* if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        // Log.i(TAG, "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }*/
                dialog.dismiss();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("application/*");
               // intent1.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent1, "Select a File to Upload"),
                            REQUEST_Document );
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Leave_Application.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void OnDialog(View view) {
        showDialog(Leave_Application.this);
    }



    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor=null;
        int column_index=0;
        try {
            // can post image
            String[] proj = {MediaStore.Images.Media.DATA};
             cursor = managedQuery(contentUri,
                    proj, // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null); // Order-by clause (ascending by name)
             column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
           // return cursor.getString(column_index);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return cursor.getString(column_index);
    }
}


