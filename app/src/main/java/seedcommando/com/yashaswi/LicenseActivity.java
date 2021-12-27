package seedcommando.com.yashaswi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.LicensePOJO;
import seedcommando.com.yashaswi.pojos.config.EmployeeConfig;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 1/25/2018.
 */

public class LicenseActivity extends AppCompatActivity {

    private EditText licensecode;
    private TextInputLayout inputLayoutlicensecode;
    private Button register;
    private seedcommando.com.yashaswi.restforreg.ApiInterface apiService;
    private ApiInterface apiService1;
    SharedPreferences sp;
    String PREFS_NAME = "EmployeeData";
    public SharedPreferences.Editor editor;
    ProgressDialog pd;
    TextView message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licensecode1);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        message = findViewById(R.id.message1);
        String email = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("Mail"));
        String email1 = "License code has been sent to your Mail id" + " " + "\"" + EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("Mail")) + "\"" + "," + " " + "please enter the same here to register.";
        //message.setText(Html.fromHtml("License code has been sent to your Mail id"+" "+email1+","+" "+"please enter the same here to register."));
        String htmlText = email1.replace(email, "<font color='#ffffff'>" + email + "</font>");
        message.setText(Html.fromHtml(htmlText));

        // message.setText("License code has been sent to your Mail id"+" "+"\""+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("Mail"))+"\""+","+" "+"please enter the same here to register.");

        licensecode = findViewById(R.id.editText_license_code);
        inputLayoutlicensecode = findViewById(R.id.input_layout_floatreg);
        register = findViewById(R.id.button_reg);
        licensecode.addTextChangedListener(new MyTextWatcher(licensecode));
        apiService = seedcommando.com.yashaswi.restforreg.ApiClient.getClient().create(seedcommando.com.yashaswi.restforreg.ApiInterface.class);

        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatelicencecode()) {
                    return;
                }
                // Toast.makeText(LicenseActivity.this,"in reg",Toast.LENGTH_LONG).show();
                if (licensecode.getText().toString().length() <= 50) {

                    // Toast.makeText(LicenseActivity.this,"submitted sucessfully",Toast.LENGTH_LONG).show();
                    //Log.e("empcode",sp.getString("companyCode",""));
                    //Log.e("Companycode",sp.getString("employeeCode",""));
                    //Log.e("Deviceid",getDeviceID());
                    String fcid = getFirebaseRegId();


                    if (Utilities.isNetworkAvailable(LicenseActivity.this)) {

                        SendLicenseRegData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")), getDeviceID(), licensecode.getText().toString(), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("companyCode")), fcid);
                    } else {
                        Toast.makeText(LicenseActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                    }


                    //startActivity(new Intent(LicenseActivity.this,MainActivity.class));
                } else {
                    //licensecode.setError("License code Should be less than 50 Integers");
                    inputLayoutlicensecode.setError("License code Should be less than 50 digits");
                }

            }
        });

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
                case R.id.editText_license_code:
                    validatelicencecode();
                    break;


            }
        }
    }

    private boolean validatelicencecode() {
        if (licensecode.getText().toString().trim().isEmpty()) {
            inputLayoutlicensecode.setError("Enter LicenseCode");
            licensecode.requestFocus();
            return false;
        } else {
            inputLayoutlicensecode.setErrorEnabled(false);
        }

        return true;
    }

    public void SendLicenseRegData(String empcode, String deviceid, String licensecode, String companycode, String fcmid) {

        LicensePOJO licensePOJO = new LicensePOJO();
        licensePOJO.setLicensecode(licensecode);
        licensePOJO.setEmpId(empcode);
        licensePOJO.setDeviceId(deviceid);
        //Log.e("sdata",licensePOJO.getLicensecode());
        //Log.e("sdata",licensePOJO.getEmpId());
        // Log.e("sdata",licensePOJO.getDeviceId());
        pd = new ProgressDialog(LicenseActivity.this);
        pd.setMessage("loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        Map<String, String> regdata = new HashMap<String, String>();
        regdata.put("employeeCode", empcode);
        regdata.put("deviceId", deviceid);
        regdata.put("licenseNumber", licensecode);
        regdata.put("companyCode", companycode);
        regdata.put("FCMID", "empty for yashwaswi");
       /* Log.e("name: ", deviceid);
        Log.e("name: ", empcode);
        Log.e("name: ",licensecode );
        Log.e("name: ",companycode );*/
        // Log.e("fcid: ",fcmid);
        retrofit2.Call<LicensePOJO> call = apiService.SendLicenseRegData(regdata);
        call.enqueue(new Callback<LicensePOJO>() {
            @Override
            public void onResponse(retrofit2.Call<LicensePOJO> call, Response<LicensePOJO> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    // Log.d("User ID: ", response.body().getStatus());
                    if (response.body().getStatus().equals("1")) {
                        ((EmpowerApplication) getApplication()).EmployeePersonalDetails(EmpowerApplication.aesAlgorithm.Encrypt(String.valueOf(response.body().getData().getEmployee().getEmployeeID())), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getEmployee().getFirstName()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getEmployee().getLastName()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getEmployee().getEmployeeImage()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getEmployee().getGender()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getEmployee().getOfficalEmailID()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getDesignationName()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getEmployee().getBirthDate()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getDepartmentName()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getCategoryName()), EmpowerApplication.aesAlgorithm.Encrypt(response.body().getData().getBranchName()));

                        EmpowerApplication.set_session("employeeId", String.valueOf(response.body().getData().getEmployee().getEmployeeID()));
                        EmpowerApplication.set_session("serviceUrl", response.body().getServiceUrl());
                        //Log.e("serviceUrl",EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("serviceUrl")));

                        apiService1 = ApiClient.getClient().create(ApiInterface.class);


                        if (Utilities.isNetworkAvailable(LicenseActivity.this)) {
                            // Log.e("getEmployeeCalled","Called");

                            getEmployeeConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")));
                        } else {
                            Toast.makeText(LicenseActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), LicenseActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", LicenseActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", LicenseActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", LicenseActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LicensePOJO> call, Throwable t) {
                // Log error here since request failed
                //Log.e("TAG", t.toString());
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), LicenseActivity.this);

            }
        });
    }

    public String getDeviceID() {
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        EmpowerApplication.set_session("deviceId", android_id);

        return android_id;
    }

    private String getFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        //Log.e( "Firebase reg id: " , regId);

        return regId;
    }

    public void getEmployeeConfig(String employeeid) {

        Map<String, String> configdata = new HashMap<String, String>();
        configdata.put("employeeId", employeeid);
        retrofit2.Call<EmployeeConfig> call = apiService1.getConfigData(configdata);
        call.enqueue(new Callback<EmployeeConfig>() {
            @Override
            public void onResponse(retrofit2.Call<EmployeeConfig> call, Response<EmployeeConfig> response) {


                if (response.isSuccessful()) {
                    // Log.e("getEmployeeCalled","inSuccess");

                    if (response.body().getStatus().equals("1")) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            //  if(response.body().getData().get(i).getValue().equals("0")) {

                            if (response.body().getData().get(i).getKey().equals("AllowLoginUsingOTP")) {
                                // Log.e("getEmployeeCalled",response.body().getData().get(i).getValue());

                                EmpowerApplication.set_session("AllowLoginUsingOTP", response.body().getData().get(i).getValue());
                            }
                        }

                        try {
                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")) {
                                Intent intent = new Intent(LicenseActivity.this, OTPLoginActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            } else {
                                // startActivity(new Intent(LicenseActivity.this, MainActivity.class));
                                Intent intent = new Intent(LicenseActivity.this, MainActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            startActivity(new Intent(LicenseActivity.this, OTPLoginActivity.class));
                            finish();

                        }
                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), LicenseActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", LicenseActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", LicenseActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", LicenseActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<EmployeeConfig> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), LicenseActivity.this);
            }
        });
    }
}
