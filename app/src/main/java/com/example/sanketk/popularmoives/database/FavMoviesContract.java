package com.example.sanketk.popularmoives.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sanketk on 12-Aug-18.
 */

public class FavMoviesContract {
    public static final String CONTENT_AUTH = "com.example.sanketk.popularmoives";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTH);

    public static final String PATH_FAV = "fav_movies";


    public static final class FavMoviesEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAV)
                .build();


        public static final String TABLE_NAME = "favMovies";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_RDATE = "release_date";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATINGS = "ratings";




        public static Uri buildFavMoviesUri(String movieId){

            return CONTENT_URI.buildUpon()
                    .appendPath(movieId)
                    .build();
        }


    }

}
