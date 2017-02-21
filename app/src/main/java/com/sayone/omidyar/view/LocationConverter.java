package com.sayone.omidyar.view;

import android.location.Location;

/**
 * Created by sayone on 17/2/17.
 */

public class LocationConverter {

    public static String getLatitudeDMS(double latitudeDEC) {
        String latitudeDMS;
        String direction;
        if (latitudeDEC < 0)
            direction = "S";
        else
            direction = "N";
        latitudeDMS = Location.convert(Math.abs(latitudeDEC), Location.FORMAT_SECONDS);
        latitudeDMS = replaceDelimiters(latitudeDMS);
        latitudeDMS = latitudeDMS + direction;
        return latitudeDMS;
    }

    public static String getLongitudeDMS(double longitudeDEC) {
        String longitudeDMS;
        String direction;
        if (longitudeDEC < 0)
            direction = "W";
        else
            direction = "E";
        longitudeDMS = Location.convert(Math.abs(longitudeDEC), Location.FORMAT_SECONDS);
        longitudeDMS = replaceDelimiters(longitudeDMS);
        longitudeDMS = longitudeDMS + direction;
        return longitudeDMS;
    }

    private static String replaceDelimiters(String str) {
        str = str.replaceFirst(":", "°");
        str = str.replaceFirst(":", "'");
        str = str + "\"";
        return str;
    }
}
