package com.pavelprymak.popularmovies.utils;

import java.util.ArrayList;

public class StringUtils {

    public static String buildStringWithComaSeparator(ArrayList<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i < list.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
