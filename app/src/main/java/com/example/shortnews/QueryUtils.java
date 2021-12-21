package com.example.shortnews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String TAG = QueryUtils.class.getSimpleName();
    public QueryUtils() {
    }


    public static ArrayList<NewsData> fetchNewsArticles(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "fetchNewsArticles: Problem in making the HttpRequest", e);
        }

        ArrayList<NewsData> newsArticles = extractFeaturesFromJson(jsonResponse);
        return newsArticles;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem building the URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) {
            Log.v(TAG, "makeHttpRequest: url is null");
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*Milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*Milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest: " + "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: Problem receiving the JSON results", e);
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(inputStream != null) {
                //Closing inputStream might throw an IOException.
                //Therefore, makeHttpRequest() method signature specifies to throw an IOException
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<NewsData> extractFeaturesFromJson(String jsonResponse) {
        String newsSource;
        String newsTitle;
        String newsUrl;
        String newsDateTime;
        String thumbnailUrl;

        if(TextUtils.isEmpty(jsonResponse)) {
            Log.v(TAG, "The json string is empty or null. Returning early");
            return null;
        }

        ArrayList<NewsData> newsArticles = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(jsonResponse);

            JSONObject responseObject = rootJsonObject.getJSONObject("response");

            JSONArray articlesArray = responseObject.getJSONArray("results");

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject currArticle = articlesArray.getJSONObject(i);
                newsDateTime = currArticle.getString("webPublicationDate");

                JSONObject currFields = currArticle.getJSONObject("fields");
                newsTitle = currFields.optString("headline");
                newsSource = currFields.optString("byline");
                newsUrl = currFields.optString("shortUrl");
                thumbnailUrl = currFields.optString("thumbnail");

                newsArticles.add(new NewsData(newsSource, newsTitle, newsUrl, newsDateTime, thumbnailUrl));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsArticles;
    }

    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
