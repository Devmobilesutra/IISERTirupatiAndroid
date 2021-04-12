package com.mobilesutra.iiser_tirupati.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Model.DTO_ExamSchedule;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ExamSchedule extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context=null;
    RecyclerView recycler_view = null;
    public List<DTO_ExamSchedule> assign_List = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    //LOG_TAG = "Exam_Sechdule";

    SliderLayout mDemoSlider=null;

    public Fragment_ExamSchedule() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_five, container, false);
        final View view = inflater.inflate(R.layout.activity_fragment_exam_schedule, container, false);

        initComponentData(view);
        initComponentListeners();
       bindComponentData();
        displayBanner(view);
        return view;
    }

    private void initComponentData(View view) {

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);
        assign_List = new ArrayList<DTO_ExamSchedule>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

    }

    private void initComponentListeners() {
    }
    private void bindComponentData() {


        for(int i=0;i<10;i++)
        {
            DTO_ExamSchedule assignment=new DTO_ExamSchedule("","22","APR","Mid Semester Examination","Chemistry"
                    ,"10.30-12.30 pm","","","Mr.Prakash","Aryabhatt Hall");
            assign_List.add(assignment);
            //IISERApp.log(LOG_TAG, "student_calendar: " + assignment);
        }
        recyclerAdapter = new EventAdapter(assign_List);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

    }

  /* public void refreshData() {
        bindComponentData();
    }*/
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

        private List<DTO_ExamSchedule> assign_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView exam_date,exam_month,exam_title,exam_time,sub_name,supervisor_name,venue;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                exam_date = (TextView) view.findViewById(R.id.txt_exam_date);
                exam_month = (TextView) view.findViewById(R.id.txt_exam_month);
                exam_title= (TextView) view.findViewById(R.id.txt_exam_title);
                exam_time= (TextView) view.findViewById(R.id.txt_exam_time);
                sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
              //  supervisor_name= (TextView) view.findViewById(R.id.txt_supervisor_name);
                venue = (TextView) view.findViewById(R.id.txt_venue);
                lnrLayout = (LinearLayout)view.findViewById(R.id.lnrlayout_event_cardview);

            }
        }


        public EventAdapter(List<DTO_ExamSchedule> assign_List) {
            this.assign_List = assign_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_exam_schedule_row, parent, false);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(getContext(),ActivityExamScheduleDetails.class);
                    startActivity(intent);
                }
            });*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_ExamSchedule assign = assign_List.get(position);
            holder.exam_date.setText(assign.getStr_exam_date());
            holder.exam_month.setText(assign.getStr_exam_month());
            holder.exam_title.setText(assign.getStr_exam_title());
            holder.exam_time.setText(assign.getStr_exam_time());
            holder.sub_name.setText(assign.getStr_exam_sub_name());
          //  holder.supervisor_name.setText(assign.getStr_exam_supervisr_name());
            holder.venue.setText(assign.getStr_exam_venue());
        }

        @Override
        public int getItemCount() {
            return assign_List.size();
        }


    }
}
