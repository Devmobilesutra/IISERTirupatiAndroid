package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ASSIGNMENT;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SCHEDULE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by kalyani on 28/04/2016.
 */
public class ActivityCourseSelection extends Activity {

    ImageButton back = null;
    LinearLayout lnrlayout_checkbox = null;
    TextView txt_course_selection = null;
    Button btn_apply = null;
    ProgressBar prgresbar = null;
    ProgressDialog dialog = null;


    Context context = null;
    Intent intent = null;

    String semester_name = "", LOG_TAG = "ActivityCourseSelection";


    int courses_list_count = 0;

    List<LinkedHashMap<String, String>> courses_list = new ArrayList<>();
 //   LinkedHashMap<String, String> selected_course_lhm = new LinkedHashMap<>();
    List<LinkedHashMap<String, String>> course_menuItems = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_course_selection);
        IISERApp.log(LOG_TAG, "Course are");

        context = this;
        getIntentData();
        initComponents();
        initComponentsListeners();
        bindComponentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();

        semester_name = intent.getStringExtra("semester_name");
        IISERApp.log(LOG_TAG, "semester_name:" + semester_name);
    }

    private void initComponents() {
        back = (ImageButton) findViewById(R.id.img_back);
        btn_apply = (Button) findViewById(R.id.btn_course_apply);
        prgresbar = (ProgressBar) findViewById(R.id.progressbar);
        txt_course_selection = (TextView) findViewById(R.id.txt_course_selection);
        lnrlayout_checkbox = (LinearLayout) findViewById(R.id.lnrlayout_checkbox);
    }

    private void initComponentsListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TABLE_ASSIGNMENT.delete_tbl_assignment_sync();
                TABLE_TIMETABLE.delete_tbl_timetable_sync();
                TABLE_EXAM_SCHEDULE.delete_tbl_exam_shedule_sync();

                IISERApp.log(LOG_TAG, "selected_course_lhm" + IISERApp.selected_course_lhm);
                if (IISERApp.selected_course_lhm.size() > 0) {
                    JSONArray jsn_course = new JSONArray();
                    Set<String> keys = IISERApp.selected_course_lhm.keySet();
                    for (String key : keys) {
                        IISERApp.selected_course_lhm.get(key);
                        //selected_course_lhm.setChecked(true);
                        if (IISERApp.selected_course_lhm.get(key).equalsIgnoreCase("Y")) {
                            try {
                                IISERApp.log(LOG_TAG, "IF selected_course_lhm is:" + IISERApp.selected_course_lhm);
                                JSONObject jsn_corse_obj = new JSONObject();
                                jsn_corse_obj.put("course_id", key);
                                jsn_course.put(jsn_corse_obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //jsn_course.put(key);
                        }
                        /*else
                        {
                            IISERApp.log(LOG_TAG, "You have deselected an course: " + selected_course_lhm);
                            if (selected_course_lhm.get(key).equalsIgnoreCase("N")){

                                    JSONObject jsn_corse_obj = new JSONObject();
                                    jsn_corse_obj.remove( jsn_corse_obj);
                                    //jsn_course.remove(jsn_corse_obj);

                            }
                           // jsn_course.remove(jsn_corse_obj);
                        }*/
                    }
                    IISERApp.log(LOG_TAG, "json_array: " + jsn_course);
                    if (((IISERApp) getApplication()).isInternetAvailable()) {
                        Bundle extras = new Bundle();
                        Intent intent1 = new Intent(context, IISERIntentService.class);
                        extras.putString(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_APPLY_SELECTED_COURSE);
                        extras.putString("course_json", "" + jsn_course);
                        intent1.putExtras(extras);
                      //  intent1.putExtras(extras);
                        dialog = ProgressDialog.show(context, "Please Wait...", "Applying selected courses.");
                        prgresbar.setVisibility(View.GONE);
                        context.startService(intent1);

                    } else {
                        Snackbar.make(btn_apply, "No Internet available...", Snackbar.LENGTH_LONG).show();
                    }
                } else
                    IISERApp.log(LOG_TAG, "You are in else" );
                // if (selected_course_lhm.get(key).equalsIgnoreCase("N")){


                // Toast.makeText(ActivityCourseSelection.this, "You Have Selected The Same Courses And Have Applied It Already", Toast.LENGTH_SHORT).show();
                // IISERApp.log(LOG_TAG, "You Have Selected The Same Courses And Have Applied It Already" );

            }
        });

    }

    private void bindComponentData() {


        prgresbar.setVisibility(View.GONE);
        txt_course_selection.setText("Course Selection for Semester " + semester_name);
        // courses_list = TABLE_COURSE.get_course_list(semester_name);
        courses_list = TABLE_COURSE.get_course_listnew(semester_name);
        IISERApp.log(LOG_TAG, "course_list:" + courses_list);

        courses_list_count = courses_list.size();
        IISERApp.log(LOG_TAG, "courses_list_count:" + courses_list_count);


        //code for semester above IV
       /* if (semester_name.equalsIgnoreCase("I") | semester_name.equalsIgnoreCase("II") | semester_name.equalsIgnoreCase("III")
                | semester_name.equalsIgnoreCase("IV")) {

            courselist_below_sem_IV();
        } else {*/
        int count = 0;
        ArrayList<String> subject_name = new ArrayList<String>();
        int k = 0;
        if (courses_list_count > 0) {
            while (k < courses_list_count - 1)
            {
                if (courses_list.get(k).get("subject_name").toString().equalsIgnoreCase(courses_list.get(k + 1).get("subject_name").toString())) {
                    IISERApp.log(LOG_TAG, "IF subject_name array:" + subject_name);
                    k++;

                } else {
                    //subject_name[count] = courses_list.get(j).get("subject_name").toString();
                    subject_name.add(courses_list.get(k).get("subject_name").toString());
                    IISERApp.log(LOG_TAG, " else count:" + count + " subject_name array:" + subject_name);
                    count++;
                    k++;
                }
            }

            subject_name.add(courses_list.get(k).get("subject_name").toString());
            IISERApp.log(LOG_TAG, " else count:" + count + " subject_name array:" + subject_name);
            for (int j = 0; j < subject_name.size(); j++) {

                TextView txt_sub_name = new TextView(this);
                txt_sub_name.setText(subject_name.get(j).toString());
                txt_sub_name.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                param.setMargins(10, 10, 10, 10);
                txt_sub_name.setLayoutParams(param);
                txt_sub_name.setTextColor(getResources().getColor(R.color.edittext_textcolor));
                lnrlayout_checkbox.addView(txt_sub_name);

                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                lnrlayout_checkbox.addView(v);

                for (int i = 0; i < courses_list_count; i++) {
                    if (courses_list.get(i).get("subject_name").toString().equalsIgnoreCase(subject_name.get(j).toString())) {
                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);
                        row.setPadding(5, 5, 5, 5);
                        // row.setBackgroundColor(Color.parseColor("#ffffff"));
                        row.setWeightSum(2);
                        //  row.setMinimumHeight(18);
                        TableRow.LayoutParams lp1, lp2;

                        lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f);

                        final CheckBox Values = new CheckBox(this);
                        addCheckboxToTableRow(lp1, Values, courses_list.get(i).get("course_name")
                                + "(" + courses_list.get(i).get("course_id") + ")", courses_list.get(i).get("is_selected").toString(),courses_list.get(i).get("course_id"));
                        Values.setId(i);
                        Values.setTag(courses_list.get(i).get("course_id"));
                        row.addView(Values);
                        //Values selected_course_lhm
                        //Values.setEnabled(false);
                        //selected_course_lhm.setChecked(true);

                        lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);

                        TextView txt_credit = new TextView(this);
                        txt_credit.setText(courses_list.get(i).get("credit"));
                        txt_credit.setTypeface(null, Typeface.BOLD);
                        txt_credit.setLayoutParams(lp2);
                        txt_credit.setGravity(Gravity.CENTER);
                        txt_credit.setBackgroundResource(R.drawable.circular_textvw);
                        txt_credit.setTextColor(getResources().getColor(R.color.edittext_txtcolor_white));
                        row.addView(txt_credit);


                        //////////////Check box changed
                        Values.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (Values.isChecked()) {
                                    IISERApp.log(LOG_TAG, "Cheked item:" + Values.getText().toString());
                                    IISERApp.log(LOG_TAG, "Cheked item Tag:" + Values.getTag());
                                    IISERApp. selected_course_lhm.put(String.valueOf(Values.getTag()), "Y");
                                    IISERApp.log(LOG_TAG, "selected_course_lhmm:" + IISERApp.selected_course_lhm);
                                    // lnrlayout_checkbox.addView(Values);

                                } else {
                                    Values.setChecked(false);
                                    IISERApp.log(LOG_TAG, "Cheked item else:" + Values.getText().toString());
                                    IISERApp.log(LOG_TAG, "Cheked item Tag else:" + Values.getTag());
                                   // selected_course_lhm.remove(String.valueOf(Values.getTag()), "N");
                                    //selected_course_lhm.remove(Values.getId());
                                    IISERApp.selected_course_lhm.put(String.valueOf(Values.getTag()), "N");
                                    IISERApp.log(LOG_TAG, "checked false");

                                /*for(Iterator<Map.Entry<String, String>> it = selected_course_lhm.entrySet().iterator(); it.hasNext(); ) {
                                    Map.Entry<String, String> entry = it.next();
                                    if(entry.getKey().equals("test")) {
                                        it.remove();
                                    }
                                }*/
                                    IISERApp.log(LOG_TAG, "DEselected_course_lhm:" + IISERApp.selected_course_lhm);
                                }

                            }
                        });
                        //lnrlayout_checkbox.addView(Values);
                        lnrlayout_checkbox.addView(row);
                    }
                }
            }
        }
        // }


    }

    private void courselist_below_sem_IV() {
        int count = 0;
        ArrayList<String> subject_name = new ArrayList<String>();
        int k = 0;
        if (courses_list_count > 0) {
            while (k < courses_list_count - 1) {
                if (courses_list.get(k).get("subject_name").toString().equalsIgnoreCase(courses_list.get(k + 1).get("subject_name").toString())) {
                    IISERApp.log(LOG_TAG, "if subject_name array:" + subject_name);
                    k++;

                } else {
                    //subject_name[count] = courses_list.get(j).get("subject_name").toString();

                    subject_name.add(courses_list.get(k).get("subject_name").toString());
                    IISERApp.log(LOG_TAG, " else count:" + count + " subject_name array:" + subject_name);
                    count++;
                    k++;
                }
            }

            subject_name.add(courses_list.get(k).get("subject_name").toString());
            IISERApp.log(LOG_TAG, " else count:" + count + " subject_name array:" + subject_name);
            for (int j = 0; j < subject_name.size(); j++) {

                TextView txt_sub_name = new TextView(this);
                txt_sub_name.setText(subject_name.get(j).toString());
                txt_sub_name.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                param.setMargins(10, 10, 10, 10);
                txt_sub_name.setLayoutParams(param);
                txt_sub_name.setTextColor(getResources().getColor(R.color.edittext_textcolor));
                lnrlayout_checkbox.addView(txt_sub_name);

                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                lnrlayout_checkbox.addView(v);

                for (int i = 0; i < courses_list_count; i++) {
                    if (courses_list.get(i).get("subject_name").toString().equalsIgnoreCase(subject_name.get(j).toString())) {
                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableRow.LayoutParams
                                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);
                        row.setPadding(5, 5, 5, 5);
                        // row.setBackgroundColor(Color.parseColor("#ffffff"));
                        row.setWeightSum(2);
                        //  row.setMinimumHeight(18);
                        TableRow.LayoutParams lp1, lp2;

                        lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f);

                        final CheckBox Values = new CheckBox(this);
                        addCheckboxToTableRow(lp1, Values, courses_list.get(i).get("course_name")
                                + "(" + courses_list.get(i).get("course_id") + ")", courses_list.get(i).get("is_selected").toString(),courses_list.get(i).get("course_id"));
                        Values.setId(i);
                        // Values.setChecked(true);
                        // Values.setEnabled(false);
                        Values.setTag(courses_list.get(i).get("course_id"));
                        IISERApp.selected_course_lhm.put(courses_list.get(i).get("course_id"), "Y");
                        IISERApp.log(LOG_TAG, "selected_course_lhm:" + IISERApp.selected_course_lhm);

                        row.addView(Values);

                        lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f);

                        TextView txt_credit = new TextView(this);
                        txt_credit.setText(courses_list.get(i).get("credit"));
                        txt_credit.setTypeface(null, Typeface.BOLD);
                        txt_credit.setLayoutParams(lp2);
                        txt_credit.setGravity(Gravity.CENTER);
                        txt_credit.setBackgroundResource(R.drawable.circular_textvw);
                        txt_credit.setTextColor(getResources().getColor(R.color.edittext_txtcolor_white));
                        row.addView(txt_credit);

                        Values.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (Values.isChecked()) {
                                    IISERApp.log(LOG_TAG, "Cheked item:" + Values.getText().toString());
                                    IISERApp.log(LOG_TAG, "Cheked item Tag:" + Values.getTag());
                                    IISERApp.selected_course_lhm.put(String.valueOf(Values.getTag()), "Y");
                                    IISERApp.log(LOG_TAG, "selected_course_lhm:" + IISERApp.selected_course_lhm);

                                } else {
                                    Values.setChecked(false);
                                    IISERApp.log(LOG_TAG, "Cheked item else:" + Values.getText().toString());
                                    IISERApp.log(LOG_TAG, "Cheked item Tag else:" + Values.getTag());
                                    IISERApp.selected_course_lhm.put(String.valueOf(Values.getTag()), "N");

                                    IISERApp.log(LOG_TAG, "selected_course_lhm:" + IISERApp.selected_course_lhm);
                                }

                            }
                        });
                        lnrlayout_checkbox.addView(row);
                    }
                }
            }
        }
    }

    private void addCheckboxToTableRow(LinearLayout.LayoutParams lp1, CheckBox Values2, String chkbx_text, String is_selected,String str_course_code){

        Values2.setTextSize(13.0f);
        Values2.setTextColor(getResources().getColor(R.color.edittext_textcolor));
        IISERApp.log(LOG_TAG, "chkbx_text:" + chkbx_text);
        Values2.setText(chkbx_text);
        if (is_selected.equalsIgnoreCase("Y")) {
            Values2.setChecked(true);
            IISERApp.selected_course_lhm.put(str_course_code,"Y");
        }
        else
            Values2.setChecked(false);
        Values2.setLayoutParams(lp1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IISERApp.log(LOG_TAG, "ondestroy");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        context.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
//selected_course_lhm.setChecked(true)
        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));

    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();

            IISERApp.log_bundle(bundle);

           /* if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
                *//*Intent intent1 = new Intent(context, ActivityCourseSelection.class);
                startActivity(intent1);
                finish();*//*
                initComponents();
                initComponentsListeners();
                bindComponentData();
                prgresbar.setVisibility(View.GONE);
               Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();

            } else {
                prgresbar.setVisibility(View.GONE);
                Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }*/

            if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {

                // prgresbar.setVisibility(View.GONE);
                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("1")) {
                    IISERApp.log(LOG_TAG, "in if condition of response stastus 1");
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                        dialog.dismiss();
                       // Toast.makeText(ActivityCourseSelection.this, "Courses applied sucessfully...", Toast.LENGTH_SHORT).show();

                        if (IISERApp.selected_course_lhm.size() > 0) {

                            Set<String> keys = IISERApp.selected_course_lhm.keySet();
                            for (String key : keys) {
                                IISERApp.selected_course_lhm.get(key);
                                IISERApp.log(LOG_TAG, "select" + key + "-" + IISERApp.selected_course_lhm.get(key));
                                if (IISERApp.selected_course_lhm.get(key).equalsIgnoreCase("Y")) {

                                }
                            }
                        }
                        //TABLE_COURSE.updateSelectedCourse();
                        IISERApp.log(LOG_TAG, "in if conditiense stastus 1 if dialog showing..");
                        // Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
                        initComponents();
                        initComponentsListeners();
                        bindComponentData();
                    }
                } else {
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                        dialog.dismiss();
                        Toast.makeText(ActivityCourseSelection.this, "Courses applied sucessfully...", Toast.LENGTH_SHORT).show();
                    }
                }

                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("0")) {
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.hide();
                    }
                }

            } else {
                // prgresbar.setVisibility(View.GONE);
                if (dialog!= null && dialog.isShowing())
                    dialog.dismiss();
                //Toast.makeText(ActivityCourseSelection.this, "Courses applied sucessfully...", Toast.LENGTH_SHORT).show();
                // Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
