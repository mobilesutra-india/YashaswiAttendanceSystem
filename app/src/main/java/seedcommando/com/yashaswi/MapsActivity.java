package seedcommando.com.yashaswi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
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
import seedcommando.com.yashaswi.encryptionanddecryption.AESAlgorithm;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    //for camera
    Camera camera;

    Camera.PictureCallback jpegCallback;
    //for file
    private File file;
    String img_log = "", imgfile, dtm, photostring, img = "";

    //for GPS
    String lat1, long1, Provider1;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    //private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    //private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    boolean flag1 = false;
    ViewStub stub;

    //for date and time
    TextView current_date, current_time, last_punch_txt;
    public int a, b;
    java.util.Date noteTS;

    //for encryption
    public static AESAlgorithm aesAlgorithm;
    //for dialog show
    boolean dialog_flag = false;

    //for dialog remark
    Dialog dialogRemark = null;
    //for mark hostory
    ExpandableListView expListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<HeaderData> listGroupTitles1;
    HashMap<String, ArrayList<EmployeeMarkHistory>> listDataMembers;
    public static ArrayList<EmployeeMarkHistory> mlist;
    //for MAP
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleMap mMap;

    String formattedDate;


    String remark_text;
    //latitide and longitude..
    double longitude;
    double latitude;
    String Provide;
    Location locations;
    LocationManager locationManager;
    Button In_button;
    LinearLayout bottom_layout;
    Button leave_app_btn, attd_reg_btn, out_duty_btn, app_comp_off, app_wfh;
    ProgressDialog pd, pd1, pd2;
    private ApiInterface apiService;
    boolean flag = false, isInorOUTPunch = false, isOutAreaPunch = false,  gpsfirststate = true, internetfirststate = true;

    boolean remarkflag = false;
    private ArrayList<String> latitude1;
    private ArrayList<String> longitude1;
    private ArrayList<String> nearByDistance;
    private ArrayList<String> officeAreaDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(new InternetBroadcastReceiver(), filter);
        } catch (Exception e) {
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);
        stub = findViewById(R.id.layout_stub);

        In_button = findViewById(R.id.button_in);
        last_punch_txt = findViewById(R.id.txt_last_date);
        bottom_layout = findViewById(R.id.bottom);
        //leave_app_btn=(Button)findViewById(R.id.btn_leave_app);
        // attd_reg_btn=(Button)findViewById(R.id.btn_attd_reg);
        // out_duty_btn=(Button)findViewById(R.id.btn_out_duty);
        // app_comp_off=(Button)findViewById(R.id.btn_comp_off);
        // app_wfh=(Button)findViewById(R.id.btn_wfh);
        last_punch_txt.setText(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("LastPunch")));
        current_date = findViewById(R.id.txt_date);
        AddApplicationViews();

        apiService = ApiClient.getClient().create(ApiInterface.class);


        getEmployeeAreaConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));//data



        if (EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("AllowAttendanceAsPerLocation"))) {
            int flagForPunch = Integer.parseInt(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")));

            if (flagForPunch == 1) {
                isOutAreaPunch = false;
            } else {
                isOutAreaPunch = true;
            }

        }


        try {
            if (HomeFragment.key1.contains("Remark")) {
                if (HomeFragment.value1.get(HomeFragment.key1.indexOf("Remark")).equals("0")) {
                    remarkflag = false;

                }
                if (HomeFragment.value1.get(HomeFragment.key1.indexOf("Remark")).equals("1")) {
                    remarkflag = true;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Call your Alert message

                // pd1 = new ProgressDialog(MapsActivity.this);
                // pd1.setMessage("Loading Location....");
                // pd1.setCanceledOnTouchOutside(false);
                //   pd1.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       /* if (ContextCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            pd1 = new ProgressDialog(MapsActivity.this);
            pd1.setMessage("Loading Location....");
            pd1.setCanceledOnTouchOutside(false);
            pd1.show();

        }*/

        //initGoogleAPIClient();//Init Google API Client
        //checkPermissions();//Check Permission
        aesAlgorithm = new AESAlgorithm();

        //timer ....
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                //current_date.setText(formattedDate);
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        expListView = findViewById(R.id.lvExp);
        expListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(40));


        //for mark History...........

        // Get the expandable list
        //  expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // Setting up list
        setUpExpList();
        expandableListAdapter = new ExpandableListAdapter(getApplicationContext(), listGroupTitles1, listDataMembers);

        //ExpandableListAdapter(this, listGroupTitles1, listDataMembers);
        // Setting list adapter
        expListView.setAdapter(expandableListAdapter);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                bottom_layout.setVisibility(View.GONE);
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                bottom_layout.setVisibility(View.VISIBLE);
            }
        });

        //for map
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //for expandable arrow....
    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.3f);
    }

    //..............

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
                Intent a = new Intent(this,MapsActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);


            }else{
                //  Toast.makeText(this, "No Internet Connection...", Toast.LENGTH_SHORT).show();
                EmpowerApplication.alertdialog("No Internet Connection...", MapsActivity.this);

            }
        }

        return super.onOptionsItemSelected(item);
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
        current_date.setText(formattedDate + " " + android.text.format.DateFormat.format(time, noteTS));
    }

    //.........
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setPadding(0, 0, 0, 700);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        final String provider = locationManager.getBestProvider(new Criteria(), true);
       /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }*/
        locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations) {
            longitude = locations.getLongitude();
            latitude = locations.getLatitude();
            //Provide = locations.getProvider();
            onLocationChanged(locations);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (pd1 != null) {
            pd1.dismiss();
        }
        if (pd2 != null) {
            pd2.dismiss();
        }
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        lat1 = String.valueOf(location.getLatitude());
        long1 = String.valueOf(location.getLongitude());
        Provider1 = location.getProvider();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        /* LocationManager*/
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
// TODO: Consider calling

            return;
        }
        /* Location*/
        locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            longitude = locations.getLongitude();
            latitude = locations.getLatitude();
            //  Toast.makeText(this, "latitude" + latitude, Toast.LENGTH_LONG).show();
            // Log.d("long " + longitude, "lat " + latitude);
            // Toast.makeText(this, "longitude" + longitude, Toast.LENGTH_LONG).show();

            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
// Here we are finding , whatever we want our marker to show when clicked
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
//move map camera
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);

            mMap.animateCamera(cameraUpdate);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//this code stops location updates
           /* if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
// Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
// Show an explanation to the user *asynchronously* -- don't block
// this thread waiting for the user's response! After the user
// sees the explanation, try again to request the permission.
//Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void in_picture(View view) {

        if(latitude1.size() !=0){
            for (int i=0; i<latitude1.size(); i++){
                if (Double.parseDouble(latitude1.get(i)) != 0.0 && Double.parseDouble(longitude1.get(i)) != 0.0 && Integer.parseInt(nearByDistance.get(i)) != 0 && Integer.parseInt(officeAreaDistance.get(i))!= 0) {
                    Float distance = locationDistance(Double.parseDouble(latitude1.get(i)),Double.parseDouble(longitude1.get(i)));
                    if (distance < Integer.parseInt(officeAreaDistance.get(i)) || distance < Integer.parseInt(nearByDistance.get(i))) {
                        inPunch();
                        break;
                    } else {
                        if(latitude1.size()-1== i) {

                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")).equalsIgnoreCase("0")) {
                                isInorOUTPunch = true;
                                OAMPermission("You are not in office area.");
                            } else {
                                EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);

                            }
                        }
                    }
                } else {
                    inPunch();
                    break;

                }
            }
        }else {
            inPunch();
        }

    }

 void inPunch(){
     locationManager = (LocationManager)
             getSystemService(Context.LOCATION_SERVICE);
     final String provider = locationManager.getBestProvider(new Criteria(), true);
     if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         // TODO: Consider calling
         requestLocationPermission();

         return;
     }
     locations = locationManager.getLastKnownLocation(provider);
     List<String> providerList = locationManager.getAllProviders();
     if (null != locations) {
         longitude = locations.getLongitude();
         latitude = locations.getLatitude();
         //Provide = locations.getProvider();
         pd1 = new ProgressDialog(MapsActivity.this);
         pd1.setMessage("Loading Location....");
         pd1.setCanceledOnTouchOutside(false);
         pd1.show();
         onLocationChanged(locations);
     }
     //Dialogue...

     // camera.takePicture(null, null, jpegCallback);
     dialog_flag = false;
     //refreshLocationProvider();
     //for check automatic time setting
     if (AutomaticDateTimeSetting()) {
         if (remarkflag) {
             // camera.takePicture(null, null, jpegCallback);
             Calendar c = Calendar.getInstance();
             SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
             String CurrDate1 = dateformat.format(c.getTime());
             SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yy hh:mm a");
             String CurrTime1 = timeformat.format(c.getTime());
             Log.e("datetime", CurrTime1);


             // String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
             // String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));

             //Log.e("lat&lon", latitude + "" + longitude);
             // String Provide = locations.getProvider();
             // Log.e("getProvider",provider);













             if (((EmpowerApplication) getApplication()).isInternetOn()) {
                 try {
                     String adress = getLocationAddress(Double.parseDouble(String.valueOf(lat1)), Double.parseDouble(String.valueOf(long1)));

                     AddMobileSwipesDataForMap(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), CurrTime1, String.valueOf(lat1), String.valueOf(long1), "1", adress, "", "True", "", Provider1);
                     EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                     last_punch_txt.setText(CurrTime1 + " " + "IN");
                     ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                     setUpExpList();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             } else {
                 // Toast.makeText(getApplicationContext(), "Before Data saved in local", Toast.LENGTH_LONG).show();
                 if (lat1 != null && long1 != null) {
                     String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                             CurrTime1, String.valueOf(lat1), String.valueOf(long1), "1", "", "", "False", "", Provider1);
                     EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                     last_punch_txt.setText(CurrTime1 + " " + "IN");

                     ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                     Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                     Log.e("rowid", rowid);
                     setUpExpList();
                     // Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                     dialog(current_date.getText().toString());
                 } else {
                     Snackbar.make(findViewById(R.id.snackbar), "Location not found please try again", Snackbar.LENGTH_LONG).show();
                     //Toast.makeText(this,"Location not found please try again",Toast.LENGTH_LONG).show();
                 }
             }
         } else {

             dialogRemark = new Dialog(MapsActivity.this);
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
             btn_submit.setOnClickListener(new View.OnClickListener() {

                 public void onClick(View arg0) {
                     remark_text = remark.getText().toString();
                     LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                     boolean gps_enabled = false;
                     boolean network_enabled = false;
                     if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                         if (!remark_text.isEmpty()) {
                             dialogRemark.dismiss();

                             Calendar c = Calendar.getInstance();
                             SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                             String CurrDate1 = dateformat.format(c.getTime());
                             SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                             String CurrTime1 = timeformat.format(c.getTime());

                             //String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
                             //String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));
                             //String Provide = locations.getProvider();//EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
                             // Log.e("getProvider",provider);

                             //getProvider

                             //Toast.makeText(getApplicationContext(),"latitude"+latitude,Toast.LENGTH_LONG).show();
                             if (((EmpowerApplication) getApplication()).isInternetOn()) {
                                 try {
                                     String adress = getLocationAddress(Double.parseDouble(String.valueOf(lat1)), Double.parseDouble(String.valueOf(long1)));


                                     //dialog(current_date.getText().toString());
                                     // AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")),CurrDate1,latitude,longitude,"1",adress,imagedata,"True",remark_text);

                                     AddMobileSwipesDataForMap(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), CurrTime1, String.valueOf(lat1), String.valueOf(long1), "1", adress, "", "True", remark_text, Provider1);

                                     EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                                     last_punch_txt.setText(CurrTime1 + " " + "IN");
                                     ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                                     setUpExpList();
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                             } else {
                                 if (lat1 != null && long1 != null) {
                                     //Toast.makeText(getApplicationContext(), "Before Data saved in local", Toast.LENGTH_LONG).show();
                                     String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                             CurrTime1, String.valueOf(lat1), String.valueOf(long1), "1", "", "", "False", remark_text, Provider1);
                                     EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                                     last_punch_txt.setText(CurrTime1 + " " + "IN");
                                     ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                                     Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                                     // Log.e("rowid", rowid);
                                     setUpExpList();

                                     dialog(current_date.getText().toString());
                                 } else {
                                     Snackbar.make(findViewById(R.id.snackbar), "Location not found please try again", Snackbar.LENGTH_LONG).show();
                                     //Toast.makeText(MapsActivity.this,"Location not found please try again",Toast.LENGTH_LONG).show();
                                 }
                             }
                         } else {
                             EmpowerApplication.dialog("Please Enter Remark", MapsActivity.this);
                         }

                     } else {
                         //dialogRemark.dismiss();
                         checkPermissions();
                     }
                 }
             });

         }


     } else {
         AutomaticDateTimeSetting();
     }
 }
    //...........


    public void out_picture(View view) {

        if(latitude1.size() !=0){
            for (int i=0; i<latitude1.size(); i++){
                if (Double.parseDouble(latitude1.get(i)) != 0.0 && Double.parseDouble(longitude1.get(i)) != 0.0 && Integer.parseInt(nearByDistance.get(i)) != 0 && Integer.parseInt(officeAreaDistance.get(i))!= 0) {
                    Float distance = locationDistance(Double.parseDouble(latitude1.get(i)),Double.parseDouble(longitude1.get(i)));
                    if (distance < Integer.parseInt(officeAreaDistance.get(i)) || distance < Integer.parseInt(nearByDistance.get(i))) {
                        outPuch();
                        break;
                    } else {
                        if(latitude1.size()-1== i) {

                            if (EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")).equalsIgnoreCase("0")) {
                                isInorOUTPunch = true;
                                OAMPermission("You are not in office area.");
                            } else {
                                EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);

                            }
                        }
                    }
                } else {
                    outPuch();
                    break;

                }
            }
        }else {
            outPuch();
        }
    }
    void outPuch(){


        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        final String provider1 = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestLocationPermission();

            return;
        }
       /* if(checkPermissions1()){
            requestLocationPermission();
            return;

        }*/
        locations = locationManager.getLastKnownLocation(provider1);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations) {
            longitude = locations.getLongitude();
            latitude = locations.getLatitude();
            onLocationChanged(locations);
        }
        dialog_flag = true;
        // refreshLocationProvider();
        if (AutomaticDateTimeSetting()) {
            if (remarkflag) {
                //camera.takePicture(null, null, jpegCallback);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                String CurrDate1 = dateformat.format(c.getTime());
                SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                String CurrTime1 = timeformat.format(c.getTime());
                //Log.e("datetime",formattedDate+""+timeformat);

                //String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
                //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);

                //String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
                //String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));
                //String provider = locations.getProvider();//EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
                //Log.e("getProvider",provider);
                if (((EmpowerApplication) getApplication()).isInternetOn()) {
                    try {
                        if (lat1 != null && long1 != null) {
                            String adress = getLocationAddress(Double.parseDouble(String.valueOf(lat1)), Double.parseDouble(String.valueOf(long1)));


                            //AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")),CurrTime1,latitude,longitude,"2",adress,imagedata,"True",remark_text);
                            AddMobileSwipesDataForMap(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), CurrTime1, String.valueOf(lat1), String.valueOf(long1), "2", adress, "", "True", "", Provider1);


                            EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                            last_punch_txt.setText(CurrTime1 + " " + "OUT");
                            ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "OUT");
                            setUpExpList();
                        } else {
                            Snackbar.make(findViewById(R.id.snackbar), "Location not found please try again", Snackbar.LENGTH_LONG).show();
                            //Toast.makeText(this,"Location not found please try again",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (lat1 != null && long1 != null) {


                        String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                CurrTime1, String.valueOf(lat1), String.valueOf(long1), "2", "", "", "False", "", Provider1);
                        EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                        last_punch_txt.setText(CurrTime1 + " " + "OUT");
                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "OUT");


                        Log.e("rowid", rowid);
                        setUpExpList();
                        Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                        dialog(current_date.getText().toString());
                    } else {
                        Snackbar.make(findViewById(R.id.snackbar), "Location not found please try again", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(this,"Location not found please try again",Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                /*if (longitude == 0.0 && latitude == 0.0) {
                    Toast.makeText(getApplicationContext(), "no location found", Toast.LENGTH_LONG).show();
                    In_button.setEnabled(false);

                } else {
*/
                dialogRemark = new Dialog(MapsActivity.this);
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
                //btn_submit...
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        String remark_text = remark.getText().toString();
                        LocationManager lm1 = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        if (lm1.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (!remark_text.isEmpty()) {
                                dialogRemark.dismiss();

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                                String CurrDate1 = dateformat.format(c.getTime());
                                SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                String CurrTime1 = timeformat.format(c.getTime());

                                // Log.e("timedateout",CurrTime1);


                                // String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE));
                                // String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE));
                                //String provider =locations.getProvider(); //EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider"));
                                // Log.e("getProvider",provider);

                                if (((EmpowerApplication) getApplication()).isInternetOn()) {

                                    try {
                                        if (lat1 != null && long1 != null) {
                                            String adress = getLocationAddress(Double.parseDouble(String.valueOf(lat1)), Double.parseDouble(String.valueOf(long1)));
                                            //dialog(current_date.getText().toString());
                                            // AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")),CurrDate1,latitude,longitude,"2",adress,imagedata,"True",remark_text);
                                            AddMobileSwipesDataForMap(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), CurrTime1, String.valueOf(lat1), String.valueOf(long1), "2", adress, "", "True", remark_text, Provider1);

                                            EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                                            last_punch_txt.setText(CurrTime1 + " " + "OUT");
                                            ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "OUT");
                                            setUpExpList();
                                        } else {
                                            Snackbar.make(findViewById(R.id.snackbar), "Location not found please try again", Snackbar.LENGTH_LONG).show();
                                            //Toast.makeText(this,"Location not found please try again",Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    if (lat1 != null && long1 != null) {
                                        String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                                CurrTime1, String.valueOf(lat1), String.valueOf(long1), "2", "", "", "False", remark_text, Provider1);
                                        EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                                        last_punch_txt.setText(CurrTime1 + " " + "OUT");
                                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "OUT");
                                        //  Log.e("rowid", rowid);
                                        setUpExpList();
                                        Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                                        dialog(current_date.getText().toString());
                                    } else {
                                        Snackbar.make(findViewById(R.id.snackbar), "Location not found please try again", Snackbar.LENGTH_LONG).show();
                                        //Toast.makeText(MapsActivity.this,"Location not found please try again",Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                EmpowerApplication.dialog("Please Enter Remark", MapsActivity.this);
                            }
                        } else {
                            dialogRemark.dismiss();
                            checkPermissions();

                        }
                    }
                });


                // }
            }
        } else {
            AutomaticDateTimeSetting();
        }

    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {

                showSettingDialog();
            }
        } else
            showSettingDialog();
    }

    private boolean checkPermissions1() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //requestLocationPermission();
                return true;
            } else {

                showSettingDialog();
            }
        } else
            showSettingDialog();

        return false;

    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(3 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
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
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
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

                        pd2 = new ProgressDialog(MapsActivity.this);
                        pd2.setMessage("Loading Location....");
                        pd2.setCanceledOnTouchOutside(false);
                        pd2.show();
                        // Log.e("Settings", "Result OK");
                        //updateGPSStatus("GPS is Enabled in your device");
                        flag1 = true;
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


        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(new InternetBroadcastReceiver(), filter);
        } catch (Exception e) {
        }



       /* locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        final String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations ) {
            longitude = locations.getLongitude();
            latitude = locations.getLatitude();
            //Provide = locations.getProvider();
            onLocationChanged(locations);
        }*/

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        //registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        //Unregister receiver on destroy

    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    //this is for refresh location
    private void refreshLocationProvider() {
        // TODO Auto-generated method stub
        if (EmpowerApplication.check_is_gps_on(getApplicationContext())) {
            requestLocationPermission();
        } else {
            startService(new Intent(MapsActivity.this, MyService.class));
        }
    }

    public boolean AutomaticDateTimeSetting() {
        boolean auto = false;
        if (Build.VERSION.SDK_INT > 16) {
            try {
                a = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
                String aa = TimeZone.getDefault().getDisplayName();
                b = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE);

                if (a == 1 && b == 1) {
                    // Log.d("b"," "+b);
                    // Toast.makeText(getApplicationContext(),"automatic setting"+a,Toast.LENGTH_LONG).show();
                    Calendar c = Calendar.getInstance();
                    Date d = c.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String time1 = sdf.format(d);
                    auto = true;

                } else if (a == 0 || b == 0) {
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
        final Dialog dialog1 = new Dialog(MapsActivity.this);
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

    public void AddMobileSwipesDataForMap(String employeeId,String deviceId,String swipeTime,String latitude,String longitude,String door,String locationAddress,String swipeImageFileName,String isOnlineSwipe,String remark,String lprovider) {

        pd = new ProgressDialog(MapsActivity.this);
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> MarkAttendancedata=new HashMap<String,String>();
        MarkAttendancedata.put("employeeId",employeeId);
        MarkAttendancedata.put("deviceId",deviceId);
        MarkAttendancedata.put("swipeTime",swipeTime);
        MarkAttendancedata.put("latitude",latitude);
        MarkAttendancedata.put("longitude",longitude);
        MarkAttendancedata.put("door",door);
        MarkAttendancedata.put("locationAddress",locationAddress);
        MarkAttendancedata.put("swipeImageFileName","");
        MarkAttendancedata.put("isOnlineSwipe",isOnlineSwipe);
        MarkAttendancedata.put("remark",remark);
        MarkAttendancedata.put("locationProvider",lprovider);

        retrofit2.Call<CommanResponsePojo> call = apiService.AddMobileSwipesData(MarkAttendancedata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();
                //Log.d("User ID1: ", response.body().toString());
                if (response.isSuccessful()) {


                    if (response.body().getStatus().equals("1")) {

                        dialog(current_date.getText().toString());


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), MapsActivity.this);

                    }

                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", MapsActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", MapsActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", MapsActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public void AddApplicationViews(){

        if(HomeFragment.value.size()==1){

            //for(int i=0;i<key.size();i++) {

            //if(key.get(i).equals("HideLeaveApp"))

            stub.setLayoutResource(R.layout.dynamic_application_layout_one);
            View inflated = stub.inflate();
            //TextView textView = inflated.findViewById(R.id.te_xt);
            if(HomeFragment.key.get(0).equals("HideLeaveApp")) {
                TextView textView = inflated.findViewById(R.id.te_xt);
                textView.setText("Leave Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
                       startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideCompOffApp")) {
                TextView textView1 = inflated.findViewById(R.id.te_xt);
                textView1.setText("CompOff Application");
                textView1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideRegularizationApp")) {
                TextView textView2 = inflated.findViewById(R.id.te_xt);
                textView2.setText("Regularization Application");
                textView2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideWFHApp")) {
                TextView textView3 = inflated.findViewById(R.id.te_xt);
                textView3.setText("WFH Application");
                textView3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
                        startActivity(i);
                    }

                });
            }
            if(HomeFragment.key.get(0).equals("HideOutDutyApp")) {
                TextView textView4 = inflated.findViewById(R.id.te_xt);
                textView4.setText("OutDuty Application");
                textView4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                textView.setText("Leave Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                textView.setText("Leave Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
                        startActivity(i);
                    }

                });
            }


        }
        if(HomeFragment.value.size()==4){

            stub.setLayoutResource(R.layout.dynamic_application_layout);
            View inflated = stub.inflate();
            TextView textView=inflated.findViewById(R.id.te_xt);
                            /*textView.setText("Leave Application");
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            textView1.setText("Regularization Application");
                            TextView textView3=inflated.findViewById(R.id.te_xt2);
                            textView3.setText("OutDuty Application");*/

            if(HomeFragment.key.get(0).equals("HideLeaveApp")) {
                textView.setText("Leave Application");
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, Leave_Application.class);
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
                        Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
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
                        Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
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
                        Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
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
                    Intent i=new Intent(MapsActivity.this, Leave_Application.class);
                   startActivity(i);
                }
            });

            attd_reg_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(MapsActivity.this, Attend_Regularization.class);
                    startActivity(i);
                }
            });


            out_duty_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(MapsActivity.this, Out_Duty_Application.class);
                   startActivity(i);
                }

            });

            app_comp_off.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(MapsActivity.this, CompoffActivity.class);
                    startActivity(i);
                }

            });

            app_wfh.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent i=new Intent(MapsActivity.this, WorkFromHomeActivity.class);
                    startActivity(i);
                }

            });




        }
    }


    public float locationDistance( Double lat,Double lon) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat);
        loc1.setLongitude(lon);

        Location loc2 = new Location("");
        loc2.setLatitude(latitude);
        loc2.setLongitude(longitude);
     //   loc2.setLatitude(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE))));
     //   loc2.setLongitude(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE))));
        float distanceInMeters = loc1.distanceTo(loc2);
        Log.e("distance", String.valueOf(distanceInMeters));

        return distanceInMeters;
    }

    public void OAMPermission(String msg) {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

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
                    inPunch();

                } else {
                    outPuch();

                }
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                Toast.makeText(getApplicationContext(),
                        "No Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }



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
        Log.d("", "getEmployeeAreaConfig_ID: "+configdata);
        latitude1=new ArrayList<>();
        longitude1=new ArrayList<>();
        nearByDistance=new ArrayList<>();
        officeAreaDistance=new ArrayList<>();
        Call<AreaPojo> call = apiService.getEmployeeAreaConfig(configdata);
        call.enqueue(new Callback<AreaPojo>() {
            @Override
            public void onResponse(retrofit2.Call<AreaPojo> call, Response<AreaPojo> response) {
                latitude1.clear();
                longitude1.clear();
                nearByDistance.clear();
                // pd.dismiss();

                officeAreaDistance.clear();
                if(response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                        //       pd.dismiss();


                      //  Toast.makeText(MapsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        for(int i=0;i<response.body().getData().size();i++) {
                            if(response.body().getData().get(i).getLatitude()!=null || !response.body().getData().get(i).getLatitude().equals("")){
                                latitude1.add(response.body().getData().get(i).getLatitude());
                            }
                            if(response.body().getData().get(i).getLongitude()!=null  || !response.body().getData().get(i).getLongitude().equals("")){
                                longitude1.add(response.body().getData().get(i).getLongitude());
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
                            EmpowerApplication.alertdialog("File or directory not found", MapsActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", MapsActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", MapsActivity.this);

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
                EmpowerApplication.alertdialog(t.getMessage(), MapsActivity.this);



            }
        });
    }

}