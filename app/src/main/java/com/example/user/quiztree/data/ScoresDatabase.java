package com.example.user.quiztree.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.user.quiztree.data.MyContentProvider.Tables;
public class ScoresDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiztree_scores.db";
    private static final int DATABASE_VERSION = 1;

    public ScoresDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +Tables.SCORES + " ("
                + ScoresContract.ScoreColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ScoresContract.ScoreColumns.CHAPTER + " TEXT NOT NULL ,"
                + ScoresContract.ScoreColumns.SCORE + " INTEGER NOT NULL DEFAULT 0," +
                "UNIQUE("+ScoresContract.ScoreColumns.CHAPTER+"))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +Tables.SCORES);
        onCreate(db);
    }

}
