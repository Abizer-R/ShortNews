package com.example.shortnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

// c0515b0da7f54bcd993091bbd5232e56
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int NEWS_LOADER_ID = 1;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView itemsListView;
    private NewsAdapter newsAdapter;
    private TextView emptyStateView;
    private CircularProgressIndicator progressIndicator;

    private final static String REQUEST_URL = "https://content.guardianapis.com/search?pageSize=10&show-fields=headline,thumbnail,byline,trailText,shortUrl&show-references=author&api-key=f2931c30-4d53-4790-8140-e3b507f5556a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsListView = findViewById(R.id.itemsListView);

        emptyStateView = findViewById(R.id.empty_state_view);
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

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                loadUI();
            }
        });

        loadUI();

    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {

         return new NewsArticlesLoader(this, REQUEST_URL);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> newsArticles) {

        if(newsArticles == null || newsArticles.size() == 0) {
            emptyStateView.setText(R.string.empty_state_text);
        }
        else {
            newsAdapter.addAll(newsArticles);
        }
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {

        newsAdapter.clear();

    }

    public void loadUI() {
        if(QueryUtils.isNetworkConnected(this)) {
            getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
            emptyStateView.setText(R.string.no_internet);
        }
    }


//    public class NewsAsyncTask extends AsyncTask<String, Void, List<NewsData>> {
//
//        @Override
//        protected List<NewsData> doInBackground(String... strings) {
//            List<NewsData> newsArticles = QueryUtils.fetchNewsArticles(strings[0]);
//            return  newsArticles;
//        }
//
//        @Override
//        protected void onPostExecute(List<NewsData> newsArticles) {
//            newsAdapter.addAll(newsArticles);
//        }
//    }
}