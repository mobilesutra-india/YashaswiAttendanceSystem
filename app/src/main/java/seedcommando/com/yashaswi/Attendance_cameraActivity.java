package seedcommando.com.yashaswi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization;
import seedcommando.com.yashaswi.compoffapplication.CompoffActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;

import seedcommando.com.yashaswi.database.EmpowerDatabase;
import seedcommando.com.yashaswi.encryptionanddecryption.AESAlgorithm;
import seedcommando.com.yashaswi.geofencing.Constants;
import seedcommando.com.yashaswi.leaveapplication.Leave_Application;
import seedcommando.com.yashaswi.markhistory.EmployeeMarkHistory;
import seedcommando.com.yashaswi.markhistory.ExpandableListAdapter;
import seedcommando.com.yashaswi.markhistory.HeaderData;
import seedcommando.com.yashaswi.pojos.AreaPojo;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.utilitys.InternetBroadcastReceiver;
import seedcommando.com.yashaswi.workfromhomeapplication.WorkFromHomeActivity;

/**
 * Created by commando1 on 7/31/2017.
 */

public class Attendance_cameraActivity extends AppCompatActivity  implements SurfaceHolder.Callback{
    //for camera
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;
    int whichpunch=0,flag = 0, x,a,b;
    //for file
    private File file;
    String img_log = "",imgfile,dtm,photostring,img = "",formattedDate,remark_text;
    //Bitmap bitmap;
    Bitmap scaledBitmap,bitmap;
    ByteArrayOutputStream ByteArray;
    // int flag = 0, x;
    // byte[] ba;
    byte[] ba1, ba;
    public   String  status = "";
    //for GPSo
    String InOut_Flag ="";
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    boolean flag1 = false, isInorOUTPunch = false,isOutAreaPunch=false,dialog_flag=false,remarkflag=false;

    private String delRowId = "";
    EmpowerDatabase db;
    String time,remark1,lat,log,addreee,isonline,door1,provider;
    //for date and time
    TextView current_date,current_time,last_punch_txt;
    //public int a,b;
    java.util.Date noteTS;
    //String  formattedDate;
    //for encryption
    public static AESAlgorithm aesAlgorithm;
    //for dialog show
   // boolean dialog_flag=false;

    //for dialog remark
    Dialog dialogRemark = null;
    //for mark hostory
    ExpandableListView expListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<HeaderData> listGroupTitles1;
    HashMap<String, ArrayList<EmployeeMarkHistory>> listDataMembers;
    public static ArrayList<EmployeeMarkHistory> mlist;
    boolean isInProgress=false;
    private ArrayList<String> latitude;
    private ArrayList<String> longitude;
    private ArrayList<String> nearByDistance;
    private ArrayList<String> officeAreaDistance;



    LinearLayout bottom_layout;
    //String remark_text;
    Button leave_app_btn,attd_reg_btn,out_duty_btn,app_comp_off,app_wfh;
    static ProgressDialog pd,pd1;
    private ApiInterface apiService;
    //boolean remarkflag=false;
    ViewStub stub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        /*toolbar declaration.....*/

        db = new EmpowerDatabase(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);
        stub = findViewById(R.id.layout_stub);
        AddApplicationViews();
        apiService= ApiClient.getClient().create(ApiInterface.class);
       // leave_app_btn=(Button)findViewById(R.id.btn_leave_app);
        //attd_reg_btn=(Button)findViewById(R.id.btn_attd_reg);
       // out_duty_btn=(Button)findViewById(R.id.btn_out_duty);
       // app_comp_off=(Button)findViewById(R.id.btn_comp_off);
       // app_wfh=(Button)findViewById(R.id.btn_wfh);

        getEmployeeAreaConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));//data


        if (EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("AllowAttendanceAsPerLocation"))) {
            int flagForPunch = Integer.parseInt(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")));
           // Toast.makeText(this, ""+flagForPunch, Toast.LENGTH_SHORT).show();

            if (flagForPunch == 1) {
                isOutAreaPunch = false;
            } else {
                isOutAreaPunch = true;
            }

        }



        bottom_layout= findViewById(R.id.bottom_menu);
        last_punch_txt= findViewById(R.id.txt_last_date);
        last_punch_txt.setText(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("LastPunch")));
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        try {

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Call your Alert message

                pd1 = new ProgressDialog(Attendance_cameraActivity.this);
                pd1.setMessage("Loading Location....");
                pd1.setCanceledOnTouchOutside(false);
                pd1.show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        try {
            if(HomeFragment.key1.contains("Remark")){
                if(HomeFragment.value1.get(HomeFragment.key1.indexOf("Remark")).equals("0")){
                    remarkflag=false;

                }
                if(HomeFragment.value1.get(HomeFragment.key1.indexOf("Remark")).equals("1")){
                    remarkflag=true;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        current_date= findViewById(R.id.txt_date);
        initGoogleAPIClient();//Init Google API Client
        //checkPermissions();//Check Permission
        refreshLocationProvider();
           aesAlgorithm=new AESAlgorithm();
        surfaceView = findViewById(R.id.CameraView);
        surfaceHolder = surfaceView.getHolder();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.i("tag", "At on picture taken");
                ByteArray = new ByteArrayOutputStream();

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                x = bitmap.getWidth();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                Matrix matrix = new Matrix();
                if (flag == 1) {
                    matrix.postRotate(0);
                } else {
                    matrix.postRotate(-90);
                }
                Bitmap bitmap1 = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        false);
                matrix.reset();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, ByteArray);
                ba = null;
                ba1=null;
                ba = ByteArray.toByteArray();
                 ba1=ByteArray.toByteArray();
                Log.i("tag", "Bytearray_first" + ByteArray);
                try {
                    ByteArray.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(remarkflag){

                    if(whichpunch==1) {
                        in_punch();
                    }
                    if(whichpunch==2){
                        out_punch();

                    }
                }
                if (insertinfile(ba) == 1) ;
                    refreshCamera();

            }
        };

        //for time and date set to text view
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        //for mark History
        // Get the expandable list
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expListView = findViewById(R.id.lvExp);
        expListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(40));
        setUpExpList();
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
               // expListView.setSelectedGroup(groupPosition);
                bottom_layout.setVisibility(View.GONE);
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                bottom_layout.setVisibility(View.VISIBLE);
            }
        });

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(new InternetBroadcastReceiver(), filter);
        }catch (Exception e){}
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.3f);
    }

    //timer....
    private void updateTextView() {
        noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm:ss a"; // 12:00
        // String date = "dd MMM yy";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
        formattedDate = df.format(c.getTime());

        //current_date.setText(formattedDate);*/
        current_date.setText(formattedDate + "  " + android.text.format.DateFormat.format(time, noteTS));
    }

    //.........
    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
        }
        try {
           // camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();

        } catch (Exception e) {
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.

        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        // You need to choose the most appropriate previewSize for your app
        Camera.Size previewSize =  previewSizes.get(0);// .... select one of previewSizes here
        //previewSizes.get(0);
                parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);
        camera.startPreview();
         refreshCamera();
    }
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
           // camera = Camera.open();
            if (Camera.getNumberOfCameras() > 1) {
                camera = Camera.open(1);
            } else {
                camera = Camera.open(0);
            }
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        //setWillNotDraw(false)
        // modify parameter

        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        // You need to choose the most appropriate previewSize for your app
        Camera.Size previewSize =  previewSizes.get(0);// .... select one of previewSizes here
        //previewSizes.get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setVisible(true);
        return true;
    }
    public void in_picture(View view){
       // camera.takePicture(null, null, jpegCallback);



        if(latitude.size() !=0){
            for (int i=0; i<latitude.size(); i++){
                if (Double.parseDouble(latitude.get(i)) != 0.0 && Double.parseDouble(longitude.get(i)) != 0.0 && Integer.parseInt(nearByDistance.get(i)) != 0 && Integer.parseInt(officeAreaDistance.get(i))!= 0) {
                    Float distance = locationDistance(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i)));
                    Log.d("TAG", "distance: "+distance+" officeAreaDistance"+Integer.parseInt(officeAreaDistance.get(i))+"nearByDistance"+ Integer.parseInt(nearByDistance.get(i)));
                    if (distance < Integer.parseInt(officeAreaDistance.get(i)) || distance < Integer.parseInt(nearByDistance.get(i))) {
                        InPicture();

                        break;
                    } else {
                        if(latitude.size()-1== i) {
                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")).equalsIgnoreCase("0")) {
                                isInorOUTPunch = true;
                                OAMPermission("You are not in office area.");
                            } else {
                                EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);

                            }
                        }
                    }
                } else {
                    InPicture();
                    break;

                }
            }
        }else {
            InPicture();
        }

    }
    void InPicture(){
        dialog_flag=false;
        refreshLocationProvider();
        //for check automatic time setting


        if(AutomaticDateTimeSetting()){
            if(remarkflag) {
                camera.takePicture(null, null, jpegCallback);
                // insertinfile(ba);
                whichpunch=1;


            }else {
                camera.takePicture(null, null, jpegCallback);
                dialogRemark = new Dialog(Attendance_cameraActivity.this);
                dialogRemark.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRemark.setContentView(R.layout.remark_dailogue);
                dialogRemark.show();
                dialogRemark.setCancelable(true);
                dialogRemark.setCanceledOnTouchOutside(false);

                final EditText remark = dialogRemark
                        .findViewById(R.id.remark_txt_reason);

                Button btn_submit = dialogRemark
                        .findViewById(R.id.submit);

                Button btn_cancel = dialogRemark
                        .findViewById(R.id.cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogRemark.dismiss();
                    }
                });

                //dialogue code...

                btn_submit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        InOut_Flag="IN";

                        if (remark.getText().toString().length() <= 500) {
                            remark_text = remark.getText().toString();
                            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            boolean network_enabled = false;

                            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                if (!remark_text.isEmpty()) {
                                    dialogRemark.dismiss();
                                    //camera.takePicture(null, null, jpegCallback);
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                                    String CurrDate1 = dateformat.format(c.getTime());
                                    SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                    String CurrTime1 = timeformat.format(c.getTime());

                                    String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
                                    String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));
                                    // Log.e("lat And Lon",latitude+longitude);
                                    String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
                                    //Log.e("getProvider",provider);

                                    //String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
                                    //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);
                                    // getLocationAddress(Double.parseDouble(aesAlgorithm.Decrypt(latitude)),Double.parseDouble(aesAlgorithm.Decrypt(longitude)));
                                    String imagedata=loadImageFromStorage(img);

                                    if (((EmpowerApplication) getApplication()).isInternetOn()) {
                                        // if(latitude !=null && longitude !=null) {
                                        try {
                                            String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                            //dialog(current_date.getText().toString());
                                            AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), CurrTime1, latitude, longitude, "1", adress, imagedata, "True", remark_text, provider);
                                            EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                                            last_punch_txt.setText(CurrTime1 + " " + "IN");
                                            status ="one";
                                            // ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                                            // setUpExpList();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                         /*}else {
                                            showSnack();
                                         }*/
                                        //(((EmpowerApplication) getApplication()).db).InsertMarkAttendanceHistory(((EmpowerApplication) getApplication()).get_session("empid"), CurrDate1, CurrTime1, "IN");
                                    } else {
                                        // Toast.makeText(getApplicationContext(), "Before Data saved in local", Toast.LENGTH_LONG).show();
                                        String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                                CurrTime1,latitude,longitude,"1","",img,"False",remark_text,provider);
                                        EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"IN");
                                        last_punch_txt.setText(CurrTime1+" "+"IN");
                                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"IN");
                                        Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                                        // Log.e("rowid", rowid);
                                        setUpExpList();
                                        dialog(current_date.getText().toString());

                                    }
                                } else {
                                    EmpowerApplication.dialog("Please Enter Remark", Attendance_cameraActivity.this);
                                }

                            } else {
                                //dialogRemark.dismiss();
                                checkPermissions();
                            }
                        }else {
                            remark.setError("Remark Should be less than 500 charactor");
                        }
                    }
                });

                //................
            }
        }else {
            AutomaticDateTimeSetting();
        }


    }
    public  void in_punch(){

        InOut_Flag="IN";
        String pString=getFilePath();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
        String CurrDate1 = dateformat.format(c.getTime());
        SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yy hh:mm a");
        String CurrTime1 = timeformat.format(c.getTime());
       // Log.e("datetime",CurrTime1);
        //String imagedata=loadImageFromStorage(img);
        // String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
        //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);
        String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
        String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));
        String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
        //Log.e("getProvider",provider);
        String  encoded1=null;
        try {
            // byte[] bytes = ByteArray.toByteArray();
            //Log.e("bytess", String.valueOf(bytes));
            encoded1 = Base64.encodeToString(ba1, Base64.DEFAULT);
           // Log.e("vikas", encoded1);
            ba1=null;
        }catch (Exception e){
            e.printStackTrace();
        }
        if (((EmpowerApplication) getApplication()).isInternetOn()) {
            try{
                if(encoded1!=null) {
                    String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));

                    AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), CurrTime1, latitude, longitude, "1", adress, encoded1, "True", "", provider);
                    EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                    last_punch_txt.setText(CurrTime1 + " " + "IN");
                  //  ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                   // setUpExpList();
               }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Before Data saved in local", Toast.LENGTH_LONG).show();
            String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                    CurrTime1,latitude,longitude,"1","",img,"False","",provider);
            EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"IN");
            last_punch_txt.setText(CurrTime1+" "+"IN");

            ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"IN");
            Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
           // Log.e("rowid", rowid);
            setUpExpList();
            // Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
            dialog(current_date.getText().toString());
        }

    }
    public void out_picture(View view){
        if(latitude.size() !=0){
            for (int i=0; i<latitude.size(); i++){
                if (Double.parseDouble(latitude.get(i)) != 0.0 && Double.parseDouble(longitude.get(i)) != 0.0 && Integer.parseInt(nearByDistance.get(i)) != 0 && Integer.parseInt(officeAreaDistance.get(i))!= 0) {
                    Float distance = locationDistance(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i)));
                    if (distance < Integer.parseInt(officeAreaDistance.get(i)) || distance < Integer.parseInt(nearByDistance.get(i))) {
                        OutPicture();
                        break;
                    } else {
                        if(latitude.size()-1== i) {

                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")).equalsIgnoreCase("0")) {
                                isInorOUTPunch = true;
                                OAMPermission("You are not in office area.");
                            } else {
                                EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);

                            }
                        }
                    }
                } else {
                    OutPicture();
                    break;

                }
            }
        }else {
            OutPicture();
        }
    }
    void OutPicture(){
        dialog_flag=true;
        refreshLocationProvider();
        if(AutomaticDateTimeSetting()) {
            if(remarkflag) {
                camera.takePicture(null, null, jpegCallback);
                whichpunch=2;

            }else {
                camera.takePicture(null, null, jpegCallback);

                dialogRemark = new Dialog(Attendance_cameraActivity.this);
                dialogRemark.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRemark
                        .setContentView(R.layout.remark_dailogue);
                dialogRemark.show();
                dialogRemark.setCancelable(true);
                dialogRemark.setCanceledOnTouchOutside(false);

                final EditText remark = dialogRemark
                        .findViewById(R.id.remark_txt_reason);

                Button btn_submit = dialogRemark
                        .findViewById(R.id.submit);
                Button btn_cancel = dialogRemark
                        .findViewById(R.id.cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogRemark.dismiss();
                    }
                });


                //dialogue code....

                btn_submit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        InOut_Flag="OUT";

                        remark_text = remark.getText().toString();
                        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        boolean network_enabled = false;
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (!remark_text.isEmpty()) {
                                dialogRemark.dismiss();
                                //camera.takePicture(null, null, jpegCallback);
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                                String CurrDate1 = dateformat.format(c.getTime());
                                SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                String CurrTime1 = timeformat.format(c.getTime());

                                //Log.e("timedateout",CurrTime1);
                                String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
                                String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));

                                String imagedata=loadImageFromStorage(img);
                                String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
                                // Log.e("getProvider",provider);
                                if (((EmpowerApplication) getApplication()).isInternetOn()) {

                                    try{
                                        String adress= getLocationAddress(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                        //dialog(current_date.getText().toString());
                                        AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")),CurrTime1,latitude,longitude,"2",adress,imagedata,"True",remark_text,provider);

                                        EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"OUT");
                                        last_punch_txt.setText(CurrTime1+" "+"OUT");
                                    /*((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"OUT");
                                    setUpExpList();*/
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                } else {
                                    String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                            CurrTime1,latitude,longitude,"2","",img,"False",remark_text,provider);
                                    EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"OUT");
                                    last_punch_txt.setText(CurrTime1+" "+"OUT");
                                    ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"OUT");
                                    // Log.e("rowid", rowid);
                                    setUpExpList();
                                    // Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                                    dialog(current_date.getText().toString());
                                }
                            } else {
                                EmpowerApplication.dialog("Please Enter Remark", Attendance_cameraActivity.this);
                            }

                        }else {
                            //dialogRemark.dismiss();
                            checkPermissions();
                        }
                    }
                });
                //.................
            }
        }else {
            AutomaticDateTimeSetting();
        }
    }
    public void out_punch(){
        InOut_Flag="OUT";

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
        String CurrDate1 = dateformat.format(c.getTime());
        SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String CurrTime1 = timeformat.format(c.getTime());
        //Log.e("datetime",formattedDate+""+timeformat);

        //String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
        //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);

        String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
        String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));
        String imagedata=loadImageFromStorage(img);
        String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
        //Log.e("getProvider",provider);
        //Log.e("lat&lon",lat+""+lon);
        if (((EmpowerApplication) getApplication()).isInternetOn()) {
            try{
                String adress= getLocationAddress(Double.parseDouble(latitude),Double.parseDouble(longitude));
                AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")),CurrTime1,latitude,longitude,"2",adress,imagedata,"True","",provider);
                EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"OUT");
                last_punch_txt.setText(CurrTime1+" "+"OUT");
              /*  ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"OUT");
                setUpExpList();
*/            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                    CurrTime1,latitude,longitude,"2","",img,"False","",provider);
            EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"OUT");
            last_punch_txt.setText(CurrTime1+" "+"OUT");
            ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"OUT");



           // Log.e("rowid", rowid);
            setUpExpList();
            //Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
            dialog(current_date.getText().toString());
        }

    }
    public long insertinfile(byte[] ba2) {
        boolean image_flag = false;
        imgfile = getFilePath();
       // Log.i("tag", "imgfile" + imgfile);
        img_log = "\nimgfile" + imgfile;
        file = new File(imgfile);
        try {
            image_flag = file.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            img_log = img_log + "\n exception is" + e1.getMessage();
        }
        //Log.i("tag", "At in insertFile");
       // Log.i("tag", "At in insertFile" + file);
        //Log.e("","file exists is:" + file.exists());
        img_log = img_log + "\n image_flag" + image_flag;
        img_log = img_log + "\n exists" + file.exists();
        long l = ba2.length;
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(ba2);

            // bytes.close();
            l = file.length();
           // loadImageFromStorage(img);
            ba = null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
           // System.out.println("Exception is:" + e.getMessage());
            //Log.e("","image_captured exception is:" + e.getMessage());
            img_log = img_log + "\n" + e.getMessage();
        }
        // TODO Auto-generated method stub
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Log.e("","image_captured exception is:" + e.getMessage());
            img_log = img_log + "\n" + e.getMessage();
        }
        // db.add(pinentered,Globals.schoolid,photoid,df1.format(c.getTime()));
        //Toast.makeText(Attendance_cameraActivity.this, "photo_captured", Toast.LENGTH_SHORT).show();
       // Log.i("tag", "At end of insertFile");
       // Log.e("","file exists is:" + file.exists());
        MediaScannerConnection.scanFile(Attendance_cameraActivity.this,
                new String[] { imgfile }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // TODO Auto-generated method stub
                      //  Log.i("MyFileStorage", "Scanned " + path);
                    }
                });
        return 1;
    }
    public String getFilePath() {
        File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(filepath, "Empower");
        if (!file.exists())
            file.mkdirs();
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                Locale.ENGLISH);
        dtm = f1.format(new Date());
       // Log.i("imagename", "file-->" + file.getAbsolutePath() + "/" + dtm + ".jpg");
        img = (dtm + ".jpg");
        photostring = file.getAbsolutePath() + "/" + img;

       // Log.i("TAG", "photostring" + photostring);
        return photostring;
    }


    private String loadImageFromStorage(String img)
    {

        String encoded=null;

        try {
            String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

            File dirfile = new File(filepath, "Empower");
            dirfile.mkdirs();

            File f=new File(dirfile, img);
            //Log.e("image file",f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
           // Log.e("file Stram", String.valueOf(new FileInputStream(f)));
            FileInputStream fileInputStream=new FileInputStream(f);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                  //  System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
                ex.fillInStackTrace();
                //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] bytes = bos.toByteArray();
            //Log.e("bytess", String.valueOf(bytes));
            encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
            //Log.e("encoded bytes", encoded);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
        return encoded;

    }
    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient= new GoogleApiClient.Builder(Attendance_cameraActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(Attendance_cameraActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }
    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Attendance_cameraActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(Attendance_cameraActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
        else {
            ActivityCompat.requestPermissions(Attendance_cameraActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }
    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(3* 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Attendance_cameraActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                       // Log.e("Settings", "Result OK");
                        //updateGPSStatus("GPS is Enabled in your device");
                        if(pd1!=null){
                            pd1.dismiss();
                        }
                        pd1 = new ProgressDialog(Attendance_cameraActivity.this);
                        pd1.setMessage("Loading Location....");
                        pd1.setCanceledOnTouchOutside(false);
                        pd1.show();
                        flag1=true;


                        refreshLocationProvider();
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                       // Log.e("Settings", "Result Cancel");
                        //updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(new InternetBroadcastReceiver(), filter);
        }catch (Exception e){}
//Register broadcast receiver to check the status of GPS
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);

        try {
            if (new InternetBroadcastReceiver() != null)
                unregisterReceiver(new InternetBroadcastReceiver());
        }catch (Exception e){}
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (new InternetBroadcastReceiver() != null)
                unregisterReceiver(new InternetBroadcastReceiver());
        }catch (Exception e){}
    }
    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //Check if GPS is turned ON or OFF
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
               // Log.e("About GPS", "GPS is Enabled in your device");
                showSettingDialog();
            }
        }
    };
    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                  //  Log.e("About GPS", "GPS is Enabled in your device");
                   // updateGPSStatus("GPS is Enabled in your device");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                   // updateGPSStatus("GPS is Disabled in your device");
                   // Log.e("About GPS", "GPS is Disabled in your device");
                }
            }
        }
    };
    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showSettingDialog();
                    } else
                        showSettingDialog();


                } else {
                   // updateGPSStatus("Location Permission denied.");
                    Toast.makeText(Attendance_cameraActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
               // return;
            }
        }
    }
    //this is for refresh location
    private void refreshLocationProvider() {



        // TODO Auto-generated method stub
        if (EmpowerApplication.check_is_gps_on(getApplicationContext())) {
            requestLocationPermission();


        } else {
            startService(new Intent(Attendance_cameraActivity.this, MyService.class));
        }
}
    public boolean AutomaticDateTimeSetting(){
        boolean auto =false;
        if (Build.VERSION.SDK_INT > 16) {
            try {
                 a=    Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
                String aa=  TimeZone.getDefault().getDisplayName();
                 b= Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE);

                if(a==1 && b==1)
                {
                   // Log.d("b"," "+b);
                   // Toast.makeText(getApplicationContext(),"automatic setting"+a,Toast.LENGTH_LONG).show();
                    Calendar c = Calendar.getInstance();
                    Date d=c.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String time1 = sdf.format(d);
                    auto=true;

                }
                else if(a==0||b==0)
                {
                   // Toast.makeText(this,"Go to automatic setting "+a,Toast.LENGTH_LONG).show();
                    startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 1);

                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }


    return auto;
    }
    public  void dialog(String msg) {
        final Dialog dialog1 = new Dialog(Attendance_cameraActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
       if(dialog_flag){
            dialog1.setContentView(R.layout.dialog_markattendance_out);
        }else {
            dialog1.setContentView(R.layout.dialog_markattendance);
       }
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = dialog1.findViewById(R.id.textView_dialog_mark_attendance);
        title.setText(msg);
        Button btn_ok = dialog1.findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
    }

    //for Mark hostory

    private void setUpExpList() {
        listGroupTitles1= new ArrayList<HeaderData>();
        listDataMembers= new HashMap<String, ArrayList<EmployeeMarkHistory>>();
        // Adding province names and number of population as groups
        HeaderData headerData=new HeaderData();
        headerData.setDate("Punch history");
        headerData.setPunch("");

        listGroupTitles1.add(headerData);
        ArrayList<EmployeeMarkHistory> listData =((EmpowerApplication) getApplication()).getPunchHistry();
        listDataMembers.put(listGroupTitles1.get(0).getDate(), listData);
        /*EmployeeMarkHistory employeeMarkHistoryHeader =new EmployeeMarkHistory();

        String header_datetxt= "<b> <font color='#1c2039'>Date</font> </b> ";

        String header_punchstatustxt="<b><font color='#1c2039'>Punch IN/OUT </font></b>";*/
        expandableListAdapter= new ExpandableListAdapter(getApplicationContext(), listGroupTitles1, listDataMembers ) ;
        //ExpandableListAdapter(this, listGroupTitles1, listDataMembers);
        // Setting list adapter
        expListView.setAdapter(expandableListAdapter);
    }
    //punch hoistory..........

     //Back press................
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            //noinspection SimplifiableIfStatement
           
        }

        if (id == R.id.action_sync) {

            if (((EmpowerApplication) getApplication()).isInternetOn()) {
                Intent a = new Intent(this,Attendance_cameraActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);

/*

                List<OffLineAttendancePoJo> empList = db.getAttendenceList();
                if (!empList.isEmpty()) {
                    addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                    lat=empList.get(0).getLatitude();
                    log=empList.get(0).getLongitude();
                    remark1=empList.get(0).getRemark();
                    isonline=empList.get(0).getIsOnlineSwipe();
                    time=empList.get(0).getSwipeTime();
                    door1=empList.get(0).getDoor();
                    provider=empList.get(0).getLocationProvider();
                    delRowId = empList.get(0).getId_mark();
                    String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                    // addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                    //delRowId = empList.get(0).getId_mark();
                 //   if(empList.get(0).getSwipeImageFileName().isEmpty()){
                        if(Utilities.isNetworkAvailable(getApplicationContext())) {
                            if(!isInProgress)
                                AddMobileSwipesDataRefresh(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, "", isonline, remark1,provider);

                        }else {
                            Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                        }
                  */
/*  }else {
                        // loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                    }
*//*


                }else{
                    Toast.makeText(this, "Empty data", Toast.LENGTH_SHORT).show();
                }



*/
            }else{
              //  Toast.makeText(this, "No Internet Connection...", Toast.LENGTH_SHORT).show();
                EmpowerApplication.alertdialog("No Internet Connection...", Attendance_cameraActivity.this);

            }
        }

        return super.onOptionsItemSelected(item);
    }

    //....................to call AddMobileSwipes service..........

    public void AddMobileSwipesData(String employeeId, String deviceId, final String swipeTime, String latitude, String longitude, String door, String locationAddress, String swipeImageFileName, String isOnlineSwipe, String remark, String lprovider) {

        pd = new ProgressDialog(Attendance_cameraActivity.this);
        pd.setMessage("Loading....");
      //  pd.setCanceledOnTouchOutside(false);

        pd.show();

        Map<String,String> MarkAttendancedata=new HashMap<String,String>();
        MarkAttendancedata.put("employeeId",employeeId);
        MarkAttendancedata.put("deviceId",deviceId);
        MarkAttendancedata.put("swipeTime",swipeTime);
        MarkAttendancedata.put("latitude",latitude);
        MarkAttendancedata.put("longitude",longitude);
        MarkAttendancedata.put("door",door);
        MarkAttendancedata.put("locationAddress",locationAddress);
        MarkAttendancedata.put("swipeImageFileName",swipeImageFileName);
        //Log.e("imageData",swipeImageFileName);
        MarkAttendancedata.put("isOnlineSwipe",isOnlineSwipe);
        //Log.e("isOnlineSwipe",isOnlineSwipe);
        MarkAttendancedata.put("remark",remark);
        MarkAttendancedata.put("locationProvider",lprovider);
        Log.d("", "MarkAttendancedata: "+MarkAttendancedata);

        retrofit2.Call<CommanResponsePojo> call = apiService.AddMobileSwipesData(MarkAttendancedata);

        call.enqueue(new Callback<CommanResponsePojo>() {

            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();
                // response.code();


                if (response.isSuccessful()) {

                    pd.dismiss();

                    Log.d("Attendance_response ",new Gson().toJson(response.body()));

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    String CurrTime1 = timeformat.format(c.getTime());


                    if(InOut_Flag.equalsIgnoreCase("OUT")){
                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), swipeTime, "OUT");
                        setUpExpList();

                    }else{
                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), swipeTime, "IN");
                        setUpExpList();

                    }







                    Log.d("User response Data: ", ""+response.body().getData());
                    Log.d("User response Message: ", ""+response.body().getMessage());
                    if (response.body().getStatus().equals("1")) {
                        dialog(current_date.getText().toString());
                        // Change flag of door or isOnline as Its record is sent to server.


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Attendance_cameraActivity.this);

                    }

                }else {


                    switch (response.code()) {
                        case 404:
                            pd.dismiss();
                          //  Toast.makeText(Attendance_cameraActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            pd.dismiss();
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(Attendance_cameraActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            pd.dismiss();

                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(Attendance_cameraActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed\
                EmpowerApplication.alertdialog("This Wi-Fi/Internet Network has no access to the internet", Attendance_cameraActivity.this);

                pd.dismiss();

/*

                String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                        CurrTime1,latitude,longitude,"1","",img,"False",remark_text,provider);
                EmpowerApplication.set_session("LastPunch", CurrTime1+" "+"IN");
                last_punch_txt.setText(CurrTime1+" "+"IN");
                ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),CurrTime1,"IN");
                Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                // Log.e("rowid", rowid);
                setUpExpList();
                dialog(current_date.getText().toString());


*/



                Log.e("TAG", t.toString());

            }
        });
    }

    //for to get the location address from latitude and langitude

    public String getLocationAddress(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return address;
    }

    public void AddApplicationViews(){

        if(HomeFragment.value.size()==1){
            stub.setLayoutResource(R.layout.dynamic_application_layout_one);
            View inflated = stub.inflate();
            //TextView textView = inflated.findViewById(R.id.te_xt);
            if(HomeFragment.key.get(0).equals("HideLeaveApp")) {
                TextView textView = inflated.findViewById(R.id.te_xt);
                textView.setText(R.string.LeaveApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideCompOffApp")) {
                TextView textView1 = inflated.findViewById(R.id.te_xt);
                textView1.setText(R.string.CompOffApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideRegularizationApp")) {
                TextView textView2 = inflated.findViewById(R.id.te_xt);
                textView2.setText(R.string.RegApp);
                textView2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideWFHApp")) {
                TextView textView3 = inflated.findViewById(R.id.te_xt);
                textView3.setText(R.string.WFHApp);
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideOutDutyApp")) {
                TextView textView4 = inflated.findViewById(R.id.te_xt);
                textView4.setText(R.string.ODApp);
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }




        }
        if(HomeFragment.value.size()==2){

            stub.setLayoutResource(R.layout.dynamic_application_layout_two);
            View inflated = stub.inflate();
            TextView textView=inflated.findViewById(R.id.te_xt);
            //textView.setText("Leave Application");
            if(HomeFragment.key.get(0).equals("HideLeaveApp")) {
                textView.setText(R.string.LeaveApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideCompOffApp")) {
                textView.setText(R.string.CompOffApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideRegularizationApp")) {
                textView.setText(R.string.RegApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideWFHApp")) {
                textView.setText(R.string.WFHApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideOutDutyApp")) {
                textView.setText(R.string.ODApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }
            TextView textView1=inflated.findViewById(R.id.te_xt1);
            //textView1.setText("Regularization Application");
            if(HomeFragment.key.get(1).equals("HideLeaveApp")) {
                textView1.setText(R.string.LeaveApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideCompOffApp")) {
                textView1.setText(R.string.CompOffApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideRegularizationApp")) {
                textView1.setText(R.string.RegApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideWFHApp")) {
                textView1.setText(R.string.WFHApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideOutDutyApp")) {
                textView1.setText(R.string.ODApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }


        }
        if(HomeFragment.value.size()==3){

            stub.setLayoutResource(R.layout.daynamic_application_three);
            View inflated = stub.inflate();
            TextView textView=inflated.findViewById(R.id.te_xt);
                           /* textView.setText("Leave Application");
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            textView1.setText("Regularization Application");*/
            if(HomeFragment.key.get(0).equals("HideLeaveApp")) {
                textView.setText(R.string.LeaveApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideCompOffApp")) {
                textView.setText(R.string.CompOffApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideRegularizationApp")) {
                textView.setText(R.string.RegApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideWFHApp")) {
                textView.setText(R.string.WFHApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideOutDutyApp")) {
                textView.setText(R.string.ODApp);
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }
            TextView textView1=inflated.findViewById(R.id.te_xt1);
            //textView1.setText("Regularization Application");
            if(HomeFragment.key.get(1).equals("HideLeaveApp")) {
                textView1.setText(R.string.LeaveApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideCompOffApp")) {
                textView1.setText(R.string.CompOffApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideRegularizationApp")) {
                textView1.setText(R.string.RegApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideWFHApp")) {
                textView1.setText(R.string.WFHApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideOutDutyApp")) {
                textView1.setText(R.string.ODApp);
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }
            TextView textView3=inflated.findViewById(R.id.te_xt2);
            //textView3.setText("OutDuty Application");
            if(HomeFragment.key.get(2).equals("HideLeaveApp")) {
                textView3.setText(R.string.LeaveApp);
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideCompOffApp")) {
                textView3.setText(R.string.CompOffApp);
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideRegularizationApp")) {
                textView3.setText(R.string.RegApp);
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideWFHApp")) {
                textView3.setText(R.string.WFHApp);
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideOutDutyApp")) {
                textView3.setText(R.string.ODApp);
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }


        }
        if(HomeFragment.value.size()==4){

            stub.setLayoutResource(R.layout.dynamic_application_layout);
            View inflated = stub.inflate();
            TextView textView=inflated.findViewById(R.id.te_xt);
            if(HomeFragment.key.get(0).equals("HideLeaveApp")) {
                textView.setText("Leave Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideCompOffApp")) {
                textView.setText("CompOff Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideRegularizationApp")) {
                textView.setText("Regularization Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideWFHApp")) {
                textView.setText("WFH Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideOutDutyApp")) {
                textView.setText("OutDuty Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }
            TextView textView1=inflated.findViewById(R.id.te_xt1);
            //textView1.setText("Regularization Application");
            if(HomeFragment.key.get(1).equals("HideLeaveApp")) {
                textView1.setText("Leave Application");
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideCompOffApp")) {
                textView1.setText("CompOff Application");
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideRegularizationApp")) {
                textView1.setText("Regularization Application");
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideWFHApp")) {
                textView1.setText("WFH Application");
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(1).equals("HideOutDutyApp")) {
                textView1.setText("OutDuty Application");
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }
            TextView textView3=inflated.findViewById(R.id.te_xt2);
            //textView3.setText("OutDuty Application");
            if(HomeFragment.key.get(2).equals("HideLeaveApp")) {
                textView3.setText("Leave Application");
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideCompOffApp")) {
                textView3.setText("CompOff Application");
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideRegularizationApp")) {
                textView3.setText("Regularization Application");
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideWFHApp")) {
                textView3.setText("WFH Application");
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(2).equals("HideOutDutyApp")) {
                textView3.setText("OutDuty Application");
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }

            TextView textView4=inflated.findViewById(R.id.te_xt3);
            //textView4.setText("CompOff Application");
            if(HomeFragment.key.get(3).equals("HideLeaveApp")) {
                textView4.setText("Leave Application");
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(3).equals("HideCompOffApp")) {
                textView4.setText("CompOff Application");
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(3).equals("HideRegularizationApp")) {
                textView4.setText("Regularization Application");
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(3).equals("HideWFHApp")) {
                textView4.setText("WFH Application");
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(3).equals("HideOutDutyApp")) {
                textView4.setText("OutDuty Application");
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }


        }

        if(HomeFragment.value.size()==5) {

            stub.setLayoutResource(R.layout.applicationfooter_wfh);
            View inflated = stub.inflate();
            leave_app_btn= inflated.findViewById(R.id.btn_leave_app);
            attd_reg_btn= inflated.findViewById(R.id.btn_attd_reg);
            out_duty_btn= inflated.findViewById(R.id.btn_out_duty);
            app_comp_off= inflated.findViewById(R.id.btn_comp_off);
            app_wfh= inflated.findViewById(R.id.btn_wfh);


            leave_app_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(Attendance_cameraActivity.this, Leave_Application.class);
                    startActivity(i);
                }
            });

            attd_reg_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(Attendance_cameraActivity.this, Attend_Regularization.class);
                    startActivity(i);
                }
            });


            out_duty_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(Attendance_cameraActivity.this, Out_Duty_Application.class);
                    startActivity(i);
                }

            });

            app_comp_off.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(Attendance_cameraActivity.this, CompoffActivity.class);
                    startActivity(i);
                }

            });

            app_wfh.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(Attendance_cameraActivity.this, WorkFromHomeActivity.class);
                    startActivity(i);
                }

            });




        }
    }

    // Showing the status in Snackbar
    private void showSnack() {
        String message;
        int color;
       /* if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {*/
            message = "Sorry! Location Coordinates are Null";
            color = Color.RED;
       // }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.cameraSnackbar), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public void OAMPermission(String msg) {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Attendance_cameraActivity.this);

        // Set a title for alert dialog
        builder.setTitle("You are not in office area.");

        // Ask the final question
        builder.setMessage("Are you sure to mark Attendance outside office area?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                if (isInorOUTPunch) {
                    isInorOUTPunch = false;
                    InPicture();

                } else {
                    //outPunch();

                }
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
               // Toast.makeText(getApplicationContext(),
                   //     "No Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }

    public float locationDistance(double lat, double lon){

        LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_STAN_UNI);
        Location loc1 = new Location("");

        loc1.setLatitude(lat);
        loc1.setLongitude(lon);

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE))));
        loc2.setLongitude(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE))));

        float distanceInMeters = loc1.distanceTo(loc2);
       // Log.e("distance",String.valueOf(distanceInMeters));

        return distanceInMeters;
    }


/*

    public void AddMobileSwipesDataRefresh(String employeeId,String deviceId,String swipeTime,String latitude,String longitude,String door,String locationAddress,
                                    String swipeImageFileName,String isOnlineSwipe,String remark,String lprovider) {
        Log.d("InternetService AddMobileSwipesData()- ", "");
        Map<String,String> MarkAttendancedata=new HashMap<String,String>();
        MarkAttendancedata.put("employeeId",employeeId);
        MarkAttendancedata.put("deviceId",deviceId);
        MarkAttendancedata.put("swipeTime",swipeTime);
        MarkAttendancedata.put("latitude",latitude);
        MarkAttendancedata.put("longitude",longitude);
        MarkAttendancedata.put("door",door);
        MarkAttendancedata.put("locationAddress",locationAddress);
        MarkAttendancedata.put("swipeImageFileName",swipeImageFileName);
        MarkAttendancedata.put("isOnlineSwipe",isOnlineSwipe);
        MarkAttendancedata.put("remark",remark);
        MarkAttendancedata.put("locationProvider",lprovider);
        isInProgress=true;


        retrofit2.Call<CommanResponsePojo> call = apiService.AddMobileSwipesData(MarkAttendancedata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                Log.d("InternetService AddMobileSwipesData()- ", "onResponse()");
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                        Log.e("service ","1");
                        setUpExpList();

                        Toast.makeText(Attendance_cameraActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        db.deleteMarkDataRecord(delRowId);
                        isInProgress=false;
                        List<OffLineAttendancePoJo> empList = db.getAttendenceList();
                        if(!empList.isEmpty()) {
                            lat = empList.get(0).getLatitude();
                            log = empList.get(0).getLongitude();
                            remark1 = empList.get(0).getRemark();
                            isonline = empList.get(0).getIsOnlineSwipe();
                            time = empList.get(0).getSwipeTime();
                            door1 = empList.get(0).getDoor();
                            delRowId = empList.get(0).getId_mark();
                            addreee = getLocationAddress(Double.parseDouble(lat), Double.parseDouble(log));
                            provider=empList.get(0).getLocationProvider();

                            // addreee = getLocationAddress(Double.parseDouble(lat), Double.parseDouble(log));
                           // if(empList.get(0).getSwipeImageFileName().isEmpty()){
                                if(Utilities.isNetworkAvailable(getApplicationContext())) {
                                    if(!isInProgress)


                                        AddMobileSwipesDataRefresh(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, "", isonline, remark1,provider);
                                }else {
                                    Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                                }
                         //   }else {
                             //   String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                           // }
                        }
                        isInProgress=false;
                        //dialog(current_date.getText().toString());


                    } else {
                      //  db.deleteMarkDataRecord(delRowId);
                        isInProgress=false;
                        List<OffLineAttendancePoJo> empList = db.getAttendenceList();
                        if(!empList.isEmpty()) {
                            //addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                            lat = empList.get(0).getLatitude();
                            log = empList.get(0).getLongitude();
                            remark1 = empList.get(0).getRemark();
                            isonline = empList.get(0).getIsOnlineSwipe();
                            time = empList.get(0).getSwipeTime();
                            door1 = empList.get(0).getDoor();
                            delRowId = empList.get(0).getId_mark();
                            addreee = getLocationAddress(Double.parseDouble(lat), Double.parseDouble(log));

                          //  String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                        }
                        // EmpowerApplication.alertdialog(response.body().getMessage(), InternetService.this);

                    }

                }else {
                    isInProgress=false;
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                isInProgress=false;
                Log.e("TAG", t.toString());

            }
        });
    }


*/


    public void getEmployeeAreaConfig(String employeeid,String token) {


/*
        pd = new ProgressDialog(Attendance_cameraActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
*/



        Map<String,String> configdata=new HashMap<String,String>();
        configdata.put("employeeId",employeeid);
        configdata.put("token",token);
        Log.d("", "getEmployeeAreaConfig_ID: "+token);
        latitude=new ArrayList<>();
        longitude=new ArrayList<>();
        nearByDistance=new ArrayList<>();
        officeAreaDistance=new ArrayList<>();
        Call<AreaPojo> call = apiService.getEmployeeAreaConfig(configdata);
        call.enqueue(new Callback<AreaPojo>() {
            @Override
            public void onResponse(retrofit2.Call<AreaPojo> call, Response<AreaPojo> response) {
                latitude.clear();
                longitude.clear();
                nearByDistance.clear();
               // pd.dismiss();

                officeAreaDistance.clear();
                if(response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                 //       pd.dismiss();


                      //  Toast.makeText(Attendance_cameraActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        for(int i=0;i<response.body().getData().size();i++) {
                            if(response.body().getData().get(i).getLatitude()!=null || !response.body().getData().get(i).getLatitude().equals("")){
                                latitude.add(response.body().getData().get(i).getLatitude());
                            }
                            if(response.body().getData().get(i).getLongitude()!=null  || !response.body().getData().get(i).getLongitude().equals("")){
                                longitude.add(response.body().getData().get(i).getLongitude());
                            }
                            if(response.body().getData().get(i).getDistanceNearBy()!=null  || !response.body().getData().get(i).getDistanceNearBy().equals("")){
                                nearByDistance.add(response.body().getData().get(i).getDistanceNearBy());
                            }
                            if(response.body().getData().get(i).getDistanceOfficeArea()!=null || !response.body().getData().get(i).getDistanceOfficeArea().equals("")){
                                officeAreaDistance.add(response.body().getData().get(i).getDistanceOfficeArea());
                            }


                        }

                    } else {
                        //   EmpowerApplication.alertdialog(response.body().getMessage(), Attendance_cameraActivity.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<AreaPojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
             //   pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), Attendance_cameraActivity.this);



            }
        });
    }



}

