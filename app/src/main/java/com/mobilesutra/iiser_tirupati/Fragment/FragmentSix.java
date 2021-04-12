package com.mobilesutra.iiser_tirupati.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Activities.ActivityTabHeaderFooter;
import com.mobilesutra.iiser_tirupati.R;


public class FragmentSix extends Fragment {


    TextView txt_new_login=null;
    Context context=null;
    Intent intent=null;
    public FragmentSix() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_six, container, false);
        final View view = inflater.inflate(R.layout.fragment_six, container, false);

        initComponents(view);
        initComponentsListeners();
        bindComponentData();


        return view;
    }

    private void initComponents(View view) {
        txt_new_login=(TextView) view.findViewById(R.id.txt_new_login);
    }

    private void initComponentsListeners() {
        txt_new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getContext(),ActivityTabHeaderFooter.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }

    private void bindComponentData() {
    }
}
