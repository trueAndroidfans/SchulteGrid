package com.aokiji.schultegrid.utils;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String convertDateToString(long date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

}
