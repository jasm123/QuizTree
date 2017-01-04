package com.example.user.quiztree.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.quiztree.R;
import com.example.user.quiztree.data.ScoresContract;
import com.example.user.quiztree.utils.CustomChapterAdapter;

public class ChapterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int subject;
    private ListView list;
    private Resources resources;
    private String[] topics;
    private CustomChapterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        if (getIntent().hasExtra("Subject")) {
            subject = getIntent().getIntExtra("Subject", 0);
        }
        list = (ListView) findViewById(R.id.chapters_list);
        getLoaderManager().initLoader(0, null, this);
        resources = getResources();
        topics = resources.getStringArray(R.array.itemName);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setTitle(topics[subject]);

        list.setEmptyView(findViewById(R.id.emptly));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connection_unavailable), Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("Subject", subject);
                    TextView title = (TextView) view.findViewById(R.id.item);
                    switch (title.getText().toString()) {
                        case "Ratio and Proportion":
                        case "Biology":
                            intent.putExtra("Chapter", 0);
                            break;
                        case "Decimals":
                        case "Physics":
                            intent.putExtra("Chapter", 1);
                            break;

                    }
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] mProjection = ScoresContract.Scores.PROJECTION_ALL;
        String[] mSelectionArgs = null;
        String mSelectionClause = ScoresContract.Scores.CHAPTER + " IN (";
        if (subject == 0) {
            mSelectionArgs = getResources().getStringArray(R.array.maths_chapters);
        } else if (subject == 1) {
            mSelectionArgs = getResources().getStringArray(R.array.science_chapters);
        }
        for (int i = 0; i < mSelectionArgs.length; i++) {
            mSelectionClause += "?, ";
        }
        mSelectionClause = mSelectionClause.substring(0, mSelectionClause.length() - 2) + ")";
        CursorLoader loader = new CursorLoader(getApplicationContext(), ScoresContract.Scores.CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter = new CustomChapterAdapter(ChapterActivity.this, data, 0);
        list.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        list.setAdapter(null);
    }
}
