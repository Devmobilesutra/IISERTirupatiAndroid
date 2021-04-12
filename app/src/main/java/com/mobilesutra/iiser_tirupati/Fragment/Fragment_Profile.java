package com.mobilesutra.iiser_tirupati.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesutra.iiser_tirupati.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Profile extends Fragment {


    public Fragment_Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.activity_fragment_profile, container, false);
        final View view = inflater.inflate(R.layout.activity_fragment_profile, container, false);

        initComponents(view);
        initComponentsListeners();
        bindComponentData();

        return view;
    }

    private void initComponents(View view) {
    }

    private void initComponentsListeners() {
    }

    private void bindComponentData() {
    }
}
