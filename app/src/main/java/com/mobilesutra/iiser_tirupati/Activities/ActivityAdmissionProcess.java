package com.mobilesutra.iiser_tirupati.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_BSMS_Admission;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_IntegratedPhD_Admission;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_PhD_Admission;
import com.mobilesutra.iiser_tirupati.Model.DTO_Courses_offered;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalyani on 22/04/2016.
 */
public class ActivityAdmissionProcess extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Courses_offered> course_List = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<PagerItem> listTabs = new ArrayList<PagerItem>();
    private SlidingTabLayout objSlidingTabLayout = null;
    ViewPagerAdapter objViewPagerAdapter = null;

    SliderLayout mDemoSlider = null;
    FloatingActionButton fab_refresh=null;
    TextView user_name=null;

    String[] course_array={"BS-MS Programme","Integrated PhD Programme","PhD Program"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_three);

        context=this;
        getIntentData();
        initComponentData();
        initComponentListeners();
        displayBanner();
        bindComponentData();
        populateViewPager();
    }

    private void getIntentData() {
    }

    private void initComponentData() {
        fab_refresh=(FloatingActionButton)findViewById(R.id.fab);
        fab_refresh.setVisibility(View.GONE);

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



       /* recycler_view = (RecyclerView) findViewById(R.id.recycler_event_view);
        course_List = new ArrayList<DTO_Courses_offered>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);*/

        /*viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();*/

    //}

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("BS-MS"+"\n"+"Programme");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabtwo.setText("Integrated" + "\n" + "PhD Programme");
        tabLayout.getTabAt(1).setCustomView(tabtwo);

        TextView tabthree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabthree.setText("PhD" + "\n" + "Programme");
        tabLayout.getTabAt(2).setCustomView(tabthree);
    }

    private void initComponentListeners() {
    }

    private void bindComponentData() {


        /*for (int i = 0; i < course_array.length; i++) {
            DTO_Courses_offered assignment = new DTO_Courses_offered(course_array[i]);
            course_List.add(assignment);
        }
        recyclerAdapter = new EventAdapter(course_List);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();*/


    }

  /*  private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment_BSMS_Course(),"BS-MS Programme");
        adapter.addFrag(new Fragment_IntegratedPhD_course(), "Integrated PhD Programme");
        adapter.addFrag(new Fragment_PhD_Course(), "PhD Program");
        viewPager.setAdapter(adapter);
    }
*/

    void populateViewPager()
    {
        if (objSlidingTabLayout == null) {
            try {
                final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
                // mViewPager.setOffscreenPageLimit(1);
                listTabs.add(new PagerItem("BS-MS Programme",0,0));
                listTabs.add(new PagerItem("Integrated PhD Programme",0,0));
                listTabs.add(new PagerItem("PhD Program",0,0));

                objViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(objViewPagerAdapter);

                objSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
                objSlidingTabLayout.setViewPager(mViewPager);
                objSlidingTabLayout.set_selected_text_color(0);
                objSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        objSlidingTabLayout.set_selected_text_color(position);
                       /* Log.d("Fragment", "onPageSelected->" + position);
                        Fragment fragmentDemo =
                                getSupportFragmentManager().findFragmentById(R.id.viewpager);
                        Log.d("Fragment", "fragmentDemo->" + fragmentDemo);
                        RVFApp.set_session(RVFApp.SESSION_CURRENT_TAB, position + "");
                        if(position == 0) {
                            FragmentOne first = (FragmentOne) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }
                        else if(position == 1) {
                            FragmentTwo first = (FragmentTwo) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }
                        else if(position == 2) {
                            FragmentThree first = (FragmentThree) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }*/

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // objSlidingTabLayout.set_current_tab(RVFApp.get_Intsession(RVFApp.SESSION_CURRENT_TAB));
                objSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

                    @Override
                    public int getIndicatorColor(int position) {
                        int color = listTabs.get(position).getIndicatorColor();
                        return color;
                    }

                    @Override
                    public int getDividerColor(int position) {
                        int color = listTabs.get(position).getDividerColor();
                        return color;
                    }

                });
            } catch (final Exception ex) {
                Log.d("Fragment", "Exception in thread->" + ex.getMessage());
            }
        }
    }

    static class PagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        PagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        /**
         * @return A new {@link android.support.v4.app.Fragment} to be displayed by a {@link ViewPager}
         */
        android.support.v4.app.Fragment createFragment() {
            Log.d("Fragment", "In createFragment");
            if (mTitle.toString().equalsIgnoreCase("BS-MS Programme"))
                return Fragment_BSMS_Admission.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("Integrated PhD Programme"))
                return Fragment_IntegratedPhD_Admission.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("PhD Program"))
                return Fragment_PhD_Admission.newInstance(mTitle.toString());
            else
                return null;
        }


        CharSequence getTitle() {
            return mTitle;
        }
        int getIndicatorColor() {
            return mIndicatorColor;
        }
        int getDividerColor() {
            return mDividerColor;
        }


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            return listTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return listTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTabs.get(position).getTitle();
        }

    }


    /* class ViewPagerAdapter extends FragmentPagerAdapter {
         private final List<Fragment> mFragmentList = new ArrayList<>();
         private final List<String> mFragmentTitleList = new ArrayList<>();

         public ViewPagerAdapter(FragmentManager manager) {
             super(manager);
         }

         @Override
         public Fragment getItem(int position) {
             return mFragmentList.get(position);
         }

         @Override
         public int getCount() {
             return mFragmentList.size();
         }

         public void addFrag(Fragment fragment, String title) {
             mFragmentList.add(fragment);
             mFragmentTitleList.add(title);
             Log.d("Fragment", "FragmentTitleList:" + mFragmentTitleList);
         }

         @Override
         public CharSequence getPageTitle(int position) {

             // return null to display only the icon
             return mFragmentTitleList.get(position);
         }
     }*/
    public void displayBanner() {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


        mDemoSlider = (SliderLayout)findViewById(R.id.sample_output);
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

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>
    {

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
        public MyViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {
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

                    Log.d("Fragment","position of view:"+position);
                    Intent intent = new Intent(context, ActivityCourseDetails.class);
                    intent.putExtra("position",String.valueOf(position));
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

