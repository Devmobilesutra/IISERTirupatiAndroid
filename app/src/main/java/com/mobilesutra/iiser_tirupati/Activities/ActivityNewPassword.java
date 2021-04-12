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
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTOService;
import com.mobilesutra.iiser_tirupati.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityNewPassword extends AppCompatActivity {
    Button btn_reset;
    EditText edtxt_new_password, edtxt_confirm_password;
    String str_new_password, str_confirm_password;
    Context context;
    String LOG_TAG = "ActivityOTP.class";
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        context = this;
        initicomponent();
        initicomponentlistner();
    }

    private void initicomponent() {
        btn_reset = (Button) findViewById(R.id.btn_reset);
        edtxt_new_password = (EditText) findViewById(R.id.edtxt_new_password);
        edtxt_confirm_password = (EditText) findViewById(R.id.edtxt_confirm_password);
    }

    private void initicomponentlistner() {

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_new_password = edtxt_new_password.getText().toString().trim();
                str_confirm_password = edtxt_confirm_password.getText().toString().trim();

                if (str_new_password.equals("") || str_confirm_password.equals("")) {
                    IISERApp.log(LOG_TAG, "NEW PASSWORD UNSUCCESSFULL");
                    Snackbar.make(btn_reset, "New Password or Retype Password is empty...", Snackbar.LENGTH_LONG).show();
                } else if (!str_new_password.equalsIgnoreCase(str_confirm_password)) {
                    Snackbar.make(btn_reset, "Please enter same password...", Snackbar.LENGTH_LONG).show();
                } else {

                    if (((IISERApp) getApplication()).isInternetAvailable()) {

                        SavePassword savePassword = new SavePassword();
                        savePassword.execute(str_new_password);
                    } else {

                    }

                }
            }
        });
    }


    public class SavePassword extends AsyncTask<String, Void, String> {

        String response = "", user_id, password;
        DTOService dtoService;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String title = getResources().getString(R.string.app_name);
            SpannableString ss1 = new SpannableString(title);
            ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss1.length(), 0);
            progressDialog = ProgressDialog.show(ActivityNewPassword.this, ss1, "Please wait...", false, false);

        }

        @Override
        protected String doInBackground(String... params) {
            password = params[0];
            user_id = IISERApp.get_session(IISERApp.SESSION_USER_ID);

            dtoService = IISERApp.set_new_password(user_id, password);

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
                                if (response_status.equalsIgnoreCase("1")) {

                                    IISERApp.set_session(IISERApp.SESSION_PASSWORD, str_new_password);

                                    Intent intent = new Intent(ActivityNewPassword.this, ActivityLogin.class);
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
                       /* Intent intent = new Intent(ActivityNewPassword.this, ActivityLogin.class);
                        intent.putExtra("userName", "true");
                        startActivity(intent);
                        finish();*/


                    }
                }).show();

    }

}
