package com.example.shortnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class UrlConstructor {
    private static final String TAG = UrlConstructor.class.getSimpleName();
    private static final String BASE_URL = "https://content.guardianapis.com/search?";
    private static final String EXTRAS = "&show-fields=headline,thumbnail,byline,trailText,shortUrl&show-references=author";
    private static final String API_KEY = "&api-key=f2931c30-4d53-4790-8140-e3b507f5556a";
// https://content.guardianapis.com/search?page-size=5&show-fields=headline,thumbnail,byline,trailText,shortUrl&show-references=author
    public UrlConstructor() {
    }

    public static String constructUrl(String pageSizePref, String orderByPref, String datePref) {
        StringBuilder urlBuilder = new StringBuilder();

        String pageSize = "page-size=" + pageSizePref;
        String orderBy = "&order-by=" + orderByPref;
        String dateRange = "&from-date=" + getDateRange(datePref);

        urlBuilder.append(BASE_URL);
        urlBuilder.append(pageSize);
        urlBuilder.append(orderBy);
        urlBuilder.append(dateRange);
        urlBuilder.append(EXTRAS);
        urlBuilder.append(API_KEY);

        return urlBuilder.toString();
    }

    private static String getDateRange(String datePref) {
        long currUTCTime = Calendar.getInstance().getTimeInMillis() - 19800000;
        Log.e(TAG, "getDateRange: currUTCTime = " + currUTCTime);
        long startingDateUnixTime = currUTCTime - (Integer.parseInt(datePref) * 1000L);
        Log.e(TAG, "getDateRange: startingDate = " + startingDateUnixTime);

        Date startingDate = new Date(startingDateUnixTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(startingDate);
    }


}
