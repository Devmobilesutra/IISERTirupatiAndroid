package com.mobilesutra.iiser_tirupati.banner.Transformers;

/**
 * Created by daimajia on 14-5-29.
 */
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class AccordionTransformer extends com.mobilesutra.iiser_tirupati.banner.Transformers.BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setPivotX(view,position < 0 ? 0 : view.getWidth());
        ViewHelper.setScaleX(view,position < 0 ? 1f + position : 1f - position);
    }

}