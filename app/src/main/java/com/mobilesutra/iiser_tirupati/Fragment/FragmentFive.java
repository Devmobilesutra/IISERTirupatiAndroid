package com.mobilesutra.iiser_tirupati.Fragment;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Activities.ActivityEventDetails;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTO_Event;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentFive extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Event> DTOEventList = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    SliderLayout mDemoSlider = null;
    FloatingActionButton fab = null;

    private PendingIntent pendingIntent;

    public FragmentFive() {
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
        final View view = inflater.inflate(R.layout.fragment_five, container, false);

        initComponentData(view);
        initComponentListeners();
        bindComponentData();
        displayBanner(view);
        return view;
    }

    private void initComponentData(View view) {

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        DTOEventList = new ArrayList<DTO_Event>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

    }

    private void initComponentListeners() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                // Toast.makeText(getContext(),"You want to set alarm for event??",Toast.LENGTH_LONG);
                /*Intent intent=new Intent(getContext(),ActivityAssignmntDetails.class);
                startActivity(intent);*/
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", false);
                intent.putExtra("rrule", "FREQ=DAILY");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "A Test DTO_Event from android app");
                startActivity(intent);
            }
        });
    }

    private void bindComponentData() {


        DTO_Event DTOEvent = new DTO_Event("","APR", "21", "Teacher Training Programme for School Teachers","", "Dates:21-22 April",
                "","IISER Pune");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("","JUL","4", "Conference on Metapletic groups","", "Dates:4-9 July","",
                "Madhava Hall,3rd floor,IISER Pune");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("","JUL", "10", "44th National Seminar on Crystallography (NSC44)","",
                "Dates:10-13 July","", "Lecture Hall Complex at IISER Pune and NCL Auditorium");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("","DES", "15", "Indian Strings Meeting(ISM)","", "Dates:15-21 DES","", "IISER Pune");
        DTOEventList.add(DTOEvent);


        recyclerAdapter = new EventAdapter(DTOEventList);
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
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1,  R.drawable.banner_3};

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

        private List<DTO_Event> DTOEventList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView event_title, event_date, event_venue, event_month, event_day;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                event_month = (TextView) view.findViewById(R.id.txt_month);
                event_day = (TextView) view.findViewById(R.id.txt_day);
                event_title = (TextView) view.findViewById(R.id.txt_event_header);
                event_date = (TextView) view.findViewById(R.id.txt_event_organiser);
                event_venue = (TextView) view.findViewById(R.id.txt_event_venue);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);

            }
        }


        public EventAdapter(List<DTO_Event> DTOEventList) {
            this.DTOEventList = DTOEventList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_row_event, parent, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("FragmentFive", "clicked on item");
                    if (IISERApp.hasLollipop()) {
                        Intent intent = new Intent(getContext(), ActivityEventDetails.class);
                        startActivity(intent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tutorialspoint.com/android/android_tutorial.pdf"));
                        startActivity(browserIntent);
                    }

                }
            });
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_Event DTOEvent = DTOEventList.get(position);
            holder.event_month.setText(DTOEvent.getStr_event_month());
            holder.event_day.setText(DTOEvent.getStr_event_day());
            holder.event_title.setText(DTOEvent.getStr_event_title());
            holder.event_date.setText(DTOEvent.getStr_event_date());
            holder.event_venue.setText(DTOEvent.getStr_event_venue());

        }

        @Override
        public int getItemCount() {
            return DTOEventList.size();
        }


    }
}
