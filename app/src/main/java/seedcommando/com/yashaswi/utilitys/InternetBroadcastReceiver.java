package seedcommando.com.yashaswi.utilitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by Admin on 22/01/2017.
 */
public class InternetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        if(isNetworkAvailable(context)) {
           // context.startService(new Intent(context, InternetService.class));
            Intent intents = new Intent(context.getApplicationContext(), InternetService.class);
            context.getApplicationContext().startService(intents);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
