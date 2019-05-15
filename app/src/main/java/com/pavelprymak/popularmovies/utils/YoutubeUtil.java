package com.pavelprymak.popularmovies.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pavelprymak.popularmovies.R;

public class YoutubeUtil {
    public static void watchYoutubeVideo(@NonNull Context context, @NonNull String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        if (appIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(appIntent);
        } else if (webIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(webIntent);
        } else {
            Toast.makeText(context, R.string.youtube_error_message, Toast.LENGTH_LONG).show();
        }
    }
}
