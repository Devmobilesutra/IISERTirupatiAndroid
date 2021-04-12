package com.mobilesutra.iiser_tirupati.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.FragmentFive;
import com.mobilesutra.iiser_tirupati.Fragment.FragmentFour;
import com.mobilesutra.iiser_tirupati.Fragment.FragmentOne;
import com.mobilesutra.iiser_tirupati.Fragment.FragmentSix;
import com.mobilesutra.iiser_tirupati.Fragment.FragmentTwo;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Assign;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_ExamSchedule;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Notice;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Profile;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_TimeTable;
import com.mobilesutra.iiser_tirupati.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalyani on 12/04/2016.
 */
public class ActivityTabHeaderFooter extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout,tabLayout1;
    private ViewPager viewPager,viewPager_footer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tab_header_footer);

       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

       /* viewPager = (ViewPager) findViewById(R.id.viewpager);*/
        viewPager_footer = (ViewPager) findViewById(R.id.viewpager_footer);
        /*setupViewPager(viewPager);*/
        setupViewPager_Footer(viewPager_footer);



       /* tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();*/


        tabLayout1 = (TabLayout) findViewById(R.id.tabs_footer);
        tabLayout1.setupWithViewPager(viewPager_footer);
        setupTab_footerIcons();


        initcomponantListener();
    }

    private void initcomponantListener() {
       viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.setVisibility(View.VISIBLE);
                viewPager_footer.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager_footer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));
        tabLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager_footer.setCurrentItem(tab.getPosition());

                viewPager_footer.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTab_footerIcons() {
        int[] tabIcons = {
                R.drawable.img_notice,
                R.drawable.img_exam_schedule,
                R.drawable.img_assign,
                R.drawable.img_timetable,
                R.drawable.img_profile
        };

        tabLayout1.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout1.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout1.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout1.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout1.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.img_overview,
                R.drawable.img_faculty_profile,
                R.drawable.img_courses_offered,
                R.drawable.img_admission_process,
                R.drawable.img_events,
                R.drawable.img_home
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setVisibility(View.VISIBLE);
        viewPager_footer.setVisibility(View.GONE);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentOne(), "ONE");
        adapter.addFrag(new FragmentTwo(), "TWO");
       // adapter.addFrag(new FragmentThree(), "THREE");
        adapter.addFrag(new FragmentFour(), "FOUR");
        adapter.addFrag(new FragmentFive(), "FIVE");
        adapter.addFrag(new FragmentSix(), "SIX");
        viewPager.setAdapter(adapter);

    }

    private void setupViewPager_Footer(ViewPager viewPager_footer) {
        viewPager_footer.setVisibility(View.VISIBLE);
    //    viewPager.setVisibility(View.GONE);
        ViewPagerAdapter adapter1 = new ViewPagerAdapter(getSupportFragmentManager());
        adapter1.addFrag(new Fragment_Notice(), "ONE");
        adapter1.addFrag(new Fragment_ExamSchedule(), "TWO");
        adapter1.addFrag(new Fragment_Assign(), "THREE");
        adapter1.addFrag(new Fragment_TimeTable(), "FOUR");
        adapter1.addFrag(new Fragment_Profile(), "FIVE");
        viewPager_footer.setAdapter(adapter1);

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
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
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }
}
