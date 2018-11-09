package com.test.testandroid.util;

/**
 * Created by jyothis on 19/4/17.
 */

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by entangled on 5/2/17.
 */

public class TimeUtili {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String getLocalTimeFromUTC(String date) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 2018-10-02 19:46:52
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date parsed = new Date();
        try {
            parsed = sourceFormat.parse(date); // => Date is in UTC now 2011-03-01 15:10:37
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        destFormat.setTimeZone(tz);
        String result = destFormat.format(parsed);

        return destFormat.format(parsed);
    }

}
