package com.mobilesutra.iiser_tirupati.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.R;

public class ActivityOTP extends AppCompatActivity {
    Button btn_send_otp_number;
    EditText ed_txt_otp_no;
    String str_otp_no;
    Context context;
    String LOG_TAG = ActivityOTP.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        context = this;
        initicomponent();
        initicomponentlistner();
    }

    private void initicomponent() {
        btn_send_otp_number = (Button) findViewById(R.id.btn_send_otp_number);
        ed_txt_otp_no = (EditText) findViewById(R.id.ed_txt_otp_no);
    }

    private void initicomponentlistner() {
        btn_send_otp_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IISERApp.log(LOG_TAG, "Enter in OTP Button");
                str_otp_no = ed_txt_otp_no.getText().toString();
                if (str_otp_no.equals("")) {
                    Snackbar.make(btn_send_otp_number, "Please Enter OTP Here", Snackbar.LENGTH_LONG).show();
                } else {
                    IISERApp.log(LOG_TAG, "IISERAPP Otp" + IISERApp.get_session(IISERApp.SESSION_OTP));
                    if (str_otp_no.equals(IISERApp.get_session(IISERApp.SESSION_OTP))) {
                        Intent intent = new Intent(ActivityOTP.this, ActivityNewPassword.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(btn_send_otp_number, "RE-Enter OTP", Snackbar.LENGTH_LONG).show();
                    }
                }
            }


        });
    }


}
