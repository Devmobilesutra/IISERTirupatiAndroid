package com.mobilesutra.iiser_tirupati.background;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

//import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobilesutra.iiser_tirupati.Activities.ActivityTabHost;
import com.mobilesutra.iiser_tirupati.Activities.ActivityTabHost_AfterLogin;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ACADMIC_CALENDER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ASSIGNMENT;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EVENT;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SCHEDULE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_PROFILE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_NOTICE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_STUDENT_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_USER_PROFILE;
import com.mobilesutra.iiser_tirupati.Model.DTOService;
import com.mobilesutra.iiser_tirupati.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kalyani on 30/04/2016.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class IISERIntentService extends IntentService {

    private static String LOG_TAG = "IISERIntentService", assignment_id = "", course_id = "", semester_name = "";
    private Context context = null;

    public IISERIntentService() {
        super(LOG_TAG);
        IISERApp.log(LOG_TAG, "In Constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = this;
        IISERApp.log(LOG_TAG, "In onHandleIntent");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (!extras.isEmpty()) {

                processBundle(extras);

               // processNotificationData(extras);


                // GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
              //  String messageType = gcm.getMessageType(intent);
               // IISERApp.log(LOG_TAG, "MessageType->" + messageType);

               /* if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                    processNotificationData(extras);
                } else {
                    IISERApp.log(LOG_TAG, "Bundle->" + extras);
                    processBundle(extras);
                }
*/
            }
        }
    }

    protected void processNotificationData(Bundle bundle) {
        IISERApp.log(LOG_TAG, "In processNotificationData");
        IISERApp.log_bundle(bundle);
        String str_notification_flag = bundle.getString("notification_flag");
        if (str_notification_flag.equalsIgnoreCase("notice")) {
            try {

                IISERApp.log(LOG_TAG, "in notification flag = notice");

                String str_id = bundle.getString("id");
                String str_notice_title = bundle.getString("title");
                String str_description = bundle.getString("description");
                String str_pdf_link = bundle.getString("pdf_link");
                String str_expiry_date = bundle.getString("expiry_date");
                String str_is_seen = "Y";

                TABLE_NOTICE.InsertNotice(str_id, str_notice_title, str_description, str_pdf_link, str_expiry_date, str_is_seen);

                int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                notification_id = 1000 + notification_id;
                generateNoticesNotification(notification_id, str_notice_title);
                Bundle extras = new Bundle();
                IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                /* extras.putString(IISERApp.BUNDLE_RESPONSE_CODE, "200");*/
                generateNoticesNotification1(notification_id, str_notice_title, "ActivityTabHost_AfterLogin", "Notice");
                broadcast(context, "ActivityNotice", extras);

            } catch (Exception e) {
                IISERApp.log(LOG_TAG, "logtime:E1->" + e + "");
                IISERApp.log(LOG_TAG, "logtime:E2->" + e.getMessage());
                IISERApp.log(LOG_TAG, "logtime:E3->" + e.getLocalizedMessage());
            }
        } else if (str_notification_flag.equalsIgnoreCase("event")) {
            try {

                String str_id = bundle.getString("id");
                String str_event_title = bundle.getString("title");
                String str_description = bundle.getString("description");
                String str_event_date = bundle.getString("event_date");
                String str_pdf_link = bundle.getString("pdf_link");
                String str_venue = bundle.getString("venue");
                String str_is_seen = "Y";

                IISERApp.log(LOG_TAG, " str_id:" + str_id + " str_event_title:" + str_event_title + " str_description:" + str_description
                        + " str_event_date:" + str_event_date + " str_pdf_link:" + str_pdf_link + " str_venue:" + str_venue);
                TABLE_EVENT.InsertEventData(str_id, str_event_title, str_description, str_event_date,
                        str_pdf_link, str_venue, str_is_seen);

                int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                notification_id = 1000 + notification_id;
                // generateNoticesNotification(notification_id, str_event_title);
                Bundle extras = new Bundle();
                IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                // extras.putString(IISERApp.BUNDLE_RESPONSE_CODE, "200");
                generateNoticesNotification1(notification_id, str_event_title, "ActivityTabHost", "Event");
                broadcast(context, "ActivityEvents", extras);
            } catch (Exception e) {
                IISERApp.log(LOG_TAG, "logtime:E1->" + e + "");
                IISERApp.log(LOG_TAG, "logtime:E2->" + e.getMessage());
                IISERApp.log(LOG_TAG, "logtime:E3->" + e.getLocalizedMessage());
            }
        } else if (str_notification_flag.equalsIgnoreCase("assignment")) {
            try {

                IISERApp.log(LOG_TAG, "in assignmnt of simple assignment added from server");


                String assignment_id = bundle.getString("assignment_id");
                String course_id = bundle.getString("course_id");
                String str_assignment_title = bundle.getString("title");
                String str_description = bundle.getString("description");
                String str_pdf_link = bundle.getString("pdf_link");
                String str_submission_date = bundle.getString("submission_date");
                String str_is_seen = "Y";

                String str_img_url = "";
                if (bundle.containsKey("img_url")) {
                    str_img_url = bundle.getString("img_url");
                    //get_assignment_notification_data();
                }

                TABLE_ASSIGNMENT.InsertAssignment(assignment_id, str_assignment_title, course_id, str_description, str_pdf_link,
                        str_submission_date, str_is_seen, str_img_url);

                int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                notification_id = 1000 + notification_id;
                generateNoticesNotification1(notification_id, str_assignment_title, "ActivityTabHost_AfterLogin", "Assignment");
                Bundle extras = new Bundle();
                broadcast(context, "ActivityAssignment", bundle);

            } catch (Exception e) {
                IISERApp.log(LOG_TAG, "logtime:E1->" + e + "");
                IISERApp.log(LOG_TAG, "logtime:E2->" + e.getMessage());
                IISERApp.log(LOG_TAG, "logtime:E3->" + e.getLocalizedMessage());
            }
        } else if (str_notification_flag.equalsIgnoreCase("examschedule")) {
            try {
                String semester_name1 = bundle.getString("semester_name");
                IISERApp.log(LOG_TAG, "Semester_name:" + semester_name1);
                get_exam_schedule_notification_data(semester_name1);

            } catch (Exception e) {
                IISERApp.log(LOG_TAG, "logtime:E1->" + e + "");
                IISERApp.log(LOG_TAG, "logtime:E2->" + e.getMessage());
                IISERApp.log(LOG_TAG, "logtime:E3->" + e.getLocalizedMessage());
            }
        } else if (str_notification_flag.equalsIgnoreCase("timetable")) {
            try {
                String sem_name = bundle.getString("semester_name");
                IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                get_timetable_notification_data(sem_name);

            } catch (Exception e) {
                IISERApp.log(LOG_TAG, "logtime:E1->" + e + "");
                IISERApp.log(LOG_TAG, "logtime:E2->" + e.getMessage());
                IISERApp.log(LOG_TAG, "logtime:E3->" + e.getLocalizedMessage());
            }
        }

       /* else if (str_notification_flag.equalsIgnoreCase("new faculty assignment added")) {
            try {

                IISERApp.log(LOG_TAG,"NOTIFCATION IS GENERATED");


                String assignment_id = bundle.getString("assignment_id");
                String course_id = bundle.getString("course_id");
                String str_assignment_title = bundle.getString("title");
                String str_description = bundle.getString("description");
                String str_pdf_link = bundle.getString("pdf_link");
                String str_submission_date = bundle.getString("submission_date");
                String str_is_seen = "Y";
                String str_img_url = bundle.getString("img_url");
                //get_assignment_notification_data();
                TABLE_ASSIGNMENT.InsertAssignment(assignment_id, str_assignment_title, course_id, str_description, str_pdf_link,
                        str_submission_date, str_is_seen, str_img_url);

                int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                notification_id = 1000 + notification_id;
                generateNoticesNotification1(notification_id, str_assignment_title, "ActivityTabHost_AfterLogin", "Assignment");
                Bundle extras = new Bundle();
                broadcast(context, "ActivityAssignment", bundle);

            } catch (Exception e) {
                IISERApp.log(LOG_TAG, "logtime:E1->" + e + "");
                IISERApp.log(LOG_TAG, "logtime:E2->" + e.getMessage());
                IISERApp.log(LOG_TAG, "logtime:E3->" + e.getLocalizedMessage());
            }

        }*/
    }

    private void generateNoticesNotification(int notification_id, String str_notice_title) {
        String title = "IISER";
        String subtitle = str_notice_title;
        Intent intent;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context)
                .setContentTitle(title).setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        intent = new Intent(context, ActivityTabHost.class).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra(IISERApp.NOTIFICATION_ID, notification_id);
        mBuilder.setTicker(subtitle);
        mBuilder.setContentText(subtitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mBuilder.setColor(getResources().getColor(R.color.tab_default_color));
        }
        mBuilder.setSmallIcon(R.mipmap.app_icon);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notification_id, mBuilder.build());

    }

    private void generateNoticesNotification1(int notification_id, String str_notice_title, String activity_name
            , String notice_flag) {
        String title = "IISER";
        String subtitle = str_notice_title;
        try {
            Intent intent;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context)
                    .setContentTitle(title).setAutoCancel(true)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            activity_name = "com.mobilesutra.iiser_tirupati.Activities." + activity_name;
            intent = new Intent(context, Class.forName(activity_name)).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            intent.putExtra(IISERApp.NOTIFICATION_ID, notification_id);
            intent.putExtra(IISERApp.NOTIFICATION_FLAG, notice_flag);
            mBuilder.setTicker(subtitle);
            mBuilder.setContentText(subtitle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                mBuilder.setColor(getResources().getColor(R.color.tab_default_color));
            }

            mBuilder.setSmallIcon(R.mipmap.app_icon);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(notification_id, mBuilder.build());
        } catch (ClassNotFoundException e) {
            IISERApp.log(LOG_TAG, "error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateNotification(Context context, int notification_id,
                                      String str_notice_title, String activity_name
            , String notice_flag) {
        String title = "", subtitle = "", message = "";

        //if (extras != null) {
        //if (extras.containsKey("notification_title"))
        title = str_notice_title;
        //if (extras.containsKey("notification_subtitle"))
        subtitle = str_notice_title;//extras.getString("notification_subtitle");
        // if (extras.containsKey("notification_text"))
        message = str_notice_title;//extras.getString("notification_text");
        subtitle = message;


        Intent intent;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)

                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
       /* NOTIFICATION_ID = MyApp.get_Intsession(MyApp.SESSION_NOTIFICATION_ID);
        MyApp.set_session(MyApp.SESSION_NOTIFICATION_ID, (NOTIFICATION_ID + 1) + "");*/
        intent = new Intent(context, ActivityTabHost_AfterLogin.class).setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.putExtra("message", message);
        intent.putExtra(IISERApp.NOTIFICATION_ID, notification_id);
        intent.putExtra(IISERApp.NOTIFICATION_FLAG, notice_flag);
        mBuilder.setTicker(subtitle);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(subtitle));
        mBuilder.setContentText(subtitle);
        mBuilder.setSmallIcon(R.mipmap.app_icon);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
        PendingIntent contentIntent = PendingIntent.getActivity(context, notification_id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notification_id, mBuilder.build());
        //}

    }

    protected void processBundle(Bundle bundle) {
        IISERApp.log(LOG_TAG, "In processBundle");
        IISERApp.log_bundle(bundle);
        try {
            if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_LOGIN)) {
                insert_user_data();
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_EVENT)) {
                get_event_data();
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_NOTICE)) {
                get_notice_data(bundle.getString("Activity_name"));
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_EXAM_SCHEDULE)) {
                get_exam_schedule_data(bundle.getString("Activity_name"));
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_TIMETABLE)) {
                get_timetable_data(bundle.getString("Activity_name"));
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_ASSIGNMENT)) {
                get_assignment_data(bundle.getString("Activity_name"));
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_UPDATE_USER_DATA)) {
                update_user_profile_toserver(bundle);
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_APPLY_SELECTED_COURSE)) {
                send_selected_course_to_server(bundle);
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_ASSIGNMENT_NOTIFICATION)) {
                get_assignment_notification_data();
            } /*else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_GET_COURSE_DATA)) {
                IISERApp.log(LOG_TAG, "enter into get course data if");
                insert_hardcode_course_data_frm_json();
            }*/ else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_FACULTY)) {
                insert_complete_faculty_data();
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_ACADEMIC_CALENDER)) {
                get_academic_calender_data();
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_SEND_ASSIGNMENT)) {
                send_assignment_to_server(bundle);
            } else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_STUD_ATTENDENCE)) {
                getStudenetAttendenceFromserver();
            }else if (bundle.getString(IISERApp.INTENT_FLAG).equalsIgnoreCase(IISERApp.INTENT_FLAG_FACULTY_ATTENDENCE)) {
                getFacultyAttendenceFromserver();
            }

        } catch (NullPointerException e) {
            IISERApp.log(LOG_TAG, "Bundle->" + e + "");
            IISERApp.log(LOG_TAG, "Bundle->" + e.getMessage());
            IISERApp.log(LOG_TAG, "Bundle->" + e.getLocalizedMessage());
        }
    }

    private void send_assignment_to_server(Bundle extras) {

        IISERApp.log(LOG_TAG, "in  send_assignment_to_server->");


        // str_byte_string = ConvertString(new_str_path);

        /*MultipartBuilder obj = new MultipartBuilder().type(MultipartBuilder.FORM);
        try {
            if (bundle.get("document_file") != null) {
                File fileD = new File(bundle.get("document_file").toString());
                RVFApp.logi(LOG_TAG, "IsD FIle->" + fileD.exists());
                if (fileD.exists())
                    obj.addFormDataPart("document_file", fileD.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileD));//"application/pdf"
            }
        } catch (Exception e) {
            obj.addFormDataPart("document_file", "");
        }
        try {
            if (bundle.get("video_file") != null) {
                File fileV = new File(bundle.get("video_file").toString());
                RVFApp.logi(LOG_TAG, "IsV FIle->" + fileV.exists());
                if (fileV.exists())
                    obj.addFormDataPart("video_file", fileV.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileV));//"application/pdf"
            }
        } catch (Exception e) {
            obj.addFormDataPart("document_file", "");
        }
        obj
                .addFormDataPart("patient_id", bundle.getString("patient_id"))
                .addFormDataPart("question_id", bundle.getString("question_id"))
                .addFormDataPart("disease_type", bundle.getString("disease_type"))
                .addFormDataPart("video_status", bundle.getString("video_status"))
                .addFormDataPart("document_status", bundle.getString("document_status"));
        if (TextUtils.isEmpty(bundle.getString("answer_text")))
            obj.addFormDataPart("answer_text", "");
        else
            obj.addFormDataPart("answer_text", bundle.getString("answer_text"));


        RequestBody formBody = obj.build();

        DTOService dtoService = post_server_call(RVFApp.url_send_cerebral_answer, formBody);








        */

       /* IISERApp.log(LOG_TAG, "In send_assignment_to_server");
        File fileD = new File(extras.getString("document_file"));
        IISERApp.log(LOG_TAG, "IsD FIle->" + fileD.exists());
        if (fileD.exists())
            IISERApp.log(LOG_TAG,"document_file:"+fileD.getName()+"   :"
                    +RequestBody.create(MediaType.parse("application/pdf"), fileD));//"application/pdf"*/
        MultipartBuilder obj = new MultipartBuilder().type(MultipartBuilder.FORM);
       /* try {
            if (extras.getString("document_file") != null) {
                IISERApp.log(LOG_TAG, "in  document_file null->");

                File fileD = new File(extras.getString("document_file").toString());
                IISERApp.log(LOG_TAG, "IsD FIle->" + fileD.exists());
                if (fileD.exists())
                    obj.addFormDataPart("document_string", fileD.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileD));//"application/pdf"
            }
        } catch (Exception e) {
            obj.addFormDataPart("document_string", "");
        }*/


        //obj.addFormDataPart("document_string", fileD.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileD));

        /*File fileAudio = new File(extras.getString("audio_file"));
        IISERApp.log(LOG_TAG, "isfileAudio FIle->" + fileAudio.exists());
        if (fileAudio.exists())
            IISERApp.log(LOG_TAG,"audio_file:"+fileAudio.getName()+"   :"
                    +RequestBody.create(MediaType.parse("application/audio"), fileAudio));//"application/pdf"*/

//change parameters here if want to
        obj.addFormDataPart("document_string", IISERApp.get_session(IISERApp.SESSION_DOCUMENT));//"application/pdf"  fileD.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileD)

        obj.addFormDataPart("audio_string", "");//fileAudio.getName(), RequestBody.create(MediaType.parse("application/audio"), fileAudio)
        obj.addFormDataPart("course", extras.getString("course_id"));


        obj.addFormDataPart("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID));
        obj.addFormDataPart("submitted_date", extras.getString("submitted_date"));
        obj.addFormDataPart("my_text", extras.getString("assignment_text"));
        obj.addFormDataPart("image_string", IISERApp.get_session(IISERApp.SESSION_IMAGE_STRING));
        //obj.addFormDataPart("course", extras.getString("course_id"));

        obj.addFormDataPart("mime_type", IISERApp.get_session(IISERApp.SESSION_MIME_TYPE));

        IISERApp.log(LOG_TAG, "in values :  extras.getStringcourse_id):" + extras.getString("course_id")
                + "IISERApp.get_session(IISERApp.SESSION_USER_ID):" + IISERApp.get_session(IISERApp.SESSION_USER_ID)
                + "extras.getString(\"photoString\"):" + extras.getString("photoString") + "date" + extras.getString("submitted_date") + "mime:" + IISERApp.get_session(IISERApp.SESSION_IMAGE_STRING));
        RequestBody formBody = obj.build();


        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_send_assignment, formBody);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {

            IISERApp.log(LOG_TAG, "in  getStr_response_code() == 200->");

            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {
                    IISERApp.log(LOG_TAG, "in if equalsIgnoreCase(\"1\"))>");


                    //insert this assignment in assignment table


                   /* SQLiteDatabase db = IISERApp.db.getWritableDatabase();
                    db.beginTransaction();

                    String sql = TABLE_USER_PROFILE.QUERY_UPDATE;
                    SQLiteStatement statement = db.compileStatement(sql);

                    statement.clearBindings();
                    statement.bindString(1, extras.getString("course"));
                    statement.bindString(2, extras.getString("user_id"));
                    statement.bindString(2, extras.getString("submitted_date"));
                    statement.bindString(2, extras.getString("my_text"));
                    statement.bindString(2, extras.getString("image_string"));
                    statement.execute();
*/

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
                    broadcast(context, "ActivityAddAssignment", bundle);

                } else {
                    IISERApp.log(LOG_TAG, "in else  equalsIgnoreCase(\"1\"))>");

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    broadcast(context, "ActivityAddAssignment", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

    }

    private void insert_complete_faculty_data() {
        IISERApp.log(LOG_TAG, "insert_complete_faculty_data()");
        Bundle bundle;

        bundle = new Bundle();
        TABLE_FACULTY_PROFILE.delete_tbl_faculty_profile();

        /*List<DTO_Faculty_Profile> DTOFacultyProfileList = new ArrayList<>();
        DTO_Faculty_Profile DTOFacultyProfile;

        DTOFacultyProfile = new DTO_Faculty_Profile("1", "Prof. BJ Rao",
                "Professor, Chair, Biology and Dean Faculty", "Biology",
                "bjrao@iisertirupati.ac.in", "+918772500401",
                "",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("2", "Dr. V Siva Kumar",
                "Associate Professor", "Biology",
                "vallabsr@iisertirupati.ac.in", "+918772500404",
                "http://www.iisertirupati.ac.in/people/faculty/siva.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("3", "Dr. Eswar Rami Reddy",
                "Assistant Professor", "Biology",
                "eswar.ramireddy@iisertirupati.ac.in", "918772500442",
                "http://www.iisertirupati.ac.in/people/faculty/eswar.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("4", "Dr. Nandini Rajamani ",
                "Assistant Professor", "Biology",
                "nandini@iisertirupati.ac.in", "+918772500402",
                "http://www.iisertirupati.ac.in/people/faculty/nandini.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("5", "Dr. Pakala Suresh Babu",
                "Assistant Professor (On Contract)", "Biology",
                "pakalasb@iisertirupati.ac.in", "+918772500405",
                "http://www.iisertirupati.ac.in/people/faculty/suresh.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("6", "Dr. Raju Mukherjee",
                "Assistant Professor", "Biology",
                "raju.mukherjee@iisertirupati.ac.in", "+918772500441",
                "http://www.iisertirupati.ac.in/people/faculty/raju.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("7", "Dr. Suchi Goel",
                "Assistant Professor", "Biology",
                "suchi@iisertirupati.ac.in", "08772500447",
                "",
                "http://www.iisertirupati.ac.in/people/faculty/suchigoel.php");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("8", "Dr. Vasudharani Devanathan",
                "Assistant Professor", "Biology",
                "vasudharani@iisertirupati.ac.in", "08772500403",
                "http://www.iisertirupati.ac.in/people/faculty/vasudha.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("9", "Dr. V V Robin",
                "Assistant Professor", "Biology",
                "robin@iisertirupati.ac.in", "08772500443",
                "http://www.iisertirupati.ac.in/people/faculty/robin.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("10", "Dr. Sanjay Kumar",
                "Ramalingaswami Fellow", "Biology",
                "sanjay@iisertirupati.ac.in", "08772500901",
                "http://www.iisertirupati.ac.in/people/faculty/sanjay.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("11", "Prof. K.N. Ganesh",
                "Director, Professor and Chair", "Chemistry",
                "kn.ganesh@iisertirupati.ac.in", "08772500202",
                "",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);


        DTOFacultyProfile = new DTO_Faculty_Profile("12", "Dr. Rajesh Viswanathan ",
                "", "Chemistry",
                "@iisertirupati.ac.in", "08772500",
                "",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("13", "Dr. Ashwani Sharma",
                "Assistant Professor", "Chemistry & Biology",
                "a.sharma@iisertirupati.ac.in", "08772500415",
                "http://www.iisertirupati.ac.in/people/faculty/asharma.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("14", "Dr. Gopinath Purushothaman",
                "Assistant Professor", "Chemistry",
                "gopi@iisertirupati.ac.in", "08772500413",
                "http://www.iisertirupati.ac.in/people/faculty/gopi.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);


        DTOFacultyProfile = new DTO_Faculty_Profile("15", "Prof. Nagaraj D S",
                "Professor, Chair", "Mathematics",
                "dsn@iisertirupati.ac.in", "08772500421",
                "http://www.iisertirupati.ac.in/people/faculty/nagaraj.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("16", "Dr. Anilatmaja Aryasomayajula",
                "Assistant Professor", "Mathematics",
                "anil.arya@iisertirupati.ac.in", "08772500425",
                "http://www.iisertirupati.ac.in/people/faculty/anil.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("17", "Dr. Girja Shanker Tripathi",
                "Assistant Professor", "Mathematics",
                "girja@iisertirupati.ac.in", "08772500427",
                "http://www.iisertirupati.ac.in/people/faculty/girja.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);


        DTOFacultyProfile = new DTO_Faculty_Profile("18", "Prof. Ambika",
                "Professor, Chair, Physics and Dean Academics", "Physics",
                "g.ambika@iisertirupati.ac.in", "08772500230",
                "http://www.iisertirupati.ac.in/people/faculty/ambika.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);
        DTOFacultyProfile = new DTO_Faculty_Profile("19", "Dr. Arunima Banerjee",
                "Assistant Professor", "Physics",
                "arunima@iisertirupati.ac.in", "08772500446",
                "http://www.iisertirupati.ac.in/people/faculty/arunima/index.html",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("20", "Dr. Chitrasen Jena",
                "Assistant Professor", "Physics",
                "cjena@iisertirupati.ac.in", "08772500445",
                "http://www.iisertirupati.ac.in/people/faculty/chitrasen.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("21", "Dr. Aniket Chakrabarty ",
                "Assistant Professor", "Earth and Climate Science",
                "aniketc@iisertirupati.ac.in", "08772500448",
                "http://www.iisertirupati.ac.in/people/faculty/aniket.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("22", "Dr. Sai Kranthi",
                "Inspire Faculty", "Earth and Climate Science",
                "saikranthi@iisertirupati.ac.in", "08772500239",
                "http://www.iisertirupati.ac.in/people/faculty/saikranthi.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("23", "Dr. P Lakshmana Rao",
                "Assistant Professor (On Contract)", "English (HSS)",
                "lakshman@iisertirupati.ac.in", "08772500440",
                "http://www.iisertirupati.ac.in/people/faculty/lakshman.php",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("24", "Prof. P. C. Deshmukh",
                "Visiting Professor", "Physics",
                "pcd@iisertirupati.ac.in", "08772500249",
                "",
                "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        //IISERApp.inset_complete_faculty_data(json_faculty, json_user_type);
        IISERApp.inset_complete_faculty_data(DTOFacultyProfileList, "1");


        IISERApp.set_session(IISERApp.SESSION_FACULTY_DATA_FLAG, "Y");
        bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, "");
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, "200");
        //if (IISERApp.get_session(IISERApp.SESSION_FACULTY_PROFILE).equalsIgnoreCase("Yes")) {
        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
        broadcast(context, "ActivityFacultyProfile", bundle);*/
        //}

        // SOFTCORE CODE
        bundle = new Bundle();
        RequestBody params = new FormEncodingBuilder()
                .build();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_faculty_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    String json_user_type = "";

                    JSONArray json_faculty = json.getJSONArray("faculty");
                    int len = json_faculty.length();
                    if (len > 0) {
                        TABLE_FACULTY_PROFILE.delete_tbl_faculty_profile();
                        IISERApp.log(LOG_TAG, "faculty Data:" + json_faculty);
                        IISERApp.inset_complete_faculty_data(json_faculty, json_user_type);
                    }

                    IISERApp.set_session(IISERApp.SESSION_FACULTY_DATA_FLAG, "Y");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    if (IISERApp.get_session(IISERApp.SESSION_FACULTY_PROFILE).equalsIgnoreCase("Yes")) {
                        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "3");
                        broadcast(context, "ActivityFacultyProfile", bundle);

                    } else {
                        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
                        broadcast(context, "ActivityFacultyProfile", bundle);
                    }
                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    broadcast(context, "ActivityFacultyProfile", bundle);
                }
            } catch (JSONException e) {

            }
        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, " " + "ErrorCode:" + dtoService.getStr_exception());
        }

    }

    private void send_selected_course_to_server(Bundle extras) {
        IISERApp.log(LOG_TAG, "In get_exam_schedule_data_from_server");
        IISERApp.log(LOG_TAG, "semester_name:" + IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME));

        RequestBody params = new FormEncodingBuilder()
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                //.add("semester_name", IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME))
                .add("course_json", extras.getString("course_json"))
                .build();

        IISERApp.log(LOG_TAG, "IISERApp.get_session(IISERApp.SESSION_USER_ID)" + IISERApp.get_session(IISERApp.SESSION_USER_ID));

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_send_selected_course, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {
                    TABLE_COURSE.updateSelectedCourse("N");

                    SQLiteDatabase db = IISERApp.db.getWritableDatabase();
                    db.beginTransaction();

                    String sql = TABLE_COURSE.QUERY_UPDATE;
                    SQLiteStatement statement = db.compileStatement(sql);

                    IISERApp.log(LOG_TAG, "updatedd->" + sql);

                    JSONArray jArry_course = new JSONArray(extras.getString("course_json"));
                    for (int i = 0; i < jArry_course.length(); i++) {
                        IISERApp.log(LOG_TAG, "In for->");
                        JSONObject json_corse_object = jArry_course.getJSONObject(i);
                        String course_id1 = json_corse_object.getString("course_id");
                        // String course_id = jArry_course.getString(i);
                        statement.clearBindings();
                        statement.bindString(1, course_id1);
                        statement.execute();
                    }

                    IISERApp.log(LOG_TAG, "EndTime->");
                    //jArry_course.setChecked(true);
                    db.setTransactionSuccessful();
                    db.endTransaction();

                    // bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, dtoService.g() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
                    broadcast(context, "ActivityCourseSelection", bundle);

                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ASSIGNMENT);
                    intent1.putExtra("Activity_name", "ActivityCourseSelection");
                    context.startService(intent1);
                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, json.getString("response_status"));
                    broadcast(context, "ActivityCourseSelection", bundle);
                }
            } catch (JSONException e) {
                IISERApp.log(LOG_TAG, "send_selected_course_to_server(), ERROR-> " + e.getMessage());
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
    }

    private void update_user_profile_toserver(Bundle extras) {
        RequestBody params = new FormEncodingBuilder()
                .add("user_type", extras.getString("user_type"))
                .add("user_id", extras.getString("user_id"))
                .add("name", extras.getString("name"))
                .add("email_id", extras.getString("email_id"))
                .add("mobile_no", extras.getString("mobile_no"))
                .add("username", extras.getString("username"))
                .add("password", extras.getString("password"))
                .add("roll_no", extras.getString("roll_no"))
                .add("semester_name", extras.getString("semester_name"))
                .add("batch", extras.getString("batch"))
                .add("photo_url", extras.getString("photo_url"))
                .add("degree", extras.getString("degree"))
                .add("designation", extras.getString("designation"))
                .add("research", extras.getString("research"))
                .add("personal_page", extras.getString("personal_page"))
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_update_user_data, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    SQLiteDatabase db = IISERApp.db.getWritableDatabase();
                    db.beginTransaction();

                    String sql = TABLE_USER_PROFILE.QUERY_UPDATE;
                    SQLiteStatement statement = db.compileStatement(sql);

                    statement.clearBindings();
                    statement.bindString(1, extras.getString("user_id"));
                    statement.bindString(2, extras.getString("name"));
                    statement.bindString(3, extras.getString("roll_no"));
                    statement.bindString(4, extras.getString("semester_name"));
                    statement.bindString(5, extras.getString("email_id"));
                    statement.bindString(6, extras.getString("mobile_no"));
                    statement.bindString(7, extras.getString("username"));
                    statement.bindString(8, extras.getString("password"));
                    statement.bindString(9, extras.getString("photo_url"));
                    statement.bindString(10, "");
                    statement.bindString(11, extras.getString("user_type"));
                    statement.bindString(12, "");
                    statement.bindString(13, extras.getString("batch"));
                    statement.bindString(14, extras.getString("degree"));
                    statement.bindString(15, extras.getString("designation"));
                    statement.bindString(16, extras.getString("research"));
                    statement.bindString(17, extras.getString("personal_page"));
                    statement.execute();

                    IISERApp.log(LOG_TAG, "EndTime->");
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    /*IISERApp.set_session(IISERApp.SESSION_EVENT_FLAG, "Y");*/
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
                    broadcast(context, "ActivityProfile", bundle);
                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    broadcast(context, "ActivityProfile", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        // broadcast(context, "ActivityProfile", bundle);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            //InputStream is = getApplicationContext().getAssets().open("json_course.json");
            InputStream is = getApplicationContext().getAssets().open("new_course_json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void insert_hardcode_course_data_frm_json() {

        try {
            SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_COURSE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);

            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("course_data");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> course_data;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                IISERApp.log(LOG_TAG, "course_data-->" + jo_inside.getString("Code") + " course_name:" + jo_inside.getString("Name"));
                String Code = jo_inside.getString("Code");
                String Name = jo_inside.getString("Name");
                String Credit = jo_inside.getString("Credit");
                String Semester = jo_inside.getString("Semister");
                String Subject_name = jo_inside.getString("Subject");

                statement.clearBindings();
                statement.bindString(1, Code);
                statement.bindString(2, Name);
                statement.bindString(3, Semester);
                statement.bindString(4, Subject_name);
                statement.bindString(5, Credit);
                statement.bindString(6, "");
                statement.bindString(7, "");
                statement.execute();
            }

            IISERApp.log(LOG_TAG, "EndTime->");
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        IISERApp.set_session(IISERApp.INTENT_FLAG_INSERT_COURSE_DATA, "Y");
        // broadcast(context, "ActivityTabHost",new Bundle());
    }


    private void get_assignment_notification_data() {
        IISERApp.log(LOG_TAG, "In get_assignment_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .add("assignment_id", assignment_id)
                .add("course_id", course_id)
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_assignment_notification_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    IISERApp.parse_assignmentnotification_data(json);
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");

                    int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                    IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                    notification_id = 1000 + notification_id;
                    generateNoticesNotification(notification_id, assignment_id);
                    Bundle extras = new Bundle();
                    //IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                    broadcast(context, "ActivityAssignment", bundle);

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        /*  broadcast(context, "ActivityAssignment", bundle);*/
    }


    private void get_assignment_data(String activity_name) {
        IISERApp.log(LOG_TAG, "In get_assignment_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .add("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE))
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                .add("semester_name", IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME))
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_assignment_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = json.getJSONArray("list");
                    int len = jsonArray.length();
                    if (len > 0) {
                        if (activity_name.equalsIgnoreCase("ActivityAssignment")) {

                            TABLE_ASSIGNMENT.delete_tbl_assignment_sync();
                        }
                        IISERApp.parse_assignment_data_and_insert(jsonArray);

                    }
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");


                    if (activity_name.equalsIgnoreCase("ActivityAssignment")) {
                        broadcast(context, "ActivityAssignment", bundle);

                        /*Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, ActivityAssignment.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ASSIGNMENT);
                        intent1.putExtra("Activity_name", "ActivityAssignment");
                        context.startService(intent1);*/
                    } else {
                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
                        intent1.putExtra("Activity_name", "ActivityCourseSelection");
                        context.startService(intent1);
                        //broadcast(context, "ActivityLogin", bundle);
                    }
                    //  broadcast(context, "ActivityCourseSelection", bundle);


                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    // bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, json.getString("response_status"));
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");


                    //broadcast(context, "ActivityCourseSelection", bundle);
//                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
//                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
//                    broadcast(context, "ActivityLogin", bundle);
//

                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);

                } /*else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
                    broadcast(context, "ActivityLogin", bundle);
                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
                    context.startService(intent1);

                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        if (activity_name.equalsIgnoreCase("ActivityCourseSelection"))
            broadcast(context, "ActivityCourseSelection", bundle);
        else
            broadcast(context, "ActivityLogin", bundle);
    }

    private void get_timetable_data(String activity_name) {

        TABLE_TIMETABLE.delete_tbl_timetable_sync();

        IISERApp.log(LOG_TAG, "In get_timetable_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .add("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE))
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                .add("batch", IISERApp.get_session(IISERApp.SESSION_BATCH))
                //.add("semester_name", IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME))
                .build();

        IISERApp.log(LOG_TAG, "value of SESSION_USER_TYPE: " + IISERApp.get_session(IISERApp.SESSION_USER_TYPE));
        IISERApp.log(LOG_TAG, "value of SESSION_USER_ID: " + IISERApp.get_session(IISERApp.SESSION_USER_ID));
        IISERApp.log(LOG_TAG, "value of SESSION_BATCH: " + IISERApp.get_session(IISERApp.SESSION_BATCH));

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_timetable_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = json.getJSONArray("list");
                    int len = jsonArray.length();
                    if (len > 0) {
                        if (activity_name.equalsIgnoreCase("ActivityTimeTable")) {

                            TABLE_TIMETABLE.delete_tbl_timetable_sync();
                        }
                        IISERApp.parse_timetable_data_and_insert(jsonArray);
                    }

                    // bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    //  bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, json.getString("response_status"));

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
                    // broadcast(context, "ActivityCourseSelection", bundle);

                    /*Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                   // intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);
*/


                    if (activity_name.equalsIgnoreCase("ActivityTimeTable")) {
                        broadcast(context, "ActivityTimeTable", bundle);

                       /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, ActivityTimeTable.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
                        intent1.putExtra("Activity_name", "ActivityTimeTable");
                        context.startService(intent1);*/
                    } else if
                    (activity_name.equalsIgnoreCase("ActivityProfile")) {
                        broadcast(context, "ActivityProfile", bundle);
                    } else {
                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                        intent1.putExtra("Activity_name", "ActivityCourseSelection");
                        context.startService(intent1);
                        //broadcast(context, "ActivityLogin", bundle);
                    }
                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, json.getString("response_status"));
                    //  broadcast(context, "ActivityExamSchedule", bundle);
//                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
//                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
//                    broadcast(context, "ActivityLogin", bundle);
//
                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);
                    //  bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    //  bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        // broadcast(context, "ActivityLogin", bundle);
        // broadcast(context, "ActivityCourseSelection", bundle);
        if (activity_name.equalsIgnoreCase("ActivityCourseSelection"))
            broadcast(context, "ActivityCourseSelection", bundle);
        else
            broadcast(context, "ActivityLogin", bundle);

    }


    private void get_timetable_notification_data(String sem_name) {
        IISERApp.log(LOG_TAG, "In get_timetable_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .add("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE))
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                //.add("semester_name", sem_name)
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_timetable_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = json.getJSONArray("list");
                    IISERApp.log(LOG_TAG, "data notification: " + jsonArray);

                    int len = jsonArray.length();
                    if (len > 0) {
                        TABLE_TIMETABLE.delete_tbl_timetable_sync();
                        IISERApp.parse_timetable_data_and_insert(jsonArray);
                    }

                  /*  if (len > 0) {

                       // IISERApp.parse_timetable_data_and_insert(jsonArray);

                    }
                    else {
                        TABLE_TIMETABLE.delete_tbl_timetable_sync();

                    }*/


                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");

                    int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                    IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                    notification_id = 1000 + notification_id;
                    // IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                    generateNoticesNotification1(notification_id, dtoService.getStr_response_mesaage(),
                            "ActivityTabHost_AfterLogin", "Timetable");
                    Bundle extras = new Bundle();
                    broadcast(context, "ActivityTimeTable", bundle);

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        //  broadcast(context, "ActivityTimeTable", bundle);
    }

    private void get_exam_schedule_data(String activity_name) {
        IISERApp.log("!!!!!!!", "In get_exam_schedule_data_from_server");
        IISERApp.log(LOG_TAG, "In get_exam_schedule_data_from_server");
//        IISERApp.log(LOG_TAG, "In exam_schedule_para->"+IISERApp.get_session(IISERApp.SESSION_USER_TYPE)+"-"
//                +IISERApp.get_session(IISERApp.SESSION_USER_ID)+"-"+IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME));

        RequestBody params = new FormEncodingBuilder()
                .add("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE))
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                //.add("semester_name", IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME))
                .build();
        Log.d("", "SESSION_USER_ID: "+IISERApp.get_session(IISERApp.SESSION_USER_ID));
        Log.d("", "SESSION_USER_TYPE: "+IISERApp.get_session(IISERApp.SESSION_USER_TYPE));

        IISERApp.log(LOG_TAG, "activity_name : " + activity_name);
        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_exam_schedule_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        Log.d("", "getStr_response_code: "+ dtoService.getStr_response_code());
        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
      //  if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                Log.d("", "get_exam_schedule_data_json: "+json);

                if (json.getString("response_status").equalsIgnoreCase("1")) {


                    IISERApp.log(LOG_TAG, "exam_schedule: " + json);
                    JSONArray jsonArray = json.getJSONArray("exam_schedule");
                    int len = jsonArray.length();
                    if (len > 0) {
                        if (activity_name.equalsIgnoreCase("ActivityExamSchedule")) {
                            TABLE_EXAM_SCHEDULE.delete_tbl_exam_shedule_sync();
                        }
                        IISERApp.parse_exam_schedule_data_and_insert(jsonArray);

                    }
                    IISERApp.log("123", "In if of get_exam_schedule_data_from_server");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");

                    if (activity_name.equalsIgnoreCase("ActivityExamSchedule")) {
                        broadcast(context, "ActivityExamSchedule", bundle);

                       /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, ActivityExamSchedule.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                        intent1.putExtra("Activity_name", "ActivityTimeTable");
                        context.startService(intent1);*/
                    } else {

                        // intent call for student attendence
                        if(IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
                        {
                           // service for faculty attendence
                            Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_FACULTY_ATTENDENCE);
                           // intent1.putExtra("Activity_name", "ActivityCourseSelection");
                            context.startService(intent1);
                            //broadcast(context, "ActivityLogin", bundle);
                            broadcast(context, "ActivityLogin", bundle);
                        }else {
                            Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_STUD_ATTENDENCE);
                         //   intent1.putExtra("Activity_name", "ActivityCourseSelection");
                            context.startService(intent1);
                            //broadcast(context, "ActivityLogin", bundle);
                            broadcast(context, "ActivityLogin", bundle);
                        }
                    }
                    // broadcast(context, "ActivityLogin", bundle);

                    /*Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_NOTICE);
                    context.startService(intent1);*/


                    IISERApp.log("1234", "going from In if of get_exam_schedule_data_from_server");

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
                    broadcast(context, "ActivityLogin", bundle);

                    // intent call for student attendence
                    if(IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
                    {
                        // service for faculty attendence
                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_FACULTY_ATTENDENCE);
                        context.startService(intent1);
                    }else {
                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_STUD_ATTENDENCE);
                        context.startService(intent1);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

      /*  } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }*/
        if (activity_name.equalsIgnoreCase("ActivityCourseSelection"))
            broadcast(context, "ActivityCourseSelection", bundle);
        else
            broadcast(context, "ActivityLogin", bundle);

        /*broadcast(context, "ActivityCourseSelection", bundle);*/
    }



    private void get_exam_schedule_notification_data(String sem_name) {
        IISERApp.log(LOG_TAG, "In get_exam_schedule_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .add("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE))
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                .add("semester_name", sem_name)
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_exam_schedule_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    IISERApp.log(LOG_TAG, "exam_schedule: " + json);
                    JSONArray jsonArray = json.getJSONArray("exam_schedule");
                    int len = jsonArray.length();
                    if (len > 0) {

                        //TABLE_EXAM_SCHEDULE.delete_tbl_exam_shedule_sync();
                        IISERApp.parse_exam_schedule_data_and_insert(jsonArray);
                    }

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    //  bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
                    int notification_id = IISERApp.get_Intsession(IISERApp.NOTIFICATION_ID);
                    IISERApp.set_session(IISERApp.NOTIFICATION_ID, (notification_id + 1) + "");
                    notification_id = 1000 + notification_id;
                    generateNoticesNotification1(notification_id, dtoService.getStr_response_mesaage(),
                            "ActivityTabHost_AfterLogin", "Examschedule");
                    Bundle extras = new Bundle();
                    broadcast(context, "ActivityExamSchedule", bundle);

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        /* broadcast(context, "ActivityLogin", bundle);*/
    }

    private void get_notice_data(String activity_name) {
        IISERApp.log(LOG_TAG, "In get_notice_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .add("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE))
                .add("user_id", IISERApp.get_session(IISERApp.SESSION_USER_ID))
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_notice_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");

        Log.d("", "getStr_response_code200: "+dtoService.getStr_response_code());

        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                IISERApp.log(LOG_TAG, "response_statusRaj="+json.getString("response_status"));
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = json.getJSONArray("notices");
                    int len = jsonArray.length();
                    if (len > 0) {

                        if (activity_name.equalsIgnoreCase("ActivityNotice")) {
                            TABLE_NOTICE.delete_notice();
                        }
                        IISERApp.parse_notice_data_and_insert(jsonArray);
                    }

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
//                    broadcast(context, "ActivityLogin", bundle);

                    if (activity_name.equalsIgnoreCase("ActivityNotice")) {
                        broadcast(context, "ActivityNotice", bundle);

                     /*   Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, ActivityNotice.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_NOTICE);
                        intent1.putExtra("Activity_name", "ActivityNotice");
                        context.startService(intent1);*/
                    } else {
                        //  bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ACADEMIC_CALENDER);
                        context.startService(intent1);
                    }

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
//                    broadcast(context, "ActivityLogin", bundle);
                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ACADEMIC_CALENDER);
                    context.startService(intent1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }
        if (activity_name.equalsIgnoreCase("ActivityNotice"))
            broadcast(context, "ActivityNotice", bundle);
        else
            broadcast(context, "ActivityLogin", bundle);
    }

    private void get_academic_calender_data() {
        IISERApp.log(LOG_TAG, "In get_notice_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_academic_cal, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {
            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = json.getJSONArray("academic");
                    int len = jsonArray.length();
                    if (len > 0) {
                        TABLE_ACADMIC_CALENDER.delete_tbl_aca_cal_sync();

                        TABLE_ACADMIC_CALENDER.parse_notice_data_and_insert(jsonArray);
                    }


                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
//                    broadcast(context, "ActivityLogin", bundle);

                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ASSIGNMENT);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);




                   /* bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
                   broadcast(context, "ActivityLogin", bundle);*/
                   /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ACADEMIC_CALENDER);
                    context.startService(intent1);*/


                } else {

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "2");
//                    broadcast(context, "ActivityLogin", bundle);

                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ASSIGNMENT);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);


                    /*bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    broadcast(context, "ActivityLogin", bundle);
*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

        broadcast(context, "ActivityLogin", bundle);
    }


    private void get_event_data() {

        IISERApp.log(LOG_TAG, "In get_event_data_from_server");
        RequestBody params = new FormEncodingBuilder()
                .build();

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_event_list, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        if (dtoService.getStr_response_code() == 200) {

            try {
                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = json.getJSONArray("list");
                    int len = jsonArray.length();
                    if (len > 0) {
                        TABLE_EVENT.delete_tbl_event_sync();
                        IISERApp.parse_event_data_and_insert(jsonArray);
                    }
                    IISERApp.set_session(IISERApp.SESSION_EVENT_FLAG, "Y");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
                    broadcast(context, "ActivityEvents", bundle);

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    broadcast(context, "ActivityEvents", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

        // broadcast(context, "ActivityEvents", bundle);
    }


    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

    private void insert_user_data() {
        IISERApp.log(LOG_TAG, "In get_user_data_from_server");
        IISERApp.log(LOG_TAG, "device_id:" + IISERApp.get_session(IISERApp.SESSION_DEVICE_ID));
        IISERApp.log(LOG_TAG, "gcmId:" + IISERApp.regid);
        IISERApp.log(LOG_TAG, "phone_release:" + Build.VERSION.RELEASE);
        IISERApp.log(LOG_TAG, "phone_sdk:" + String.valueOf(Build.VERSION.SDK_INT));
        RequestBody params = new FormEncodingBuilder()
                .add("username", IISERApp.get_session(IISERApp.SESSION_USERNAME))
                .add("password", IISERApp.get_session(IISERApp.SESSION_PASSWORD))
               // .add("gcm_id", IISERApp.regid)
                .add("gcm_id", refreshedToken)
                .add("device_id", IISERApp.get_session(IISERApp.SESSION_DEVICE_ID))
                .add("phone_brand", Build.BRAND)
                .add("phone_device", Build.DEVICE)
                .add("phone_model", Build.MODEL)
                .add("phone_sdk", String.valueOf(Build.VERSION.SDK_INT))
                .add("phone_release", Build.VERSION.RELEASE)
                .add("device_type", "Android")
                .build();

        IISERApp.log(LOG_TAG, "insert_user_data(), params -> "+params.toString());
        IISERApp.log(LOG_TAG, "insert_user_data(), username -> "+IISERApp.get_session(IISERApp.SESSION_USERNAME));
        IISERApp.log(LOG_TAG, "insert_user_data(), password -> "+IISERApp.get_session(IISERApp.SESSION_PASSWORD));
        IISERApp.log(LOG_TAG, "insert_user_data(), gcm_id -> "+refreshedToken);
        IISERApp.log(LOG_TAG, "insert_user_data(), device_id -> "+IISERApp.get_session(IISERApp.SESSION_DEVICE_ID));
        IISERApp.log(LOG_TAG, "insert_user_data(), phone_brand -> "+Build.BRAND);
        IISERApp.log(LOG_TAG, "insert_user_data(), phone_device -> "+Build.DEVICE);
        IISERApp.log(LOG_TAG, "insert_user_data(), phone_model -> "+Build.MODEL);
        IISERApp.log(LOG_TAG, "insert_user_data(), phone_sdk -> "+String.valueOf(Build.VERSION.SDK_INT));
        IISERApp.log(LOG_TAG, "insert_user_data(), phone_release -> "+Build.VERSION.RELEASE);
        IISERApp.log(LOG_TAG, "insert_user_data(), device_type -> "+"Android");

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_login_details, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
        if (dtoService.getStr_response_code() == 200) {
            try {

                IISERApp.log(LOG_TAG, "Response is " + dtoService.getStr_response_body());

                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    String json_user_type = json.getString("user_type");
                    IISERApp.set_session(IISERApp.SESSION_USER_TYPE, json_user_type);

                    if (json_user_type.equalsIgnoreCase("student")) {
                        JSONObject json_student = json.getJSONObject("student");
                        IISERApp.log(LOG_TAG, "Student Data:" + json_student);
                        IISERApp.inset_student_data_from_server(json, json_user_type);

                    } else if (json_user_type.equalsIgnoreCase("faculty")) {
                        JSONObject json_faculty = json.getJSONObject("faculty");
                        IISERApp.log(LOG_TAG, "faculty Data:" + json_faculty);
                        IISERApp.inset_faculty_data_from_server(json_faculty, json_user_type);
                    } else if (json_user_type.equalsIgnoreCase("supervisor")) {
                        JSONObject json_supervisor = json.getJSONObject("supervisor");
                        IISERApp.log(LOG_TAG, "faculty Data:" + json_supervisor);
                        IISERApp.inset_supervisor_data_from_server(json_supervisor, json_user_type);
                    }

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");

                    //  broadcast(context, "ActivityLogin", bundle);

                    //  updateMyActivity(context, "2", IISERApp.BUNDLE_RESPONSE_MESSAGE,"ActivityLogin");
                /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                   intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                   context.startService(intent1);*/

                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_NOTICE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    //broadcast(context, "ActivityLogin", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

        broadcast(context, "ActivityLogin", bundle);

    }

    private void getStudenetAttendenceFromserver() {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        /*RequestBody params = new FormEncodingBuilder()
                .build();*/
        JSONObject json1= new JSONObject();
        try {
            json1.put("roll_number",IISERApp.get_session(IISERApp.SESSION_USERNAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody params = RequestBody.create(JSON, json1.toString());
        /*Request request = new Request.Builder()
                .build();
*/
   /*     RequestBody params = new FormEncodingBuilder()
                .add("roll_number", IISERApp.get_session(IISERApp.SESSION_USERNAME))
                .build();*/

        IISERApp.log(LOG_TAG, "insert_user_data(), params -> "+params.toString());

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_stud_attendence, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
        if (dtoService.getStr_response_code() == 200) {
            try {

                IISERApp.log(LOG_TAG, "Response is " + dtoService.getStr_response_body());

                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    String json_roll_number = json.getString("roll_number");
                    //  IISERApp.set_session(IISERApp.SESSION_USER_TYPE, json_user_type);
                    TABLE_ATTENDENCE_MASTER.deleteAllRecord();
                    TABLE_STUDENT_ATTENDENCE.deleteAllRecord();
                    JSONArray courseArray = json.getJSONArray("course_info");
                    for (int i = 0; i < courseArray.length();i++)
                    {
                        JSONObject jsonObj = courseArray.getJSONObject(i);
                        String coursecode = jsonObj.getString("course_code");
                        String tot_no_classes = jsonObj.getString("tot_no_classes");
                        String attended_classes = jsonObj.getString("attended_classes");
                        String average_attendance = jsonObj.getString("average_attendance");
                        TABLE_ATTENDENCE_MASTER.insertAttendenceMaster(coursecode,tot_no_classes,attended_classes,average_attendance);
                        JSONArray presentJsonArray = jsonObj.getJSONArray("present");
                        for (int j =0; j < presentJsonArray.length();j++)
                        {
                            JSONObject jsonObj_present = presentJsonArray.getJSONObject(j);
                            String date = jsonObj_present.getString("date");
                            String course_code = jsonObj_present.getString("course_code");
                            String student_present = jsonObj_present.getString("student_present");
                            String lecture_present = jsonObj_present.getString("lecture_present");
                            TABLE_STUDENT_ATTENDENCE.insertStudentAttendence(date,course_code,student_present,lecture_present);
                        }
                    }

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");

                    //  broadcast(context, "ActivityLogin", bundle);

                    //  updateMyActivity(context, "2", IISERApp.BUNDLE_RESPONSE_MESSAGE,"ActivityLogin");
                /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                   intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                   context.startService(intent1);*/

                    /*Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_NOTICE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);*/

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    //broadcast(context, "ActivityLogin", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

        broadcast(context, "ActivityLogin", bundle);

    }


    private void  getFacultyAttendenceFromserver() {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        /*RequestBody params = new FormEncodingBuilder()
                .build();*/
        JSONObject json1= new JSONObject();
        try {
            json1.put("username",IISERApp.get_session(IISERApp.SESSION_USERNAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody params = RequestBody.create(JSON, json1.toString());
        /*Request request = new Request.Builder()
                .build();
*/
   /*     RequestBody params = new FormEncodingBuilder()
                .add("roll_number", IISERApp.get_session(IISERApp.SESSION_USERNAME))
                .build();*/

        IISERApp.log(LOG_TAG, "insert_user_data(), params -> "+params.toString());

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_faculty_attendence, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
        if (dtoService.getStr_response_code() == 200) {
            try {

                IISERApp.log(LOG_TAG, "Response is facultly Attandance " + dtoService.getStr_response_body());

                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                 //   String json_roll_number = json.getString("roll_number");
                    //  IISERApp.set_session(IISERApp.SESSION_USER_TYPE, json_user_type);
                    TABLE_FACULTY_ATTENDENCE.deleteAllRecord();
                    JSONArray courseArray = json.getJSONArray("faculty_info");

                    for (int i = 0; i < courseArray.length();i++)
                    {
                        String roll_number = "",
                                batch = "-",
                                average_attendance ="";
                        JSONObject jsonObj = courseArray.getJSONObject(i);
                        String coursecode = jsonObj.getString("course_code");
                        if (jsonObj.has("roll_number")) {
                             roll_number = jsonObj.getString("roll_number");
                          //   batch = jsonObj.getString("batch");
                          //   average_attendance = jsonObj.getString("average_attendance");
                        }
                       /* String roll_number = jsonObj.getString("roll_number");
                        String batch = jsonObj.getString("batch");
                        String average_attendance = jsonObj.getString("average_attendance");*/
                        TABLE_FACULTY_ATTENDENCE.insertFacultyAttendenceMaster(coursecode,roll_number,batch,average_attendance);
                       // JSONArray presentJsonArray = jsonObj.getJSONArray("present");
                     /*   for (int j =0; j < presentJsonArray.length();j++)
                        {
                            JSONObject jsonObj_present = presentJsonArray.getJSONObject(j);
                            String date = jsonObj_present.getString("date");
                            String course_code = jsonObj_present.getString("course_code");
                            String student_present = jsonObj_present.getString("student_present");
                            String lecture_present = jsonObj_present.getString("lecture_present");
                            TABLE_STUDENT_ATTENDENCE.insertStudentAttendence(date,course_code,student_present,lecture_present);
                        }*/
                    }

                    JSONArray mentor_phdArray = json.getJSONArray("mentor_phd");
                    for (int i = 0; i < mentor_phdArray.length();i++)
                    {
                        JSONObject jsonObj = mentor_phdArray.getJSONObject(i);
                        String roll_number1 = jsonObj.getString("roll_number");
                        Log.d("", "getFacultyAttendenceFromserver_RollNo: "+roll_number1);
                        TABLE_FACULTY_ATTENDENCE.insertFacultyAttendenceMaster("MENTOR PhD",roll_number1,"","");
                    }

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");

                    //  broadcast(context, "ActivityLogin", bundle);

                    //  updateMyActivity(context, "2", IISERApp.BUNDLE_RESPONSE_MESSAGE,"ActivityLogin");
                /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                   intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                   context.startService(intent1);*/

                    /*Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_NOTICE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);*/

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    //broadcast(context, "ActivityLogin", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

        broadcast(context, "ActivityLogin", bundle);

    }

    static void updateMyActivity(Context context, String response_code, String response_message, String flag) {

        Intent intent = new Intent(flag);
        //put whatever data you want to send, if any
        intent.putExtra("response_code", response_code);
        intent.putExtra("response_message", response_message);
        //send broadcast
        context.sendBroadcast(intent);

    }

    static void broadcast(Context context, String flag, Bundle bundle) {
        IISERApp.log(LOG_TAG, "in update activity->" + flag);
        Intent intent = new Intent(flag);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        //IISERApp.log(LOG_TAG, "onDestroy123->");
        super.onDestroy();
        IISERApp.log(LOG_TAG, "onDestroy->");

    }
}
