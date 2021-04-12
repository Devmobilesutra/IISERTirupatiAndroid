package com.mobilesutra.iiser_tirupati.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTOService;
import com.mobilesutra.iiser_tirupati.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityMobileNo extends AppCompatActivity {

    public static String LOG_TAG = ActivityMobileNo.class.getSimpleName();
    Context context;
    EditText edt_mobile;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no);
        context = this;

        initComponents();
        initComponentListener();
        bindComponentData();
    }

    private void initComponents() {
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        btn_send = (Button) findViewById(R.id.btn_send);
    }

    private void initComponentListener() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = edt_mobile.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Snackbar.make(edt_mobile, "Please enter mobile number", Snackbar.LENGTH_SHORT).show();
                } else if (mobile.length() != 10) {
                    Snackbar.make(edt_mobile, "Please enter 10 digit mobile number", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (((IISERApp) getApplication()).isInternetAvailable()) {
                        AsyncSendMobile asyncSendMobile = new AsyncSendMobile();
                        asyncSendMobile.execute(mobile);
                    } else {
                        Snackbar.make(edt_mobile, "No internet connection", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void bindComponentData() {

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
            progressDialog = ProgressDialog.show(ActivityMobileNo.this, ss1, "Please wait...", false, false);

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
                                    Intent intent = new Intent(ActivityMobileNo.this, ActivityOTP.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    showSuccessResponse(context, "Please try after some time..");
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

    private void showSuccessResponse(Context context, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.app_name)).setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
}
