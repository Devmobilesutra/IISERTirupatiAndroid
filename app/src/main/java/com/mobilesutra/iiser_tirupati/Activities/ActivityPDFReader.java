package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Pdf;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by selim_tekinarslan on 10.10.2014.
 */
public class ActivityPDFReader extends Activity {
    private static final String TAG = "SampleActivity",LOG_TAG="ActivityPDFReader";
    private static final String SAMPLE_FILE = "sample.pdf";
    private static final String FILE_PATH = "filepath";
    private static final String SEARCH_TEXT = "text";
    private static String pdfUrl="";
    private Fragment_Pdf fragment;
    private static Context context;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        IISERApp.log(LOG_TAG, "in activityPDFReader::" + pdfUrl);
        context = ActivityPDFReader.this;

        /*FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });*/

        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();

      //  getintentdata();
        if(savedInstanceState != null) {

            //App.log_bundle(savedInstanceState);

            pdfUrl = savedInstanceState.getString("PdfUrl");
            IISERApp.log(LOG_TAG, "PdfUrl in activityPDFReader:" + pdfUrl);


            // getintentdata();

        }

        openPdfWithFragment();

    }

    private void getintentdata() {
        Intent intent=getIntent();


        pdfUrl=intent.getStringExtra("PdfUrl");
        IISERApp.log(LOG_TAG,"PdfUrl in activityPDFReader:"+pdfUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query)//burada klavyeden ara ya basiyor user
            {
                fragment.search(1, query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        return super.onCreateOptionsMenu(menu);
    }

    public void openPdfWithFragment() {
        fragment = new Fragment_Pdf();
        Bundle args = new Bundle();
      //  args.putString(FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + IISERApp.get_session("pdfFile"));
        args.putString(FILE_PATH, pdfUrl);

        fragment.setArguments(args);
        fragmentManager = getFragmentManager();

        /*fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });*/


       /* for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }*/
        //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
       // onBackPressed();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //fragment.onDestroyView();
        IISERApp.log(LOG_TAG,"IN onBackPressed");
        /*Intent intent=new Intent(context,ActivityEvents.class);
        startActivity(intent);*/
//finish();
        if (fragmentManager.getBackStackEntryCount() >  1) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
 }
}
