package com.example.shortnews;

import android.content.Context;
import android.util.Log;
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

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<NewsData> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item,
                    parent,
                    false
            );
        }

        NewsData currNewsObject = getItem(position);

        ImageView thumbnail = listItemView.findViewById(R.id.thumbnail);
        String thumbnailUrl = currNewsObject.getThumbnailUrl();
        Glide
                .with(getContext())
                .load(thumbnailUrl)
                .centerCrop()
                .placeholder(R.drawable.thumbnail_placeholder)
                .into(thumbnail);
        // To get rounded corners
        thumbnail.setClipToOutline(true);

        TextView source = listItemView.findViewById(R.id.source);
        source.setText(currNewsObject.getNewsSource());

        TextView title = listItemView.findViewById(R.id.title);
        title.setText(currNewsObject.getNewsTitle());

        String date_time = currNewsObject.getNewsDateTime();
        TextView date = listItemView.findViewById(R.id.date);
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

        //19800 is subtracted because India Standard Time (IST) is 5:30 hours ahead of Coordinated Universal Time (UTC) or Greenwich Mean Time (GMT)
        if((currUnixTime - unixTime - 19800) / 3600 > 24)
            return "" + ((currUnixTime - unixTime - 19800) / (3600*24)) + " days ago";
        else if((currUnixTime - unixTime - 19800) / 60 > 60)
            return "" + ((currUnixTime - unixTime - 19800) / 3600) + " hours ago";
        else if((currUnixTime - unixTime - 19800) > 60)
            return "" + ((currUnixTime - unixTime - 19800) / 60) + " minutes ago";
        else
            return "" + (currUnixTime - unixTime - 19800) + " seconds ago";

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
