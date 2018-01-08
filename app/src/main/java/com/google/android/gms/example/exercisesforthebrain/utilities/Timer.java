package com.google.android.gms.example.exercisesforthebrain.utilities;

import android.os.SystemClock;
import android.widget.Chronometer;

import java.text.DecimalFormat;

/**
 * Created by jman on 1/09/17.
 */

public class Timer {

    public static String showElapsedTime(Chronometer crono) {
        String twoDigitNum;
        DecimalFormat precision = new DecimalFormat("0.0");
        long elapsedMillis = (SystemClock.elapsedRealtime() - crono.getBase()) / 1000;
        twoDigitNum = precision.format(elapsedMillis);
        return twoDigitNum;
    }
}
