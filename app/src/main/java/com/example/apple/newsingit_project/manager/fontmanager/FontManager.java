package com.example.apple.newsingit_project.manager.fontmanager;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Tacademy on 2016-09-20.
 */
public class FontManager {

    Context context;
    Typeface typefaceBold, typefaceRegular, typefaceMedium;

    public FontManager(Context context) {
        this.context = context;
    }

    public Typeface getTypefaceBoldInstance() {
        if (typefaceBold == null) {
            typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansKR-Bold-Hestia.otf");
        }
        return typefaceBold;
    }

    public Typeface getTypefaceRegularInstance() {
        if (typefaceRegular == null) {
            typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansKR-Regular-Hestia.otf");
        }
        return typefaceRegular;
    }

    public Typeface getTypefaceMediumInstance() {
        if (typefaceMedium == null) {
            typefaceMedium = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansKR-Medium-Hestia.otf");
        }
        return typefaceMedium;
    }

}
