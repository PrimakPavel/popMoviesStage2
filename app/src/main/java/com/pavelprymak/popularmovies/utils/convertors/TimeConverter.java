package com.pavelprymak.popularmovies.utils.convertors;

import java.util.Locale;

public class TimeConverter {
    public static String convertMinToHHMM(int t){
        int hours = t / 60; //since both are ints, you get an int
        int minutes = t % 60;
        return String.format(Locale.ENGLISH,"%d:%02d", hours, minutes);
    }
}
