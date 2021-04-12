package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ACADMIC_CALENDER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ASSIGNMENT;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_CALENDAR;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SCHEDULE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SUPERVISOR;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_CALENDAR;
import com.mobilesutra.iiser_tirupati.Database.TABLE_NOTICE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_STUDENT_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_USER_PROFILE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_WEBSERVICE;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by kalyani on 26/04/2016.
 */
public class ActivityProfile extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private static final String ARG_PARAM_TITLE = "title", LOG_TAG = "ActivityProfile";
    Context context = null;
    SliderLayout mDemoSlider = null;
    FloatingActionButton fab = null;
    FloatingActionButton logout = null;

    EditText edtxt_name = null, edtxt_email_id = null, edtxt_mobile_no = null, edtxt_username = null, edtxt_password = null,
            edtxt_roll_no = null, edtxt_semester_name = null, edtxt_degree = null, edtxt_designation = null,
            edtxt_research = null;
    TextView txt_batch_selection = null;
    CheckBox mCbShowPwd;

    Button btn_update = null,add_courses = null;
    ImageView img_user_photo = null;
    RadioButton radiobtn1 = null, radiobtn2 = null, radiobtn3 = null, radiobtn4 = null;
    ProgressBar prgresbar = null;
    ProgressDialog dialog = null;

    String str_name = "", str_email_id = "", str_mobile_no = "", str_username = "", str_password = "",
            str_roll_no = "", str_semester_name = "", str_degree = "", str_designation = "", str_research = "",
            str_batch = "", str_personal_page = "";
    LinearLayout lnrlayout_designation = null, lnrlayout_research = null, lnrlayout_roll_no = null, lnrlayout_semester_name = null,
            lnrlayout_degree = null, lnrlayout_batch_selection = null;

    List<LinkedHashMap<String, String>> student_data = new ArrayList<>(), faculty_data = new ArrayList<>(), supervisor_data = new ArrayList<>();
    String[] sem_name_arr = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X","IPhD","PhD"};
    Spinner spnr_semester = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_fragment_profile);

        context = this;
        getIntentData();
        initComponents();
        bindComponentData();
        initComponentsListeners();
        //  displayBanner();

        final ScrollView main = (ScrollView) findViewById(R.id.scrollView123);
        main.post(new Runnable() {
            public void run() {
                main.scrollTo(0, 0);
            }
        });
    }

    private void getIntentData() {

    }

    private void initComponents() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        logout = (FloatingActionButton) findViewById(R.id.logout);
        prgresbar = (ProgressBar) findViewById(R.id.progressbar);
        mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);
        edtxt_name = (EditText) findViewById(R.id.edtxt_name);
        edtxt_email_id = (EditText) findViewById(R.id.edtxt_email_id);
        edtxt_mobile_no = (EditText) findViewById(R.id.edtxt_mobile_no);
        edtxt_username = (EditText) findViewById(R.id.edtxt_username);
        edtxt_password = (EditText) findViewById(R.id.edtxt_password);
        edtxt_roll_no = (EditText) findViewById(R.id.edtxt_roll_no);
        //  edtxt_semester_name = (EditText) findViewById(R.id.edtxt_semester_name);
        spnr_semester = (Spinner) findViewById(R.id.edtxt_semester_name);
        edtxt_degree = (EditText) findViewById(R.id.edtxt_degree);
        edtxt_designation = (EditText) findViewById(R.id.edtxt_designation);
        edtxt_research = (EditText) findViewById(R.id.edtxt_research);
        add_courses =(Button)findViewById(R.id.add_courses);

        txt_batch_selection = (TextView) findViewById(R.id.txt_batch_selection);
        txt_batch_selection.setVisibility(View.GONE);
        // entered_email = edit_email.getText().toString();
        btn_update = (Button) findViewById(R.id.btn_update);
        img_user_photo = (ImageView) findViewById(R.id.img_profile_photo);
        // logout = (Button)findViewById(R.id.logout);

        radiobtn1 = (RadioButton) findViewById(R.id.radiobtn1);
        radiobtn2 = (RadioButton) findViewById(R.id.radiobtn2);
        radiobtn3 = (RadioButton) findViewById(R.id.radiobtn3);
        radiobtn4 = (RadioButton) findViewById(R.id.radiobtn4);

        lnrlayout_roll_no = (LinearLayout) findViewById(R.id.lnrlayout_roll_no);
        lnrlayout_semester_name = (LinearLayout) findViewById(R.id.lnrlayout_semester_name);
        lnrlayout_degree = (LinearLayout) findViewById(R.id.lnrlayout_degree);
        lnrlayout_designation = (LinearLayout) findViewById(R.id.lnrlayout_designation);
        lnrlayout_research = (LinearLayout) findViewById(R.id.lnrlayout_research);
        lnrlayout_batch_selection = (LinearLayout) findViewById(R.id.lnrlayout_batch_selection);
    }

    private void set_student_data()
    {
        edtxt_name.setText(student_data.get(0).get("student_name").toString());
        add_courses.setVisibility(View.VISIBLE);
        IISERApp.set_session(IISERApp.SESSION_STUDENT_NAME, student_data.get(0).get("student_name").toString());
       /*String name=student_data.get(0).get("student_name").toString();
        if (!(name.equalsIgnoreCase("null"))){
            IISERApp.log(LOG_TAG,"In If " + name);
            edtxt_name.setText(name);
        }*/



        edtxt_email_id.setText(student_data.get(0).get("email_id").toString());

        edtxt_mobile_no.setText(student_data.get(0).get("mobile_no").toString());
        edtxt_username.setText(student_data.get(0).get("username").toString());
        edtxt_password.setText(student_data.get(0).get("password").toString());
        edtxt_roll_no.setText(student_data.get(0).get("roll_no").toString());
       // edtxt_semester_name.setText(student_data.get(0).get("semester_name").toString());
        IISERApp.set_session(IISERApp.SESSION_SEMESTER_NAME, student_data.get(0).get("semester_name").toString());

        if (student_data.get(0).get("batch_id").equalsIgnoreCase("b1")) {

            radiobtn1.setChecked(true);

        } else if (student_data.get(0).get("batch_id").equalsIgnoreCase("b2")) {
            radiobtn2.setChecked(true);
        } else if (student_data.get(0).get("batch_id").equalsIgnoreCase("b3")) {
            radiobtn3.setChecked(true);
        } else if (student_data.get(0).get("batch_id").equalsIgnoreCase("b4")) {
            radiobtn4.setChecked(true);
        }
        IISERApp.set_session(IISERApp.SESSION_BATCH, student_data.get(0).get("batch_id").toString());


        IISERApp.log(LOG_TAG,"in get session batch"+IISERApp.get_session(IISERApp.SESSION_BATCH));


        String sem_name = IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME);


        ArrayAdapter semester_adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sem_name_arr);
        semester_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_semester.setAdapter(semester_adapter);
        int spinnerPosition = semester_adapter.getPosition(sem_name);
        //set the default according to value
        spnr_semester.setSelection(spinnerPosition);

       /* if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("I")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("II")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("III")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("IV")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("V")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("VI")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("VII")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("VIII")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        }*/

        visible_batch_block();



       /* if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("I")){
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("II")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("III")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("IV")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("V")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("VI")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("VII")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        }else  if(student_data.get(0).get(str_semester_name).equalsIgnoreCase("VIII")) {
            lnrlayout_batch_selection.setVisibility(View.GONE);
        }*/
    }

    private void visible_batch_block() {
        if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("I")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("II")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("III")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("IV")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("V")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("VI")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("VII")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("VIII")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("IX")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        } else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("X")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("PhD")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }else if (IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME).equalsIgnoreCase("IPhD")) {
            lnrlayout_batch_selection.setVisibility(View.VISIBLE);
        }
    }

    private void set_faculty_data() {
        edtxt_name.setText(faculty_data.get(0).get("student_name").toString());
        edtxt_email_id.setText(faculty_data.get(0).get("email_id").toString());
        edtxt_mobile_no.setText(faculty_data.get(0).get("mobile_no").toString());
        edtxt_username.setText(faculty_data.get(0).get("username").toString());
        edtxt_password.setText(faculty_data.get(0).get("password").toString());
        edtxt_degree.setText(faculty_data.get(0).get("degree").toString());
        edtxt_designation.setText(faculty_data.get(0).get("designation").toString());
        edtxt_research.setText(faculty_data.get(0).get("research").toString());
    }

    private void set_supervisor_data() {
        edtxt_name.setText(supervisor_data.get(0).get("student_name").toString());
        edtxt_email_id.setText(supervisor_data.get(0).get("email_id").toString());
        edtxt_mobile_no.setText(supervisor_data.get(0).get("mobile_no").toString());
        edtxt_username.setText(supervisor_data.get(0).get("username").toString());
        edtxt_password.setText(supervisor_data.get(0).get("password").toString());
        edtxt_degree.setText(supervisor_data.get(0).get("degree").toString());
        edtxt_designation.setText(supervisor_data.get(0).get("designation").toString());
        edtxt_research.setText(supervisor_data.get(0).get("research").toString());
    }


    private void get_faculty_data() {
        str_name = edtxt_name.getText().toString();
        str_email_id = edtxt_email_id.getText().toString();
        str_mobile_no = edtxt_mobile_no.getText().toString();
        str_username = edtxt_username.getText().toString();
        str_password = edtxt_password.getText().toString();
        str_degree = edtxt_degree.getText().toString();
        str_designation = edtxt_designation.getText().toString();
        str_research = edtxt_research.getText().toString();
        str_personal_page = "";

    }

    private void get_supervisor_data() {
        str_name = edtxt_name.getText().toString();
        str_email_id = edtxt_email_id.getText().toString();
        str_mobile_no = edtxt_mobile_no.getText().toString();
        str_username = edtxt_username.getText().toString();
        str_password = edtxt_password.getText().toString();
        str_degree = edtxt_degree.getText().toString();
        str_designation = edtxt_designation.getText().toString();
        str_research = edtxt_research.getText().toString();
        str_personal_page = "";

    }

    private void get_student_data() {
        str_name = edtxt_name.getText().toString();
        str_email_id = edtxt_email_id.getText().toString();
        str_mobile_no = edtxt_mobile_no.getText().toString();
        str_username = edtxt_username.getText().toString();
        str_password = edtxt_password.getText().toString();
        str_roll_no = edtxt_roll_no.getText().toString();
        IISERApp.log("abcc", "in activity profile is->" + str_roll_no);
       // str_semester_name = edtxt_semester_name.getText().toString();
        str_semester_name = spnr_semester.getSelectedItem().toString();
        IISERApp.set_session(IISERApp.SESSION_SEMESTER_NAME,str_semester_name);
        IISERApp.log(LOG_TAG, "sem in selected_semester:" +  IISERApp.get_session(IISERApp.SESSION_SEMESTER_NAME));

    }

    private void initComponentsListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                   // String sem_name = edtxt_semester_name.getText().toString();
                    String sem_name = spnr_semester.getSelectedItem().toString();
                    /*if(sem_name.equalsIgnoreCase("V") |  sem_name.equalsIgnoreCase("VI") | sem_name.equalsIgnoreCase("VII")
                            | sem_name.equalsIgnoreCase("VIII") | sem_name.equalsIgnoreCase("XI") | sem_name.equalsIgnoreCase("X")) {
                       */
                    Intent intent = new Intent(ActivityProfile.this, ActivityCourseSelection.class);
                    intent.putExtra("semester_name", sem_name);
                    startActivity(intent);
                    //  finish();
                    //}
                }
            }
        });

        add_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                    // String sem_name = edtxt_semester_name.getText().toString();
                    String sem_name = spnr_semester.getSelectedItem().toString();
                    /*if(sem_name.equalsIgnoreCase("V") |  sem_name.equalsIgnoreCase("VI") | sem_name.equalsIgnoreCase("VII")
                            | sem_name.equalsIgnoreCase("VIII") | sem_name.equalsIgnoreCase("XI") | sem_name.equalsIgnoreCase("X")) {
                       */
                    Intent intent = new Intent(ActivityProfile.this, ActivityCourseSelection.class);
                    intent.putExtra("semester_name", sem_name);
                    startActivity(intent);
                    //  finish();
                    //}
                }
            }
        });


        spnr_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                IISERApp.log(LOG_TAG,"selected_semester:"+item);
                IISERApp.set_session(IISERApp.SESSION_SEMESTER_NAME,item);
                visible_batch_block();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                IISERApp.set_session(IISERApp.SESSION_LOGIN_FLAG, "N");
                intent = new Intent(context, ActivityLogin.class);
                TABLE_TIMETABLE.delete_tbl_timetable_sync();

                TABLE_ACADMIC_CALENDER.delete_tbl_aca_cal_sync();
                TABLE_ASSIGNMENT.delete_tbl_assignment_sync();
                TABLE_CALENDAR.delete_tbl_cal();
             //   TABLE_TIMETABLE.delete_tbl_timetable_sync();
                TABLE_COURSE.delete_tbl_course();
                //TABLE_EVENT.delete_tbl_event_sync();
                TABLE_EXAM_SCHEDULE.delete_tbl_exam_shedule_sync();
                TABLE_EXAM_SUPERVISOR.delete_tbl_exam_supervisor();
                TABLE_FACULTY_CALENDAR.delete_tbl_faculty_cal();
                // TABLE_FACULTY_PROFILE.delete_tbl_faculty_profile();
                TABLE_NOTICE.delete_notice();
                TABLE_USER_PROFILE.delete_tbl_user_profile_sync();
                TABLE_WEBSERVICE.delete_tbl_web_service();
                TABLE_ATTENDENCE_MASTER.deleteAllRecord();
                TABLE_STUDENT_ATTENDENCE.deleteAllRecord();
                TABLE_FACULTY_ATTENDENCE.deleteAllRecord();
                IISERApp.delet_log_Details();

                startActivity(intent);
                finish();
            }
        });

        radiobtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radiobtn1.isChecked()) {
                    str_batch = radiobtn1.getText().toString();
                    IISERApp.log(LOG_TAG, "str_batch: " + str_batch);

                    radiobtn2.setChecked(false);
                    radiobtn3.setChecked(false);
                    radiobtn4.setChecked(false);
                    IISERApp.set_session(IISERApp.SESSION_BATCH, str_batch);


                }
            }
        });
        radiobtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radiobtn2.isChecked()) {
                    str_batch = radiobtn2.getText().toString();
                    IISERApp.log(LOG_TAG, "str_batch: " + str_batch);
                    radiobtn1.setChecked(false);
                    radiobtn3.setChecked(false);
                    radiobtn4.setChecked(false);
                    IISERApp.set_session(IISERApp.SESSION_BATCH, str_batch);
                }
            }
        });
        radiobtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radiobtn3.isChecked()) {
                    str_batch = radiobtn3.getText().toString();

                    IISERApp.log(LOG_TAG, "str_batch: " + str_batch);
                    radiobtn2.setChecked(false);
                    radiobtn1.setChecked(false);
                    radiobtn4.setChecked(false);
                    IISERApp.set_session(IISERApp.SESSION_BATCH, str_batch);
                }
            }
        });
        radiobtn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radiobtn4.isChecked()) {
                    str_batch = radiobtn4.getText().toString();
                    IISERApp.log(LOG_TAG, "str_batch: " + str_batch);
                    radiobtn2.setChecked(false);
                    radiobtn3.setChecked(false);
                    radiobtn1.setChecked(false);
                    IISERApp.set_session(IISERApp.SESSION_BATCH, str_batch);
                }
            }
        });
       /*btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((IISERApp) getApplication()).isInternetAvailable()) {
                    Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
                    prgresbar.setVisibility(View.VISIBLE);
                    context.startService(intent1);
                } else {
                    Snackbar.make(btn_update, "No Internet available...", Snackbar.LENGTH_LONG).show();
                }
            }

        });*/

        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    edtxt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    edtxt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });




        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // Snackbar.make(btn_update, "Comming Soon...", Snackbar.LENGTH_LONG).show();

                IISERApp.set_session(IISERApp.SESSION_STUDENT_NAME, str_name);
                IISERApp.log(LOG_TAG,"value of batch"+IISERApp.get_session(IISERApp.SESSION_BATCH));

                if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {

                    prgresbar.setVisibility(View.VISIBLE);

                    get_student_data();
                    if (((IISERApp) getApplication()).isInternetAvailable()) {
                        Context cn = getApplicationContext();
                        IISERApp.log(LOG_TAG, "context:" + ActivityProfile.this);
                        try {
                            dialog = ProgressDialog.show(getParent(), null, null);
                            dialog.show();
                        } catch (Exception e) {
                            IISERApp.log(LOG_TAG, "exception:" + e.getMessage());
                        }
                        Bundle extras = new Bundle();
                        IISERApp.log(LOG_TAG, "Data is:" + student_data);
                        Intent intent1 = new Intent(context, IISERIntentService.class);
                        extras = set_bundle(extras);
                        intent1.putExtras(extras);
                      //  dialog.dismiss();
                        prgresbar.setVisibility(View.GONE);

                        Snackbar.make(btn_update, "Profile Updated Successfully ...", Snackbar.LENGTH_SHORT).show();


                        // Toast.makeText(ActivityProfile.this, "Calling webservice", Toast.LENGTH_SHORT).show();
                        context.startService(intent1);
                       // service_timetable();

                            //   prgress_dialog = ProgressDialog.show(context, null, null);

                            Intent intent2 = new Intent(context, IISERIntentService.class);
                            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_TIMETABLE);
                            intent2.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
                            intent2.putExtra("Activity_name", "ActivityProfile");
                            context.startService(intent2);

                    } else {
                        Snackbar.make(btn_update, "No Internet available...", Snackbar.LENGTH_LONG).show();
                    }
                } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {

                    get_faculty_data();
                    if (((IISERApp) getApplication()).isInternetAvailable()) {
                        Bundle extras = new Bundle();
                        Intent intent1 = new Intent(context, IISERIntentService.class);
                        extras = set_bundle_faculty(extras);
                        intent1.putExtras(extras);
                        Snackbar.make(btn_update, "Profile Updated Successfully ...", Snackbar.LENGTH_SHORT).show();
                        //prgresbar.setVisibility(View.VISIBLE);
                        context.startService(intent1);
                    } else {
                        Snackbar.make(btn_update, "No Internet available...", Snackbar.LENGTH_LONG).show();
                    }
                } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {

                    get_supervisor_data();
                    if (((IISERApp) getApplication()).isInternetAvailable()) {
                        Bundle extras = new Bundle();
                        Intent intent1 = new Intent(context, IISERIntentService.class);
                        extras = set_bundle_supervisor(extras);
                        intent1.putExtras(extras);
                        Snackbar.make(btn_update, "Profile Updated Successfully ...", Snackbar.LENGTH_SHORT).show();
                        prgresbar.setVisibility(View.GONE);
                        context.startService(intent1);
                    } else {
                        Snackbar.make(btn_update, "No Internet available...", Snackbar.LENGTH_LONG).show();
                    }
                }





            }
        });
    }

    private void service_timetable() {

        Intent intent2 = new Intent(context, IISERIntentService.class);
        IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_TIMETABLE);
        intent2.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
        intent2.putExtra("Activity_name", "ActivityProfile");
        context.startService(intent2);
    }

    private Bundle set_bundle(Bundle extras) {
        extras.putString(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_UPDATE_USER_DATA);
        extras.putString("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE));
        extras.putString("user_id", student_data.get(0).get("user_id").toString());
        extras.putString("name", str_name);
        extras.putString("email_id", str_email_id);
        extras.putString("mobile_no", str_mobile_no);
        extras.putString("username", str_username);
        extras.putString("password", str_password);
        extras.putString("roll_no", TABLE_USER_PROFILE.get_student_roll_no());
        extras.putString("semester_name", str_semester_name);
        extras.putString("batch", str_batch);
        extras.putString("photo_url", student_data.get(0).get("photo_url").toString());
        extras.putString("degree", str_degree);
        extras.putString("designation", str_designation);
        extras.putString("research", str_research);
        extras.putString("personal_page", str_personal_page);
        return extras;
    }

    private Bundle set_bundle_faculty(Bundle extras) {
        extras.putString(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_UPDATE_USER_DATA);
        extras.putString("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE));
        extras.putString("user_id", faculty_data.get(0).get("user_id").toString());
        extras.putString("name", str_name);
        extras.putString("email_id", str_email_id);
        extras.putString("mobile_no", str_mobile_no);
        extras.putString("username", str_username);
        extras.putString("password", str_password);
        extras.putString("roll_no", str_roll_no);
        extras.putString("semester_name", str_semester_name);
        extras.putString("batch", str_batch);
        extras.putString("photo_url", faculty_data.get(0).get("photo_url").toString());
        extras.putString("degree", str_degree);
        extras.putString("designation", str_designation);
        extras.putString("research", str_research);
        extras.putString("personal_page", str_personal_page);
        return extras;
    }

    private Bundle set_bundle_supervisor(Bundle extras) {
        extras.putString(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_UPDATE_USER_DATA);
        extras.putString("user_type", IISERApp.get_session(IISERApp.SESSION_USER_TYPE));
        extras.putString("user_id", supervisor_data.get(0).get("user_id").toString());
        extras.putString("name", str_name);
        extras.putString("email_id", str_email_id);
        extras.putString("mobile_no", str_mobile_no);
        extras.putString("username", str_username);
        extras.putString("password", str_password);
        extras.putString("roll_no", str_roll_no);
        extras.putString("semester_name", str_semester_name);
        extras.putString("batch", str_batch);
        extras.putString("photo_url", supervisor_data.get(0).get("photo_url").toString());
        extras.putString("degree", str_degree);
        extras.putString("designation", str_designation);
        extras.putString("research", str_research);
        extras.putString("personal_page", str_personal_page);
        return extras;
    }

    private void bindComponentData() {
      /*  prgresbar.setIndeterminate(true);
        prgresbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.tab_back_color), android.graphics.PorterDuff.Mode.MULTIPLY);
       */
        prgresbar.setVisibility(View.GONE);
        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
            lnrlayout_roll_no.setVisibility(View.GONE);
            lnrlayout_degree.setVisibility(View.GONE);
            lnrlayout_designation.setVisibility(View.GONE);
            lnrlayout_research.setVisibility(View.GONE);
            student_data = TABLE_USER_PROFILE.get_student_details();
            if (student_data.size() != 0) {
                IISERApp.log(LOG_TAG, "student_data:" + student_data);
                txt_batch_selection.setText("Batch Selection for Semester " + student_data.get(0).get("semester_name").toString());
                set_student_data();
                String sem_name = student_data.get(0).get("semester_name").toString();
            }
            /*if (sem_name.equalsIgnoreCase("V") | sem_name.equalsIgnoreCase("VI") | sem_name.equalsIgnoreCase("VII")
                    | sem_name.equalsIgnoreCase("VIII") | sem_name.equalsIgnoreCase("XI") | sem_name.equalsIgnoreCase("X")) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }*/

        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
            lnrlayout_roll_no.setVisibility(View.GONE);
            lnrlayout_semester_name.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            add_courses.setVisibility(View.GONE);
            lnrlayout_batch_selection.setVisibility(View.GONE);
            faculty_data = TABLE_USER_PROFILE.get_faculty_details();
            IISERApp.log(LOG_TAG, "faculty_data:" + faculty_data);
            set_faculty_data();
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
            add_courses.setVisibility(View.GONE);
            lnrlayout_roll_no.setVisibility(View.GONE);
            lnrlayout_semester_name.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            lnrlayout_batch_selection.setVisibility(View.GONE);
            supervisor_data = TABLE_USER_PROFILE.get_supervisor_details();
            IISERApp.log(LOG_TAG, "faculty_data:" + faculty_data);
            set_supervisor_data();
        }
    }

    public void displayBanner() {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        mDemoSlider = (SliderLayout) findViewById(R.id.sample_output);
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

        for (int i = 0; i < 3; i++) {

            TextSliderView textSliderView = new TextSliderView(context);
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
        context = this;
        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        bindComponentData();

    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            IISERApp.log_bundle(bundle);
            /*if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
               *//* Intent intent1 = new Intent(context, ActivityProfile.class);
                startActivity(intent1);
                finish();
                getIntentData();
                initComponents();
                initComponentsListeners();*//*
                //  displayBanner();
                bindComponentData();

                prgresbar.setVisibility(View.GONE);
                Snackbar.make(btn_update, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();


            } else {
                prgresbar.setVisibility(View.GONE);
                Snackbar.make(btn_update, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }*/

            if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {

                // prgresbar.setVisibility(View.GONE);
                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("1")) {
                    IISERApp.log(LOG_TAG, "in if condition of response stastus 1");
                    //dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    // if (dialog.isShowing())
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        IISERApp.log(LOG_TAG, "in if condition of response stastus 1 if dialog showing..");
                        // Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
                        bindComponentData();
                    }
                } else if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("0")) {
                    if (dialog!= null && dialog.isShowing()) {
                        // dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                        dialog.dismiss();
                    }
                    Snackbar.make(btn_update, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();

                } else {
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    }
                }
            } else {
                // prgresbar.setVisibility(View.GONE);
                if (dialog!= null && dialog.isShowing())
                    dialog.dismiss();
                // Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
