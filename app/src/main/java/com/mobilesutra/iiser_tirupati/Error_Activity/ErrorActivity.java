package com.mobilesutra.iiser_tirupati.Error_Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.R;

import java.util.ArrayList;
import java.util.List;


public class ErrorActivity extends Activity implements AsyncResponse<String> {

	TextView error,send,tv_title_page;
    ImageView img_back;
    String Error ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_error);
	
		error = (TextView) findViewById(R.id.error);
		send = (TextView) findViewById(R.id.send);
		tv_title_page = (TextView) findViewById(R.id.tv_title_page);
		img_back =(ImageView)findViewById(R.id.img_back);
		Error = getIntent().getStringExtra("error");
		
		error.setText(Error);
		
		
		 
		
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SendErrorMail(getApplicationContext(),Error);
				Toast.makeText(ErrorActivity.this, "Error Report sent to server.", Toast.LENGTH_LONG).show();
			}
		});
		
		img_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				System.exit(0);
				finish();
			}
		});

	}
	
	private void SendErrorMail(Context _context, String ErrorContent )
	 {
//	  Intent sendIntent = new Intent(Intent.ACTION_SEND);
	  String subject ="CrashReport_MailSubject";
	  String body = "CrashReport_MailBody " +
	   "\n\n"+
	   ErrorContent+
	   "\n\n";
//	  sendIntent.putExtra(Intent.EXTRA_EMAIL,
//	    new String[] {"bodakesatish@gmail.com"});
//	  sendIntent.putExtra(Intent.EXTRA_TEXT, body);
//	  sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//	  sendIntent.setType("message/rfc822");
//	  _context.startActivity( Intent.createChooser(sendIntent, "Title:") );
		
		
		
		
		List<Intent> targetShareIntents=new ArrayList<Intent>();
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfos=this.getPackageManager().queryIntentActivities(shareIntent, 0);
        if(!resInfos.isEmpty()){
            System.out.println("Have package");
            for(ResolveInfo resInfo : resInfos){
                String packageName=resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);
                /*if(packageName.contains("com.whatsapp") || packageName.contains("com.facebook.katana") || packageName.contains("com.google.android.gm")){*/
                    Intent intent=new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                /*}*/
            }
            if(!targetShareIntents.isEmpty()){
                System.out.println("Have Intent");
                Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            }else{
                System.out.println("Do not Have Intent");

            }
        }

	 }
	

	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
	}

	public void processFinishLog(String output) {
		
		
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		System.exit(0);
		finish();
	}
	
}
