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

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Model.DTO_TimeTable;
import com.mobilesutra.iiser_tirupati.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TimeTable extends Fragment {

    Context context = null;
    RecyclerView recycler_view = null;

    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    static final String[] days = new String[]
            {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    Intent intent = null;
    int timetable_list_count = 0;
    View view = null;

    ArrayList<LinkedHashMap<String, String>> menuItems = new ArrayList<>();
    List<LinkedHashMap<String, String>> timetable_list = new ArrayList<>();

    private static final String ARG_PARAM_TITLE = "title";
    private static String LOG_TAG = "Fragment_TimeTable";
    String weekday = "";


    public static Fragment_TimeTable newInstance(String str_title) {
        Log.d("Fragment", "In newInstance");
        Fragment_TimeTable fragment = new Fragment_TimeTable();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        IISERApp.log(LOG_TAG, "str_title: " + str_title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment_TimeTable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.activity_fragment_timetable, container, false);
        IISERApp.log(LOG_TAG, "In oncreateview" + weekday);
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_timetable, container, false);

            Bundle args = getArguments();
            IISERApp.log(LOG_TAG, "tab title next:" + args.getCharSequence(ARG_PARAM_TITLE).toString());
            weekday = args.getCharSequence(ARG_PARAM_TITLE).toString();
            LOG_TAG=LOG_TAG+weekday;

            initComponents(view);
            bindComponentData();
            initComponentsListeners(view);
        }
        return view;
    }

    private void initComponents(View view) {


        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);
        // assign_List = new ArrayList<DTO_TimeTable>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
    }

    private void initComponentsListeners(final View view) {

    }

    private void bindComponentData() {


        if(IISERApp.get_session("onRES").equalsIgnoreCase("one"))
        {
            IISERApp.log("XYZ", "two from Activity TT onResumeFragments():");
        }
        List<DTO_TimeTable> assign_List = new ArrayList<>();
        if (view != null) {
            timetable_list = TABLE_TIMETABLE.get_timetable_details(weekday);
            assign_List.clear();

            String time="",venue="",course_id="";
            IISERApp.log(LOG_TAG, "timetable_list: " + timetable_list);
            timetable_list_count = timetable_list.size();
            for (int i = 0; i < timetable_list_count; i++) {

                 time = timetable_list.get(i).get("period_time").toString();
                 venue = timetable_list.get(i).get("venue").toString();
                 course_id = timetable_list.get(i).get("course_id").toString();
                String day=timetable_list.get(i).get("day");

                IISERApp.log("xyz", "time" + time +"venue" + venue +"course_id" + course_id );
           /* String batch = timetable_list.get(i).get("batch").toString();
            String lesson = timetable_list.get(i).get("exam_date").toString();*/
                IISERApp.log(LOG_TAG, "1assign list before clear:" + assign_List);

                IISERApp.log(LOG_TAG, " 2 1assign list after clear:" + assign_List);
                DTO_TimeTable assignment = new DTO_TimeTable(time, venue, course_id, "", "");
                assign_List.add(assignment);
                IISERApp.log("PQR", " DTO_TimeTable assignment day" + day);
                IISERApp.log("PQR", " DTO_TimeTable assignment" + assignment.getStr_time());
                IISERApp.log("PQR", " DTO_TimeTable assignment" + assignment.getStr_venue());
                IISERApp.log("PQR", " DTO_TimeTable assignment" + assignment.getStr_sub_name());
                IISERApp.log("PQR", "3 assign list after adding data clear:" + assign_List.get(i).getStr_time());
                IISERApp.log("PQR", "3 assign list after adding data clear:" + assign_List.get(i).getStr_venue());
                IISERApp.log("PQR", "3 assign list after adding data clear:" + assign_List.get(i).getStr_sub_name());

            }
            time="";venue="";course_id="";
        }
//        else
//        {
//            Toast.makeText(Fragment_TimeTable.this, "Please select courses", Toast.LENGTH_SHORT).show();
//        }
        /*for (int i = 0; i < 10; i++) {
            DTO_TimeTable assignment = new DTO_TimeTable("12.00-12.55 pm", "LHC 201", "BIO 321", "", "Lesson");
            assign_List.add(assignment);
        }*/
        IISERApp.log(LOG_TAG, "view:" + view);
        if (view != null) {
            if (recyclerAdapter == null) {
                IISERApp.log(LOG_TAG, "recyclerAdapter:null" + recyclerAdapter);
                recyclerAdapter = new EventAdapter(assign_List);
                recycler_view.setAdapter(recyclerAdapter);
            }
            else
            {
                IISERApp.log(LOG_TAG, "recyclerAdapter:not null" + recyclerAdapter);
                IISERApp.log(LOG_TAG, "assign_List size clear:" + assign_List.size());
                recyclerAdapter.assign_List.clear();
                IISERApp.log(LOG_TAG, "assign_List size after clear" + assign_List.size());
                recyclerAdapter.assign_List.addAll(assign_List);
                IISERApp.log(LOG_TAG, "assign_List size after addition" + assign_List.size());
                recyclerAdapter.notifyDataSetChanged();///-mrunal for nt repeating the data
            }
        }

    }

    public void refreshData() {

        IISERApp.log(LOG_TAG, "in refresh data:" + weekday);

        if(IISERApp.get_session("onRES").equalsIgnoreCase("one"))
        {
            IISERApp.log("XYZ", "from Activity TT onResumeFragments():");
        }
        bindComponentData();
    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<DTO_TimeTable> assign_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView time, venue, sub_name, teacher_name, lesson;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                time = (TextView) view.findViewById(R.id.txt_time);
                venue = (TextView) view.findViewById(R.id.txt_venue);
                sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
                teacher_name = (TextView) view.findViewById(R.id.txt_teacher_name);
                // lesson = (TextView) view.findViewById(R.id.txt_lesson);

                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);

            }
        }


        public EventAdapter(List<DTO_TimeTable> assign_List) {
            this.assign_List = assign_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_timetable_row, parent, false);

           /*itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   Intent intent=new Intent(getContext(),ActivityAssignmntDetails.class);
                   startActivity(intent);


               }
           });*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_TimeTable assign = assign_List.get(position);
            holder.time.setText(assign.getStr_time());
            holder.venue.setText(assign.getStr_venue());
            holder.sub_name.setText(assign.getStr_sub_name());
            holder.teacher_name.setText(assign.getStr_teacher_name());
            //  holder.lesson.setText(assign.getStr_lesson());

        }

        @Override
        public int getItemCount() {
            return assign_List.size();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        bindComponentData();


    }
}
