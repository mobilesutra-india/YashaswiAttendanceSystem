package seedcommando.com.yashaswi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.loginpojo.generateotp.GenerateOTP;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.utilitys.NotificationUtils;

public class OTPLoginActivity extends AppCompatActivity {

    private EditText Username, password;
    private Button login;
    private CheckBox Remember_me;
    static SharedPreferences sp;
    private TextView newregister;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    boolean allgranted = false, login_status = false;
    private ApiInterface apiService;
    ProgressDialog pd;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //LinearLayout OTP,passwordLayout,alreadyOTPlayout,loginLayout,parentLayout;


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
        setContentView(R.layout.otp_login_activity);
        Username = findViewById(R.id.editText_username);
        Remember_me = findViewById(R.id.checkBox_remember_me);
        newregister = findViewById(R.id.move_Reg_page);
        inputLayoutName = findViewById(R.id.input_layout_floatusername);
        inputLayoutPassword = findViewById(R.id.input_layout_floatpassword);
        Username.addTextChangedListener(new MyTextWatcher(Username));
      //  password.addTextChangedListener(new MyTextWatcher(password));
        //SpannableStringBuilder builder1 = setStarToLabel("Username");
        //inputLayoutName.setHint(builder1);

        //for firebase notification
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

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

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
        if (sp.contains("username1") ) {
            Username.setText(sp.getString("username1", null));

        }

        newregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTPLoginActivity.this, RegistrationActivity.class));
            }
        });

        Remember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    /*if (!validateName()) {
                        return;
                    }*/
                   // if(!Username.getText().toString().isEmpty()) {


                    Intent i = new Intent(OTPLoginActivity.this, OTPAuthActivity.class);
                        startActivity(i);
                        // close this activity
                        finish();
                   // }
                }

            }
        });

       // passwordLayout.removeAllViews();
        //loginLayout.removeAllViews();
    }

    public void onGenerateOTP(View v) {
        loginCheck1();
    }



    public void loginCheck1() {

        if (!validateName()) {
            return;
        }
        //check username and password are correct and then add them to SharedPreferences
        if (Username.getText().toString().length() <= 150) {

                login_status = true;

                String devId = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId"));
                String empcode = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode"));
                String ServiceUrl = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("serviceUrl"));
                // Log.e("id&code", devId + "" + empcode);
                //Log.e("serviceUrl",ServiceUrl);

                //startActivity(new Intent(OTPLoginActivity.this, MapsActivity.class));
                // finish();
                if (allgranted) {

                    EmpowerApplication.set_session("User_Name",Username.getText().toString());


                    if (devId != null && empcode != null && ServiceUrl != null) {
                        apiService = ApiClient.getClient().create(ApiInterface.class);

                        if (Utilities.isNetworkAvailable(OTPLoginActivity.this)) {
                            //if(EmpowerApplication.sharedPref.contains("deviceId") && EmpowerApplication.sharedPref.contains("employeeCode")) {

                            generateOTP(Username.getText().toString(), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")));
                            /*}else {
                                Toast.makeText(OTPLoginActivity.this,"Your Are Not Registered",Toast.LENGTH_LONG).show();
                            }*/
                        } else {
                            Toast.makeText(OTPLoginActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        EmpowerApplication.alertdialog("Empolyee Need to do Registration", OTPLoginActivity.this);

                    }


                    //finish();
                } else {
                    permissionCheck();
                }

        } else {
            Username.setError("Lenth Should be less than 150");
            //Toast.makeText(this, "Lenth Shoud be less than 150", Toast.LENGTH_LONG).show();
        }
    }

    public void dialog(String msg) {
        final Dialog dialog1 = new Dialog(OTPLoginActivity.this);
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
        if (ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[4])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(OTPLoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(OTPLoginActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(OTPLoginActivity.this);
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
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
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
                ActivityCompat.requestPermissions(OTPLoginActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(OTPLoginActivity.this, permissionsRequired[4])) {
                //txtPermissions.setText("Permissions Required");
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(OTPLoginActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(OTPLoginActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

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
            if (ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
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
            if (ActivityCompat.checkSelfPermission(OTPLoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
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

        Log.e("Firebase reg id: ", regId);


    }

    public void generateOTP(String username, String deviceid, String employeecode) {
        pd = new ProgressDialog(OTPLoginActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String, String> generateOTP = new HashMap<String, String>();
        generateOTP.put("userName", username);
        generateOTP.put("deviceId", deviceid);
        generateOTP.put("employeeCode", employeecode);

        retrofit2.Call<GenerateOTP> call = apiService.generateOTP(generateOTP);
        call.enqueue(new Callback<GenerateOTP>() {
            @Override
            public void onResponse(retrofit2.Call<GenerateOTP> call, Response<GenerateOTP> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {

                        if (Remember_me.isChecked()) {
                            //EmpowerApplication.set_session("username", Username.getText().toString());
                            //EmpowerApplication.set_session("password", password.getText().toString());
                            SharedPreferences.Editor e = sp.edit();
                            e.putString("username1", Username.getText().toString());
                           // e.putString("password", password.getText().toString());
                            e.commit();
                        } else {
                            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();

                        }
                        Toast.makeText(OTPLoginActivity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();

                        /*for (Data o : response.body().getData()) {
                            if (o.getKey().equals("Token")) {
                                Log.e("token", o.getValue());
                                EmpowerApplication.set_session("data", o.getValue());


                            }
                            if (o.getKey().equals("IsManager")) {
                                Log.e("IsManager", o.getValue());
                                EmpowerApplication.set_session("IsManager", o.getValue());


                            }


                        }*/


                        startActivity(new Intent(OTPLoginActivity.this, OTPAuthActivity.class));

                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), OTPLoginActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", OTPLoginActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", OTPLoginActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", OTPLoginActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<GenerateOTP> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                try {
                    if (t.getMessage().equals("Field map contained null value for key 'employeeCode'") || t.getMessage().equals("Field map contained null value for key 'deviceId'")) {
                        EmpowerApplication.alertdialog("Empolyee need to do registration", OTPLoginActivity.this);
                    } else {
                        EmpowerApplication.alertdialog(t.getMessage(), OTPLoginActivity.this);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }


}
