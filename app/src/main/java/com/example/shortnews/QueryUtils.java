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
//    private static final String jsonResponseHardCoded = "{\"status\":\"ok\",\"totalResults\":38,\"articles\":[{\"source\":{\"id\":null,\"name\":\"The Indian Express\"},\"author\":\"Express News Service\",\"title\":\"Lakhimpur Kheri: SIT says killings a ‘planned conspiracy’ - The Indian Express\",\"description\":\"\uD83D\uDD34 Applies to court to include Section covering acts done with common intention\",\"url\":\"https://indianexpress.com/article/cities/lucknow/lakhimpur-violence-sit-pre-planned-conspiracy-murder-charge-7672383/\",\"urlToImage\":\"https://images.indianexpress.com/2021/12/Lakhimpur-Kheri.jpg\",\"publishedAt\":\"2021-12-14T11:49:31Z\",\"content\":\"After two months of investigation into the Lakhimpur Kheri incident, the UP Polices Special Investigation Team (SIT) has submitted to a court that the killing of four farmers and a journalist, in whi… [+1962 chars]\"},{\"source\":{\"id\":null,\"name\":\"The Weather Channel\"},\"author\":null,\"title\":\"NASA Collaborates With Axiom Space for Its Second Private Astronaut Mission to International Space Station | The Weather Channel - Articles from The Weather Channel | weather.com - The Weather Channel\",\"description\":\"NASA will negotiate with the company on a mission order agreement for the Axiom Mission 2 (Ax-2) targeted to launch between fall 2022 and late spring 2023. - Articles from The Weather Channel | weather.com\",\"url\":\"https://weather.com/en-IN/india/space/news/2021-12-14-nasa-collaborates-with-axiom-space\",\"urlToImage\":\"https://s.w-x.co/in-ISS_1.jpg\",\"publishedAt\":\"2021-12-14T11:17:48Z\",\"content\":\"In June, NASA sought proposals for two new private astronaut missions to the ISSeach of the new missions to last for 14 days.\\r\\nThe US space agency has since selected space habitat company Axiom Space… [+2380 chars]\"},{\"source\":{\"id\":\"google-news\",\"name\":\"Google News\"},\"author\":null,\"title\":\"Omicron Variant And Vaccines: Should India Be Considering COVID-19 Booster Doses? Dr K Srinath Reddy Answe - TheHealthSite\",\"description\":null,\"url\":\"https://news.google.com/__i/rss/rd/articles/CBMinwFodHRwczovL3d3dy50aGVoZWFsdGhzaXRlLmNvbS9uZXdzL29taWNyb24tdmFyaWFudC1hbmQtdmFjY2luZXMtc2hvdWxkLWluZGlhLWJlLWNvbnNpZGVyaW5nLWNvdmlkLTE5LWJvb3N0ZXItZG9zZXMtZHItay1zcmluYXRoLXJlZGR5LWFuc3dlcnMtZXhjbHVzaXZlLTg1MTk2NC_SAQA?oc=5\",\"urlToImage\":null,\"publishedAt\":\"2021-12-14T11:17:00Z\",\"content\":null},{\"source\":{\"id\":null,\"name\":\"NDTV News\"},\"author\":null,\"title\":\"Newlyweds Katrina Kaif And Vicky Kaushal Return To Mumbai. See Pics - NDTV Movies\",\"description\":\"Katrina looked beautiful in a pastel pink salwar-suit while Vicky Kaushal was looking dashing in a beige shirt and pants at the airport\",\"url\":\"https://www.ndtv.com/entertainment/newlyweds-katrina-kaif-and-vicky-kaushal-return-to-mumbai-see-pics-2650707\",\"urlToImage\":\"https://c.ndtvimg.com/2021-12/f3dc2cp8_katrina-kaif-vicky-kaushal_625x300_14_December_21.jpg\",\"publishedAt\":\"2021-12-14T11:14:06Z\",\"content\":\"Katrina and Vicky at the Mumbai airport.\\r\\nHighlights\\r\\n<ul><li>The couple were pictured at the Mumbai airport on Tuesday evening \\r\\n</li><li>They waved at the paparazzi \\r\\n</li><li>The couple got marrie… [+2592 chars]\"},{\"source\":{\"id\":null,\"name\":\"Moneycontrol\"},\"author\":null,\"title\":\"Pre-booking RT-PCR tests mandatory for passengers arriving from 'at-risk' countries to 6 six metro cities - Moneycontrol\",\"description\":\"The Aviation ministry, however, clarified that the passengers will be allowed to board the flights even if they fail to pre-book the RT-PCR tests. In such cases, it would be the responsibility of the airlines to identify and accompany such passengers to the r…\",\"url\":\"https://www.moneycontrol.com/news/trends/travel-trends/pre-booking-rt-pcr-tests-mandatory-for-international-passengers-arriving-at-6-airports-including-delhi-mumbai-7825121.html\",\"urlToImage\":\"https://images.moneycontrol.com/static-mcnews/2017/11/AP11_23_2017_000015B-770x433.jpg\",\"publishedAt\":\"2021-12-14T11:04:01Z\",\"content\":\"Pre-booking of RT-PCR tests has been made mandatory for international passengers arriving from 'at-risk' countries to six metro cities including Mumbai and Delhi, the Ministry of Civil Aviation (MoCA… [+2065 chars]\"},{\"source\":{\"id\":null,\"name\":\"NDTV News\"},\"author\":\"Reported by Shashank Singh\",\"title\":\"Virat Kohli To Skip South Africa ODIs, Rohit Sharma Will Recover: Sources - NDTV Sports\",\"description\":\"Virat Kohli is set to opt of the ODI series in South Africa while Rohit Sharma will recover from his hamstring in injury in time for the three ODIs, sources told NDTV.\",\"url\":\"https://sports.ndtv.com/south-africa-vs-india-2021-22/virat-kohli-to-opt-out-of-south-africa-odis-rohit-sharma-will-recover-sources-2649887\",\"urlToImage\":\"https://c.ndtvimg.com/2021-04/48e9nhng_virat-kohli-rohit-sharma-bcci_650x400_15_April_21.jpg?im=FaceCrop,algorithm=dnn,width=1200,height=675\",\"publishedAt\":\"2021-12-14T10:41:00Z\",\"content\":\"Virat Kohli is set to miss the One-Day International (ODI) series in South Africa while Rohit Sharma is expected to recover from his hamstring injury in time for the three ODIs, sources told NDTV on … [+2016 chars]\"},{\"source\":{\"id\":null,\"name\":\"Livemint\"},\"author\":\"Reuters\",\"title\":\"No Omicron deaths in J&J vaccine study in South Africa, scientist says - Mint\",\"description\":\"'As of today we have had no one who has died from Omicron from the J&J study, so that's the good news, it shows again that the vaccine is effective against severe disease and death,' South African Medical Research Council president Glenda Gray said\",\"url\":\"https://www.livemint.com/news/world/no-omicron-deaths-in-j-j-vaccine-study-in-south-africa-scientist-says-11639477946333.html\",\"urlToImage\":\"https://images.livemint.com/img/2021/12/14/600x338/Omicron_1639478040151_1639478040320.jpg\",\"publishedAt\":\"2021-12-14T10:37:20Z\",\"content\":null},{\"source\":{\"id\":null,\"name\":\"Greater Kashmir\"},\"author\":\"Sumit Bhargav\",\"title\":\"Pak militant Abu Zarrara killed in Poonch gunfight: Army - Greater Kashmir\",\"description\":null,\"url\":\"https://www.greaterkashmir.com/pir-panjal/pak-militant-abu-zarrara-killed-in-poonch-gunfight-army\",\"urlToImage\":\"https://gumlet.assettype.com/greaterkashmir%2F2021-12%2F1822df90-a1a0-4785-a019-454281537d3e%2FPicsArt_12_14_03_41_06.jpg?rect=0%2C0%2C3464%2C1819&w=1200&auto=format%2Ccompress&ogImage=true\",\"publishedAt\":\"2021-12-14T10:14:22Z\",\"content\":\"An Army spokesman while confirming Abi Zar's killing in the encounter said that one AK assault rifle, four magazines, hand grenade \\\"and other incriminating material\\\" has been recovered in the ongoing… [+153 chars]\"},{\"source\":{\"id\":null,\"name\":\"Moneycontrol\"},\"author\":null,\"title\":\"RBI issues prompt corrective action framework for NBFCs - Moneycontrol\",\"description\":\"The PCA framework for NBFCs will come into effect from October 1, 2022, based on the financial position of NBFCs on or after March 31, 2022, the RBI said.\",\"url\":\"https://www.moneycontrol.com/news/business/rbi-issues-prompt-corrective-action-framework-for-nbfcs-7825091.html\",\"urlToImage\":\"https://images.moneycontrol.com/static-mcnews/2021/01/Reserve-Bank-770x433.jpg\",\"publishedAt\":\"2021-12-14T10:09:58Z\",\"content\":\"The Reserve Bank of India (RBI) on December 14 issued prompt corrective action (PCA) framework for non-banking finance companies (NBFCs) by introducing three risk threshold categories.\\r\\nPCA refers to… [+3262 chars]\"},{\"source\":{\"id\":null,\"name\":\"NDTV News\"},\"author\":null,\"title\":\"\\\"Yogi Adityanath Didn't Take Dip In Ganga Because...\\\": Akhilesh Yadav - NDTV\",\"description\":\"Former Uttar Pradesh Chief Minister Akhilesh Yadav -- targeted by his successor Yogi Adiyanath over the \\\"filth\\\" and \\\"congestion\\\" in Varanasi yesterday -- has responded with a barb as sharp.\",\"url\":\"https://www.ndtv.com/india-news/yogi-adityanath-didnt-take-dip-in-ganga-because-akhilesh-yadav-2650522\",\"urlToImage\":\"https://c.ndtvimg.com/2021-12/f6poq10k_akhilesh-yadav-_625x300_14_December_21.jpg\",\"publishedAt\":\"2021-12-14T10:08:00Z\",\"content\":\"Yogi Adityanath is aware that none of the rivers are clean, Akhilesh Yadav said.\\r\\nLucknow: Former Uttar Pradesh Chief Minister Akhilesh Yadav -- targeted by his successor Yogi Adiyanath over the \\\"fil… [+1619 chars]\"}]}";

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
                newsTitle = currFields.getString("headline");
                newsSource = currFields.getString("byline");
                newsUrl = currFields.getString("shortUrl");
                thumbnailUrl = currFields.getString("thumbnail");

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
