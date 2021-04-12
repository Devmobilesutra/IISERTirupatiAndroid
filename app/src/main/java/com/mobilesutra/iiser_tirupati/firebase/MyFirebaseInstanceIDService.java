package com.mobilesutra.iiser_tirupati.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Bhavesh on 10/10/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();


    Context context;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        // storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        Log.d(TAG, "sendRegistrationToServer111111111: "+refreshedToken);


        // Notify UI that registration has completed, so the progress indicator can be hidden.
        /*Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        //  Log.e(TAG, "sendRegistrationToServer: " + token);

        Log.d(TAG, "sendRegistrationToServer: "+token);
      //  IISERApp.set_session(IISERApp.SESSION_FCM_ID,token);
    }
}
