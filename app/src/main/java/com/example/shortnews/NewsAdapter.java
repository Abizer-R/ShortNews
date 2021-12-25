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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsData> {
    private final int MAINACTIVITY_ID = 1;
    private final int SEARCHACTIVITY_ID = 2;
    int contextID;
    int layoutResourceId;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<NewsData> objects, int layoutResourceId, int contextID) {
        super(context, 0, objects);
        this.contextID = contextID;
        this.layoutResourceId = layoutResourceId;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    layoutResourceId,
                    parent,
                    false
            );
        }

        NewsData currNewsObject = getItem(position);
        ImageView thumbnail;
        TextView source;
        TextView title;
        TextView date;


        if(contextID == MAINACTIVITY_ID) {
            thumbnail = listItemView.findViewById(R.id.thumbnail_homepage);
            source = listItemView.findViewById(R.id.source_homepage);
            title = listItemView.findViewById(R.id.title_homepage);
            date = listItemView.findViewById(R.id.date_homepage);
        } else {
            thumbnail = listItemView.findViewById(R.id.thumbnail_searchpage);
            source = listItemView.findViewById(R.id.source_searchpage);
            title = listItemView.findViewById(R.id.title_searchpage);
            date = listItemView.findViewById(R.id.date_searchpage);
        }

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

        source.setText(currNewsObject.getNewsSource());
        title.setText(currNewsObject.getNewsTitle());

        // Converting date into "time elapsed since article posted"
        String date_time = currNewsObject.getNewsDateTime();
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
        if(secondsElapsedAfterPublishing / 3600 > 24 && secondsElapsedAfterPublishing / 3600 < 48)
            return "" + (secondsElapsedAfterPublishing / (3600*24)) + " day ago";
        else if(secondsElapsedAfterPublishing / 3600 >= 48)
            return "" + (secondsElapsedAfterPublishing / (3600*24)) + " days ago";
        else if(secondsElapsedAfterPublishing / 60 > 60 && secondsElapsedAfterPublishing / 60 < 120)
            return "" + (secondsElapsedAfterPublishing / 3600) + " hour ago";
        else if(secondsElapsedAfterPublishing / 60 >= 120)
            return "" + (secondsElapsedAfterPublishing / 3600) + " hours ago";
        else if(secondsElapsedAfterPublishing > 60 && secondsElapsedAfterPublishing < 120)
            return "" + (secondsElapsedAfterPublishing / 60) + " minute ago";
        else if(secondsElapsedAfterPublishing >= 120)
            return "" + (secondsElapsedAfterPublishing / 60) + " minutes ago";
        else
            return "" + secondsElapsedAfterPublishing + " seconds ago";


    }
}
