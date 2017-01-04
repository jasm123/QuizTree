package com.example.user.quiztree.data;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * The ScoresContract class contains all the information about
 * chapters and scores obtained in them.
 */
public class ScoresContract {
    public static final String CONTENT_AUTHORITY = "com.example.user.quiztree";
    public static final Uri BASE_URI = Uri.parse("content://com.example.user.quiztree");

    interface ScoreColumns {
        String _ID = "_id";
        String CHAPTER = "chapter";
        String SCORE="score";
    }
    public static final class Scores implements ScoreColumns{
        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(
                        ScoresContract.BASE_URI, "scores");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE ="vnd.android.cursor.dir/vnd.com.example.user.quiztree.scores";

        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/vnd.com.example.user.quiztree.scores";
        /**
         * A projection of all columns
         * in the items table.
         */
        public static final String[] PROJECTION_ALL =
                {_ID, CHAPTER, SCORE};


        /** Matches: /scores/ */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("scores").build();
        }




    }

    private ScoresContract(){

    }

}
