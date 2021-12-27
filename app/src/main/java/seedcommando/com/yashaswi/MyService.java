package seedcommando.com.yashaswi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import seedcommando.com.yashaswi.constantclass.EmpowerApplication;

public class MyService extends Service {

	// flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    protected LocationManager locationManager;
    LocationListener listener = null;
    Location location; // location
    static double latitude=0.0; // latitude
    static double longitude=0.0; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 minute
    private Context mContext = null;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
    	log("onBind");
        return null;
    }
    @Override
    public void onCreate() {
    	log("onCreate");
   //     Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	latitude = 0.0;
    	longitude = 0.0;
    	log("onStart");
        //Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
        mContext = this;
        getLocation();
    }

    public void getLocation()
    {
    	 locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
         listener = new LocationListener() {
             @Override
             public void onLocationChanged(Location location) {
                 if(Attendance_cameraActivity.pd1!=null){
                     Attendance_cameraActivity.pd1.dismiss();
                 }
                 if (location != null) {
                     latitude = location.getLatitude();
                     longitude = location.getLongitude();
                     Log.d("", "latitude: "+ latitude);
                     Log.d("", "longitude: "+ longitude);

                     //location.getTime();

                     //location.getProvider();
                     Log.e("provider",location.getProvider());
                     log("latitude->"+latitude);
                     log("longitude->"+longitude);
                     EmpowerApplication.set_session("getProvider", location.getProvider());
                     EmpowerApplication.set_session(EmpowerApplication.SESSION_LATTITUDE, latitude+"");
                     EmpowerApplication.set_session(EmpowerApplication.SESSION_LONGITUDE, longitude+"");
                   //  update_my_activity(mContext, SeedApplication.get_session(SeedApplication.SESSION_ACTIVITY));
                    // Toast.makeText(mContext, "onLocationChanged\n" + "" + "latitude->" + latitude + "\n" + "longitude->" + longitude, Toast.LENGTH_SHORT).show();
                 }

             }

             @Override
             public void onStatusChanged(String provider, int status, Bundle extras) {

             }

             @Override
             public void onProviderEnabled(String provider) {

             }

             @Override
             public void onProviderDisabled(String provider) {

             }
         };
         getCellularLocation();
    }
    @Override
    public void onDestroy() {
    	log("onDestroy");
  //      Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
    }


    public Location getCellularLocation() {
        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        log("IsNetworkEnabled->"+isNetworkEnabled);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        log("isGPSEnabled->"+isGPSEnabled);

        if (isNetworkEnabled) {
        	int i = 10;
        	while((i--)>1)
        	{
        		log("i->"+i);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
                   log("locationManager->"+locationManager);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                log("Nlocation->"+location);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    log("Nlatitude->"+latitude);
                    log("Nlongitude->"+longitude);
                }
                if(true)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        log("Glocation->"+location);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.e("latitude","latitude"+latitude);
                            log("Glatitude->"+latitude);
                            log("Glongitude->"+longitude);
                           
                        }
                    }
                }
               
            }
            }
        	//update_my_activity(mContext, EmpowerApplication.get_session(EmpowerApplication.SESSION_ACTIVITY));
        	//Toast.makeText(mContext, "Lattitude->"+latitude+"\nLongitude->"+longitude, Toast.LENGTH_SHORT).show();
        }

        return location;
    }
    
    public void log(String str)
    {
        Log.i("GPSTracker", str);
    }




    public boolean isNetworkEnabled() {
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isNetworkEnabled;
    }

    public boolean isGPSEnabled() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }
    
    static void update_my_activity(Context context, String flag) {
        //SeedApplication.log("in update my activity");
        Intent intent = new Intent(flag);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        context.sendBroadcast(intent);
    }
    
    
}