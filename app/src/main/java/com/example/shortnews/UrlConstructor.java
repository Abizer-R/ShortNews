package com.example.shortnews;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public final class UrlConstructor {
    private static final String BASE_URL = "https://content.guardianapis.com/search?";
    private static final String EXTRAS = "&show-fields=headline,thumbnail,byline,trailText,shortUrl&show-references=author";
    private static final String API_KEY = "&api-key=f2931c30-4d53-4790-8140-e3b507f5556a";
// https://content.guardianapis.com/search?page-size=5&show-fields=headline,thumbnail,byline,trailText,shortUrl&show-references=author
    public UrlConstructor() {
    }

    public static String constructUrl(String pageSizePref) {
        StringBuilder urlBuilder = new StringBuilder();

        String pageSize = "page-size=" + pageSizePref;

        urlBuilder.append(BASE_URL);
        urlBuilder.append(pageSize);
        urlBuilder.append(EXTRAS);
        urlBuilder.append(API_KEY);

        return urlBuilder.toString();
    }


}
