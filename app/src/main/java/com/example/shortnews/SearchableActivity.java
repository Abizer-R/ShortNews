package com.example.shortnews;

import android.app.ActionBar;
import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    private final static String TAG = SearchableActivity.class.getSimpleName();
    private final static int SEARCH_NEWS_LOADER_ID = 2;

    private int count = 0;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView itemsListView;
    private NewsAdapter newsAdapter;
    private TextView emptyStateView;
    private CircularProgressIndicator progressIndicator;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        count++;
        Log.e(TAG, "handleIntent: COUNT = " + count);

        invalidateOptionsMenu();

        // ----------------------
        itemsListView = findViewById(R.id.itemsListView_searchpage);

        emptyStateView = findViewById(R.id.empty_state_view_searchpage);
        itemsListView.setEmptyView(emptyStateView);

        progressIndicator = findViewById(R.id.progress_indicator);

        newsAdapter = new NewsAdapter(this, 0, new ArrayList<NewsData>());
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

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_searchpage);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUI();
            }
        });
        // ----------------------

        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            loadUI();
        }
    }



    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        String pageSize = getPrefStringValue(R.string.pref_pageSize_key, R.string.pref_pageSize_default);
        String orderBy = getPrefStringValue(R.string.pref_orderBy_key, R.string.pref_orderBy_default);
        String dateRange = getPrefStringValue(R.string.pref_dateRange_key, R.string.pref_dateRange_default);

        String requestUrl = UrlConstructor.constructUrl(pageSize, orderBy, dateRange, query);
        Log.e(TAG, "onCreateLoader: requestURL = " + requestUrl);
        return new NewsArticlesLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> newsArticles) {

        if(newsArticles == null || newsArticles.size() == 0) {
            emptyStateView.setText(R.string.empty_state_text);
        } else {
            newsAdapter.addAll(newsArticles);
        }
        swipeRefreshLayout.setRefreshing(false);
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        menuItem.setVisible(false);


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
        return super.onOptionsItemSelected(item);
    }


    public void loadUI() {
        getSupportActionBar().setTitle("\"" + query + "\"");
        if(QueryUtils.isNetworkConnected(this)) {
            getSupportLoaderManager().destroyLoader(SEARCH_NEWS_LOADER_ID);
            getSupportLoaderManager().restartLoader(SEARCH_NEWS_LOADER_ID, null, this);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
            emptyStateView.setText(R.string.no_internet);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private String getPrefStringValue(int key, int default_value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.getString(
                getString(key),
                getString(default_value)
        );
    }
}