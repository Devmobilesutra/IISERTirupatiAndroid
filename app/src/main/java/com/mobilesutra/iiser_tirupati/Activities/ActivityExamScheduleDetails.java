package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Model.DTO_ExamSchedule;
import com.mobilesutra.iiser_tirupati.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalyani on 22/04/2016.
 */
public class ActivityExamScheduleDetails extends Activity {

    ImageButton back=null;

    Context context=null;
    RecyclerView recycler_view = null;
    public List<DTO_ExamSchedule> assign_List = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_exam_sch_detail);

        getIntentData();
        initComponents();
        initComponentsListeners();
        bindComponentData();
    }

    private void getIntentData() {
    }

    private void initComponents() {
        back= (ImageButton) findViewById(R.id.img_back);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_event_view);
        assign_List = new ArrayList<DTO_ExamSchedule>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
    }

    private void initComponentsListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                } else {
                    Log.i("MainActivity", "nothing on backstack, calling super");

                }
                finish();
            }
        });
    }

    private void bindComponentData() {
        for(int i=0;i<10;i++)
        {
            DTO_ExamSchedule assignment=new DTO_ExamSchedule("","22","APR","SEM-II","English","10.30-12.30 am","","",
                    "Mr.Prakash","R1004");
            assign_List.add(assignment);
        }
        recyclerAdapter = new EventAdapter(assign_List);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

    }
    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<DTO_ExamSchedule> assign_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView exam_date,exam_time,sub_name,supervisor_name,venue;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                exam_date = (TextView) view.findViewById(R.id.txt_exam_date);
                exam_time= (TextView) view.findViewById(R.id.txt_exam_time);
                sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
                supervisor_name= (TextView) view.findViewById(R.id.txt_supervisor_name);
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
                    .inflate(R.layout.activity_exam_sc_detail_row, parent, false);

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
            holder.exam_time.setText(assign.getStr_exam_time());
            holder.sub_name.setText(assign.getStr_exam_sub_name());
            holder.supervisor_name.setText(assign.getStr_exam_supervisr_name());
            holder.venue.setText(assign.getStr_exam_venue());
        }

        @Override
        public int getItemCount() {
            return assign_List.size();
        }


    }
}
