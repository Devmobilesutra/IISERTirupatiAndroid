package com.mobilesutra.iiser_tirupati.Config;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mobilesutra.iiser_tirupati.Activities.ActivityLogin;
import com.mobilesutra.iiser_tirupati.Activities.ActivityMobileNo;
import com.mobilesutra.iiser_tirupati.Database.Database;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ASSIGNMENT;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EVENT;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SCHEDULE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_PROFILE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_NOTICE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_USER_PROFILE;
import com.mobilesutra.iiser_tirupati.Model.DTOResponse;
import com.mobilesutra.iiser_tirupati.Model.DTOService;
import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Profile;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okio.Buffer;

/**
 * Created by kalyani on 05/04/2016.
 */
public class IISERApp extends Application {

    private static final String LOGTAG = "IISERApp", TAG = "IISERApp";
    public static final String SESSION_OTP = "session_otp";
    public static final String SESSION_NEW_PASSWORD = "session_new_password";

    // Session Variables
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    static Context context;
    public static LinkedHashMap<String, String> selected_course_lhm = new LinkedHashMap<>();
    String PREFS_NAME = "jdviudsfb4r4327_Sdfj";
    public static String SessionKey = "j5aD9uweHEAncbhd";// Must have 16

    public static String
            BUNDLE_RESPONSE_CODE = "response_code",
            BUNDLE_EXCEPTION = "exception",
            BUNDLE_RESPONSE_STATUS = "response_status",
            BUNDLE_RESPONSE_MESSAGE = "response_message";

    public static AESAlgorithm aes;

    public static String regid = "";
    public static final String GCM_SENDER_ID ="578083304270"; //"800008097460";//"578083304270";
    public static final String PREFS_PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
   // GoogleCloudMessaging gcm;

    public static Database db;

    public static String
            INTENT_FLAG = "intent_flag",
            LOGIN_FLAG = "login_flag",
            FLAG_DISCIPLINE = "flag_discipline",
            INTENT_FLAG_LOGIN = "intent_flag_login",
            INTENT_FLAG_EVENT = "intent_flag_event",
            INTENT_FLAG_FACULTY = "intent_flag_faculty",
            INTENT_FLAG_NOTICE = "intent_flag_notice",
            INTENT_FLAG_EXAM_SCHEDULE = "intent_flag_exam_schedule",
            INTENT_FLAG_TIMETABLE = "intent_flag_timetable",
            INTENT_FLAG_ASSIGNMENT = "intent_flag_assignment",
            INTENT_FLAG_UPDATE_USER_DATA = "intent_flag_update_user_data",
            INTENT_FLAG_APPLY_SELECTED_COURSE = "intent_flag_apply_selected_course",
            INTENT_FLAG_ASSIGNMENT_NOTIFICATION = "intent_flag_assignment_notification",
            INTENT_FLAG_GET_COURSE_DATA = "intent_flag_get_coursedata",
            INTENT_FLAG_ACADEMIC_CALENDER = "intent_flag_get_academic_calender",
            INTENT_FLAG_INSERT_COURSE_DATA = "intent_flag_insert_course_data",
            INTENT_FLAG_SEND_ASSIGNMENT = "send_assignment_to_server",
            INTENT_FLAG_FACULTY_ATTENDENCE="intent_flag_faculty_attendence",
            INTENT_FLAG_STUD_ATTENDENCE="intent_flag_studenet_attendence";
    ///  INTENT_FLAG_ASSIGNMENT_ADDED ="new _faculty assignment added";


    public static String

            //local_url = "http://192.168.0.245/Iiser/isser/",
            //local_url="http://192.168.0.240/isser/index.php/",
            local_url = "http://192.168.0.242/ms-projects/mobilesutra.com/iiser/index.php/",

    //  server_url = "http://pkclasses.co.in/",
    server_url = "http://mobilesutra.com/iiser/index.php/",
    server_url_for_attendence ="http://mobilesutra.com/iiser/",

    //url = server_url,
    url = server_url,
    // url_get_login_details = url + "ms-projects/mobilesutra.com/isser/service/login/authenticate",
    url_get_login_details = url + "service/login/authenticate",
            url_get_event_list = url + "service/event",
            url_get_notice_list = url + "service/notice",
            url_get_exam_schedule_list = url + "service/exam/schedule",
            url_get_timetable_list = url + "service/timetable",
            url_get_assignment_list = url + "service/assignment",
            url_get_assignment_notification_list = url + "",
            url_update_user_data = url + "service/user/save_profile",
            url_send_selected_course = url + "service/course/apply",
            url_get_academic_cal = url + "service/Academic",
            url_get_faculty_list = url + "service/Faculty",
            url_send_assignment = url + "service/Assignment/upload_assignment",
            url_get_mobile = url + "service/User/check_mobile",
            url_get_otp = url + "service/User/send_otp",
          // get_check_user = url + "check_user",
            url_get_stud_attendence = url + "service/Attendance/attendance",
            url_get_faculty_attendence = url + "service/Faculty_attendance/get_faculty_attendance",
            url_save_password = url + "service/User/save_password";

    public static String
            get_check_user =" http://mobilesutra.com/iiser/service/User/check_user";
    public static final String
            PACKAGE_NAME = "com.mobilesutra.iiser_tirupati",
            SESSION_USERNAME = "username",
            SESSION_PASSWORD = "password",
            SESSION_GCM_ID = "gcm_id",
            SESSION_DEVICE_ID = "device_id",
            SESSION_USER_TYPE = "user_type",
            SESSION_USER_ID = "user_id",
            SESSION_BATCH = "batch",
            SESSION_SEMESTER_NAME = "semester_name",
            SESSION_LOGIN_FLAG = "login_flag",
            SESSION_EVENT_FLAG = "event_flag",
            SESSION_FACULTY_DATA_FLAG = "faculty_data_flag",
            NOTIFICATION_ID = "notification_id",
            NOTIFICATION_FLAG = "notification_flag",
            SESSION_NOTIFICATION_FLAG = "session_notification_flag",
            SESSION_FRAGEMENT_FLAG = "session_fragement_flag",
            SESSION_FACULTY_PROFILE = "session_facuty_profile",
            SESSION_STUDENT_NAME = "student_name",
            SESSION_SUPERVISOR_NAME = "supervisor_name",
            SESSION_ASSIGNMENT_DOCUMENT_FLAG = "assignment_Flag",
            SESSION_ASSIGNMENT_DOCUMENT_URI = "assignment_document_uri",
            SESSION_IMAGE_STRING = "image_string",
            SESSION_DOCUMENT = "document_string",
            SESSION_MIME_TYPE = "mime_type",
            SESSION_OTPs= "otp",
         SESSION_LOGIN = "SESSION_LOGIN",
    SESSION_TAB_TITLE= "tab_title",
            SESSION_IMG_PATH = "mime_type";


    public static String AudioPathLocal = "/storage/emulated/0/IISERPUNE AUDIO/";

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        aes = new AESAlgorithm();

        db = new Database(context);

      //  gcm = GoogleCloudMessaging.getInstance(this);
        if (regid.length() == 0)
            regid = getRegistrationId(context);

        log(TAG, "gcm_id:" + regid);

    }

    public static void set_session(String key, String value) {
        Log.i("SetSession", "Key=" + key + "__value=" + value);
        String temp_key = aes.Encrypt(key);
        String temp_value = aes.Encrypt(value);
        IISERApp.editor.putString(temp_key, temp_value);
        IISERApp.editor.commit();

    }

    public static String get_session(String key) {
        String temp_key = aes.Encrypt(key);
        if (sharedPref.contains(temp_key)) {
            log(TAG, "GetSession->Key:" + key + "_Value:" + aes.Decrypt(sharedPref.getString(temp_key, "")));
            return aes.Decrypt(sharedPref.getString(temp_key, ""));
        } else
            return "";
    }

    public static int get_Intsession(String key) {
        String temp_key = aes.Encrypt(key);
        int value = 0;
        if (sharedPref.contains(temp_key)) {
            String str = aes.Decrypt(sharedPref.getString(temp_key, ""));
            value = convert_str_to_int(str);
        }
        log(TAG, "get_Intsession->key:" + key + "_value:" + value);
        return value;
    }

    public static  void delet_log_Details(){
        sharedPref.edit().clear().commit();

        log(TAG, "aaa"+IISERApp.get_session(SESSION_USERNAME));

        log(TAG, "bbb"+IISERApp.get_session(SESSION_PASSWORD));
    }

    public static boolean is_marshmellow() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static int convert_str_to_int(String str) {
        log(TAG, "convert_str_to_int");
        int value = 0;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException n) {
            log(TAG, "NumberFormatException->" + n);
        } catch (NullPointerException n) {
            log(TAG, "NullPointerException->" + n);
        }
        return value;
    }

    public static void log(String TAG, String str) {
        if (str.length() > 4000) {
            Log.i(LOGTAG, TAG + ": " + str.substring(0, 4000));
            log(TAG, str.substring(4000));
        } else
            Log.i(LOGTAG, TAG + ": " + str);
    }

    public static void log_bundle(Bundle extras) {
        Log.i(LOGTAG, "In log_bundle");
        if (extras != null) {
            for (String key : extras.keySet()) {
                Log.d(LOGTAG, key + " = " + extras.get(key) + "\"");
            }
        } else {
            Log.i(LOGTAG, "Bundle->" + extras);
        }
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public static boolean hasNoghat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public final boolean isInternetAvailable() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public String getdialog_internetslow(Context context) {
        /* Toast.makeText(Login.this,
        "Internet Is Not Available..",Toast.LENGTH_LONG).show();*/
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Connection Error...")
                .setMessage("Internet connection is slow...")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Yes button clicked, do something

                    }
                }).show();
        return null;
    }

    public String getdialog_checkinternet(Context context) {
        // Toast.makeText(Login.this,
        // "Internet Is Not Available..",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Connection Error...")
                .setMessage(
                        "Please connect to internet to get data for the user")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Yes button clicked, do something

                    }
                }).show();
        return null;
    }

    public static String get_day_from_date(String date_value) {
        /*String input = "Jan,23,2014";*/
        String input = "2016-05-20";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(date_value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(new SimpleDateFormat("MMM").format(calendar.getTime()));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        int day_cal = calendar.get(Calendar.DATE);
        IISERApp.log("date", "is " + day_cal);
        return day;
    }


    public static int get_day_from_date_new(String date_value) {
        /*String input = "Jan,23,2014";*/
        String input = "2016-05-20";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(date_value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(new SimpleDateFormat("MMM").format(calendar.getTime()));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        int day_cal = calendar.get(Calendar.DATE);
        IISERApp.log("date", "is " + day_cal);
        return day_cal;
    }

    public static String get_month_from_date(String date_value) {
        /*String input = "Jan,23,2014";*/
        IISERApp.log(TAG, "get_month_from_date() date-> : " + date_value);
        String input = "2016-05-20";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(date_value);
            IISERApp.log(TAG, "get_month_from_date() date-> : " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(new SimpleDateFormat("MMM").format(calendar.getTime()));
        String month = String.valueOf(new SimpleDateFormat("MMM").format(calendar.getTime()));
        // String month = (String) android.text.format.DateFormat.format("MMM", date);
        log(TAG, "month of exam schedule: " + month);
        return month;
    }

    public static String get_weekday_from_date(String date_value) {
        /*String input = "Jan,23,2014";*/
        String input = "2016-05-20";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(date_value);
            log(TAG, "month of exam schedule1111: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(new SimpleDateFormat("EEEE").format(calendar.getTime()));
        String day = String.valueOf(new SimpleDateFormat("EEEE").format(calendar.getTime()));
        // String month = (String) android.text.format.DateFormat.format("MMM", date);
        log(TAG, "day of exam schedule: " + day);
        return day;
    }
    //////////////////////////rajani//////////////////
    public static String post_server_call1(String url, RequestBody formBody) {
        long REQ_TIMEOUT = 1800;
        IISERApp.log("","post_server_call Url:" + url);
        try {
            Buffer buffer = new Buffer();
            formBody.writeTo(buffer);
            log("","post_for_body:" + buffer.readUtf8().toString());
        } catch (IOException e) {
            log("","post_body_excepion:" + e.getMessage());
        }

        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(REQ_TIMEOUT, TimeUnit.SECONDS);
            client.setReadTimeout(REQ_TIMEOUT, TimeUnit.SECONDS);
            client.setWriteTimeout(REQ_TIMEOUT, TimeUnit.SECONDS);
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();


            Response response = client.newCall(request).execute();
            int status_code = response.code();
            boolean status = response.isSuccessful();
            IISERApp.log("","logtime:StatusCode:" + status_code);
            log("","logtime:ResponseStatus:" + status);
            String res = response.body().string();
            log("","logtime:ResponseSize:" + res.length());
            log("","logtime:Response:" + res);
            log("","post_server_callResponseStatus:" + response.isSuccessful());
            return res;
        } catch (Exception e) {
            log("","exception post_server_call " + e);
            return "2";
        }
    }
    /////////////////////////////////////////////////////////////////

    public static DTOService post_server_call(String url, RequestBody postBody) {
        IISERApp.log(TAG, "In post_server_call");
        IISERApp.log(TAG, "URL->" + url);

        long REQ_TIMEOUT = 300;
        long READ_TIMEOUT = 600;
        DTOService dtoService = new DTOService();

        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(REQ_TIMEOUT, TimeUnit.SECONDS);
            client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            com.squareup.okhttp.Request request = new Request.Builder()
                    .url(url)
                    .post(postBody)
                    .build();
            Response response = client.newCall(request).execute();
            IISERApp.log(TAG, "ResponseStatus:" + response.isSuccessful());
            IISERApp.log(TAG, "ResponseCode:" + response.code());
            String str_response_body = response.body().string();
            IISERApp.log(TAG, "str_response_body:" + str_response_body);
            if (!str_response_body.equalsIgnoreCase("")) {
                JSONObject jsonObject = new JSONObject(str_response_body);

                if (jsonObject.has("response_message")) {
                    String str_res_msg = jsonObject.getString("response_message");
                    dtoService.setStr_response_mesaage(str_res_msg);
                    IISERApp.log(TAG, "Response Message:" + str_res_msg);
                }
            }


            dtoService.setStr_response_code(response.code());
            dtoService.setStr_response_body(str_response_body);
        } catch (SocketTimeoutException e) {
            IISERApp.log(TAG, "SocketTimeoutException STE1->" + e + "");
            IISERApp.log(TAG, "SocketTimeoutException STE2->" + e.getMessage());
            IISERApp.log(TAG, "SocketTimeoutException STE3->" + e.getLocalizedMessage());
            dtoService.setStr_response_code(1);
            dtoService.setStr_response_body("");
            dtoService.setStr_exception(e.getMessage());
        } catch (Exception e) {
            IISERApp.log(TAG, "E1->" + e + "");
            IISERApp.log(TAG, "E2->" + e.getMessage());
            IISERApp.log(TAG, "E3->" + e.getLocalizedMessage());
            dtoService.setStr_response_code(2);
            dtoService.setStr_response_body("");
            dtoService.setStr_exception(e.getMessage());
        }
        return dtoService;
    }

    public static void inset_student_data_from_server(JSONObject json, String str_user_type) {
        try {

            IISERApp.log(TAG, "in insert student JSON->" + json);
            JSONObject json_student = json.getJSONObject("student");

            JSONObject json_student_info = null;
            if (json_student.has("student_info")) {
                json_student_info = json_student.getJSONObject("student_info");
            }

            if (json_student_info != null) {
                IISERApp.log(TAG, "json_student_info->" + json_student_info);
                SQLiteDatabase db = IISERApp.db.getWritableDatabase();
                db.beginTransaction();

                String sql = TABLE_USER_PROFILE.QUERY_INSERT;
                SQLiteStatement statement = db.compileStatement(sql);

                String str_id = json_student_info.getString("id");
                String str_name = json_student_info.getString("name");
                if (str_name.equalsIgnoreCase("null"))
                    str_name = "";
                IISERApp.log("abcc ", "USERNAME FROM SERVER->" + str_name);
                IISERApp.set_session(IISERApp.SESSION_STUDENT_NAME, str_name);
                IISERApp.log("abcc ", "in USER NAME IS :->" + IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME)));
                String str_roll_number = json_student_info.getString("roll_number");
                if (str_roll_number.equalsIgnoreCase("null"))
                    str_roll_number = "";
                IISERApp.log("abcc ", "in isser appis->" + str_roll_number);
                String str_semester_name = json_student_info.getString("semester");
                if (str_semester_name.equalsIgnoreCase("null"))
                    str_semester_name = "";
                String str_mobile_no = json_student_info.getString("mobile_no");
                if (str_mobile_no.equalsIgnoreCase("null"))
                    str_mobile_no = "";
                String str_email_id = json_student_info.getString("email_id");
                if (str_email_id.equalsIgnoreCase("null"))
                    str_email_id = "";
                String str_username = get_session(SESSION_USERNAME);
                String str_password = get_session(SESSION_PASSWORD);
                String str_student_name = get_session(SESSION_STUDENT_NAME);
                String str_photo_url = json_student_info.getString("photo");
                String str_batch = json_student_info.getString("batch");
                String str_updated_date = "04/05/2016";
                String str_is_sync = "N";

                statement.clearBindings();
                statement.bindString(1, str_id);
                statement.bindString(2, str_name);
                statement.bindString(3, str_roll_number);
                statement.bindString(4, str_semester_name);
                statement.bindString(5, str_email_id);
                statement.bindString(6, str_mobile_no);
                statement.bindString(7, str_username);
                statement.bindString(8, str_password);
                statement.bindString(9, str_photo_url);
                statement.bindString(10, str_updated_date);
                statement.bindString(11, str_user_type);
                statement.bindString(12, str_is_sync);
                statement.bindString(13, str_batch);
                statement.execute();

                IISERApp.set_session(IISERApp.SESSION_USER_ID, str_id);
                IISERApp.log(TAG, "EndTime->");
                db.setTransactionSuccessful();
                db.endTransaction();


                JSONArray json_course_list = json_student.getJSONArray("course_list");
                IISERApp.log(TAG, "json_course_list->" + json_course_list);
                int json_count = json_course_list.length();
                for (int i = 0; i < json_course_list.length(); i++) {
                    JSONObject json_course = json_course_list.getJSONObject(i);

                    String str_course_code = json_course.getString("course_code");
                    String str_course_name = json_course.getString("course_name");
                    String str_credit = json_course.getString("credit");
                    String str_sem_name = json_course.getString("semester");
                    String str_subj_name = json_course.getString("subject");
                    //  String str_is_selected = json_course.getString("is_selected");
                    String str_is_selected = "N";
                    TABLE_COURSE.insert_student_id_with_course(str_id,
                            str_sem_name,
                            str_course_code,
                            str_course_name,
                            str_subj_name,
                            str_credit,
                            str_is_selected);
                }


               /* JSONArray json_timetable_list = json_student.getJSONArray("timetable_list");
                IISERApp.log(TAG, "json_course_list->" + json_timetable_list);
                int json_timetable_count = json_timetable_list.length();
                for (int i = 0; i < json_count; i++) {
                    JSONObject json_timetable = json_timetable_list.getJSONObject(i);

                    String str_tt_id = json_timetable.getString("id");
                    String str_tt_coures_code = json_timetable.getString("course_code");
               // String str_tt_course_name = json_timetable.getString("course_name");
                    String str_tt_day = json_timetable.getString("day");
                  //  String str_tt_period_no = json_timetable.getString("period_no");
                    String str_tt_period_time = json_timetable.getString("period_time");
                    String str_tt_batch = json_timetable.getString("batch");
                    String str_tt_venue = json_timetable.getString("venue");
                    String str_is_seen = "Y";
                    //  String str_is_selected = "N";
                    TABLE_TIMETABLE.InsertTimetable(str_tt_id,
                            str_tt_coures_code,
                            str_tt_day,
                            str_tt_period_time,
                            str_tt_batch,
                            str_tt_venue,
                            str_is_seen);
                }

                JSONArray json_assign_list = json_student.getJSONArray("assignment_list");
                IISERApp.log(TAG, "json_course_list->" + json_assign_list);
                int json_assignment_count = json_assign_list.length();

              //  JSONArray jsonArray = json.getJSONArray("list");
              //  int len = jsonArray.length();
                if (json_assignment_count > 0) {

                    IISERApp.parse_assignment_data_and_insert(json_assign_list);
                }
  *//*              for (int i = 0; i < json_assignment_count; i++) {
                    JSONObject json_assigment = json_assign_list.getJSONObject(i);

                    String str_assign_id = json_assigment.getString("id");
                    String json_assig_exam_title = json_assigment.getString("exam_title");
                    String  json_assig_course_id = json_assigment.getString("course_id");
                    String  json_assig_course_name = json_assigment.getString("course_name");
                    String  json_assig_exam_date = json_assigment.getString("CEC_member");
                    String  json_assig_exam_date = json_assigment.getString("exam_date");
                    String  json_assig_exam_time = json_assigment.getString("exam_time")
                    String  json_assig_exam_day_ = json_assigment.getString("exam_day")
                    String  json_assig_venue = json_assigment.getString("venue")
                    String str_is_seen = "Y";
                    //  String str_is_selected = "N";
                    TABLE_TIMETABLE.InsertTimetable(str_assign_id,
                            json_assig_exam_title,
                            json_assig_course_id,
                            json_assig_course_name,
                            json_assig_exam_date,
                            str_tt_venue,
                            str_is_seen);
                }
*/


                JSONArray json_course_list1 = json_student.getJSONArray("updated_list");
                IISERApp.log(TAG, "json_course_list->" + json_course_list1);
                int json_count1 = json_course_list1.length();
                for (int i = 0; i < json_count1; i++) {
                    JSONObject json_course = json_course_list1.getJSONObject(i);
                    String str_student_id = json_course.getString("student_id");
                    String str_course_code = json_course.getString("course_code");

                    TABLE_COURSE.updateSelectedCourse(str_student_id, str_course_code);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void inset_faculty_data_from_server(JSONObject json, String str_user_type) {
        try {


           /* IISERApp.log(TAG, "in insert faculty JSON->" + json);
            JSONObject json_faculty = json.getJSONObject("faculty");
            JSONObject json_student_info = null;
            if (json_faculty.has("faculty_info")) {
                json_student_info = json_faculty.getJSONObject("faculty_info");
            }
            if (json_faculty != null) {
                IISERApp.log(TAG, "json_student_info->" + json_student_info);
*/


            SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_USER_PROFILE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);
            IISERApp.log(TAG, "in faculty data JSON->" + json);

            String str_id = json.getString("id");
            String str_name = json.getString("name");
            if (str_name.equalsIgnoreCase("null"))
                str_name = "";
            IISERApp.set_session(IISERApp.SESSION_STUDENT_NAME, str_name);
            String str_degree = json.getString("degree");
            String str_mobile_no = json.getString("mobile_no");
            String str_email_id = json.getString("email_id");
            String str_designation = json.getString("designation");
            String str_research = json.getString("research");
            String str_username = get_session(SESSION_USERNAME);
            String str_password = get_session(SESSION_PASSWORD);
            String str_photo_url = json.getString("photo_url");
            String str_personal_page = json.getString("personal_page_link");
            String str_is_sync = "N";
            if (str_degree.equalsIgnoreCase("null"))
                str_degree = "";
            if (str_mobile_no.equalsIgnoreCase("null"))
                str_mobile_no = "";
            if (str_email_id.equalsIgnoreCase("null"))
                str_email_id = "";
            if (str_designation.equalsIgnoreCase("null"))
                str_designation = "";
            if (str_research.equalsIgnoreCase("null"))
                str_research = "";


            statement.clearBindings();
            statement.bindString(1, str_id);//
            statement.bindString(2, str_name);//
            statement.bindString(5, str_email_id); //
            statement.bindString(6, str_mobile_no);
            statement.bindString(7, str_username);
            statement.bindString(8, str_password);
            statement.bindString(9, str_photo_url);
            statement.bindString(11, str_user_type);
            statement.bindString(12, str_is_sync);
            statement.bindString(14, str_degree);
            statement.bindString(15, str_designation);
            statement.bindString(16, str_research);
            statement.bindString(17, str_personal_page);
            statement.execute();

            IISERApp.set_session(IISERApp.SESSION_USER_ID, str_id);
            IISERApp.log(TAG, "EndTime->");
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void inset_supervisor_data_from_server(JSONObject json, String str_user_type) {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        try {

            db.beginTransaction();

            String sql = TABLE_USER_PROFILE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);
            IISERApp.log(TAG, "in faculty data JSON->" + json);

            String str_id = json.getString("id");
            String str_name = json.getString("name");
            if (str_name.equalsIgnoreCase("null"))
                str_name = "";
            IISERApp.set_session(IISERApp.SESSION_STUDENT_NAME, str_name);
            String str_degree = json.getString("degree");
            if (str_degree.equalsIgnoreCase("null"))
                str_degree = "";
            String str_mobile_no = json.getString("mobile_no");
            if (str_mobile_no.equalsIgnoreCase("null"))
                str_mobile_no = "";
            String str_email_id = json.getString("email_id");
            if (str_email_id.equalsIgnoreCase("null"))
                str_email_id = "";
            String str_designation = json.getString("designation");
            if (str_designation.equalsIgnoreCase("null"))
                str_designation = "";
            String str_research = json.getString("research");
            if (str_research.equalsIgnoreCase("null"))
                str_research = "";
            String str_username = get_session(SESSION_USERNAME);
            String str_password = get_session(SESSION_PASSWORD);
            String str_photo_url = "";
            String str_personal_page = json.getString("personal_page_link");
            String str_is_sync = "N";

            statement.clearBindings();
            statement.bindString(1, str_id);//
            statement.bindString(2, str_name);//
            statement.bindString(5, str_email_id); //
            statement.bindString(6, str_mobile_no);
            statement.bindString(7, str_username);
            statement.bindString(8, str_password);
            statement.bindString(9, str_photo_url);
            statement.bindString(11, str_user_type);
            statement.bindString(12, str_is_sync);
            statement.bindString(14, str_degree);
            statement.bindString(15, str_designation);
            statement.bindString(16, str_research);
            statement.bindString(17, str_personal_page);
            statement.execute();

            IISERApp.set_session(IISERApp.SESSION_USER_ID, str_id);
            IISERApp.log(TAG, "EndTime->");
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }

    }


    public static void inset_complete_faculty_data(JSONArray jsonarray, String str_user_type) {
        IISERApp.log(TAG, " inset_complete_faculty_data()");
        try {

            SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();
            String sql = TABLE_FACULTY_PROFILE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);
            IISERApp.log(TAG, " inset_complete_faculty_data(), JSON->" + jsonarray);
            int count_array = jsonarray.length();
            IISERApp.log(TAG, " inset_complete_faculty_data(), ArrayLen->" + count_array);
            IISERApp.log(TAG, " inset_complete_faculty_data(), StartTime->");
            for (int i = 0; i < count_array; i++) {
                JSONObject json = jsonarray.getJSONObject(i);

                String str_id = json.getString("id");
                String str_name = json.getString("name");
                String str_degree = json.getString("degree");
                String str_mobile_no = json.getString("mobile_no");
                String str_email_id = json.getString("email_id");
                String str_designation = json.getString("designation");
                String str_subject = json.getString("subject");
                String str_research = json.getString("research");
                //  String str_photo_url = json.getString("photo_url");
                String str_photo_url = "";
                String str_personal_page = json.getString("personal_page_link");
                String str_is_sync = "N";

                statement.clearBindings();
                statement.bindString(1, str_id);//
                statement.bindString(2, str_name);//
                statement.bindString(3, str_degree);
                statement.bindString(4, str_email_id); //
                statement.bindString(5, str_mobile_no);
                statement.bindString(6, str_designation);
                statement.bindString(7, str_subject);
                statement.bindString(8, str_research);
                statement.bindString(9, str_photo_url);
                statement.bindString(10, str_personal_page);
                statement.bindString(11, str_user_type);
                statement.bindString(12, str_is_sync);

                statement.execute();
                IISERApp.log(TAG, " inset_complete_faculty_data(), EndTime->");
            }
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (JSONException e) {
            IISERApp.log(TAG, " inset_complete_faculty_data(), JSONException ->" + e.getMessage());
        }

    }


    public static void inset_complete_faculty_data(List<DTO_Faculty_Profile> DTOFacultyProfileList, String str_user_type) {
        IISERApp.log(TAG, " inset_complete_faculty_data()");
        try {
            SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();
            String sql = TABLE_FACULTY_PROFILE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);
            int count_array = DTOFacultyProfileList.size();
            IISERApp.log(TAG, " inset_complete_faculty_data(), ArrayLen->" + count_array);
            for (int i = 0; i < count_array; i++) {
                DTO_Faculty_Profile DTO_Faculty = DTOFacultyProfileList.get(i);

                String str_id = DTO_Faculty.getStr_faculty_id(); //json.getString("id");
                String str_name = DTO_Faculty.getStr_faculty_name();
                String str_degree = DTO_Faculty.getStr_faculty_degree();
                String str_mobile_no =  DTO_Faculty.getStr_faculty_mobile_no();
                String str_email_id = DTO_Faculty.getStr_faculty_email_id();
                String str_designation = DTO_Faculty.getStr_faculty_designation();
                String str_subject = DTO_Faculty.getSubject();
                String str_research = DTO_Faculty.getStr_faculty_research();
                String str_photo_url = "";
                String str_personal_page =DTO_Faculty.getStr_faculty_personal_page();
                String str_is_sync = "N";

                statement.clearBindings();
                statement.bindString(1, str_id);//
                statement.bindString(2, str_name);//
                statement.bindString(3, str_degree);
                statement.bindString(4, str_email_id); //
                statement.bindString(5, str_mobile_no);
                statement.bindString(6, str_designation);
                statement.bindString(7, str_subject);
                statement.bindString(8, str_research);
                statement.bindString(9, str_photo_url);
                statement.bindString(10, str_personal_page);
                statement.bindString(11, str_user_type);
                statement.bindString(12, str_is_sync);

                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (Exception e) {
            IISERApp.log(TAG, " inset_complete_faculty_data(), JSONException ->" + e.getMessage());
        }

    }


    public static void parse_event_data_and_insert(JSONArray jEvent_array) {
        try {

            /*SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_EVENT.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);*/
            IISERApp.log(TAG, " in parse_event_data_and_insert JSON->" + jEvent_array);

            JSONObject jObj = null;
            int count_array = jEvent_array.length();
            IISERApp.log(TAG, "ArrayLen->" + count_array);
            IISERApp.log(TAG, "StartTime->");
            for (int i = 0; i < count_array; i++) {
                JSONObject jsonObject = jEvent_array.getJSONObject(i);

                String str_id = jsonObject.getString("id");
                String str_event_title = jsonObject.getString("title");
                String str_event_discription = jsonObject.getString("description");
                String str_event_date = jsonObject.getString("event_date");
                String str_event_venue = jsonObject.getString("venue");
                String str_event_pdf_link = jsonObject.getString("pdf_link");
                String str_is_seen = "Y";
               /* statement.clearBindings();
                statement.bindString(1, str_id);
                statement.bindString(2, str_event_title);
                statement.bindString(3, str_event_discription);
                statement.bindString(4, str_event_date);
                statement.bindString(5, str_event_pdf_link);
                statement.bindString(6, str_event_venue);
                statement.bindString(7, str_is_seen);
                statement.execute();*/

                TABLE_EVENT.InsertEventData(str_id, str_event_title, str_event_discription, str_event_date, str_event_pdf_link,
                        str_event_venue, str_is_seen);
            }

            IISERApp.log(TAG, "EndTime->");
           /* db.setTransactionSuccessful();
            db.endTransaction();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parse_notice_data_and_insert(JSONArray jNotice_array) {
        try {

            /*SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_NOTICE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);*/
            IISERApp.log(TAG, "JSON NOTICE->" + jNotice_array);

            JSONObject jObj = null;
            int count_array = jNotice_array.length();
            IISERApp.log(TAG, "ArrayLen->" + count_array);
            IISERApp.log(TAG, "StartTime->");
            for (int i = 0; i < count_array; i++) {
                JSONObject jsonObject = jNotice_array.getJSONObject(i);

                String str_id = jsonObject.getString("id");
                String str_notice_title = jsonObject.getString("title");
                String str_notice_discription = jsonObject.getString("description");
                String str_expiry_date = jsonObject.getString("expiry_date");
                String str_notice_pdf_link = jsonObject.getString("pdf_link");
                String str_is_seen = "Y";
              /*  statement.clearBindings();
                statement.bindString(1, str_id);
                statement.bindString(2, str_notice_title);
                statement.bindString(3, str_notice_discription);
                statement.bindString(4, str_notice_pdf_link);
                statement.bindString(5, str_expiry_date);
                statement.bindString(6, str_is_seen);

                statement.execute();*/
                TABLE_NOTICE.InsertNotice(str_id, str_notice_title, str_notice_discription, str_notice_pdf_link, str_expiry_date,
                        str_is_seen);
            }

            IISERApp.log(TAG, "EndTime->");
           /* db.setTransactionSuccessful();
            db.endTransaction();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parse_exam_schedule_data_and_insert(JSONArray jNotice_array) {
        try {

           /* SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_EXAM_SCHEDULE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);*/
            IISERApp.log(TAG, "JSON NOTICE->" + jNotice_array);

            JSONObject jObj = null;
            int count_array = jNotice_array.length();
            IISERApp.log(TAG, "ArrayLen->" + count_array);
            IISERApp.log(TAG, "StartTime->");
            for (int i = 0; i < count_array; i++) {
                JSONObject jsonObject = jNotice_array.getJSONObject(i);


                String str_id = jsonObject.getString("id");
                Log.d(TAG, "str_id" +str_id);

                String str_exam_name = jsonObject.getString("exam_title");
                Log.d(TAG, "exam_title" +str_exam_name);


                String str_course_id = jsonObject.getString("course_code");

                Log.d(TAG, "course_id1234566" +str_course_id);

               // String str_course_name = jsonObject.getString("course_name");
                String str_cec_member = jsonObject.getString("CEC_member");
                Log.d(TAG, "CEC_member" +str_cec_member);

                String str_exam_date = jsonObject.getString("exam_date");
                Log.d(TAG, "exam_date" +str_exam_date);

                String str_exam_time = jsonObject.getString("exam_time");
                Log.d(TAG, "exam_time" +str_exam_time);

                String str_exam_day = jsonObject.getString("exam_day");
                Log.d(TAG, "exam_day" +str_exam_day);

                String str_venue = jsonObject.getString("venue");
                Log.d(TAG, "venue" +str_venue);

                String str_is_seen = "Y";
                Log.d(TAG, "str_is_seen" +str_is_seen);


                /*statement.clearBindings();
                statement.bindString(1, str_id);
                statement.bindString(2, str_exam_name);
                statement.bindString(3, str_course_id);
                statement.bindString(4, str_cec_member);
                statement.bindString(5, str_exam_date);
                statement.bindString(6, str_exam_time);
                statement.bindString(7, str_exam_day);
                statement.bindString(8, str_venue);
                statement.bindString(9, str_is_seen);*/

                //  statement.execute();

                TABLE_EXAM_SCHEDULE.InsertExamShedule(str_id, str_exam_name, str_course_id, str_exam_date, str_exam_day,
                        str_exam_time, str_venue, str_cec_member, str_is_seen);
            }

            IISERApp.log(TAG, "EndTime->");
          /*  db.setTransactionSuccessful();
            db.endTransaction();*/


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parse_timetable_data_and_insert(JSONArray jNotice_array) {
        try {

            /*SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_TIMETABLE.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);*/
            IISERApp.log(TAG, "JSON TIMETABLE->" + jNotice_array);

            JSONObject jObj = null;
            int count_array = jNotice_array.length();
            IISERApp.log(TAG, "ArrayLen->" + count_array);
            IISERApp.log(TAG, "StartTime->");
            for (int i = 0; i < count_array; i++) {
                JSONObject jsonObject = jNotice_array.getJSONObject(i);

                String str_id = jsonObject.getString("id");
                String str_course_id = jsonObject.getString("course_code");
                String str_day = jsonObject.getString("day");
                String str_period_time = jsonObject.getString("period_time");
                String str_batch = jsonObject.getString("batch");
                String str_venue = jsonObject.getString("venue");
                String str_is_seen = "Y";
               /* statement.clearBindings();
                statement.bindString(1, str_id);
                statement.bindString(2, str_course_id);
                statement.bindString(3, str_day);
                statement.bindString(4, str_period_time);
                statement.bindString(5, str_batch);
                statement.bindString(6, str_venue);
                statement.bindString(7, str_is_seen);

                statement.execute();*/
                TABLE_TIMETABLE.InsertTimetable(str_id, str_course_id, str_day, str_period_time,
                        str_batch, str_venue, str_is_seen);
            }

            IISERApp.log(TAG, "EndTime->");
            /*db.setTransactionSuccessful();
            db.endTransaction();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parse_assignment_data_and_insert(JSONArray jNotice_array) {
        try {

           /* SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_ASSIGNMENT.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);*/
            IISERApp.log(TAG, "JSON ASSIGNMENT->" + jNotice_array);

            JSONObject jObj = null;
            int count_array = jNotice_array.length();
            IISERApp.log(TAG, "ArrayLen->" + count_array);
            IISERApp.log(TAG, "StartTime->");
            for (int i = 0; i < count_array; i++) {
                JSONObject jsonObject = jNotice_array.getJSONObject(i);

                String str_id = jsonObject.getString("id");
                String str_course_id = jsonObject.getString("course_code");
                String str_assignment_title = jsonObject.getString("title");
                String str_description = jsonObject.getString("description");
                String str_pdf_link = jsonObject.getString("pdf_link");
                String str_submission_date = jsonObject.getString("submission_date");
                String str_is_seen = "Y";/*
                if(jsonObject.has("image_path"))*/
                String str_img_url = jsonObject.getString("image_path");
              /*  statement.clearBindings();
                statement.bindString(1, str_id);
                statement.bindString(2, str_course_id);
                statement.bindString(3,
                str_assignment_title);
                statement.bindString(4, str_description);
                statement.bindString(5, str_pdf_link);
                statement.bindString(6, str_submission_date);
                statement.bindString(7, str_is_seen);

                statement.execute();*/
                TABLE_ASSIGNMENT.InsertAssignment(str_id, str_assignment_title, str_course_id, str_description, str_pdf_link,
                        str_submission_date, str_is_seen, str_img_url);
            }

            IISERApp.log(TAG, "EndTime->");
           /* db.setTransactionSuccessful();
            db.endTransaction();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parse_assignmentnotification_data(JSONObject jAssignment) {
        try {

            SQLiteDatabase db = IISERApp.db.getWritableDatabase();
            db.beginTransaction();

            String sql = TABLE_ASSIGNMENT.QUERY_INSERT;
            SQLiteStatement statement = db.compileStatement(sql);
            IISERApp.log(TAG, "JSON ASSIGNMENT->" + jAssignment);

            String str_id = jAssignment.getString("id");
            String str_course_id = jAssignment.getString("course_id");
            String str_assignment_title = jAssignment.getString("title");
            String str_description = jAssignment.getString("description");
            String str_pdf_link = jAssignment.getString("pdf_link");
            String str_submission_date = jAssignment.getString("submission_date");
            String str_is_seen = "Y";
            statement.clearBindings();
            statement.bindString(1, str_id);
            statement.bindString(2, str_course_id);
            statement.bindString(3, str_assignment_title);
            statement.bindString(4, str_description);
            statement.bindString(5, str_pdf_link);
            statement.bindString(6, str_submission_date);
            statement.bindString(7, str_is_seen);
            statement.execute();

            IISERApp.log(TAG, "EndTime->");
            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /*
     *  GCM FUNCTIONS
     */

    public void getRegistrationGCMID() {
        if (checkPlayServices()) {
            // Retrieve registration id from local storage
            regid = getRegistrationId(context);

            if (TextUtils.isEmpty(regid)) {
//		    	Log.i("Empty",regid);
             //   registerInBackground();

            } else {
//		    	Log.i("Not empty",regid);

            }
            Log.i("Store in database", regid);
        } else {
//		    Log.i(Globals.TAG, "No valid Google Play Services APK found.");
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //		GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //	    	Log.i(Globals.TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PREFS_PROPERTY_REG_ID, "");
        if (registrationId == null || registrationId.equals("")) {
//		    Log.i(Globals.TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
//		    Log.i(Globals.TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but how you store the regID in your app is up to you.
        return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

  /*  private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    log(TAG, msg);
                    // You should send the registration ID to your server over
                    // HTTP, so it can use GCM/HTTP or CCS to send messages to your app.
//		    sendRegistrationIdToBackend();
//		    postData(regid);
                    // For this demo: we use upstream GCM messages to send the
                    // registration ID to the 3rd party server

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            private void storeRegistrationId(Context context, String regId) {
                final SharedPreferences prefs = getGcmPreferences(context);
                int appVersion = getAppVersion(context);
                log(TAG, "Saving regId on app version " + appVersion);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PREFS_PROPERTY_REG_ID, regId);
                editor.putInt(PROPERTY_APP_VERSION, appVersion);
                editor.commit();
                set_session(PREFS_PROPERTY_REG_ID, regId);
                set_session(PROPERTY_APP_VERSION, appVersion + "");
                log(TAG, "Saving regId on app version " + regId);
            }

            @Override
            protected void onPostExecute(String msg) {
//	    	Log.e("","Device Registered");
//		((EditText) findViewById(R.id.txtPin)).setText(regid);
            }
        }.execute(null, null, null);
    }*/



    /*public static String get_play_store_version() {
        RequestBody formBody = new FormEncodingBuilder().build();
        return post_server_call(url_version_check, formBody);
    }*/

    public String get_device_id() {
        ContentResolver cr = context.getContentResolver();
        return URLEncoder.encode(Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID));
    }

    public static String get_current_date() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static DTOService set_new_password(String user_id, String password) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("user_id", user_id)
                .add("password", password)
                .build();
        return post_server_call(url_save_password, formBody);
    }

    public static DTOService get_otp(String mobile, String user_id) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("user_id", user_id)
                .add("mobile_no", mobile)
                .build();
        return post_server_call(url_get_otp, formBody);
    }



    public static DTOService get_mobile(String user_id) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", user_id)
                .build();
        return post_server_call(url_get_mobile, formBody);
    }

    public static DTOService get_check_user(String username,String password) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", username)
                .add("password",password)
                .build();
        return post_server_call(get_check_user, formBody);
    }


    public static String getLastDateOfMonth(String firstDate) {
        IISERApp.log("", "in getLastDateOfMonth() " + firstDate);
        Date dt = new Date();
        Calendar c = Calendar.getInstance();

        Date date1 = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(firstDate);
            c.setTime(date1);
            c.add(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        IISERApp.log("", "last date simpleDateFormat.format(date):- " + simpleDateFormat.format(c.getTime()));
        return simpleDateFormat.format(c.getTime());
    }



  /*  public static DTOResponse get_check_user(RequestBody formBody,String username,String password) {
        String Response = IISERApp.post_server_call1(IISERApp.get_check_user, formBody);
        DTOResponse dtoResponse = null;
        if (!Response.equals("2")) {
            try {
                JSONObject jsonObject = new JSONObject(Response);
                if (jsonObject != null && jsonObject.length() > 0) {
                    if (jsonObject.has("response_status")) {
                        String response_status = jsonObject.getString("response_status");
                        String response_message = jsonObject.getString("response_message");
                        IISERApp.log("","responseMessage->" + response_message);
                        String otp = jsonObject.getString("otp");
                        IISERApp.set_session(SESSION_OTPs,otp);

                        IISERApp.log("","otp->" + otp);
                        dtoResponse = new DTOResponse(response_status, response_message);
                        if (response_status.equalsIgnoreCase("1")) {



                            Log.d(TAG, "get_check_user: ");

                           // Toast.makeText(context, response_message, Toast.LENGTH_SHORT).show();
//                            if (jsonObject.has("user_data")) {
//                                JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");
//                                String id = jsonObject1.getString("id");
//                                //IISERApp.log(LOG_TAG, "","iddddddddddd " + id);
//
//                                IISERApp.set_session(IISERApp.SESSION_USER_ID, id);
//                                String mobile_no = jsonObject1.getString("mobile_no");
//
//
//                            }

                        } if(response_status.equalsIgnoreCase("0")){

                            Log.d(TAG, "get_check_user wrong: ");




                        }

                        else {
//                            Intent intent = new Intent(ActivityLogin.this, ActivityMobileNo.class);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                } *//*else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }*//*
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            dtoResponse = new DTOResponse("2", Response);
        }
        return dtoResponse;
    }

*/



}
