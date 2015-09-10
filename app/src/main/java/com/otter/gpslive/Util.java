package com.otter.gpslive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    /** Return the readable date & time string. */
    public static String convertMillisecondToDateTime(long milliseconds) {
        if (milliseconds < 0) {
            return "Wrong time";
        }

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
        return format.format(new Date(milliseconds));
    }
}
