package com.mobilesutra.iiser_tirupati.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_FacultyProfileNew;
import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Profile;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by kalyani on 22/04/2016.
 *
 */
public class ActivityFacultyProfile extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private static final String ARG_PARAM_TITLE = "title",
            LOG_TAG = "ActivityFacultyProfile";

    Context context = null;
    LinkedHashMap<String,String> lhm =null;
    RecyclerView recycler_view = null;
    public List<DTO_Faculty_Profile> DTOFacultyProfileList = new ArrayList<>();
   // FacultyProfile_Adapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    SliderLayout mDemoSlider = null;
    TextView add_process=null;
    TextView user_name=null;

    List<LinkedHashMap<String, String>> faculty_data = new ArrayList<>();
    int faculty_list_count = 0;
    ProgressDialog dialog=null;

    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();
    private SlidingTabLayout mSlidingTabLayout;
    SampleFragmentPagerAdapter sampleFragmentPagerAdapter = null;
    ViewPager mViewPager=null;
    FloatingActionButton fab_refresh=null;
    public static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_faculty_profile_new);

        context = this;
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initComponentData();
        //initComponentListeners();
        displayBanner();
        if(!IISERApp.get_session(IISERApp.SESSION_FRAGEMENT_FLAG).equals(""))
            setupViewPager();


        IISERApp.log(LOG_TAG, "SESSION_EVENT_FLAG:" + IISERApp.get_session(IISERApp.SESSION_EVENT_FLAG));
        if (IISERApp.get_session(IISERApp.SESSION_FACULTY_DATA_FLAG).equalsIgnoreCase(""))
        {
            if (((IISERApp) getApplication()).isInternetAvailable())
            {
                Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_FACULTY);
                dialog= ProgressDialog.show(context, "Please wait...", "Fetching data from server");
                context.startService(intent1);
            } else
            {
                Toast.makeText(ActivityFacultyProfile.this, "No Internet available", Toast.LENGTH_LONG).show();
                // Snackbar.make(mSlidingTabLayout, "No Internet available...", Snackbar.LENGTH_LONG).show();
            }
        } else
        {
            //bindComponentData();
        }
    }


    private void initComponentData() {

        add_process=(TextView)findViewById(R.id.add_process);
        add_process.setVisibility(View.GONE);
        fab_refresh=(FloatingActionButton)findViewById(R.id.fab);
//        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
//        DTOFacultyProfileList = new ArrayList<DTO_Faculty_Profile>();
//        linearLayoutManager = new LinearLayoutManager(context);
//        recycler_view.setLayoutManager(linearLayoutManager);
//        recycler_view.setHasFixedSize(true);


        user_name=(TextView)findViewById(R.id.user_name);
        //  IISERApp.log(LOG_TAG,"session value is"+IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG));
        if((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y"))) {

            String user = IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME));
            String sem_name = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME));

            // String sem = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME ));

            //user_name.setText(user);
            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student"))
            {
                user_name.setText(user + " (sem- " + sem_name + ")");
            }
            else  if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
            {
                user_name.setText(user + " (" + "Faculty" +")");
            }
            else  if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor"))
            {
                user_name.setText(user + " (" + "Invigilator" +")");
            }
        }
        else
        {
            user_name.setVisibility(View.GONE);
        }
    }

    private void initComponentListeners() {
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IISERApp.log(LOG_TAG, "button pressed for refreshing.");
                Toast.makeText(ActivityFacultyProfile.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();
                //refresh_data();

            }
        });
    }

    /*private void bindComponentData() {

        recyclerAdapter = new FacultyProfile_Adapter(DTOFacultyProfileList);
        recycler_view.setAdapter(recyclerAdapter);
        // prepareMovieData();

        faculty_data = TABLE_FACULTY_PROFILE.get_faculty_details();
        faculty_list_count = faculty_data.size();
        for (int i = 0; i < faculty_list_count; i++) {

            String faculty_id = faculty_data.get(i).get("user_id").toString();
            String faculty_name = faculty_data.get(i).get("faculty_name").toString();
            String faculty_degree = faculty_data.get(i).get("degree").toString();
            String faculty_designation = faculty_data.get(i).get("designation").toString();
            String faculty_research = faculty_data.get(i).get("research").toString();
            String faculty_email_id = faculty_data.get(i).get("email_id").toString();
            String faculty_mobile_no = faculty_data.get(i).get("mobile_no").toString();
            String faculty_photo_url = faculty_data.get(i).get("photo_url").toString();
            String faculty_personal_page = faculty_data.get(i).get("personal_page").toString();
            String subject = faculty_data.get(i).get("subject").toString();

            DTO_Faculty_Profile DTOFacultyProfile = new DTO_Faculty_Profile(faculty_id, faculty_name, faculty_degree
                    , faculty_designation, faculty_research, faculty_email_id, faculty_mobile_no, faculty_photo_url,
                    faculty_personal_page);
            DTOFacultyProfileList.add(DTOFacultyProfile);

            recyclerAdapter.notifyDataSetChanged();
        }
    }*/

    public void displayBanner() {
        mDemoSlider = (SliderLayout) findViewById(R.id.sample_output);
        mDemoSlider.setFocusable(true);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);

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

   /* public class FacultyProfile_Adapter extends RecyclerView.Adapter<FacultyProfile_Adapter.MyViewHolder> {

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
    }*/

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

        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));

    }


    public final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            IISERApp.log_bundle(bundle);

            IISERApp.log(LOG_TAG, " bundle.getString(BUNDLE_RESPONSE_CODE) :"+bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE));
            IISERApp.log(LOG_TAG, " bundle.getString(BUNDLE_RESPONSE_STATUS) :"+bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS));


            if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {

                // prgresbar.setVisibility(View.GONE);
                if(bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("1"))
                {
                    IISERApp.log(LOG_TAG, "in if condition of response stastus 1");
                    //dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.dismiss();
                        IISERApp.log(LOG_TAG, "in if condition of response stastus 1 if dialog showing..");
                        // Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
//                        bindComponentData();
                        setupViewPager();
                        IISERApp.set_session(IISERApp.SESSION_FRAGEMENT_FLAG,"dfd");
                    }
                }else if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("0"))
                {
                    // dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    if (dialog!= null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(mViewPager, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();

                } else if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("3"))
                {
                    IISERApp.set_session(IISERApp.SESSION_FACULTY_PROFILE, "No");
                    Fragment_FacultyProfileNew first =(Fragment_FacultyProfileNew) sampleFragmentPagerAdapter.instantiateItem(mViewPager, position);
                    first.refresh_data1();
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

    private void setupViewPager() {
        IISERApp.log(LOG_TAG, "In setupViewPager");


        lhm =new    LinkedHashMap<String,String>();
        if(lhm !=null) {
            lhm.put("Biology", "1");
            lhm.put("Chemistry", "2");
            lhm.put("Earth and Climate Science", "3");
            //lhm.put("Humanities and Social Sciences", "4");
            lhm.put("Mathematics", "5");
            lhm.put("Physics", "6");
          //  lhm.put("Administration ", "7");

        }

        Set<String> keys = lhm.keySet();
        for (String key : keys) {

            String value=lhm.get(key);

            mTabs.add(new SamplePagerItem(
                    key, // Title
                    Color.parseColor("#ffffff"), // Indicator color
                    android.R.color.transparent,
                    value// Divider color
            ));
        }
        sampleFragmentPagerAdapter=new SampleFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sampleFragmentPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.set_selected_text_color(0);

        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int pos) {
                IISERApp.log(LOG_TAG, "In onPageSelected:  -->" + pos);
                position = pos;
                mSlidingTabLayout.set_selected_text_color(pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                if (mTabs.size() > position)
                    return mTabs.get(position).getIndicatorColor();
                else
                    return Color.parseColor("#E14025");
            }

            @Override
            public int getDividerColor(int position) {
                if (mTabs.size() > position)
                    return Color.parseColor("#FFFFFF");//mTabs.get(position).getDividerColor();
                else
                    return Color.parseColor("#000000");
            }
        });

    }

    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the {@link android.support.v4.app.Fragment} to be displayed at {@code position}.
         * <p/>
         * Here we return the value returned from {@link SamplePagerItem#createFragment()}.
         */
        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            IISERApp.log(LOG_TAG,"Position is " + i);
            //position = i;
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we return the value returned from {@link SamplePagerItem#getTitle()}.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }
        // END_INCLUDE (pageradapter_getpagetitle)

    }

    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;
        private final CharSequence mValue;
        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor,CharSequence value) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            mValue = value;
        }

        /**
         * @return A new {@link android.support.v4.app.Fragment} to be displayed by a {@link ViewPager}
         */
        android.support.v4.app.Fragment createFragment() {
            return Fragment_FacultyProfileNew.newInstance(mTitle, mIndicatorColor, mDividerColor, mValue);
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {
            return mTitle;
        }

        /**
         * @return the color to be used for indicator on the {@link SlidingTabLayout}
         */
        int getIndicatorColor() {
            return mIndicatorColor;
        }

        /**
         * @return the color to be used for right divider on the {@link SlidingTabLayout}
         */
        int getDividerColor() {
            return mDividerColor;
        }

        CharSequence getValue() {
            return mValue;
        }
    }
    public void refresh_data()
    {
        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG,"button is pressed for refreshing in FACULTY");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_FACULTY);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_FACULTY);
            intent1.putExtra("Activity_name", "ActivityFacultyProfile");
            context.startService(intent1);
        } else {
            ((IISERApp)  getApplicationContext()).getdialog_checkinternet(context);
        }
    }
}

