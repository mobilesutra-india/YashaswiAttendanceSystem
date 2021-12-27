package seedcommando.com.yashaswi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.config.EmployeeConfig;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 7/31/2017.
 */

public class SplashScreen extends AppCompatActivity
{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    ProgressDialog progressDoalog;
    private ApiInterface apiService1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        // FirebaseCrash.report(new Exception("My first Android ` error"));


        setContentView(R.layout.plashscreen);
        progressDoalog = new ProgressDialog(SplashScreen.this);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //progressDoalog.getWindow().setGravity(Gravity.CENTER);
        progressDoalog.show();
        String temp_key = EmpowerApplication.aesAlgorithm.Encrypt("serviceUrl");

        if (EmpowerApplication.sharedPref.contains(temp_key)) {
            apiService1 = ApiClient.getClient().create(ApiInterface.class);
            if (Utilities.isNetworkAvailable(SplashScreen.this)) {
               // Log.e("getEmployeeCalled", "Called");

                getEmployeeConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")));
            } else {
                //Toast.makeText(SplashScreen.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        progressDoalog.dismiss();
                        try {


                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")) {

                     try {
                                    if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("isLogout")).equals("Yes")) {
                                        Intent i = new Intent(SplashScreen.this, OTPLoginActivity.class);
                                        startActivity(i);
                                        // close this activity
                                        finish();
                                    } else {
                                        Intent i = new Intent(SplashScreen.this, ManagerActivity.class);
                                        startActivity(i);
                                        // close this activity
                                        finish();

                                    }

                                } catch (Exception ex) {
                                    Intent i = new Intent(SplashScreen.this, OTPLoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                                // }
                            } else {
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(i);
                                // close this activity
                                finish();
                            }
                        } catch (Exception ex) {
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);
                            // close this activity
                            finish();

                        }

                    }
                }, SPLASH_TIME_OUT);
            }
        } else {

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
//                    progressDoalog.dismiss();
                    try {


                        if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")) {

                   /* if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("isLogout"))==null){
                        Intent i = new Intent(SplashScreen.this, OTPLoginActivity.class);
                        startActivity(i);
                        finish();

                    }else {*/
                            try {
                                if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("isLogout")).equals("Yes")) {
                                    Intent i = new Intent(SplashScreen.this, OTPLoginActivity.class);
                                    startActivity(i);
                                    // close this activity
                                    finish();
                                } else {
                                    Intent i = new Intent(SplashScreen.this, ManagerActivity.class);
                                    startActivity(i);
                                    // close this activity
                                    finish();

                                }

                            } catch (Exception ex) {
                                Intent i = new Intent(SplashScreen.this, OTPLoginActivity.class);
                                startActivity(i);
                                finish();
                            }

                            // }
                        } else {
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);
                            // close this activity
                            finish();
                        }
                    } catch (Exception ex) {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        // close this activity
                        finish();

                    }

                }
            }, SPLASH_TIME_OUT);
        }
    }


    public void getEmployeeConfig(String employeeid) {


        Map<String,String> configdata=new HashMap<String,String>();

        configdata.put("employeeId",employeeid);

        retrofit2.Call<EmployeeConfig> call = apiService1.getConfigData(configdata);
        call.enqueue(new Callback<EmployeeConfig>() {
            @Override
            public void onResponse(retrofit2.Call<EmployeeConfig> call, Response<EmployeeConfig> response) {

               progressDoalog.dismiss();
                if(response.isSuccessful()) {
                    Log.e("getEmployeeCalled","inSuccess");

                    if (response.body().getStatus().equals("1")) {
                        Log.d("login response ",new Gson().toJson(response.body()));

                        Log.e("responseHello ",response.body().toString());
                        for(int i=0;i<response.body().getData().size();i++) {
                            //  if(response.body().getData().get(i).getValue().equals("0")) {

                            if(response.body().getData().get(i).getKey().equals("AllowLoginUsingOTP")) {
                                Log.e("getEmployeeCalled",response.body().getData().get(i).getValue());

                                EmpowerApplication.set_session("AllowLoginUsingOTP",response.body().getData().get(i).getValue());
                            }

                        }

                        try {
                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")) {
                                String temp_key = EmpowerApplication.aesAlgorithm.Encrypt("isLogout");
                                if (EmpowerApplication.sharedPref.contains(temp_key)) {

                                    if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("isLogout")).equals("Yes")) {
                                        Intent i = new Intent(SplashScreen.this, OTPLoginActivity.class);
                                        startActivity(i);
                                        // close this activity
                                        finish();
                                    }else {
                                        Intent i = new Intent(SplashScreen.this, ManagerActivity.class);
                                        startActivity(i);
                                        // close this activity
                                        finish();

                                    }
                                } else {
                                    Intent i = new Intent(SplashScreen.this,OTPLoginActivity.class);
                                    startActivity(i);
                                    // close this activity
                                    finish();

                                }
                                /*startActivity(new Intent(SplashScreen.this, OTPLoginActivity.class));
                                finish();*/
                            } else {
                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                finish();

                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            startActivity(new Intent(SplashScreen.this, OTPLoginActivity.class));
                            finish();

                        }
                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), SplashScreen.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", SplashScreen.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", SplashScreen.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", SplashScreen.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<EmployeeConfig> call, Throwable t) {
                // Log error here since request failed
                progressDoalog.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), SplashScreen.this);
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if( RootUtil.isDeviceRooted()){
            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
        }
    }


    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        //alertDialog.show();
    }
    }

