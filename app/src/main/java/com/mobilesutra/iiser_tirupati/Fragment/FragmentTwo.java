package com.mobilesutra.iiser_tirupati.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Profile;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;


public class FragmentTwo extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private static final String ARG_PARAM_TITLE = "title",
            LOG_TAG = "FragmentTwo";
    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Faculty_Profile> DTOFacultyProfileList = new ArrayList<>();
    FacultyProfile_Adapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    SliderLayout mDemoSlider = null;


    public FragmentTwo() {
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
        // return inflater.inflate(R.layout.fragment_two, container, false);
        final View view = inflater.inflate(R.layout.fragment_two, container, false);

        initComponentData(view);
        initComponentListeners();
        bindComponentData();
        displayBanner(view);
        return view;

    }

    private void initComponentData(View view) {
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        DTOFacultyProfileList = new ArrayList<DTO_Faculty_Profile>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
    }

    private void initComponentListeners() {
    }

    private void bindComponentData() {

        recyclerAdapter = new FacultyProfile_Adapter(DTOFacultyProfileList);
        recycler_view.setAdapter(recyclerAdapter);
        prepareMovieData();

    }

    public void displayBanner(View view) {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


        mDemoSlider = (SliderLayout) view.findViewById(R.id.sample_output);
        mDemoSlider.setFocusable(true);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);
       /* LinkedHashMap<String, String> lhm = ShopApp.db.getFrontBanner();
        int length = lhm.size();
        if (length == 0) {*/
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1, R.drawable.banner_3};

        for (int i = 0; i < 4; i++) {

            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description("")
                    .image(bannerArray[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");

            mDemoSlider.addSlider(textSliderView);
        }
       /* } else {*/

          /*  Set<String> keys = lhm.keySet();
            for (String key : keys) {
                TextSliderView textSliderView = new TextSliderView(getContext());
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get(key))
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", key);

                mDemoSlider.addSlider(textSliderView);
            }*/
          /*  for (int i = 0; i < length; i++) {

                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get("" + i))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", lhm.get("" + i));

                mDemoSlider.addSlider(textSliderView);
            }*/
        // }


    }

    private void prepareMovieData() {
        DTO_Faculty_Profile DTOFacultyProfile = new DTO_Faculty_Profile("", "Dr.Aloke Das", "", "Associate Professor",
                "Gas phase laser spectroscopy, Non-covalent interactions in biomolecules", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Prof. Sanjeev Galande", "", "Professor",
                "Epigenetics, Chromatin biology, Regulation of gene expression, Nuclear organization and function", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Dr. John Mathew", "", "Associate Professor",
                "Natural History, Colonial History of Science, History of Zoology, Environmental History", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Prof. A. Raghuram", "", "Professor and Coordinator",
                "Langlands Program, Representations of p-adic and adelic groups and their associated L-functions", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Dr.Aloke Das", "", "Associate Professor"
                , "Gas phase laser spectroscopy, Non-covalent interactions in biomolecules", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Prof. Sanjeev Galande", "", "Professor",
                "Epigenetics, Chromatin biology, Regulation of gene expression, Nuclear organization and function", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Dr. John Mathew", "", "Associate Professor",
                "Natural History, Colonial History of Science, History of Zoology, Environmental History", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        DTOFacultyProfile = new DTO_Faculty_Profile("", "Prof. A. Raghuram", "", "Professor and Coordinator",
                "Langlands Program, Representations of p-adic and adelic groups and their associated L-functions", "", "", "", "");
        DTOFacultyProfileList.add(DTOFacultyProfile);

        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

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
            holder.faculty_photo_url.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }

        @Override
        public int getItemCount() {
            return faculty_List.size();
        }
    }

}
