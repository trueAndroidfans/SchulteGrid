package com.aokiji.schultegrid.utils;

import android.content.Context;

public class ScreenUtil {

    public static int getScreenWidth(Context context)
    {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight(Context context)
    {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public static float getScreenDensity(Context context)
    {
        return context.getResources().getDisplayMetrics().density;
    }


    public static int dp2px(Context context, int value)
    {
        return (int) (value * getScreenDensity(context) + 0.5f);
    }


    public static int px2dp(Context context, int value)
    {
        return (int) (value / getScreenDensity(context) + 0.5f);
    }

}
