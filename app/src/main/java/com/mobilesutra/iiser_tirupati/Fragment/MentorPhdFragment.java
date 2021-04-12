package com.mobilesutra.iiser_tirupati.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilesutra.iiser_tirupati.Activities.ActivityAttendenceFaculty;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Model.DTO_Courses_offered;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class MentorPhdFragment extends Fragment {
    View view = null;
    Context context = null;
    private GoogleApiClient client;

    public List<DTO_Courses_offered> course_List = new ArrayList<>();
    // ActivityAssignment.EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    FragmentFacultyAttendence first = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab_refresh = null, fab_add_assingment = null;
    private List<PagerItem> listTabs = new ArrayList<PagerItem>();
    private SlidingTabLayout objSlidingTabLayout = null;
    ViewPager mViewPager = null;
    TextView add_process = null;
    TextView user_name = null;

    List<LinkedHashMap<String, String>> assign_list = new ArrayList<>();
    SliderLayout mDemoSlider = null;
    String LOG_TAG = "ActivityAttendenceFaculty";
    private static final String ARG_PARAM_TITLE = "title";
    private ViewPagerAdapter objViewPagerAdapter;

    public MentorPhdFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MentorPhdFragment newInstance(String str_title) {
        MentorPhdFragment fragment = new MentorPhdFragment();
        Log.d("Fragment", "In newInstance");
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_five, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mentor_phd, container, false);

          /*  Bundle args = getArguments();
            IISERApp.log("", "tab title next:" + args.getCharSequence(ARG_PARAM_TITLE).toString());
            tab_title = args.getCharSequence(ARG_PARAM_TITLE).toString();
            LOG_TAG = LOG_TAG + tab_title;*/
            getIntentData();
            populateViewPager(view);
        //    initComponentData(view);
          //  initComponentListeners();
          //  displayBanner(view);

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(getContext()).addApi(AppIndex.API).build();
        }
        return view;

    }

    private void getIntentData() {
    }

    private void initComponentData(View view) {


    }

    void populateViewPager(View view)
    {
        //  if (objSlidingTabLayout == null) {
        try {
            mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

//mViewPager.setOnPageChangeListener(this);


            LinkedHashMap<String, String> lhm=null;
            IISERApp.log(LOG_TAG,"in else if of session ");
            lhm = TABLE_ATTENDENCE_MASTER.get_Faculty_stud_courseCode();


            int lhmLength = lhm.size();

            IISERApp.log(LOG_TAG, "Course_lhm:" + lhm);


            if (lhmLength > 0) {

                Set<String> keys = lhm.keySet();
                for (String key : keys) {
                    IISERApp.log(LOG_TAG, "key:" + key);
                    listTabs.add(new PagerItem(
                            key, // Title
                            0, // Indicator color
                            0 // Divider color
                    ));
                }

            }


            objViewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
            mViewPager.setAdapter(objViewPagerAdapter);

            objSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.tabs);
            objSlidingTabLayout.setViewPager(mViewPager);
            objSlidingTabLayout.set_selected_text_color(0);
            int item = mViewPager.getCurrentItem(); // get

          //  IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, String.valueOf(listTabs.get(item).getTitle()));

            objSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Log.d("inonPageScrolled","");

                }

                @Override
                public void onPageSelected(int position) {
                    Log.d("onPageSelected", "");

                    CharSequence title = listTabs.get(position).getTitle();
                    Log.d("onPageSelected ", String.valueOf(title));
                  //  IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, String.valueOf(title));
                  //  String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
                 //   Log.d("tabtitleinfrag ", tabTitel);
                    objSlidingTabLayout.set_selected_text_color(position);




                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    Log.d("onPageScrollStateChan","");
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


     class PagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        PagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        /**
         * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
         */

        Fragment createFragment() {
            Log.d("Fragment", "In createFragment");
            /*Fragment fragment;
            if(mTitle.toString().equals("MENTOR PhD"))
            {
               fragment = MentorPhdFragment.newInstance(mTitle.toString());
            }else {
                fragment = FragmentFacultyAttendence.newInstance(mTitle.toString());
            }
            return fragment;*/

            return Fragment_Attendence.newInstance(mTitle.toString());


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
        public Fragment getItem(int i) {
            return listTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return listTabs.size();
            //IISERApp.log(LOG_TAG"Count of tabs is "+listTabs.getSize());
            //if(listTabs.size()==0)
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return listTabs.get(position).getTitle();
        }

    }


}
