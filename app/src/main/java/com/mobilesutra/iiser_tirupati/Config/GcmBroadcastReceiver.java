package com.mobilesutra.iiser_tirupati.Config;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.mobilesutra.iiser_tirupati.background.IISERIntentService;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    String LOG_TAG = "GcmBroadCastReceiver";
    String str_action = "";

    @Override
    public void onReceive(Context context, Intent intent) {
       /* IISERApp.log(LOG_TAG, "In onReceive");
        if (intent != null) {
            IISERApp.log(LOG_TAG, "Intent Action->" + intent.getAction().toString());
            str_action = intent.getAction().toString();
            if (str_action.equals("com.google.android.c2dm.intent.REGISTRATION") || str_action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                ComponentName comp = new ComponentName(context.getPackageName(), IISERIntentService.class.getName());
                // Start the service, keeping the device awake while it is launching.
          //      startWakefulService(context, (intent.setComponent(comp)));
                setResultCode(Activity.RESULT_OK);
            } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
                // Now to check if we're actually connected
                if (cm != null) {
                    if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra("Flag", "Connection");
                        intent1.putExtra("Status", "ON");
                        context.startService(intent1);
                    }
                }
            }
        }*/
    }
}
