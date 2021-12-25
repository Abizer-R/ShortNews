package com.example.shortnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

// c0515b0da7f54bcd993091bbd5232e56
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int NEWS_LOADER_ID = 1;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView itemsListView;
    private NewsAdapter newsAdapter;
    private TextView emptyStateView;
    private CircularProgressIndicator progressIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsListView = findViewById(R.id.itemsListView_homepage);

        emptyStateView = findViewById(R.id.empty_state_view);
        itemsListView.setEmptyView(emptyStateView);

        progressIndicator = findViewById(R.id.progress_indicator);

        newsAdapter = new NewsAdapter(this, 0, new ArrayList<NewsData>(), R.layout.listview_item_homepage, 1);
        itemsListView.setAdapter(newsAdapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                NewsData currNewsArticle = newsAdapter.getItem(i);

                Uri newsArticleUri = Uri.parse(currNewsArticle.getNewsUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, newsArticleUri);
                startActivity(browserIntent);
            }
        });

        loadUI();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUI();
            }
        });
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        String pageSize = getPrefStringValue(R.string.pref_pageSize_key, R.string.pref_pageSize_default);
        String orderBy = getPrefStringValue(R.string.pref_orderBy_key, R.string.pref_orderBy_default);
        String dateRange = getPrefStringValue(R.string.pref_dateRange_key, R.string.pref_dateRange_default);

        String requestUrl = UrlConstructor.constructUrl(pageSize, orderBy, dateRange);
        Log.e(TAG, "onCreateLoader: requestURL = " + requestUrl);
        return new NewsArticlesLoader(this, requestUrl);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> newsArticles) {

        if(newsArticles == null || newsArticles.size() == 0) {
            emptyStateView.setText(R.string.empty_state_text);
        }
        else {
            newsAdapter.addAll(newsArticles);
        }
        swipeRefreshLayout.setRefreshing(false);
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        newsAdapter.clear();

    }



    public void loadUI() {

        if(QueryUtils.isNetworkConnected(this)) {
            getSupportLoaderManager().destroyLoader(NEWS_LOADER_ID);
            getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
            emptyStateView.setText(R.string.no_internet);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_view).getActionView();
        ComponentName componentName = new ComponentName(this, SearchableActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        else if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getPrefStringValue(int key, int default_value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.getString(
                getString(key),
                getString(default_value)
        );
    }
}