package com.mobilesutra.iiser_tirupati.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mobilesutra.iiser_tirupati.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_BSMS_Admission extends android.support.v4.app.Fragment {

   /* String[] asset_array={"file:///android_asset/BSMSProgramme.html","file:///android_asset/Integrated_PhD_Programme.html"
            ,"file:///android_asset/Ph.html"};*/
    WebView wbvw_course=null;
    int position=0;
    View view=null;
    private static final String ARG_PARAM_TITLE = "title";

    public static Fragment_BSMS_Admission newInstance(String str_title) {
        Log.d("Fragment", "In newInstance");
        Fragment_BSMS_Admission fragment = new Fragment_BSMS_Admission();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        fragment.setArguments(bundle);
        return fragment;
    }
    public Fragment_BSMS_Admission() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment__bsms__course, container, false);

            getIntentData();
            initComponents(view);
            initComponentsListeners();
            bindComponentData();
            initComponetData();
        }
            return view;

    }

    private void initComponetData() {

    }

    private void getIntentData() {

    }

    private void initComponents(View view) {
        wbvw_course= (WebView) view.findViewById(R.id.webview_course);


    }

    private void initComponentsListeners() {
    }

    private void bindComponentData() {
        wbvw_course.getSettings().setJavaScriptEnabled(true);
        wbvw_course.getSettings().setPluginState(WebSettings.PluginState.ON);

        wbvw_course.loadUrl("file:///android_asset/admission_BSMSProgramme.html".replaceAll(" ", "%20"));
    }
}

