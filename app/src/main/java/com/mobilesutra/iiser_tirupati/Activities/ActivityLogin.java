package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_USER_PROFILE;
import com.mobilesutra.iiser_tirupati.Model.DTOResponse;
import com.mobilesutra.iiser_tirupati.Model.DTOService;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static android.content.ContentValues.TAG;

/**
 * Created by kalyani on 22/04/2016.
 */
public class ActivityLogin extends Activity {

    TextView txt_sign_up = null;
    CheckBox chk_shw_password = null;
    Button btn_login = null;
    EditText edtxt_username = null, edtxt_password = null;
    ProgressBar prgresbar = null;
    ProgressDialog dialog = null;
    Context context = null;
    Intent intent = null;
    TextView txt_forgot_password;
    private ProgressDialog dialog1 = null;
    String EnteredOtp=null;


    String LOG_TAG = "ActivityLogin", str_username = null, str_password = null;
    ArrayList<String> userdetails = new ArrayList<String>();
String response_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_six);
        context = this;
        getIntentData();
        initComponents();
        initComponentsListeners();
        bindComponentData();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
      //  Toast.makeText(context, refreshedToken, Toast.LENGTH_SHORT).show();

    }

    private void getIntentData() {
    }

    private void initComponents() {
        //  txt_sign_up = (TextView) findViewById(R.id.txt_new_login);
        btn_login = (Button) findViewById(R.id.btn_login);
        chk_shw_password = (CheckBox) findViewById(R.id.chkbx_remembr_me);
        prgresbar = (ProgressBar) findViewById(R.id.progressbar);

        edtxt_username = (EditText) findViewById(R.id.edtxt_username);
        edtxt_password = (EditText) findViewById(R.id.edtxt_password);

        txt_forgot_password = (TextView) findViewById(R.id.txt_forgot_password);

    }

    private void initComponentsListeners() {
        chk_shw_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    //Toast.makeText(firstpage.this, " No internet connection", Toast.LENGTH_SHORT).show();
                    edtxt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtxt_password.setSelection(edtxt_password.getText().length());
                    // hide password
                } else {
                    edtxt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    int pos = edtxt_password.getText().length();
                    edtxt_password.setSelection(pos);
                }
            }
        });

        btn_login.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        str_username = edtxt_username.getText().toString();
                        str_password = edtxt_password.getText().toString();  //super un : vijaykumar@students.iisetirupati.ac.in  pw: iiser123

                        IISERApp.set_session(IISERApp.SESSION_USERNAME, str_username);
                        IISERApp.set_session(IISERApp.SESSION_PASSWORD, str_password);


                        if (edtxt_username.getText().toString().equalsIgnoreCase("")) {
                            edtxt_username.setError("Please Enter User Name");
                        } else if (edtxt_password.getText().toString().equalsIgnoreCase("")) {
                            edtxt_password.setError("Please Enter Your Address");
                        }else{
                            IISERApp.log(LOG_TAG, "In else cal........................");

//                            AsyncSendMobile_otp asyncGetMobile_otp = new AsyncSendMobile_otp();
//                            asyncGetMobile_otp.execute();

                          //  new AsyncSendMobile_otp().execute();
                              call_LoginServices();


                        }







                    }
                });







        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = edtxt_username.getText().toString().trim();
                if (TextUtils.isEmpty(user_id)) {
                    Snackbar.make(edtxt_username, "Please enter user id", Snackbar.LENGTH_SHORT).show();
                } else if (((IISERApp) getApplication()).isInternetAvailable()) {
                    AsyncGetMobile asyncGetMobile = new AsyncGetMobile();
                    asyncGetMobile.execute(user_id);
                }
            }
        });


       /* txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(Vaiew v) {
                intent=new Intent(context,ActivityRegistration.class);
                startActivity(intent);
               // finish();
            }
        });*/
    }



    private void getdialog_showResponse(Context context, String msg) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.Response_dialog)).setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //   ((App) getApplicationContext()).getRegistrationGCMID();

                    }
                }).show();

    }



//





    private void bindComponentData() {
        prgresbar.setVisibility(View.GONE);
        userdetails = TABLE_USER_PROFILE.getusername_password();
        IISERApp.log(LOG_TAG, "Userdetails:" + userdetails);
        if (!userdetails.isEmpty()) {
            edtxt_username.setText(userdetails.get(0).toString());
            edtxt_password.setText(userdetails.get(1).toString());
            //edtxt_username.setEnabled(false);
            //edtxt_password.setEnabled(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IISERApp.log(LOG_TAG, "ondestroy");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        context.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            IISERApp.log_bundle(bundle);
            if (bundle != null) {
                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
                    // prgresbar.setVisibility(View.GONE);

                    if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("1")) {

                        IISERApp.log(LOG_TAG, "in if condition of response stastus 1");
                        //dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            IISERApp.log(LOG_TAG, "in if condition of response stastus 1 if dialog showing..");
                            // Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();

                            IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "Y");
                            Intent intent1 = new Intent(context, ActivityTabHost_AfterLogin.class);
                            startActivity(intent1);
                            finish();
                        }
                    } else if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("0")) {
                        if (dialog != null && dialog.isShowing()) {
                            // dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                            dialog.dismiss();
                        }
                        Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();

                    } else {
                        dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    }
                } else {
                    // prgresbar.setVisibility(View.GONE);
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };


    public void call_LoginServices() {
        if (userdetails.isEmpty()) {
            if (!str_username.equals("") && !str_password.equals("")) {

                if (((IISERApp) getApplication()).isInternetAvailable()) {

                   // new AsyncSendMobile_otp().execute();

                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_LOGIN);
                    //  prgresbar.setVisibility(View.VISIBLE);
                    dialog = ProgressDialog.show(context, "IISER", "Please Wait... Checking Login Credentials");
                    context.startService(intent1);


                } else {
                    Snackbar.make(btn_login, "No Internet available...", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(btn_login, "Username or Password is empty...", Snackbar.LENGTH_LONG).show();
            }
        } else {
            if ((userdetails.get(0).toString()).equals(str_username) &&
                    (userdetails.get(1).toString()).equals(str_password)) {
                IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                intent = new Intent(context, ActivityTabHost_AfterLogin.class);
                startActivity(intent);
                // Toast.makeText(ActivityLogin.this, "User Logged In Successfully ...", Toast.LENGTH_SHORT).show();
                finish();
                // Toast.makeText(ActivityLogin.this, "User Logged In Successfully ...", Toast.LENGTH_SHORT).show();
            } else
                Snackbar.make(btn_login, "You are not authorised to login from this device.", Snackbar.LENGTH_LONG).show();
        }
    }



    public class AsyncGetMobile extends AsyncTask<String, Void, String> {

        String response = "", user_id;
        DTOService dtoService;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String title = getResources().getString(R.string.app_name);
            SpannableString ss1 = new SpannableString(title);
            ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss1.length(), 0);
            progressDialog = ProgressDialog.show(ActivityLogin.this, ss1, "Please wait...", false, false);

        }


        @Override
        protected String doInBackground(String... params) {
            user_id = params[0];

            dtoService = IISERApp.get_mobile(user_id);
            return null;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (dtoService != null) {
                if (dtoService.getStr_response_code() == 200) {
                    response = dtoService.getStr_response_body();
                    IISERApp.log(LOG_TAG, "Response is " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject != null && jsonObject.length() > 0) {
                            if (jsonObject.has("response_status")) {
                                String response_status = jsonObject.getString("response_status");
                                String response_message = jsonObject.getString("response_message");

                                if (response_status.equalsIgnoreCase("1")) {

                                    if (jsonObject.has("user_data")) {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");
                                        String id = jsonObject1.getString("id");
                                        IISERApp.log(LOG_TAG, "iddddddddddd " + id);

                                        IISERApp.set_session(IISERApp.SESSION_USER_ID, id);
                                        String mobile_no = jsonObject1.getString("mobile_no");

                                        AsyncSendMobile asyncSendMobile = new AsyncSendMobile();
                                        asyncSendMobile.execute(mobile_no);
                                    }

                                } else if (response_status.equalsIgnoreCase("0"))
                                {
                                    Toast.makeText(context, response_message, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent intent = new Intent(ActivityLogin.this, ActivityMobileNo.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } /*else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    public class AsyncSendMobile_otp extends AsyncTask<String, Void, String> {

        String response = "";
        DTOService dtoService;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String title = getResources().getString(R.string.app_name);
            SpannableString ss1 = new SpannableString(title);
            ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss1.length(), 0);
            progressDialog = ProgressDialog.show(ActivityLogin.this, ss1, "Please wait...", false, false);

        }


        @Override
        protected String doInBackground(String... params) {
           // user_id = params[0];

            dtoService = IISERApp.get_check_user(str_password,str_username);
            return null;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (dtoService != null) {
                if (dtoService.getStr_response_code() == 200) {
                    response = dtoService.getStr_response_body();
                    IISERApp.log(LOG_TAG, "Response is " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject != null && jsonObject.length() > 0) {
                            if (jsonObject.has("response_status")) {
                                String response_status = jsonObject.getString("response_status");
                                String response_message = jsonObject.getString("response_message");

                                IISERApp.log(LOG_TAG, "response_statusResponse is " + response_status);

                                IISERApp.log(LOG_TAG, "rightotp is " + response_otp);


                                if (response_status.equalsIgnoreCase("1")) {

                                    response_otp = jsonObject.getString("otp");


                                    if (jsonObject.has("user_data")) {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");
                                        String id = jsonObject1.getString("id");
                                        IISERApp.log(LOG_TAG, "iddddddddddd111 " + id);

                                        IISERApp.set_session(IISERApp.SESSION_USER_ID, id);
                                       // String otp = jsonObject1.getString("otp");

                                      //  Log.d(TAG, "onPostExecute: otpttt"+otp);

                                        successDialog(response);


                                       /* AsyncSendMobile asyncSendMobile = new AsyncSendMobile();
                                        asyncSendMobile.execute(mobile_no);*/
                                    }

                                } else if (response_status.equalsIgnoreCase("0"))
                                {
                                    Toast.makeText(context, response_message, Toast.LENGTH_SHORT).show();
                                }
                                else {
/*                                    Intent intent = new Intent(ActivityLogin.this, ActivityMobileNo.class);
                                    startActivity(intent);
                                    finish();*/
                                }
                            }
                        } /*else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


  /*  class AsyncSendMobile_otp extends AsyncTask<Void, String, String> {

        String Response = "";
        String response = "";
        int getstate = 0;
        DTOResponse dtoResponse = null;

        public AsyncSendMobile_otp() {


        }

        protected void onPreExecute() {
            Response = "";
            dialog1 = ProgressDialog.show(ActivityLogin.this, getResources().getString(R.string.Sending_data1),
                    getResources().getString(R.string.Sending_data1), true, false);

        }

        @SuppressWarnings("static-access")
        protected String doInBackground(Void... params) {

            RequestBody formBody = new FormEncodingBuilder()
                    .add("username", str_username)
                    .add("password", str_password)
                    .build();
            dtoResponse = new DTOResponse("101", "");
            publishProgress("");
            dtoResponse = IISERApp.get_check_user(formBody, str_username,str_password);
            if (dtoResponse.getResponse_code().equalsIgnoreCase("1")) {
                dtoResponse.setResponse_code("102");
                publishProgress("");

            } else {
                publishProgress("");
            }
            return "";
        }

        protected void onProgressUpdate(String... progress) {
            IISERApp.log("", "In onProgressUpdate ");
            IISERApp.log("", "responseCode->" + dtoResponse.getResponse_code());
            IISERApp.log("", "responseMessage->" + dtoResponse.getResponse_message());


                    if (dtoResponse.getResponse_code().equalsIgnoreCase("101")) {
                    } else {
                        IISERApp.log("", "In Else....... ");
                        dialog1.dismiss();
                        if (dtoResponse.getResponse_code().toString().equalsIgnoreCase("2")) {
                            String internet_response = getResources().getString(R.string.Internet_response);
                            getdialog_showResponse(context, internet_response);
                        }
                        if (dtoResponse.getResponse_code().equalsIgnoreCase("1")) {
                            *//*Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);*//*
//                    Intent intent = new Intent(Login.this, ActivityOTP.class);
//                    startActivity(intent);
//                    finish();

                            IISERApp.log("", "In Elseone....... ");


                        } else {

                            IISERApp.set_session(IISERApp.SESSION_LOGIN, "true");

                            successDialog(dtoResponse.getResponse_message());
                            //   getdialog_showResponse(context, dtoResponse.getResponse_message());
                        }
                    }

                }
            }
*/

    private void successDialog(String response_message) {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(ActivityLogin.this);
        LayoutInflater factory = LayoutInflater.from(ActivityLogin.this);
        final View view = factory.inflate(R.layout.custom_dialog, null);
        // alertadd.setMessage(response_message);
        // TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView txt_msg=(TextView) view.findViewById(R.id.txt_msg );



        txt_msg.setText(response_message);
        Log.d(TAG, "successDialog1: "+txt_msg);
        alertadd.setView(view);

        alertadd.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                IISERApp.log("","alertadd  click111");
                IISERApp.log("","SessionOtp===  "+IISERApp.get_session(IISERApp.SESSION_OTPs));

                EditText edt=(EditText)view.findViewById(R.id.edt);
                EnteredOtp = edt.getText().toString();
                IISERApp.log("","EnteredOtp===  "+EnteredOtp);

                if (EnteredOtp.equals("")) {
                    Toast.makeText(context, " Please Enter OTP Here", Toast.LENGTH_SHORT).show();
                //    Snackbar.make(btn_send_otp_number, "Please Enter OTP Here", Snackbar.LENGTH_LONG).show();
                } else {
                    IISERApp.log(LOG_TAG, "chitale Otp" + IISERApp.get_session(IISERApp.SESSION_OTPs));

                    if (EnteredOtp.equals(response_otp)) {
                        IISERApp.log(LOG_TAG, "inIf........");

                       // call_LoginServices();
//


                        Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                        intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_LOGIN);
                        //  prgresbar.setVisibility(View.VISIBLE);
                        dialog = ProgressDialog.show(context, "IISER", "Please Wait... Checking Login Credentials");
                        context.startService(intent1);

                    } else {
                        Toast.makeText(context, " Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                        //Toast.make(btn_send_otp_number,, Snackbar.LENGTH_LONG).show();
                    }
                }



            }

        });

        alertadd.show();

    }

    public class AsyncSendMobile extends AsyncTask<String, Void, String> {

            String response = "", mobile;
            DTOService dtoService;
            ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                String title = getResources().getString(R.string.app_name);
                SpannableString ss1 = new SpannableString(title);
                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss1.length(), 0);
                progressDialog = ProgressDialog.show(ActivityLogin.this, ss1, "Please wait...", false, false);

            }

            @Override
            protected String doInBackground(String... params) {
                mobile = params[0];

                dtoService = IISERApp.get_otp(mobile, IISERApp.get_session(IISERApp.SESSION_USER_ID));

                return response;
            }

            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (dtoService != null) {
                if (dtoService.getStr_response_code() == 200) {
                    response = dtoService.getStr_response_body();
                    IISERApp.log(LOG_TAG, "Response is " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject != null && jsonObject.length() > 0) {
                            if (jsonObject.has("status")) {
                                boolean status = jsonObject.getBoolean("status");
                                if (status) {
                                    String otp = jsonObject.getString("OTP");
                                    IISERApp.set_session(IISERApp.SESSION_OTP, otp);
                                    Intent intent = new Intent(ActivityLogin.this, ActivityOTP.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    //showSuccessResponse(context, "Please try after some time..");
                                }
                            }
                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }



   /* public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else {
            Toast.makeText(getBaseContext(), R.string.press_once_again_to_exit, Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }*/





}
