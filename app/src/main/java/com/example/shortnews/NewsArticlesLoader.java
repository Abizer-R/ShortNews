package com.example.shortnews;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class NewsArticlesLoader extends AsyncTaskLoader<ArrayList<NewsData>> {
    private String requestUrl;


    public NewsArticlesLoader(@NonNull Context context, String requestUrl) {
        super(context);
        this.requestUrl = requestUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<NewsData> loadInBackground() {
        return  QueryUtils.fetchNewsArticles(requestUrl);
    }
}
