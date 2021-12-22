package com.example.shortnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsData> {

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<NewsData> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_homepage,
                    parent,
                    false
            );
        }

        NewsData currNewsObject = getItem(position);

        ImageView thumbnail = listItemView.findViewById(R.id.thumbnail_homepage);
        String thumbnailUrl = currNewsObject.getThumbnailUrl();
        if(thumbnailUrl == null) {
            TextView noThumbnail = listItemView.findViewById(R.id.no_thumbnail_warning_homepage);
            noThumbnail.setText(R.string.no_thumbnail_available);
            thumbnail.setImageResource(R.drawable.thumbnail_placeholder);
        }
        else {
            Glide
                    .with(getContext())
                    .load(thumbnailUrl)
                    .centerCrop()
                    .placeholder(R.drawable.thumbnail_placeholder)
                    .into(thumbnail);
        }
        // To get rounded corners
        thumbnail.setClipToOutline(true);

        TextView source = listItemView.findViewById(R.id.source_homepage);
        source.setText(currNewsObject.getNewsSource());

        TextView title = listItemView.findViewById(R.id.title_homepage);
        title.setText(currNewsObject.getNewsTitle());

        String date_time = currNewsObject.getNewsDateTime();
        TextView date = listItemView.findViewById(R.id.date_homepage);
        date.setText(formatDate(date_time));

        return listItemView;
    }


    private String formatDate(String inputDate) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date outputDate = null;
        try {
            outputDate = inputParser.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = outputDate.getTime() / 1000;
        long currUnixTime = Calendar.getInstance().getTimeInMillis() / 1000;
        long secondsElapsedAfterPublishing = (currUnixTime - unixTime - 19800);

        //19800 is subtracted because India Standard Time (IST) is 5:30 hours ahead of Coordinated Universal Time (UTC) or Greenwich Mean Time (GMT)
        if(secondsElapsedAfterPublishing / 3600 > 24)
            return "" + (secondsElapsedAfterPublishing / (3600*24)) + " days ago";
        else if(secondsElapsedAfterPublishing / 60 > 60)
            return "" + (secondsElapsedAfterPublishing / 3600) + " hours ago";
        else if(secondsElapsedAfterPublishing > 60)
            return "" + (secondsElapsedAfterPublishing / 60) + " minutes ago";
        else
            return "" + secondsElapsedAfterPublishing + " seconds ago";


    }

    private String formatTime(String inputTime) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date outputTime = null;
        try {
            outputTime = inputParser.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final SimpleDateFormat outputFormatter = new SimpleDateFormat("hh:mm a");
        return outputFormatter.format(outputTime);
    }
}
