package com.mobilesutra.iiser_tirupati.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.R;
import com.squareup.picasso.Picasso;

public class ActivityAssignImage extends AppCompatActivity {
    ImageView img_assignment = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_assign_image);
        context = this;

        img_assignment = (ImageView) findViewById(R.id.img_assignment);
        String img_url = IISERApp.get_session(IISERApp.SESSION_IMG_PATH).toString();
        if (!TextUtils.isEmpty(img_url)) {


            Picasso.with(context)
                    .load(img_url)
                    .placeholder(R.drawable.wait)
                    .error(R.drawable.wait)
                    .resize((int) getResources().getDimension(R.dimen.txt_profile_label_width), (int) getResources().getDimension(R.dimen.txt_profile_label_width))
                    .into(img_assignment);
        }

    }
}
