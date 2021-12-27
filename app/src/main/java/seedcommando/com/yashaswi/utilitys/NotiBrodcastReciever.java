package seedcommando.com.yashaswi.utilitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import seedcommando.com.yashaswi.Config;

/**
 * Created by commando4 on 4/20/2018.
 */

public class NotiBrodcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
            // new push notification is received

           // context.updatemenu("10");


            String message = intent.getStringExtra("message");

            Toast.makeText(context, "Push notification: " + message, Toast.LENGTH_LONG).show();

            //txtMessage.setText(message);
        }
    }

}
