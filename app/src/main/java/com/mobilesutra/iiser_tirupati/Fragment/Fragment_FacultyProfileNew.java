package com.mobilesutra.iiser_tirupati.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_PROFILE;
import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Profile;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;

import java.util.ArrayList;
import java.util.List;

public class Fragment_FacultyProfileNew extends Fragment {
    FloatingActionButton fab_refresh = null;

    private OnFragmentInteractionListener mListener;
    String LOG_TAG = "Fragment_FacultyProfileNew";
    Context context;
    View view = null;
    RecyclerView recycler_view = null;
    FrameLayout frameLayout = null;
    public List<DTO_Faculty_Profile> DTOFacultyProfileList = new ArrayList<>();
    FacultyProfile_Adapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    String str_discipline = "";

    public Fragment_FacultyProfileNew() {
        // Required empty public constructor
    }


    public static Fragment_FacultyProfileNew newInstance(CharSequence title, int indicatorColor, int dividerColor, CharSequence tvalue) {
        Fragment_FacultyProfileNew fragment = new Fragment_FacultyProfileNew();
        Bundle args = new Bundle();
        args.putString(IISERApp.FLAG_DISCIPLINE, title.toString());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.fragment_five);

        if (getArguments() != null) {
            str_discipline = getArguments().getString(IISERApp.FLAG_DISCIPLINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IISERApp.log(LOG_TAG, "In onCreateView");
        context = container.getContext();

        if (view == null) {
            view = inflater.inflate(R.layout.fragment__faculty_profile_new, container, false);

            getIntentData();
            initComponents(view);
            initComponentListeners();
            initComponentData();
            refresh_data1();
        }
        return view;
    }

    private void initComponentData() {
        // fab_refresh=(FloatingActionButton)view.findViewById(R.id.fab);
    }


    private void getIntentData() {
        IISERApp.log(LOG_TAG, "In getIntentData");

    }

    public void initComponents(View view) {
        IISERApp.log(LOG_TAG, "In initComponents");
        Bundle bundle = getArguments();
        str_discipline = bundle.get(IISERApp.FLAG_DISCIPLINE).toString();
        IISERApp.log(LOG_TAG, "discipline->" + str_discipline);
        //  Toast.makeText(context,"1->"+str_discipline,Toast.LENGTH_SHORT).show();

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        fab_refresh = (FloatingActionButton) view.findViewById(R.id.fab);

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Please wait... Refreshing Data.", Toast.LENGTH_LONG).show();
                refresh_data();
            }
        });
        DTOFacultyProfileList = new ArrayList<DTO_Faculty_Profile>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
        // prepareMovieData();
/*
        DTOFacultyProfileList= TABLE_FACULTY_PROFILE.get_faculty_details_with_Discipline(str_discipline);
        recyclerAdapter = new FacultyProfile_Adapter(DTOFacultyProfileList);
        recycler_view.setAdapter(recyclerAdapter);
*/
        //refresh_data1();
    }

    public void refresh_data1() {
        IISERApp.log(LOG_TAG, "IN FRAGMENT PROFIE ");
        DTOFacultyProfileList = TABLE_FACULTY_PROFILE.get_faculty_details_with_Discipline(str_discipline);
        IISERApp.log(LOG_TAG, "IN FRAGMENT PROFIE DTOFacultyProfileList" + DTOFacultyProfileList.size());
        recyclerAdapter = new FacultyProfile_Adapter(DTOFacultyProfileList);
        recycler_view.setAdapter(recyclerAdapter);
    }

    private void initComponentListeners() {
        IISERApp.log(LOG_TAG, "In initComponentListeners");
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IISERApp.log(LOG_TAG, "button pressed for refreshing.");
                //Toast.makeText(ActivityFacultyProfile.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();
                refresh_data();

            }
        });
    }

    public class FacultyProfile_Adapter extends RecyclerView.Adapter<FacultyProfile_Adapter.MyViewHolder> {

        private List<DTO_Faculty_Profile> faculty_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView faculty_id, faculty_name, faculty_degree, faculty_designation, faculty_research,
                    faculty_email_id, faculty_mobile_no, faculty_personalpage;

            ImageView faculty_photo_url;

            public MyViewHolder(View view) {
                super(view);
                faculty_id = (TextView) view.findViewById(R.id.txt_faculty_id);
                faculty_name = (TextView) view.findViewById(R.id.txt_faculty_name);
                faculty_degree = (TextView) view.findViewById(R.id.txt_faculty_degree);
                faculty_designation = (TextView) view.findViewById(R.id.txt_faculty_designation);
                faculty_research = (TextView) view.findViewById(R.id.txt_faculty_research);
                faculty_mobile_no = (TextView) view.findViewById(R.id.txt_faculty_mobileno);
                faculty_email_id = (TextView) view.findViewById(R.id.txt_faculty_emailid);
                faculty_personalpage = (TextView) view.findViewById(R.id.txt_faculty_personalpage);
                faculty_photo_url = (ImageView) view.findViewById(R.id.img_profile_photo);
            }
        }


        public FacultyProfile_Adapter(List<DTO_Faculty_Profile> faculty_List) {
            this.faculty_List = faculty_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_row_faculty_profile, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DTO_Faculty_Profile DTOFacultyProfile = faculty_List.get(position);


            holder.faculty_id.setText(DTOFacultyProfile.getStr_faculty_id());
            holder.faculty_name.setText(DTOFacultyProfile.getStr_faculty_name());
            holder.faculty_degree.setText(DTOFacultyProfile.getStr_faculty_degree());
            holder.faculty_designation.setText(DTOFacultyProfile.getStr_faculty_designation());
            holder.faculty_research.setText(DTOFacultyProfile.getStr_faculty_research());
            holder.faculty_mobile_no.setText(DTOFacultyProfile.getStr_faculty_mobile_no());
            holder.faculty_email_id.setText(DTOFacultyProfile.getStr_faculty_email_id());
            holder.faculty_personalpage.setText(DTOFacultyProfile.getStr_faculty_personal_page());
            //holder.faculty_photo_url.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }

        @Override
        public int getItemCount() {
            return faculty_List.size();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void prepareMovieData() {


        DTO_Faculty_Profile DTOFacultyProfile = new DTO_Faculty_Profile("1", "Dr.Aloke Das", "Chemistry", "Associate Professor",
                "Gas phase laser spectroscopy, Non-covalent interactions in biomolecules", "a.das@iiserpune.ac.in", "+91 (20) 2590 8078",
                "", "http://www.iiserpune.ac.in/~a.das");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("2", "Prof. Sanjeev Galande", "Biology", "Professor",
                "Epigenetics, Chromatin biology, Regulation of gene expression, Nuclear organization and function", "sanjeev@iiserpune.ac.in",
                "+91 20 2590 8060", "", "http://www.iiserpune.ac.in/~sanjeev/");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("3", "Dr. John Mathew", "Humanities and Social Sciences", "Associate Professor",
                "Natural History, Colonial History of Science, History of Zoology, Environmental History", "john.mathew@iiserpune.ac.in"
                , "+91 (20) 2590 8265", "", "http://www.iiserpune.ac.in/~john");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("4", "Prof. A. Raghuram", "Mathematics", "Professor and Coordinator",
                "Langlands Program, Representations of p-adic and adelic groups and their associated L-functions", "",
                "", "", "https://sites.google.com/site/math4raghuram/");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("5", "Dr.Aloke Das", "Chemistry", "Associate Professor",
                "Gas phase laser spectroscopy, Non-covalent interactions in biomolecules", "a.das@iiserpune.ac.in", "+91 (20) 2590 8078",
                "", "http://www.iiserpune.ac.in/~a.das");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("6", "Prof. Sanjeev Galande", "Biology", "Professor",
                "Epigenetics, Chromatin biology, Regulation of gene expression, Nuclear organization and function", "sanjeev@iiserpune.ac.in",
                "+91 20 2590 8060", "", "http://www.iiserpune.ac.in/~sanjeev/");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("7", "Dr. John Mathew", "Humanities and Social Sciences", "Associate Professor",
                "Natural History, Colonial History of Science, History of Zoology, Environmental History", "john.mathew@iiserpune.ac.in"
                , "+91 (20) 2590 8265", "", "http://www.iiserpune.ac.in/~john");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("8", "Prof. A. Raghuram", "Mathematics", "Professor and Coordinator",
                "Langlands Program, Representations of p-adic and adelic groups and their associated L-functions", "",
                "", "", "https://sites.google.com/site/math4raghuram/");
        DTOFacultyProfileList.add(DTOFacultyProfile);

    }

    public void refresh_data() {
        if (((IISERApp) getActivity().getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG, "button is pressed for refreshing in FACULTY");
            IISERApp.set_session(IISERApp.SESSION_FACULTY_PROFILE, "Yes");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_FACULTY);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_FACULTY);
            intent1.putExtra("Activity_name", "Fragment_FacultyProfileNew");
            context.startService(intent1);
        } else {
            ((IISERApp) getContext()).isInternetAvailable();
        }
    }
}
