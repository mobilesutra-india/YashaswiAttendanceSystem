package seedcommando.com.yashaswi.constantclass;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import seedcommando.com.yashaswi.MainActivity;
import seedcommando.com.yashaswi.OTPLoginActivity;
import seedcommando.com.yashaswi.database.EmpowerDatabase;
import seedcommando.com.yashaswi.encryptionanddecryption.AESAlgorithm;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.markhistory.EmployeeMarkHistory;

/**
 * Created by commando1 on 8/2/2017.
 */

public class EmpowerApplication extends Application {
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    static Context context;

    String PREFS_NAME = "empowerfapp4327_vik_soumya";

    public static String SESSION_LATTITUDE = "session_lattitude",
            SESSION_LONGITUDE = "session_longitude";
    public EmpowerDatabase db;
    public static String ForSessionExpire = "Your Session Expired";
    public static String SessionKey = "j5aD9uweHEAncbhd";// Must have 16
    // character session
    // key

    //for encryption
    public static AESAlgorithm aesAlgorithm;


    public void onCreate() {
        super.onCreate();
        db = new EmpowerDatabase(getApplicationContext());
        context = getApplicationContext();
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        aesAlgorithm = new AESAlgorithm();
    }

    public static Context getAppContext() {
        return context;
    }

    public static void set_session(String key, String value) {
        Log.e("", "set_session->" + key + ":" + value);
        String temp_key = aesAlgorithm.Encrypt(key);
        String temp_value = aesAlgorithm.Encrypt(value);
        EmpowerApplication.editor.putString(temp_key, temp_value);
        EmpowerApplication.editor.commit();
    }

    public static String get_session(String key) {
        String temp_key = aesAlgorithm.Encrypt(key);
        String str = "";
        if (sharedPref.contains(temp_key))
            str = sharedPref.getString(temp_key, "");
        Log.e("", "get_session->" + key + ":" + str);
        return str;
    }

    //for multidex----becoz when file size cross the limit
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean check_is_gps_on(Context context) {
        boolean flag = false;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.e("", "IsNetworkEnabled->" + isNetworkEnabled);
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("", "isGPSEnabled->" + isGPSEnabled);
        flag = !(isGPSEnabled && isNetworkEnabled);
        return flag;
    }

    public final boolean isInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

    }

    public String send_Mark_Attendance_to_SQLiteDatabase(String emp_id, String date, String in_time,
                                                         String out_time, String in_time_latitude, String out_time_latitude,
                                                         String in_time_longitude, String out_time_longitude, String in_photo,
                                                         String out_photo, String in_remark, String out_remark,
                                                         String is_sync) {
        String rowid = db.InsertMobileAttendance(emp_id,
                date, in_time, out_time,
                in_time_latitude, in_time_longitude,
                out_time_latitude, out_time_longitude,
                in_photo, out_photo, in_remark, out_remark, is_sync);

        return rowid;
    }


    public String EmployeePersonalDetails(String employee_ID,
                                          String first_Name, String last_Name, String employee_Image, String _Gender, String officalEmail_ID, String designation_Name, String BirthDate, String deptment, String Category, String branch) {
        String rowid = db.InsertPersonalDetails(employee_ID, first_Name, last_Name, employee_Image, _Gender, officalEmail_ID, designation_Name, BirthDate, deptment, Category, branch);

        return rowid;
    }

    public String EmployeeMarkDetails(
            String swaptime, String latitude, String longitude, String door, String locationAddress, String swapimage, String isonline, String remark, String lprovider) {
        String rowid = db.InsertSwapDetails(swaptime, latitude, longitude, door, locationAddress, swapimage, isonline, remark, lprovider);

        return rowid;
    }

    public String PunchHistry(
            String id, String swaptime, String door) {
        String rowid = db.InsertMarkAttendanceHistory(id, swaptime, "", door);

        return rowid;
    }

    public ArrayList<EmployeeMarkHistory> getPunchHistry() {
        ArrayList<EmployeeMarkHistory> rowid = db.GetLastPunches();

        return rowid;
    }

    // GetLastPunches
    public LinkedHashMap<String, String> PersonalDetails(String employee_ID) {

        LinkedHashMap<String, String> rowid = db.getPersonalDetails(employee_ID);

        return rowid;
    }


    public static void dialog(String msg, Context context) {
        final Dialog dialog1 = new Dialog(context);
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

    public static void alertdialog(String msg, final Context context1) {

        final String session;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
        //String str1=msg+";";
        // msg = msg.Replace("\r", " ").Replace("\n", " ");
        msg = msg.replace("\\r", ".").replace("\\n", "");
        // String newstr= msg.replace("[\r\n]", ".");
        // newstr =  newstr.replace("\r", ".").replace("\r", "");
        //msg.replace("\r\n", ".");
        session = msg;
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (session.equals(ForSessionExpire)) {
                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")) {
                                Intent i = new Intent(context1, OTPLoginActivity.class);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                context1.startActivity(i);
                            } else {
                                Intent intent = new Intent(context1, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context1.startActivity(intent);
                            }


                        }
                        Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void dialogForApplication(String msg, String days, Context context) {
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
                dialog1.dismiss();
            }
        });
    }

    public static void dialogForRegApplication(String msg, Context context) {
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
                dialog1.dismiss();
            }
        });
    }

   /* private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/


}
