package com.mobilesutra.iiser_tirupati.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

public class FragmentOne extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    SliderLayout mDemoSlider=null;
    Fragment frg=null;

    public FragmentOne() {
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
       // return inflater.inflate(R.layout.fragment_one, container, false);
        final View view = inflater.inflate(R.layout.fragment_one, container, false);
        displayBanner(view);

        initComponents(view);
        initComponentsListeners();
        bindComponentData();

        return view;
    }

    private void initComponents(View view) {
    }

    private void initComponentsListeners() {
    }

    private void bindComponentData() {
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
/*  //  frg = getSupportFragmentManager().findFragmentByTag("FragmentOne");
    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.detach(frg);
    ft.attach(frg);
    ft.commit();*/




  /*  Fragment Fragment0nee = getFragmentManager().findFragmentByTag("FragmentOne");
    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    fragmentTransaction.detach(FragmentOne).
    fragmentTransaction.attach(FragmentOne).
    fragmentTransaction.commit();
*/
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
}