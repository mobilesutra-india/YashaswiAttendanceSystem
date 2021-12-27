package seedcommando.com.yashaswi.utilitys;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.database.EmpowerDatabase;
import seedcommando.com.yashaswi.interfaces.WebServiceListener;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.markattendance.OffLineAttendancePoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by Admin on 22/01/2017.
 */
public class InternetService extends Service   {


    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;
    boolean isInProgress=false;


    /**
     * interface for clients that bind
     */
    IBinder mBinder;

    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;

    ProgressDialog pd;


    private String delRowId = "";
    EmpowerDatabase db;
    ApiInterface apiService;
    String time,remark1,lat,log,addreee,isonline,door1,provider;
    private WebServiceListener mWebServiceListener;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {

        db = new EmpowerDatabase(this);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        //mWebServiceListener=this;

    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
            //String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
           // addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
            //delRowId = empList.get(0).getId_mark();
            if(empList.get(0).getSwipeImageFileName().isEmpty()){
                if(Utilities.isNetworkAvailable(getApplicationContext())) {
                    if(!isInProgress)

                        Log.d("DATTATTATTA", "onStartCommand: ");


                    AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, "", isonline, remark1,provider);

                }else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                }
            }else {
                String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
            }


        }


        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {

    }

    public void AddMobileSwipesData(String employeeId,String deviceId,String swipeTime,String latitude,String longitude,String door,String locationAddress,
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

                Toast.makeText(getApplication(), ""+response.code(), Toast.LENGTH_LONG).show();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                        Log.e("service ","1");

                        Log.d("InternetService_response ",new Gson().toJson(response.body()));

                        Toast.makeText(InternetService.this, "BackGroundAPI_"+response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
                            if(empList.get(0).getSwipeImageFileName().isEmpty()){
                                if(Utilities.isNetworkAvailable(getApplicationContext())) {
                                    if(!isInProgress)
                                    AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, "", isonline, remark1,provider);
                                }else {
                                    Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                                }
                            }else {
                                String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                                                           }
                        }
                        isInProgress=false;
                        //dialog(current_date.getText().toString());


                    } else {
                     //   db.deleteMarkDataRecord(delRowId);
                        Log.e("failed ","0");
                        Toast.makeText(InternetService.this, "BackGroundAPI_Failed"+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        isInProgress=false;
                       /* List<OffLineAttendancePoJo> empList = db.getAttendenceList();
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

                            String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                        }
                       // EmpowerApplication.alertdialog(response.body().getMessage(), InternetService.this);
*/
                    }

                }else {
                    isInProgress=false;
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", InternetService.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", InternetService.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", InternetService.this);

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

    public String getLocationAddress(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address="";
        if(addresses!=null && !addresses.isEmpty()) {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        }

        return address;
    }

    private String loadImageFromStorage(String img)
    {

        String encoded=null;

        try {
            String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

            File dirfile = new File(filepath, "Empower");
            dirfile.mkdirs();

            File f=new File(dirfile, img);

            Log.e("image file",f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            Log.e("file Stram", String.valueOf(new FileInputStream(f)));
            FileInputStream fileInputStream=new FileInputStream(f);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                    System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
                ex.fillInStackTrace();
                //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] bytes = bos.toByteArray();
            Log.e("bytess", String.valueOf(bytes));
            encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
            Log.e("encoded bytes", encoded);
            if(!isInProgress)
            AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, encoded, isonline,remark1,provider);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return encoded;

    }

}