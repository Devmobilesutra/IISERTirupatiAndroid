package com.mobilesutra.iiser_tirupati.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Activities.ActivityAdmissionDetails;
import com.mobilesutra.iiser_tirupati.Model.DTO_Courses_offered;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;


public class FragmentFour extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Courses_offered> course_List = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    SliderLayout mDemoSlider = null;

    String[] course_array = {"BS-MS Programme", "Integrated PhD Programme", "PhD Program"};

    public FragmentFour() {
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
//return inflater.inflate(R.layout.fragment_five, container, false);
        final View view = inflater.inflate(R.layout.fragment_three, container, false);

        initComponentData(view);
        initComponentListeners();
        bindComponentData();
        displayBanner(view);
        return view;
    }

    private void initComponentData(View view) {

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);
        course_List = new ArrayList<DTO_Courses_offered>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

    }

    private void initComponentListeners() {
    }

    private void bindComponentData() {


        for (int i = 0; i < course_array.length; i++) {
            DTO_Courses_offered assignment = new DTO_Courses_offered(course_array[i]);
            course_List.add(assignment);
        }
        recyclerAdapter = new EventAdapter(course_List);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

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

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<DTO_Courses_offered> course_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView course_name;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                course_name = (TextView) view.findViewById(R.id.txt_course_name);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);

            }
        }


        public EventAdapter(List<DTO_Courses_offered> course_List) {
            this.course_List = course_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_course_list_row, parent, false);

           /* Log.d("Fragment","position of view:"+viewType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Fragment","position of view:"+view.getTag());
                    Intent intent = new Intent(getContext(), ActivityCourseDetails.class);
                    intent.putExtra("position",String.valueOf(view.getTag()));
                    startActivity(intent);


                }
            });*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_Courses_offered assign = course_List.get(position);
            Log.d("Fragment", "course name:" + assign.getStr_course_name());
            holder.course_name.setText(assign.getStr_course_name());


            holder.lnrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Fragment", "position of view:" + position);
                    Intent intent = new Intent(getContext(), ActivityAdmissionDetails.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return course_List.size();
        }


    }
}
