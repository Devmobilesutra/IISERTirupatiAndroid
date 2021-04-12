package com.mobilesutra.iiser_tirupati.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobilesutra.iiser_tirupati.Activities.ActivityEvents;
import com.mobilesutra.iiser_tirupati.Activities.ActivityTabHost;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        // ...
//        context = this;
        Log.d("JARVIS", "onMessageReceived data payload: ");
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //    Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        IISERApp.log("JARVIS", " responce is  ->>>>>>>>>>>>>" + remoteMessage.getData().toString());
        if (remoteMessage == null)
            return;
        if (remoteMessage.getData().size() > 0) {
              Log.d("JARVIS", "Message data payload: " + remoteMessage.getData());
            try {
                Log.d("JARVIS", "In rty block" );



                Map<String, String> map = remoteMessage.getData();



                  //JSONObject json = new JSONObject(String.valueOf((remoteMessage.getData())));

                Log.d("JARVIS", "Message data payloadssss: JASON " + map);
                handleDataMessage(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //   Log.d("JARVIS", "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleDataMessage(Map<String,String> json) {
        IISERApp.log("JARVIS", "push json: " + json.toString());
        String notification_flag = "";
        Intent resultIntent = null;
        String message_event = "";
        String title = "";
        String message = "";
        try {


           // String badge = json.get("badge");
           // JSONObject data = json;
         //   notification_flag = json.getString("notification_flag");
            IISERApp.log("JARVIS", "aaaa: " + notification_flag);
          //  if (notification_flag.equalsIgnoreCase("notification_flag")) {
                IISERApp.log("JARVIS", "In if");

                title = json.get("title");
                message = json.get("description");
             //  JSONObject jsonObject = json.getJSONObject("notice_data");
             //  title = json.getString("title");
              //  message = json.getString("description");
             //   IISERApp.set_session("title", title);
               // IISERApp.set_session("description", message);
                IISERApp.log("JARVIS", " title->>>>>>>>>" + title );

            createNotification(title, message, "");


          //  }


            IISERApp.log("JARVIS", "message: --->" + message);



        } catch (Exception e) {
            //    MyApp.log("JARVIS", "Json Exception: " + e.getMessage());
        } /*catch (Exception e) {
            //  MyApp.log("JARVIS", "Exception: " + e.getMessage());
        }*/
    }





    // Bhavesh
    private NotificationManager notifManager;
    public void createNotification(String aMessage,String message, String click_action) {

        Intent intent;
        intent = new Intent(this,ActivityTabHost.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings.
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.


        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notifManager.createNotificationChannel(mChannel);

            builder = new NotificationCompat.Builder(this, id);

            pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT);

           /* intent = new Intent(this, MyFirebaseMessagingService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);*/

            builder.setContentTitle(aMessage)  // required
                    .setContentTitle("IISER Tirupati")
                    .setSmallIcon(R.drawable.iiser_logo) // required
                    .setContentText(message)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);


            pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT);
          /*  intent = new Intent(this, MyFirebaseMessagingService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);*/

            builder.setContentTitle(aMessage) // required
                    .setContentTitle("IISER Tirupati")
                    .setSmallIcon(R.drawable.iiser_logo) // required
                    .setContentText(message)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

}