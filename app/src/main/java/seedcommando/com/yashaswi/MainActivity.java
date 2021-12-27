package seedcommando.com.yashaswi;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.ChangePwdPOJO;
import seedcommando.com.yashaswi.pojos.LoginPOJO;
import seedcommando.com.yashaswi.pojos.loginpojo.Data;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.utilitys.NotificationUtils;

public class MainActivity extends AppCompatActivity {
    private EditText Username, password;
    private Button login;
    private CheckBox Remember_me;
    SharedPreferences sp;
    private TextView newregister ,forgotpwd;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    boolean allgranted = false, login_status = false;
    private ApiInterface apiService;
    ProgressDialog pd;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //permission declartion.............
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        // FirebaseCrash.report(new Exception("My first Android ` error"));
        setContentView(R.layout.activity_main);
        //EmpowerApplication.set_session("serviceUrl", "http://seedmanagement.cloudapp.net/Enterprise_MobileApp/app/Api/");
        Username = findViewById(R.id.editText_username);
        password = findViewById(R.id.editText_password);
        login = findViewById(R.id.button_login);
        Remember_me = findViewById(R.id.checkBox_remember_me);
        newregister = findViewById(R.id.move_Reg_page);
        inputLayoutName = findViewById(R.id.input_layout_floatusername);
        inputLayoutPassword = findViewById(R.id.input_layout_floatpassword);
        forgotpwd = findViewById(R.id.forgotpwd);
        Username.addTextChangedListener(new MyTextWatcher(Username));
        password.addTextChangedListener(new MyTextWatcher(password));
        //SpannableStringBuilder builder1 = setStarToLabel("Username");
        //inputLayoutName.setHint(builder1);
        //for firebase notification
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
  //      Log.d("", "onCreate_refreshedToken: "+refreshedToken);


        check_for_upgrade();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    getFirebaseRegId();
                    //displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                   // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }

            }
        };
        // apiService= ApiClient.getClient().create(ApiInterface.class);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (Build.VERSION.SDK_INT > 23) {
            permissionCheck();
        }
        permissionCheck();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        //if SharedPreferences contains username and password then redirect to Home activity
        if (sp.contains("username") && sp.contains("password")) {
            Username.setText(sp.getString("username", null));
            password.setText(sp.getString("password", null));
            Remember_me.setChecked(true);
           /* if(Utilities.isNetworkAvailable(MainActivity.this)) {

                SendLoginData(sp.getString("username",""),sp.getString("password",""),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")));
            }else {
                Toast.makeText(MainActivity.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
            }*/
            //startActivity(new Intent(MainActivity.this,ManagerActivity.class));
            // finish();   //finish current activity
        }

        newregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });


        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(MainActivity.this, ChangepwdActivity.class));
                showCustomDialog();



            }
        });
    }

    public void onLogin(View v) {
        loginCheck();
    }

    public void loginCheck() {

        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        //check username and password are correct and then add them to SharedPreferences
        if (Username.getText().toString().length() <= 150) {
            if (password.getText().toString().length() <= 100) {
                //if (Username.getText().toString().equals("emp") && password.getText().toString().equals("sa")) {
                //Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                login_status = true;
                String devId = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId"));
                String empcode = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode"));
                String ServiceUrl = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("serviceUrl"));
                // Log.e("id&code", devId + "" + empcode);
                //Log.e("serviceUrl",ServiceUrl);
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
                // finish();
                if (allgranted) {
                    if (devId != null && empcode != null && ServiceUrl != null) {
                        apiService = ApiClient.getClient().create(ApiInterface.class);
                        if (Utilities.isNetworkAvailable(MainActivity.this)) {
                            //if(EmpowerApplication.sharedPref.contains("deviceId") && EmpowerApplication.sharedPref.contains("employeeCode")) {
                            SendLoginData(Username.getText().toString(), password.getText().toString(), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")));
                            /*}else {
                                Toast.makeText(MainActivity.this,"Your Are Not Registered",Toast.LENGTH_LONG).show();
                            }*/
                        } else {
                            Toast.makeText(MainActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        EmpowerApplication.alertdialog("Empolyee Need to do Registration", MainActivity.this);
                    }
                    //finish();
                } else {
                    permissionCheck();
                }
                /*} else {
                    dialog("Incorrect Login Details");
                    //Toast.makeText(MainActivity.this,"Incorrect Login Details",Toast.LENGTH_LONG).show();
                }*/
            } else {
                password.setError("Lenth Shoud be less than 100");
               // Toast.makeText(this, "Lenth Shoud be less than 100", Toast.LENGTH_LONG).show();
            }
        } else {
            Username.setError("Lenth Shoud be less than 150");
            //Toast.makeText(this, "Lenth Shoud be less than 150", Toast.LENGTH_LONG).show();
        }
    }

    public void dialog(String msg) {
        final Dialog dialog1 = new Dialog(MainActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = dialog1.findViewById(R.id.textView_dialog);
        title.setText(msg);
        Button btn_ok = dialog1.findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
    }

    public void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[4])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // builder.show();

                AlertDialog alert = builder.create();
                alert.show();
                // Button nbutton = alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor("#3F51B5");
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLUE);

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                       // Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            // txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[4])) {
                //txtPermissions.setText("Permissions Required");
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //builder.show();
                AlertDialog alert = builder.create();
                alert.show();

                alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLUE);

            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        //txtPermissions.setText("We've got all permissions");
        //Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
        allgranted = true;
        if (allgranted) {
            //registerdata = new HTTPLogin_Register(1);
            //registerdata.execute();
               /* if(login_status) {
                    Intent i = new Intent(getApplicationContext(), ManagerActivity
                            .class);
                    startActivity(i);
                }
            else
                {
                   // Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
                }*/

        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private boolean validateName() {
        if (Username.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            Username.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            password.requestFocus();
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
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
                case R.id.editText_username:
                    validateName();
                    break;

                case R.id.editText_password:
                    validatePassword();
                    break;
            }
        }
    }

    @NonNull
    private SpannableStringBuilder setStarToLabel(String text) {
        String simple = text;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public void SendLoginData(String username, String Password, String deviceid, String employeecode) {
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String, String> logindata = new HashMap<String, String>();
        logindata.put("userName", username);
        logindata.put("password", Password);
        logindata.put("deviceId", deviceid);
        logindata.put("employeeCode", employeecode);

        retrofit2.Call<LoginPOJO> call = apiService.SendLoginData(logindata);
        call.enqueue(new Callback<LoginPOJO>() {
            @Override
            public void onResponse(retrofit2.Call<LoginPOJO> call, Response<LoginPOJO> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {

                        Log.d("login Data response ",new Gson().toJson(response.body()));

                        if (Remember_me.isChecked()) {
                            //EmpowerApplication.set_session("username", Username.getText().toString());
                            //EmpowerApplication.set_session("password", password.getText().toString());
                            SharedPreferences.Editor e = sp.edit();
                            e.putString("username", Username.getText().toString());
                            e.putString("password", password.getText().toString());
                            e.commit();
                        } else {
                            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();

                        }

                        for (Data o : response.body().getData()) {
                            if (o.getKey().equals("Token")) {
                               // Log.e("token", o.getValue());
                                EmpowerApplication.set_session("data", o.getValue());


                            }
                            if(o.getKey().equals("AllowAttendancefromMobileApp")){
                                EmpowerApplication.set_session("AllowAttendancefromMobileApp", o.getValue());

                            }
                            if (o.getKey().equals("IsManager")) {
                               // Log.e("IsManager", o.getValue());
                                EmpowerApplication.set_session("IsManager", o.getValue());


                            }




                        }


                        startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), MainActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", MainActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", MainActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", MainActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<LoginPOJO> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                try {
                    if (t.getMessage().equals("Field map contained null value for key 'employeeCode'") || t.getMessage().equals("Field map contained null value for key 'deviceId'")) {
                        EmpowerApplication.alertdialog("Empolyee need to do registration", MainActivity.this);
                    } else {
                        EmpowerApplication.alertdialog(t.getMessage(), MainActivity.this);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void getFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

       // Log.e("Firebase reg id: ", regId);


    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button buttonOk = (Button) dialogView.findViewById(R.id.buttonOk);
        final EditText Empid = (EditText)dialogView.findViewById(R.id.editText_Empid1);



        dialogView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Empid.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    //ForgotPasswordMethod(user_name.getText().toString(),  EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")));

                } else {
                    String ServiceUrl = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("serviceUrl"));
                    if(ServiceUrl != null){
                        ForgotPasswordMethod(Empid.getText().toString().trim(), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")));
                        alertDialog.dismiss();
                    }else {
                        EmpowerApplication.alertdialog("Empolyee Need to do Registration", MainActivity.this);
                    }


                }

            }
        });


    }


    public void ForgotPasswordMethod(String user_name, String device_id) {


        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String, String> logindata = new HashMap<>();
        logindata.put("userName", user_name);
        logindata.put("employeeCode", user_name);
        logindata.put("deviceId", device_id);

        Log.d("", "ForgotPasswordMethod: " + logindata);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ChangePwdPOJO> call = apiService.ForgotPassword(logindata);
        call.enqueue(new Callback<ChangePwdPOJO>() {
            @Override
            public void onResponse(retrofit2.Call<ChangePwdPOJO> call, Response<ChangePwdPOJO> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {

                        pd.dismiss();

                        EmpowerApplication.alertdialog(response.body().getMessage(), MainActivity.this);

                        // Toast.makeText(MainActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), MainActivity.this);
                        pd.dismiss();

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", MainActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", MainActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", MainActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ChangePwdPOJO> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                try {
                    if (t.getMessage().equals("Field map contained null value for key 'employeeCode'") || t.getMessage().equals("Field map contained null value for key 'deviceId'")) {
                        EmpowerApplication.alertdialog("Empolyee need to do registration", MainActivity.this);
                    } else {
                        EmpowerApplication.alertdialog(t.getMessage(), MainActivity.this);


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }



    void check_for_upgrade() {


        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion,MainActivity.this).execute();


    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;
        public ForceUpdateAsync(String currentVersion, Context context){
            this.currentVersion = currentVersion;
            this.context = context;
        }


        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()+ "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();



            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            Log.d("", "latestVersion: "+latestVersion);

            if(latestVersion!=null){
                if(!currentVersion.equalsIgnoreCase(latestVersion)){
                    // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                    if(!(context instanceof ManagerActivity)) {
                        if(!((Activity)context).isFinishing()){
                            showForceUpdateDialog();
                        }
                    }
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                    R.style.DialogDark));

            alertDialogBuilder.setTitle(context.getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(context.getString(R.string.youAreNotUpdatedMessage) + "  " +  latestVersion+" " + context.getString(R.string.youAreNotUpdatedMessage1));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }
    }






}


